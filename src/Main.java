import java.util.*;
import java.io.*;

/**
 * Main Class
 * <p>
 * Entry point for the social media application. Initializes and starts the server
 * on a specified port, providing basic setup and error handling for the server instance.
 * <p>
 * Status: Incomplete
 * </p>
 *
 * @author Soleil Pham
 * @version 11/01/2024
 * @since 1.0
 */
public class Main {
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