package page;

import common.PageManager;
import common.RoundedButton;
import common.Writer;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Represents the user profile page.
 * This class extends JPanel and provides a user interface to view and interact with the user's own profile.
 * It includes methods to set up the account information, followers, following, blocked users, and action listeners for various UI components.
 */
public class UserProfilePage extends JPanel {
    /**
     * The PageManager instance used for managing page navigation.
     */
    private PageManager pageManager;

    /**
     * The BufferedReader instance used for reading data from the server.
     */
    private BufferedReader bufferedReader;

    /**
     * The BufferedWriter instance used for writing data to the server.
     */
    private BufferedWriter bufferedWriter;

    /**
     * The target width for scaling the profile picture.
     */
    private int targetWidth = 50;

    /**
     * The target height for scaling the profile picture.
     */
    private int targetHeight = 50;

    /**
     * The BufferedImage instance representing the profile picture of the user.
     */
    private BufferedImage image;

    /**
     * The button used to view the profile picture of the user.
     */
    private JButton profileButton;

    /**
     * The button used to open the account settings.
     */
    private JButton settingButton;

    /**
     * The button used to navigate back to the previous page.
     */
    private JButton backButton;

    /**
     * The button used to navigate to the next page.
     */
    private JButton nextButton;

    /**
     * The button used to log out the user.
     */
    private JButton logoutButton;

    /**
     * The button used to navigate to the feed page.
     */
    private JButton feedButton;
    /**
    * Constructs a UserProfilePage object.
    * Initializes the page layout, sets up the account information, followers, following, blocked users,
    * and adds action listeners to the UI components.
    * @param pageManager The PageManager instance to handle page navigation.
    * @param bufferedWriter The BufferedWriter instance for writing data.
    * @param bufferedReader The BufferedReader instance for reading data.
    */
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
    /**
    * Asynchronously loads and sets the user's profile picture.
    * This method reads the image name from a buffered reader, loads the image,
    * scales it, and updates the profile button's icon on the Event Dispatch Thread (EDT).
    * If an I/O error occurs during image loading, it prints the stack trace.
    */
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
                image = ImageIO.read(new File("./SampleTestFolder/" + imageName + ".png"));

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
                    BufferedImage img = ImageIO.read(new File("SampleTestFolder/I_0000.png"));
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
    /**
     * Sets up and returns the account information panel.
     * This method retrieves and displays the username and account type of the user.
     * @return The account information panel containing the user's details.
     */
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
        usernameField.setBackground(new Color(230, 230, 230));

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
    /**
     * Sets up and returns a panel displaying a list of people (followers, following, or blocked users).
     * This method retrieves the list of people from the server, creates buttons for each person,
     * and adds them to a scrollable panel.
     * @param category The category of people to display (1 for followers, 2 for following, 3 for blocked users).
     * @param label The label for the panel (e.g., "Follower", "Following", or "Blocked").
     * @return The panel containing the list of people.
     */
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
                    ArrayList<String> buttonNames = common.Writer.readAndPrint(bufferedReader);

                    for (String buttonName : buttonNames) {
                        RoundedButton button = new RoundedButton(buttonName, 18);
                        button.setFont(new Font("Roboto", Font.BOLD, 14));
                        button.setBackground(new Color(230, 230, 230));
                        // button.setBorder(BorderFactory.createCompoundBorder(
                        //     BorderFactory.createLineBorder(Color.BLACK),
                        //     BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
                        //     ));
                        button.addActionListener(e -> {
                            common.Writer.write("2", bufferedWriter);
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
    /**
     * Sets up and returns the footer panel.
     * This method creates a panel with buttons for navigating to the feed.
     * @return The footer panel containing navigation buttons.
     */
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(Color.WHITE);
//        backButton = new JButton("Back");
        feedButton = new RoundedButton("Feed", 18);
//        nextButton = new JButton("Next");
//        footer.add(backButton);
        footer.add(feedButton);
//        footer.add(nextButton);
        return footer;
    }
    /**
     * Sets up action listeners for various UI components.
     * This method assigns action listeners to buttons and other UI elements
     * to handle user interactions such as uploading a profile picture, navigating to the feed,
     * opening account settings, and logging out.
     */
    private void setupActionListeners() {
        /**
         * Action listener for the profile button.
         * Opens a file chooser dialog to select a profile picture, uploads the selected file to the server,
         * and updates the profile picture on the user interface.
         * If the file upload is successful, a success message is displayed; otherwise, an error message is shown.
         */
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Notify server of file upload action
                    common.Writer.write("1", bufferedWriter);
                    System.out.println("write: " + "1");

                    // Open file chooser dialog
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = fileChooser.showOpenDialog(profileButton.getParent());

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();

                        // Send file path to server
                        common.Writer.write(path, bufferedWriter);
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
        /**
         * Action listener for the setting button.
         * Opens a modal dialog for account settings, allowing the user to log out.
         * The dialog contains fields for username and password, and buttons for saving changes and logging out.
         */
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
                passwordField.setBackground(new Color(230, 230, 230));
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

                // Logout button action listener
                logoutButton.addActionListener(ev -> {
                    common.Writer.write("6", bufferedWriter);
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
        /**
         * Action listener for the feed button.
         * Navigates to the feed page when the button is clicked.
         */
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("5", bufferedWriter);
                System.out.println("write: " + "5");
                pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
    }
}