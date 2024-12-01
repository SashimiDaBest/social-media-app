package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserProfilePage extends JPanel {
    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private JButton profileButton, settingButton, backButton, nextButton, logoutButton;

    public UserProfilePage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.pageManager = pageManager;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;

        setLayout(new BorderLayout());

        JPanel accountPanel = setAccountInfo();
        JPanel followerPanel = setPeople(1, "Follower");
        JPanel followingPanel = setPeople(2, "Following");
        JPanel blockedPanel = setPeople(3, "Blocked");

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        mainPanel.add(accountPanel);
        mainPanel.add(followerPanel);
        mainPanel.add(followingPanel);
        mainPanel.add(blockedPanel);
        add(mainPanel, BorderLayout.CENTER);

        add(createFooter(), BorderLayout.SOUTH);

//        setupActionListeners();
    }

    private JPanel setAccountInfo() {
        JPanel accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Reduced gap
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension fixedSize = new Dimension(150, 25);

        // User Information
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        usernameField.setEditable(false);
        usernameField.setMinimumSize(fixedSize);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        JTextField accountTypeField = new JTextField(15);
        accountTypeField.setEditable(false);
        accountTypeField.setMinimumSize(fixedSize);

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
        gbcButton.insets = new Insets(2, 0, 2, 0); // Narrower gap between buttons        gbc.fill = GridBagConstraints.NONE; // Prevent stretching
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        gbc.gridy = 0;
        buttonPanel.add(profileButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(settingButton, gbc);

        // Combine Info Panel and Button Panel (Side by Side)
        JPanel accountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAccount = new GridBagConstraints();
        gbcAccount.insets = new Insets(5, 5, 5, 5); // Reduced gap between panels
        gbcAccount.fill = GridBagConstraints.BOTH;

        // Add Account Info Panel on the left
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 0;
        gbcAccount.weightx = 0.3; // Allocate less width for button panel
        accountPanel.add(buttonPanel, gbcAccount);

        // Add Button Panel on the right
        gbcAccount.gridx = 1;
        gbcAccount.weightx = 0.7; // Allocate more width for account info
        accountPanel.add(accountInfoPanel, gbcAccount);
        return accountPanel;
    }

    private JPanel setPeople(int category, String label) {
        // Create the main panel with a border title
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(label));

        // Create the button panel for the list of people
        JPanel peopleButtonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPanel = new JScrollPane(peopleButtonPanel);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setPreferredSize(new Dimension(400, 300));
        scrollPanel.setMinimumSize(new Dimension(300, 50));

        // Status area for showing messages
        JLabel statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(scrollPanel, BorderLayout.CENTER);

        // Load data in a separate thread
        new Thread(() -> {
            try {
                String peopleValidity = bufferedReader.readLine();
                if (!"[EMPTY]".equals(peopleValidity)) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText(""));

                    // Populate the button panel with user buttons
                    ArrayList<String> buttonNames = UserPageClient.readAndPrint(bufferedReader);
                    for (String buttonName : buttonNames) {
                        JButton button = new JButton(buttonName);

                        button.addActionListener(e -> {
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
                    // Update the status message based on the category
                    String noDataMessage = "";
                    switch (category) {
                        case 1:
                            noDataMessage = "You have no followers!";
                            break;
                        case 2:
                            noDataMessage = "You are not following anyone!";
                            break;
                        case 3:
                            noDataMessage = "You have not blocked any users!";
                            break;
                        default:
                            noDataMessage = "No data available.";
                            break;
                    }

                    String finalNoDataMessage = noDataMessage;
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText(finalNoDataMessage);
                        peopleButtonPanel.revalidate();
                        peopleButtonPanel.repaint();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        footer.add(backButton);
        footer.add(nextButton);
        return footer;
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