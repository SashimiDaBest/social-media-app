package uiPage;

import javax.swing.*;

import clientPageOperation.UserPageClient;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OtherProfilePage  extends JPanel{

    private int count = 0;
    private JButton followButton;
    private JButton blockButton;

    private JButton profileButton;
    private JButton backButton;
    private JButton nextButton;

    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String otherUsername;

    public OtherProfilePage(PageManager pageManager, BufferedWriter writer, BufferedReader reader, String otherUsername) {
        this.pageManager = pageManager;
        this.bufferedWriter = writer;
        this.bufferedReader = reader;
        this.otherUsername = otherUsername;
        count++;

        setLayout(new BorderLayout());

        JPanel accountPanel = setAccountInfo();
//        JPanel relationPanel = new JPanel();
        JPanel followerPanel = setPeople(1);
        JPanel followingPanel = setPeople(2);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        mainPanel.add(accountPanel);
//        mainPanel.add(relationPanel);
        mainPanel.add(followerPanel);
        mainPanel.add(followingPanel);
        add(mainPanel, BorderLayout.CENTER);

        JPanel footer = setFooter();
        add(footer, BorderLayout.SOUTH);

        setupActionListeners();
    }

    private JPanel setAccountInfo() {
        UserPageClient.write(otherUsername, bufferedWriter);

        JPanel accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Reduced gap
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension fixedSize = new Dimension(150, 25);

        // User Information
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setEditable(false);
        usernameField.setMinimumSize(fixedSize);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        JTextField accountTypeField = new JTextField(20);
        accountTypeField.setEditable(false);
        accountTypeField.setMinimumSize(fixedSize);

        // Retrieve and Display User Information
        try {
            String line = bufferedReader.readLine();
            System.out.println(line);
            usernameField.setText(line);

            line = bufferedReader.readLine();
            System.out.println(line);
            String accountType = "1".equals(line) ? "private" : "public";
            accountTypeField.setText(accountType);

            line  = bufferedReader.readLine();
            System.out.println(line);
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
        profileButton = new JButton("View Profile");

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(2, 0, 2, 0); // Narrower gap between buttons
        gbcButton.fill = GridBagConstraints.NONE; // Prevent stretching
        gbcButton.anchor = GridBagConstraints.CENTER; // Center the buttons

        gbc.gridy = 0;
        buttonPanel.add(profileButton, gbc);

        // Combine Info Panel and Button Panel (Side by Side)
        JPanel accountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAccount = new GridBagConstraints();
        gbcAccount.insets = new Insets(5, 5, 5, 5); // Reduced gap between panels
        gbcAccount.fill = GridBagConstraints.BOTH;

        // Add Button Panel on the left
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 0;
        gbcAccount.weightx = 0.3; // Allocate less width for button panel
        accountPanel.add(buttonPanel, gbcAccount);

        // Add Account Info Panel on the right
        gbcAccount.gridx = 1;
        gbcAccount.weightx = 0.7; // Allocate more width for account info
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
                if (!"[EMPTY]".equals("")) {
                    if (category == 1) {
                        peopleStatus.setText("You have followers!");
                    } else if (category == 2) {
                        peopleStatus.setText("You have following!");
                    }
                    SwingUtilities.invokeLater(() -> peopleButtonPanel.add(peopleStatus));

                    ArrayList<String> buttonNames = UserPageClient.readAndPrint(bufferedReader);
                    for (String buttonName : buttonNames) {
                        JButton button = new JButton(buttonName);

                        button.addActionListener(e -> {
                            UserPageClient.write("3", bufferedWriter);
                            UserPageClient.write(buttonName, bufferedWriter);
                            // TODO: do page loading...
                        });

                        SwingUtilities.invokeLater(() -> {
                            peopleButtonPanel.add(button);
                            peopleButtonPanel.revalidate();
                            peopleButtonPanel.repaint();
                        });
                    }
                } else if (peopleValidity.equals("[EMPTY]")) {
                    if (category == 1) {
                        peopleStatus.setText("You have no followers!");
                    } else if (category == 2) {
                        peopleStatus.setText("You have no following!");
                    }
                } else if (peopleValidity.equals("message")) {
                    peopleStatus.setText("You have no permission to view!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        /*
//        UserPageClient.write("3", bufferedWriter);
//        if (canView) {
//            System.out.print("Do you want to view another user? (Y/N): ");
//            String input2 = scanner.nextLine();
//            if (input2.equals("Y")) {
//                UserPageClient.write("CHANGE", bw);
//                System.out.print("Other Username: ");
//                String other = scanner.nextLine();
//                otherPage(scanner, other, br, bw, socket);
//                break;
//            } else {
//                UserPageClient.write("", bw);
//            }
//        }
         */
        gbc.gridx = 1;
        gbc.weightx = 0; // Fix width
        peoplePanel.add(scrollPane, gbc);

        return peoplePanel;
    }

    private JPanel setFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButton = new JButton("Back");
        nextButton = new JButton("Next");

        footer.add(backButton);
        footer.add(nextButton);
        return footer;
    }

    private void setupActionListeners() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("5", bufferedWriter);
                pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
                pageManager.removePage("other");
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
    public static void otherPage(Scanner scanner, String otherUsername, BufferedReader br, BufferedWriter bw,
                                 Socket socket) {
        try {
            // Send the other username to the server
            System.out.println("OTHER USERNAME: " + otherUsername);
            bw.write(otherUsername);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Main loop for handling user options
        while (true) {
            // Display menu options
            System.out.println("Welcome to the Other Page\n" +
                    "OTHER USERNAME: " + otherUsername + "\n" +
                    "1 - Follow/Unfollow Other\n" +
                    "2 - Block/Unblock Other\n" +
                    "3 - View Follower\n" +
                    "4 - View Following\n" +
                    "5 - Go Back to Feed View\n" +
                    "6 - Quit\n" +
                    "Input: ");
            String input = scanner.nextLine();

            if (input.equals("1")) {
                UserPageClient.write("1", bw);
                try {
                    System.out.println(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (input.equals("2")) {
                UserPageClient.write("2", bw);
                try {
                    System.out.println(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (input.equals("3")) {
            } else if (input.equals("4")) {
                UserPageClient.write("4", bw);
                boolean canView = false;
                try {
                    String line = br.readLine();
                    if (line.equals("")) {
                        canView = true;
                    } else if (line.equals("[EMPTY]")) {
                        System.out.println("User has no following!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UserPageClient.readAndPrint(br);
                if (canView) {
                    System.out.print("Do you want to view another user? (Y/N): ");
                    String input2 = scanner.nextLine();
                    if (input2.equals("Y")) {
                        UserPageClient.write("CHANGE", bw);
                        System.out.print("Other Username: ");
                        String other = scanner.nextLine();
                        otherPage(scanner, other, br, bw, socket);
                        break;
                    } else {
                        //UserPageClient.write("", bw);
                    }
                }
            } else if (input.equals("5")) {
                UserPageClient.write("5", bw);
//                TODO: IMPLEMENT
//                FeedPageClient.feedPage(scanner, br, bw, socket);
//                break;
            } else if (input.equals("6")) {
                UserPageClient.write("6", bw);
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
