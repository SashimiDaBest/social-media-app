import clientPageOperation.WelcomePageClient;
// import uiPage.*;

import java.net.Socket;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

/**
 * ClientHandler
 * <p>
 * Manages individual client connections for a social media application. This class
 * facilitates communication between the client and the server, handling input and
 * output streams and user interface management.
 * <p>
 *
 * <p>
 * Implements {@link Runnable} to allow handling client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 * </p>
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

    // // UI pages
    // private WelcomePage welcomePage;
    // private CreateUserPage createUserPage;
    // private FeedViewPage feedViewPage;
    // private UserProfilePage userProfilePage;
    // private OtherProfilePage otherProfilePage;

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
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        String hostname = "localhost"; // Server hostname
        int port = 12;              // Port number

        try {
            ClientHandler client = new ClientHandler(hostname, port);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method for the ClientHandler. It initializes the user interface
     * and manages communication with the server using a Scanner for user input.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        WelcomePageClient.welcomePage(scanner, br, bw, socket);


            frame = new JFrame("Boiler Gram");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setSize(600, 400);

            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);

            welcomePage = new WelcomePage(cardLayout, cardPanel);
            createUserPage = new CreateUserPage(cardLayout, cardPanel);
            feedViewPage = new FeedViewPage(cardLayout, cardPanel);
            userProfilePage = new UserProfilePage(cardLayout, cardPanel);
            otherProfilePage = new OtherProfilePage(cardLayout, cardPanel);

            cardPanel.add(welcomePage, "welcomePage");
            cardPanel.add(createUserPage, "createUserPage");
            cardPanel.add(feedViewPage, "feedViewPage");
            cardPanel.add(userProfilePage, "userProfilePage");
            cardPanel.add(otherProfilePage, "otherProfilePage");

            frame.add(cardPanel);
            frame.setVisible(true);

            cardLayout.show(frame.getContentPane(), "welcomePage");
    }
}
