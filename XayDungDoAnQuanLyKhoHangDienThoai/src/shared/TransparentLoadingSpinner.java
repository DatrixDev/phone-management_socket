package shared;

import javax.swing.*;
import java.awt.*;

public class TransparentLoadingSpinner extends JWindow {
    private int angle = 0;
    public final Timer timer;

    public TransparentLoadingSpinner(JFrame parent) {
        setBackground(new Color(0, 0, 0, 0)); // Nền trong suốt
        setSize(100, 100);
        setLocationRelativeTo(parent);
        setAlwaysOnTop(true);

        // Tạo Timer xoay mượt mà
        timer = new Timer(100, e -> {
            angle = (angle + 30) % 360;
            repaint(); // Gọi vẽ lại
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Đừng quên gọi super để tránh lỗi nền
        Graphics2D g2 = (Graphics2D) g.create();

        int size = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = size / 5;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 12; i++) {
            float alpha = (i + 1) / 12f;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.GRAY);

            double theta = Math.toRadians((angle + i * 30) % 360);
            int x = (int) (centerX + radius * Math.cos(theta));
            int y = (int) (centerY + radius * Math.sin(theta));

            g2.fillOval(x - 5, y - 5, 10, 10);
        }

        g2.dispose();
    }

    public void showSpinner(int milliseconds) {
        setVisible(true);
        timer.start();

        // Sau X giây tự tắt spinner
        new Timer(milliseconds, e -> {
            timer.stop();
            setVisible(false);
            dispose();
        }).start();
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("App chính");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JButton btn = new JButton("Hiện loading");
        mainFrame.add(btn, BorderLayout.SOUTH);

        mainFrame.setVisible(true);

        btn.addActionListener(e -> {
            TransparentLoadingSpinner spinner = new TransparentLoadingSpinner(mainFrame);
            spinner.showSpinner(3000); // hiện 3 giây
        });
    }
}
