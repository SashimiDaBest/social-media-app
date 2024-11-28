package uiPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class FeedViewPage extends JPanel {

    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private UserProfilePage userProfilePage;

    public FeedViewPage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.pageManager = pageManager;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JButton profileIconButton = new JButton("Profile icon");
        profileIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, bufferedWriter, bufferedReader));
                pageManager.removePage("feed");
            }
        });
        headerPanel.add(profileIconButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JButton createChatButton = new JButton("Create chat");
        JTextField userSearchField = new JTextField(15);
        searchPanel.add(userSearchField);
        searchPanel.add(createChatButton);

        JPanel chatFeedPanel = new JPanel();
        chatFeedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatFeedPanel.setLayout(new BoxLayout(chatFeedPanel, BoxLayout.X_AXIS));

        JPanel chatSelectionPanel = new JPanel();
        chatSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatSelectionPanel.setLayout(new BoxLayout(chatSelectionPanel, BoxLayout.Y_AXIS));

        JPanel chatViewPanel = new JPanel();
        chatViewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatViewPanel.setLayout(new BoxLayout(chatViewPanel, BoxLayout.X_AXIS));

        chatFeedPanel.add(chatSelectionPanel);
        chatFeedPanel.add(chatViewPanel);



        JPanel navigationPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(chatFeedPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    /*
    public static void feedPage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket) {
        boolean continueFeed = true;
        try {
            while (continueFeed) {
                displayMenu();
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    UserPageClient.write("1", bw);

                    // Display list of users from the server
                    System.out.println("List of users to chat with:");
                    String receivedUserList = br.readLine();
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
                            UserPageClient.write("[DONE]", bw);
                        } else {
                            // Ask the user which user they want to add to their chat
                            System.out.print("Username to add: ");
                            String friendUsername = scanner.nextLine();

                            // Write the username to the server to ensure the user can chat with them
                            UserPageClient.write(friendUsername, bw);
                            String serverValidityResponse = br.readLine();
                            if (serverValidityResponse.isEmpty()) {
                                System.out.println("User selected successfully!");
                                usernames.add(friendUsername);
                            } else if (serverValidityResponse.equals("self")) {
                                System.out.println("You cannot add yourself to a chat!");
                            } else if (serverValidityResponse.equals("That user does not exist!")) {
                                System.out.println("That user does not exist!");
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
                    UserPageClient.write(usernamesToSend, bw);

                    System.out.println("Chat created successfully!");
                } else if (input.equals("2")) {
                    UserPageClient.write("2", bw);

                    // Obtain and display all chats this user is a member of from the server.
                    String userChats = br.readLine();

                    if (!userChats.isEmpty()) {
                        System.out.println("Enter the number (ex. 0001) of the chat you'd like to open!");
                        String[] chatList = userChats.split(";");
                        for (String chat : chatList) {
                            System.out.println(chat);
                        }

                        // Write the selected chat to the server.
                        String chatNumber = scanner.nextLine();
                        UserPageClient.write(chatNumber, bw);

                        // Obtain the server's response -- either the chat is invalid or the selected
                        // chat will be displayed.
                        String serverChatResponse = br.readLine();
                        if (serverChatResponse.equals("Invalid Chat")) {
                            System.out.println("Invalid chat selection!");
                        } else {
                            // Read the chat menu from the server, looping through the menu
                            // until the client requests to stop.
                            boolean viewChat = true;
                            do {
                                String[] chatMenuLines = serverChatResponse.split(";");
                                for (String line : chatMenuLines) {
                                    System.out.println(line);
                                }

                                // Collect and write the client's decision.
                                switch (scanner.nextLine()) {
                                    case "1":
                                        UserPageClient.write("1", bw);

                                        // Compose Message
                                        System.out.println("Enter message to compose:");
                                        String message = scanner.nextLine();
                                        UserPageClient.write(message, bw);

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "2":
                                        UserPageClient.write("2", bw);

                                        // Delete Previous Message
                                        System.out.println("Message deleted!");

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "3":
                                        UserPageClient.write("3", bw);

                                        // Edit Previous Message
                                        System.out.println("Enter replacement message:");
                                        String replacement = scanner.nextLine();
                                        UserPageClient.write(replacement, bw);

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "4":
                                        UserPageClient.write("4", bw);

                                        // End chat loop
                                        viewChat = false;
                                }
                            } while (viewChat);
                        }
                    } else {
                        System.out.println("You have no chats!");
                    }
                } else if (input.equals("3")) {
                    UserPageClient.write("3", bw);
                    UserPageClient.userPage(scanner, br, bw, socket);
                    break;
                } else if (input.equals("4")) {
                    UserPageClient.write("4", bw);

                    // Display list of users from the server
                    System.out.println("Type the username of the user to view! If there are none, type anything to" +
                            " refresh the Feed.");
                    String receivedUserList = br.readLine();
                    String[] userList = receivedUserList.split(";");
                    for (String username : userList) {
                        System.out.println(username);
                    }

                    // Obtain user selection and validate it through server
                    String userSelection = scanner.nextLine();
                    UserPageClient.write(userSelection, bw);

                    // Obtain server validation response
                    String validUser = br.readLine();
                    if (Boolean.parseBoolean(validUser)) {
                        OtherPageClient.otherPage(scanner, userSelection, br, bw, socket);
                        break;
                    } else {
                        System.out.println("Invalid user selection!");
                    }
                } else if (input.equals("5")) {
                    UserPageClient.write("5", bw);
                    try {
                        if (bw != null) {
                            bw.close(); // Close BufferedWriter
                        }
                        if (br != null) {
                            br.close(); // Close BufferedReader
                        }
                        if (socket != null && !socket.isClosed()) {
                            socket.close(); // Close the socket
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continueFeed = false;
                } else {
                    System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while processing the feed page", e);
        }
    }

     */
    /**
     * Displays the main menu for the feed page.
     */
    /*
    private static void displayMenu() {
        System.out.print(
                "Welcome to your Feed! What would you like to do?\n" +
                        "1 - Create a new chat with selected users\n" +
                        "2 - Open an existing chat\n" +
                        "3 - View your profile\n" +
                        "4 - View another user's profile\n" +
                        "5 - Exit\n"
        );
    }
     */
}
