package page;

import common.PageManager;
import common.RoundedButton;
import common.Writer;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the feed view page of the application. This page displays chat messages,
 * allows user search and selection, and provides various utility functions such as
 * sending, editing, and deleting messages.
 */
public class FeedViewPage extends JPanel {

    /**
     * Timer for refreshing the chat view.
     */
    private Timer chatRefreshTimer;

    /**
     * Refresh interval for the chat view in milliseconds.
     */
    private final int REFRESH_INTERVAL = 5000; // Refresh every 5 seconds

    /**
     * Maximum number of selected users allowed.
     */
    private static final int MAX_SELECTED_USERS = 8;

    /**
     * Side length of the icons used in the UI.
     */
    private final int iconSideLength = 20;

    /**
     * Button for navigating to the user profile.
     */
    private JButton profileButton;

    /**
     * Button for initiating a user search.
     */
    private JButton searchButton;

    /**
     * Button for deleting the most recently added user from the selection.
     */
    private JButton deleteButton;

    /**
     * Button for clearing all selected users.
     */
    private JButton clearButton;

    /**
     * Button for adding selected users to a chat.
     */
    private JButton addSelectedToChat;

    /**
     * Button for deleting the currently displayed message.
     */
    private JButton deleteTextButton;

    /**
     * Button for refreshing the chat view.
     */
    private JButton refreshButton;

    /**
     * List of chat buttons.
     */
    private ArrayList<JButton> chatButtons;

    /**
     * List of selection buttons.
     */
    private ArrayList<JButton> selectionButtons;

    /**
     * Text field for entering chat messages.
     */
    private JTextField chatField;

    /**
     * Text field for searching users.
     */
    private JTextField searchField;

    /**
     * Button for editing the currently displayed message.
     */
    private JButton editButton;

    /**
     * Button for sending a chat message.
     */
    private JButton sendButton;

    /**
     * Scroll pane for the chat view.
     */
    private JScrollPane chatViewPanel;

    /**
     * Scroll pane for the chat buttons.
     */
    private JScrollPane chatButtonPanel;

    /**
     * Panel for dynamic chat updates.
     */
    private JPanel chatPanel;

    /**
     * Label for displaying chat messages.
     */
    private JLabel messageLabel;

    /**
     * Path for the add selection icon.
     */
    private final String addSelectionIconPath = "SampleTestFolder/addToSelectionIcon.png";

    /**
     * Path for the clear selection icon.
     */
    private final String clearSelectionIconPath = "SampleTestFolder/clearSelectionIcon.png";

    /**
     * Path for the delete message icon.
     */
    private final String deleteMessageIconPath = "SampleTestFolder/deleteMessageIcon.png";

    /**
     * Path for the edit message icon.
     */
    private final String editMessageIconPath = "SampleTestFolder/editMessageIcon.png";

    /**
     * Path for the search icon.
     */
    private final String searchIconPath = "SampleTestFolder/searchIcon.png";

    /**
     * Path for the send message icon.
     */
    private final String sendMessageIconPath = "SampleTestFolder/sendMessageIcon.png";

    /**
     * Path for the delete selection icon.
     */
    private final String deleteSelectionIconPath = "SampleTestFolder/deleteSelectionIcon.png";

    /**
     * Icon for editing messages.
     */
    BufferedImage editMessageIcon;

    /**
     * Icon for deleting messages.
     */
    BufferedImage deleteMessageIcon;

    /**
     * Icon for deleting selections.
     */
    BufferedImage deleteSelectionIcon;

    /**
     * Icon for sending messages.
     */
    BufferedImage sendMessageIcon;

    /**
     * Icon for adding selections.
     */
    BufferedImage addToSelectionIcon;

    /**
     * Icon for clearing selections.
     */
    BufferedImage clearSelectionIcon;

    /**
     * Icon for searching.
     */
    BufferedImage searchIcon;

    /**
     * User profile picture.
     */
    BufferedImage userProfilePic;

    /**
     * List of selected usernames.
     */
    private ArrayList<String> selectedUsers;

    /**
     * BufferedWriter for writing data.
     */
    private BufferedWriter bufferedWriter;

    /**
     * BufferedReader for reading data.
     */
    private BufferedReader bufferedReader;

    /**
     * PageManager for managing page navigation.
     */
    private PageManager pageManager;

    /**
     * Current chat ID.
     */
    private String currentChatID;

    /**
     * Constructs a FeedViewPage with the specified parameters.
     *
     * @param pageManager The PageManager instance to manage page navigation.
     * @param bw          The BufferedWriter instance for writing data.
     * @param br          The BufferedReader instance for reading data.
     */

    public FeedViewPage(PageManager pageManager, BufferedWriter bw, BufferedReader br) {
        System.out.println("This is feed view page");

        this.pageManager = pageManager;
        this.bufferedWriter = bw;
        this.bufferedReader = br;
        this.selectedUsers = new ArrayList<>();
        this.selectionButtons = new ArrayList<>();
        this.chatButtons = new ArrayList<>();
        this.currentChatID = "";

        setLayout(new BorderLayout());

        // Top Panel: Profile and Search
        add(createTopPanel(), BorderLayout.NORTH);

        // Center Panel: Chat view and message panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel chatPanel = new JPanel(new BorderLayout());

        chatViewPanel = createChatPanel();
        chatPanel.add(chatViewPanel, BorderLayout.CENTER);
        chatPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        chatButtonPanel = createChatViewPanel();
        mainPanel.add(chatButtonPanel, BorderLayout.WEST);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        createUtilityIcons();
        createProfilePic();
        setupActionListeners();
    }
    /**
    * Asynchronously loads and sets the user's profile picture.
    * This method reads the image name from a buffered reader, loads the image,
    * scales it, and updates the profile button's icon on the Event Dispatch Thread (EDT).
    * If the image name is missing or invalid, it sets a placeholder icon.
    * If an I/O error occurs during image loading, it also sets a placeholder icon.
    */
    private void createProfilePic() {
        // Run the image loading task on a new thread
        new Thread(() -> {
            try {
                // Read image name from BufferedReader
                Writer.write("image", bufferedWriter);
                String imageName = bufferedReader.readLine();

                System.out.println("read1: " + imageName);
                if (imageName == null || imageName.isEmpty()) {
                    throw new IllegalStateException("Image name is missing or invalid");
                }

                // Load image from file
                userProfilePic = ImageIO.read(new File("./SampleTestFolder/" + imageName + ".png"));

                // Scale the image
                Image newImage = userProfilePic.getScaledInstance(iconSideLength + 10, iconSideLength + 10, Image.SCALE_SMOOTH);

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
                    Image newImage = img.getScaledInstance(iconSideLength + 10, iconSideLength + 10, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> {
                        profileButton.setIcon(new ImageIcon(newImage));
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
     * Asynchronously loads and sets utility icons for various actions.
     * This method loads multiple icons from specified file paths, scales them,
     * and updates the corresponding buttons' icons on the Event Dispatch Thread (EDT).
     * If an I/O error occurs during image loading, it prints the stack trace.
     */
    private void createUtilityIcons() {

        // Run the image loading task on a new thread
        new Thread(() -> {
           try {

               // Load image from file
               addToSelectionIcon = ImageIO.read(new File(addSelectionIconPath));
               clearSelectionIcon = ImageIO.read(new File(clearSelectionIconPath));
               deleteMessageIcon = ImageIO.read(new File(deleteMessageIconPath));
               deleteSelectionIcon = ImageIO.read(new File(deleteSelectionIconPath));
               searchIcon = ImageIO.read(new File(searchIconPath));
               sendMessageIcon = ImageIO.read(new File(sendMessageIconPath));
               editMessageIcon = ImageIO.read(new File(editMessageIconPath));

               // Scale the image
               Image addToSelectionImage = addToSelectionIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image clearSelectionImage = clearSelectionIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image deleteMessageImage = deleteMessageIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image deleteSelectionImage = deleteSelectionIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image searchImage = searchIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image sendMessageImage = sendMessageIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);
               Image editMessageImage = editMessageIcon.getScaledInstance(iconSideLength, iconSideLength, Image.SCALE_SMOOTH);

               // Update the profile button on the EDT
               SwingUtilities.invokeLater(() -> {

                   addSelectedToChat.setIcon(new ImageIcon(addToSelectionImage));
                   addSelectedToChat.revalidate();
                   addSelectedToChat.repaint();

                   clearButton.setIcon(new ImageIcon(clearSelectionImage));
                   clearButton.revalidate();
                   clearButton.repaint();

                   deleteTextButton.setIcon(new ImageIcon(deleteMessageImage));
                   deleteTextButton.revalidate();
                   deleteTextButton.repaint();

                   deleteButton.setIcon(new ImageIcon(deleteSelectionImage));
                   deleteButton.revalidate();
                   deleteButton.repaint();

                   searchButton.setIcon(new ImageIcon(searchImage));
                   searchButton.revalidate();
                   searchButton.repaint();

                   sendButton.setIcon(new ImageIcon(sendMessageImage));
                   sendButton.revalidate();
                   sendButton.repaint();

                   editButton.setIcon(new ImageIcon(editMessageImage));
                   editButton.revalidate();
                   editButton.repaint();
               });

           } catch (IOException e) {
               e.printStackTrace();
           }
       }).start();
   }
    /**
     * Creates and returns the top panel of the user interface.
     * This panel contains a search panel and a selection panel, arranged vertically.
     * @return The top panel containing the search and selection panels.
     */
    public JPanel createTopPanel(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        // profileButton = new RoundedButton("User Page", 18);
        // profileButton.setFont(new Font("Arial", Font.BOLD, 12));
        profileButton = new JButton();
        searchPanel.add(profileButton, BorderLayout.WEST);
        searchPanel.add(createSearchPanel(), BorderLayout.CENTER);

        // Selection Panel
        JPanel selectionPanel = createSelectionPanel();

        topPanel.add(searchPanel);
        topPanel.add(selectionPanel);
        return topPanel;
    }

    /**
     * Creates and returns the search panel of the user interface.
     * This panel contains a search field and buttons for search, delete, and clear actions.
     * @return The search panel containing the search field and buttons.
     */
    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(20);
        searchField.setToolTipText("Search for users (case-insensitive)");

        searchField.setPreferredSize(new Dimension(200, 30));

        // searchButton = new RoundedButton("Search", 18);
        searchButton = new JButton();
        // deleteButton = new RoundedButton("Delete", 18);
        deleteButton = new JButton();
        // clearButton = new RoundedButton("Clear", 18);
        clearButton = new JButton();
        // searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        // deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        // clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setToolTipText("Remove the most recently added user from the selection.");
        clearButton.setToolTipText("Clear all selected users.");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        return searchPanel;
    }
    /**
     * * Creates and returns the selection panel of the user interface.
     * * This panel contains a text pane indicating selected users and buttons for each selected user.
     * * @return The selection panel containing the text pane and selection buttons.
     * */
    public JPanel createSelectionPanel() {
        JPanel selectionPanel = new JPanel(new FlowLayout());

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setText("Selected Users: ");
        selectionPanel.add(textPane);

        // Add selection buttons (hidden initially)
        for (int i = 0; i < MAX_SELECTED_USERS; i++) {
            JButton button = new JButton();
            button.setVisible(false);
            button.setEnabled(false);
            selectionButtons.add(button);
            selectionPanel.add(button);
        }

        // addSelectedToChat = new RoundedButton("Add Selected to Chat", 18);
        addSelectedToChat = new JButton();
        addSelectedToChat.setEnabled(false);
        selectionPanel.add(addSelectedToChat);

        return selectionPanel;
    }
    /**
     * Creates and returns a scrollable chat panel.
     * This panel is used to display chat messages dynamically.
     * @return A scrollable panel containing the chat messages.
     */
    public JScrollPane createChatPanel() {
        chatPanel = new JPanel(); // Create a new panel for dynamic chat updates
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // Vertical alignment of chat messages
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    /**
     * Updates the chat panel with the provided list of messages.
     * This method clears the existing messages, adds the new messages,
     * and ensures the panel is updated on the Event Dispatch Thread (EDT).
     * It also scrolls the chat panel to the bottom.
     * @param messages The list of messages to display in the chat panel.
     */
    private void updateChatPanel(ArrayList<String> messages) {
        chatPanel.removeAll(); // Clear existing components
        for (String message : messages) {
            messageLabel = new JLabel(message);
            chatPanel.add(messageLabel); // Add new messages dynamically
        }
        chatPanel.revalidate(); // Recalculate layout
        chatPanel.repaint();    // Refresh UI

        // Scroll to the bottom
        SwingUtilities.invokeLater(() -> chatViewPanel.getVerticalScrollBar().setValue(chatViewPanel.getVerticalScrollBar().getMaximum()));
    }

    /**
     * Creates and returns the bottom panel of the user interface.
     * This panel contains a chat input field and buttons for edit, send, delete, and refresh actions.
     * @return The bottom panel containing the chat input field and buttons.
     */
    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        chatField = new JTextField();
        chatField.setPreferredSize(new Dimension(200, 30));

        // editButton = new RoundedButton("Edit", 18);
        editButton = new JButton();
        // sendButton = new RoundedButton("Send", 18);
        sendButton = new JButton();
        // deleteTextButton = new RoundedButton("Delete", 18);
        deleteTextButton = new JButton();
        refreshButton = new JButton("Refresh");
        // editButton.setFont(new Font("Arial", Font.BOLD, 12));
        // sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        // deleteTextButton.setFont(new Font("Arial", Font.BOLD, 12));

        editButton.setEnabled(false);
        sendButton.setEnabled(false);
        deleteTextButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(deleteTextButton);
        buttonPanel.add(refreshButton);

        bottomPanel.add(chatField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        return bottomPanel;
    }
        /**
     * Creates and returns a scrollable panel containing chat buttons.
     * This panel is used to display buttons for each available chat.
     * @return A scrollable panel containing the chat buttons.
     */
    public JScrollPane createChatViewPanel() {
        // Create a panel for buttons with a grid layout
        JPanel buttonPanelLeft = new JPanel(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout

        try {
            String line = bufferedReader.readLine();
            while (line != null && !line.equals("stop")) {
                chatButtons.add(new RoundedButton(line, 18));
                buttonPanelLeft.add(chatButtons.get(chatButtons.size() - 1));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Wrap the panel in a scroll pane
        JScrollPane leftScrollPane = new JScrollPane(buttonPanelLeft);
        leftScrollPane.setPreferredSize(new Dimension(150, 0));

        return leftScrollPane;
    }
        /**
     * Sets up action listeners for various UI components.
     * This method assigns action listeners to buttons and other UI elements
     * to handle user interactions such as navigating to user profiles, searching for users,
     * sending messages, editing messages, deleting messages, and refreshing chat views.
     */
    private void setupActionListeners() {

        // Profile button navigates to user profile
        profileButton.addActionListener(e -> {
            common.Writer.write("user", bufferedWriter);
            System.out.println("write: user");
            pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, bufferedWriter, bufferedReader));
            pageManager.removePage("feed");
        });

        // Search for users
        searchButton.addActionListener(e -> handleUserSearch());

        for (JButton selectionButton : selectionButtons) {
            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedUser = selectionButton.getText();
                    common.Writer.write("6", bufferedWriter);
                    System.out.println("write: " + "6");
                    selectedUsers.add(selectedUser);
                    pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, bufferedWriter, bufferedReader, selectedUser));
                    pageManager.removePage("feed");
                }
            });
        }

        for (JButton chatButton : chatButtons) {
            chatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    common.Writer.write("2", bufferedWriter);
                    String selectedChat = chatButton.getText();
                    common.Writer.write(selectedChat, bufferedWriter);

                    Thread loadingThread = new Thread(() -> {
                        try {
                            String response = bufferedReader.readLine();
                            if (response.equals("valid")) {
                                String messagesLine = bufferedReader.readLine();
                                ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesLine.split(";")));
                                SwingUtilities.invokeLater(() -> {
                                    updateChatPanel(messages);
                                    currentChatID = selectedChat; // Update current chat ID

                                    // Enable buttons
                                    editButton.setEnabled(true);
                                    deleteTextButton.setEnabled(true);
                                    sendButton.setEnabled(true);
                                }); // Update chat panel on the Event Dispatch Thread
                            } else {
                                ArrayList<String> messages = new ArrayList<>();
                                messages.add("Invalid chat selected.");
                                SwingUtilities.invokeLater(() -> {
                                    updateChatPanel(messages);
                                    currentChatID = ""; // Reset current chat ID

                                    // Disable buttons
                                    editButton.setEnabled(false);
                                    deleteTextButton.setEnabled(false);
                                    sendButton.setEnabled(false);
                                });
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    loadingThread.start();

                    try {
                        loadingThread.join(); // makes sure this thread and its caller end at the same time
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        // Set up action listeners
        deleteButton.addActionListener(e -> {
            deleteMostRecentSelection();

            // Disable the `deleteButton` if there are no more users to delete
            if (selectedUsers.isEmpty()) {
                deleteButton.setEnabled(false);
            }
        });

        clearButton.addActionListener(e -> {
            clearSelections();

            // Disable both `deleteButton` and `clearButton` after clearing
            deleteButton.setEnabled(false);
            clearButton.setEnabled(false);
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                common.Writer.write("send", bufferedWriter);
                common.Writer.write(currentChatID, bufferedWriter);
                String message = chatField.getText();
                Thread loadingThread = new Thread(() -> {
                    try {
                        String response = bufferedReader.readLine();
                        if (response.equals("valid")) {
                            common.Writer.write(message, bufferedWriter);

                            String messagesLine = bufferedReader.readLine();
                            ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesLine.split(";")));
                            SwingUtilities.invokeLater(() -> {
                                updateChatPanel(messages);
                            }); // Update chat panel on the Event Dispatch Thread
                        } else {
                            ArrayList<String> messages = new ArrayList<>();
                            messages.add("Invalid chat selected.");
                            SwingUtilities.invokeLater(() -> {
                                updateChatPanel(messages);
                            });
                        }
                        chatField.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                loadingThread.start();

                try {
                    loadingThread.join(); // makes sure this thread and its caller end at the same time
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                common.Writer.write("refresh", bufferedWriter);
                common.Writer.write(currentChatID, bufferedWriter);
                String message = chatField.getText();
                Thread loadingThread = new Thread(() -> {
                    try {
                        String response = bufferedReader.readLine();
                        if (response.equals("valid")) {

                            String messagesLine = bufferedReader.readLine();
                            ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesLine.split(";")));
                            SwingUtilities.invokeLater(() -> {
                                updateChatPanel(messages);
                            }); // Update chat panel on the Event Dispatch Thread
                        } else {
                            ArrayList<String> messages = new ArrayList<>();
                            messages.add("Invalid chat selected.");
                            SwingUtilities.invokeLater(() -> {
                                updateChatPanel(messages);
                            });
                        }
                        chatField.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                loadingThread.start();

                try {
                    loadingThread.join(); // makes sure this thread and its caller end at the same time
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newMessage = chatField.getText().trim();
                if (newMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "New message cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (messageLabel.getText().isEmpty()) {
                    return;
                }

                common.Writer.write("edit", bufferedWriter);
                common.Writer.write(currentChatID, bufferedWriter);

                Thread loadingThread = new Thread(() -> {
                    try {
                        String response = bufferedReader.readLine();
                        if (response.equals("valid")) {
                            common.Writer.write(newMessage, bufferedWriter);

                            // Refresh chat panel
                            String messagesLine = bufferedReader.readLine();
                            ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesLine.split(";")));
                            SwingUtilities.invokeLater(() -> updateChatPanel(messages));
                        } else {
                            ArrayList<String> messages = new ArrayList<>();
                            messages.add("Invalid chat selected.");
                            SwingUtilities.invokeLater(() -> {
                                updateChatPanel(messages);
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Failed to edit the message. Please try again.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            });
                        }
                        chatField.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                loadingThread.start();

                try {
                    loadingThread.join(); // makes sure this thread and its caller end at the same time
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread loadingThread = new Thread(() -> {
                    if (messageLabel.getText().isEmpty()) {
                        return;
                    }

                    try {
                        common.Writer.write("delete", bufferedWriter);
                        common.Writer.write(currentChatID, bufferedWriter);
                        String response = bufferedReader.readLine();
                        System.out.println("read: " + response);
                        if (response.equals("valid")) {
                            // Refresh the chat panel
                            String messagesLine = bufferedReader.readLine();
                            ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesLine.split(";")));
                            SwingUtilities.invokeLater(() -> updateChatPanel(messages));
                        } else {
                            ArrayList<String> messages = new ArrayList<>();
                            messages.add("Invalid chat selected.");
                            SwingUtilities.invokeLater(() -> updateChatPanel(messages));
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Failed to delete the message.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        chatField.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                loadingThread.start();

                try {
                    loadingThread.join(); // makes sure this thread and its caller end at the same time
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });


        addSelectedToChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> validUsers = new ArrayList<>();

                try {
                    // Initiate the chat creation process
                    common.Writer.write("1", bufferedWriter);
                    System.out.println("write: 1");

                    // Read available users
                    String[] availableUsers = bufferedReader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(availableUsers));

                    // Validate selected users
                    for (String username : selectedUsers) {
                        common.Writer.write(username, bufferedWriter);
                        System.out.println("write: " + username);

                        String validation = bufferedReader.readLine();
                        System.out.println("read: " + validation);

                        // Skip invalid users
                        if (!validation.equals("self") && !validation.equals("User cannot be chatted with!")) {
                            validUsers.add(username);
                        }
                    }

                    // Signal the end of user validation
                    common.Writer.write("[DONE]", bufferedWriter);
                    System.out.println("write: [DONE]");

                    // Prepare and send the list of valid users
                    String membersList = String.join(";", validUsers);
                    common.Writer.write(membersList, bufferedWriter);
                    System.out.println("write: " + membersList);

                    // Check chat creation status
                    String chatCreationValidation = bufferedReader.readLine();
                    System.out.println("read: " + chatCreationValidation);

                    // Handle chat creation result
                    if ("[SUCCESSFUL CHAT CREATION]".equals(chatCreationValidation)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "You've created a new chat!",
                                "CHAT SUCCESSFULLY MADE",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Refresh chat buttons to include the newly created chat
                        refreshChatViewPanel();

                    } else if ("[CHAT CREATION UNSUCCESSFUL]".equals(chatCreationValidation)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Chat could not be made with the selected users!",
                                "INVALID USERS SELECTED FOR CHAT",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (IOException error) {
                    error.printStackTrace();
                } finally {
                    // Resets the selection panel, disable the button, and clears the selected users list.
                    addSelectedToChat.setEnabled(false);
                    for (JButton button : selectionButtons) {
                        button.setVisible(false);
                        button.setEnabled(false);
                    }
                    selectedUsers.clear();
                }
            }
        });
    }
        /**
     * Handles the user search action.
     * This method sends a search request to the server, processes the search result,
     * and adds the selected user to the selection if a valid user is found.
     * If no user is selected or the input is invalid, it shows an error message.
     */
    private void handleUserSearch() {
        try {
            common.Writer.write("4", bufferedWriter);
            System.out.println("write: 4");

            String line = bufferedReader.readLine();
            System.out.println("read: " + line);

            // Logic for user search and dropdown
            String userInput = searchField.getText();
            String selectedUser = processSearchResult(line, userInput);

            if (selectedUser != null) {
                addUserToSelection(selectedUser);
            } else {
                JOptionPane.showMessageDialog(null, "No user selected or invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            searchField.setText("");
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    /**
     * Processes the search result to find a matching user.
     * This method checks if the user input matches any user exactly or provides a dropdown
     * of close matches if no exact match is found.
     * @param userList The list of users to search within.
     * @param userInput The user input to search for.
     * @return The selected user from the search result, or null if no user is selected.
     */
    private String processSearchResult(String userList, String userInput) {
        String[] originalUsers = userList.split(";");

        // return searched user if input matches exactly
        if (Arrays.asList(originalUsers).contains(userInput)) {
            return userInput;

            // else, show dropdown (NOT case sensitive)
        } else {

            // Create dropdown for similar usernames
            String[] lowercaseUsers = userList.toLowerCase().split(";");
            ArrayList<String> closeMatches = new ArrayList<>();

            for (String user : lowercaseUsers) {
                if (user.contains(userInput.toLowerCase())) {
                    closeMatches.add(user);
                }
            }

            // convert all found matches back to their original case
            for (int i = 0; i < closeMatches.size(); i++) {

                for (int j = 0; j < originalUsers.length; j++) {
                    String convertedOriginalUser = originalUsers[j].toLowerCase();

                    if (convertedOriginalUser.equals(closeMatches.get(i))) {
                        closeMatches.set(i, originalUsers[j]);
                        continue; // because all usernames should be unique
                    }
                }
            }

            if (closeMatches.isEmpty()) {
                return null;
            }

            return (String) JOptionPane.showInputDialog(
                    null, "Choose a user:", "Matching Users",
                    JOptionPane.QUESTION_MESSAGE, null,
                    closeMatches.toArray(new String[0]), closeMatches.get(0));
        }
    }
    /**
     * Adds the selected user to the selection.
     * This method updates the selection buttons to reflect the newly selected user.
     * If the user is already selected or the maximum number of users is reached,
     * it shows an error message.
     * @param selectedUser The user to add to the selection.
     */
    private void addUserToSelection(String selectedUser) {
        if (selectedUsers.contains(selectedUser)) {
            JOptionPane.showMessageDialog(null, "User already selected.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (selectedUsers.size() >= MAX_SELECTED_USERS) {
            JOptionPane.showMessageDialog(null, "Too many users selected.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            selectedUsers.add(selectedUser);

            // Update selection buttons
            for (int i = 0; i < selectedUsers.size(); i++) {
                JButton button = selectionButtons.get(i);
                button.setVisible(true);
                button.setEnabled(true);
                button.setText(selectedUsers.get(i));
            }

            // Enable `deleteButton`, `clearButton`, and `addSelectedToChat`
            deleteButton.setEnabled(true);
            clearButton.setEnabled(true);
            addSelectedToChat.setEnabled(true);
        }
    }
    /**
     * Deletes the most recent user selection.
     * This method removes the most recent user from the selection and updates the corresponding button.
     * If no users are left in the selection, it disables the `addSelectedToChat` button.
     */
    private void deleteMostRecentSelection() {
        if (!selectedUsers.isEmpty()) {
            // Remove the most recent user and corresponding button
            int lastIndex = selectedUsers.size() - 1;
            selectedUsers.remove(lastIndex);

            JButton button = selectionButtons.get(lastIndex);
            button.setVisible(false);
            button.setEnabled(false);
            button.setText("");

            // Disable `addSelectedToChat` if no users are selected
            if (selectedUsers.isEmpty()) {
                addSelectedToChat.setEnabled(false);
            }
        }
    }
    /**
     * Clears all user selections.
     * This method resets all selection buttons and disables the `addSelectedToChat` button.
     */
    private void clearSelections() {
        // Clear all selected users
        selectedUsers.clear();

        // Reset all selection buttons
        for (JButton button : selectionButtons) {
            button.setVisible(false);
            button.setEnabled(false);
            button.setText("");
        }

        // Disable `addSelectedToChat`
        addSelectedToChat.setEnabled(false);
    }
    /**
     * Refreshes the chat view panel with updated chat buttons.
     * This method clears the existing chat buttons, requests an updated list of chats from the server,
     * and updates the chat view panel with the new buttons.
     */
    private void refreshChatViewPanel() {
        // Clear existing chat buttons
        chatButtons.clear();

        // Create a new panel for updated chat buttons
        JPanel buttonPanelLeft = new JPanel(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout

        // Request the updated list of chats from the server
        Writer.write("refreshChats", bufferedWriter);
        System.out.println("write: refreshChats");

        try {
            String line = bufferedReader.readLine();
            while (line != null && !line.equals("stop")) {
                chatButtons.add(new JButton(line));
                buttonPanelLeft.add(chatButtons.get(chatButtons.size() - 1));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Update the chat view panel with new buttons
        chatButtonPanel.setViewportView(buttonPanelLeft); // Replace the old panel
        chatButtonPanel.revalidate();
        chatButtonPanel.repaint();
    }

}