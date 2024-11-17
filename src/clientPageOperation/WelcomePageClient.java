package clientPageOperation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class WelcomePageClient {

    public static void welcomePage(Scanner scanner, BufferedReader br, BufferedWriter bw) {

        try {
            boolean isSignedIn = false;

            // for redirecting after option 1 fails
            String signUpDecision = "";
            String mainChoice = "";

            while (true) {

                // move on once finally signed in
                if (isSignedIn) {
                    FeedPageClient.feedPage(scanner, br, bw);
                    break;
                }

                // skip first screen if sign in has already been attempted
                if (!signUpDecision.equals("")) {
                    mainChoice = signUpDecision;

                } else {
                    System.out.print("Welcome to the Welcome Page\n" +
                            "1 - Sign in\n" +
                            "2 - Sign up\n");
                    mainChoice = scanner.nextLine();
                    UserPageClient.write(mainChoice, bw);
                }

                // for Sigining In
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

                        // successfully signing in
                        if (messageFromServer.equals("Successful sign-in")) {
                            System.out.println("You have entered the user feed!");
                            isSignedIn = true;
                            break;

                        } else if (messageFromServer.equals("Sign-in was unsuccessful")) {

                            while(true) {
                                System.out.println("1 - Retry signing in\n2 - Create account");

                                signUpDecision = scanner.nextLine();
                                UserPageClient.write(signUpDecision, bw);
                                // System.out.println("signUpDecision: " + signUpDecision);

                                if (signUpDecision.equals("1") || signUpDecision.equals("2")) {
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

                    while(true) {
                        System.out.print("New Username: ");
                        String username = scanner.nextLine();
                        UserPageClient.write(username, bw);

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
