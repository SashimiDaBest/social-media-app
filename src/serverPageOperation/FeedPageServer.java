package serverPageOperation;

import object.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
    public static void feedPageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users, ArrayList<Chat> chats) {
        System.out.println("This is feed page");
        boolean continueFeed = true;

        do {
            try {
                String clientChosenOperation = br.readLine();

                if (clientChosenOperation == null)
                    break;

                // 1 - Chat Creation
                if (clientChosenOperation.equals("1")) {

                    // Write list of available users to chat with to the client
                    String listOfAvailableUsers = "";
                    for (int i = 0; i < users.size(); i++) {
                        if (!users.get(i).getUserID().equals(user.getUserID())) {   // <- Do not include the logged-in user
                            listOfAvailableUsers += users.get(i).getUsername() + ";";
                        }
                    }

                    // Separate list of users with semicolons (except the last one)
                    if (!listOfAvailableUsers.isEmpty()) {
                        listOfAvailableUsers = listOfAvailableUsers.substring(0,
                                listOfAvailableUsers.length() - 1);
                    }

                    // Write list of available users to client
                    bw.write(listOfAvailableUsers);
                    bw.newLine();
                    bw.flush();

                    // Read each selected user from client and make sure they can be chatted with
                    String usernameToCheck = br.readLine();
                    while (!usernameToCheck.equals("[DONE]")) {

                        // Identify the target user
                        User targetUser = null;
                        for (User u : users) {
                            if (u.getUsername().equals(usernameToCheck)) {
                                targetUser = u;
                                break;
                            }
                        }

                        // Check if the target can be chatted with and report back to client
                        if (targetUser == null) {
                            bw.write("That user does not exist!");
                        } else {
                            switch (user.checkChatAbility(targetUser)) {
                                case "true":
                                    bw.write("");
                                    break;
                                case "self":
                                    bw.write("self");
                                    break;
                                case "false":
                                    bw.write("User cannot be chatted with!");
                                    break;
                            }
                        }
                        bw.newLine();
                        bw.flush();

                        usernameToCheck = br.readLine();
                    }


                    // Create and properly initialize a new chat based on the member input from the user.
                    String membersFromClient = user.getUsername() + ";" + br.readLine();
                    ArrayList<String> newChatMembers = new ArrayList<>(Arrays.asList(membersFromClient.split(";")));

                    // Turn the list of usernames into a list of IDs so they fit the Chat constructor
                    newChatMembers.replaceAll(User::findIDFromUsername);

                    // Create the new chat and add all the members to it.
                    Chat newChat = new Chat(newChatMembers);
                    chats.add(newChat);
                    for (User u : users) {
                        if (newChatMembers.contains(u.getUserID())) {
                            u.addChat(newChat.getChatID());
                        }
                    }

                    // 2 - View Existing Chat
                } else if (clientChosenOperation.equals("2")) {
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
                    bw.write(chatOutput);
                    bw.newLine();
                    bw.flush();

                    if (!chatOutput.isEmpty()) {
                        // Obtain the selected chat from the client.
                        String chatID = "C_" + br.readLine();

                        // Validate and store the index of the chat corresponding to the received chat ID.
                        int chatIndex = -1;

                        for (int i = 0; i < chats.size(); i++) {
                            if (chats.get(i).getChatID().equals(chatID)) {
                                chatIndex = i;
                                break;
                            }
                        }

                        if (chatIndex == -1) {
                            bw.write("Invalid Chat");
                            bw.newLine();
                            bw.flush();
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

                                // Ensure that only the last 10 messages of the chat are displayed. If the chat has less
                                // than 10 messages, display all of its message to avoid a negative index.
                                int startingMessageIndex;
                                if (chats.get(chatIndex).getMessageList().size() < 10)
                                    startingMessageIndex = 0;
                                else
                                    startingMessageIndex = chats.get(chatIndex).getMessageList().size() - 10;

                                // Display the 10 recent messages in the chat.
                                chatContent += ";;[Displaying up to 10 most recent messages]";
                                for (int i = startingMessageIndex; i < chats.get(chatIndex).getMessageList().size(); i++) {

                                    // If the author is the logged-in user, display "You:" as the sender. Otherwise, display
                                    // the author's username.
                                    if (chats.get(chatIndex).getMessageList().get(i).getAuthorID().equals(user.getUserID())) {
                                        chatContent += ";You: ";
                                    } else {
                                        chatContent += ";" + User.findUsernameFromID(chats.get(chatIndex).getMessageList().
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
                                bw.write(chatContent);
                                bw.newLine();
                                bw.flush();

                                // Collect the client's decision and process accordingly.
                                String chatDecision = br.readLine();
                                switch (chatDecision) {
                                    case "1":
                                        // Compose message
                                        String messageToCompose = br.readLine();
                                        chats.get(chatIndex).addMessage(new Message(user.getUserID(), 0, messageToCompose));
                                        break;
                                    case "2":
                                        // Delete previous message
                                        chats.get(chatIndex).deleteMessage(user.getUserID());
                                        break;
                                    case "3":
                                        // Edit previous message
                                        String replacementMessage = br.readLine();
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
                } else if (clientChosenOperation.equals("3")) {
                    UserPageServer.userPageOperation(br, bw, user, users, chats);
                } else if (clientChosenOperation.equals("4")) {
                    // Write list of available users to view to the client
                    String listOfAvailableUsers = "";
                    for (int i = 0; i < users.size(); i++) {
                        if (!users.get(i).getUserID().equals(user.getUserID())) {   // <- Do not include the logged-in user
                            listOfAvailableUsers += users.get(i).getUsername();

                            // Separate list of users with semicolons
                            if (i != users.size() - 1) {
                                listOfAvailableUsers += ";";
                            }
                        }
                    }

                    // Write list of available users to client
                    bw.write(listOfAvailableUsers);
                    bw.newLine();
                    bw.flush();

                    // Ensure that the user selected by the client exists.
                    boolean validUser = false;
                    String userSelection = br.readLine();
                    for (User u : users) {
                        if (u.getUsername().equals(userSelection)) {
                            validUser = true;
                        }
                    }
                    bw.write(String.valueOf(validUser));
                    bw.newLine();
                    bw.flush();

                    if (validUser) {
                        OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                    }
                } else if (clientChosenOperation.equals("5")) {
                    continueFeed = false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (continueFeed);
    }
}
