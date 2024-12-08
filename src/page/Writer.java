package page;

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
 * @version 1.0
 */
public final class Writer {

    /**
     * Reads and prints lines from the server until "END" is received.
     *
     * @param br BufferedReader for reading server responses
     * @return true if successful, false otherwise
     */
    public static ArrayList<String> readAndPrint(BufferedReader br) {
        ArrayList<String> buttonNames = new ArrayList<>();
        try {
            String line = br.readLine();
            System.out.println("WRITER READ: " + line);
            while (line != null && !line.equals("END")) {
//                if (!line.equals("[EMPTY]")) {
                    buttonNames.add(line);
                    System.out.println("added");
//                }
                line = br.readLine();
                System.out.println("WRITER READ: " + line);
            }
            return buttonNames;
        } catch (IOException e) {
            System.out.println("UserPageClient: readAndPrint() ERROR");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Writes a message to the server and flushes the BufferedWriter.
     *
     * @param outMessage The message to send
     * @param bw         BufferedWriter for sending the message
     * @return true if successful, false otherwise
     */
    public static boolean write(String outMessage, BufferedWriter bw) {
        try {
            bw.write(outMessage);
            bw.newLine();
            bw.flush();
            return true;
        } catch (IOException e) {
            System.out.println("UserPageClient: write() ERROR");
            e.printStackTrace();
            return false;
        }
    }
}
