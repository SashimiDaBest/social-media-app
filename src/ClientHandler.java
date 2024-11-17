import clientPageOperation.WelcomePageClient;
import uiPage.*;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/**
 * Client Handler
 * <p>
 * Handles individual client connections for a social media application, managing input and output streams
 * to facilitate communication between the server and the client.
 * <p>
 * Status: Incomplete
 * <p>
 * This class implements {@link Runnable} to allow handling client connections in separate threads,
 * supporting concurrent communication with multiple clients.
 * </p>
 *
 * @author Soleil Pham
 * @version 11/01/2024
 * @since 1.0
 */
public class ClientHandler implements Runnable {
    private String hostname;
    private int port;

    private Socket socket;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private WelcomePage welcomePage;
    private CreateUserPage createUserPage;
    private FeedViewPage feedViewPage;
    private UserProfilePage userProfilePage;
    private OtherProfilePage otherProfilePage;

    private BufferedWriter bw;
    private BufferedReader br;

    public ClientHandler(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        this.socket = new Socket(hostname, port);
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        String hostname = "localhost"; // Server hostname
        int port = 12;              // Port number

        try {
            ClientHandler client = new ClientHandler(hostname, port);
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        WelcomePageClient.welcomePage(scanner, br, bw);
        /*
            frame = new JFrame("Boiler Gram");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setSize(600, 400);

            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);

            welcomePage = new WelcomePage(cardLayout, cardPanel);
            createUserPage = new CreateUserPage(cardLayout, cardPanel);
            feedViewPage = new FeedViewPage(cardLayout, cardPanel);
            userProfilePage = new UserProfilePage(cardLayout, cardPanel);
            otherProfilePage = new OtherProfilePage(cardLayout, cardPanel);

            cardPanel.add(welcomePage, "welcomePage");
            cardPanel.add(createUserPage, "createUserPage");
            cardPanel.add(feedViewPage, "feedViewPage");
            cardPanel.add(userProfilePage, "userProfilePage");
            cardPanel.add(otherProfilePage, "otherProfilePage");

            frame.add(cardPanel);
            frame.setVisible(true);
//            setupActionListeners();
            out.write("hello");
           */
    }
    /*
    private void setupActionListeners() {

        welcomePage.getSignInButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = welcomePage.getUsernameField().getText();
                char[] password = welcomePage.getPasswordField().getPassword();
                String passwordString = new String(password);

                if (username == null || password == null) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userID = ""; //replace this with method to find userID based on username
//                    user = new User(userID);
                    cardLayout.show(cardPanel, "feedViewPage");
                }
            }
        });

        welcomePage.getNewAccountButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "createUserPage");
            }
        });

        createUserPage.getSignUpButtonButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = welcomePage.getUsernameField().getText();
                char[] password = welcomePage.getPasswordField().getPassword();
                String passwordString = new String(password);

                if (username == null || password == null) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String userID = ""; // Replace with method to find userID based on username
//                    user = new User(userID);
                    cardLayout.show(cardPanel, "feedViewPage");
                }
            }

        });
    }
     */
}
