package uiPage;

import clientPageOperation.UserPageClient;
import object.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class UserProfilePage extends JPanel {
    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private int targetWidth = 50;  // Set your desired width
    private int targetHeight = 50; // Set your desired height
    private BufferedImage image;
    private JButton profileButton, settingButton, backButton, nextButton, logoutButton, feedButton;

    public UserProfilePage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.pageManager = pageManager;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;

        setLayout(new BorderLayout());

        createImagePanel();
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

        setupActionListeners();
    }

    private void createImagePanel() {
        // Run the image loading task on a new thread
        new Thread(() -> {
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
        }).start();
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
                            pageManager.lazyLoadPage(buttonName, () -> new OtherProfilePage(pageManager, bufferedWriter, bufferedReader, buttonName));
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
        feedButton = new JButton("Feed");
        nextButton = new JButton("Next");
        footer.add(backButton);
        footer.add(feedButton);
        footer.add(nextButton);
        return footer;
    }

    private void setupActionListeners() {

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(profileButton.getParent()); // Use parent component for context

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Save the image locally
                    saveImageAsNewFile(selectedFile);

                    // Send the file path to the server
                    try {
                        UserPageClient.write("1", bufferedWriter);

                        // Send the absolute path of the selected file
                        String path = selectedFile.getAbsolutePath();
                        UserPageClient.write(path, bufferedWriter);

                        // Read server response
                        String response = bufferedReader.readLine();
                        if ("SAVE".equals(response)) {
                            System.out.println("Set image successfully");
                        } else {
                            System.out.println("Set image failed");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a modal dialog for account settings
                JDialog settingsDialog = new JDialog((Frame) null, "Account Settings", true);
                settingsDialog.setLayout(new BorderLayout(10, 10));
                settingsDialog.setSize(400, 200);
                settingsDialog.setLocationRelativeTo(null);

                // Create a panel to hold the fields and buttons
                JPanel settingsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Create components
                JTextField usernameField = new JTextField(15);
                JPasswordField passwordField = new JPasswordField(15);
                JButton saveButton = new JButton("Save");
                JButton logoutButton = new JButton("Logout");

                // Add components to the panel
                settingsPanel.add(new JLabel("Username"));
                settingsPanel.add(usernameField);
                settingsPanel.add(new JLabel("Password"));
                settingsPanel.add(passwordField);

                // Create a button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
                buttonPanel.add(saveButton);
                buttonPanel.add(logoutButton);

                // Add panels to the dialog
                settingsDialog.add(settingsPanel, BorderLayout.CENTER);
                settingsDialog.add(buttonPanel, BorderLayout.SOUTH);

                /*
                // Load current account details
                try {
                    UserPageClient.write("getAccountInfo", bufferedWriter);
                    String currentUsername = bufferedReader.readLine();
                    usernameField.setText(currentUsername);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(settingsDialog, "Failed to load account details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                */

                /*
                // Save button action listener
                saveButton.addActionListener(ev -> {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(settingsDialog, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        UserPageClient.write("updateAccount", bufferedWriter);
                        UserPageClient.write(username, bufferedWriter);
                        UserPageClient.write(password, bufferedWriter);

                        String response = bufferedReader.readLine();
                        if ("success".equalsIgnoreCase(response)) {
                            JOptionPane.showMessageDialog(settingsDialog, "Settings updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            settingsDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(settingsDialog, "Failed to update settings. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(settingsDialog, "Error while updating settings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                */

                // Logout button action listener
                logoutButton.addActionListener(ev -> {
                    UserPageClient.write("6", bufferedWriter);
                    pageManager.lazyLoadPage("welcome", () -> new WelcomePage(pageManager, bufferedWriter, bufferedReader));
                    settingsDialog.dispose();
                    JOptionPane.showMessageDialog(null, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                });

                // Display the dialog
                settingsDialog.setVisible(true);
            }
        });

        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("5", bufferedWriter);
                pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
/*
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.goBack(); //REVISE
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.goForward(); //REVISE
            }
        });

 */
    }

    private void saveImageAsNewFile(File sourceFile) throws Exception {
        // Destination file (change this path as needed)

        String fileName = sourceFile.getName();
        if (!fileName.endsWith(".png")) {
            throw new Exception("Error");
        }

        File destinationFile = new File("Sample Test Folder/" + sourceFile.getName());

        // Ensure the destination directory exists
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

            // Copy file data
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Update status label on success
            JOptionPane.showMessageDialog(null, "File uploaded", "Notification", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            // Handle errors
            JOptionPane.showMessageDialog(null, "Uploading fail", "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}