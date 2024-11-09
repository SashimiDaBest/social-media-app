import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
/**
 * Client Handler
 * <p>
 * Handles individual client connections for a social media application, managing input and output streams
 * to facilitate communication between the server and the client.
 * <p>
 * Status: Incomplete
 * <p>
 * This class implements {@link Runnable} to allow handling client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 * </p>
 *
 * @author Soleil Pham
 * @version 11/01/2024
 * @since 1.0
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12);
            SwingUtilities.invokeLater(new ClientHandler(socket));
            // Replace "localhost" with the server's IP address if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Executes the client handling logic. Manages input and output streams for client-server communication.
     * Reads data from the client and sends responses. Ensures resources are properly closed in case of an error.
     * <p>
     * This method runs when the thread is started, allowing the server to handle each client connection
     * in a separate thread.
     * </p>
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            frame = new JFrame("Boiler Gram");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setSize(600, 400);

            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);

            // Create instances of each page
            WelcomePage welcomePage = new WelcomePage(cardLayout, cardPanel);
            CreateUserPage createUserPage = new CreateUserPage(cardLayout, cardPanel);
            FeedViewPage feedViewPage = new FeedViewPage(cardLayout, cardPanel);
            UserProfilePage userProfilePage = new UserProfilePage(cardLayout, cardPanel);
            OtherProfilePage otherProfilePage = new OtherProfilePage(cardLayout, cardPanel);

            cardPanel.add(welcomePage, "welcomePage");
            cardPanel.add(createUserPage, "createUserPage");
            cardPanel.add(feedViewPage, "feedViewPage");
            cardPanel.add(userProfilePage, "userProfilePage");
            cardPanel.add(otherProfilePage, "otherProfilePage");

            frame.add(cardPanel);
            frame.setVisible(true);

            out.write("hello");
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }

}
