package client.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorFadePanel extends JPanel {
    private Color color1 = new Color(232, 241, 241);
    private Color color2 = new Color(173, 216, 216);
    private float progress = 0f;
    private int direction = 1; // 1: tăng, -1: giảm
    private Timer timer;

    public ColorFadePanel() {
        setPreferredSize(new Dimension(400, 300));

        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 0.01f * direction;

                // Đảo chiều khi chạm biên
                if (progress >= 1f) {
                    progress = 1f;
                    direction = -1;
                } else if (progress <= 0f) {
                    progress = 0f;
                    direction = 1;
                }

                repaint();
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color currentColor = blend(color1, color2, progress);
        g.setColor(currentColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private Color blend(Color c1, Color c2, float ratio) {
        int r = (int) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio);
        int g = (int) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio);
        int b = (int) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio);
        return new Color(r, g, b);
    }
}
