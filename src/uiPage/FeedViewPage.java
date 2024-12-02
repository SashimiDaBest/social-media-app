package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class FeedViewPage extends JPanel {

    private JButton profileButton;
//    private JButton viewAnotherProfile;
//    private JButton endFeedButton;
//    private JButton loadChatsButton;
//    private JButton openChatButton;

//    private JLabel chatViewLabel;
//    private JLabel chatContent;
//    private JTextField messageContent;
//    private JButton composeMessageButton;
//    private JButton editMessageButton;
//    private JButton deleteMessageButton;
//    private JButton exitChatButton;

//    private JComboBox activeChatsBox;

    private BufferedWriter writer;
    private BufferedReader reader;
    private PageManager pageManager;

    public FeedViewPage(PageManager pageManager, BufferedWriter bw, BufferedReader br) {
        this.pageManager = pageManager;
        this.writer = bw;
        this.reader = br;

        setLayout(new BorderLayout());

        // Top panel (Profile Icon and Search)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Profile Icon Button
        profileButton = new JButton("User Page");
        profileButton.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(profileButton, BorderLayout.WEST);

        // Search Panel
        topPanel.add(createSearchPanel(), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center panel (Chat messages and left panel)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Left panel (Buttons) inside scroll pane
        mainPanel.add(createChatViewPanel(), BorderLayout.WEST);

        // Chat panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createChatPanel(), BorderLayout.CENTER);

        // Bottom panel (Text input and actions)
        rightPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30)); // Fixed size for text field
        JButton uploadButton = new JButton("Upload Picture");
        JButton sendButton = new JButton("Send");

        JPanel buttonPanelBottom = new JPanel(new FlowLayout());
        buttonPanelBottom.add(uploadButton);
        buttonPanelBottom.add(sendButton);

        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanelBottom, BorderLayout.EAST);
        return bottomPanel;
    }

    public JScrollPane createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= 6; i++) {
            JPanel chatRow = new JPanel();
            chatRow.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel chatLabel = new JLabel("CHAT " + i);
            JLabel userLabel = new JLabel("| U" + (i % 2 + 1) + ": Message here");
            chatRow.add(chatLabel);
            chatRow.add(userLabel);
            chatPanel.add(chatRow);
        }

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        return chatScrollPane;
    }

    public JScrollPane createChatViewPanel() {
        JPanel buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout

        for (int i = 1; i <= 8; i++) {
            JButton button = new JButton("Button " + i);
            button.setPreferredSize(new Dimension(50, 40)); // Fixed size for buttons
            buttonPanelLeft.add(button);
        }

        JScrollPane leftScrollPane = new JScrollPane(buttonPanelLeft);
        leftScrollPane.setPreferredSize(new Dimension(150, 0)); // Adjust width to fit buttons
        return leftScrollPane;
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton prevButton = new JButton("<<");
        JButton nextButton = new JButton(">>");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        return searchPanel;
    }

    /*
    public FeedViewPage(PageManager pageManager, BufferedWriter bw, BufferedReader br) {

        // for viewing other users' profiles
        viewAnotherProfile = new JButton("View another user's profile");
        headerPanel.add(viewAnotherProfile);

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JButton createChatButton = new JButton("Create chat");
        JTextField userSearchField = new JTextField(15);
        searchPanel.add(userSearchField);
        searchPanel.add(createChatButton);

        JPanel chatFeedPanel = new JPanel();
        chatFeedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatFeedPanel.setLayout(new BoxLayout(chatFeedPanel, BoxLayout.X_AXIS));

        // Initialize chatSelectionPanel with GridBagLayout
        JPanel chatSelectionPanel = new JPanel();
        chatSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatSelectionPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;  // Align components at the top

        // Add the "Load chats" button at the left, centered in the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;  // Span 1 column
        loadChatsButton = new JButton("Load chats");
        chatSelectionPanel.add(loadChatsButton, gbc);

        // Add the "Open chat" button just to the right of the "Load chats" button
        gbc.gridx = 1;  // Move to the next column
        openChatButton = new JButton("Open chat");
        openChatButton.setEnabled(false);
        chatSelectionPanel.add(openChatButton, gbc);

        // Add the combo box below the buttons and center it
        gbc.gridx = 0;  // Start at the first column
        gbc.gridy = 1;  // Move to the second row
        gbc.gridwidth = 2;  // Span across 2 columns to center the combo box
        gbc.anchor = GridBagConstraints.CENTER;  // Center the combo box
        activeChatsBox = new JComboBox();
        activeChatsBox.setEditable(false);
        chatSelectionPanel.add(activeChatsBox, gbc);

        // Chat View Panel (Right side)
        JPanel chatViewPanel = new JPanel();
        chatViewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatViewPanel.setLayout(new GridBagLayout());

        // Add "Chat View" label at the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Span across the whole panel
        gbc.anchor = GridBagConstraints.NORTH;  // Align to the top
        chatViewLabel = new JLabel("Chat view");
        chatViewPanel.add(chatViewLabel, gbc);

        // Add the "Chat content" label below "Chat view"
        gbc.gridy = 1;
        gbc.gridwidth = 2;  // Span across the whole panel
        chatContent = new JLabel("Chat content");
        chatContent.setVerticalAlignment(SwingConstants.TOP);  // Align text to the top
        chatContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));  // Add a border for clarity

        // Set a preferred size that will fill the remaining vertical space
        chatContent.setPreferredSize(new Dimension(400, 200));  // Make it large enough to hold chat content
        chatContent.setMinimumSize(new Dimension(400, 100));  // Minimum size, in case chat content is small
        chatViewPanel.add(chatContent, gbc);

        // Add the new JTextField (messageContent) between chatContent and the buttons
        gbc.gridy = 2;  // Move to the next row after the chat content label
        messageContent = new JTextField();  // Create the text field
        messageContent.setEnabled(false);  // Disable it by default
        messageContent.setPreferredSize(new Dimension(400, 30));  // Set a preferred size for the text field
        chatViewPanel.add(messageContent, gbc);

        // Add the four buttons (compose, edit, delete, exit) at the bottom
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset grid width to 1 for each button
        composeMessageButton = new JButton("Compose message");
        composeMessageButton.setEnabled(false);
        chatViewPanel.add(composeMessageButton, gbc);

        gbc.gridx = 1;
        editMessageButton = new JButton("Edit message");
        editMessageButton.setEnabled(false);
        chatViewPanel.add(editMessageButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        deleteMessageButton = new JButton("Delete message");
        deleteMessageButton.setEnabled(false);
        chatViewPanel.add(deleteMessageButton, gbc);

        gbc.gridx = 1;
        exitChatButton = new JButton("Exit chat and refresh");
        exitChatButton.setEnabled(false);
        chatViewPanel.add(exitChatButton, gbc);

        // Add the chatSelectionPanel to the chatFeedPanel
        gbc.weightx = 0.5;
        gbc.gridx = 0;  // Add to the first column of chatFeedPanel
        chatFeedPanel.add(chatSelectionPanel, gbc);

        // Add chatViewPanel to the second column of chatFeedPanel
        gbc.gridx = 1;  // Add to the second column
        chatFeedPanel.add(chatViewPanel, gbc);

        // Navigation Panel
        JPanel navigationPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        endFeedButton = new JButton("Exit feed");
        navigationPanel.add(endFeedButton);
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        // Add components to the main layout
        add(headerPanel, BorderLayout.NORTH);
        add(chatFeedPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        setupActionListeners();
    }
     */
    private void setupActionListeners() {

        // shows the current user's profile when clicked
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("3", writer);
                // load the user's page
                pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, writer, reader));
                pageManager.removePage("feed");

            }
        });

        /*
        // shows another users' profile
        viewAnotherProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    UserPageClient.write("4", writer);
                    // display list of users from the server
                    String[] userList = reader.readLine().split(";");
                    // String selectedUser = (String) JOptionPane.showInputDialog(null,
                    //         "Select a user to view", "USERS FOUND",
                    //         JOptionPane.QUESTION_MESSAGE, null, userList, userList[0]);
                    String selectedUser = JOptionPane.showInputDialog(null, "Enter an existing username: ", "USER SELECTION",
                            JOptionPane.QUESTION_MESSAGE);

                    // choose user, send back for validation
                    UserPageClient.write(selectedUser, writer);

                    // show other user profile if valid
                    boolean validation = Boolean.parseBoolean(reader.readLine());
                    if (validation) {
                        pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, writer, reader, selectedUser));
                        pageManager.removePage("feed");

                    } else {
                        JOptionPane.showMessageDialog(null, "Could not find selected user!",
                                "ERROR: USER SELECTION", JOptionPane.QUESTION_MESSAGE);
                    }

                } catch (IOException error) {
                    error.printStackTrace();
                }
            }

        });

        // discountiue feed when clicked
        endFeedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Logging out...",
                        "CLOSING FEED", JOptionPane.INFORMATION_MESSAGE);

                try {

                    if (writer != null) {
                        writer.close(); // Close BufferedWriter
                    }
                    if (reader != null) {
                        reader.close(); // Close BufferedReader
                    }

                } catch (IOException error) {
                    error.printStackTrace();
                }

                pageManager.removePage("feed");
            }
        });

        loadChatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadChatsButton.setEnabled(false);
                    openChatButton.setEnabled(true);
                    UserPageClient.write("2", writer);
                    String[] currentChats = reader.readLine().split(";");
                    for (String chat : currentChats) {
                        activeChatsBox.addItem(chat);
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        openChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (activeChatsBox.getSelectedItem() != null) {
                        openChatButton.setEnabled(false);
                        composeMessageButton.setEnabled(true);
                        deleteMessageButton.setEnabled(true);
                        editMessageButton.setEnabled(true);
                        exitChatButton.setEnabled(true);
                        String selectedChatID = (String) activeChatsBox.getSelectedItem();
                        selectedChatID = selectedChatID.substring(6, 10); // Extract chat ID
                        UserPageClient.write(selectedChatID, writer);

                        String serverResponse = reader.readLine();  // Read the server response
                        String[] serverChatContent = serverResponse.split(";");  // Split the content by semicolons

                        ArrayList<String> linesToDisplay = new ArrayList<>();

                        for (String line : serverChatContent) {
                            if (line.equals("---------------------------------------------------------------------") ||
                                    line.equals("[Displaying up to 6 most recent messages]") ||
                                    line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                    line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                    line.contains("Chat #") || line.contains("Members: You, ") || line.isEmpty()) {
                                continue;
                            } else {
                                linesToDisplay.add(line);
                            }
                        }

                        // Join the chat content using newline characters and set it as the text of chatContent label
                        String chatText = String.join("\n", linesToDisplay);  // Join array elements with '\n' to create a multiline string

                        // Set the chat content text to the JLabel
                        chatContent.setText("<html>" + chatText.replaceAll("\n", "<br>") + "</html>");  // Using HTML for line breaks
                        chatViewLabel.setText((String) activeChatsBox.getSelectedItem());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        composeMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!messageContent.isEnabled()) {
                        messageContent.setEnabled(true);
                        editMessageButton.setEnabled(false);
                        deleteMessageButton.setEnabled(false);
                        exitChatButton.setEnabled(false);
                        messageContent.setText("Enter your message here!");
                        UserPageClient.write("1", writer);
                    } else {
                        UserPageClient.write(messageContent.getText(), writer);
                        messageContent.setText("");
                        messageContent.setEnabled(false);
                        editMessageButton.setEnabled(true);
                        deleteMessageButton.setEnabled(true);
                        exitChatButton.setEnabled(true);

                        String serverResponse = reader.readLine();  // Read the server response
                        String[] serverChatContent = serverResponse.split(";");  // Split the content by semicolons

                        ArrayList<String> linesToDisplay = new ArrayList<>();

                        for (String line : serverChatContent) {
                            if (line.equals("---------------------------------------------------------------------") ||
                                    line.equals("[Displaying up to 6 most recent messages]") ||
                                    line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                    line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                    line.contains("Chat #") || line.contains("Members: You, ") || line.isEmpty()) {
                                continue;
                            } else {
                                linesToDisplay.add(line);
                            }
                        }

                        // Join the chat content using newline characters and set it as the text of chatContent label
                        String chatText = String.join("\n", linesToDisplay);  // Join array elements with '\n' to create a multiline string

                        // Set the chat content text to the JLabel
                        chatContent.setText("<html>" + chatText.replaceAll("\n", "<br>") + "</html>");  // Using HTML for line breaks
                        chatViewLabel.setText((String) activeChatsBox.getSelectedItem());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        editMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!messageContent.isEnabled()) {
                        messageContent.setEnabled(true);
                        composeMessageButton.setEnabled(false);
                        deleteMessageButton.setEnabled(false);
                        exitChatButton.setEnabled(false);
                        messageContent.setText("Enter revised message here!");
                        UserPageClient.write("3", writer);
                    } else {
                        UserPageClient.write(messageContent.getText(), writer);
                        messageContent.setText("");
                        messageContent.setEnabled(false);
                        composeMessageButton.setEnabled(true);
                        deleteMessageButton.setEnabled(true);
                        exitChatButton.setEnabled(true);

                        String serverResponse = reader.readLine();  // Read the server response
                        String[] serverChatContent = serverResponse.split(";");  // Split the content by semicolons

                        ArrayList<String> linesToDisplay = new ArrayList<>();

                        for (String line : serverChatContent) {
                            if (line.equals("---------------------------------------------------------------------") ||
                                    line.equals("[Displaying up to 6 most recent messages]") ||
                                    line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                    line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                    line.contains("Chat #") || line.contains("Members: You, ") || line.isEmpty()) {
                                continue;
                            } else {
                                linesToDisplay.add(line);
                            }
                        }

                        // Join the chat content using newline characters and set it as the text of chatContent label
                        String chatText = String.join("\n", linesToDisplay);  // Join array elements with '\n' to create a multiline string

                        // Set the chat content text to the JLabel
                        chatContent.setText("<html>" + chatText.replaceAll("\n", "<br>") + "</html>");  // Using HTML for line breaks
                        chatViewLabel.setText((String) activeChatsBox.getSelectedItem());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        deleteMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserPageClient.write("2", writer);

                    String serverResponse = reader.readLine();  // Read the server response
                    String[] serverChatContent = serverResponse.split(";");  // Split the content by semicolons

                    ArrayList<String> linesToDisplay = new ArrayList<>();

                    for (String line : serverChatContent) {
                        if (line.equals("---------------------------------------------------------------------") ||
                                line.equals("[Displaying up to 6 most recent messages]") ||
                                line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                line.contains("Chat #") || line.contains("Members: You, ") || line.isEmpty()) {
                            continue;
                        } else {
                            linesToDisplay.add(line);
                        }
                    }

                    // Join the chat content using newline characters and set it as the text of chatContent label
                    String chatText = String.join("\n", linesToDisplay);  // Join array elements with '\n' to create a multiline string

                    // Set the chat content text to the JLabel
                    chatContent.setText("<html>" + chatText.replaceAll("\n", "<br>") + "</html>");  // Using HTML for line breaks
                    chatViewLabel.setText((String) activeChatsBox.getSelectedItem());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exitChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("4", writer);
                chatViewLabel.setText("Chat view");
                chatContent.setText("Chat content");
                loadChatsButton.setEnabled(true);
                openChatButton.setEnabled(false);
                composeMessageButton.setEnabled(false);
                deleteMessageButton.setEnabled(false);
                editMessageButton.setEnabled(false);
                exitChatButton.setEnabled(false);
                messageContent.setText("");
                messageContent.setEnabled(false);
                activeChatsBox.removeAllItems();
            }
        });

         */
    }


    /*
    public static void feedPage(Scanner scanner, BufferedReader br, BufferedWriter bw, Socket socket) {
        boolean continueFeed = true;
        try {
            while (continueFeed) {
                displayMenu();
                String input = scanner.nextLine();
                if (input.equals("1")) {
                    UserPageClient.write("1", bw);

                    // Display list of users from the server
                    System.out.println("List of users to chat with:");
                    String receivedUserList = br.readLine();
                    String[] userList = receivedUserList.split(";");
                    for (String username : userList) {
                        System.out.println(username);
                    }

                    // Prompt user to finalize group creation with selected users
                    boolean makeGroup = false;
                    ArrayList<String> usernames = new ArrayList<>();

                    while (!makeGroup) {
                        System.out.print("Finalize Members (Y/N): ");
                        if (scanner.nextLine().equals("Y")) {
                            if (usernames.isEmpty()) {
                                System.out.print("Can't make group - group is empty!");
                                continue;
                            }
                            makeGroup = true;
                            UserPageClient.write("[DONE]", bw);
                        } else {
                            // Ask the user which user they want to add to their chat
                            System.out.print("Username to add: ");
                            String friendUsername = scanner.nextLine();

                            // Write the username to the server to ensure the user can chat with them
                            UserPageClient.write(friendUsername, bw);
                            String serverValidityResponse = br.readLine();
                            if (serverValidityResponse.isEmpty()) {
                                System.out.println("User selected successfully!");
                                usernames.add(friendUsername);
                            } else if (serverValidityResponse.equals("self")) {
                                System.out.println("You cannot add yourself to a chat!");
                            } else if (serverValidityResponse.equals("That user does not exist!")) {
                                System.out.println("That user does not exist!");
                            } else {
                                System.out.println("The user you are trying to chat with has blocked you," +
                                        " you have blocked them, or their account is private.");
                            }
                        }
                    }
                    // Write the finalized list of chat members to the server for processing.
                    // Format: username;username;username...etc
                    String usernamesToSend = "";
                    for (int i = 0; i < usernames.size(); i++) {
                        usernamesToSend += usernames.get(i);
                        if (i != usernames.size() - 1) {
                            usernamesToSend += ";";
                        }
                    }
                    UserPageClient.write(usernamesToSend, bw);

                    System.out.println("Chat created successfully!");
                } else if (input.equals("2")) {
                    UserPageClient.write("2", bw);

                    // Obtain and display all chats this user is a member of from the server.
                    String userChats = br.readLine();

                    if (!userChats.isEmpty()) {
                        System.out.println("Enter the number (ex. 0001) of the chat you'd like to open!");
                        String[] chatList = userChats.split(";");
                        for (String chat : chatList) {
                            System.out.println(chat);
                        }

                        // Write the selected chat to the server.
                        String chatNumber = scanner.nextLine();
                        UserPageClient.write(chatNumber, bw);

                        // Obtain the server's response -- either the chat is invalid or the selected
                        // chat will be displayed.
                        String serverChatResponse = br.readLine();
                        if (serverChatResponse.equals("Invalid Chat")) {
                            System.out.println("Invalid chat selection!");
                        } else {
                            // Read the chat menu from the server, looping through the menu
                            // until the client requests to stop.
                            boolean viewChat = true;
                            do {
                                String[] chatMenuLines = serverChatResponse.split(";");
                                for (String line : chatMenuLines) {
                                    System.out.println(line);
                                }

                                // Collect and write the client's decision.
                                switch (scanner.nextLine()) {
                                    case "1":
                                        UserPageClient.write("1", bw);

                                        // Compose Message
                                        System.out.println("Enter message to compose:");
                                        String message = scanner.nextLine();
                                        UserPageClient.write(message, bw);

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "2":
                                        UserPageClient.write("2", bw);

                                        // Delete Previous Message
                                        System.out.println("Message deleted!");

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "3":
                                        UserPageClient.write("3", bw);

                                        // Edit Previous Message
                                        System.out.println("Enter replacement message:");
                                        String replacement = scanner.nextLine();
                                        UserPageClient.write(replacement, bw);

                                        serverChatResponse = br.readLine();
                                        break;
                                    case "4":
                                        UserPageClient.write("4", bw);

                                        // End chat loop
                                        viewChat = false;
                                }
                            } while (viewChat);
                        }
                    } else {
                        System.out.println("You have no chats!");
                    }
                } else if (input.equals("3")) {
                    UserPageClient.write("3", bw);
                    UserPageClient.userPage(scanner, br, bw, socket);
                    break;
                } else if (input.equals("4")) {
                    UserPageClient.write("4", bw);

                    // Display list of users from the server
                    System.out.println("Type the username of the user to view! If there are none, type anything to" +
                            " refresh the Feed.");
                    String receivedUserList = br.readLine();
                    String[] userList = receivedUserList.split(";");
                    for (String username : userList) {
                        System.out.println(username);
                    }

                    // Obtain user selection and validate it through server
                    String userSelection = scanner.nextLine();
                    UserPageClient.write(userSelection, bw);

                    // Obtain server validation response
                    String validUser = br.readLine();
                    if (Boolean.parseBoolean(validUser)) {
                        OtherPageClient.otherPage(scanner, userSelection, br, bw, socket);
                        break;
                    } else {
                        System.out.println("Invalid user selection!");
                    }
                } else if (input.equals("5")) {
                    UserPageClient.write("5", bw);
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
                    continueFeed = false;
                } else {
                    System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while processing the feed page", e);
        }
    }

     */
    /**
     * Displays the main menu for the feed page.
     */
    /*
    private static void displayMenu() {
        System.out.print(
                "Welcome to your Feed! What would you like to do?\n" +
                        "1 - Create a new chat with selected users\n" +
                        "2 - Open an existing chat\n" +
                        "3 - View your profile\n" +
                        "4 - View another user's profile\n" +
                        "5 - Exit\n"
        );
    }
     */
}
