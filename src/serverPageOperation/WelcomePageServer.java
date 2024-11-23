package serverPageOperation;

import exception.InvalidCreateAccountException;
import object.Chat;
import object.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * WelcomePageServer
 * <p>
 * Handles the operations for the welcome page on the server side, including
 * user sign-in and sign-up functionality. This class communicates with the client
 * to validate user credentials, create new user accounts, and navigate to the feed page.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Sign-In: Validates the username and password provided by the client and grants access if correct.</li>
 *     <li>Sign-Up: Creates a new user account with a unique username and password if the provided details are
 *     valid.</li>
 *     <li>Redirect: Once the user is signed in or a new account is created successfully, the user is redirected to the
 *     feed page.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>The class provides a static method {@code welcomePageOperation} that handles incoming client requests,
 * processes sign-in or sign-up, and communicates back to the client with appropriate responses.</p>
 *
 * @author Derek McTume
 * @version 1.0
 * @since 11/16/2024
 */
public final class WelcomePageServer {

    /**
     * Manages the welcome page operations, including sign-in and sign-up,
     * and communicates with the client to validate user credentials or create
     * new accounts.
     *
     * @param br    BufferedReader for reading client input
     * @param bw    BufferedWriter for sending messages to the client
     * @param user  The user object to be updated upon successful sign-in or sign-up
     * @param users List of all users in the system
     */
    public static void welcomePageOperation(BufferedReader br, BufferedWriter bw, User user, ArrayList<User> users,
                                            ArrayList<Chat> chats) {
        System.out.println("This is welcome page");
        boolean isSignedIn = false;

        try {
            while (true) {
                // Proceed to the feed page once the user is signed in
                if (isSignedIn) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                }

                // Wait for the client to choose an option (1 - Sign in, 2 - Sign up)
                String mainChoice = br.readLine();
                while (mainChoice == null) {
                    mainChoice = br.readLine();
                    
                }
                System.out.println("Received the message!");
                
                if (mainChoice.equals("1")) { // Sign in
                    while (true) {
                        String username = br.readLine();
                        String password = br.readLine();

                        if (User.hasLogin(username, password)) {
                            bw.write("Successful sign-in");
                            bw.newLine();
                            bw.flush();
                            isSignedIn = true;
                            for (User u : users) {
                                if (u.getUsername().equals(username)) {
                                    user = u;
                                    break;
                                }
                            }
                            break;

                        } else {
                            bw.write("Sign-in was unsuccessful");
                            bw.newLine();
                            bw.flush();
                            break;
                        }
                    }

                } else if (mainChoice.equals("2")) { // Sign up
                    while (true) {
                        String newUsername = br.readLine();
                        String newPassword = br.readLine();

                        // If new username/password is valid
                        try {
                            User newUser = new User(newUsername, newPassword);
                            if (!User.userNameValidation(newUsername)) {
                                throw new InvalidCreateAccountException("Username is taken!");
                            }
                            newUser.createNewUser(newUsername, newPassword, newUser.getUserID());
                            users.add(newUser);
                            user = newUser;
                            isSignedIn = true;

                            bw.write("User creation successful");
                            bw.newLine();
                            bw.flush();
                            break;

                            // If new username/password is invalid
                        } catch (InvalidCreateAccountException e) {
                            bw.write("Invalid fields");
                            bw.newLine();
                            bw.flush();
                            continue;
                        }
                    }

                } else if (mainChoice.equals("3")) {
                    break;
                } else { // Invalid response
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
