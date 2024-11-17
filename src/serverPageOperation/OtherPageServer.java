package serverPageOperation;

import objects.Chat;
import objects.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 * OtherPageServer
 * <p>
 * This class handles operations related to viewing and interacting with another user's profile
 * on the server side. It provides features such as following/unfollowing, blocking/unblocking,
 * and viewing the other user's followers or following lists.
 * </p>
 *
 * <p>Key Features:</p>
 * <ul>
 *     <li>Follow/Unfollow: Allows the current user to follow or unfollow another user.</li>
 *     <li>Block/Unblock: Enables the current user to block or unblock another user.</li>
 *     <li>View Followers/Following: Sends the list of followers or following users to the client and handles navigation requests.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The main method in this class is {@code otherPageOperation}, which handles client interactions
 * and manages profile-related actions based on the user's input.</p>
 *
 * @author Soleil Pham
 * @version 1.0
 * @since 11/16/2024
 */
public final class OtherPageServer {

    /**
     * Manages interactions on the other user's profile page. Handles actions such as
     * following/unfollowing, blocking/unblocking, and viewing followers and following lists.
     *
     * @param br    BufferedReader for reading client input
     * @param bw    BufferedWriter for sending messages to the client
     * @param user  The current user
     * @param users List of all users in the system
     * @param chats List of all chats in the system
     */
    public static void otherPageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users, ArrayList<Chat> chats) {
        System.out.println("This is other user page");

        try {
            // Load the other user based on the client-provided username
            String otherUsername = br.readLine();
            User otherUser = new User("Sample Test Folder/" + User.findIDFromUsername(otherUsername) + ".txt");
            System.out.println("Viewing profile of: " + otherUser.getUsername());

            // Display basic information about the other user
            System.out.println("User ID: " + otherUser.getUserID());
            System.out.println("Account Type: " + otherUser.getAccountType());
            System.out.println("Followers: " + otherUser.getFollowerList());
            System.out.println("Following: " + otherUser.getFollowingList());
            System.out.println("Blocked Users: " + otherUser.getBlockedList());

            String input = br.readLine();
            while (input != null) {
                System.out.println("Client input: " + input);
                if (input.equals("1")) {
                    if (user.getFollowingList().contains(otherUser.getUserID())) {
                        user.deleteFollowing(otherUser.getUserID());
                        bw.write("unfollowed " + otherUser.getUsername());
                    } else {
                        user.addFollowing(otherUser.getUserID());
                        bw.write("followed " + otherUser.getUsername());
                    }
                    bw.newLine();
                    bw.flush();
                } else if (input.equals("2")) {
                    if (user.getBlockedList().contains(otherUser.getUserID())) {
                        user.deleteBlock(otherUser.getUserID());
                        bw.write("unblocked " + otherUser.getUsername());
                    } else {
                        user.addBlock(otherUser.getUserID());
                        bw.write("blocked " + otherUser.getUsername());
                    }
                    bw.newLine();
                    bw.flush();
                } else if (input.equals("3")) {
                    try {
                        if (otherUser.getAccountType() == 1 && user.getFollowerList().contains(otherUser.getUserID())) {
                            bw.write("message");
                            UserPageServer.write(new ArrayList<>(), bw);
                        } else {
                            bw.write("");
                            UserPageServer.write(otherUser.getFollowerList(), bw);
                        }
                        if (br.readLine().equals("CHANGE")){
                            OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (input.equals("4")) {
                    try {
                        if (otherUser.getAccountType() == 1 && user.getFollowingList().contains(otherUser.getUserID())) {
                            bw.write("message");
                            UserPageServer.write(new ArrayList<>(), bw);
                        } else {
                            bw.write("no message");
                            UserPageServer.write(otherUser.getFollowerList(), bw);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    UserPageServer.write(user.getFollowingList(), bw);
                } else if (input.equals("5")) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                } else {
                    System.out.println("ERROR: " + input);
                }
                input = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("ERROR: server");
            e.printStackTrace();
        }
    }

}
