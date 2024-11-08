import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class WelcomePage extends JPanel {

    public WelcomePage() {
        setLayout(new BorderLayout());
        //Set label, set font, add top and bottom padding
        JLabel label = new JLabel("Welcome to Boiler Gram!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(new EmptyBorder(50, 0, 10, 0));

        //Set label, set font
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        //set text and password field
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));

        // "Forget Password?" Button
        JButton forgetPasswordButton = new JButton("Forget Password?");
        forgetPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));
        forgetPasswordButton.setContentAreaFilled(false); // Make the button background transparent
        forgetPasswordButton.setBorderPainted(false); // Remove border

        // "OR" Label
        JLabel orText = new JLabel("-------- OR --------", JLabel.CENTER);

        // New Account Section
        JLabel newAccount = new JLabel("Don't have an account?", JLabel.CENTER);
        newAccount.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton newAccountButton = new JButton("Sign Up");
        newAccountButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a Panel for Username and Password Fields
        JPanel signInPanel = new JPanel();
        signInPanel.setLayout(new GridBagLayout());
        signInPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // No extra horizontal space for the label
        gbc.fill = GridBagConstraints.NONE;
        signInPanel.add(usernameLabel, gbc);

        // Add Username Field
        gbc.gridx = 1;
        gbc.weightx = 1; // Request extra horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make the text field expand horizontally
        signInPanel.add(usernameField, gbc);

        // Add Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // No extra horizontal space for the label
        gbc.fill = GridBagConstraints.NONE;
        signInPanel.add(passwordLabel, gbc);

        // Add Password Field
        gbc.gridx = 1;
        gbc.weightx = 1; // Request extra horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make the text field expand horizontally
        signInPanel.add(passwordField, gbc);

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
        optionsPanel.add(Box.createVerticalStrut(10)); // Space between elements
        optionsPanel.add(forgetPasswordButton);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(orText);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(newAccount);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(newAccountButton);

        // Add Components to the Main Panel
        add(label, BorderLayout.NORTH);
        add(signInPanel, BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.SOUTH);
    }

}
