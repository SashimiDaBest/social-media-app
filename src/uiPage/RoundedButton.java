package uiPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int radius;
    private Color backgroundColor;
    private int animationSteps = 5;
    private Timer animationTimer;
    private int currentStep = 0;
    private boolean isExpanding = false;
    private double textScaleFactor = 1.0;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        this.backgroundColor = new Color(230, 230, 230); // Light grey color

        // Remove standard button look
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        // Custom border and rendering
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Create animation timer
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateText();
            }
        });
        animationTimer.setRepeats(true);

        // Add mouse listeners to create press effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPopAnimation();
            }
        });
    }

    private void startPopAnimation() {
        currentStep = 0;
        isExpanding = true;
        animationTimer.start();
    }

    private void animateText() {
        if (isExpanding) {
            currentStep++;
            if (currentStep > animationSteps) {
                isExpanding = false;
            }
        } else {
            currentStep--;
            if (currentStep <= 0) {
                animationTimer.stop();
            }
        }

        // Calculate text scale factor
        textScaleFactor = 1 + (0.2 * (currentStep / (double)animationSteps));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        // Prepare for scaled text
        Font originalFont = getFont();
        Font scaledFont = originalFont.deriveFont((float)(originalFont.getSize() * textScaleFactor));
        g2.setFont(scaledFont);
        g2.setColor(getForeground());

        // Get text dimensions
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();

        // Draw text centered with scaling
        g2.drawString(getText(), 
            (getWidth() - textWidth) / 2, 
            (getHeight() + textHeight) / 2 - fm.getDescent());

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        // Ensure clickable area matches rounded shape
        return new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius)
            .contains(x, y);
    }
}