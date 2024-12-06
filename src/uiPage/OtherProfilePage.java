package uiPage;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OtherProfilePage extends JPanel {

    private JButton followButton;
    private JButton blockButton;

    private JButton profileButton;
    private JButton backButton;
    private JButton feedButton;

    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String otherUsername;

    private int targetWidth = 50;  // Set your desired width
    private int targetHeight = 50; // Set your desired height
    private BufferedImage image;

    public OtherProfilePage(PageManager pageManager, BufferedWriter writer, BufferedReader reader, String otherUsername) {
        this.pageManager = pageManager;
        this.bufferedWriter = writer;
        this.bufferedReader = reader;
        this.otherUsername = otherUsername;

        setLayout(new BorderLayout());

        JPanel accountPanel = setAccountInfo();
        createImagePanel();
        JPanel followerPanel = setPeople(1, "Follower");
        JPanel followingPanel = setPeople(2, "Following");
        JPanel relationPanel = setRelation();

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        mainPanel.add(accountPanel);
        mainPanel.add(relationPanel);
        mainPanel.add(followerPanel);
        mainPanel.add(followingPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footer = setFooter();
        add(footer, BorderLayout.SOUTH);

        setupActionListeners();
    }

    private void createImagePanel() {
        // Run the image loading task on a new thread
        Thread loadingThread = new Thread(() -> {
            try {
                // Read image name from BufferedReader
                String imageName = bufferedReader.readLine();
                if (imageName == null || imageName.isEmpty()) {
                    throw new IllegalStateException("Image name is missing or invalid");
                }

                // Load image from file
                image = ImageIO.read(new File("./Sample Test Folder/" + imageName + ".png"));

                // Scale the image
                Image newImage = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);

                // Create an ImageIcon
                ImageIcon imageIcon = new ImageIcon(newImage);

                // Update the profile button on the EDT
                SwingUtilities.invokeLater(() -> profileButton.setIcon(imageIcon));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadingThread.start();

        try {   
            loadingThread.join(); // makes sure this thread and its caller end at the same time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private JPanel setAccountInfo() {
        Writer.write(otherUsername, bufferedWriter);

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
            usernameField.setText(line);

            line = bufferedReader.readLine();
            String accountType = "1".equals(line) ? "private" : "public";
            accountTypeField.setText(accountType);

            line = bufferedReader.readLine(); // receives "stop", I think
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
        profileButton = new JButton();

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
        Thread loadingThread = new Thread(() -> {
            try {
                String peopleValidity = bufferedReader.readLine();
                if ("look".equals(peopleValidity)) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText(""));

                    // Populate the button panel with user buttons
                    ArrayList<String> buttonNames = Writer.readAndPrint(bufferedReader);
                    for (String buttonName : buttonNames) {
                        JButton button = new JButton(buttonName);

                        button.addActionListener(e -> {
                            Writer.write("3", bufferedWriter);
                            Writer.write(buttonName, bufferedWriter);
                            pageManager.lazyLoadPage(buttonName, () -> new OtherProfilePage(pageManager, bufferedWriter, bufferedReader, buttonName));
                            pageManager.removePage(otherUsername);
                        });

                        SwingUtilities.invokeLater(() -> {
                            peopleButtonPanel.add(button);
                            peopleButtonPanel.revalidate();
                            peopleButtonPanel.repaint();
                        });
                    }
                } else if ("message".equals(peopleValidity)) {
                    // Update the status message based on the category
                    String noDataMessage = "You have no permission to view";

                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText(noDataMessage);
                        peopleButtonPanel.revalidate();
                        peopleButtonPanel.repaint();
                    });
                    
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
        });
        loadingThread.start();
            
        try {
            loadingThread.join(); // makes sure thread ends with the function call
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel setFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        backButton = new JButton("Back");
        feedButton = new JButton("Feed");
//        nextButton = new JButton("Next");

//        footer.add(backButton);
        footer.add(feedButton);
//        footer.add(nextButton);
        return footer;
    }

    private JPanel setRelation() {
        JPanel relationPanel = new JPanel(new GridBagLayout());

        try {
            // Check if following otherUser, then create button
            Writer.write("4", bufferedWriter);
            String followResponse = bufferedReader.readLine();
            followButton = new JButton(followResponse);

            // Do the same for block button
            Writer.write("6", bufferedWriter);
            String blockResponse = bufferedReader.readLine();
            blockButton = new JButton(blockResponse);

            relationPanel.add(followButton);
            relationPanel.add(blockButton);
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relationPanel;
    }

    private void setupActionListeners() {
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("5", bufferedWriter);
                pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
            }
        });

        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("1", bufferedWriter);
                    
                    String response = bufferedReader.readLine();
                    // System.out.println("Initial response: " + response);
                    // // eat up any leftover invalid responses from server
                    // while (!response.contains("unfollowed") || !response.contains("followed")) {
                    //     System.out.println("Waiting to read response...");
                    //     response = bufferedReader.readLine();
                    //     System.out.println("RESPONSE RECEIVED");
                    // }
                    System.out.println("Real response: " + response);

                    if (!response.contains("unfollowed")) {
                        JOptionPane.showMessageDialog(null, "Followed the user!", "Boiler Gram", JOptionPane.INFORMATION_MESSAGE);
                        followButton.setText("Unfollow");
                    } else {
                        JOptionPane.showMessageDialog(null, "Unfollowed the user!", "Boiler Gram", JOptionPane.INFORMATION_MESSAGE);
                        followButton.setText("Follow");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("2", bufferedWriter);
                    String response = bufferedReader.readLine();
                    if (!response.contains("unblocked")) {
                        JOptionPane.showMessageDialog(null, "Blocked the user!", "Boiler Gram", JOptionPane.INFORMATION_MESSAGE);
                        blockButton.setText("Unblock");
                    } else {
                        JOptionPane.showMessageDialog(null, "Unblocked the user!", "Boiler Gram", JOptionPane.INFORMATION_MESSAGE);
                        blockButton.setText("Block");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
/*
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.printHistory();
                pageManager.goBack();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.goForward();
                pageManager.printHistory();
            }
        });

        blockButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               UserPageClient.write("2", bufferedWriter);
               try {
                   System.out.println(bufferedReader.readLine());
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }
        });

        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("1", bufferedWriter);
                try {
                    System.out.println(bufferedReader.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

 */
    }
}
