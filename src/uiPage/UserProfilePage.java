package uiPage;

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
import java.util.ArrayList;
import java.util.Scanner;

public class UserProfilePage extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private JButton profileButton;
    private JButton settingButton;
    private JButton backButton;
    private JButton nextButton;
    private JButton logoutButton;

    private ArrayList<JButton> followerButtons = new ArrayList<>();
    private ArrayList<JButton> followingButtons = new ArrayList<>();
    private ArrayList<JButton> blockedButtons = new ArrayList<>();

    public UserProfilePage(CardLayout cardLayout, JPanel cardPanel, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        System.out.println("USER FEED");
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        UserPageClient.write("USER_START", bufferedWriter);

        setLayout(new BorderLayout());
        JLabel title = new JLabel("User Profile Page", JLabel.CENTER);

        // set mainView layout so it orders item top to bottom
        JPanel mainView = new JPanel();
        mainView.setLayout(new BoxLayout(mainView, BoxLayout.PAGE_AXIS));

        JPanel box1 = new JPanel();
        JPanel box2 = new JPanel();
        JPanel box3 = new JPanel();
        JPanel box4 = new JPanel();

        mainView.add(box1);
        mainView.add(box2);
        mainView.add(box3);
        mainView.add(box4);

        // create box 1 - profile view
        box1.setLayout(new BoxLayout(box1, BoxLayout.X_AXIS));
        profileButton = new JButton("Profile");
        JPanel accountInfo = new JPanel();
        accountInfo.setLayout(new BoxLayout(accountInfo, BoxLayout.Y_AXIS));
        settingButton = new JButton("Settings");
        JTextPane username = new JTextPane();
        JTextPane accountType = new JTextPane();

        try {
            String line = bufferedReader.readLine();
            System.out.println("Received line: " + line); // Debugging output
            if (line != null && !line.equals("STOP")) {
                username.setText(line);

                line = bufferedReader.readLine();
                System.out.println("Account type line: " + line); // Debugging output
                String account = "1".equals(line) ? "private" : "public";
                accountType.setText(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        accountType.add(settingButton);
        accountType.add(username);
        accountInfo.add(accountType);

        box1.add(profileButton);
        box1.add(accountType);

        // create box 2 - follower view
        box2.setLayout(new BoxLayout(box2, BoxLayout.X_AXIS));
        JTextPane boxLabel1 = new JTextPane();
        boxLabel1.setText("Follower");
        box2.add(boxLabel1);

        JPanel followerList = new JPanel();
        followerList.setLayout(new BoxLayout(followerList, BoxLayout.Y_AXIS));
        box2.add(followerList);

        JTextPane followerStatus = new JTextPane();
        followerList.add(followerStatus);

        ArrayList<String> list = new ArrayList<>();
/*
        String followerValidity;
        try {
            followerValidity = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!followerValidity.equals("EMPTY")) {
            list = UserPageClient.readAndPrint(bufferedReader);
        } else {
            followerStatus.setText("You have no followers!");
        }

        for (int i = 0; i < list.size(); i++) {
            followerButtons.add(new JButton(list.get(i)));
            followerList.add(followerButtons.get(i));
        }

        list.clear();


 */
        // create box 3 - following view
        box3.setLayout(new BoxLayout(box3, BoxLayout.X_AXIS));
        JTextPane boxLabel2 = new JTextPane();
        boxLabel1.setText("Following");
        box3.add(boxLabel2);

        JPanel followingList = new JPanel();
        followingList.setLayout(new BoxLayout(followingList, BoxLayout.Y_AXIS));
        box2.add(followingList);

        JTextPane followingStatus = new JTextPane();
        followingList.add(followingStatus);

        /*
        String followingValidity;
        try {
            followingValidity = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!followingValidity.equals("EMPTY")) {
            list = UserPageClient.readAndPrint(bufferedReader);
        } else {
            followingStatus.setText("You have no following!");
        }

        for (int i = 0; i < list.size(); i++) {
            followingButtons.add(new JButton(list.get(i)));
            followingList.add(followingButtons.get(i));
        }

        list.clear();


         */
        // create box 4 - blocked view
        box4.setLayout(new BoxLayout(box4, BoxLayout.X_AXIS));
        JTextPane boxLabel3 = new JTextPane();
        boxLabel1.setContentType("Blocked");
        box4.add(boxLabel3);

        JPanel blockedList = new JPanel();
        blockedList.setLayout(new BoxLayout(blockedList, BoxLayout.Y_AXIS));
        box2.add(blockedList);

        JTextPane blockedStatus = new JTextPane();
        blockedList.add(blockedStatus);
/*
        String blockedValidity;
        try {
            blockedValidity = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!blockedValidity.equals("EMPTY")) {
            list = UserPageClient.readAndPrint(bufferedReader);
        } else {
            System.out.println("You have not blocked anyone!");
        }

        for (int i = 0; i < list.size(); i++) {
            blockedButtons.add(new JButton(list.get(i)));
            blockedList.add(blockedButtons.get(i));
        }

        list.clear();


 */
        // add header and footer for navigation
        JPanel navigationPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        nextButton = new JButton("Next");

        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(logoutButton, BorderLayout.CENTER);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(title, BorderLayout.NORTH);
        add(mainView, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);



        setupActionListeners();
    }

    public JButton getProfileButton() {
        return profileButton;
    }

    public JButton getSettingButton() {
        return settingButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }


    private void setupActionListeners() {
        getProfileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                UserPageClient.write("1", bufferedWriter);
//                System.out.print("What is your image file path: ");
//                String path = "scanner.nextLine()"; //TODO: get path
//                UserPageClient.write(path, bufferedWriter);
//                try {
//                    if (bufferedReader.readLine().equals("SAVE")) {
//                        System.out.println("Set image successfully");
//                    } else {
//                        System.out.println("Set image failed");
//                    }
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
            }
        });

        getSettingButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

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

        getLogoutButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    /*

        public static void userPage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket) {
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
                    UserPageClient.write("2", bufferedWriter);
                    System.out.print("Do you want to view Other (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        try {
                            bufferedWriter.write("VIEW");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            System.out.print("Other Username: ");
                            String otherUsername = scanner.nextLine();
                            OtherPageClient.otherPage(otherUsername, br, bw, socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }``
                } else if (input.equals("3")) {
                    write("3", bw);
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

                } else if (input.equals("4")) {
                    write("4", bw);
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



