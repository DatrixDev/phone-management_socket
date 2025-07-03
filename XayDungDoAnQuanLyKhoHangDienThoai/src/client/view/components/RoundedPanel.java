package client.view.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int radius;
    private Color backgroundColor; // Added to store the background color

    public RoundedPanel(int radius) {
        this(radius, UIManager.getColor("Panel.background")); // Default to system panel background
    }

    public RoundedPanel(int radius, Color backgroundColor) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        setOpaque(false); // Make the panel transparent so we can custom paint the rounded background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the fill color to the specified background color
        g2d.setColor(backgroundColor);

        // Fill the rounded rectangle
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }

    // You might also want to add setters if you plan to change these properties dynamically
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint(); // Repaint to reflect the change
    }

    @Override
    public Color getBackground() {
        // Override getBackground to return our custom background color
        return backgroundColor;
    }

    @Override
    public void setBackground(Color bg) {
        // Override setBackground to update our custom background color
        this.backgroundColor = bg;
        repaint(); // Repaint to reflect the change
    }
}