package uiPage;

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
        System.out.println("This is user profile page");
        this.pageManager = pageManager;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        createImagePanel();
        JPanel accountPanel = setAccountInfo();
        JPanel followerPanel = setPeople(1, "Follower");
        JPanel followingPanel = setPeople(2, "Following");
        JPanel blockedPanel = setPeople(3, "Blocked");

        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        mainPanel.setBackground(Color.WHITE);
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
                System.out.println("read1: " + imageName);
                if (imageName == null || imageName.isEmpty()) {
                    throw new IllegalStateException("Image name is missing or invalid");
                }

                // Load image from file
                image = ImageIO.read(new File("./Sample Test Folder/" + imageName + ".png"));

                // Scale the image
                Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

                // Update the profile button on the EDT
                SwingUtilities.invokeLater(() -> {
                    profileButton.setIcon(new ImageIcon(newImage));
                    profileButton.revalidate(); // Ensure layout updates properly
                    profileButton.repaint();    // Force the button to redraw
                });

            } catch (IOException e) {
                e.printStackTrace();

                // Handle missing or error scenarios by setting a placeholder icon
                try {
                    BufferedImage img = ImageIO.read(new File("./Sample Test Folder/I_0000.png"));
                    Image newImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> {
                        profileButton.setIcon(new ImageIcon(img));
                        profileButton.revalidate();
                        profileButton.repaint();
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private JPanel setAccountInfo() {
        JPanel accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBackground(Color.WHITE);
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

        usernameLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        usernameField.setFont(new Font("Roboto", Font.BOLD, 14));
        accountTypeField.setFont(new Font("Roboto", Font.BOLD, 14));
        accountTypeLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        // Retrieve and Display User Information
        try {
            String line = bufferedReader.readLine();
            System.out.println("read2: " + line);
            usernameField.setText(line);
            if (line != null) {
                line = bufferedReader.readLine();
                System.out.println("read3: " + line);
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
        profileButton = new JButton();
        settingButton = new JButton("Settings");
        settingButton.setFont(new Font("Roboto", Font.BOLD, 14));


        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(2, 0, 2, 0); // Narrower gap between buttons        gbc.fill = GridBagConstraints.NONE; // Prevent stretching
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        gbc.gridy = 0;
        buttonPanel.add(profileButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(settingButton, gbc);

        // Combine Info Panel and Button Panel (Side by Side)
        JPanel accountPanel = new JPanel(new GridBagLayout());
        accountPanel.setBackground(Color.WHITE);
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
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(label));

        // Create the button panel for the list of people
        JPanel peopleButtonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        peopleButtonPanel.setBackground(Color.WHITE);
        JScrollPane scrollPanel = new JScrollPane(peopleButtonPanel);
        scrollPanel.setBackground(Color.WHITE);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setPreferredSize(new Dimension(400, 300));
        scrollPanel.setMinimumSize(new Dimension(300, 50));

        // Status area for showing messages
        JLabel statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
        statusLabel.setBackground(Color.WHITE);
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(scrollPanel, BorderLayout.CENTER);

        // Load data in a separate thread
        Thread loadingThread = new Thread(() -> {
            try {
                String peopleValidity = bufferedReader.readLine();
                System.out.println("read4: " + peopleValidity);
                if (!"[EMPTY]".equals(peopleValidity)) {
                    SwingUtilities.invokeLater(() -> statusLabel.setText(""));

                    // Populate the button panel with user buttons
                    ArrayList<String> buttonNames = Writer.readAndPrint(bufferedReader);

                    for (String buttonName : buttonNames) {
                        RoundedButton button = new RoundedButton(buttonName, 18);
                        button.setFont(new Font("Roboto", Font.BOLD, 14));
                        button.setBackground(new Color(230, 230, 230));
                        // button.setBorder(BorderFactory.createCompoundBorder(
                        //     BorderFactory.createLineBorder(Color.BLACK),
                        //     BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
                        //     ));
                        button.addActionListener(e -> {
                            Writer.write("2", bufferedWriter);
                            System.out.println("write: " + "2");
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
        });
        loadingThread.start();

        try {
            loadingThread.join(); // makes sure this thread and its caller end at the same time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(Color.WHITE);
//        backButton = new JButton("Back");
        feedButton = new JButton("Feed");
//        nextButton = new JButton("Next");
//        footer.add(backButton);
        footer.add(feedButton);
//        footer.add(nextButton);
        return footer;
    }

    private void setupActionListeners() {

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Notify server of file upload action
                    Writer.write("1", bufferedWriter);
                    System.out.println("write: " + "1");

                    // Open file chooser dialog
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = fileChooser.showOpenDialog(profileButton.getParent());

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();

                        // Send file path to server
                        Writer.write(path, bufferedWriter);
                        System.out.println("write: " + path);

                        // Read response from server
                        String response = bufferedReader.readLine();
                        System.out.println("read5: " + response);
                        if ("SAVE".equals(response)) {
                            JOptionPane.showMessageDialog(null, "File uploaded successfully!", "Notification", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "File upload failed.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException ex) {
                    // Show error dialog for IOException
                    JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    createImagePanel();
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
                    Writer.write("6", bufferedWriter);
                    System.out.println("write: " + "6");
                    pageManager.lazyLoadPage("welcome", () -> new WelcomePage(pageManager, bufferedWriter, bufferedReader));
                    pageManager.removePage("user");
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
                Writer.write("5", bufferedWriter);
                System.out.println("write: " + "5");
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
}