package client.view.shared;

import client.view.components.ButtonUtils;
import client.view.components.RoundedPanel;
import client.view.components.TextFieldUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopPanel_Two extends RoundedPanel {
    public JButton btnAdd, btnEdit, btnDelete, btnDetail, btnExport, btnReload;
    public JTextField txtSearch;
    private JPanel leftPanel, rightPanel;

    public TopPanel_Two() {
        super(20);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 6, -1, 6));

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);

        btnAdd = createButton("Thêm", "/images/accountt.svg.png");
        btnAdd.setIcon(new FlatSVGIcon("images/aded.svg", 41, 41));

        btnDelete = createButton("Xóa", "/images/delete.svg.png");
        btnDelete.setIcon(new FlatSVGIcon("images/huyphieu.svg", 42, 42));
        btnDetail = createButton("Chi tiết", "/images/chitiet.pngs");
        btnDetail.setIcon(new FlatSVGIcon("images/chitiett.svg", 42, 42));

        btnExport = createButton("Xuất Excel", "/images/expot.svg.png");
        btnExport.setIcon(new FlatSVGIcon("images/export_excel.svg", 42, 42));

        leftPanel.add(btnAdd);
        leftPanel.add(btnDelete);
        leftPanel.add(btnDetail);
        leftPanel.add(btnExport);

        // Right panel chứa combobox, search và reload
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 13, 25));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(190, 31));
        TextFieldUtils.setPlaceholder(txtSearch, "Nhập nội dung tìm kiếm..");

        btnReload = new JButton("Làm mới");
        btnReload.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        btnReload.setBackground(Color.WHITE);
        btnReload.setPreferredSize(new Dimension(100, 29));
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.setFocusPainted(false);
        btnReload.setFont(new Font("Arial", Font.BOLD, 13));

        rightPanel.add(txtSearch);
        rightPanel.add(btnReload);

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(110, 80));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ButtonUtils.setIconButton(button, iconPath, 38, 38);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.decode("#BBDEFA"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        return button;
    }
    public JButton getBtnAdd(){
        return btnAdd;
    }
    public JButton getBtnDelete() {
        return btnDelete;
    }
    public JButton getBtnEdit() {
        return btnEdit;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public void setBtnEdit(JButton btnEdit) {
        this.btnEdit = btnEdit;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public void setBtnDetail(JButton btnDetail) {
        this.btnDetail = btnDetail;
    }

    public JButton getBtnExport() {
        return btnExport;
    }

    public void setBtnExport(JButton btnExport) {
        this.btnExport = btnExport;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    public void setBtnReload(JButton btnReload) {
        this.btnReload = btnReload;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(JTextField txtSearch) {
        this.txtSearch = txtSearch;
    }



    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(JPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(JPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public JButton getBtnDetail() {
        return btnDetail;
    }
}
