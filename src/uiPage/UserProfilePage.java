package uiPage;

import clientPageOperation.FeedPageClient;
import clientPageOperation.OtherPageClient;
import clientPageOperation.UserPageClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class UserProfilePage extends JPanel {

    private JButton profileButton;
    private JButton backButton;
    private JButton nextButton;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public UserProfilePage(CardLayout cardLayout, JPanel cardPanel, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        setLayout(new BorderLayout());
        JLabel title = new JLabel("User Profile Page", JLabel.CENTER);


        JPanel navigationPanel = new JPanel(new BorderLayout());
        profileButton = new JButton("Profile");
        backButton = new JButton("Back");
        nextButton = new JButton("Next");

        navigationPanel.add(title, BorderLayout.NORTH);
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(title, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
        setupActionListeners();
    }

    public JButton getProfileButton() {
        return profileButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    private void setupActionListeners() {
        getProfileButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("1", bufferedWriter);
                System.out.print("What is your image file path: ");
                String path = "scanner.nextLine()"; //TODO: get path
                UserPageClient.write(path, bufferedWriter);
                try {
                    if (bufferedReader.readLine().equals("SAVE")) {
                        System.out.println("Set image successfully");
                    } else {
                        System.out.println("Set image failed");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        getBackButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("5", bufferedWriter);
                cardLayout.show(cardPanel, "feedViewPage");
            }
        });

        getNextButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    /*

        public static void userPage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket) {
            String username = "";
            String accountType = "";

            // Read username and account type from the server
            try {
                String line = br.readLine();
                username = line;
                if (line != null) {
                    username = line;
                    line = br.readLine();
                    accountType = "1".equals(line) ? "private" : "public";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Display menu and handle user input
            while (true) {
                System.out.println("Welcome to the User Page\n" +
                        "USERNAME: " + username + "\n" +
                        "ACCOUNT_TYPE: " + accountType + "\n" +
                        "1 - Change User Profile\n" +
                        "2 - View Follower\n" +
                        "3 - View Following\n" +
                        "4 - View Blocked\n" +
                        "5 - Go Back to Feed View\n" +
                        "6 - Quit");
                String input = scanner.nextLine();

                if (input.equals("1")) {

                } else if (input.equals("2")) {
                    write("2", bw);

                    String followerValidity;
                    try {
                        followerValidity = br.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (!followerValidity.equals("[EMPTY]")) {
                        readAndPrint(br);
                        System.out.print("Do you want to view Other (Y/N): ");
                        String input2 = scanner.nextLine();
                        if (input2.equals("Y")) {
                            try {
                                bw.write("VIEW");
                                bw.newLine();
                                bw.flush();
                                System.out.print("Other Username: ");
                                String otherUsername = scanner.nextLine();
                                OtherPageClient.otherPage(scanner, otherUsername, br, bw, socket);
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bw.newLine();
                                bw.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        System.out.println("You have no followers!");
                    }
                } else if (input.equals("3")) {
                    write("3", bw);

                    String followingValidity;
                    try {
                        followingValidity = br.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (!followingValidity.equals("[EMPTY]")) {
                        readAndPrint(br);
                        System.out.print("Do you want to view Other (Y/N): ");
                        String input2 = scanner.nextLine();
                        if (input2.equals("Y")) {
                            try {
                                bw.write("VIEW");
                                bw.newLine();
                                bw.flush();
                                System.out.print("Other Username: ");
                                String otherUsername = scanner.nextLine();
                                OtherPageClient.otherPage(scanner, otherUsername, br, bw, socket);
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bw.newLine();
                                bw.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        System.out.println("You are not following anyone!");
                    }
                } else if (input.equals("4")) {
                    write("4", bw);

                    String blockedValidity;
                    try {
                        blockedValidity = br.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (!blockedValidity.equals("[EMPTY]")) {
                        readAndPrint(br);
                        System.out.print("Do you want to view Other (Y/N): ");
                        String input2 = scanner.nextLine();
                        if (input2.equals("Y")) {
                            try {
                                bw.write("VIEW");
                                bw.newLine();
                                bw.flush();
                                System.out.print("Other Username: ");
                                String otherUsername = scanner.nextLine();
                                OtherPageClient.otherPage(scanner, otherUsername, br, bw, socket);
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bw.newLine();
                                bw.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        System.out.println("You have not blocked anyone!");
                    }
                } else if (input.equals("6")) {
                    write("6", bw);
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
                    System.out.println("Invalid input. Please try again.");
                }
            }
        }

     */
}



