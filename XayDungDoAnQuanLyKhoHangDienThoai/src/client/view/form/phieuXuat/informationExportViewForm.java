package client.view.form.phieuXuat;

import client.view.components.RoundedButton;
import server.DAO.chiTietSanPhamDAO;
import shared.models.ChiTietPhieuXuatDTO;
import shared.models.phieuXuat;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class informationExportViewForm extends JDialog {
    private JPanel container;
    private JPanel panelTop;
    private JPanel panelContent;
    private JPanel inner_Content,inner_right_Content;
    private JPanel panelBottom;
    private JLabel lblTitle;
    private JButton btnClose, btnExport;
    private JLabel lblID, lblEmployee, lblSupplier, lblTime;
    private JTextField txtID, txtEmployee, txtSupplier, txtTime;
    private JTable table,table_right;
    private DefaultTableModel tableModel,tableModel_right;


    public informationExportViewForm() {
        setTitle("Thông tin phiếu nhập");
        setSize(970, 470);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // ==== Top Panel ====
        panelTop = new JPanel(new BorderLayout());
        lblTitle = new JLabel("THÔNG TIN PHIẾU XUẤT");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        // ==== Content ====
        panelContent = new JPanel(new BorderLayout());
        inner_Content = new JPanel(new GridLayout(2, 4, 30, 10));
        inner_Content.add(lblID = new JLabel("Mã phiếu"));
        inner_Content.add(lblEmployee = new JLabel("Nhân viên xuất"));
        inner_Content.add(lblSupplier = new JLabel("Khách Hàng"));
        inner_Content.add(lblTime = new JLabel("Thời gian tạo"));
        inner_Content.add(txtID = new JTextField(20));
        inner_Content.add(txtEmployee = new JTextField(20));
        inner_Content.add(txtSupplier = new JTextField(20));
        inner_Content.add(txtTime = new JTextField(20));

        panelContent.add(inner_Content, BorderLayout.NORTH);

        // ==== Table ====
        String[] columns = {
                "STT", "Mã SP", "Tên SP", "RAM", "ROM", "Màu sắc", "Đơn Giá", "Số lượng"
        };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245));
        header.setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));


        // ==== Table ====
        String[] column = {
                "STT", "Imei"
        };
        tableModel_right = new DefaultTableModel(column, 0);
        table_right = new JTable(tableModel_right);
        table_right.setShowHorizontalLines(false);
        table_right.setShowVerticalLines(false);
        table_right.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader headerr = table_right.getTableHeader();
        headerr.setReorderingAllowed(false);
        headerr.setResizingAllowed(false);
        headerr.setBackground(new Color(245, 245, 245));
        headerr.setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer centerRendererr = new DefaultTableCellRenderer();
        centerRendererr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table_right.getColumnCount(); i++) {
            table_right.getColumnModel().getColumn(i).setCellRenderer(centerRendererr);
        }

        JScrollPane scrollPanee = new JScrollPane(table_right);
        scrollPanee.setPreferredSize(new Dimension(220, 235));
        scrollPanee.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        inner_right_Content = new JPanel();
        inner_right_Content.add(scrollPanee);


        panelContent.setBorder(BorderFactory.createEmptyBorder(10, 8, 8, 8));
        panelContent.add(scrollPane, BorderLayout.CENTER);
        panelContent.add(inner_right_Content, BorderLayout.EAST);

        // ==== Bottom Buttons ====
        panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        btnExport = new RoundedButton("Xuất file PDF", 40);
        btnExport.setFont(new Font("Arial", Font.BOLD, 14));
        btnExport.setBackground(Color.decode("#3289B9"));
        btnExport.setForeground(Color.WHITE);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.setPreferredSize(new Dimension(150, 40));

        btnClose = new RoundedButton("Hủy bỏ", 40);
        btnClose.setPreferredSize(new Dimension(150, 40));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setBackground(Color.decode("#A9444E"));
        btnClose.setForeground(Color.WHITE);
        panelBottom.add(btnExport);
        panelBottom.add(btnClose);

        // ==== Container ====
        container = new JPanel(new BorderLayout());
        container.add(panelTop, BorderLayout.NORTH);
        container.add(panelContent, BorderLayout.CENTER);
        container.add(panelBottom, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
    }


    // ==== Load dữ liệu ====
    public void loadPhieuXuat(phieuXuat phieuXuat, List<ChiTietPhieuXuatDTO> list) {
        System.out.println(list);
        if (phieuXuat != null) {
            txtID.setText(String.valueOf(phieuXuat.getMaphieuXuat()));
            txtEmployee.setText(phieuXuat.getTennhanvien());
            txtTime.setText(phieuXuat.getThoigiantao().toString());
            txtSupplier.setText(phieuXuat.getTenKhachHang());
        }

        tableModel.setRowCount(0); // Clear old data

        for (int i = 0; i < list.size(); i++) {
            ChiTietPhieuXuatDTO ct = list.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    ct.getMaSP(),
                    ct.getTenSP(),
                    ct.getRam(),
                    ct.getRom(),
                    ct.getMausac(),
                    ct.getGiaxuat(),
                    ct.getSoLuong()
            });
        }


    }

    public void loadImei(int maSP,int maphieuxuat){
        List<String> imeiList = chiTietSanPhamDAO.getImeiByMaSPMaPhieuXuat(maSP,0, maphieuxuat);
        tableModel_right.setRowCount(0);
        int stt = 1;
        for (String imei : imeiList) {
            tableModel_right.addRow(new Object[]{stt++, imei});
        }
    }

    // ==== Getters ====
    public JPanel getPanelTop() {
        return panelTop;
    }

    public JPanel getPanelContent() {
        return panelContent;
    }

    public JPanel getInner_Content() {
        return inner_Content;
    }

    public JButton getBtnClose() {
        return btnClose;
    }

    public JButton getBtnExport() {
        return btnExport;
    }

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public void setPanelTop(JPanel panelTop) {
        this.panelTop = panelTop;
    }

    public void setPanelContent(JPanel panelContent) {
        this.panelContent = panelContent;
    }

    public void setInner_Content(JPanel inner_Content) {
        this.inner_Content = inner_Content;
    }

    public JPanel getInner_right_Content() {
        return inner_right_Content;
    }

    public void setInner_right_Content(JPanel inner_right_Content) {
        this.inner_right_Content = inner_right_Content;
    }

    public JPanel getPanelBottom() {
        return panelBottom;
    }

    public void setPanelBottom(JPanel panelBottom) {
        this.panelBottom = panelBottom;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public void setBtnClose(JButton btnClose) {
        this.btnClose = btnClose;
    }

    public void setBtnExport(JButton btnExport) {
        this.btnExport = btnExport;
    }

    public JLabel getLblID() {
        return lblID;
    }

    public void setLblID(JLabel lblID) {
        this.lblID = lblID;
    }

    public JLabel getLblEmployee() {
        return lblEmployee;
    }

    public void setLblEmployee(JLabel lblEmployee) {
        this.lblEmployee = lblEmployee;
    }

    public JLabel getLblSupplier() {
        return lblSupplier;
    }

    public void setLblSupplier(JLabel lblSupplier) {
        this.lblSupplier = lblSupplier;
    }

    public JLabel getLblTime() {
        return lblTime;
    }

    public void setLblTime(JLabel lblTime) {
        this.lblTime = lblTime;
    }

    public JTextField getTxtID() {
        return txtID;
    }

    public void setTxtID(JTextField txtID) {
        this.txtID = txtID;
    }

    public JTextField getTxtEmployee() {
        return txtEmployee;
    }

    public void setTxtEmployee(JTextField txtEmployee) {
        this.txtEmployee = txtEmployee;
    }

    public JTextField getTxtSupplier() {
        return txtSupplier;
    }

    public void setTxtSupplier(JTextField txtSupplier) {
        this.txtSupplier = txtSupplier;
    }

    public JTextField getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(JTextField txtTime) {
        this.txtTime = txtTime;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JTable getTable_right() {
        return table_right;
    }

    public void setTable_right(JTable table_right) {
        this.table_right = table_right;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public DefaultTableModel getTableModel_right() {
        return tableModel_right;
    }

    public void setTableModel_right(DefaultTableModel tableModel_right) {
        this.tableModel_right = tableModel_right;
    }
}
