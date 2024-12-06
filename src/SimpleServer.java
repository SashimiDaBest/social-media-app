import exception.*;
import object.*;
import serverPageOperation.WelcomePageServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SimpleServer
 *
 * A basic server implementation that listens on a specified port and handles client connections.
 * Supports multiple clients using a thread pool for efficient resource management.
 * Delegates client-specific operations to WelcomePageServer.
 *
 * This implementation can be extended to support multiple clients concurrently by using
 * an {@code ExecutorService} for thread management.
 *
 *
 * @author Soleil Pham
 * @author Connor Pugliese
 * @author Derek McTume
 * @version 11/01/2024
 * @since 1.0
 */
public class SimpleServer implements Runnable {
    private static int port = 12;
    private static final int THREAD_POOL_SIZE = 10; // Limit the number of concurrent threads
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private Socket socket;
    private ArrayList<User> users;
    private ArrayList<Chat> chats;

    private BufferedWriter bw;
    private BufferedReader br;


    /**
     * Constructs a SimpleServer instance to handle a client connection.
     *
     * @param socket The socket representing the client connection.
     * @param users  The list of users in the system.
     * @param chats  The list of chats in the system.
     */
    public SimpleServer(Socket socket, ArrayList<User> users, ArrayList<Chat> chats) {
        this.socket = socket;
        this.users = users;
        this.chats = chats;
    }

    /**
     * The main logic for handling a client connection.
     * Sets up input and output streams, and delegates operations to the WelcomePageServer.
     */
    @Override
    public void run() {
        try {
            System.out.println("Client connected: " + socket.getInetAddress());

            // Set up input and output streams
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Delegate to WelcomePageServer
            WelcomePageServer.welcomePageOperation(br, bw, null, users, chats);
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    /**
     * Closes the resources associated with the client connection, including the socket,
     * input stream, and output stream.
     */
    private void closeResources() {
        try {
            if (bw != null) bw.close();
            if (br != null) br.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    /**
     * The entry point of the application. Initializes the server, loads data from files,
     * and starts listening for client connections.
     *
     * @param args Command-line arguments. Not used in this implementation.
     */
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Chat> chats = new ArrayList<>();

        // Load user and chat data
        try {
            File dataDirectory = new File("Sample Test Folder");
            if (!dataDirectory.exists() || !dataDirectory.isDirectory()) {
                throw new FileNotFoundException("Data directory does not exist: " + dataDirectory.getAbsolutePath());
            }

            File[] userFiles = dataDirectory.listFiles((dir, name) -> name.startsWith("U_"));
            if (userFiles != null) {
                for (File userFile : userFiles) {
                    users.add(new User(userFile.getAbsolutePath()));
                }
            }

            File[] chatFiles = dataDirectory.listFiles((dir, name) -> name.startsWith("C_"));
            if (chatFiles != null) {
                for (File chatFile : chatFiles) {
                    try {
                        chats.add(new Chat(chatFile.getAbsolutePath().substring(0,
                                chatFile.getAbsolutePath().lastIndexOf("."))));
                    } catch (InvalidFileFormatException e) {
                        System.err.println("Invalid chat file: " + chatFile.getName());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return;
        }

        // Start the server
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new SimpleServer(clientSocket, users, chats));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}
