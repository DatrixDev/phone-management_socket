package client.view.shared;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toast extends JDialog {
    private float opacity = 0f;

    public Toast(JFrame owner, String title, String message, int duration, ImageIcon iconCustom) {
        super(owner);
        setUndecorated(true);
        setLayout(new BorderLayout());
        setOpacity(opacity);

        // ===== Panel chính =====
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2));
        panel.setBackground(Color.WHITE);

        // ==== Icon xử lý tái sử dụng ====
        if (iconCustom == null) {
            iconCustom = new ImageIcon(getClass().getResource("/images/success.png"));
        }
        Image scaledImage = iconCustom.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(scaledImage));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 20, 1, 20));
        panel.add(icon, BorderLayout.WEST);

        // ==== Nội dung ====
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 128, 0));
        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(lblTitle);
        textPanel.add(lblMessage);

        panel.add(textPanel, BorderLayout.CENTER);
        add(panel);

        // ===== Vị trí Toast =====
        int x = owner.getX() + owner.getWidth() / 2 - 150;
        int y = owner.getY() + 20;
        setBounds(x, y, 320, 70);

        // ===== Hiệu ứng hiện mờ dần =====
        Timer fadeIn = new Timer(7, null);
        fadeIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1f) {
                    opacity = 1f;
                    fadeIn.stop();

                    // Chờ duration rồi fadeOut
                    new Timer(duration, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            fadeOut();
                        }
                    }).start();
                }
                setOpacity(opacity);
            }
        });
        fadeIn.start();
        setVisible(true);
    }

    private void fadeOut() {
        Timer fadeOut = new Timer(7, null);
        fadeOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0f) {
                    opacity = 0f;
                    fadeOut.stop();
                    dispose();
                }
                setOpacity(opacity);
            }
        });
        fadeOut.start();
    }
}

