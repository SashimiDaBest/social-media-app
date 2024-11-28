package uiPage;

import clientPageOperation.UserPageClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
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

    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private static final String SUCCESSFUL_SIGN_IN = "Successful sign-in";
    private static final String UNSUCCESSFUL_SIGN_IN = "Sign-in was unsuccessful";

    public WelcomePage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.pageManager = pageManager;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        setLayout(new BorderLayout());


        //1st Panel - Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        //2nd Panel - Text Input Panel
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

        //3rd Panel - Options Panel
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

        //4th Panel - Combine Panels
        JPanel ultimatePanel = new JPanel();
        ultimatePanel.setLayout(new BoxLayout(ultimatePanel, BoxLayout.Y_AXIS));
        ultimatePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ultimatePanel.add(titlePanel);
        ultimatePanel.add(inputPanel);
        ultimatePanel.add(optionsPanel);

        add(ultimatePanel, BorderLayout.CENTER);
        setupActionListeners();
    }

    private void setupActionListeners() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();

                if (username.isEmpty() || passwordChars.length == 0) {
                    JOptionPane.showMessageDialog(null, "Username or password cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    UserPageClient.write("1", bufferedWriter); // "1" for sign-in operation
                    UserPageClient.write(username, bufferedWriter);
                    UserPageClient.write(new String(passwordChars), bufferedWriter);

                    Arrays.fill(passwordChars, '\0'); // Clear password from memory

                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer == null) throw new IOException("Server closed the connection");

                    if (SUCCESSFUL_SIGN_IN.equals(messageFromServer)) {
                        pageManager.lazyLoadPage("feed", () -> new FeedViewPage(pageManager, bufferedWriter, bufferedReader));
                    } else if (UNSUCCESSFUL_SIGN_IN.equals(messageFromServer)) {
                        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Communication error with the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.lazyLoadPage("signup", () -> new CreateUserPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
    }
}
