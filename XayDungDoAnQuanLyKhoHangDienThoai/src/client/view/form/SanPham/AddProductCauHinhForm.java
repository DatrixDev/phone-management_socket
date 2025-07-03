package client.view.form.SanPham;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatIntelliJLaf;
import server.DAO.MauSacDAO;
import server.DAO.RamDAO;
import server.DAO.RomDAO;
import shared.models.mausac;
import shared.models.ram;
import shared.models.rom;


import javax.swing.*;
public class AddProductCauHinhForm extends JDialog {
    private JPanel panelTop;
    private JPanel panelContent;
    private JComboBox<mausac> txtColor;
    private JComboBox<rom> txtROM;
    private JComboBox<ram> txtRAM;
    private JTextField SoLuong;
    private JTextField txtImportPrice;
    private JTextField txtExportPrice;
    private JPanel panelContainer;
    private JPanel panelCauHinhTable;
    private JPanel panelHead;
    private JPanel panelCauHinhButton;
    private JTable table;
    private JButton btnAddSp;
    private JButton btnBack;
    private AddProductForm addProductForm;


    public AddProductCauHinhForm() {
        setTitle("Thêm sản phẩm");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());


        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("THÊM SẢN PHẨM");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        // Panel Container
        panelContainer = new JPanel(new BorderLayout());

        // Panel Container/Panel Head
        panelHead = new JPanel(new GridLayout(2, 5, 17, 0));
        panelHead.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 5));

        panelHead.add(new JLabel("Màu sắc"));
        panelHead.add(new JLabel("ROM"));
        panelHead.add(new JLabel("RAM"));
        panelHead.add(new JLabel("Giá nhập"));
        panelHead.add(new JLabel("Giá xuất"));



        txtColor = new JComboBox<>();
        List<mausac> mausacList = new MauSacDAO().getMausac();
        System.out.println(mausacList);
        if (mausacList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách xuất xứ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<mausac> mausacModel = new DefaultComboBoxModel<>();
            for (mausac ms : mausacList) {
                mausacModel.addElement(ms);
            }
            txtColor.setModel(mausacModel);

        }
        panelHead.add(wrapWithPreferredSize(txtColor, 190, 50));


        // ======== ROM =========
        txtROM = new JComboBox<>();
        List<rom> romList = new RomDAO().getRomm();
        System.out.println(romList);
        if (romList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách ROM từ cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<rom> romModel = new DefaultComboBoxModel<>();
            for (rom r : romList) {
                romModel.addElement(r);
            }
            txtROM.setModel(romModel);

        }
        panelHead.add(wrapWithPreferredSize(txtROM, 190, 50));

// ======== RAM =========
        txtRAM = new JComboBox<>();
        List<ram> ramList = new RamDAO().getRamm();
        if (ramList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách RAM từ cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<ram> ramModel = new DefaultComboBoxModel<>();
            for (ram item : ramList) {
                ramModel.addElement(item);
            }
            txtRAM.setModel(ramModel);
        }
        panelHead.add(wrapWithPreferredSize(txtRAM, 190, 50));


        txtImportPrice = new JTextField();
        panelHead.add(wrapWithPreferredSize(txtImportPrice, 190, 50));

        txtExportPrice = new JTextField();
        panelHead.add(wrapWithPreferredSize(txtExportPrice, 190, 50));


        // Panel Container/Panel Content
        panelContent = new JPanel(new BorderLayout());


        // Panel Container/Panel Content/Panel CauHinhTable
        panelCauHinhTable = new JPanel(new BorderLayout());
        String[] columns = {
                "STT", "RAM", "ROM", "Màu sắc", "Giá nhập", "Giá xuất"
        };
        Object[][] data = {};

        table = new JTable(data, columns);
        // Tắt viền ngoài
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0)); // Không có khoảng cách giữa các ô
        // Tùy chỉnh header
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245)); // Màu nền nhạt cho header
        header.setFont(new Font("Arial", Font.BOLD, 14));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        panelCauHinhTable.setBorder(BorderFactory.createEmptyBorder(10, 8, 8, 8));
        panelCauHinhTable.add(scrollPane, BorderLayout.CENTER);

        // Panel Container/Panel Content/Panel CauHinhButton
        panelCauHinhButton = new JPanel();
        panelCauHinhButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 20));
        panelCauHinhButton.setLayout(new BoxLayout(panelCauHinhButton, BoxLayout.Y_AXIS));

        panelCauHinhButton.add(Box.createVerticalStrut(3));



        SoLuong = new JTextField();
        SoLuong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        SoLuong.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelCauHinhButton.add(SoLuong);

        panelCauHinhButton.add(Box.createVerticalStrut(600));




        // Thêm Panel vào Panel Content
        panelContent.add(panelCauHinhButton, BorderLayout.EAST);
        panelContent.add(panelCauHinhTable, BorderLayout.CENTER);

        // Thêm Panel Head, Content vào Panel Container
        panelContainer.add(panelHead, BorderLayout.NORTH);
        panelContainer.add(panelContent, BorderLayout.CENTER);


        // Panel Button
        JPanel panelButton = new JPanel();
        panelButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        btnAddSp = new JButton("Thêm sản phẩm");
        btnAddSp.setBorderPainted(false);
        btnAddSp.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddSp.setPreferredSize(new Dimension(180, 40));
        btnAddSp.setBackground(new Color(51, 142, 193));
        btnAddSp.setForeground(Color.WHITE);

        btnBack = new JButton("Quay lại trang trước");
        btnBack.setBorderPainted(false);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.setBackground(Color.decode("#ECA300"));
        btnBack.setForeground(Color.WHITE);

        panelButton.add(btnAddSp);
        panelButton.add(btnBack);

        // Thêm các Panel vào JDialog
        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);


    }

    private JPanel wrapWithPreferredSize(JComponent comp, int width, int height) {
        comp.setPreferredSize(new Dimension(width, height));
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false); // Không nền để giữ màu trắng của panel chính
        wrapper.add(comp);
        return wrapper;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            new AddProductCauHinhForm().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JComboBox getTxtColor() {
        return txtColor;
    }

    public JComboBox getTxtROM() {
        return txtROM;
    }

    public JComboBox getTxtRAM() {
        return txtRAM;
    }

    public JTextField getTxtImportPrice() {
        return txtImportPrice;
    }

    public JTextField getTxtExportPrice() {
        return txtExportPrice;
    }



    public JButton getBtnAddSp() {
        return btnAddSp;
    }

    public JTextField getSoLuong() {
        return SoLuong;
    }

    public JButton getBtnBack() {
        return btnBack;
    }
    public AddProductForm getAddProductForm() {
        return addProductForm;
    }


}
