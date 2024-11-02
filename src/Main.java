import java.util.*;
import java.io.*;
/**
 * Social Media App - Main Class
 *
 * Status: Incomplete
 *
 * @author soleil pham
 *
 * @version 11/01/2024
 *
 */
public class Main {
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