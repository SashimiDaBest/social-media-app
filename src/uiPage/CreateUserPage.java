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

public class CreateUserPage extends JPanel {
    private static final String USER_CREATION_SUCCESSFUL = "User creation successful";
    private static final String INVALID_FIELDS = "Invalid fields";

    private JLabel title = new JLabel("Boiler Gram!", JLabel.CENTER);
    private JLabel slogan = new JLabel("Sign up to text all your boilermakers!", JLabel.CENTER);
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passwordLabel = new JLabel("Password: ");
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
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(signUpButton);
        optionsPanel.add(backButton);

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

    private void setupActionListeners() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();

                // Input validation
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid username. Please type in a username.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (username.isEmpty() || username.contains(";")) {
                    JOptionPane.showMessageDialog(null, "Invalid username. Please avoid special characters like ';'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (passwordChars.length < 10) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 10 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    Arrays.fill(passwordChars, '\0'); // Clear password for security
                    return;
                }
                if (!containsLetterAndDigit(passwordChars)) {
                    JOptionPane.showMessageDialog(null, "Password must contain letters and numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    Arrays.fill(passwordChars, '\0'); // Clear password for security
                    return;
                }
                if (containsSemicolon(passwordChars)) {
                    JOptionPane.showMessageDialog(null, "Password must not include semicolons.", "Error", JOptionPane.ERROR_MESSAGE);
                    Arrays.fill(passwordChars, '\0'); // Clear password for security
                    return;
                }

                try {
                    UserPageClient.write("2", bufferedWriter); // "2" for sign-up operation
                    UserPageClient.write(username, bufferedWriter);
                    UserPageClient.write(new String(passwordChars), bufferedWriter);

                    Arrays.fill(passwordChars, '\0'); // Clear password after use

                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer == null) throw new IOException("Server closed the connection");

                    if (USER_CREATION_SUCCESSFUL.equals(messageFromServer)) {
                        JOptionPane.showMessageDialog(null, "Account created successfully! Redirecting to login page.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        pageManager.showPage("welcome"); // Assuming "welcomePage" is the login page
                    } else if (INVALID_FIELDS.equals(messageFromServer)) {
                        JOptionPane.showMessageDialog(null, "Invalid fields. Please check your input and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Communication error with the server. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // Log error for debugging
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.showPage("welcome");
            }
        });
    }

    private boolean containsLetterAndDigit(char[] password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
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