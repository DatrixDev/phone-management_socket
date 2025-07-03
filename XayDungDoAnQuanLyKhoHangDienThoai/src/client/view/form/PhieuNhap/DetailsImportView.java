package client.view.form.PhieuNhap;

import client.view.components.RoundedButton;
import client.view.components.TextFieldUtils;
import client.view.shared.BaseView;
import client.view.shared.SidebarMenu;
import server.DAO.NhaCungCapDAO;
import server.DAO.SanPhamDAO;
import server.DAO.phieuNhapDAO;
import shared.models.NhaCungCap;
import shared.models.NhanVien;
import shared.models.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class DetailsImportView  extends BaseView {

    private JPanel container;
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private SidebarMenu sidebarMenu ;


    // LEFT PANEL
    private JPanel leftPanel;
    private JPanel productPanel, infoPanel, bottomPanel;
    private JPanel buttonPanel, buttonPanel_info;
    private JPanel prinPanel;
    private JTextField txtSearch,comboBox_Config;
    private JTable table_Product;
    private JButton btnAdd;
    private JLabel lblName, lblID, lblPrice, lblMethod, lblImei, lblQuantity, lblConfiguration, lblSpace;
    private JTextField txtName, txtID, txtPrice, txtImei, txtQuantity;
    private JComboBox<String>  comboBox_Method;
    private JButton btnEdit, btnDelete;
    private JTable bottomTable;

    // RIGHT PANEL
    private JPanel rightPanel;
    private JPanel importing_goods, inner_price;
    private JLabel lbl_Id_import, lbl_Employ, lbl_Supplier, lblTotal, lblTotalPrice;
    private JTextField txtId_import, txt_Employ;
    private JComboBox<NhaCungCap> comboBox_Supplier;
    private JButton btnImport_goods;
    private NhanVien nhanVien;

    private int Imei = 1;





    public DetailsImportView(NhanVien nhanVien) {

        super();
        this.nhanVien = nhanVien;

        container = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(213, 221, 220));
        leftPanel.setPreferredSize(new Dimension(870, 0));
        rightPanel = new JPanel(new BorderLayout());
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);

        productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(new Color(213, 221, 220));
        productPanel.setPreferredSize(new Dimension(430, 0));

        JPanel inner_info = new JPanel(new GridLayout(13, 2, 30, 5));

        Font labelFont = new Font("Arial", Font.BOLD, 13); // Font chữ to và đậm

        inner_info.add(lblID = new JLabel("Mã sản phẩm"));
        lblID.setFont(labelFont);

        inner_info.add(lblName = new JLabel("Tên sản phẩm"));
        lblName.setFont(labelFont);

        inner_info.add(txtID = new JTextField(20));
        inner_info.add(txtName = new JTextField(20));
        txtID.setBackground(new Color(237, 235, 235));
        txtName.setBackground(new Color(237, 235, 235));
        txtID.setEditable(false);
        txtName.setEditable(false);

        inner_info.add(lblConfiguration = new JLabel("Cấu hình"));
        lblConfiguration.setFont(labelFont);

        inner_info.add(lblPrice = new JLabel("Giá nhập"));
        lblPrice.setFont(labelFont);

        inner_info.add(comboBox_Config = new JTextField(20));
        inner_info.add(txtPrice = new JTextField(20));
        txtPrice.setEditable(false);
        comboBox_Config.setEditable(false);
        comboBox_Config.setBackground(new Color(237, 235, 235));

        txtPrice.setBackground(new Color(237, 235, 235));


        inner_info.add(lblMethod = new JLabel("Phương thức nhập"));
        lblMethod.setFont(labelFont);

        inner_info.add(lblSpace = new JLabel(" "));
        inner_info.add(comboBox_Method = new JComboBox<>(new String[]{"Nhập từng máy","Nhập theo lô"}));

        inner_info.add(lblSpace = new JLabel(" "));

        inner_info.add(lblImei = new JLabel("Mã Imei "));
        lblImei.setFont(labelFont);

        inner_info.add(lblQuantity = new JLabel("Số lượng"));
        lblQuantity.setFont(labelFont);
        lblQuantity.setVisible(false);


        inner_info.add(txtImei = new JTextField(20));
        inner_info.add(txtQuantity = new JTextField(20));
        txtQuantity.setVisible(false);



        JPanel buttonPanel_info = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 12));
        btnEdit = new RoundedButton("Sửa sản phẩm", 40);
        btnEdit.setPreferredSize(new Dimension(140, 34));
        btnEdit.setBackground(new Color(216, 215, 215));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Arial", Font.BOLD, 14));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setEnabled(false);

        btnDelete = new RoundedButton("Xóa sản phẩm", 40);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setPreferredSize(new Dimension(140, 34));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setBackground(new Color(216, 215, 215));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setEnabled(false);
        buttonPanel_info.add(btnEdit);
        buttonPanel_info.add(btnDelete);

        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(213, 221, 220));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(9, 10, 0, 0));
        inner_info.setBackground(Color.WHITE);
        inner_info.setBorder(BorderFactory.createEmptyBorder(4, 10, 0, 10));
        buttonPanel_info.setBackground(Color.WHITE);
        infoPanel.add(inner_info, BorderLayout.CENTER);
        infoPanel.add(buttonPanel_info, BorderLayout.SOUTH);

        prinPanel = new JPanel(new BorderLayout());
        prinPanel.setPreferredSize(new Dimension(0, 540));
        prinPanel.add(productPanel, BorderLayout.WEST);
        prinPanel.add(infoPanel, BorderLayout.CENTER);


//        // LEFT - TOP: Product Panel
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(190, 31));
        JPanel txtSearch_Panel = new JPanel(new BorderLayout());
        txtSearch_Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));
        txtSearch_Panel.add(txtSearch, BorderLayout.CENTER);
        txtSearch_Panel.setBackground(Color.WHITE);
        txtSearch_Panel.setBorder(BorderFactory.createMatteBorder(10, 9, 10, 0, new Color(213, 221, 220)));


        TextFieldUtils.setPlaceholder(txtSearch, "Nhập nội dung tìm kiếm..");
        productPanel.add(txtSearch_Panel, BorderLayout.NORTH);
        String[] Columns = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng"};
        Object[][] Data = {

        };

        DefaultTableModel model = new DefaultTableModel(Data, Columns);

        table_Product = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table_Product);


        formatTable(table_Product);
        JScrollPane scrollPaneProduct = new JScrollPane(table_Product);
        scrollPaneProduct.setBackground(Color.WHITE);

        scrollPaneProduct.setBorder(BorderFactory.createEmptyBorder(7, 0, 10, 0));
        scrollPaneProduct.setBorder(BorderFactory.createMatteBorder(0,10,0,0,new Color(213, 221, 220)));

        productPanel.add(scrollPaneProduct, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBackground(Color.WHITE);
        btnAdd = new RoundedButton("Thêm sản phẩm", 40);
        btnAdd.setPreferredSize(new Dimension(140, 34));
        btnAdd.setBackground(new Color(96, 138, 104));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(0,10,0,0,new Color(213,221,220)));
        buttonPanel.add(btnAdd);
        productPanel.add(buttonPanel, BorderLayout.SOUTH);


//
        String[] columns = {"STT", "Mã SP", "Tên sản phẩm", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng","Imei","Method"};
        Object[][] data = {};



        DefaultTableModel model1 = new DefaultTableModel(data, columns);

        bottomTable = new JTable(model1);
        TableColumnModel columnModel = bottomTable.getColumnModel();
        columnModel.getColumn(9).setMinWidth(0);
        columnModel.getColumn(9).setMaxWidth(0);
        columnModel.getColumn(9).setWidth(0);
        formatTable(bottomTable);
//
        JScrollPane table_bottom = new JScrollPane(bottomTable);
        table_bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        table_bottom.setBackground(new Color(213, 221, 220));

        leftPanel.add(prinPanel, BorderLayout.NORTH);
        leftPanel.add(table_bottom, BorderLayout.CENTER);


        JPanel innerRight = new JPanel(new GridLayout(20, 1, 0, 5));

        innerRight.add(lbl_Id_import = new JLabel("Mã phiếu nhập"));
        innerRight.add(txtId_import = new JTextField("NV01"));
        String displayId = "PN" + generateUniqueImportNumber();
        txtId_import.setText(displayId);

        innerRight.add(lbl_Employ = new JLabel("Nhân viên nhập"));
        innerRight.add(txt_Employ = new JTextField("Quốc Đọt"));
        sidebarMenu = getSidebarMenu();
        txt_Employ.setText(hienThiTenNhanVien());

        innerRight.add(lbl_Supplier = new JLabel("Nhà cung cấp"));
        txtId_import.setEditable(false);
        txtId_import.setBackground(new Color(244, 243, 243));
        txt_Employ.setEditable(false);
        txt_Employ.setBackground(new Color(246, 245, 245));



        comboBox_Supplier = new JComboBox<>();
        List<NhaCungCap> nhacungcap = new NhaCungCapDAO().selectAll();
        System.out.println(nhacungcap + "có nhà cung cấp");
        if (nhacungcap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<NhaCungCap> nhacungcapModel = new DefaultComboBoxModel<>();
            for (NhaCungCap th : nhacungcap) {
                nhacungcapModel.addElement(th);
            }
            comboBox_Supplier.setModel(nhacungcapModel);
            comboBox_Supplier.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof NhaCungCap) {
                        setText(((NhaCungCap) value).getTenncc());
                    }
                    return this;
                }
            });
        }
        innerRight.add(comboBox_Supplier);

        innerRight.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        JPanel innerBottom = new JPanel(new GridLayout(2, 1, 10, 13));
        importing_goods = new JPanel(new FlowLayout(FlowLayout.CENTER));
        importing_goods.add(lblTotal = new JLabel("TỔNG TIỀN:   "));



        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);
        importing_goods.add(lblTotalPrice = new JLabel("VNĐ"));
        lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 18));

        inner_price = new JPanel(new GridLayout(1, 1, 0, 0));
        inner_price.setBorder(BorderFactory.createEmptyBorder(0, 27, 10, 27));
        inner_price.add(btnImport_goods = new RoundedButton("Nhập hàng", 40));
        btnImport_goods.setPreferredSize(new Dimension(140, 37));
        btnImport_goods.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImport_goods.setFont(new Font("Arial", Font.BOLD, 14));
        btnImport_goods.setBackground(new Color(96, 138, 104));
        btnImport_goods.setForeground(Color.WHITE);

        innerBottom.add(importing_goods);
        innerBottom.add(inner_price);

        rightPanel.add(innerRight, BorderLayout.CENTER);
        rightPanel.setBackground(new Color(213, 221, 220));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(1, 10, 0, 10));
        rightPanel.add(innerBottom, BorderLayout.SOUTH);


        getMainPanel().add(container, BorderLayout.CENTER);

        updateTable(sanPhamDAO.getAllSanPham());

    }
    public String  hienThiTenNhanVien() {
        return    nhanVien.getHoten();
    }


    public void resetImportId() {
        String newDisplayId = "PN" + generateUniqueImportNumber();
        txtId_import.setText(newDisplayId);
    }

    private void formatTable(JTable table) {
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setFont(new Font("Arial", Font.BOLD, 13));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void updateTable(List<?> list) {
        if (list.isEmpty()) return;
        if (!(list.get(0) instanceof SanPham)) return;

        List<SanPham> sanPhamList = (List<SanPham>) list;

        DefaultTableModel model = (DefaultTableModel) getTable_Product().getModel();
        model.setRowCount(0);

        for (SanPham sp : sanPhamList) {
            model.addRow(new Object[]{
                    sp.getId(),
                    sp.getTenSanPham(),
                    sp.getSoLuong(),

            });
        }
    }
    private int generateUniqueImportNumber() {
        int number;
        phieuNhapDAO dao = new phieuNhapDAO();

        do {
            number = (int) (Math.random() * 10000); // từ 0–9999
        } while (dao.existsImportId(number)); // kiểm tra trùng số

        return number;
    }


    public JPanel getContenPanel(){
        return mainPanel;
    }

    public JPanel getContainer() {
        return container;
    }


    public void setContainer(JPanel container) {
        this.container = container;
    }




    public JTextField getTxtSearch() {
        return txtSearch;
    }


    public JTable getTable_Product() {
        return table_Product;
    }


    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }


    public JLabel getLblImei() {
        return lblImei;
    }


    public JLabel getLblQuantity() {
        return lblQuantity;
    }



    public JTextField getTxtName() {
        return txtName;
    }


    public JTextField getTxtID() {
        return txtID;
    }

    public JTextField getTxtPrice() {
        return txtPrice;
    }

    public JTextField getTxtImei() {
        return txtImei;
    }

    public JTextField getTxtQuantity() {
        return txtQuantity;
    }

    public JComboBox<String> getComboBox_Method() {
        return comboBox_Method;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JTable getBottomTable() {
        return bottomTable;
    }

    public JLabel getLblTotalPrice() {
        return lblTotalPrice;
    }

    public JTextField getTxtId_import() {
        return txtId_import;
    }

    public JComboBox<NhaCungCap> getComboBox_Supplier() {
        return comboBox_Supplier;
    }

    public JTextField getComboBox_Config() {
        return comboBox_Config;
    }

    public JButton getBtnImport_goods() {
        return btnImport_goods;
    }

}
