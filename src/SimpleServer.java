import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
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
    private ServerSocket serverSocket;
    private static ArrayList<User> users;
    private static ArrayList<Chat> chats;
    private User user;
    private BufferedReader clientReader;
    private PrintWriter clientWriter;

    public SimpleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        users = new ArrayList<>();
        chats = new ArrayList<>();

        File dataDirectory = new File("Sample Test Folder");
        File[] userFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("U_02"));
        for (File userFile : userFiles) {
            users.add(new User(userFile.getAbsolutePath()));
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
        try {
            Socket socket = serverSocket.accept();
            clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientWriter = new PrintWriter(socket.getOutputStream());

            ClientHandler clientHandler = new ClientHandler(socket);
            Thread newClientThread = new Thread(clientHandler);
            newClientThread.start();
        } catch (Exception e) {
            System.out.println("Error accepting connection" + e.getMessage());
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) {
        try {
            SimpleServer server = new SimpleServer(12);
            server.start();
            server.welcomePageOperation();

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void welcomePageOperation() {

        boolean isSignedIn = false;

        try {
            while(true) {

                // Stop once either options 1 or 2 are successful
                if (isSignedIn) {
                    break;
                }

                clientWriter.println("1 - Sign In\n2 - Create New Account");
                String mainChoice = clientReader.readLine();

                // for Signing In                
                if (mainChoice.equals("1")) {

                    while (true) {
                        clientWriter.println("Enter username: ");
                        String username = clientReader.readLine();

                        clientWriter.println("Enter password");
                        String password = clientReader.readLine();

                        // if existing username/password is valid
                        if (User.hasLogin(username, password)) {
                            clientWriter.println("You have entered the user feed!");
                            isSignedIn = true;
                            break;
                        
                        // if existing username/password is invalid
                        } else {
                            clientWriter.println("Please enter a valid username or password!");
                            continue;
                        }
                    }

                // for creating a new account
                } else if (mainChoice.equals("2")) {

                    while (true) {
                        clientWriter.println("Create a new username: ");
                        String newUsername = clientReader.readLine();

                        clientWriter.println("Create a new password: ");
                        String newPassword = clientReader.readLine();

                        // if new username/password is valid
                        try {
                            User newUser = new User(newUsername, newPassword);
                            users.add(newUser);
                            

                        // if new username/password is invalid
                        } catch (InvalidCreateAccountException e) {
                            clientWriter.println("Please enter a valid username or password!");
                            continue;
                        }
                    }

                } else { // response was invalid
                    clientWriter.println("Invalid argument, try again");
                    continue;
                }
            }    

        } catch (IOException e) {
            System.out.println("Could not read from client; no errors should be thrown!");
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
            String feedPageOperation = clientReader.readLine();
            if (feedPageOperation.equals("1")) {
                // write list of available users to chat with to the client

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


    public User grabUserByID(String userID) {

        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    public User grabUserByName(String username) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void userPageOperation(String clientUserName) {

        // find which user to work with:
        User currentClient = null;
        for (User user : users) {
            if (user.getUsername().equals(clientUserName)) {
                currentClient = user;
            }
        }

        // Convert account type for display and test if user was found
        String clientAccountType = null;
        try {
            if (currentClient.getAccountType() == 0) {
                clientAccountType = "Public";
            } else {
                clientAccountType = "Private";
            }

        } catch (NullPointerException e) {
            System.out.println("User could not be found in server; server should throw no errors!");
            e.printStackTrace();
        }

        try {
            while (true) {

                // main screen
                System.out.println(
                        currentClient.getUsername() + "\n" +
                                clientAccountType + "\n" +
                                currentClient.getProfilePic() + "\n" +
                                "1 - Follwers\n" +
                                "2 - Following\n" +
                                "3 - Blocked\n" +
                                "0 - Exit");


                String mainChoice = clientReader.readLine();

                if (mainChoice.equals("1")) { // show followers

                    while (true) {
                        System.out.println("Currently followed by: ");
                        // NOTE: only shows IDs for now
                        for (String userID : currentClient.getFollowerList()) {
                            System.out.println(userID);
                        }

                        System.out.println("Press 0 to exit\nPress 1 to view a profile");
                        String displayChoice = clientReader.readLine();

                        if (displayChoice.equals("0")) { // return to main menu
                            break;

                        } else if (displayChoice.equals("1")) { // profile view

                            while (true) {
                                System.out.println("Please select a profile; Press 0 to go back at any time");
                                String profile = clientReader.readLine(); // because options are IDs for now, this is also an ID

                                if (profile.equals("0")) { // go back to Follower screen
                                    break;

                                } else if (!currentClient.getFollowerList().contains(profile) && !profile.equals("0")) {
                                    System.out.println("The selected user is not following you, please try again");
                                    continue;

                                } else { // display selected profile's info

                                    User profiledUser = this.grabUserByID(profile);

                                    System.out.println(profile + "\nSome of their current followers:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowerList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                    System.out.println("Some of who they're currently following:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowingList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                }
                            }

                        } else { // redisplay Follower screen on invalid input
                            System.out.println("displayChoice is invalid, try again");
                            continue;
                        }
                    } // end of main screen option

                } else if (mainChoice.equals("2")) { // show following

                    while (true) {
                        System.out.println("Currently following: ");
                        // NOTE: only shows IDs for now
                        for (String userID : currentClient.getFollowingList()) {
                            System.out.println(userID);
                        }

                        System.out.println("Press 0 to exit\nPress 1 to view a profile\n" +
                                "Press 2 to follow someone new\nPress 3 to un-follow someone");
                        String displayChoice = clientReader.readLine();

                        if (displayChoice.equals("0")) { // return to main menu
                            break;

                        } else if (displayChoice.equals("1")) { // profile view

                            while (true) {
                                System.out.println("Please select a profile; Press 0 to go back at any time");
                                String profile = clientReader.readLine(); // because options are IDs for now, this is also an ID

                                if (profile.equals("0")) { // go back to Following screen
                                    break;

                                } else if (!currentClient.getFollowingList().contains(profile) && !profile.equals("0")) {
                                    System.out.println("You are not following the selected user, please try again");
                                    continue;

                                } else { // display selected profile's information

                                    User profiledUser = this.grabUserByID(profile);

                                    System.out.println(profile + "\nSome of their current followers:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowerList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                    System.out.println("Some of who they're currently following:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowingList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                }
                            }

                        } else if (displayChoice.equals("2")) { // follow someone new

                            while (true) {

                                System.out.println("Enter the ID of someone you want to follow\n" +
                                        "Press 0 to exit at anytime to go back");

                                String addedUser = clientReader.readLine();

                                if (addedUser.equals("0")) {
                                    break;

                                } else if (this.grabUserByID(addedUser) == null) {
                                    System.out.println("User is not found in the database; try again");
                                    continue;

                                } else {
                                    currentClient.addFollowing(addedUser);
                                    System.out.println("You are now following " + addedUser + "!");
                                }

                            }

                        } else if (displayChoice.equals("3")) { // unfollow someone you're already following

                            while (true) {

                                System.out.println("Enter the ID of a follower you want to un-follow\n" +
                                        "Press 0 to exit at anytime to go back");

                                String unfollowedUser = clientReader.readLine();

                                if (unfollowedUser.equals("0")) {
                                    break;

                                } else if (this.grabUserByID(unfollowedUser) == null) {
                                    System.out.println("User is not found in the database; try again");
                                    continue;

                                } else if (!currentClient.getFollowingList().contains(unfollowedUser)) {
                                    System.out.println("You can't unfollow someone you're not currently following!");
                                    continue;

                                } else {

                                    currentClient.deleteFollowing(unfollowedUser);
                                    System.out.println("You are no longer following " + unfollowedUser + "...");
                                }
                            }

                        } else { // redisplay Following screen on invalid input
                            System.out.println("displayChoice was invalid, try again");
                            continue;
                        }

                    } // end of main screen option

                } else if (mainChoice.equals("3")) { // show blocked

                    while (true) {
                        System.out.println("Currently blocked: ");
                        // NOTE: only shows IDs for now
                        for (String userID : currentClient.getBlockedList()) {
                            System.out.println(userID);
                        }

                        System.out.println("Press 0 to exit\nPress 1 to view a profile\n" +
                                "Press 2 to block someone\nPress 3 to unblock someone");
                        String displayChoice = clientReader.readLine();

                        if (displayChoice.equals("0")) { // return to main menu
                            break;

                        } else if (displayChoice.equals("1")) { // profile view

                            while (true) {
                                System.out.println("Please select a profile; Press 0 to go back at any time");
                                String profile = clientReader.readLine(); // because options are IDs for now, this is also an ID

                                if (profile.equals("0")) { // go back to Blocked screen
                                    break;

                                } else if (!currentClient.getBlockedList().contains(profile) && !profile.equals("0")) {
                                    System.out.println("Selected is not blocked, please try again");
                                    continue;

                                } else { // display selected profile's information

                                    User profiledUser = this.grabUserByID(profile);

                                    System.out.println(profile + "\nSome of their current followers:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowerList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                    System.out.println("Some of who they're currently following:");
                                    for (int i = 0; i < 3; i++) {
                                        System.out.println(profiledUser.getFollowingList().get(i));
                                        if (i == 2) {
                                            System.out.println("...");
                                        }
                                    }
                                }
                            }


                        } else if (displayChoice.equals("2")) { // block someone new

                            while (true) {

                                System.out.println("Enter the ID of someone you want to block\n" +
                                        "Press 0 to exit at anytime to go back");

                                String blockedUser = clientReader.readLine();

                                if (blockedUser.equals("0")) {
                                    break;

                                } else if (this.grabUserByID(blockedUser) == null) {
                                    System.out.println("User is not found in the database; try again");
                                    continue;

                                } else { // block also automatically unfollows them
                                    currentClient.addBlock(blockedUser);

                                    if (currentClient.getFollowingList().contains(blockedUser)) {
                                        currentClient.deleteFollowing(blockedUser);
                                        System.out.println("You have successfully blocked and un-followed " + blockedUser + "...");
                                    } else {
                                        System.out.println("You have successfully blocked " + blockedUser + "...");
                                    }
                                }

                            }

                        } else if (displayChoice.equals("3")) { // unblock someone already blocked

                            while (true) {

                                System.out.println("Enter the ID of someone you've blocked who you want to un-block\n" +
                                        "Press 0 to exit at anytime to go back");

                                String unBlockedUser = clientReader.readLine();

                                if (unBlockedUser.equals("0")) {
                                    break;

                                } else if (this.grabUserByID(unBlockedUser) == null) {
                                    System.out.println("User is not found in the database; try again");
                                    continue;

                                } else if (!currentClient.getBlockedList().contains(unBlockedUser)) {
                                    System.out.println("You can't unblock someone you haven't blocked!");
                                    continue;

                                } else {
                                    currentClient.deleteBlock(unBlockedUser);
                                    System.out.println("You have un-blocked " + unBlockedUser + "!");
                                }
                            }

                        } else { // redisplay Following screen on invalid input
                            System.out.println("displayChoice was invalid, try again");
                            continue;
                        }
                    }

                } else if (mainChoice.equals("0")) {
                    System.out.println("Exiting user page...");
                    break;

                } else { // invalid choice selection
                    System.out.println("Invalid selection");
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read from user; User Profile stream should not throw an error!");
            e.printStackTrace();
        }
        

    }

    public void otherPageOperation() {

    }

}

