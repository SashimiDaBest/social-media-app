import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    /**
     * Initializes a new {@code SimpleServer} that binds to the specified port.
     *
     * @param port the port on which the server will listen for incoming connections
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public SimpleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
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
        System.out.println("Server listening on port " + serverSocket.getLocalPort());
        while (true) {
            try {
                Socket socket = serverSocket.accept();
//                executorService.submit(new ClientHandler(clientSocket));
            } catch (IOException e) {
                System.out.println("Error accepting connection" + e.getMessage());
            }
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
        System.out.println("Hello world!");

        try {
            SimpleServer server = new SimpleServer(12345);
            server.start();
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }

    }
}