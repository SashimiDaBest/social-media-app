package serverPageOperation;

import exception.InvalidCreateAccountException;
import object.Chat;
import object.User;
import uiPage.Writer;

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

    private static final String SUCCESSFUL_SIGN_IN = "Successful sign-in";
    private static final String UNSUCCESSFUL_SIGN_IN = "Sign-in was unsuccessful";

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
        System.out.println("Welcome page operations started");
        boolean isSignedIn = false;

        try {
            while (true) {
                System.out.println("LOOP");
                // Proceed to the feed page once the user is signed in
                if (isSignedIn) {
                    FeedPageServer.feedPageOperation(br, bw, user, users, chats);
                    break;
                }

                // Wait for the client to choose an option (1 - Sign in, 2 - Sign up)
                String mainChoice = br.readLine();
                System.out.println("read: " + mainChoice);
                while (mainChoice == null) {
                    mainChoice = br.readLine();
                    System.out.println("read: " + mainChoice);

                }
                System.out.println("Received the message!");

                if (mainChoice.equals("1")) { // Sign in
                    System.out.println("SIGN IN");

                    String username = br.readLine();
                    System.out.println("read: " + username);
                    String password = br.readLine();
                    System.out.println("read: " + password);

                    if (User.hasLogin(username, password)) {
                        Writer.write(SUCCESSFUL_SIGN_IN, bw);
                        System.out.println("writer: " + SUCCESSFUL_SIGN_IN);
                        isSignedIn = true;
                        for (User u : users) {
                            if (u.getUsername().equals(username)) {
                                user = u;
                                break;
                            }
                        }

                    } else {
                        Writer.write(UNSUCCESSFUL_SIGN_IN, bw);
                        System.out.println("writer: " + UNSUCCESSFUL_SIGN_IN);
                    }

                } else if (mainChoice.equals("2")) { // Sign up
                    System.out.println("SIGN UP");
                    String newUsername = br.readLine();
                    System.out.println("read: " + newUsername);
                    String newPassword = br.readLine();
                    System.out.println("read: " + newPassword);

                    // If new username/password is valid
                    try {
                        User newUser;

                        if (!User.userNameValidation(newUsername)) {
                            throw new InvalidCreateAccountException("Username is taken!");
                        }

                        try {
                            newUser = new User(newUsername, newPassword);

                        } catch (InvalidCreateAccountException e) {
                            throw new InvalidCreateAccountException("Username is taken!");
                        }

                        newUser.createNewUser(newUsername, newPassword, newUser.getUserID());
                        users.add(newUser);
                        user = newUser;

                        Writer.write("User creation successful", bw);
                        System.out.println("writer: " + "User creation successful");

                    // If new username/password is invalid
                    } catch (InvalidCreateAccountException e) {
                        Writer.write("Invalid fields", bw);
                        System.out.println("write: " + "Invalid fields");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
