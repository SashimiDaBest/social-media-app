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
    private JButton profileButton, searchButton, deleteButton, clearButton, addSelectedToChat, deleteTextButton;
    private ArrayList<JButton> chatButtons, selectionButtons;
    private ArrayList<JLabel> chatLabels;
    private JTextField chatField, searchField;
    private JButton uploadButton, editButton, sendButton;
    private JScrollPane chatViewPanel;
    private JPanel chatPanel; // Dynamic chat panel

    // Logic
    private ArrayList<String> selectedUsers; // Holds selected usernames
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private PageManager pageManager;
    private String currentChatID;

    public FeedViewPage(PageManager pageManager, BufferedWriter bw, BufferedReader br) {
        System.out.println("This is feed view page");

        this.pageManager = pageManager;
        this.bufferedWriter = bw;
        this.bufferedReader = br;
        this.selectedUsers = new ArrayList<>();
        this.selectionButtons = new ArrayList<>();
        this.chatButtons = new ArrayList<>();
        this.chatLabels = new ArrayList<>();
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
        searchField.setToolTipText("Search for users (case-insensitive)");

        searchField.setPreferredSize(new Dimension(200, 30));

        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
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
        chatPanel = new JPanel(); // Create a new panel for dynamic chat updates
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // Vertical alignment of chat messages
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private void updateChatPanel(ArrayList<String> messages) {
        chatPanel.removeAll(); // Clear existing components
        for (String message : messages) {
            JLabel messageLabel = new JLabel(message);
            chatPanel.add(messageLabel); // Add new messages dynamically
        }
        chatPanel.revalidate(); // Recalculate layout
        chatPanel.repaint();    // Refresh UI

        // Scroll to the bottom
        SwingUtilities.invokeLater(() -> chatViewPanel.getVerticalScrollBar().setValue(chatViewPanel.getVerticalScrollBar().getMaximum()));
    }

    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        chatField = new JTextField();
        chatField.setPreferredSize(new Dimension(200, 30));

        uploadButton = new JButton("Upload Picture");
        editButton = new JButton("Edit");
        sendButton = new JButton("Send");
        deleteTextButton = new JButton("Delete");

        uploadButton.setEnabled(false);
        editButton.setEnabled(false);
        sendButton.setEnabled(false);
        deleteTextButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(uploadButton);
        buttonPanel.add(editButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(deleteTextButton);

        bottomPanel.add(chatField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        return bottomPanel;
    }

    public JScrollPane createChatViewPanel() {
        // Create a panel for buttons with a grid layout
        JPanel buttonPanelLeft = new JPanel(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout

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

        // Wrap the panel in a scroll pane
        JScrollPane leftScrollPane = new JScrollPane(buttonPanelLeft);
        leftScrollPane.setPreferredSize(new Dimension(150, 0));

        return leftScrollPane;
    }

    private void setupActionListeners() {

        // Profile button navigates to user profile
        profileButton.addActionListener(e -> {
            Writer.write("user", bufferedWriter);
            System.out.println("write: user");
            pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, bufferedWriter, bufferedReader));
            pageManager.removePage("feed");
        });

        // Search for users
        searchButton.addActionListener(e -> handleUserSearch());

        for (JButton selectionButton: selectionButtons) {
            selectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedUser = selectionButton.getText();
                    Writer.write("6", bufferedWriter);
                    System.out.println("write: " + "6");
                    selectedUsers.add(selectedUser);
                    pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, bufferedWriter, bufferedReader, selectedUser));
                    pageManager.removePage("feed");
                }
            });
        }

        for (JButton chatButton: chatButtons) {
            chatButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   Writer.write("2", bufferedWriter);
                   String selectedChat = chatButton.getText();
                   Writer.write(selectedChat, bufferedWriter);

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
                                   uploadButton.setEnabled(true);
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
                                   uploadButton.setEnabled(false);
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
                Writer.write("send", bufferedWriter);
                Writer.write(currentChatID, bufferedWriter);
                String message = chatField.getText();
                Thread loadingThread = new Thread(() -> {
                    try {
                        String response = bufferedReader.readLine();
                        if (response.equals("valid")) {
                            Writer.write(message, bufferedWriter);

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

                Writer.write("edit", bufferedWriter);
                Writer.write(currentChatID, bufferedWriter);

                Thread loadingThread = new Thread(() -> {
                    try {
                        String response = bufferedReader.readLine();
                        if (response.equals("valid")) {
                            Writer.write(newMessage, bufferedWriter);

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
                    try {
                        Writer.write("delete", bufferedWriter);
                        Writer.write(currentChatID, bufferedWriter);
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

        /*
        addSelectedToChat.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> validUsers = new ArrayList<>();
                
                try {
                    // Initiate the chat creation process
                    Writer.write("1", bufferedWri);
                    System.out.println("write: 1");

                    // Read available users
                    String[] availableUsers = bufferedReader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(availableUsers));

                    // Validate selected users
                    for (String username : selectedUsers) {
                        Writer.write(username, bufferedWri);
                        System.out.println("write: " + username);

                        String validation = bufferedReader.readLine();
                        System.out.println("read: " + validation);

                        // Skip invalid users
                        if (!validation.equals("self") && !validation.equals("User cannot be chatted with!")) {
                            validUsers.add(username);
                        }
                    }

                    // Signal the end of user validation
                    Writer.write("[DONE]", bufferedWri);
                    System.out.println("write: [DONE]");

                    // Prepare and send the list of valid users
                    String membersList = String.join(";", validUsers);
                    Writer.write(membersList, bufferedWri);
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

    private void handleUserSearch() {
        try {
            Writer.write("4", bufferedWriter);
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
}
