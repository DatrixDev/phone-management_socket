package shared;

import javax.swing.*;
import java.awt.*;

public class MiniSidebarPanel extends JPanel {
    public MiniSidebarPanel(Runnable onOpenClick) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Panel chứa nút và label trên cùng
        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Nút menu ≡
        JButton openBtn = new JButton("≡");
        openBtn.setFont(new Font("Arial", Font.PLAIN, 28));
        openBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        openBtn.setFocusPainted(false);
        openBtn.setContentAreaFilled(false);
        openBtn.setBorderPainted(false);

        openBtn.addActionListener(e -> onOpenClick.run());

        // Label dòng chữ
        JLabel label = new JLabel("HỆ THỐNG QUẢN LÝ KHO ĐIỆN THOẠI");
        label.setForeground(new Color(43, 137, 143));
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(43, 137, 143));
        topBar.setBackground(Color.WHITE);

        // Canh giữa: thêm glue hai bên
        topBar.add(openBtn);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(label);
        topBar.add(Box.createHorizontalGlue());


        add(topBar);
    }
}
