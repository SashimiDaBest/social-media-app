package uiPage;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class CreateUserPage extends JComponent {

    private JLabel title = new JLabel("Boiler Gram!", JLabel.CENTER);
    private JLabel slogan = new JLabel("Sign up to text all your boilermakers!", JLabel.CENTER);
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passwordLabel = new JLabel("Password: ");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton signUpButton = new JButton("Sign Up");

    public CreateUserPage(CardLayout cardLayout, JPanel cardPanel) {
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
}
