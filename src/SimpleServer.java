import exception.*;
import object.*;
import serverPageOperation.WelcomePageServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * SimpleServer
 * <p>
 * A basic server implementation that listens on a specified port and handles client connections.
 * When a client connects, it establishes input and output streams and delegates the handling
 * of client requests to the WelcomePageServer.
 * <p>
 *
 * <p>
 * This implementation can be extended to support multiple clients concurrently by using
 * an {@code ExecutorService} for thread management.
 * </p>
 *
 * @author Soleil Pham
 * @author Connor Pugliese
 * @author Derek McTume
 * @version 11/01/2024
 * @since 1.0
 */
public class SimpleServer implements Runnable {
    private static int port = 12;
    private Socket socket;

    // List of all users and chats in the system
    private static ArrayList<User> users;
    private static ArrayList<Chat> chats;
    private User user;

    // Input and output streams for client communication
    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * Constructs a SimpleServer that listens on the given port and initializes user and chat data.
     *
     * @param port The port number on which the server will listen for client connections
     * @throws IOException If an I/O error occurs while setting up the server socket
     */
    public SimpleServer(Socket socket) throws IOException {
        this.socket = socket;
        users = new ArrayList<>();
        chats = new ArrayList<>();
        // Load user and chat data from files
        File dataDirectory = new File("Sample Test Folder");
        File[] userFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("U_"));
        for (File userFile : userFiles) {
            User newUser = new User(userFile.getAbsolutePath());
            users.add(newUser);
        }

        File[] chatFiles = dataDirectory.listFiles((ignored, name) -> name.startsWith("C_"));
        for (File chatFile : chatFiles) {
            try {
                chats.add(new Chat(chatFile.getAbsolutePath().substring(0,
                        chatFile.getAbsolutePath().lastIndexOf("."))));
            } catch (InvalidFileFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Accessors for testing:
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }
    //////

    /**
     * Starts the server, listens for client connections, and handles them using the WelcomePageServer.
     *
     * @throws IOException If an I/O error occurs during communication
     */
    @Override
    public void run() {
        System.out.println("Server is listening on port " + port);
        try {

            // Set up input and output streams
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Delegate the client request handling to WelcomePageServer
            WelcomePageServer.welcomePageOperation(br, bw, user, users, chats);
            
        } catch (Exception e) {
            System.out.println("Error accepting connection" + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure resources are closed when the server stops
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the server by closing the server socket.
     *
     * @throws IOException If an I/O error occurs while closing the server socket
     */
    public void stop() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (bw != null) {
            bw.close();
        }
        if (br != null) {
            br.close();
        }
    }

    /**
     * Main method to start the SimpleServer.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                SimpleServer server = new SimpleServer(socket);
                new Thread(server).start();
            }
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            try {
                serverSocket.close();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
}
