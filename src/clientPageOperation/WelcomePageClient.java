package clientPageOperation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import uiPage.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

import uiPage.WelcomePage;

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
 *     <li>Retry or Create Account: Offers options to retry signing in or create a new account if the sign-in fails.</li>
 *     <li>Redirect: Navigates to the feed page upon successful sign-in or account creation.</li>
 * </ul>
 *
 * @version 1.0
 * @author Derek McTume
 */

public final class WelcomePageClient {


    private static CardLayout cardLayout;
    private static JFrame cardPanel;
    private static CreateUserPage createUserPageUI;

    private static WelcomePage welcomePageUI;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    /**
     * Displays the welcome page and handles user input for signing in or signing up.
     * Redirects to the feed page upon successful sign-in or account creation.
     *
     * @param scanner Scanner object for reading user input
     * @param br      BufferedReader for reading responses from the server
     * @param bw      BufferedWriter for sending data to the server
     * @param socket  Socket
     */
    public static void welcomePage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket, WelcomePage welcomePageUI, CreateUserPage createUserPageUI, CardLayout cardLayout, JPanel cardPanel) throws IOException {
        WelcomePageClient.welcomePageUI = welcomePageUI;
        WelcomePageClient.createUserPageUI = createUserPageUI;
        bufferedReader = br;
        bufferedWriter = bw;
        setupActionListeners();
        /*
                } else if(mainChoice.equals("3")) {
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
                }
             */
        setupActionListeners();
    }

    private static void setupActionListeners() {

        welcomePageUI.getSignInButton().addActionListener(e -> System.out.println("Clicked"));

        welcomePageUI.getSignInButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = welcomePageUI.getUsernameField().getText();
                String password = new String(welcomePageUI.getPasswordField().getPassword());
                UserPageClient.write(username, bufferedWriter);
                UserPageClient.write(password, bufferedWriter);

                String messageFromServer = "";
                try {
                    messageFromServer = bufferedReader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (messageFromServer.equals("Successful sign-in")) {
                    System.out.println("You have entered the user feed!");
                    cardLayout.show(cardPanel, "feedViewPage");
                } else if (messageFromServer.equals("Sign-in was unsuccessful")) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        welcomePageUI.getNewAccountButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "createUserPage");
            }
        });

        createUserPageUI.getSignUpButtonButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = welcomePageUI.getUsernameField().getText();
                String password = new String(welcomePageUI.getPasswordField().getPassword());
                UserPageClient.write(username, bufferedWriter);
                UserPageClient.write(password, bufferedWriter);

                System.out.println("New usernames cannot contain semicolons!");
                System.out.println("New passwords must contain a letter and a number, " +
                        "be at least 10 characters, and cannot contain semicolons!");

                String messageFromServer = "";
                try {
                    messageFromServer = bufferedReader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (messageFromServer.equals("User creation successful")) {
                    System.out.println("Successfuly created new account!");
                    cardLayout.show(cardPanel, "feedViewPage");

                } else if (messageFromServer.equals("Invalid fields")) {
                    System.out.println("One of the fields is invalid, please try again");
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }
}
