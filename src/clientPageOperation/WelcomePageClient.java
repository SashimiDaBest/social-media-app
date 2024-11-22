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
     * @param br      BufferedReader for reading responses from the server
     * @param bw      BufferedWriter for sending data to the server
     * @param socket  Socket
     */
    public static void welcomePage(BufferedReader br, BufferedWriter bw, Socket socket, WelcomePage welcomePageUI, CreateUserPage createUserPageUI, CardLayout cardLayout, JPanel cardPanel) throws IOException {
        WelcomePageClient.welcomePageUI = welcomePageUI;
        WelcomePageClient.createUserPageUI = createUserPageUI;
        bufferedReader = br;
        bufferedWriter = bw;
//        setupActionListeners();
    }
}
