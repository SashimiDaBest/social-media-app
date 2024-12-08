package common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom JButton with a rounded rectangle shape and a "zoom in and zoom back out" animation effect when clicked.
 *
 * <p>This button provides a modern and visually appealing rounded appearance
 * with smooth scaling animations during mouse press events.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *     <li>Customizable corner radius for rounded edges.</li>
 *     <li>Zoom-in and shrink-back animation effect when clicked.</li>
 *     <li>Anti-aliased rendering for smooth graphics.</li>
 * </ul>
 *
 * @version 1.3 (2024-12-08)
 */
public class RoundedButton extends JButton {
    private final int radius;
    private Color backgroundColor;
    private final int animationSteps = 10;
    private final Timer animationTimer;
    private int currentStep = 0;
    private boolean isZoomingIn = true;
    private double textScaleFactor = 1.0;

    /**
     * Creates a RoundedButton with the specified text and corner radius.
     *
     * @param text   The text to display on the button.
     * @param radius The corner radius for the rounded edges.
     */
    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        this.backgroundColor = new Color(230, 230, 230); // Default light gray background

        // Remove the default button appearance
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        // Add padding for the button content
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Create an animation timer for zoom-in and shrink-back effect
        animationTimer = new Timer(3, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateZoom();
            }
        });
        animationTimer.setRepeats(true);

        // Add mouse listener for triggering animation on click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startZoomAnimation();
            }
        });
    }

    /**
     * Starts the zoom-in and shrink-back animation effect.
     */
    private void startZoomAnimation() {
        currentStep = 0;
        isZoomingIn = true;
        animationTimer.start();
    }

    /**
     * Handles the zoom-in and shrink-back animation logic.
     */
    private void animateZoom() {
        if (isZoomingIn) {
            currentStep++;
            if (currentStep > animationSteps) {
                isZoomingIn = false; // Reverse to shrink back
            }
        } else {
            currentStep--;
            if (currentStep <= 0) {
                animationTimer.stop(); // Stop animation once back to normal size
            }
        }

        // Update the text scale factor based on the animation step
        if (isZoomingIn) {
            textScaleFactor = 1.0 + (0.3 * (currentStep / (double) animationSteps)); // Scale up to 150%
        } else {
            textScaleFactor = 1.0 + (0.3 * (currentStep / (double) animationSteps)); // Scale back to 100%
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded background
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        // Scale and render the button text
        Font originalFont = getFont();
        Font scaledFont = originalFont.deriveFont((float) (originalFont.getSize() * textScaleFactor));
        g2.setFont(scaledFont);
        g2.setColor(getForeground());

        // Center the text on the button
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        g2.drawString(
                getText(),
                (getWidth() - textWidth) / 2,
                (getHeight() + textHeight) / 2 - fm.getDescent()
        );

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        // Ensure the clickable area matches the rounded shape
        return new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
    }

    /**
     * Sets the background color of the button.
     *
     * @param color The background color to set.
     */
    @Override
    public void setBackground(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}