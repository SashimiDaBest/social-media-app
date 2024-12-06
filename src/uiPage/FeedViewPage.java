package uiPage;

import javax.swing.*;

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
    private ArrayList<String> selectedUsers; // holds selected usernames
    private ArrayList<JButton> selectionButtons; // for accessing selected users later
    private JTextField searchField;
    private JButton addSelectedToChat;

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

        // Selection panel: shows current usernames that have been selected
        mainPanel.add(createSelectionPanel(), BorderLayout.EAST);

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
        this.searchField = searchField;

        deleteButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        deleteButton.setEnabled(false);
        clearButton.setEnabled(false);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        return searchPanel;
    }

    public JPanel createSelectionPanel() {

        JPanel selectionPanel = new JPanel();
        selectionPanel.setPreferredSize(new Dimension(100, 0));;
        selectionPanel.setLayout(new GridLayout(8, 1, 5, 5)); // 8 buttons in a grid layout\

        selectionButtons = new ArrayList<>(); // For accessing buttons after creation
        
        // for adding selected to chat
        this.addSelectedToChat = new JButton("Add Selected To Chat");
        addSelectedToChat.setPreferredSize(new Dimension(50, 40));
        addSelectedToChat.setEnabled(false);
        addSelectedToChat.setVisible(true);

        selectionPanel.add(addSelectedToChat);
        for (int i = 0; i < 7; i++) { // limit of 8 buttons
            JButton button = new JButton("");
            button.setSize(new Dimension(50, 40)); // Fixed size for buttons
            selectionPanel.add(button);
            selectionButtons.add(button);
            button.setEnabled(false);
            button.setVisible(false);
        }

        selectionButtons.get(0).setVisible(true);
        selectionButtons.get(0).setText("Waiting for user selection...");

        // JScrollPane leftScrollPane = new JScrollPane(selectionPanel);
        // leftScrollPane.setPreferredSize(new Dimension(150, 0)); // Adjust width to fit buttons

        return selectionPanel;
    }


    private void setupActionListeners() {

        // navigate to user profile
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("user", writer);
                // load the user's page
                pageManager.lazyLoadPage("user", () -> new UserProfilePage(pageManager, writer, reader));
                pageManager.removePage("feed");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Writer.write("2", writer);
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

        for (JButton chatButton : chatButtons) {
            chatButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Writer.write(chatButton.getText().substring(6), writer);
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

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Writer.write("1", writer);
                    Writer.write(chatField.getText(), writer);

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
                    Writer.write(chatField.getText(), writer);

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
                    Writer.write("4", writer);

                    String line = reader.readLine();
                    System.out.println("read: " + line);

                    // skirts past possible runtime error with OtherPageServer
                    while (true) {
                        if (line.equals("END") || line.equals("[EMPTY]") || line.equals("message")) {
                            line = reader.readLine();
                            System.out.println("read: " + line);
                        } else {
                            break;
                        }
                    }

                    String[] userList = line.split(";");
                    String userInput = searchField.getText();
                    String selectedUser;

                    // if the user types in the exact username, then there is no need for a dropdown
                    if (Arrays.asList(userList).contains(userInput)) {
                        selectedUser = userInput;
                    
                    } else if (userInput.equals("")) { // displays error if field is empty
                        selectedUser = "";
                    
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

                    // VALIDATION 
                    if (selectedUser == null) { // if user closes the drop down menu before selection
                        // pass 

                    } else if (selectedUser.equals("")) {
                        JOptionPane.showMessageDialog(null, "Could not find selected user" +
                        " or the search field was empty!",
                            "ERROR: USER SELECTION", JOptionPane.QUESTION_MESSAGE);
                    
                    } else if (selectedUsers.contains(selectedUser)) {
                        JOptionPane.showMessageDialog(null, "That user already exists in selection!",
                            "ERROR: USER SELECTION", JOptionPane.QUESTION_MESSAGE);
                    
                    } else if (selectedUsers.size() >= 8) {
                        JOptionPane.showMessageDialog(null, "Too many users have been selected (try clearing)!",
                            "ERROR: USER SELECTION", JOptionPane.QUESTION_MESSAGE);

                    } else {
                        selectedUsers.add(selectedUser);

                        // display current selection by changing the buttons
                        for (int i = 0; i < selectedUsers.size(); i++) {
                            selectionButtons.get(i).setEnabled(true);
                            selectionButtons.get(i).setVisible(true);
                            selectionButtons.get(i).setText(selectedUsers.get(i));
                        }
                        // if addSelectedChat isn't an option, make it an option
                        if (!addSelectedToChat.isEnabled()) {
                            addSelectedToChat.setEnabled(true);
                        }
                    }
                    searchField.setText("");

                } catch (IOException error) {
                    error.printStackTrace();
                }
            }

        });
        
        for (JButton selectionButton: selectionButtons) {

            selectionButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {

                    String selectedUser = selectionButton.getText();

                    Writer.write("6", writer);
                    Writer.write(selectedUser, writer);

                    selectedUsers.add(selectedUser);
                    pageManager.lazyLoadPage(selectedUser, () -> new OtherProfilePage(pageManager, writer, reader, selectedUser));
                    pageManager.removePage("feed");
               }    
            });
        }
    
        // for adding all selected users to a chat
        addSelectedToChat.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> invalidUsers = new ArrayList<>();
                ArrayList<String> validUsers = new ArrayList<>();

                // to be sent for chat info (change later)
                String membersList = "";
                
                try {
                    Writer.write("1", writer);
                    String[] availableUsers = reader.readLine().split(";");
                    System.out.println("read: " + Arrays.toString(availableUsers));

                    for (String username: selectedUsers) {

                        Writer.write(username, writer);

                        String validation = reader.readLine();
                        System.out.println("read: " + validation);
                        if (validation.equals("self") || validation.equals("User cannot be chatted with!")) {
                            invalidUsers.add(username);
                            continue;
                        }

                        validUsers.add(username);
                    }
                    Writer.write("[DONE]", writer);

                    // send members using only valid Users (send empty string if membersList is empty)

                    if (validUsers.size() > 0) {
                        for(int i = 0; i < validUsers.size(); i++) {
                            if (i == validUsers.size() - 1) {
                                membersList += validUsers.get(i);
                            } else {
                                membersList += validUsers.get(i) + ";";
                            }
                        }
                    }
                   
                    Writer.write(membersList, writer);

                    // check if chat was made successfully
                    String chatCreationValidation = reader.readLine();
                    System.out.println("read: " + chatCreationValidation);

                    if (chatCreationValidation.equals("[SUCCESSFUL CHAT CREATION]")) {
                        JOptionPane.showMessageDialog(null, "You've created a new chat!", 
                            "CHAT SUCCESSFULLY MADE", JOptionPane.INFORMATION_MESSAGE);
                    
                    } else if (chatCreationValidation.equals("[CHAT CREATION UNSUCCESSFUL]")) {
                        JOptionPane.showMessageDialog(null, "Chat could not be made with the selected users!", 
                            "INVALID USERS SELECTED FOR CHAT", JOptionPane.ERROR_MESSAGE);
                    }

                    

                } catch (IOException error) {
                    error.printStackTrace();
                }

                // display users that couldn't be added
                if (invalidUsers.size() > 0) {
                    String invalids = "";
                    for (String username: invalidUsers) {
                        invalids += username + "\n";
                    }
                    JOptionPane.showMessageDialog(null, invalids, 
                        "USERS WHO COULDN'T BE ADDED", JOptionPane.INFORMATION_MESSAGE);
                }
                
                // clear selectedUsers and reset the Selection Panel
                addSelectedToChat.setEnabled(false);
                for (JButton button: selectionButtons) {
                    button.setVisible(false);
                    button.setEnabled(false);
                }
                selectedUsers.clear();
            }
        });
    
    }
}
