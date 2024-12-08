package common;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A dialog for displaying a user's profile picture in a larger format.
 *
 * <p>This dialog takes a {@link BufferedImage} of the user's profile picture, scales it
 * to fit within a maximum size while maintaining the aspect ratio, and displays it
 * along with the user's username.</p>
 *
 * <p>The dialog includes a "Close" button to dismiss the window.</p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * BufferedImage profileImage = ImageIO.read(new File("profile.jpg"));
 * ProfilePictureDialog dialog = new ProfilePictureDialog(parentFrame, profileImage, "JohnDoe");
 * dialog.setVisible(true);
 * }</pre>
 *
 * @since 1.0
 * @version 12/08/2024
 */
public class ProfilePictureDialog extends JDialog {

    /**
     * Constructs a new {@code ProfilePictureDialog}.
     *
     * @param parent        The parent frame for the dialog.
     * @param originalImage The original profile picture as a {@link BufferedImage}.
     * @param username      The username of the user whose profile picture is displayed.
     */
    public ProfilePictureDialog(JFrame parent, BufferedImage originalImage, String username) {
        super(parent, "Profile Picture - " + username, true);

        // Panel to hold the image
        JPanel imagePanel = new JPanel(new BorderLayout());

        // Scale the image while maintaining aspect ratio
        int maxWidth = 300;
        int maxHeight = 300;
        Image scaledImage = scaleImage(originalImage, maxWidth, maxHeight);

        // Create a label for the scaled image
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Add padding to the image panel
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        // Button panel to hold the close button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Add components to the dialog
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Pack and center the dialog on the parent
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Scales the provided image to fit within the specified maximum width and height
     * while maintaining the aspect ratio.
     *
     * @param originalImage The original image to be scaled.
     * @param maxWidth      The maximum width for the scaled image.
     * @param maxHeight     The maximum height for the scaled image.
     * @return The scaled {@link Image} instance.
     */
    private Image scaleImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        // Calculate scaling factor to maintain aspect ratio
        double scaleFactor = Math.min(
                (double) maxWidth / originalImage.getWidth(),
                (double) maxHeight / originalImage.getHeight()
        );

        int newWidth = (int) (originalImage.getWidth() * scaleFactor);
        int newHeight = (int) (originalImage.getHeight() * scaleFactor);

        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
}
