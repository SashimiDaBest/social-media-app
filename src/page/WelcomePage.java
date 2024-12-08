package page;

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

public class WelcomePage extends JPanel {
    private String username;
    private String password;
    private Color gold = new Color(255, 215, 0);

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
        BufferedImage image = ImageIO.read(new File("Sample Test Folder/BoilerGramLogo.png"));
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

    static class RoundedBorder implements Border {
        private int radius;
    
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
    
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0); // No extra space for border
        }
    
        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(230, 230, 230)); // Border color
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
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

        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageManager.lazyLoadPage("signup", () -> new CreateUserPage(pageManager, bufferedWriter, bufferedReader));
            }
        });
    }
}