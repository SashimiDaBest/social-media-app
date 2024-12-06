package serverPageOperation;

import object.Chat;
import object.User;
import uiPage.UserProfilePage;
import uiPage.Writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
        System.out.println("Other page operations started");
        try {
            // Load the other user based on the client-provided username
            String otherUsername = br.readLine();
            System.out.println("read: " + otherUsername);
            User otherUser = new User("Sample Test Folder/" + User.findIDFromUsername(otherUsername) + ".txt");

            Writer.write(otherUser.getUsername(), bw);
            Writer.write(Integer.toString(otherUser.getAccountType()), bw);
            Writer.write("stop", bw);

            // not sent in setAccounts
            Writer.write(otherUser.getProfilePic(), bw);

            // runs immediately after setAccount() and createImage() on client side
            // runs for other user's FOLLOWER list
            try {
                // client cannot view other user's info if they're privated and not following the client
                if (otherUser.getAccountType() == 1 && !user.getFollowerList().contains(otherUser.getUserID())) {
                    Writer.write("message", bw);
                    UserPageServer.write(new ArrayList<>(), bw);
                
                // if the selected user's following list is not empty, populate it with their information
                } else if (!otherUser.getFollowerList().get(0).isEmpty()) {
                    Writer.write("look", bw);
                    UserPageServer.write(otherUser.getFollowerList(), bw);
                
                // otherwise, it's empty 
                } else {
                    Writer.write("[EMPTY]", bw);
                    UserPageServer.write(otherUser.getFollowerList(), bw);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // runs for other user's FOLLOWING list
            try {
                // If other account is private and other user follow user
                if (otherUser.getAccountType() == 1 && !user.getFollowerList().contains(otherUser.getUserID())) {
                    Writer.write("message", bw);
                    UserPageServer.write(new ArrayList<>(), bw);
                } else if (!otherUser.getFollowingList().get(0).isEmpty()) {
                    Writer.write("look", bw);
                    UserPageServer.write(otherUser.getFollowingList(), bw);
                } else {
                    Writer.write("[EMPTY]", bw);
                    UserPageServer.write(otherUser.getFollowingList(), bw);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String input = br.readLine();
            System.out.println("read: " + input);
            while (input != null) {
                System.out.println("Client input: " + input);

                // for following
                if (input.equals("1")) {
                    if (user.getFollowingList().contains(otherUser.getUserID())) {
                        user.deleteFollowing(otherUser.getUserID());
                        otherUser.deleteFollower(user.getUserID());
                        Writer.write("unfollowed " + otherUser.getUsername(), bw);
                        System.out.println("write: " + "unfollowed " + otherUser.getUsername());
                        users = FeedPageServer.updateUsers(users);

                    } else {
                        user.addFollowing(otherUser.getUserID());
                        otherUser.addFollower(user.getUserID());
                        Writer.write("followed " + otherUser.getUsername(), bw);
                        System.out.println("write: " + "followed " + otherUser.getUsername());
                        users = FeedPageServer.updateUsers(users);
                    }

                // for blocking
                } else if (input.equals("2")) {
                    if (user.getBlockedList().contains(otherUser.getUserID())) {
                        user.deleteBlock(otherUser.getUserID());
                        Writer.write("unblocked " + otherUser.getUsername(), bw);
                        System.out.println("write: " + "unblocked " + otherUser.getUsername());
                        users = FeedPageServer.updateUsers(users);
                    } else {
                        user.addBlock(otherUser.getUserID());
                        Writer.write("blocked " + otherUser.getUsername(), bw);
                        System.out.println("write: " + "blocked " + otherUser.getUsername());
                        users = FeedPageServer.updateUsers(users);
                    }
                } else if (input.equals("3")) {
                    OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                    break;
                } else if (input.equals("5")) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                }

                // the following is used only for setRelation in OtherProfilePage 
                // (literally just for button text)

                // for checking if client is following otherUser
                else if (input.equals("4")){

                    if(user.getFollowingList().contains(otherUser.getUserID())) {
                        Writer.write("Unfollow", bw);
                        System.out.println("write: " + "Unfollow");
                    } else {
                        Writer.write("Follow", bw);
                        System.out.println("write: " + "Follow");
                    }
                }

                // for checking if client has blocked other User
                else if (input.equals("6")) {

                    if(user.getBlockedList().contains(otherUser.getUserID())) {
                        Writer.write("Unblock", bw);
                        System.out.println("write: " + "Unblock");
                    } else {
                        Writer.write("Block", bw);
                        System.out.println("write: " + "Block");
                    }
                }
                else {
                    System.out.println("ERROR: " + input);
                }
                input = br.readLine();
                System.out.println("read: " + input);
            }
        } catch (Exception e) {
            System.err.println("ERROR: server");
            e.printStackTrace();
        }
    }

}
