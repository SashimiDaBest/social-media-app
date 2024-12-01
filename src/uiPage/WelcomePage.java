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
    private JLabel usernameLabel = new JLabel("Username");
    private JLabel passwordLabel = new JLabel("Password");
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

        JScrollPane scrollablePanel = new JScrollPane(createMainPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollablePanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(createTitlePanel());
        mainPanel.add(createInputPanel());
        mainPanel.add(createOptionsPanel());

        return mainPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        return titlePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username Label
        inputPanel.add(usernameLabel, createGridBagConstraints(0, 0, 0, GridBagConstraints.NONE));
        // Username Field
        inputPanel.add(usernameField, createGridBagConstraints(1, 0, 1, GridBagConstraints.HORIZONTAL));
        // Password Label
        inputPanel.add(passwordLabel, createGridBagConstraints(0, 1, 0, GridBagConstraints.NONE));
        // Password Field
        inputPanel.add(passwordField, createGridBagConstraints(1, 1, 1, GridBagConstraints.HORIZONTAL));

        return inputPanel;
    }

    private JPanel createOptionsPanel() {
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

        return optionsPanel;
    }

    private GridBagConstraints createGridBagConstraints(int x, int y, int weightx, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void setupActionListeners() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();

                if (username.isEmpty() || passwordChars.length == 0) {
                    JOptionPane.showMessageDialog(null, "Username or password cannot be empty. Please try again.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    UserPageClient.write("1", bufferedWriter); // "1" for sign-in operation
                    UserPageClient.write(username, bufferedWriter);
                    UserPageClient.write(new String(passwordChars), bufferedWriter);

                    Arrays.fill(passwordChars, '\0'); // Clear password from memory

                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer == null) throw new IOException("Server closed the connection.");

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
