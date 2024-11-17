package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FeedPageClient
 * <p>
 * This class handles the client-side operations for the feed page of the social media application.
 * It provides users with options to create chats, open existing chats, view their profile, view another user's profile,
 * and exit the feed. The class facilitates communication with the server using input and output streams.
 * </p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Create Chat: Users can create a new chat with selected users by finalizing a list of members.</li>
 *     <li>View Chat: Users can open an existing chat, compose messages, delete messages, or edit messages.</li>
 *     <li>View Profiles: Users can view their own profile or navigate to another user's profile.</li>
 *     <li>Exit: Users can exit the feed and close the connection to the server.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The main method, {@code feedPage}, is used to display the feed menu and process user input.
 * Helper methods handle specific actions such as chat creation, chat management, and profile navigation.</p>
 *
 * @author Connor Pugliese
 * @version 1.0
 * @since 11/16/2024
 */
public class FeedPageClient {

    /**
     * Manages the feed page, handling user interactions such as chat creation,
     * viewing chats, managing messages, and navigating to user profiles.
     *
     * @param scanner Scanner for reading user input
     * @param br      BufferedReader for receiving messages from the server
     * @param bw      BufferedWriter for sending messages to the server
     */
    public static void feedPage(Scanner scanner, BufferedReader br, BufferedWriter bw) {
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

                    // Obtain and display all chats this user is a member of from the client.
                    System.out.println("Enter the number (ex. 0001) of the chat you'd like to open!");

                    String userChats = br.readLine();
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
                } else if (input.equals("3")) {
                    UserPageClient.write("3", bw);
                    UserPageClient.userPage(scanner, br, bw);
                    break;
                } else if (input.equals("4")) {
                    UserPageClient.write("4", bw);

                    // Display list of users from the server
                    System.out.println("List of users to view:");
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
                        OtherPageClient.otherPage(scanner, userSelection, br, bw);
                        break;
                    }
                } else if (input.equals("5")) {
                    UserPageClient.write("5", bw);
                    br.close();
                    bw.close();
                    continueFeed = false;
                } else {
                    System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while processing the feed page", e);
        }
    }

    /**
     * Displays the main menu for the feed page.
     */
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

}
