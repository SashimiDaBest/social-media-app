import page.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 * ClientHandler
 * facilitates communication between the client and the server, handling input and
 * output streams and user interface management.
 *
 * Implements {@link Runnable} to handle client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 *
 * @author Soleil Pham
 * @version 11/01/2024
 */
public class ClientHandler implements Runnable {
    private String hostname;
    private int port;
    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;

    // GUI components
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    /**
     * Constructs a ClientHandler object to manage the client-server connection.
     *
     * @param hostname The hostname of the server
     * @param port     The port number for the server
     * @throws IOException If an I/O error occurs while creating the socket
     */
    public ClientHandler(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.socket = new Socket(hostname, port);
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Main method to start the client handler.
     * Initializes a new ClientHandler instance and starts its thread.
     *
     * @param args Command-line arguments where args[0] is the hostname and args[1] is the port.
     */
    public static void main(String[] args) {

        String hostname = args.length > 0 ? args[0] : "localhost"; // Allow hostname via arguments
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 12; // Default port is 12

        try {
            ClientHandler client = new ClientHandler(hostname, port);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to connect to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Entry point for the ClientHandler thread.
     * Invokes the initialization of the user interface on the Event Dispatch Thread (EDT).
     */
    @Override
    public void run() {
        try {
            SwingUtilities.invokeLater(this::initializeUI);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Unexpected error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Initializes the user interface components, including setting up the JFrame,
     * defining behavior for the close operation, and loading initial pages.
     */
    private void initializeUI() {
        frame = new JFrame("Boiler Gram");
        frame.setLocationRelativeTo(null);
        frame.setSize(750, 500);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        try {
            Image appIcon = new ImageIcon("./Sample Test Folder/BoilerGramLogo.png").getImage();
            frame.setIconImage(appIcon);

        } catch (Exception e) {
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        // Add a WindowListener to detect the close button click to dispose resources
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeResources();
                frame.dispose();
            }
        });

        // Initialize and manage pages
        PageManager pageManager = new PageManager();
        pageManager.addPage("welcome", new WelcomePage(pageManager, bw, br));
        pageManager.addPage("signup", new CreateUserPage(pageManager, bw, br));

        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null); // Centers the window
        frame.setContentPane(pageManager.getCardPanel());
        frame.setVisible(true);

        // Show the welcome page
        pageManager.showPage("welcome");
    }

    /**
     * Closes the socket and streams to release resources.
     * Ensures the BufferedWriter, BufferedReader, and socket are properly closed.
     */
    private void closeResources() {
        try {
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while closing resources: " + e.getMessage());
        }
    }
}
