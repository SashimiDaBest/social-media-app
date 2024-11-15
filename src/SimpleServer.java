import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
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
    /**
     * The server socket that listens for client connections.
     */
    private ServerSocket serverSocket;
    //    private ExecutorService executorService;
    private static ArrayList<User> users;
    private static ArrayList<Chat> chats;
    private User user;

    /**
     * Initializes a new {@code SimpleServer} that binds to the specified port.
     *
     * @param port the port on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public SimpleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        if (users == null && chats == null) {
            users = new ArrayList<>();
            chats = new ArrayList<>();

            File dataDirectory = new File("Sample Test Folder");
            System.out.println(dataDirectory.getAbsolutePath());
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
    }

    /**
     * Starts the server and waits for client connections.
     * <p>
     * This method enters an infinite loop where it listens for incoming client connections.
     * Upon a successful connection, a new socket is created. Optionally, the connection could
     * be handled by a {@code ClientHandler} using an {@code ExecutorService} for concurrent processing.
     * </p>
     *
     * @throws IOException if an I/O error occurs while waiting for a connection
     */
    public void start() throws IOException {
        try {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            Thread newClientThread = new Thread(clientHandler);
            newClientThread.start();

            //action();
//          executorService.submit(new ClientHandler(clientSocket));
        } catch (Exception e) {
            System.out.println("Error accepting connection" + e.getMessage());
        }
    }

    public void action() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String userRequest = in.readLine();
            String username = ""; // some parsed form of userRequest

            if (userRequest.equals("...")) {
                userPageOperation(username);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Stops the server by closing the server socket and releasing associated resources.
     * <p>
     * This method closes the server socket, which will terminate any ongoing connections.
     * If an {@code ExecutorService} were in use, it would also be shut down here to clean up resources.
     * </p>
     *
     * @throws IOException if an I/O error occurs when closing the server socket
     */
    public void stop() throws IOException {
        serverSocket.close();
        //    executorService.shutdown();
    }

    /**
     * The main method that serves as the application's entry point. Prints a welcome message,
     * initializes a {@code SimpleServer} on port 12345, and starts the server to listen for
     * incoming client connections. Handles any {@code IOException} that may occur during server setup.
     *
     * @param args command-line arguments passed to the application (not used)
     */
    public static void main(String[] args) {
        try {
            SimpleServer server = new SimpleServer(12);
            server.start();
            server.feedPageOperation();

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void welcomePageOperation() {

    }

    /**
     * This method loops the user's Feed, giving them options to create chats, view chats, view their own profile,
     * view another user's profile, and exit the Feed. Chatting can also be done within this feed, as the user can
     * create messages, edit messages, and delete messages within each Chat.
     */
    public void feedPageOperation() {
        boolean continueFeed = true;
        do {
            String loggedinUserID = "U_0200"; // will be received from client
            Scanner input = new Scanner(System.in);
            System.out.print("""
                    Welcome to your Feed! What would you like to do?
                    1 - Create a new chat with selected users
                    2 - Open an existing chat
                    3 - View your profile
                    4 - View another user's profile
                    5 - Exit
                    """);

            switch (input.nextLine()) {
                case "1":
                    System.out.println("Type the names of the users you'd like to chat with on separate lines." +
                            "Type [DONE] when you are finished.");
                    for (User user : users) {
                        if (!user.getUserID().equals(loggedinUserID)) {
                            System.out.println(user.getUsername());
                        }
                    }
                    System.out.println("Create a new chat with:");
                    ArrayList<String> usersToChatWith = new ArrayList<>();
                    usersToChatWith.add(loggedinUserID);

                    while (true) {
                        String username = input.nextLine();
                        if (!username.equals("[DONE]")) {
                            boolean success = false;
                            boolean self = false;
                            for (User user : users) {
                                if (username.equals(user.getUsername())) {
                                    usersToChatWith.add(user.getUserID());
                                    success = true;
                                } else if (User.findIDFromUsername(username).equals(loggedinUserID)) {
                                    self = true;
                                }
                            }
                            if (self) {
                                System.out.println("You cannot add yourself to a chat!");
                            } else if (!success) {
                                System.out.println("User does not exist or cannot be added to a chat!");
                            }

                        } else {
                            Chat newChat = new Chat(usersToChatWith);
                            chats.add(newChat);
                            for (User user : users) {
                                if (usersToChatWith.contains(user.getUserID())) {
                                    user.addChat(newChat.getChatID());
                                }
                            }
                            break;
                        }
                    }
                    System.out.println("New chat created!");
                    break;
                case "2":
                    System.out.println("Enter the number (ex. 0001) of the Chat you'd like to open! Type * to return" +
                            " to the main menu.");
                    for (Chat chat : chats) {
                        String chatOutput = "";
                        if (chat.getMemberList().contains(loggedinUserID)) {
                            chatOutput = "Chat #" + chat.getChatID().substring(2) + " (With ";
                            for (int i = 0; i < chat.getMemberList().size(); i++) {
                                if (!chat.getMemberList().get(i).equals(loggedinUserID)) {
                                    chatOutput += User.findUsernameFromID(chat.getMemberList().get(i));
                                    if (i != chat.getMemberList().size() - 1) {
                                        chatOutput += ", ";
                                    }
                                }
                                if (i == chat.getMemberList().size() - 1) {
                                    chatOutput += ")";
                                }
                            }
                        }
                        if (!chatOutput.isEmpty())
                            System.out.println(chatOutput);
                    }

                    String selectedChatID = input.nextLine();
                    boolean viewChat = true;
                    do {
                        for (Chat chat : chats) {
                            if (("C_" + selectedChatID).equals(chat.getChatID())) {
                                System.out.print(
                                        "---------------------------------------------------------------------\n" +
                                                "Chat #" + chat.getChatID().substring(2) +
                                                "\nMembers: You, ");

                                for (int i = 0; i < chat.getMemberList().size(); i++) {
                                    if (!chat.getMemberList().get(i).equals(loggedinUserID)) {
                                        System.out.print(User.findUsernameFromID(chat.getMemberList().get(i)));

                                        if (i != chat.getMemberList().size() - 1) {
                                            System.out.print(", ");
                                        } else {
                                            System.out.println("\n");
                                        }
                                    }
                                }

                                int indexOfFirstMessageToDisplay;
                                if (chat.getMessageList().size() < 5)
                                    indexOfFirstMessageToDisplay = 0;
                                else
                                    indexOfFirstMessageToDisplay = chat.getMessageList().size() - 5;
                                System.out.println("[Displaying up to 5 most recent messages]");

                                for (int i = indexOfFirstMessageToDisplay; i < chat.getMessageList().size(); i++) {
                                    if (chat.getMessageList().get(i).getAuthorID().equals(loggedinUserID)) {
                                        System.out.print("You: ");
                                    } else {
                                        System.out.print(
                                                User.findUsernameFromID(chat.getMessageList().get(i).getAuthorID()) + ": ");
                                    }

                                    System.out.println(chat.getMessageList().get(i).getMessage());
                                }
                                System.out.println("---------------------------------------------------------------------");
                                System.out.print("""
                                        1 - Compose message
                                        2 - Delete previous message
                                        3 - Edit previous message
                                        4 - Exit chat
                                        """);
                                switch (input.nextLine()) {
                                    case "1":
                                        System.out.println("Enter your message:");
                                        chat.addMessage(new Message(loggedinUserID, 0, input.nextLine()));
                                        break;
                                    case "2":
                                        chat.deleteMessage(loggedinUserID);
                                        System.out.println("Message deleted!");
                                        break;
                                    case "3":
                                        System.out.println("Enter your replacement message:");
                                        chat.editMessage(input.nextLine(), loggedinUserID);
                                        break;
                                    case "4":
                                        viewChat = false;
                                        break;
                                    default:
                                        System.out.println("Invalid choice!");
                                        break;
                                }
                            }
                        }
                    } while (viewChat);
                case "3":
                    // make call to userPageOperation as appropriate
                    break;
                case "4":
                    // make call to otherPageOperation as appropriate
                    break;
                case "5":
                    continueFeed = false;
                    break;
            }
        }
        while (continueFeed);
    }
    // Derek's Main Changes

    // grabs user from users ArrayList based off userID; returns null if not found
    public User grabUserByID(String userID) {

        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    // grabs user from users ArrayList based off username
    public User grabUserByName(String username) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void userPageOperation(String clientUserName) {

        Scanner userInput = new Scanner(System.in);

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


            String mainChoice = userInput.nextLine();

            if (mainChoice.equals("1")) { // show followers

                while (true) {
                    System.out.println("Currently followed by: ");
                    // NOTE: only shows IDs for now
                    for (String userID : currentClient.getFollowerList()) {
                        System.out.println(userID);
                    }

                    System.out.println("Press 0 to exit\nPress 1 to view a profile");
                    String displayChoice = userInput.nextLine();

                    if (displayChoice.equals("0")) { // return to main menu
                        break;

                    } else if (displayChoice.equals("1")) { // profile view

                        while (true) {
                            System.out.println("Please select a profile; Press 0 to go back at any time");
                            String profile = userInput.nextLine(); // because options are IDs for now, this is also an ID

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
                    String displayChoice = userInput.nextLine();

                    if (displayChoice.equals("0")) { // return to main menu
                        break;

                    } else if (displayChoice.equals("1")) { // profile view

                        while (true) {
                            System.out.println("Please select a profile; Press 0 to go back at any time");
                            String profile = userInput.nextLine(); // because options are IDs for now, this is also an ID

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

                            String addedUser = userInput.nextLine();

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

                            String unfollowedUser = userInput.nextLine();

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
                    String displayChoice = userInput.nextLine();

                    if (displayChoice.equals("0")) { // return to main menu
                        break;

                    } else if (displayChoice.equals("1")) { // profile view

                        while (true) {
                            System.out.println("Please select a profile; Press 0 to go back at any time");
                            String profile = userInput.nextLine(); // because options are IDs for now, this is also an ID

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

                            String blockedUser = userInput.nextLine();

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

                            String unBlockedUser = userInput.nextLine();

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
        userInput.close();

    }

    public void otherPageOperation() {

    }

}

