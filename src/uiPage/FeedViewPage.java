package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FeedViewPage extends JPanel {

    // for viewing own user profile
    private JButton profileButton;
    private JButton searchButton;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton prevButton;
    private JButton nextButton;

    private ArrayList<JButton> chatButtons;
    private ArrayList<JLabel> chatLabels;

    private JTextField chatField;
    private JButton uploadButton;
    private JButton editButton;
    private JButton sendButton;

    // for search panel
    private JButton viewSelectedButton;
    private ArrayList<String> selectedUsers; // holds selected usernames
    private JTextField searchField;


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
        this.selectedUsers = new ArrayList<>();

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

        chatField = new JTextField();
        chatField.setPreferredSize(new Dimension(200, 30)); // Fixed size for text field
        uploadButton = new JButton("Upload Picture");
        editButton = new JButton("Edit");
        sendButton = new JButton("Send");

        JPanel buttonPanelBottom = new JPanel(new FlowLayout());
        buttonPanelBottom.add(uploadButton);
        buttonPanelBottom.add(editButton);
        buttonPanelBottom.add(sendButton);

        editButton.setEnabled(false);
        sendButton.setEnabled(false);
        uploadButton.setEnabled(false);

        bottomPanel.add(chatField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanelBottom, BorderLayout.EAST);
        return bottomPanel;
    }

    public JScrollPane createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        chatLabels = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            JPanel chatRow = new JPanel();
            chatRow.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel userLabel = new JLabel("");
            chatRow.add(userLabel);
            chatPanel.add(chatRow);
            chatLabels.add(userLabel);
        }

        chatLabels.get(0).setText("Chat content will appear here!");

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        return chatScrollPane;
    }

    public JScrollPane createChatViewPanel() {
        JPanel buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout

        chatButtons = new ArrayList<>(); // For accessing buttons after creation

        for (int i = 1; i <= 8; i++) {
            JButton button = new JButton("Button " + i);
            button.setPreferredSize(new Dimension(50, 40)); // Fixed size for buttons
            buttonPanelLeft.add(button);
            chatButtons.add(button);
            button.setEnabled(false);
            button.setVisible(false);
        }

        chatButtons.get(0).setVisible(true);
        chatButtons.get(0).setText("Load chats!");

        JScrollPane leftScrollPane = new JScrollPane(buttonPanelLeft);
        leftScrollPane.setPreferredSize(new Dimension(150, 0)); // Adjust width to fit buttons
        return leftScrollPane;
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        this.searchButton = new JButton("Search");
        this.loadButton = new JButton("Load");
        this.deleteButton = new JButton("Delete");
        this.clearButton = new JButton("Clear");
        this.prevButton = new JButton("<<");
        this.nextButton = new JButton(">>");
        this.viewSelectedButton = new JButton("View Selected Users");
        this.searchField = searchField;

        deleteButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewSelectedButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        deleteButton.setEnabled(false);
        clearButton.setEnabled(false);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        return searchPanel;
    }


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

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("2", writer);
                try {
                    loadButton.setEnabled(false);
                    String response = reader.readLine();
                    System.out.println(response);
                    if (response.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "You have no chats!", "Boiler Gram", JOptionPane.ERROR_MESSAGE);
                        loadButton.setEnabled(true);
                    } else {
                        String[] activeChats = response.split(";");
                        for (int i = 0; i < chatButtons.size(); i++) {
                            if (i < activeChats.length) {
                                chatButtons.get(i).setEnabled(true);
                                chatButtons.get(i).setVisible(true);
                                chatButtons.get(i).setText(activeChats[i].substring(0, 10));
                            } else
                                chatButtons.get(i).setVisible(false);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        for (JButton chatButton : chatButtons) {
            chatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        UserPageClient.write(chatButton.getText().substring(6), writer);
                        for (JButton button : chatButtons) {
                            if (!button.equals(chatButton)) {
                                button.setVisible(false);
                            } else {
                                button.setEnabled(false);
                            }
                        }
                        clearButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        uploadButton.setEnabled(true);
                        editButton.setEnabled(true);
                        sendButton.setEnabled(true);

                        ArrayList<String> menuToDisplay;
                        String[] lines = reader.readLine().split(";");

                        menuToDisplay = new ArrayList<>(Arrays.asList(lines));
                        int index = 0;
                        for (int i = 0; i < menuToDisplay.size(); i++) {
                            String line = menuToDisplay.get(i);
                            if (line.equals("---------------------------------------------------------------------") ||
                                    line.equals("[Displaying up to 6 most recent messages]") ||
                                    line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                    line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                    line.contains("Chat #") || line.contains("Members: You, ") || (line.isEmpty())) {
                                continue;
                            } else {
                                chatLabels.get(index).setText(line);
                                index++;
                            }
                        }

                        for (int i = index; i < chatLabels.size(); i++) {
                            chatLabels.get(i).setText("");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserPageClient.write("1", writer);
                    UserPageClient.write(chatField.getText(), writer);

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");

                    menuToDisplay = new ArrayList<>(Arrays.asList(lines));
                    int index = 0;
                    for (int i = 0; i < menuToDisplay.size(); i++) {
                        String line = menuToDisplay.get(i);
                        if (line.equals("---------------------------------------------------------------------") ||
                                line.equals("[Displaying up to 6 most recent messages]") ||
                                line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                line.contains("Chat #") || line.contains("Members: You, ") || (line.isEmpty())) {
                            continue;
                        } else {
                            chatLabels.get(index).setText(line);
                            index++;
                        }
                    }

                    for (int i = index; i < chatLabels.size(); i++) {
                        chatLabels.get(i).setText("");
                    }

                    chatField.setText("");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserPageClient.write("3", writer);
                    UserPageClient.write(chatField.getText(), writer);

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");

                    menuToDisplay = new ArrayList<>(Arrays.asList(lines));
                    int index = 0;
                    for (int i = 0; i < menuToDisplay.size(); i++) {
                        String line = menuToDisplay.get(i);
                        if (line.equals("---------------------------------------------------------------------") ||
                                line.equals("[Displaying up to 6 most recent messages]") ||
                                line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                line.contains("Chat #") || line.contains("Members: You, ") || (line.isEmpty())) {
                            continue;
                        } else {
                            chatLabels.get(index).setText(line);
                            index++;
                        }
                    }

                    for (int i = index; i < chatLabels.size(); i++) {
                        chatLabels.get(i).setText("");
                    }
                    chatField.setText("");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserPageClient.write("2", writer);

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");

                    menuToDisplay = new ArrayList<>(Arrays.asList(lines));
                    int index = 0;
                    for (int i = 0; i < menuToDisplay.size(); i++) {
                        String line = menuToDisplay.get(i);
                        if (line.equals("---------------------------------------------------------------------") ||
                                line.equals("[Displaying up to 6 most recent messages]") ||
                                line.equals("1 - Compose message") || line.equals("2 - Delete previous message") ||
                                line.equals("3 - Edit previous message") || line.equals("4 - Exit chat") ||
                                line.contains("Chat #") || line.contains("Members: You, ") || (line.isEmpty())) {
                            continue;
                        } else {
                            chatLabels.get(index).setText(line);
                            index++;
                        }
                    }

                    for (int i = index; i < chatLabels.size(); i++) {
                        chatLabels.get(i).setText("");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("4", writer);
                chatButtons.get(0).setVisible(true);
                chatButtons.get(0).setText("Load chats!");
                chatButtons.get(0).setEnabled(false);
                for (int i = 1; i < chatButtons.size(); i++) {
                    chatButtons.get(i).setVisible(false);
                }
                chatLabels.get(0).setText("Chat content will appear here!");
                for (int i = 1; i < chatLabels.size(); i++) {
                    chatLabels.get(i).setText("");
                }
                loadButton.setEnabled(true);
                deleteButton.setEnabled(false);
                uploadButton.setEnabled(false);
                editButton.setEnabled(false);
                sendButton.setEnabled(false);
            }
        });


        // search for other user
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // all stuff used below:
                    UserPageClient.write("4", writer);
                    String[] userList = reader.readLine().split(";");
                    String userInput = searchField.getText();
                    String selectedUser;

                    // if the user types in the exact username, then there is no need for a dropdown
                    if (Arrays.asList(userList).contains(userInput)) {
                        selectedUser = userInput;
                    
                    // otherwise, use a dropdown menu
                    } else {
                        // for menu pop-up: show all usernames that are close to userList
                        ArrayList<String> closeOptions = new ArrayList<>();
                        for (String name: userList) {
                            if (name.contains(userInput)) {
                                closeOptions.add(name);
                            }
                        }
                        String[] menuOptions = closeOptions.toArray(new String[closeOptions.size()]);

                        // for selecting the user based off drop down
                        if (menuOptions.length == 0) { // for whatever reason, then skip to displaying an error
                            selectedUser = "";
                        
                        } else {
                            selectedUser = (String) JOptionPane.showInputDialog(null, "Here are the closest options:", 
                            "CLOSE TO MATCHING USERNAMES:", JOptionPane.QUESTION_MESSAGE, null, menuOptions, menuOptions[0]);
                        }
                        
                    }

                    // choose user, send back for validation
                    UserPageClient.write(selectedUser, writer);

                    // VALIDATION 
                    boolean validation = Boolean.parseBoolean(reader.readLine());
                    if (validation) {
                        selectedUsers.add(selectedUser);
                        pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, writer, reader, selectedUser));
                        pageManager.removePage("feed");
                        System.out.println(selectedUsers);

                    } else {
                        JOptionPane.showMessageDialog(null, "Could not find selected user" +
                        " or the search field was empty!",
                            "ERROR: USER SELECTION", JOptionPane.QUESTION_MESSAGE);
                    }
                    searchField.setText("");

                } catch (IOException error) {
                    error.printStackTrace();
                }
            }

        });
        
        // for viewing all currently selectedUsers
        viewSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // creates list of usernames to display
                String message = "";
                for (String username: selectedUsers) {
                    message += username + "\n";
                }
                System.out.println("All selected users: " + message);

                JOptionPane.showMessageDialog(null, message, 
                    "CURRENTLY SELECTED USERS", JOptionPane.INFORMATION_MESSAGE);
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
