package client.view.shared;

import shared.MiniSidebarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BaseView extends JFrame {
    protected JPanel mainPanel;
    protected CardLayout cardLayout;
    protected SidebarMenu sidebarMenu;
    protected MiniSidebarPanel miniSidebar;
    protected JPanel containerPanel;
    private final int SIDEBAR_WIDTH = 220;
    private boolean sidebarVisible = false;

    public BaseView() {
        setTitle("Hệ Thống Quản Lý Kho Hàng");
        setSize(1300, 770);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Container chính chứa cả sidebar và nội dung
        containerPanel = new JPanel(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Main panel dùng CardLayout để chuyển view
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // MiniSidebarPanel + nền trắng bọc ngoài
        JPanel miniWrapper = new JPanel(new BorderLayout());
        miniWrapper.setBackground(Color.WHITE);

        miniSidebar = new MiniSidebarPanel(this::openSidebar);
        miniWrapper.add(miniSidebar, BorderLayout.CENTER);
        containerPanel.add(miniWrapper, BorderLayout.NORTH);

        // Sidebar ẩn ban đầu
        sidebarMenu = new SidebarMenu();
        sidebarMenu.setPreferredSize(new Dimension(0, getHeight()));
        sidebarMenu.setOnCloseListener(this::closeSidebar);
        containerPanel.add(sidebarMenu, BorderLayout.WEST);

        // Cập nhật lại kích thước sidebar khi resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int height = getHeight();
                sidebarMenu.setPreferredSize(new Dimension(sidebarVisible ? SIDEBAR_WIDTH : 0, height));
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });
    }

    // Mở sidebar trượt
    public void openSidebar() {
        miniSidebar.setVisible(false);

        new Thread(() -> {
            for (int width = 0; width <= SIDEBAR_WIDTH; width += 10) {
                sidebarMenu.setPreferredSize(new Dimension(width, getHeight()));
                SwingUtilities.invokeLater(() -> {
                    containerPanel.revalidate();
                    containerPanel.repaint();
                });
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {
                }
            }
            sidebarVisible = true;
        }).start();
    }

    // Đóng sidebar trượt
    public void slideCloseSidebar() {
        new Thread(() -> {
            for (int width = SIDEBAR_WIDTH; width >= 0; width -= 10) {
                sidebarMenu.setPreferredSize(new Dimension(width, getHeight()));
                SwingUtilities.invokeLater(() -> {
                    containerPanel.revalidate();
                    containerPanel.repaint();
                });
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ignored) {
                }
            }
            sidebarVisible = false;
            SwingUtilities.invokeLater(() -> {
                miniSidebar.setVisible(true);
            });
        }).start();
    }

    public void closeSidebar() {
        slideCloseSidebar();
    }

    public void addView(JPanel panel, String name) {
        mainPanel.add(panel, name);
    }

    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }

    public SidebarMenu getSidebarMenu() {
        return sidebarMenu;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MiniSidebarPanel getMiniSidebar() {
        return miniSidebar;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }
}
