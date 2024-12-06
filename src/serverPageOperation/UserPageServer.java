package serverPageOperation;

import clientPageOperation.UserPageClient;
import object.Chat;
import object.User;
import uiPage.WelcomePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * UserPageServer
 * <p>
 * This class handles the operations for the user page on the server side. It manages
 * interactions such as displaying the user's account information, managing the user's
 * followers, following, and blocked lists, and handling navigation to other pages like
 * the feed page or other user profiles.
 * </p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Displays user account details, including username and account type.</li>
 *     <li>Handles requests for viewing the user's followers, following, and blocked users.</li>
 *     <li>Provides functionality to navigate to other user profiles or return to the feed page.</li>
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
     * Manages user page operations, such as displaying user details, handling
     * interactions with the follower, following, and blocked lists, and redirecting
     * to other pages based on client input.
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

        UserPageClient.write(user.getProfilePic(), bw);

        try {
            System.out.println("sending account information...");
            UserPageClient.write(user.getUsername(), bw);
            UserPageClient.write(Integer.toString(user.getAccountType()), bw);
            if (!user.getFollowerList().isEmpty() && !user.getFollowerList().get(0).isEmpty()) {
                bw.newLine();
                bw.flush();
                write(user.getFollowerList(), bw);
            } else {
                UserPageClient.write("[EMPTY]", bw);
            }

            if (!user.getFollowingList().isEmpty() && !user.getFollowingList().get(0).isEmpty()) {
                bw.newLine();
                bw.flush();
                write(user.getFollowingList(), bw);
            } else {
                UserPageClient.write("[EMPTY]", bw);
            }

            if (!user.getBlockedList().isEmpty() && !user.getBlockedList().get(0).isEmpty()) {
                bw.newLine();
                bw.flush();
                write(user.getBlockedList(), bw);
            } else {
                UserPageClient.write("[EMPTY]", bw);
            }

            // Handle client input
            String input = br.readLine();
            while (input != null) {
                System.out.println("Client input: " + input);
                if (input.equals("1")) {
                    String path = br.readLine();
                    File file = new File(path);
                    if (saveImageAsNewFile(file, user.getUserID(), bw)){
                        user.setProfilePic("I" + user.getUserID().substring(1));
                    }
                    UserPageClient.write(user.getProfilePic(), bw);
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
            }

        } catch (IOException e) {
            System.err.println("ERROR: Server communication error - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean write(ArrayList<String> people, BufferedWriter bw) {
        try {
            if (!people.isEmpty() && !people.get(0).isEmpty()) {
                for (String person : people) {
                    UserPageClient.write(User.findUsernameFromID(person), bw);
                }
            } else {
                UserPageClient.write("[EMPTY]", bw);
            }
            UserPageClient.write("END", bw);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: write() can't write to client");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean saveImageAsNewFile(File sourceFile, String userID, BufferedWriter bw) {

        try {
            String fileName = sourceFile.getName();
            if (!isImage(sourceFile)) {
                throw new Exception("Error: File is not an image");
            }
            // Destination file (change this path as needed)
            File destinationFile = new File("Sample Test Folder/" + "I" + userID.substring(1) + ".png");

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

                UserPageClient.write("SAVE", bw);
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

    private static boolean isImage(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
