import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Simple Server
 * <p>
 * A basic server implementation that listens on a specified port and waits for client connections.
 * When a connection is established, it creates a new socket for communication.
 * <p>
 * Status: Incomplete
 * <p>
 * This class could be extended by adding an {@code ExecutorService} for managing multiple clients concurrently.
 * </p>
 *
 * @author Soleil Pham
 * @version 11/01/2024
 * @since 1.0
 */
//TODO: remember to close br and bw later on
public class SimpleServer {
    private static int PORT = 12;
    private ServerSocket serverSocket;
    private Socket socket;

    private static ArrayList<User> users;
    private static ArrayList<Chat> chats;
    private User user;

    private BufferedWriter bw;
    private BufferedReader br;

    public SimpleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        users = new ArrayList<>();
        chats = new ArrayList<>();

        File dataDirectory = new File("Sample Test Folder");
        File[] userFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("U_"));
        for (File userFile : userFiles) {
            User newUser = new User(userFile.getAbsolutePath());
            users.add(newUser);
        }

        File[] chatFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("C_02"));
        for (File chatFile : chatFiles) {
            try {
                chats.add(new Chat(chatFile.getAbsolutePath().substring(0, chatFile.getAbsolutePath().lastIndexOf("."))));
            } catch (InvalidFileFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() throws IOException {
        System.out.println("Server is listening on port " + PORT);
        try {
            while (true) {
                this.socket = serverSocket.accept();
                System.out.println("New client connected");
                this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                welcomePageOperation();
            }
        } catch (Exception e) {
            System.out.println("Error accepting connection" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) {
        try {
            SimpleServer server = new SimpleServer(PORT);
            server.start();
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public void welcomePageOperation() {
        System.out.println("This is welcome page");
        boolean isSignedIn = false;

        try {
            while (true) {

                // Stop once either options 1 or 2 are successful
                if (isSignedIn) {
                    // feedPageOperation();
                    userPageOperation();
                    break;
                }

                // Wait for client to make a choice
                String mainChoice = br.readLine();

                // for Signing In
                if (mainChoice.equals("1")) {

                    while (true) {

                        // Wait for client answer
                        String username = br.readLine();

                        // Wait for client answer
                        String password = br.readLine();

                        // if existing username/password is valid
                        if (User.hasLogin(username, password)) {
                            bw.write("Successful sign-in");
                            bw.newLine();
                            bw.flush();
                            isSignedIn = true;
                            for (User u : users) {
                                if (u.getUsername().equals(username)) {
                                    this.user = u;
                                    break;
                                }
                            }
                            break;

                            // if existing username/password is invalid
                        } else {
                            bw.write("Sign-in was unsuccessful");
                            bw.newLine();
                            bw.flush();
                            break;
                        }
                    }

                    // for creating a new account
                } else if (mainChoice.equals("2")) {

                    while (true) {

                        // wait for client answer
                        String newUsername = br.readLine();

                        // wait for client answer
                        String newPassword = br.readLine();

                        // if new username/password is valid
                        try {
                            User newUser = new User(newUsername, newPassword);
                            newUser.createNewUser(newUsername, newPassword, newUser.getUserID());
                            users.add(newUser);
                            this.user = newUser;
                            isSignedIn = true;

                            bw.write("User creation successful");
                            bw.newLine();
                            bw.flush();
                            break;

                            // if new username/password is invalid
                        } catch (InvalidCreateAccountException e) {
                            bw.write("Invalid fields");
                            bw.newLine();
                            bw.flush();
                            continue;
                        }
                    }

                } else { // response was invalid
                    // tries continues with the clientHandler
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loops the user's Feed, giving them options to create chats, view chats, view their own profile,
     * view another user's profile, and exit the Feed. Chatting can also be done within this feed, as the user can
     * create messages, edit messages, and delete messages within each Chat.
     */
    public void feedPageOperation() {
        System.out.println("This is feed page");
        boolean continueFeed = true;
        do {
            try {
                String clientChosenOperation = br.readLine();

                // 1 - Chat Creation
                if (clientChosenOperation.equals("1")) {

                    // Write list of available users to chat with to the client
                    String listOfAvailableUsers = "";
                    for (int i = 0; i < users.size(); i++) {
                        if (!users.get(i).getUserID().equals(user.getUserID())) {   // <- Do not include the logged-in user
                            listOfAvailableUsers += users.get(i).getUsername();     // in the list of available users to
                            // chat with
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
                            chatOutput = chatOutput.substring(0, chatOutput.length() - 2);
                            chatOutput += ");";
                        }
                    }
                    // Write the list of chats to the client. See above for why substring is taken.
                    chatOutput = chatOutput.substring(0, chatOutput.length() - 1);
                    bw.write(chatOutput);
                    bw.newLine();
                    bw.flush();

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
                } else if (clientChosenOperation.equals("5")) {
                    continueFeed = false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (continueFeed);
    }

//    public void feedPageOperation() {
//        boolean continueFeed = true;
//        do {
//            String loggedinUserID = "U_0200"; // will be received from client
//            Scanner input = new Scanner(System.in);
//            System.out.print(
//                    "Welcome to your Feed! What would you like to do?\n" +
//                    "1 - Create a new chat with selected users\n" +
//                    "2 - Open an existing chat\n" +
//                    "3 - View your profile\n" +
//                    "4 - View another user's profile\n" +
//                    "5 - Exit\n");
//
//            switch (input.nextLine()) {
//                case "1":
//                    System.out.println("Type the names of the users you'd like to chat with on separate lines." +
//                            "Type [DONE] when you are finished.");
//                    for (User user : users) {
//                        if (!user.getUserID().equals(loggedinUserID)) {
//                            System.out.println(user.getUsername());
//                        }
//                    }
//                    System.out.println("Create a new chat with:");
//                    ArrayList<String> usersToChatWith = new ArrayList<>();
//                    usersToChatWith.add(loggedinUserID);
//
//                    while (true) {
//                        String username = input.nextLine();
//                        if (!username.equals("[DONE]")) {
//                            boolean success = false;
//                            boolean self = false;
//                            for (User user : users) {
//                                if (username.equals(user.getUsername())) {
//                                    usersToChatWith.add(user.getUserID());
//                                    success = true;
//                                } else if (User.findIDFromUsername(username).equals(loggedinUserID)) {
//                                    self = true;
//                                }
//                            }
//                            if (self) {
//                                System.out.println("You cannot add yourself to a chat!");
//                            } else if (!success) {
//                                System.out.println("User does not exist or cannot be added to a chat!");
//                            }
//
//                        } else {
//                            Chat newChat = new Chat(usersToChatWith);
//                            chats.add(newChat);
//                            for (User user : users) {
//                                if (usersToChatWith.contains(user.getUserID())) {
//                                    user.addChat(newChat.getChatID());
//                                }
//                            }
//                            break;
//                        }
//                    }
//                    System.out.println("New chat created!");
//                    break;
//                case "2":
//                    System.out.println("Enter the number (ex. 0001) of the Chat you'd like to open! Type * to return" +
//                            " to the main menu.");
//                    for (Chat chat : chats) {
//                        String chatOutput = "";
//                        if (chat.getMemberList().contains(loggedinUserID)) {
//                            chatOutput = "Chat #" + chat.getChatID().substring(2) + " (With ";
//                            for (int i = 0; i < chat.getMemberList().size(); i++) {
//                                if (!chat.getMemberList().get(i).equals(loggedinUserID)) {
//                                    chatOutput += User.findUsernameFromID(chat.getMemberList().get(i));
//                                    if (i != chat.getMemberList().size() - 1) {
//                                        chatOutput += ", ";
//                                    }
//                                }
//                                if (i == chat.getMemberList().size() - 1) {
//                                    chatOutput += ")";
//                                }
//                            }
//                        }
//                        if (!chatOutput.isEmpty())
//                            System.out.println(chatOutput);
//                    }
//
//                    String selectedChatID = input.nextLine();
//                    boolean viewChat = true;
//                    do {
//                        for (Chat chat : chats) {
//                            if (("C_" + selectedChatID).equals(chat.getChatID())) {
//                                System.out.print(
//                                        "---------------------------------------------------------------------\n" +
//                                                "Chat #" + chat.getChatID().substring(2) +
//                                                "\nMembers: You, ");
//
//                                for (int i = 0; i < chat.getMemberList().size(); i++) {
//                                    if (!chat.getMemberList().get(i).equals(loggedinUserID)) {
//                                        System.out.print(User.findUsernameFromID(chat.getMemberList().get(i)));
//
//                                        if (i != chat.getMemberList().size() - 1) {
//                                            System.out.print(", ");
//                                        } else {
//                                            System.out.println("\n");
//                                        }
//                                    }
//                                }
//
//                                int indexOfFirstMessageToDisplay;
//                                if (chat.getMessageList().size() < 5)
//                                    indexOfFirstMessageToDisplay = 0;
//                                else
//                                    indexOfFirstMessageToDisplay = chat.getMessageList().size() - 5;
//                                System.out.println("[Displaying up to 5 most recent messages]");
//
//                                for (int i = indexOfFirstMessageToDisplay; i < chat.getMessageList().size(); i++) {
//                                    if (chat.getMessageList().get(i).getAuthorID().equals(loggedinUserID)) {
//                                        System.out.print("You: ");
//                                    } else {
//                                        System.out.print(
//                                                User.findUsernameFromID(chat.getMessageList().get(i).getAuthorID()) + ": ");
//                                    }
//
//                                    System.out.println(chat.getMessageList().get(i).getMessage());
//                                }
//                                System.out.println("---------------------------------------------------------------------");
//                                System.out.print(
//                                        "1 - Compose message\n" +
//                                        "2 - Delete previous message\n" +
//                                        "3 - Edit previous message\n" +
//                                        "4 - Exit chat\n");
//                                switch (input.nextLine()) {
//                                    case "1":
//                                        System.out.println("Enter your message:");
//                                        chat.addMessage(new Message(loggedinUserID, 0, input.nextLine()));
//                                        break;
//                                    case "2":
//                                        chat.deleteMessage(loggedinUserID);
//                                        System.out.println("Message deleted!");
//                                        break;
//                                    case "3":
//                                        System.out.println("Enter your replacement message:");
//                                        chat.editMessage(input.nextLine(), loggedinUserID);
//                                        break;
//                                    case "4":
//                                        viewChat = false;
//                                        break;
//                                    default:
//                                        System.out.println("Invalid choice!");
//                                        break;
//                                }
//                            }
//                        }
//                    } while (viewChat);
//                case "3":
//                    // make call to userPageOperation as appropriate
//                    break;
//                case "4":
//                    // make call to otherPageOperation as appropriate
//                    break;
//                case "5":
//                    continueFeed = false;
//                    break;
//            }
//        }
//        while (continueFeed);
//    }

    public void userPageOperation() {
        System.out.println("This is user page");
        ArrayList<String> people;
        System.out.println("Username: " + user.getUsername());
        System.out.println("Account type: " + user.getAccountType());
        try {
            System.out.println("sending account information...");
            bw.write(user.getUsername());
            bw.newLine();
            bw.write(user.getAccountType());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String input = br.readLine();
            do {
                System.out.println("client input: " + input);
                if (input.equals("1")) {
                    System.out.println("Image Storing...");
                } else if (input.equals("2")) {
                    write(user.getFollowerList());
                    try {
                        if (br.readLine().equals("VIEW")) {
                            otherPageOperation();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (input.equals("3")) {
                    write(user.getFollowingList());
                } else if (input.equals("4")) {
                    write(user.getBlockedList());
                } else if (input.equals("5")) {
                    feedPageOperation();
                    break;
                } else {
                    System.out.println("ERROR: input " + input + " doesn't match expected!");
                }
                if (input != null) {
                    input = br.readLine();
                } else {
                    break;
                }
            } while (input != null);
        } catch (Exception e) {
            System.err.println("ERROR: server " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void otherPageOperation() {
        System.out.println("This is other user page");
        ArrayList<String> people;
        try {
            User otherUser = new User("Sample Test Folder/" + User.findIDFromUsername(br.readLine()) + ".txt");
            System.out.println(otherUser.getUsername());
            System.out.println(otherUser.getUserID());
            System.out.println(otherUser.getAccountType());
            System.out.println(otherUser.getFollowerList());
            System.out.println(otherUser.getFollowingList());
            System.out.println(otherUser.getBlockedList());
            String input = br.readLine();
            do {
                System.out.println("client input: " + input);
                if (input.equals("1")) {
                    if (user.getFollowingList().contains(otherUser.getUsername())) {
                        user.deleteFollowing(otherUser.getUsername());
                        bw.write("unfollowed " + otherUser.getUsername());
                    } else {
                        user.addFollowing(otherUser.getUserID());
                        bw.write("followed " + otherUser.getUsername());
                    }
                    bw.newLine();
                    bw.flush();
                } else if (input.equals("2")) {
                    if (user.getBlockedList().contains(otherUser.getUsername())) {
                        user.deleteBlock(otherUser.getUsername());
                        bw.write("unblocked " + otherUser.getUsername());
                    } else {
                        user.addBlock(otherUser.getUserID());
                        bw.write("blocked " + otherUser.getUsername());
                    }
                    bw.newLine();
                    bw.flush();
                } else if (input.equals("3")) {
                    try {
                        if (otherUser.getAccountType() == 1 && user.getFollowerList().contains(otherUser.getUsername())) {
                            bw.write("message");
                        } else {
                            bw.write("no message");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    write(user.getFollowerList());
                } else if (input.equals("4")) {
                    try {
                        if (otherUser.getAccountType() == 1 && user.getFollowingList().contains(otherUser.getUsername())) {
                            bw.write("message");
                        } else {
                            bw.write("no message");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    write(user.getFollowingList());
                } else if (input.equals("5")) {
                    feedPageOperation();
                    break;
                } else {
                    System.out.println("ERROR: " + input);
                }
                if (input != null) {
                    input = br.readLine();
                } else {
                    break;
                }
            } while (input != null);
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean write(ArrayList<String> people) {
        System.out.println("write()");
        try {
            for (int i = 0; i < people.size(); i++) {
                System.out.println("people: " + User.findUsernameFromID(people.get(i)));
                bw.write(people.get(i));
                bw.newLine();
                bw.flush();
            }
            bw.write("END");
            bw.newLine();
            bw.flush();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: write() can't write to client");
            e.printStackTrace();
            return false;
        }
    }
}