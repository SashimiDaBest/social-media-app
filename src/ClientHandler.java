import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
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
        while (true) {
            System.out.println("Welcome to the Welcome Page\n" +
                    "1 - Sign in\n" +
                    "2 - Sign up\n");
            String input = scanner.nextLine();
            if (input.equals("1")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                //checkPassword and Username requirement boolean method
                //write user and password to server and initialize user
                //read and see if the user can be created
                String messageFromServer = "";
                if (messageFromServer.equals("")) {
                    feedPage(scanner);
                    break;
                }
                //else show error message and do something
            } else if (input.equals("2")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                //checkPassword and Username requirement boolean method
                //write user and password to server and initialize user
                //read and see if the user can be created
                String messageFromServer = "";
                if (messageFromServer.equals("")) {
                    feedPage(scanner);
                    break;
                }
                //else show error message and do something
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public void feedPage(Scanner scanner) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                System.out.print(
                        "Welcome to your Feed! What would you like to do?\n" +
                                "1 - Create a new chat with selected users\n" +
                                "2 - Open an existing chat\n" +
                                "3 - View your profile\n" +
                                "4 - View another user's profile\n");
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    write("1");

                    boolean makeGroup = false;
                    ArrayList<String> usernames = new ArrayList<>();
                    while (!makeGroup) {
                        System.out.print("Add Group (Y/N): ");
                        if (scanner.nextLine().equals("Y")) {
                            if (usernames.isEmpty()) {
                                System.out.print("Can't make group - group is empty!");
                                continue;
                            }
                            makeGroup = true;
                        } else {
                            System.out.print("Friend Username: ");
                            String friendUsername = scanner.nextLine();

                            //check if username is valid - depends whether both account is private or public
                            //write to server
                            // if the user you want to add is private, you have to be following them to msg
                            String messageFromServer = "";
                            if (messageFromServer.equals("")) {
                                System.out.println("Add Username Successfully");
                                usernames.add(friendUsername);
                            } else {
                                System.out.println("Invalid Friend Username");
                            }
                        }
                    }
                    //write to server to prep for creating chat
                    //write a list of verified usernames
                    //create chat on server side
                    System.out.println("Chat created");
                } else if (input.equals("2")) {
                    System.out.print("Chat ID: ");
                    String chatID = scanner.nextLine();
                    //write to server and make sure chat exist
                    //read and print the last 10 messages sent
                } else if (input.equals("4")) {
                    //write to server to get the list
                    //read and print chat id
                } else if (input.equals("3")) {
                    userPage(scanner);
                    break;
                } else {
                    System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void userPage(Scanner scanner) {
        while (true) {
            System.out.println("Welcome to the Feed Page\n" +
                    "1 - Change User Profile\n" +
                    "2 - View Follower\n" +
                    "3 - View Following\n" +
                    "4 - View Blocked\n" +
                    "5 - Go Back to Feed View");
            //Display username and private/public tag
            String input = scanner.nextLine();
            if (input.equals("1")) {

            } else if (input.equals("2")) {

            } else if (input.equals("3")) {

            } else if (input.equals("4")) {

            } else if (input.equals("5")) {
                feedPage(scanner);
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public boolean write(String outMessage) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            bw.write(outMessage);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
        return true;
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
