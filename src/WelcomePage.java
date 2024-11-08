import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class WelcomePage extends JPanel {

    public WelcomePage() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Welcome to Boiler Gram!", JLabel.CENTER);
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton signInButton = new JButton("Sign In");
        JButton forgetPasswordButton = new JButton("Forget Password?");
        JLabel orText = new JLabel("-------- OR --------", JLabel.CENTER);
        JLabel newAccount = new JLabel("Don't have an account?", JLabel.CENTER);
        JButton newAccountButton = new JButton("Sign Up");

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(usernameLabel, gbc);

        // Add Username Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameField, gbc);

        // Add Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(passwordLabel, gbc);

        // Add Password Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(passwordField, gbc);

        // Create a Panel for Buttons and Extra Options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgetPasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orText.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        optionsPanel.add(signInButton);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(forgetPasswordButton);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(orText);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(newAccount);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(newAccountButton);

        JPanel ultimatePanel = new JPanel();
        ultimatePanel.setLayout(new BoxLayout(ultimatePanel, BoxLayout.Y_AXIS));
        ultimatePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        ultimatePanel.add(titlePanel);
        ultimatePanel.add(inputPanel);
        ultimatePanel.add(optionsPanel);

        add(ultimatePanel, BorderLayout.CENTER);
    }

}
