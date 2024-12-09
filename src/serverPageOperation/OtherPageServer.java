package serverPageOperation;

import object.Chat;
import object.User;
import common.Writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 * <h1>OtherPageServer</h1>
 * <p>
 * This class manages server-side operations related to interacting with another user's profile page.
 * It handles various actions like following/unfollowing, blocking/unblocking users, and viewing the
 * followers and following lists of other users.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *     <li>Follow/Unfollow: Allows the current user to follow or unfollow another user.</li>
 *     <li>Block/Unblock: Enables the current user to block or unblock another user.</li>
 *     <li>View Followers/Following: Displays the list of followers or following users to the client.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <p>The main method in this class is {@code otherPageOperation}, which handles client requests for
 * viewing and interacting with another user's profile, managing their followers and following lists,
 * and allowing following or blocking actions.</p>
 *
 * <p><b>Author:</b> Soleil Pham</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 11/16/2024</p>
 */

public final class OtherPageServer {

    /**
     * Manages interactions on the other user's profile page. Handles actions such as
     * following/unfollowing, blocking/unblocking, and viewing followers and following lists.
     *
     * <p>This method is responsible for handling the client-side input regarding interactions with
     * another user's profile, processing the actions like follow, unfollow, block, unblock, and viewing
     * followers and following lists. It also sends responses back to the client regarding these actions.</p>
     *
     * @param br    The {@link BufferedReader} used to read input from the client.
     * @param bw    The {@link BufferedWriter} used to send responses to the client.
     * @param user  The {@link User} object representing the current user interacting with the profile.
     * @param users A list of all users in the system.
     * @param chats A list of all chats in the system.
     * @throws Exception If any error occurs during input/output operations or during any other operation.
     */
    public static void otherPageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users, ArrayList<Chat> chats) {
        System.out.println("Other page operations started");
        try {
            // Load the other user based on the client-provided username
            String otherUsername = br.readLine();
            System.out.println("read: " + otherUsername);
            User otherUser = new User("SampleTestFolder/" + User.findIDFromUsername(otherUsername) + ".txt");

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
                else if (input.equals("4")) {

                    if (user.getFollowingList().contains(otherUser.getUserID())) {
                        Writer.write("Unfollow", bw);
                        System.out.println("write: " + "Unfollow");
                    } else {
                        Writer.write("Follow", bw);
                        System.out.println("write: " + "Follow");
                    }
                }

                // for checking if client has blocked other User
                else if (input.equals("6")) {

                    if (user.getBlockedList().contains(otherUser.getUserID())) {
                        Writer.write("Unblock", bw);
                        System.out.println("write: " + "Unblock");
                    } else {
                        Writer.write("Block", bw);
                        System.out.println("write: " + "Block");
                    }
                } else {
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
