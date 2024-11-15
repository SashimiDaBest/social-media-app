import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.Scanner;
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

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            int page = 0;

            switch (page) {
                case 0:
                    welcomePage(scanner);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }



            /*
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
//            setupActionListeners();
            out.write("hello");
           */
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }

    public void welcomePage(Scanner scanner) {
        System.out.println("Welcome to the Welcome Page\n" +
                "1 - Sign in\n" +
                "2 - Sign up\n");
        String input = scanner.nextLine();
        while (true) {
            if (input.equals("1")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                break;
            } else if (input.equals("2")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                //checkPassword and Username requirement boolean method
                //write user and password to server and initialize user
                feedPage();
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public void feedPage() {

    }






    /*
    private void setupActionListeners() {

        welcomePage.getSignInButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = welcomePage.getUsernameField().getText();
                char[] password = welcomePage.getPasswordField().getPassword();
                String passwordString = new String(password);

                if (username == null || password == null) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userID = ""; //replace this with method to find userID based on username
//                    user = new User(userID);
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
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = welcomePage.getUsernameField().getText();
                char[] password = welcomePage.getPasswordField().getPassword();
                String passwordString = new String(password);

                if (username == null || password == null) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userID = ""; // Replace with method to find userID based on username
//                    user = new User(userID);
                    cardLayout.show(cardPanel, "feedViewPage");
                }
            }

        });
    }
     */
}
