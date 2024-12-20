package page;


import common.PageManager;
import common.ProfilePictureDialog;
import common.RoundedButton;
import common.Writer;

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

/**
* Represents the profile page of another user.
* This class extends JPanel and provides a user interface to view and interact with another user's profile.
* It includes methods to set up the account information, followers, following, and relation buttons,
* as well as action listeners for various UI components.
*/
public class OtherProfilePage extends JPanel {

    /**
    * The button used to follow or unfollow the other user.
    */
    private JButton followButton;

    /**
    * The button used to block or unblock the other user.
    */
    private JButton blockButton;

    /**
    * The button used to view the profile picture of the other user.
    */
    private JButton profileButton;

    /**
    * The button used to navigate back to the previous page.
    */
    private JButton backButton;

    /**
    * The button used to navigate to the feed page.
    */
    private JButton feedButton;

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
    * The username of the other user whose profile is being viewed.
    */
    private String otherUsername;

    /**
    * The target width for scaling the profile picture.
    */
    private int targetWidth = 50;

    /**
    * The target height for scaling the profile picture.
    */
    private int targetHeight = 50;

    /**
    * The BufferedImage instance representing the profile picture of the other user.
    */
    private BufferedImage image;

    /**
    * Constructs an OtherProfilePage object.
    * Initializes the page layout, sets up the account information, followers, following, and relation panels,
    * and adds action listeners to the UI components.
    * @param pageManager The PageManager instance to handle page navigation.
    * @param writer The BufferedWriter instance for writing data.
    * @param reader The BufferedReader instance for reading data.
    * @param otherUsername The username of the other user whose profile is being viewed.
    */
    public OtherProfilePage(PageManager pageManager, BufferedWriter writer, BufferedReader reader, String otherUsername) {
        System.out.println("This is other profile page");
        this.pageManager = pageManager;
        this.bufferedWriter = writer;
        this.bufferedReader = reader;
        this.otherUsername = otherUsername;

        setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
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
    /**
    * Asynchronously loads and sets the profile picture of the other user.
    * This method reads the image name from a buffered reader, loads the image,
    * scales it, and updates the profile button's icon on the Event Dispatch Thread (EDT).
    * If an I/O error occurs during image loading, it prints the stack trace.
    */
    private void createImagePanel() {
        // Run the image loading task on a new thread
        Thread loadingThread = new Thread(() -> {
            try {
                // Read image name from BufferedReader
                String imageName = bufferedReader.readLine();
                System.out.println("read: " + imageName);
                if (imageName == null || imageName.isEmpty()) {
                    throw new IllegalStateException("Image name is missing or invalid");
                }

                // Load image from file
                image = ImageIO.read(new File("./SampleTestFolder/" + imageName + ".png"));

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
    /**
    * Sets up and returns the account information panel.
    * This method retrieves and displays the username and account type of the other user.
    * @return The account information panel containing the user's details.
    */
    private JPanel setAccountInfo() {
        Writer.write(otherUsername, bufferedWriter);
        System.out.println("write: " + otherUsername);

        JPanel accountInfoPanel = new JPanel(new GridBagLayout());
        accountInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        accountInfoPanel.setBackground(Color.WHITE);
        
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

        usernameLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        usernameField.setFont(new Font("Roboto", Font.BOLD, 14));
        accountTypeField.setFont(new Font("Roboto", Font.BOLD, 14));
        accountTypeLabel.setFont(new Font("Roboto", Font.BOLD, 14));

        // Retrieve and Display User Information
        try {
            String line = bufferedReader.readLine();
            System.out.println("read: " + line);
            usernameField.setText(line);

            line = bufferedReader.readLine();
            System.out.println("read: " + line);
            String accountType = "1".equals(line) ? "private" : "public";
            accountTypeField.setText(accountType);

            line = bufferedReader.readLine(); // receives "stop", I think
            System.out.println("read: " + line);
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
        buttonPanel.setBackground(Color.WHITE);
        gbc.gridy = 0;
        buttonPanel.add(profileButton, gbc);

        // Combine Info Panel and Button Panel (Side by Side)
        JPanel accountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAccount = new GridBagConstraints();
        gbcAccount.insets = new Insets(5, 5, 5, 5); // Reduced gap between panels
        gbcAccount.fill = GridBagConstraints.BOTH;
        accountPanel.setBackground(Color.WHITE);

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
    /**
    * Sets up and returns a panel displaying a list of people (followers or following).
    * This method retrieves the list of people from the server, creates buttons for each person,
    * and adds them to a scrollable panel.
    * @param category The category of people to display (1 for followers, 2 for following).
    * @param label The label for the panel (e.g., "Follower" or "Following").
    * @return The panel containing the list of people.
    */
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
        panel.setBackground(Color.WHITE);
        peopleButtonPanel.setBackground(Color.WHITE);
        
        // Status area for showing messages
        JLabel statusLabel = new JLabel("Loading...", SwingConstants.CENTER);
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(scrollPanel, BorderLayout.CENTER);

        // Load data in a separate thread
        Thread loadingThread = new Thread(() -> {
            try {
                String peopleValidity = bufferedReader.readLine();
                System.out.println("read: " + peopleValidity);
                if ("look".equals(peopleValidity)) {
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
                            Writer.write("3", bufferedWriter);
                            System.out.println("write: " + "3");
                            Writer.write(buttonName, bufferedWriter);
                            System.out.println("write: " + buttonName);
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
    /**
    * Sets up and returns the footer panel.
    * This method creates a panel with buttons for navigating to the feed.
    * @return The footer panel containing navigation buttons.
    */
    private JPanel setFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        backButton = new JButton("Back");
        feedButton = new RoundedButton("Feed", 18);
//        nextButton = new JButton("Next");
        footer.setBackground(Color.WHITE);
//        footer.add(backButton);
        footer.add(feedButton);
//        footer.add(nextButton);
        return footer;
    }
    /**
    * Sets up and returns the relation panel.
    * This method retrieves the relationship status (follow/unfollow, block/unblock) from the server
    * and creates buttons accordingly.
    * @return The relation panel containing follow and block buttons.
    */
    private JPanel setRelation() {
        JPanel relationPanel = new JPanel(new GridBagLayout());
        relationPanel.setBackground(Color.WHITE);
        try {
            // Check if following otherUser, then create button
            Writer.write("4", bufferedWriter);
            System.out.println("write: " + "4");
            String followResponse = bufferedReader.readLine();
            System.out.println("read: " + followResponse);
            followButton = new RoundedButton(followResponse, 18);

            // Do the same for block button
            Writer.write("6", bufferedWriter);
            System.out.println("write: " + "6");
            String blockResponse = bufferedReader.readLine();
            System.out.println("read: " + blockResponse);
            blockButton = new RoundedButton(blockResponse, 18);

            relationPanel.add(followButton);
            relationPanel.add(blockButton);
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relationPanel;
    }
    /**
    * Sets up action listeners for various UI components.
    * This method assigns action listeners to buttons and other UI elements
    * to handle user interactions such as navigating to the feed, following/unfollowing,
    * blocking/unblocking, and viewing the profile picture.
 */
    private void setupActionListeners() {
        feedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("5", bufferedWriter);
                System.out.println("write: " + "5");
                pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
        /**
        * Action listener for the follow button.
        * Toggles the follow status of the other user when the button is clicked.
        */
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("1", bufferedWriter);
                    System.out.println("write: " + "1");
                    
                    String response = bufferedReader.readLine();
                    System.out.println("read: " + response);
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
        /**
        * Action listener for the block button.
        * Toggles the block status of the other user when the button is clicked.
        */
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("2", bufferedWriter);
                    System.out.println("write: " + "2");
                    String response = bufferedReader.readLine();
                    System.out.println("read: " + response);
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
        /**
        * Action listener for the profile button.
        * Displays the profile picture of the other user in a dialog when the button is clicked.
        */
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (image != null) {
                    // Create and show the profile picture dialog
                    SwingUtilities.invokeLater(() -> {
                        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(OtherProfilePage.this);
                        ProfilePictureDialog dialog = new ProfilePictureDialog(parentFrame, image, otherUsername);
                        dialog.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Profile picture not available", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
