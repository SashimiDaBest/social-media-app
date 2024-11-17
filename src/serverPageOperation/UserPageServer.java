package serverPageOperation;

import object.Chat;
import object.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
    public static void userPageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users, ArrayList<Chat> chats) {
        System.out.println("This is the user page.");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Account type: " + user.getAccountType());

        try {
            System.out.println("sending account information...");
            bw.write(user.getUsername());
            bw.newLine();
            bw.write(Integer.toString(user.getAccountType()));
            bw.newLine();
            bw.flush();

            // Handle client input
            String input = br.readLine();
            while (input != null) {
                System.out.println("Client input: " + input);
                if (input.equals("1")) {
                    System.out.println("Image Storing...");
                } else if (input.equals("2")) {

                    if (!user.getFollowerList().isEmpty() && !user.getFollowerList().get(0).isEmpty()) {
                        bw.newLine();
                        bw.flush();

                        write(user.getFollowerList(), bw);
                        try {
                            String line = br.readLine();
                            if (line != null && line.equals("VIEW")) {
                                OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                                break;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        bw.write("[EMPTY]");
                        bw.newLine();
                        bw.flush();
                    }
                } else if (input.equals("3")) {

                    if (!user.getFollowingList().isEmpty() && !user.getFollowingList().get(0).isEmpty()) {
                        bw.newLine();
                        bw.flush();

                        write(user.getFollowingList(), bw);
                        try {
                            String line = br.readLine();
                            if (line != null && line.equals("VIEW")) {
                                OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        bw.write("[EMPTY]");
                        bw.newLine();
                        bw.flush();
                    }
                } else if (input.equals("4")) {
                    if (!user.getBlockedList().isEmpty() && !user.getBlockedList().get(0).isEmpty()) {
                        bw.newLine();
                        bw.flush();

                        write(user.getBlockedList(), bw);
                        try {
                            String line = br.readLine();
                            if (line != null && line.equals("VIEW")) {
                                OtherPageServer.otherPageOperation(br, bw, user, users, chats);
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        bw.write("[EMPTY]");
                        bw.newLine();
                        bw.flush();
                    }
                } else if (input.equals("5")) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                } else {
                    System.out.println("ERROR: input " + input + " doesn't match expected!");
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
            for (String person : people) {
                System.out.println("people: " + User.findUsernameFromID(person));
                bw.write(User.findUsernameFromID(person));
                bw.newLine();
                bw.flush();
            }
            bw.write("END");
            bw.newLine();
            bw.flush();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: write() can't write to client");
            e.printStackTrace();
            return false;
        }
    }

}
