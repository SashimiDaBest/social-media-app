import java.awt.event.ActionListener;
import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.event.*;
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
    /**
     * The socket representing the client connection.
     */
    private Socket socket;
    private JFrame frame;

    public static void main(String[] args) {
        try {
            // Create a socket and connect to the server
            Socket socket = new Socket("localhost", 5000);
            SwingUtilities.invokeLater(new ClientHandler(socket));
            // Replace "localhost" with the server's IP address if needed

            // Now you can use the socket to send and receive data

            // Close the socket when done
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes a new ClientHandler with the specified client socket.
     *
     * @param socket the socket representing the client connection
     */
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
            //Do something here
            frame = new JFrame("Multi-Page GUI Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new CardLayout());

            // Create instances of each page
            WelcomePage page1 = new WelcomePage();
            CreateUserPage page21 = new CreateUserPage();
            FeedViewPage page22 = new FeedViewPage();
            UserProfilePage page31 = new UserProfilePage();
            OtherProfilePage page32 = new OtherProfilePage();

            // Add pages to the frame
            frame.add(page1, "page1");
            frame.add(page21, "page21");
            frame.add(page22, "page22");
            frame.add(page31, "page31");
            frame.add(page32, "page32");

            // Show the first page
            CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
            cardLayout.show(frame.getContentPane(), "Page1");

            frame.setVisible(true);

        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }

}
