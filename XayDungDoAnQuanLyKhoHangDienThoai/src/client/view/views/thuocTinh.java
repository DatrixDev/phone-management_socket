package client.view.views;

import client.controller.thuocTinhController;
import client.view.shared.BaseView;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class thuocTinh extends BaseView {
    private JPanel mainPanel, pn1, pn2, pn3, pn4, pn5, pn6;
    private JButton btnRam, btnRom, btnMausac, btnxuatxu, btnThuongHieu, btnHeDieuHanh;
    private thuocTinhController thuocTinhController;

    public thuocTinh() throws Exception {
        super();
        mainPanel = new JPanel(new GridLayout(3, 2, 25, 25));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));


        // Khởi tạo các panel con với FlowLayout để căn giữa
        pn1 = createSubPanel();
        pn2 = createSubPanel();
        pn3 = createSubPanel();
        pn4 = createSubPanel();
        pn5 = createSubPanel();
        pn6 = createSubPanel();

        // Tạo nút và đặt icon SVG
        btnThuongHieu = createButton("Thương hiệu", "images/thuonghieu.svg", pn5);
        btnxuatxu = createButton("Xuất xứ", "images/xuatxu.svg", pn3);
        btnHeDieuHanh = createButton("Hệ điều hành", "images/hedieuhanh.svg", pn6);
        btnRam = createButton("Ram", "images/ram.svg", pn1);
        btnRom = createButton("Rom", "images/rom.svg", pn2);
        btnMausac = createButton("Màu sắc", "images/mausac.svg", pn4);

        // Thêm nút vào các panel
        pn5.add(btnThuongHieu);
        pn3.add(btnxuatxu);
        pn6.add(btnHeDieuHanh);
        pn1.add(btnRam);
        pn2.add(btnRom);
        pn4.add(btnMausac);

        // Thêm panel vào mainPanel theo đúng vị trí hình ảnh
        mainPanel.add(pn5); // Thương hiệu
        mainPanel.add(pn3); // Xuất xứ
        mainPanel.add(pn6); // Hệ điều hành
        mainPanel.add(pn1); // RAM
        mainPanel.add(pn2); // ROM
        mainPanel.add(pn4); // Màu sắc

        add(mainPanel);
        thuocTinhController = new thuocTinhController(this);

    }

    private JPanel createSubPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(260, 270));
        return panel;
    }

    private JButton createButton(String text, String svgPath,JPanel parentPanel) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(280, 250));
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setIconTextGap(20);

        button.setIcon(new FlatSVGIcon(svgPath, 100, 100));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(192, 218, 218));
                parentPanel.setBackground(new Color(192, 218, 218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                parentPanel.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getContentPanel() {
        return mainPanel;
    }

    // Getters/setters nếu cần dùng ngoài


    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setPn1(JPanel pn1) {
        this.pn1 = pn1;
    }

    public void setPn2(JPanel pn2) {
        this.pn2 = pn2;
    }

    public void setPn3(JPanel pn3) {
        this.pn3 = pn3;
    }

    public void setPn4(JPanel pn4) {
        this.pn4 = pn4;
    }

    public void setPn5(JPanel pn5) {
        this.pn5 = pn5;
    }

    public void setPn6(JPanel pn6) {
        this.pn6 = pn6;
    }

    public JButton getBtnRam() {
        return btnRam;
    }

    public void setBtnRam(JButton btnRam) {
        this.btnRam = btnRam;
    }

    public JButton getBtnRom() {
        return btnRom;
    }

    public void setBtnRom(JButton btnRom) {
        this.btnRom = btnRom;
    }

    public JButton getBtnMausac() {
        return btnMausac;
    }

    public void setBtnMausac(JButton btnMausac) {
        this.btnMausac = btnMausac;
    }

    public JButton getBtnxuatxu() {
        return btnxuatxu;
    }

    public void setBtnxuatxu(JButton btnxuatxu) {
        this.btnxuatxu = btnxuatxu;
    }

    public JButton getBtnThuongHieu() {
        return btnThuongHieu;
    }

    public void setBtnThuongHieu(JButton btnThuongHieu) {
        this.btnThuongHieu = btnThuongHieu;
    }

    public JButton getBtnHeDieuHanh() {
        return btnHeDieuHanh;
    }

    public void setBtnHeDieuHanh(JButton btnHeDieuHanh) {
        this.btnHeDieuHanh = btnHeDieuHanh;
    }

    public JPanel getPn1() {
        return pn1;
    }

    public JPanel getPn2() {
        return pn2;
    }

    public JPanel getPn3() {
        return pn3;
    }

    public JPanel getPn4() {
        return pn4;
    }

    public JPanel getPn5() {
        return pn5;
    }

    public JPanel getPn6() {
        return pn6;
    }
}
