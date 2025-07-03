package client.view.views;

import client.view.components.ColorFadePanel;
import client.view.components.RoundedPanel;
import client.view.form.ChangePasswordForm;
import client.view.shared.BaseView;

import client.view.shared.SidebarMenu;
import server.DAO.NhanVienDAO;
import shared.models.NhanVien;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainView extends BaseView {
    private NhanVien nhanVien;
    private NhomQuyen nhomQuyen;
    private TaiKhoan taiKhoan;
    public SidebarMenu sideBarMenu;
    private String tenQuyen;

    private JPanel contentPanel, titlePanel, bodyPanel, panel1, panel2, panel3, panelExactly, panelSecurity, panelEffectiveness;

    public MainView(NhanVien nhanVien, TaiKhoan taiKhoan) {
        this.nhanVien = nhanVien;

        contentPanel = new JPanel(new BorderLayout());
        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(250, 255, 255));

        JLabel titleLabel = new JLabel("Quản lý hiệu quả – Thành công vững bền - Công nghệ tiên phong – Dẫn đầu xu hướng!", JLabel.CENTER);
        titleLabel.setForeground(new Color(43, 137, 143));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 21));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel);

        ColorFadePanel bodyPanel = new ColorFadePanel();
        bodyPanel.setLayout(new GridLayout(1, 3, 70, 0));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 70, 60));

        panelExactly = createPanelImage("/images/accuracy.png");
        panelSecurity = createPanelImage("/images/security.png");
        panelEffectiveness = createPanelImage("/images/effectiveness.png");

        panel1 = createPanel(
                "QUẢN LÝ CHÍNH XÁC",
                "Hệ thống hỗ trợ theo dõi số lượng và thông tin chi tiết của từng mẫu điện thoại trong kho, đảm bảo dữ liệu luôn đầy đủ, chính xác và được cập nhật theo thời gian thực.",
                "/images/accuracy.png"
        );

        panel2 = createPanel(
                "TÍNH ỔN ĐỊNH",
                "Phân quyền người dùng và ghi nhận lịch sử nhập/xuất hàng giúp kiểm soát hoạt động kho chặt chẽ, hạn chế thất thoát và sai sót trong quá trình vận hành.",
                "/images/security.png"
        );

        panel3 = createPanel(
                "HIỆU QUẢ TỐI ƯU",
                "Giao diện trực quan, dễ sử dụng, hỗ trợ nhân viên thao tác nhanh chóng khi tra cứu, thêm mới hoặc điều chỉnh thông tin sản phẩm, nâng cao hiệu suất công việc.",
                "/images/effectiveness.png"
        );

        bodyPanel.add(panel1);
        bodyPanel.add(panel2);
        bodyPanel.add(panel3);

        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(bodyPanel, BorderLayout.CENTER);

        getMainPanel().add(contentPanel, BorderLayout.CENTER);
        sideBarMenu = getSidebarMenu();
        sideBarMenu.getLblTitle().setText(hienThiTenNhanVien());
        sideBarMenu.getLblTitle().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ChangePasswordForm form = new ChangePasswordForm(sideBarMenu, nhanVien, MainView.this);
                form.setVisible(true);
            }
        });
        tenQuyen =  sideBarMenu.anButtonTheoQuyen(taiKhoan.getManhomquyen());
       sideBarMenu.getLblNhomQuyen().setText(tenQuyen);
    }

    public String hienThiTenNhanVien() {
        return nhanVien.getHoten();
    }

    public String hienThiTenNhomQuyen() {
        return nhomQuyen.getTennhomquyen();
    }

    // Method to refresh employee information after updates
    public void refreshEmployeeInfo() {
        // Reload employee data from database
        NhanVien updatedNhanVien = NhanVienDAO.getInstance().selectById(nhanVien.getManv() + "");
        if (updatedNhanVien != null) {
            this.nhanVien = updatedNhanVien;
            // Update the sidebar title with new employee name
            sideBarMenu.getLblTitle().setText(hienThiTenNhanVien());
            // Refresh the sidebar menu display
            sideBarMenu.repaint();
            sideBarMenu.revalidate();
        }
    }

    private JPanel createPanelImage(String imagePath) {
        RoundedPanel panel = new RoundedPanel(40);
        panel.setBackground(new Color(250, 255, 255));
        URL url = MainView.class.getResource(imagePath);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel labelImage = new JLabel(new ImageIcon(img));
            panel.add(labelImage);
        } else {
            System.err.println("Không tìm thấy ảnh: " + imagePath);
        }
        return panel;
    }

    private JPanel createPanel(String title, String description, String imagePath) {
        RoundedPanel panel = new RoundedPanel(40);
        panel.setBackground(new Color(250, 255, 255));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Panel chứa hình ảnh
        JPanel imagePanel = createPanelImage(imagePath);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
        panel.add(imagePanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setForeground(new Color(43, 137, 143));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(titleLabel);

        // Mô tả
        JTextPane descPane = new JTextPane();
        descPane.setForeground(new Color(109, 110, 113));
        descPane.setText(description);
        descPane.setFont(new Font("Arial", Font.PLAIN, 14));
        descPane.setOpaque(false);
        descPane.setEditable(false);
        descPane.setFocusable(false);
        descPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Căn đều văn bản
        StyledDocument doc = descPane.getStyledDocument();
        SimpleAttributeSet justify = new SimpleAttributeSet();
        StyleConstants.setAlignment(justify, StyleConstants.ALIGN_JUSTIFIED);
        doc.setParagraphAttributes(0, doc.getLength(), justify, false);

        descPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(descPane);

        return panel;
    }

    // Getters and setters
    public JPanel getContentPanel() {
        return contentPanel;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public SidebarMenu getSideBarMenu() {
        return sideBarMenu;
    }

    public void setSideBarMenu(SidebarMenu sideBarMenu) {
        this.sideBarMenu = sideBarMenu;
    }

    public void setContentPanel(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }

    public void setTitlePanel(JPanel titlePanel) {
        this.titlePanel = titlePanel;
    }

    public JPanel getBodyPanel() {
        return bodyPanel;
    }

    public void setBodyPanel(JPanel bodyPanel) {
        this.bodyPanel = bodyPanel;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public JPanel getPanel2() {
        return panel2;
    }

    public void setPanel2(JPanel panel2) {
        this.panel2 = panel2;
    }

    public JPanel getPanel3() {
        return panel3;
    }

    public void setPanel3(JPanel panel3) {
        this.panel3 = panel3;
    }

    public NhomQuyen getNhomQuyen() {
        return nhomQuyen;
    }

    public void setNhomQuyen(NhomQuyen nhomQuyen) {
        this.nhomQuyen = nhomQuyen;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    public JPanel getPanelExactly() {
        return panelExactly;
    }

    public void setPanelExactly(JPanel panelExactly) {
        this.panelExactly = panelExactly;
    }

    public JPanel getPanelSecurity() {
        return panelSecurity;
    }

    public void setPanelSecurity(JPanel panelSecurity) {
        this.panelSecurity = panelSecurity;
    }

    public JPanel getPanelEffectiveness() {
        return panelEffectiveness;
    }

    public void setPanelEffectiveness(JPanel panelEffectiveness) {
        this.panelEffectiveness = panelEffectiveness;
    }
}