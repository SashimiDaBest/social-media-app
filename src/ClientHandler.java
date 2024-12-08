import uiPage.*;

import java.awt.event.*;
import java.net.Socket;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 * ClientHandler
 * Facilitates communication between the client and the server, handling input and
 * output streams and user interface management.
 *
 * Implements {@link Runnable} to handle client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 *
 * @author Soleil Pham
 * @version 11/01/2024
 */
public class ClientHandler implements Runnable {
    private final String hostname;
    private final int port;
    private final Socket socket;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    // GUI components
    private JFrame mainFrame;

    /**
     * Constructs a ClientHandler object to manage the client-server connection.
     *
     * @param hostname The hostname of the server.
     * @param port     The port number for the server.
     * @throws IOException If an I/O error occurs while creating the socket.
     */
    public ClientHandler(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.socket = new Socket(hostname, port);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Main method to start the client handler.
     * Initializes a new ClientHandler instance and starts its thread.
     *
     * @param args Command-line arguments where args[0] is the hostname and args[1] is the port.
     */
    public static void main(String[] args) {
        String hostname = args.length > 0 ? args[0] : "localhost"; // Default to localhost
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 12; // Default port is 12

        try {
            ClientHandler clientHandler = new ClientHandler(hostname, port);
            Thread clientThread = new Thread(clientHandler);
            clientThread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to connect to the server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Entry point for the ClientHandler thread.
     * Initializes the user interface on the Event Dispatch Thread (EDT).
     */
    @Override
    public void run() {
        SwingUtilities.invokeLater(this::initializeUI);
    }

    /**
     * Initializes the user interface components, including setting up the JFrame,
     * defining behavior for the close operation, and loading initial pages.
     */
    private void initializeUI() {
        mainFrame = new JFrame("Boiler Gram");
        mainFrame.setSize(750, 500);
        mainFrame.setLocationRelativeTo(null); // Center the frame
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        try {
            Image appIcon = new ImageIcon("./Sample Test Folder/BoilerGramLogo.png").getImage();
            mainFrame.setIconImage(appIcon);
        } catch (Exception e) {
            System.err.println("Failed to load application icon: " + e.getMessage());
        }

        // Handle window closing
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeResources();
                mainFrame.dispose();
            }
        });

        // Page management
        PageManager pageManager = new PageManager();
        pageManager.addPage("welcome", new WelcomePage(pageManager, writer, reader));
        pageManager.addPage("signup", new CreateUserPage(pageManager, writer, reader));

        mainFrame.setContentPane(pageManager.getCardPanel());
        mainFrame.setVisible(true);

        // Add Ctrl+W shortcut for closing
        KeyStroke ctrlWKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
        mainFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlWKeyStroke, "closeWindow");
        mainFrame.getRootPane().getActionMap().put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the application
            }
        });

        // Show the welcome page
        pageManager.showPage("welcome");
    }

    /**
     * Closes the socket and streams to release resources.
     */
    private void closeResources() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error while closing resources: " + e.getMessage());
        }
    }
}