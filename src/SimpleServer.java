import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
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
        File[] userFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("U_02"));
        for (File userFile : userFiles) {
            User newUser = new User(userFile.getAbsolutePath());
            users.add(newUser);
            newUser.createNewUser(newUser.getUsername(), newUser.getPassWord(), newUser.getUserID());
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

        boolean isSignedIn = false;

        try {
            while (true) {

                // Stop once either options 1 or 2 are successful
                if (isSignedIn) {
                    feedPageOperation();
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
                            continue;
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
                    bw.newLine();
                    bw.flush();

                    usernameToCheck = br.readLine();
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        ArrayList<String> people;
        try {
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
                System.out.println(input);
                if (input.equals("1")) {
                    System.out.println("Hello");
                } else if (input.equals("2")) {
                    write(user.getFollowerList());
                } else if (input.equals("3")) {
                    write(user.getFollowingList());
                } else if (input.equals("4")) {
                    write(user.getBlockedList());
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

    public void otherPageOperation(Scanner scanner) {
        ArrayList<String> people;
        try {
            User otherUser = new User(br.readLine());
            String input = br.readLine();
            do {
                System.out.println(input);
                if (input.equals("1")) {
                    if (user.getFollowingList().contains(otherUser.getUsername())) {
                        //remove following
                        bw.write("unfollowed " + otherUser.getUsername());
                    } else {
                        user.addFollowing(otherUser.getUserID());
                        bw.write("followed " + otherUser.getUsername());
                    }
                    bw.newLine();
                    bw.flush();
                } else if (input.equals("2")) {
                    if (user.getBlockedList().contains(otherUser.getUsername())) {
                        //remove blocked
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
        try {
            for (int i = 0; i < people.size(); i++) {
                bw.write(people.get(i));
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (Exception e) {
            System.out.println("Could not write to server");
            e.printStackTrace();
            return false;
        }
    }

    //remember to close br and bw later on

}

