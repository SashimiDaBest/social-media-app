package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FeedPageClient {

    public static void feedPage(Scanner scanner, BufferedReader br, BufferedWriter bw) {
        boolean continueFeed = true;
        try {
            do {
                System.out.print(
                        "Welcome to your Feed! What would you like to do?\n" +
                                "1 - Create a new chat with selected users\n" +
                                "2 - Open an existing chat\n" +
                                "3 - View your profile\n" +
                                "4 - View another user's profile\n" +
                                "5 - Exit\n");
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
            } while (continueFeed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}