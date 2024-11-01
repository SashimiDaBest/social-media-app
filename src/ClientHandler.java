import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Social Media App - Simple Server
 *
 * Server class with following methods and functionality
 * 1. run()
 *
 * Status: Incomplete
 *
 * @author soleil pham
 *
 * @version 11/01/2024
 *
 */

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            //Do something here
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }
}
