package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The WelcomePageClient class handles the user interface for the initial sign-in
 * and sign-up process of the application. It communicates with the server to validate
 * login credentials and create new user accounts, and it redirects users to the
 * main feed page upon successful login or account creation.
 *
 * <p>Features and Functionality:</p>
 * <ul>
 *     <li>Sign In: Prompts the user for their username and password and validates them with the server.</li>
 *     <li>Sign Up: Collects a new username and password for account creation and validates them with the server.</li>
 *     <li>Retry or Create Account: Offers option to retry signing in or create a new account if the sign-in fails.</li>
 *     <li>Redirect: Navigates to the feed page upon successful sign-in or account creation.</li>
 * </ul>
 *
 * @version 1.0
 * @author Derek McTume
 */

public final class WelcomePageClient {

    /**
     * Displays the welcome page and handles user input for signing in or signing up.
     * Redirects to the feed page upon successful sign-in or account creation.
     *
     * @param scanner Scanner object for reading user input
     * @param br      BufferedReader for reading responses from the server
     * @param bw      BufferedWriter for sending data to the server
     * @param socket  Socket
     */
    public static void welcomePage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket) {
        try {
            boolean isSignedIn = false;
            String signUpDecision = "";
            String mainChoice = "";

            while (true) {
                // Redirect to the feed page if signed in
                if (isSignedIn) {
                    FeedPageClient.feedPage(scanner, br, bw, socket);
                    break;
                }

                // Display welcome options unless redirected from a failed sign-in
                if (!signUpDecision.isEmpty()) {
                    mainChoice = signUpDecision;
                } else {
                    System.out.print("Welcome to the Welcome Page\n" +
                            "1 - Sign in\n" +
                            "2 - Sign up\n");
                    mainChoice = scanner.nextLine();
                    UserPageClient.write(mainChoice, bw);
                }

                if (mainChoice.equals("1")) {
                    while (true) {
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        UserPageClient.write(username, bw);

                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        UserPageClient.write(password, bw);

                        // Wait for validation from the server
                        String messageFromServer = br.readLine();

                        // Successfully signing in
                        if (messageFromServer.equals("Successful sign-in")) {
                            System.out.println("You have entered the user feed!");
                            isSignedIn = true;
                            break;

                        } else if (messageFromServer.equals("Sign-in was unsuccessful")) {

                            while (true) {
                                System.out.println("1 - Retry signing in\n2 - Create account\n3 - Quit\n");

                                signUpDecision = scanner.nextLine();
                                UserPageClient.write(signUpDecision, bw);
                                // System.out.println("signUpDecision: " + signUpDecision);

                                if (signUpDecision.equals("1") || signUpDecision.equals("2") ||
                                        signUpDecision.equals("3")) {
                                    break;
                                } else {
                                    System.out.println("Invalid response, please try again");
                                    continue;
                                }
                            }
                            break;
                        }
                    }

                // for creating a new account
                } else if (mainChoice.equals("2")) {

                    while (true) {
                        System.out.println("New usernames cannot contain semicolons!");
                        System.out.print("New Username: ");
                        String username = scanner.nextLine();
                        UserPageClient.write(username, bw);

                        System.out.println("New passwords must contain a letter and a number, " +
                            "be at least 10 characters, and cannot contain semicolons!");
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        UserPageClient.write(password, bw);

                        // Wait on server validation
                        String messageFromServer = br.readLine();

                        if (messageFromServer.equals("User creation successful")) {
                            System.out.println("Successfuly created new account!");
                            isSignedIn = true;
                            break;

                        } else if (messageFromServer.equals("Invalid fields")) {
                            System.out.println("One of the fields is invalid, please try again");
                            continue;
                        }
                    }

                } else if (mainChoice.equals("3")) {
                    UserPageClient.write(mainChoice, bw);
                    try {
                        if (bw != null) {
                            bw.close(); // Close BufferedWriter
                        }
                        if (br != null) {
                            br.close(); // Close BufferedReader
                        }
                        if (socket != null && !socket.isClosed()) {
                            socket.close(); // Close the socket
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("Invalid main input, please try again");
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read information from server; this error should not occur");
            throw new RuntimeException(e);
        }
    }
}
