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

        ArrayList<String> chatIDs = user.getChatIDList();
        for (String chatID : chatIDs) {
            Writer.write(chatID, bw);
        }
        Writer.write("stop", bw);

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
        // Update chats and users data
        chats = updateChats(chats);
        users = updateUsers(users);

        // Prepare chat summary for the logged-in user
        // Write the logged-in user's chats to the client.
        // FORMAT:
        // Chat #0000 (With username, username, ...etc);Chat #0001 (With username, username);...etc
        String selectedChat = br.readLine();
        System.out.println("read: " + selectedChat);
        String chatOutput = "";
        boolean found = false;
        for (Chat chat : chats) {
            if (selectedChat.equals(chat.getChatID())) {
                Writer.write("valid", bw);
                System.out.println("write: valid");
                found = true;
                String chatContent = "";

                for (int i = 0; i < chat.getMessageList().size(); i++) {
                    Message message = chat.getMessageList().get(i);

                    // Check if the message is from the logged-in user
                    if (message.getAuthorID().equals(user.getUserID())) {
                        chatContent += "You: ";
                    } else {
                        chatContent += User.findUsernameFromID(message.getAuthorID()) + ": ";
                    }

                    // Add the message's content
                    chatContent += message.getMessage();
                    chatContent += ";";
                }

                // Send chat content to client
                Writer.write(chatContent.substring(0, chatContent.length() - 1), bw);
                System.out.println("write: " + chatContent);

                /*
                // Process client choice
                String chatDecision = br.readLine();
                System.out.println("read: " + chatDecision);
                chats = updateChats(chats);

                switch (chatDecision) {
                    case "1":
                        // Compose a new message
                        String newMessage = br.readLine();
                        System.out.println("read: " + newMessage);
                        chat.addMessage(new Message(user.getUserID(), 0, newMessage));
                        break;
                    case "2":
                        // Delete the previous message by the user
                        chat.deleteMessage(user.getUserID());
                        break;
                    case "3":
                        // Edit the previous message by the user
                        String editedMessage = br.readLine();
                        System.out.println("read: " + editedMessage);
                        chat.editMessage(editedMessage, user.getUserID());
                        break;
                    case "4":
                        // Exit the chat
                        viewChat = false;
                        break;
                }

                 */
                break;
            }
        }
        if (!found) {
            Writer.write("invalid", bw);
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

    /**
     * Get all the existing users (except main user)
     */
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

    /**
     * Get all the existing chat
     */
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