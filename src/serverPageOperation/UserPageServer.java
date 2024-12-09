package serverPageOperation;

import common.Writer;
import object.Chat;
import object.User;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;

/**
 * UserPageServer
 * <p>
 * This class handles the server-side operations for managing a user's profile page. It provides
 * functionality for displaying user account details, managing the user's followers, following,
 * and blocked lists, as well as handling requests to update the user's profile picture and
 * navigate to other pages (like other user profiles, feed page, and welcome page).
 * </p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Displays user account details, including username and account type.</li>
 *     <li>Handles requests for viewing the user's followers, following, and blocked users.</li>
 *     <li>Allows the user to upload and save a new profile picture.</li>
 *     <li>Provides functionality to navigate to other user profiles, the feed page, or the welcome page.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The main method in this class is {@code userPageOperation}, which handles client interactions
 * and redirects to the appropriate operations based on the client's input.</p>
 *
 * @author Soleil Pham
 * @version 1.0
 * @since 11/16/2024
 */
public final class UserPageServer {

    /**
     * Handles user page operations, including displaying the user's account details, managing followers,
     * following, and blocked users, updating the profile picture, and redirecting to other pages based on
     * the client's input.
     *
     * @param br    BufferedReader for reading client input
     * @param bw    BufferedWriter for sending data to the client
     * @param user  The current user interacting with the user page
     * @param users List of all users in the system
     * @param chats List of all chats in the system
     */
    public static void userPageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users,
                                         ArrayList<Chat> chats) {
        System.out.println("User page operations started");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Account type: " + user.getAccountType());

        Writer.write(user.getProfilePic(), bw);
        System.out.println("writer: " + user.getProfilePic());

        try {
            System.out.println("sending account information...");
            Writer.write(user.getUsername(), bw);
            System.out.println("writer: " + user.getUsername());
            Writer.write(Integer.toString(user.getAccountType()), bw);
            System.out.println("writer: " + user.getAccountType());
            if (!user.getFollowerList().isEmpty() && !user.getFollowerList().get(0).isEmpty()) {
                System.out.println("writer: ");
                bw.newLine();
                bw.flush();
                write(user.getFollowerList(), bw);
            } else {
                Writer.write("[EMPTY]", bw);
                System.out.println("writer: " + "[EMPTY]");
            }

            if (!user.getFollowingList().isEmpty() && !user.getFollowingList().get(0).isEmpty()) {
                System.out.println("writer: ");
                bw.newLine();
                bw.flush();
                write(user.getFollowingList(), bw);
            } else {
                Writer.write("[EMPTY]", bw);
                System.out.println("writer: " + "[EMPTY]");
            }

            if (!user.getBlockedList().isEmpty() && !user.getBlockedList().get(0).isEmpty()) {
                System.out.println("writer: ");
                bw.newLine();
                bw.flush();
                write(user.getBlockedList(), bw);
            } else {
                Writer.write("[EMPTY]", bw);
                System.out.println("writer: " + "[EMPTY]");
            }

            // Handle client input
            String input = br.readLine();
            System.out.println("read: " + input);
            while (input != null) {
                System.out.println("Client input: " + input);
                if (input.equals("1")) {
                    String path = br.readLine();
                    System.out.println("read: " + path);
                    File file = new File(path);
                    if (saveImageAsNewFile(file, user.getUserID(), bw)) {
                        user.setProfilePic("I" + user.getUserID().substring(1));
                    }
                    Writer.write(user.getProfilePic(), bw);
                    System.out.println("writer: " + user.getProfilePic());
                } else if (input.equals("2")) {
                    OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                    break;
                } else if (input.equals("5")) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                } else if (input.equals("6")) {
                    WelcomePageServer.welcomePageOperation(br, bw, user, users, chats);
                    break;
                }
                input = br.readLine();
                System.out.println("read: " + input);
            }

        } catch (IOException e) {
            System.err.println("ERROR: Server communication error - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Writes a list of users (or IDs) to the BufferedWriter, sending it to the client.
     * It also handles the case where the list is empty and sends "[EMPTY]" to the client.
     *
     * @param people List of users or IDs to be sent to the client
     * @param bw     BufferedWriter used to send data to the client
     * @return Returns true if the data was successfully written to the client
     */
    public static boolean write(ArrayList<String> people, BufferedWriter bw) {
        try {
            if (!people.isEmpty() && !people.get(0).isEmpty()) {
                int count = 0;
                for (String person : people) {
                    Writer.write(User.findUsernameFromID(person), bw);
                    System.out.println("writer: " + User.findUsernameFromID(person) + " " + count);
                    count++;
                }
            } else {
                Writer.write("[EMPTY]", bw);
                System.out.println("writer: " + "[EMPTY]");
            }
            Writer.write("END", bw);
            System.out.println("writer: " + "[END]");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: write() can't write to client");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the uploaded image file as the new profile picture for the user.
     * This method checks if the file is an image and ensures the file is properly copied to
     * the appropriate directory.
     *
     * @param sourceFile The image file uploaded by the client
     * @param userID     The user ID of the current user
     * @param bw         BufferedWriter for sending the success message to the client
     * @return Returns true if the image is successfully saved, otherwise false
     */
    private static boolean saveImageAsNewFile(File sourceFile, String userID, BufferedWriter bw) {

        try {
            String fileName = sourceFile.getName();
            if (!isImage(sourceFile)) {
                throw new Exception("Error: File is not an image");
            }
            // Destination file (change this path as needed)
            File destinationFile = new File("SampleTestFolder/" + "I" + userID.substring(1) + ".png");

            // Ensure the destination directory exists
            if (!destinationFile.getParentFile().exists()) {
                destinationFile.getParentFile().mkdirs();
            }

            try (FileInputStream inputStream = new FileInputStream(sourceFile);
                 FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

                // Copy file data
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                Writer.write("SAVE", bw);
                System.out.println("writer: " + "SAVE");
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the given file is a valid image by attempting to read it using ImageIO.
     *
     * @param file The file to check
     * @return Returns true if the file is a valid image, otherwise false
     */
    private static boolean isImage(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
