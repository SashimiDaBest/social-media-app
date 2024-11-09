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
        if(users == null && chats == null) {
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
    public void feedPageOperation() {
        Scanner input = new Scanner(System.in);
        System.out.print("Welcome to your Feed! What would you like to do?\n" +
                "1 - Create a new chat with selected users\n" +
                "2 - Open an existing chat\n" +
                "3 - View your profile\n" +
                "4 - View another user's profile\n" +
                "* - Type at any time to return to this home page\n");

        switch (input.nextLine()) {
            case "1":
                System.out.println("Type the names of the users you'd like to chat with on separate lines." +
                        "Type [DONE] when you are finished or * to cancel.");
                for (User user : users) {
                    System.out.println(user.getUsername());
                }
        }
    }
    public void userPageOperation() {

    }
    public void otherPageOperation() {

    }
}