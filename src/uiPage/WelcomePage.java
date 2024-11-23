package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class WelcomePage extends JPanel {
    private String username;
    private String password;

    private JLabel title = new JLabel("Welcome to Boiler Gram!", JLabel.CENTER);
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passwordLabel = new JLabel("Password: ");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton signInButton = new JButton("Sign In");
    private JLabel newAccount = new JLabel("Don't have an account?", JLabel.CENTER);
    private JButton newAccountButton = new JButton("Sign Up");

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public WelcomePage(CardLayout cardLayout, JPanel cardPanel, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        setLayout(new BorderLayout());

        //1st Panel - Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        //2nd Panel - Text Input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        //Add Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(usernameLabel, gbc);

        //Add Username Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameField, gbc);

        //Add Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(passwordLabel, gbc);

        //Add Password Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(passwordField, gbc);

        //3rd Panel - Button
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(signInButton);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(newAccount);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(newAccountButton);

        //4th Panel - Group All Components
        JPanel ultimatePanel = new JPanel();
        ultimatePanel.setLayout(new BoxLayout(ultimatePanel, BoxLayout.Y_AXIS));
        ultimatePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ultimatePanel.add(titlePanel);
        ultimatePanel.add(inputPanel);
        ultimatePanel.add(optionsPanel);

        add(ultimatePanel, BorderLayout.CENTER);
        setupActionListeners();
    }

    public JButton getSignInButton() {
        return signInButton;
    }

    public JButton getNewAccountButton() {
        return newAccountButton;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    private void setupActionListeners() {

        getSignInButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "sign in button clicked", "INFO", JOptionPane.INFORMATION_MESSAGE);
                UserPageClient.write("1", bufferedWriter);
                String username = getUsernameField().getText();
                String password = new String(getPasswordField().getPassword());
                UserPageClient.write(username, bufferedWriter);
                UserPageClient.write(password, bufferedWriter);

                String messageFromServer = "";
                try {
                    messageFromServer = bufferedReader.readLine();
                    if (messageFromServer == null) {
                        throw new IOException("Server closed the connection");
                    }
                    // Process the message
                } catch (IOException ex) {
                    ex.printStackTrace(); // Log for debugging
                    JOptionPane.showMessageDialog(null, "Communication error with the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (messageFromServer.equals("Successful sign-in")) {
                    System.out.println("You have entered the user feed!");
                    cardLayout.show(cardPanel, "feedViewPage");
                } else if (messageFromServer.equals("Sign-in was unsuccessful")) {
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        getNewAccountButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "create button clicked", "INFO", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "createUserPage");
            }
        });
    }
}
