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
    private String hostname;
    private int port;

    private Socket socket;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private WelcomePage welcomePage;
    private CreateUserPage createUserPage;
    private FeedViewPage feedViewPage;
    private UserProfilePage userProfilePage;
    private OtherProfilePage otherProfilePage;

    private BufferedWriter serverWriter;

    public ClientHandler(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.socket = new Socket(hostname, port);
        this.serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

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

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        welcomePage(scanner);

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
    }

    public void welcomePage(Scanner scanner) {
        try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            boolean isSignedIn = false;

            while (true) {

                // move on once finally signed in 
                if (isSignedIn) {
                    feedPage(scanner);
                    break;
                }

                System.out.print("Welcome to the Welcome Page\n" +
                        "1 - Sign in\n" +
                        "2 - Sign up\n");
                String mainChoice = scanner.nextLine();
                this.write(mainChoice);

                // for Sigining In 
                if (mainChoice.equals("1")) {

                    while (true) {
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        this.write(username);

                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        this.write(password);

                        // Wait for validation from the server
                        String messageFromServer = serverReader.readLine();

                        // successfully signing in 
                        if (messageFromServer.equals("Successful sign-in")) {
                            System.out.println("You have entered the user feed!");
                            isSignedIn = true;
                            break;

                        } else if (messageFromServer.equals("Sign-in was unsuccessful")) {
                            System.out.println("Unsuccesful sign-in, please try again");
                            continue;

                        }
                    }

                // for creating a new account
                } else if (mainChoice.equals("2")) {

                    while(true) {
                        System.out.print("New Username: ");
                        String username = scanner.nextLine();
                        this.write(username);

                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        this.write(password);
                       
                        // Wait on server validation
                        String messageFromServer = serverReader.readLine();

                        if (messageFromServer.equals("User creation successful")) {
                            System.out.println("Successfuly created new account!");
                            isSignedIn = true;
                            break;
                        
                        } else if (messageFromServer.equals("Invalid fields")) {
                            System.out.println("One of the fields is invalid, please try again");
                            continue;
                        }
                    }

                } else {
                    System.out.println("Invalid main input, please try again");
                    continue;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void feedPage(Scanner scanner) {
        try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                System.out.print("""
                        Welcome to your Feed! What would you like to do?
                        1 - Create a new chat with selected users
                        2 - Open an existing chat
                        3 - View your profile
                        4 - View another user's profile
                        """);
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    write("1");

                    // Display list of users from the server
                    System.out.println("List of users to chat with:");
                    String receivedUserList = serverReader.readLine();
                    String[] userList = receivedUserList.split(";");
                    for (String username : userList) {
                        System.out.println(username);
                    }

                    // Prompt user to finalize group creation with selected users
                    boolean makeGroup = false;
                    ArrayList<String> usernames = new ArrayList<>();

                    while (!makeGroup) {
                        System.out.print("Finalize Members (Y/N): ");
                        if (scanner.nextLine().equals("Y")) {
                            if (usernames.isEmpty()) {
                                System.out.print("Can't make group - group is empty!");
                                continue;
                            }
                            makeGroup = true;
                            write("[DONE]");
                        } else {
                            // Ask the user which user they want to add to their chat
                            System.out.print("Username to add: ");
                            String friendUsername = scanner.nextLine();

                            // Write the username to the server to ensure the user can chat with them
                            write(friendUsername);
                            String serverValidityResponse = serverReader.readLine();
                            if (serverValidityResponse.isEmpty()) {
                                System.out.println("User selected successfully!");
                                usernames.add(friendUsername);
                            } else {
                                System.out.println("The user you are trying to chat with has blocked you," +
                                        " you have blocked them, or their account is private.");
                            }
                        }
                    }
                    // Write the finalized list of chat members to the server for processing.
                    // Format: username;username;username...etc
                    String usernamesToSend = "";
                    for (int i = 0; i < usernames.size(); i++) {
                        usernamesToSend += usernames.get(i);
                        if (i != usernames.size() - 1) {
                            usernamesToSend += ";";
                        }
                    }
                    write(usernamesToSend);

                    System.out.println("Chat created successfully!");
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
                write("1");
            } else if (input.equals("2")) {
                write("2");
            } else if (input.equals("3")) {
                write("3");
            } else if (input.equals("4")) {
                write("4");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String blocked = br.readLine();
                    System.out.println(blocked);
                    while (blocked != null) {
                        blocked = br.readLine();
                        System.out.println(blocked);
                    }
                    br.close();
                } catch (Exception e) {
                    System.err.println("Server error: " + e.getMessage());
                }
            } else if (input.equals("5")) {
                feedPage(scanner);
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public boolean write(String outMessage) {
        try {
            serverWriter.write(outMessage);
            serverWriter.newLine();
            serverWriter.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error while writing message: " + e.getMessage());
            return false;
        }
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
