package serverPageOperation;

import uiPage.Writer;
import exception.InvalidFileFormatException;
import object.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * FeedPageServer
 * <p>
 * This class handles operations related to the feed page of the social media application
 * on the server side. It provides features for chat creation, viewing existing chats,
 * managing messages within chats, and navigating to user profiles.
 * </p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Chat Creation: Allows the user to create a new chat with selected members.</li>
 *     <li>View Existing Chats: Displays the user's chats and allows message management, such as composing,
 *     deleting, and editing messages.</li>
 *     <li>User Profile Navigation: Enables the user to view other user profiles and access their own user page.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The main method in this class is {@code feedPageOperation}, which handles client requests
 * and executes the appropriate actions based on user input.</p>
 *
 * @author Connor Pugliese
 * @version 1.0
 * @since 11/16/2024
 */
public final class FeedPageServer {

    /**
     * Main method for handling feed page operations. Users can create chats,
     * view chats, manage messages, and view profiles. The method communicates
     * with the client and processes user requests.
     *
     * @param br    BufferedReader for reading client input
     * @param bw    BufferedWriter for sending messages to the client
     * @param user  The currently logged-in user
     * @param users List of all users in the system
     * @param chats List of all chats in the system
     */
    public static void feedPageOperation(BufferedReader br,
                                         BufferedWriter bw, User user, ArrayList<User> users, ArrayList<Chat> chats) {
        System.out.println("Feed page operations started");

        while (true) {
            try {
                String input = br.readLine();
                System.out.println("read: " + input);

                if (input == null)
                    continue;

                // 1 - Chat Creation
                if (input.equals("1")) {
                    handleOperationOne(users, user, chats, br, bw);
                // 2 - View Existing Chat
                } else if (input.equals("2")) {
                    handleOperationTwo(users, user, chats, br, bw);
                } else if (input.equals("user")) {
                    users = updateUsers(users);
                    UserPageServer.userPageOperation(br, bw, user, users, chats);
                    break;
                // Send a string representing a list of user separated by semi-colons to client
                } else if (input.equals("4")) {
                    handleOperationFour(users, user, bw);
                // Navigate to Other Page
                } else if (input.equals("6")) {
                    OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                    break;
                }

            } catch (IOException | InvalidFileFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void handleOperationOne(ArrayList<User> users, User user, ArrayList<Chat> chats, BufferedReader br, BufferedWriter bw) throws IOException {
        // Step 1: Handle initial operations related to the user
        handleOperationFour(users, user, bw);

        // Step 2: Validate each user provided by the client
        validateChatUsers(br, bw, user);

        // Step 3: Create a new chat with valid users
        createNewChat(br, bw, users, user, chats);
    }

    /**
     * Validates the chat ability for each selected user.
     */
    private static void validateChatUsers(BufferedReader br, BufferedWriter bw, User user) throws IOException {
        String usernameToCheck = br.readLine();
        System.out.println("read: " + usernameToCheck);

        while (!"[DONE]".equals(usernameToCheck)) {
            User targetUser = new User("Sample Test Folder/" + User.findIDFromUsername(usernameToCheck) + ".txt");

            String validationResult = user.checkChatAbility(targetUser);
            switch (validationResult) {
                case "true":
                    Writer.write("", bw); // Valid user
                    System.out.println("write: ");
                    System.out.println("VALID USER");
                    break;

                case "self":
                    Writer.write("self", bw); // User is trying to chat with themselves
                    System.out.println("write: self");
                    System.out.println("INVALID USER");
                    break;

                case "false":
                    Writer.write("User cannot be chatted with!", bw); // Invalid user
                    System.out.println("write: User cannot be chatted with!");
                    System.out.println("INVALID USER");
                    break;
            }
            usernameToCheck = br.readLine();
            System.out.println("read: " + usernameToCheck);
        }
    }

    /**
     * Creates and initializes a new chat with the list of valid members.
     */
    private static void  createNewChat(BufferedReader br, BufferedWriter bw, ArrayList<User> users, User user, ArrayList<Chat> chats) throws IOException {
        String newChattersNames = br.readLine();
        System.out.println("read: " + newChattersNames);
        System.out.println("Received new chatters names");

        if (newChattersNames != null && !newChattersNames.isEmpty()) {
            // Include the current user in the chat
            String membersFromClient = newChattersNames + user.getUsername();
            // Convert usernames to user IDs
            ArrayList<String> newChatMembers = new ArrayList<>(Arrays.asList(membersFromClient.split(";")));

            newChatMembers.replaceAll(User::findIDFromUsername);

            // Create and initialize the new chat
            Chat newChat = new Chat(newChatMembers);
            chats.add(newChat);

            // Add the new chat to the users' chat lists
            for (User u : users) {
                if (newChatMembers.contains(u.getUserID())) {
                    u.addChat(newChat.getChatID());
                }
            }

            System.out.println("New chat created!");
            Writer.write("[SUCCESSFUL CHAT CREATION]", bw);
            System.out.println("write: [SUCCESSFUL CHAT CREATION]");
        } else {
            System.out.println("Chat is empty; selection was full of invalid chatters!");
            Writer.write("[CHAT CREATION UNSUCCESSFUL]", bw);
            System.out.println("write: [CHAT CREATION UNSUCCESSFUL]");
        }
    }

    private static void handleOperationTwo(ArrayList<User> users, User user, ArrayList<Chat> chats, BufferedReader br, BufferedWriter bw) throws IOException, InvalidFileFormatException {
        chats = updateChats(chats);
        users = updateUsers(users);
        // Write the logged-in user's chats to the client.
        // FORMAT:
        // Chat #0000 (With username, username, ...etc);Chat #0001 (With username, username);...etc
        String chatOutput = "";
        for (Chat c : chats) {
            if (c.getMemberList().contains(user.getUserID())) {
                chatOutput += "Chat #" + c.getChatID().substring(2) + " (With ";

                // Add each member in the chat to the displayed list of members
                // if they are NOT the logged-in user
                for (int i = 0; i < c.getMemberList().size(); i++) {
                    if (!c.getMemberList().get(i).equals(user.getUserID())) {
                        chatOutput += User.findUsernameFromID(c.getMemberList().get(i)) + ", ";
                    }
                }

                // As the logged-in user may not always appear at the beginning of the list
                // of chat members, separating members will be done by adding ", " to the end
                // of ALL entries (and not just all except the last one) and then removing it
                // from the last one at the end.
                if (!chatOutput.isEmpty()) {
                    chatOutput = chatOutput.substring(0, chatOutput.length() - 2);
                    chatOutput += ");";
                }
            }
        }
        // Write the list of chats to the client. See above for why substring is taken.
        if (!chatOutput.isEmpty()) {
            chatOutput = chatOutput.substring(0, chatOutput.length() - 1);
        }
        Writer.write(chatOutput, bw);
        System.out.println("write: " + chatOutput);

        if (!chatOutput.isEmpty()) {
            // Obtain the selected chat from the client.
            String chatID = "C_" + br.readLine();
            System.out.println("read: " + chatID);

            // Validate and store the index of the chat corresponding to the received chat ID.
            int chatIndex = -1;

            for (int i = 0; i < chats.size(); i++) {
                if (chats.get(i).getChatID().equals(chatID)) {
                    chatIndex = i;
                    break;
                }
            }

            if (chatIndex == -1) {
                Writer.write("Invalid Chat", bw);
                System.out.println("write: " + "Invalid Chat");
            } else {
                // Loop through the chat menu until the user stops.
                boolean viewChat = true;
                do {
                    // Display the top bar of the chat and its relevant information. ; indicates a new line.
                    String chatContent = "";
                    chatContent += "---------------------------------------------------------------------";
                    chatContent += ";Chat #" + chats.get(chatIndex).getChatID().substring(2);
                    chatContent += ";Members: You, ";

                    // Display the members apart from the logged-in user in the top bar of the chat.
                    for (String userID : chats.get(chatIndex).getMemberList()) {
                        if (!userID.equals(user.getUserID())) {
                            chatContent += User.findUsernameFromID(userID) + ", ";
                        }
                    }

                    // Ensure the last user in the list does not include ", ".
                    chatContent = chatContent.substring(0, chatContent.length() - 2);

                    // Ensure that only the last 6 messages of the chat are displayed. If the chat has less
                    // than 6 messages, display all of its message to avoid a negative index.
                    int startingMessageIndex;
                    if (chats.get(chatIndex).getMessageList().size() < 6)
                        startingMessageIndex = 0;
                    else
                        startingMessageIndex =
                                chats.get(chatIndex).getMessageList().size() - 6;

                    // Display the 7 recent messages in the chat.
                    chatContent += ";;[Displaying up to 6 most recent messages]";
                    for (int i = startingMessageIndex;
                         i < chats.get(chatIndex).getMessageList().size();
                         i++) {

                        // If the author is the logged-in user, display
                        // "You:" as the sender. Otherwise, display
                        // the author's username.
                        if (chats.get(chatIndex).getMessageList().get(i).getAuthorID().
                                equals(user.getUserID())) {
                            chatContent += ";You: ";
                        } else {
                            chatContent += ";" + User.findUsernameFromID(chats.get(chatIndex).
                                    getMessageList().
                                    get(i).getAuthorID()) + ": ";
                        }

                        // Add the message's content.
                        chatContent += chats.get(chatIndex).getMessageList().get(i).getMessage();
                    }
                    chatContent += ";---------------------------------------------------------------------";
                    chatContent += ";1 - Compose message";
                    chatContent += ";2 - Delete previous message";
                    chatContent += ";3 - Edit previous message";
                    chatContent += ";4 - Exit chat";

                    // Write the fully formed chat menu to the client.
                    Writer.write(chatContent, bw);
                    System.out.println("write: " + chatContent);

                    // Collect the client's decision and process accordingly.
                    String chatDecision = br.readLine();
                    System.out.println("read: " + chatDecision);

                    switch (chatDecision) {
                        case "1":
                            // Compose message
                            chats = updateChats(chats);
                            String messageToCompose = br.readLine();
                            System.out.println("read: " + messageToCompose);
                            chats.get(chatIndex).addMessage(new Message(user.getUserID(),
                                    0,
                                    messageToCompose));
                            break;
                        case "2":
                            // Delete previous message
                            chats = updateChats(chats);
                            chats.get(chatIndex).deleteMessage(user.getUserID());
                            break;
                        case "3":
                            // Edit previous message
                            chats = updateChats(chats);
                            String replacementMessage = br.readLine();
                            System.out.println("read: " + replacementMessage);
                            chats.get(chatIndex).editMessage(replacementMessage, user.getUserID());
                            break;
                        case "4":
                            // End chat loop
                            viewChat = false;
                            break;
                    }

                } while (viewChat);
            }
        }
    }

    private static void handleOperationFour(ArrayList<User> users, User user, BufferedWriter bw) throws IOException {
        // Reinitialize user arrayList With existing user
        users = updateUsers(users);
        String listOfAvailableUsers = "";
        for (int i = 0; i < users.size(); i++) {
            // If User IDs is not equal to User ID, add to list
            if (!users.get(i).getUserID().equals(user.getUserID())) {
                listOfAvailableUsers += users.get(i).getUsername() + ";";
            }
        }

        // Remove the last semicolon
        if (!listOfAvailableUsers.isEmpty()) {
            listOfAvailableUsers = listOfAvailableUsers.substring(0,listOfAvailableUsers.length() - 1);
        }

        // Send list to client
        System.out.println("Sending list of users to client...");
        Writer.write(listOfAvailableUsers, bw);
        System.out.println("write: " + listOfAvailableUsers);
    }

    // Get all the existing users (except main user)
    public static ArrayList<User> updateUsers(ArrayList<User> users) {
        File dataDirectory = new File("Sample Test Folder");
        File[] userFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("U_"));
        users = new ArrayList<>();
        if (users != null) {
            for (File userFile : userFiles) {
                users.add(new User(userFile.getAbsolutePath()));
            }
        }
        return users;
    }

    // Get all the existing chat
    public static ArrayList<Chat> updateChats(ArrayList<Chat> chats) throws InvalidFileFormatException {
        File dataDirectory = new File("Sample Test Folder");
        File[] chatFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("C_"));
        chats = new ArrayList<>();
        for (File chatFile : chatFiles) {
            chats.add(new Chat(chatFile.getAbsolutePath().substring(0,chatFile.getAbsolutePath().lastIndexOf("."))));
        }
        return chats;
    }
}