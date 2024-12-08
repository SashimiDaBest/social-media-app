package common;

import java.io.*;
import java.util.ArrayList;

/**
 * The UserPageClient class handles operations related to a user's profile page
 * in the client-side application. It provides options to view and manage followers,
 * following, and blocked users, change the user profile (incomplete), and navigate
 * back to the main feed view.
 *
 * <p>Features and Functionality:</p>
 * <ul>
 *     <li>Display the user's username and account type (public/private)</li>
 *     <li>Option to change the user profile (future feature for image handling)</li>
 *     <li>View the list of followers, following, or blocked users and navigate to their profiles</li>
 *     <li>Navigate back to the main feed view</li>
 * </ul>
 *
 * @author Soleil Pham
 * @version 12/01/2024
 */
public final class Writer {

    /**
     * Reads lines from the server until "END" is received and stores them in an ArrayList.
     *
     * @param br the {@link BufferedReader} for reading server responses
     * @return a {@link ArrayList} of strings containing the responses from the server,
     *         or {@code null} if an exception occurs
     */
    public static ArrayList<String> readAndPrint(BufferedReader br) {
        ArrayList<String> buttonNames = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null && !line.equals("END")) {
                buttonNames.add(line);
                line = br.readLine();
            }
            return buttonNames;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Writes a message to the server and flushes the output stream.
     *
     * @param outMessage the message to send to the server
     * @param bw         the {@link BufferedWriter} for sending the message
     * @return {@code true} if the message is successfully written and flushed,
     *         {@code false} otherwise
     */
    public static boolean write(String outMessage, BufferedWriter bw) {
        try {
            bw.write(outMessage);
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
