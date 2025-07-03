package client.view.shared;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import server.DAO.NhanVienDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SidebarMenu extends JPanel {
    private JButton btnHome, btnProduct, btnProductInfo, btnCustomer, btnEmployee, btnAccount, btnStatistic, btnLogout,
            btnImport, btnExport, btnMess, btnSupplier, btnStock;
    private JLabel lblTitle, lblNhomQuyen;
    private JLabel lblAvata;
    private Runnable onCloseListener;


    private final List<JButton> allButtons = new ArrayList<>();
    private final Color defaultColor = new Color(92, 181, 181);
    private final Color hoverColor = new Color(241, 248, 248);
    private final Color activeColor = new Color(241, 248, 248);
    private JButton activeButton;
    public NhanVienDAO nhanVienDAO;
    private JButton btnClose;

    public SidebarMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        setBackground(defaultColor);

        lblAvata = new JLabel();
        lblAvata.setIcon(new FlatSVGIcon("images/account.svg", 46, 46));
        lblAvata.setPreferredSize(new Dimension(50, 50));
        lblAvata.setMaximumSize(new Dimension(50, 50));
        lblAvata.setMinimumSize(new Dimension(50, 50));
        lblAvata.setAlignmentY(Component.TOP_ALIGNMENT);
        lblAvata.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));


        lblTitle = new JLabel("Tên người dùng");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        lblTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblNhomQuyen = new JLabel("Quyền: ");
        lblNhomQuyen.setForeground(Color.DARK_GRAY);
        lblNhomQuyen.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

        JPanel topUserPanel = new JPanel(new BorderLayout());
        topUserPanel.setOpaque(false);
        topUserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topUserPanel.add(Box.createHorizontalStrut(10));
        topUserPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        btnClose = new JButton();
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setIcon(new FlatSVGIcon("images/closemenu.svg", 18, 18));
        btnClose.setOpaque(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btnClose.addActionListener(e -> {
            if (onCloseListener != null) onCloseListener.run();
        });

        infoPanel.add(lblTitle);
        infoPanel.add(lblNhomQuyen);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(lblAvata);
        leftPanel.add(infoPanel);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(btnClose);
        rightPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topUserPanel.add(leftPanel, BorderLayout.WEST);
        topUserPanel.add(rightPanel, BorderLayout.EAST);

        add(topUserPanel);

        btnHome = createSidebarButton("  Trang chủ", "images/house.svg");
        btnProduct = createSidebarButton("  Kho hàng", "images/sanpham.svg");
        btnProductInfo = createSidebarButton("  Thông số", "images/thuocTinh.svg");
        btnStock = createSidebarButton("  Tầng kho", "images/mapppp.svg");
        btnImport = createSidebarButton("  Nhập hàng", "images/phieunhap.svg");
        btnExport = createSidebarButton("  Xuất hàng", "images/exportt.svg");
        btnSupplier = createSidebarButton("  Bên giao", "images/nhacungcapp.svg");
        btnCustomer = createSidebarButton("  Khách hàng", "images/khachhang.svg");
        btnMess = createSidebarButton("  Tin nhắn", "images/messs.svg");
        btnEmployee = createSidebarButton("  Nhân viên", "images/nahnvienn.svg");
        btnAccount = createSidebarButton("  Tài khoản", "images/accc.svg");
        btnStatistic = createSidebarButton("  Số liệu", "images/thonmgke.svg");
        btnLogout = createSidebarButton("  Đăng xuất", "images/log_out.svg");
        btnLogout.setIconTextGap(-2);

        allButtons.add(btnHome);
        allButtons.add(btnProduct);
        allButtons.add(btnProductInfo);
        allButtons.add(btnStock);
        allButtons.add(btnImport);
        allButtons.add(btnExport);
        allButtons.add(btnSupplier);
        allButtons.add(btnCustomer);
        allButtons.add(btnMess);
        allButtons.add(btnEmployee);
        allButtons.add(btnAccount);
        allButtons.add(btnStatistic);
        allButtons.add(btnLogout);

        add(Box.createVerticalStrut(20));
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(Box.createVerticalStrut(10));

        add(btnHome);
        add(btnProduct);
        add(btnProductInfo);
        add(btnStock);
        add(btnImport);
        add(btnExport);
        add(btnSupplier);
        add(btnCustomer);
        add(btnMess);
        add(btnEmployee);
        add(btnAccount);
        add(btnStatistic);

        add(Box.createVerticalGlue());
        add(btnLogout);
    }

    private JButton createSidebarButton(String text, String svgPath) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBorderPainted(false);
        button.setBackground(defaultColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setMargin(new Insets(10, 20, 10, 10));

        button.setMaximumSize(new Dimension(220, 45));
        button.setPreferredSize(new Dimension(220, 45));

        button.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái trong layout Box

        button.setIcon(new FlatSVGIcon(svgPath, 28, 28));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton)
                    button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton)
                    button.setBackground(defaultColor);
            }
        });

        return button;
    }

    public String anButtonTheoQuyen(int manhomquyen) {
        String tenNhomQuyen = "";
        switch (manhomquyen) {
            case 2 -> {
                btnCustomer.setVisible(false);
                btnEmployee.setVisible(false);
                btnAccount.setVisible(false);
                btnStatistic.setVisible(false);
                tenNhomQuyen = "Nhân viên kho";
            }
            case 3 -> {
                btnProductInfo.setVisible(false);
                btnImport.setVisible(false);
                btnEmployee.setVisible(false);
                btnAccount.setVisible(false);
                btnSupplier.setVisible(false);
                tenNhomQuyen = "Nhân viên xuất hàng";
            }
            case 4 -> {
                btnProductInfo.setVisible(false);
                btnImport.setVisible(false);
                btnExport.setVisible(false);
                btnCustomer.setVisible(false);
                btnEmployee.setVisible(false);
                btnAccount.setVisible(false);
                btnSupplier.setVisible(false);
                tenNhomQuyen = "Nhân viên giám sát";
            }
            case 5 -> {
                btnHome.setVisible(false);
                btnProductInfo.setVisible(false);
                btnImport.setVisible(false);
                btnExport.setVisible(false);
                btnEmployee.setVisible(false);
                btnAccount.setVisible(false);
                btnStatistic.setVisible(false);
                btnSupplier.setVisible(false);
                tenNhomQuyen = "Chăm sóc khách hàng";
            }
            default -> {
                tenNhomQuyen = "Quản lý";
            }
        }

        revalidate();
        repaint();
        return tenNhomQuyen;
    }


    public void setActiveButton(JButton button) {
        resetAllButtonColors();
        button.setBackground(activeColor);
        activeButton = button;
    }

    public void resetAllButtonColors() {
        for (JButton btn : allButtons) {
            btn.setBackground(defaultColor);
        }
    }
    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnProduct() {
        return btnProduct;
    }

    public JButton getBtnCustomer() {
        return btnCustomer;
    }

    public JButton getBtnEmployee() {
        return btnEmployee;
    }

    public JButton getBtnAccount() {
        return btnAccount;
    }

    public JButton getBtnStatistic() {
        return btnStatistic;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JButton getBtnImport() {
        return btnImport;
    }

    public JButton getBtnExport() {
        return btnExport;
    }

    public JButton getBtnMess() {
        return btnMess;
    }

    public JButton getBtnProductInfo() {
        return btnProductInfo;
    }

    public JButton getBtnSupplier() {
        return btnSupplier;
    }

    public void setOnCloseListener(Runnable onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public JButton getBtnStock() {
        return btnStock;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public JLabel getLblNhomQuyen() {
        return lblNhomQuyen;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }
}