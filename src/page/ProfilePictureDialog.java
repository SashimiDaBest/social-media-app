package page;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProfilePictureDialog extends JDialog {
    public ProfilePictureDialog(JFrame parent, BufferedImage originalImage, String username) {
        super(parent, "Profile Picture - " + username, true);

        // Create a panel to hold the image
        JPanel imagePanel = new JPanel(new BorderLayout());

        // Scale the image to a larger size while maintaining aspect ratio
        int maxWidth = 300;
        int maxHeight = 300;
        Image scaledImage = scaleImage(originalImage, maxWidth, maxHeight);

        // Create a label with the scaled image
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Add some padding
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Add components to dialog
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Pack and center the dialog
        pack();
        setLocationRelativeTo(parent);
    }

    private Image scaleImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        // Calculate the scaling factor to fit within max dimensions while maintaining aspect ratio
        double scaleFactor = Math.min(
                (double)maxWidth / originalImage.getWidth(),
                (double)maxHeight / originalImage.getHeight()
        );

        int newWidth = (int)(originalImage.getWidth() * scaleFactor);
        int newHeight = (int)(originalImage.getHeight() * scaleFactor);

        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
}