import uiPage.*;

import java.net.Socket;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/**
 * ClientHandler
 *
 * Manages individual client connections for a social media application. This class
 * facilitates communication between the client and the server, handling input and
 * output streams and user interface management.
 *
 * Implements {@link Runnable} to allow handling client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 *
 * @author Soleil Pham
 * @version 11/01/2024
 * @since 1.0
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

    // UI pages
    private WelcomePage welcomePage;
    private CreateUserPage createUserPage;
    private FeedViewPage feedViewPage;

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
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        String hostname = args.length > 0 ? args[0] : "localhost"; // Allow hostname via arguments
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 12; // Default port is 12

        try {
            ClientHandler client = new ClientHandler(hostname, port);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * The run method for the ClientHandler. It initializes the user interface
     * and manages communication with the server.
     */
    @Override
    public void run() {
        try {
            SwingUtilities.invokeLater(this::initializeUI);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
//            TODO: look at how to close resources properly without enabling connection errors
//            closeResources();
        }
    }

    /**
     * Initializes the user interface components.
     */
    private void initializeUI() {
        frame = new JFrame("Boiler Gram");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);

        PageManager pageManager = new PageManager();

        pageManager.addPage("welcome", new WelcomePage(pageManager, bw, br));
        pageManager.addPage("signup", new CreateUserPage(pageManager, bw, br));

        frame.setContentPane(pageManager.getCardPanel());
        frame.setVisible(true);

        pageManager.showPage("welcome");

//        cardLayout = new CardLayout();
//        cardPanel = new JPanel(cardLayout);
//
//        // Initialize and add pages
//        welcomePage = new WelcomePage(cardLayout, cardPanel, bw, br);
//        createUserPage = new CreateUserPage(cardLayout, cardPanel, bw, br);
//        feedViewPage = new FeedViewPage(cardLayout, cardPanel, bw, br);
//
//        cardPanel.add(welcomePage, "welcomePage");
//        cardPanel.add(createUserPage, "createUserPage");
//        cardPanel.add(feedViewPage, "feedViewPage");
//
//        frame.add(cardPanel);
//        frame.setVisible(true);
//
//        // Show the welcome page
//        cardLayout.show(cardPanel, "welcomePage");
    }

    /**
     * Closes the socket and streams to release resources.
     */
    private void closeResources() {
        try {
            if (br != null) br.close();
            if (bw != null) bw.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
