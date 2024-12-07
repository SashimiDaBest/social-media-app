package uiPage;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FeedViewPage extends JPanel {

    // Constants
    private static final int MAX_SELECTED_USERS = 8;

    // UI Components
    private JButton profileButton, searchButton, loadButton, deleteButton, clearButton;
    private ArrayList<JButton> chatButtons, selectionButtons;
    private ArrayList<JLabel> chatLabels;
    private JTextField chatField, searchField;
    private JButton uploadButton, editButton, sendButton, addSelectedToChat;

    // Logic
    private ArrayList<String> selectedUsers; // Holds selected usernames
    private BufferedWriter writer;
    private BufferedReader reader;
    private PageManager pageManager;

    public FeedViewPage(PageManager pageManager, BufferedWriter bw, BufferedReader br) {
        System.out.println("This is feed view page");

        this.pageManager = pageManager;
        this.writer = bw;
        this.reader = br;
        this.selectedUsers = new ArrayList<>();
        this.selectionButtons = new ArrayList<>();

        setLayout(new BorderLayout());

        // Top Panel: Profile and Search
        add(createTopPanel(), BorderLayout.NORTH);

        // Center Panel: Chat view and message panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(createChatPanel(), BorderLayout.CENTER);
        chatPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        mainPanel.add(createChatViewPanel(), BorderLayout.WEST);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    public JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        profileButton = new JButton("User Page");
        searchPanel.add(profileButton, BorderLayout.WEST);
        searchPanel.add(createSearchPanel(), BorderLayout.CENTER);

        // Selection Panel
        JPanel selectionPanel = createSelectionPanel();

        topPanel.add(searchPanel);
        topPanel.add(selectionPanel);
        return topPanel;
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));

        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        deleteButton.setEnabled(false);
        clearButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        return searchPanel;
    }

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

        addSelectedToChat = new JButton("Add Selected to Chat");
        addSelectedToChat.setEnabled(false);
        selectionPanel.add(addSelectedToChat);

        return selectionPanel;
    }

    public JScrollPane createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        chatLabels = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            JLabel label = new JLabel("");
            chatLabels.add(label);
            chatPanel.add(label);
        }

        chatLabels.get(0).setText("Add Users to Chat or View!");

        return new JScrollPane(chatPanel);
    }

    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        chatField = new JTextField();
        chatField.setPreferredSize(new Dimension(200, 30));

        uploadButton = new JButton("Upload Picture");
        editButton = new JButton("Edit");
        sendButton = new JButton("Send");

        uploadButton.setEnabled(false);
        editButton.setEnabled(false);
        sendButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(uploadButton);
        buttonPanel.add(editButton);
        buttonPanel.add(sendButton);

        bottomPanel.add(chatField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        return bottomPanel;
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

    private void setupActionListeners() {

        // Profile button navigates to user profile
        profileButton.addActionListener(e -> {
            Writer.write("user", writer);
            System.out.println("write: user");
            pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, writer, reader));
            pageManager.removePage("feed");
        });

        // Search for users
        searchButton.addActionListener(e -> handleUserSearch());

        /*
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("2", writer);
                System.out.println("write: " + "2");
                try {
                    loadButton.setEnabled(false);
                    String response = reader.readLine();
                    System.out.println("read: " + response);
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
         */

        for (JButton selectionButton: selectionButtons) {
            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedUser = selectionButton.getText();
                    Writer.write("6", writer);
                    System.out.println("write: " + "6");
                    selectedUsers.add(selectedUser);
                    pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, writer, reader, selectedUser));
                    pageManager.removePage("feed");
                }
            });
        }

        /*
        for (JButton chatButton : chatButtons) {
            chatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Writer.write(chatButton.getText().substring(6), writer);
                        System.out.println("write: " + chatButton.getText().substring(6));
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
                        System.out.println("read: " + Arrays.toString(lines));

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
        */

        /*
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("1", writer);
                    System.out.println("write: " + "1");
                    Writer.write(chatField.getText(), writer);
                    System.out.println("write: " + chatField.getText());

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(lines));

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
                    Writer.write("3", writer);
                    System.out.println("write: " + "3");
                    Writer.write(chatField.getText(), writer);
                    System.out.println("write: " + chatField.getText());

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(lines));

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
                    Writer.write("2", writer);
                    System.out.println("write: " + "2");

                    ArrayList<String> menuToDisplay;
                    String[] lines = reader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(lines));

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
                Writer.write("4", writer);
                System.out.println("write: " + "4");
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

        addSelectedToChat.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> validUsers = new ArrayList<>();
                
                try {
                    // Initiate the chat creation process
                    Writer.write("1", writer);
                    System.out.println("write: 1");

                    // Read available users
                    String[] availableUsers = reader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(availableUsers));

                    // Validate selected users
                    for (String username : selectedUsers) {
                        Writer.write(username, writer);
                        System.out.println("write: " + username);

                        String validation = reader.readLine();
                        System.out.println("read: " + validation);

                        // Skip invalid users
                        if (!validation.equals("self") && !validation.equals("User cannot be chatted with!")) {
                            validUsers.add(username);
                        }
                    }

                    // Signal the end of user validation
                    Writer.write("[DONE]", writer);
                    System.out.println("write: [DONE]");

                    // Prepare and send the list of valid users
                    String membersList = String.join(";", validUsers);
                    Writer.write(membersList, writer);
                    System.out.println("write: " + membersList);

                    // Check chat creation status
                    String chatCreationValidation = reader.readLine();
                    System.out.println("read: " + chatCreationValidation);

                    // Handle chat creation result
                    if ("[SUCCESSFUL CHAT CREATION]".equals(chatCreationValidation)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "You've created a new chat!",
                                "CHAT SUCCESSFULLY MADE",
                                JOptionPane.INFORMATION_MESSAGE
                        );
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


         */
    }

    // TODO: REVIEW LATER - SOMETHING IS WRONG WITH SEARCH FUNCTIONALITY
    private void handleUserSearch() {
        try {
            Writer.write("4", writer);
            System.out.println("write: 4");

            String line = reader.readLine();
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

    private String processSearchResult(String userList, String userInput) {
        String[] users = userList.split(";");
        if (Arrays.asList(users).contains(userInput)) {
            return userInput;
        } else {
            // Create dropdown for similar usernames
            ArrayList<String> closeMatches = new ArrayList<>();
            for (String user : users) {
                if (user.contains(userInput)) {
                    closeMatches.add(user);
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
            addSelectedToChat.setEnabled(true);
        }
    }
}
