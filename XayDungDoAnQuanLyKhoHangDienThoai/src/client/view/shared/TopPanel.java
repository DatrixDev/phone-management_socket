package client.view.shared;

import client.view.components.ButtonUtils;
import client.view.components.RoundedPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopPanel extends RoundedPanel {
    private JComboBox<String> cbxChoose;
    public JButton btnAdd, btnEdit, btnDelete, btnDetail, btnExport, btnReload;
    public JTextField txtSearch;
    public JComboBox<String> cbFilter;
    private JPanel leftPanel, rightPanel;

    public TopPanel() {
        super(20);
        this.setLayout(new BorderLayout(10, 0));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));


        leftPanel = new JPanel();
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBackground(Color.WHITE);

        btnAdd = createButton("Thêm", "/images/accountt.svg.png");
        btnAdd.setIcon(new FlatSVGIcon("images/aded.svg", 41, 41));

        btnEdit = createButton("Sửa", "/images/fixed.svg.png");
        btnEdit.setIcon(new FlatSVGIcon("images/fixed.svg", 42, 42));

        btnDelete = createButton("Xóa", "/images/delete.svg.png");
        btnDelete.setIcon(new FlatSVGIcon("images/delete.svg", 42, 42));

        btnDetail = createButton("Chi tiết", "/images/chitiet.pngs");
        btnDetail.setIcon(new FlatSVGIcon("images/chitiett.svg", 42, 42));

        btnExport = createButton("Xuất Excel", "/images/expot.svg.png");
        btnExport.setIcon(new FlatSVGIcon("images/export_excel.svg", 42, 42));


        leftPanel.add(btnAdd);
        leftPanel.add(Box.createHorizontalStrut(4));
        leftPanel.add(btnEdit);
        leftPanel.add(Box.createHorizontalStrut(4));
        leftPanel.add(btnDelete);
        leftPanel.add(Box.createHorizontalStrut(4));
        leftPanel.add(btnDetail);
        leftPanel.add(Box.createHorizontalStrut(4));
        leftPanel.add(btnExport);

        // Right panel chứa combobox, search và reload
        JPanel jpSearch = new JPanel(new BorderLayout(5,10));
        jpSearch.setBorder(new EmptyBorder(18,15,18,15));
        jpSearch.setBackground(Color.white);
        cbxChoose = new JComboBox<>();
        cbxChoose.setPreferredSize(new Dimension(140, 0));
        cbxChoose.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 0, 13));
        cbxChoose.setFocusable(false);
        jpSearch.add(cbxChoose,BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font(FlatRobotoFont.FAMILY, 0, 13));
//        txtSearch.setPreferredSize(new Dimension(190, 50));
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm...");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        jpSearch.add(txtSearch);

        btnReload = new JButton("Làm mới");
        btnReload.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 0, 14));
        btnReload.setIcon(new FlatSVGIcon("./images/refresh.svg"));
        btnReload.setBackground(Color.WHITE);
        btnReload.setPreferredSize(new Dimension(125, 0));
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jpSearch.add(btnReload,BorderLayout.EAST);


        this.add(leftPanel, BorderLayout.WEST);
        this.add(jpSearch, BorderLayout.CENTER);

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

	public JButton getBtnEdit() {
		return btnEdit;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnDetail() {
		return btnDetail;
	}

	public JButton getBtnExport() {
		return btnExport;
	}

	public JButton getBtnReload() {
		return btnReload;
	}

	public JTextField getTxtSearch() {
		return txtSearch;
	}

	public JComboBox<String> getCbFilter() {
		return cbFilter;
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

    public JComboBox getCbxChoose() {
        return cbxChoose;
    }
}
