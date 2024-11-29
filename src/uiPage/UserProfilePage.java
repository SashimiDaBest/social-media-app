package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfilePage extends JPanel {
    private PageManager pageManager;
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

    public UserProfilePage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.pageManager = pageManager;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;

        setLayout(new BorderLayout());

        JPanel accountPanel = setAccountInfo();
        JPanel followerPanel = setPeople(1);
        JPanel followingPanel = setPeople(2);
        JPanel blockedPanel = setPeople(3);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        mainPanel.add(accountPanel);
        mainPanel.add(followerPanel);
        mainPanel.add(followingPanel);
        mainPanel.add(blockedPanel);
        add(mainPanel, BorderLayout.CENTER);

        // Footer Navigation Buttons
        JPanel footer = setFooter();
        add(footer, BorderLayout.SOUTH);

        // Setup Action Listeners
        setupActionListeners();
    }

    private JPanel setAccountInfo() {
        JPanel accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User Information
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        usernameField.setEditable(false);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        JTextField accountTypeField = new JTextField(15);
        accountTypeField.setEditable(false);

        // Retrieve and Display User Information
        try {
            String line = bufferedReader.readLine();
            usernameField.setText(line);
            if (line != null) {
                line = bufferedReader.readLine();
                String accountType = "1".equals(line) ? "private" : "public";
                accountTypeField.setText(accountType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add User Info to Account Info Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        accountInfoPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        accountInfoPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        accountInfoPanel.add(accountTypeLabel, gbc);

        gbc.gridx = 1;
        accountInfoPanel.add(accountTypeField, gbc);

        // Profile Actions Section
        profileButton = new JButton("Edit Profile");
        settingButton = new JButton("Settings");

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Add spacing between buttons
        gbc.fill = GridBagConstraints.NONE; // Prevent stretching
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        gbc.gridy = 0;
        buttonPanel.add(profileButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(settingButton, gbc);

        // Combine Info Panel and Button Panel (Side by Side)
        JPanel accountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAccount = new GridBagConstraints();
        gbcAccount.insets = new Insets(10, 10, 10, 10);
        gbcAccount.fill = GridBagConstraints.BOTH;

        // Add Account Info Panel on the left
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 0;
        gbcAccount.weightx = 1; // Allow resizing
        accountPanel.add(buttonPanel, gbcAccount);

        // Add Button Panel on the right
        gbcAccount.gridx = 1;
        gbcAccount.weightx = 0; // Fix width
        accountPanel.add(accountInfoPanel, gbcAccount);
        return accountPanel;
    }

    private JPanel setPeople(int category) {
        JPanel peoplePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Add Follower Label on the left
        JTextPane peopleTextPane = new JTextPane();
        peopleTextPane.setEditable(false);
        if (category == 1) {
            peopleTextPane.setText("Followers");
        } else if (category == 2) {
            peopleTextPane.setText("Following");
        } else if (category == 3) {
            peopleTextPane.setText("Blocked");
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Allow resizing
        peoplePanel.add(peopleTextPane, gbc);

        JTextPane peopleStatus = new JTextPane();
        peopleStatus.setEditable(false);
        JPanel peopleButtonPanel = new JPanel(new GridLayout(0, 1, 0, 0));

        JScrollPane scrollPane = new JScrollPane(peopleButtonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setMinimumSize(new Dimension(300, 50)); // Width: 300, Height: 200

        // Load data in a separate thread
        new Thread(() -> {
            try {
                String peopleValidity = bufferedReader.readLine();
                if (!"[EMPTY]".equals(peopleValidity)) {
                    if (category == 1) {
                        peopleStatus.setText("You have followers!");
                    } else if (category == 2) {
                        peopleStatus.setText("You have following!");
                    } else if (category == 3) {
                        peopleStatus.setText("You have blocked!");
                    }
                    SwingUtilities.invokeLater(() -> peopleButtonPanel.add(peopleStatus));

                    ArrayList<String> buttonNames = UserPageClient.readAndPrint(bufferedReader);
                    for (String buttonName : buttonNames) {
                        JButton button = new JButton(buttonName);

                        button.addActionListener(e -> {
                            System.out.println("HELLO");
                            UserPageClient.write("2", bufferedWriter);
                            UserPageClient.write(buttonName, bufferedWriter);
                            pageManager.lazyLoadPage("other", () -> new OtherProfilePage(pageManager, bufferedWriter, bufferedReader, buttonName));
                            pageManager.removePage("user");
                        });

                        SwingUtilities.invokeLater(() -> {
                            peopleButtonPanel.add(button);
                            peopleButtonPanel.revalidate();
                            peopleButtonPanel.repaint();
                        });
                    }
                } else {
                    if (category == 1) {
                        peopleStatus.setText("You have no followers!");
                    } else if (category == 2) {
                        peopleStatus.setText("You have no following!");
                    } else if (category == 3) {
                        peopleStatus.setText("You have no blocked!");
                    }
                    SwingUtilities.invokeLater(() -> peopleButtonPanel.add(peopleStatus));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        gbc.gridx = 1;
        gbc.weightx = 0; // Fix width
        peoplePanel.add(scrollPane, gbc);

        return peoplePanel;
    }

    private JPanel setFooter() {
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        nextButton = new JButton("Next");

        navigationPanel.add(backButton);
        navigationPanel.add(logoutButton);
        navigationPanel.add(nextButton);
        return navigationPanel;
    }

    private void setupActionListeners() {

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: IMPLEMENT FEATURES LATER
            }
        });

        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: IMPLEMENT FEATURES LATER
            }
        });

        logoutButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               UserPageClient.write("6", bufferedWriter);
               pageManager.showPage("welcome");
               pageManager.removePage("user");
           }
        });

        backButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               UserPageClient.write("5", bufferedWriter);
               pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
               pageManager.removePage("user");
           }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: IMPLEMENT FEATURES LATER
            }
        });
    }

    /*
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
                write("1", bw);
                System.out.print("What is your image file path: ");
                String path = scanner.nextLine();
                write(path, bw);
                try {
                    if (br.readLine().equals("SAVE")) {
                        System.out.println("Set image successfully");
                    } else {
                        System.out.println("Set image failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            } else if (input.equals("5")) {
                write("5", bw);
                FeedPageClient.feedPage(scanner, br, bw, socket);
                break;
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
     */
}