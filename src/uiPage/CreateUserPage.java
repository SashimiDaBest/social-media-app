package uiPage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.border.EmptyBorder;

public class CreateUserPage extends JPanel {
    private static final String USER_CREATION_SUCCESSFUL = "User creation successful";
    private static final String INVALID_FIELDS = "Invalid fields";

    private JLabel title = new JLabel("Welcome to Boiler Gram!", SwingConstants.CENTER);
    private JLabel slogan = new JLabel("Sign up to text all your boilermakers!", SwingConstants.CENTER);
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton signUpButton = new JButton("Sign Up");
    private JButton backButton = new JButton("Cancel");
    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public CreateUserPage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.pageManager = pageManager;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    private JPanel createMainPanel() {
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        titlePanel.setBackground(Color.WHITE);
        title.setFont(new Font("Roboto", Font.BOLD, 40));
        slogan.setFont(new Font("Roboto", Font.PLAIN, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5)); // Reduced vertical gap between labels
        titlePanel.add(slogan);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameField, gbc);
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setFont(new Font("Roboto", Font.PLAIN, 20));
        usernameLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
        ));

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(passwordField, gbc);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(new Font("Roboto", Font.PLAIN, 20));
        passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
        ));

        // Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.add(signUpButton);
        optionsPanel.add(backButton);

        signUpButton.setPreferredSize(new Dimension(130, 30));
        signUpButton.setFont(new Font("Roboto", Font.BOLD, 20));
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setFont(new Font("Roboto", Font.BOLD, 20));


        // Group All Components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(optionsPanel);

        return mainPanel;
    }

    private void setupActionListeners() {
        signUpButton.addActionListener(e -> handleSignUp());
        backButton.addActionListener(e -> {
            pageManager.showPage("welcome");
            pageManager.removePage("signup");
        });
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();

        // Validate inputs
        String validationError = validateInputs(username, passwordChars);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Error", JOptionPane.ERROR_MESSAGE);
            clearPassword(passwordChars);
            return;
        }

        try {
            // Send sign-up request to server
            Writer.write("2", bufferedWriter); // "2" for sign-up operation
            Writer.write(username, bufferedWriter);
            Writer.write(new String(passwordChars), bufferedWriter);
            clearPassword(passwordChars);

            String response = bufferedReader.readLine();
            System.out.println("read: " + response);
            if (response == null) throw new IOException("Server closed the connection.");

            if (USER_CREATION_SUCCESSFUL.equals(response)) {
                JOptionPane.showMessageDialog(null, "Account created successfully! Redirecting to login page.", "Success", JOptionPane.INFORMATION_MESSAGE);
                pageManager.showPage("welcome");
                pageManager.removePage("signup");
            } else if (INVALID_FIELDS.equals(response)) {
                JOptionPane.showMessageDialog(null, "Invalid fields. Please check your input and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Communication error with the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String validateInputs(String username, char[] password) {
        if (username.isEmpty() || username.contains(";")) {
            usernameField.requestFocus();
            return "Invalid username. Please avoid special characters like ';'.";
        }
        if (password.length < 10) {
            passwordField.requestFocus();
            return "Password must be at least 10 characters long.";
        }
        if (!containsLetterAndDigit(password)) {
            passwordField.requestFocus();
            return "Password must contain letters and numbers.";
        }
        if (containsSemicolon(password)) {
            passwordField.requestFocus();
            return "Password must not include semicolons.";
        }
        return null;
    }

    private void clearPassword(char[] password) {
        Arrays.fill(password, '\0'); // Clear sensitive data
    }

    private boolean containsLetterAndDigit(char[] password) {
        boolean hasLetter = false, hasDigit = false;
        for (char ch : password) {
            if (Character.isLetter(ch)) hasLetter = true;
            if (Character.isDigit(ch)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }

    private boolean containsSemicolon(char[] password) {
        for (char ch : password) {
            if (ch == ';') return true;
        }
        return false;
    }
}