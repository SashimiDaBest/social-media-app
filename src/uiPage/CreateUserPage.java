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

public class CreateUserPage extends JComponent {

    private JLabel title = new JLabel("Boiler Gram!", JLabel.CENTER);
    private JLabel slogan = new JLabel("Sign up to text all your boilermakers!", JLabel.CENTER);
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passwordLabel = new JLabel("Password: ");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton signUpButton = new JButton("Sign Up");

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public CreateUserPage(CardLayout cardLayout, JPanel cardPanel, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());

        //1st Panel - Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(slogan);

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

        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(signUpButton);
        optionsPanel.add(Box.createVerticalStrut(10));

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

    public JButton getSignUpButtonButton() {
        return signUpButton;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    private void setupActionListeners() {
        getSignUpButtonButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "sign up button clicked", "INFO", JOptionPane.INFORMATION_MESSAGE);
                UserPageClient.write("2", bufferedWriter);
                String username = getUsernameField().getText();
                String password = new String(getPasswordField().getPassword());

                if (username.contains(";") || username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.length() < 10 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters long, contain a letter and a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserPageClient.write(username, bufferedWriter);
                UserPageClient.write(password, bufferedWriter);

                System.out.println("New usernames cannot contain semicolons!");
                System.out.println("New passwords must contain a letter and a number, " +
                        "be at least 10 characters, and cannot contain semicolons!");

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

                if (messageFromServer.equals("User creation successful")) {
                    System.out.println("Successfuly created new account!");
                    cardLayout.show(cardPanel, "feedViewPage");

                } else if (messageFromServer.equals("Invalid fields")) {
                    System.out.println("One of the fields is invalid, please try again");
                    JOptionPane.showMessageDialog(null, "ERROR CONDITION", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
