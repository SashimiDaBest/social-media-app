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
            File[] userFiles = dataDirectory.listFiles((_, name) -> name.startsWith("U_02"));
            for (File userFile : userFiles) {
                users.add(new User(userFile.getAbsolutePath()));
            }

            File[] chatFiles = dataDirectory.listFiles((_, name) -> name.startsWith("C_02"));
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
            // Socket socket = serverSocket.accept();
            //action();
//          executorService.submit(new ClientHandler(clientSocket));
        } catch (Exception e) {
            System.out.println("Error accepting connection" + e.getMessage());
        }
    }

    public void action() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line = in.readLine();
            System.out.println(line);
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
//        executorService.shutdown();
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

    public void userPageOperation() {

    }

    public void otherPageOperation() {

    }
}