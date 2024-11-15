import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private WelcomePage welcomePage;
    private CreateUserPage createUserPage;
    private FeedViewPage feedViewPage;
    private UserProfilePage userProfilePage;
    private OtherProfilePage otherProfilePage;

    private User user;

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

            setupActionListeners();
            out.write("hello");

        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }

    private void setupActionListeners() {

        welcomePage.getSignInButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = welcomePage.getUsernameField().getText();
                String password = welcomePage.getPasswordField().getText();

                boolean haveLetter = false;
                boolean haveNumber = false;
                for (int i = 0; i < password.length(); i++) {
                    if (Character.isLetter(password.charAt(i))) {
                        haveLetter = true;
                    }
                    if (Character.isDigit(password.charAt(i))) {
                        haveNumber = true;
                    }
                }

                if (!User.hasLogin(username, password) || username == null || !User.userNameValidation(username) || (password == null || password.length() < 10 || (!haveLetter && !haveNumber))) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid username or password \n " +
                            "Password should be 10 characters or more \n " +
                            "Password should contains letters AND numbers \n " +
                            "Password should not have ;", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userID = ""; //replace this with method to find userID based on username
                    user = new User(userID);
                    cardLayout.show(cardPanel, "feedViewPage");
                }
            }
        });

        welcomePage.getNewAccountButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "createUserPage");
            }
        });

        createUserPage.getSignUpButtonButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Welcome to Boiler Gram!", "Welcome Message", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "feedViewPage");
            }
        });
    }

    // to be used by server:
    public User getClientUser() {
        return this.user;
    }

}
