package page;

import common.PageManager;
import common.RoundedButton;
import common.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.util.Arrays;
/**
 * Represents the welcome page of the application.
 * This class extends JPanel and provides a user interface for signing in or creating a new account.
 * It includes methods to set up the main panel, title panel, input panel, options panel, and action listeners for various UI components.
 */
public class WelcomePage extends JPanel {
    /**
     * The username entered by the user.
     */
    private String username;

    /**
     * The password entered by the user.
     */
    private String password;

    /**
     * The gold color used for UI elements.
     */
    private Color gold = new Color(255, 215, 0);

    /**
     * The title label of the welcome page.
     */
    private JLabel title = new JLabel("Welcome to Boiler Gram!", JLabel.CENTER);

    /**
     * The label for the username input field.
     */
    private JLabel usernameLabel = new JLabel("Username");

    /**
     * The label for the password input field.
     */
    private JLabel passwordLabel = new JLabel("Password");

    /**
     * The text field for entering the username.
     */
    private JTextField usernameField = new JTextField(15);

    /**
     * The password field for entering the password.
     */
    private JPasswordField passwordField = new JPasswordField(15);

    /**
     * The button for signing in.
     */
    private JButton signInButton = new JButton("Sign In");

    /**
     * The label prompting the user to sign up if they don't have an account.
     */
    private JLabel newAccount = new JLabel("Don't have an account?", JLabel.CENTER);

    /**
     * The button for signing up.
     */
    private JButton newAccountButton = new JButton("Sign Up");

    /**
     * The PageManager instance used for managing page navigation.
     */
    private PageManager pageManager;

    /**
     * The BufferedReader instance used for reading data from the server.
     */
    private BufferedReader bufferedReader;

    /**
     * The BufferedWriter instance used for writing data to the server.
     */
    private BufferedWriter bufferedWriter;

    /**
     * The message indicating a successful sign-in.
     */
    private static final String SUCCESSFUL_SIGN_IN = "Successful sign-in";

    /**
     * The message indicating an unsuccessful sign-in.
     */
    private static final String UNSUCCESSFUL_SIGN_IN = "Sign-in was unsuccessful";
    /**
     * Constructs a WelcomePage object.
     * Initializes the page layout, sets up the main panel, title panel, input panel, options panel,
     * and adds action listeners to the UI components.
     * @param pageManager The PageManager instance to handle page navigation.
     * @param bufferedWriter The BufferedWriter instance for writing data.
     * @param bufferedReader The BufferedReader instance for reading data.
     */
    public WelcomePage(PageManager pageManager, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        System.out.println("This is welcome page");
        this.pageManager = pageManager;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create the main panel
        JPanel mainPanel = createMainPanel();
        
        // Wrap the main panel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Make scrolling smoother
        
        // Add the scroll pane to the panel
        add(scrollPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(600, 800));
        setupActionListeners();
    }
    /**
     * Creates and returns the main panel of the welcome page.
     * This panel contains the title panel, input panel, and options panel, arranged vertically.
     * @return The main panel containing the title, input fields, and buttons.
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createTitlePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some vertical spacing
        mainPanel.add(createInputPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some vertical spacing
        mainPanel.add(createOptionsPanel());
        mainPanel.add(Box.createVerticalGlue());
        return mainPanel;
    }
    /**
     * Creates and returns the title panel of the welcome page.
     * This panel contains the application title and a logo image.
     * @return The title panel containing the title and logo.
     */
    private JPanel createTitlePanel() {
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setForeground(Color.BLACK);
    title.setFont(new Font("Roboto", Font.BOLD, 36));
    titlePanel.add(title);
    titlePanel.setBackground(Color.WHITE);

    // Load and resize the image
    try {
        BufferedImage image = ImageIO.read(new File("SampleTestFolder/BoilerGramLogo.png"));
        int width = 200;  // Desired width
        int height = (int) ((double) width / image.getWidth() * image.getHeight());  // Maintain aspect ratio
        ImageIcon resizedImageIcon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(resizedImageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(imageLabel);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading logo image. Please check the file path.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    return titlePanel;
}
    /**
     * Creates and returns the input panel of the welcome page.
     * This panel contains the username and password input fields.
     * @return The input panel containing the username and password fields.
     */
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        usernameField.setFont(new Font("Roboto", Font.PLAIN, 18));
        usernameField.setBackground(new Color(230, 230, 230)); // Light grey
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
        ));        
        usernameField.setCaretColor(Color.BLACK);


        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        passwordField.setFont(new Font("Roboto", Font.PLAIN, 18));
        passwordField.setBackground(new Color(230, 230, 230)); // Light grey
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Added padding
        ));        
        passwordField.setCaretColor(Color.BLACK);

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

    
    /**
     * Creates and returns the options panel of the welcome page.
     * This panel contains the sign-in and sign-up buttons.
     * @return The options panel containing the sign-in and sign-up buttons.
     */
    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        optionsPanel.setBackground(Color.WHITE);
        Border outline = BorderFactory.createLineBorder(Color.BLACK, 2);

        signInButton = new RoundedButton("Sign In", 18);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setForeground(Color.BLACK);
        signInButton.setFont(new Font("Roboto", Font.PLAIN, 18));


        newAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccount.setForeground(Color.BLACK);
        newAccount.setFont(new Font("Roboto", Font.PLAIN, 18));

        newAccountButton = new RoundedButton("Sign Up", 18);
        newAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newAccountButton.setForeground(Color.BLACK);
        newAccountButton.setFont(new Font("Roboto", Font.PLAIN, 18));

        optionsPanel.add(signInButton);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(newAccount);
        optionsPanel.add(Box.createVerticalStrut(5));
        optionsPanel.add(newAccountButton);

        return optionsPanel;
    }
    /**
     * A custom border implementation that draws a rounded rectangle.
     * This class implements the Border interface to provide a rounded border for UI components.
     */
    static class RoundedBorder implements Border {
        private int radius;
        /**
         * Constructs a RoundedBorder object with the specified radius.
         * @param radius The radius of the rounded corners.
         */
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        /**
         * Returns the insets of the border.
         * @param c The component for which this border insets value applies.
         * @return The insets of the border.
         */
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0); // No extra space for border
        }
        /**
         * Returns whether the border is opaque.
         * @return true if the border is opaque, false otherwise.
         */
        @Override
        public boolean isBorderOpaque() {
            return true;
        }
        /**
         * Paints the border for the specified component with the specified position and size.
         * @param c The component for which this border is being painted.
         * @param g The graphics context.
         * @param x The x-coordinate of the top-left corner of the border.
         * @param y The y-coordinate of the top-left corner of the border.
         * @param width The width of the border.
         * @param height The height of the border.
         */
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(230, 230, 230)); // Border color
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
    
    
    /**
     * Creates and returns a GridBagConstraints object with the specified parameters.
     * This method is used to configure the layout constraints for components in a GridBagLayout.
     * @param x The grid x position.
     * @param y The grid y position.
     * @param weightx The weight in the x direction.
     * @param fill The fill behavior.
     * @return A GridBagConstraints object configured with the specified parameters.
     */
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
    /**
     * Sets up action listeners for various UI components.
     * This method assigns action listeners to buttons and other UI elements
     * to handle user interactions such as signing in, signing up, and navigating to other pages.
     */
    private void setupActionListeners() {
        /**
     * Action listener for the sign-in button.
     * Validates the username and password, sends the credentials to the server,
     * and navigates to the feed page if the sign-in is successful.
     * If the sign-in is unsuccessful, an error message is displayed.
     */
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
                    Writer.write("1", bufferedWriter); // "1" for sign-in operation
                    System.out.println("write: " + "1");
                    Writer.write(username, bufferedWriter);
                    System.out.println("write: " + username);
                    Writer.write(new String(passwordChars), bufferedWriter);
                    System.out.println("write: " + new String(passwordChars));

                    Arrays.fill(passwordChars, '\0'); // Clear password from memory

                    String messageFromServer = bufferedReader.readLine();
                    System.out.println("read: " + messageFromServer);
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
        /**
     * Action listener for the sign-up button.
     * Navigates to the sign-up page when the button is clicked.
     */
        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.lazyLoadPage("signup", () -> new CreateUserPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
    }
}