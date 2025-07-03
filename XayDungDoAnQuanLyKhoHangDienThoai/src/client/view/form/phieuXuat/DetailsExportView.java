package client.view.form.phieuXuat;

import client.view.components.RoundedButton;
import client.view.components.TextFieldUtils;
import client.view.shared.BaseView;
import client.view.shared.SidebarMenu;
import server.DAO.KhachHangDAO;
import server.DAO.SanPhamDAO;
import server.DAO.phieuXuatdao;
import shared.models.KhachHang;
import shared.models.NhanVien;
import shared.models.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class DetailsExportView  extends BaseView {

    private JPanel container;
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private SidebarMenu sidebarMenu ;

    // LEFT PANEL
    private JPanel leftPanel;
    private JPanel productPanel, infoPanel, bottomPanel;
    private JPanel buttonPanel, buttonPanel_info;
    private JPanel prinPanel,imeiPanel;
    private JTextField txtSearch,comboBox_Config;
    private JTable table_Product;
    private JButton btnAdd;
    private JLabel lblName, lblID, lblPrice, lblImei, lblQuantity, lblConfiguration,lblSpace;
    private JTextField txtName, txtID, txtPrice, txtQuantity,txtSpace;
    private JTextArea txtImei;
    private JButton btnEdit, btnDelete,btnImei;
    private JTable bottomTable;

    // RIGHT PANEL
    private JPanel rightPanel;
    private JPanel importing_goods, inner_price;
    private JLabel lbl_Id_import, lbl_Employ, lbl_Supplier, lblTotal, lblTotalPrice;
    private JTextField txtId_import, txt_Employ;
    private JComboBox<KhachHang> comboBox_KhachHang;
    private JButton btnImport_goods;
    private NhanVien nhanVien;

    private int Imei = 1;





    public DetailsExportView(NhanVien nhanVien) {

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

        JPanel inner_info = new JPanel(new GridLayout(7, 2, 30, 5));

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

        inner_info.add(lblPrice = new JLabel("Giá xuất"));
        lblPrice.setFont(labelFont);

        inner_info.add(comboBox_Config = new JTextField(20));
        inner_info.add(txtPrice = new JTextField(20));
        txtPrice.setEditable(false);
        comboBox_Config.setEditable(false);
        comboBox_Config.setBackground(new Color(237, 235, 235));
        txtPrice.setBackground(new Color(237, 235, 235));

        inner_info.add(lblQuantity = new JLabel("Số lượng"));
        lblQuantity.setFont(labelFont);
        inner_info.add(lblSpace = new JLabel(" "));




        inner_info.add(txtQuantity = new JTextField(20));
        txtQuantity.setEditable(false);
        inner_info.add(btnImei = new JButton("Chọn Imei "));
        inner_info.add(lblImei = new JLabel("Imei"));


        imeiPanel = new JPanel();

        lblImei.setFont(labelFont);

        inner_info.add(lblSpace = new JLabel(" "));

        txtImei = new JTextArea(" ");
        txtImei.setEditable(false);
        txtImei.setBackground(new Color(244, 243, 243));
        txtImei.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPanee = new JScrollPane(txtImei);
        scrollPanee.setBorder(BorderFactory.createEmptyBorder(12, 0, 20, 0));
        txtImei.setPreferredSize(new Dimension(200, 10));





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
        infoPanel.setBorder(BorderFactory.createEmptyBorder(9, 10, 0, 10));
        inner_info.setBackground(Color.WHITE);
        inner_info.setBorder(BorderFactory.createEmptyBorder(4, 10, 0, 10));
        buttonPanel_info.setBackground(Color.WHITE);

        infoPanel.add(inner_info, BorderLayout.NORTH);
        infoPanel.add(scrollPanee, BorderLayout.CENTER);
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
        String[] columns = {"STT", "Mã SP", "Tên sản phẩm", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng","Imei"};
        Object[][] data = {};
        DefaultTableModel model1 = new DefaultTableModel(data, columns);

        bottomTable = new JTable(model1);
        TableColumnModel columnModel = bottomTable.getColumnModel();
        columnModel.getColumn(8).setMinWidth(0);
        columnModel.getColumn(8).setMaxWidth(0);
        columnModel.getColumn(8).setWidth(0);




        formatTable(bottomTable);
//
        JScrollPane table_bottom = new JScrollPane(bottomTable);
        table_bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        table_bottom.setBackground(new Color(213, 221, 220));

        leftPanel.add(prinPanel, BorderLayout.NORTH);
        leftPanel.add(table_bottom, BorderLayout.CENTER);


        JPanel innerRight = new JPanel(new GridLayout(20, 1, 0, 5));

        innerRight.add(lbl_Id_import = new JLabel("Mã phiếu xuất"));
        innerRight.add(txtId_import = new JTextField(""));
        String displayId = "PX" + generateUniqueImportNumber();
        txtId_import.setText(displayId);



        innerRight.add(lbl_Employ = new JLabel("Nhân viên xuất"));
        innerRight.add(txt_Employ = new JTextField(""));
        sidebarMenu = getSidebarMenu();
        txt_Employ.setText(hienThiTenNhanVien());

        innerRight.add(lbl_Supplier = new JLabel("Khách hàng"));
        txtId_import.setBackground(new Color(244, 243, 243));
        txt_Employ.setEditable(false);
        txt_Employ.setBackground(new Color(246, 245, 245));



        comboBox_KhachHang = new JComboBox<>();
        List<KhachHang> khachHang = new KhachHangDAO().selectAll();
        if (khachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<KhachHang> khachhang = new DefaultComboBoxModel<>();
            for (KhachHang kh : khachHang) {
                khachhang.addElement(kh);
            }
            comboBox_KhachHang.setModel(khachhang);
            comboBox_KhachHang.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof KhachHang) {
                        setText(((KhachHang) value).getTenKhachHang());
                    }
                    return this;
                }
            });
        }
        innerRight.add(comboBox_KhachHang);

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
        inner_price.add(btnImport_goods = new RoundedButton("Xuất hàng", 40));
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

    public void resetExportId() {
        String newDisplayId = "PX" + generateUniqueImportNumber();
        txtId_import.setText(newDisplayId);
    }
    public String  hienThiTenNhanVien() {
        return    nhanVien.getHoten();
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

    public void updateTable(java.util.List<?> list) {
        if (list.isEmpty()) return;
        if (!(list.get(0) instanceof SanPham)) return;

        java.util.List<SanPham> sanPhamList = (List<SanPham>) list;

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
        phieuXuatdao dao = new phieuXuatdao();

        do {
            number = (int) (Math.random() * 10000); // từ 0–9999
        } while (dao.existsExportId(number));

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

    public JPanel getLeftPanel() {
        return leftPanel;
    }


    public JPanel getProductPanel() {
        return productPanel;
    }


    public JPanel getInfoPanel() {
        return infoPanel;
    }


    public JPanel getBottomPanel() {
        return bottomPanel;
    }


    public JPanel getButtonPanel() {
        return buttonPanel;
    }


    public JPanel getButtonPanel_info() {
        return buttonPanel_info;
    }


    public JPanel getPrinPanel() {
        return prinPanel;
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

    public JLabel getLblName() {
        return lblName;
    }


    public JLabel getLblID() {
        return lblID;
    }


    public JLabel getLblPrice() {
        return lblPrice;
    }

    public void setLblPrice(JLabel lblPrice) {
        this.lblPrice = lblPrice;
    }



    public JLabel getLblImei() {
        return lblImei;
    }

    public void setLblImei(JLabel lblImei) {
        this.lblImei = lblImei;
    }

    public JLabel getLblQuantity() {
        return lblQuantity;
    }

    public void setLblQuantity(JLabel lblQuantity) {
        this.lblQuantity = lblQuantity;
    }

    public JLabel getLblConfiguration() {
        return lblConfiguration;
    }

    public void setLblConfiguration(JLabel lblConfiguration) {
        this.lblConfiguration = lblConfiguration;
    }


    public JTextField getTxtName() {
        return txtName;
    }

    public void setTxtName(JTextField txtName) {
        this.txtName = txtName;
    }

    public JTextField getTxtID() {
        return txtID;
    }

    public void setTxtID(JTextField txtID) {
        this.txtID = txtID;
    }

    public void setLblName(JLabel lblName) {
        this.lblName = lblName;
    }

    public void setLblID(JLabel lblID) {
        this.lblID = lblID;
    }

    public void setTable_Product(JTable table_Product) {
        this.table_Product = table_Product;
    }

    public void setTxtSearch(JTextField txtSearch) {
        this.txtSearch = txtSearch;
    }

    public void setInfoPanel(JPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    public void setProductPanel(JPanel productPanel) {
        this.productPanel = productPanel;
    }

    public void setLeftPanel(JPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public SanPhamDAO getSanPhamDAO() {
        return sanPhamDAO;
    }

    public void setSanPhamDAO(SanPhamDAO sanPhamDAO) {
        this.sanPhamDAO = sanPhamDAO;
    }

    public JTextField getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(JTextField txtPrice) {
        this.txtPrice = txtPrice;
    }

    @Override
    public SidebarMenu getSidebarMenu() {
        return sidebarMenu;
    }

    public void setSidebarMenu(SidebarMenu sidebarMenu) {
        this.sidebarMenu = sidebarMenu;
    }

    public JButton getBtnImei() {
        return btnImei;
    }

    public void setBtnImei(JButton btnImei) {
        this.btnImei = btnImei;
    }

    public JComboBox<KhachHang> getComboBox_KhachHang() {
        return comboBox_KhachHang;
    }

    public void setComboBox_KhachHang(JComboBox<KhachHang> comboBox_KhachHang) {
        this.comboBox_KhachHang = comboBox_KhachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public JLabel getLblSpace() {
        return lblSpace;
    }

    public void setLblSpace(JLabel lblSpace) {
        this.lblSpace = lblSpace;
    }

    public JTextField getTxtSpace() {
        return txtSpace;
    }

    public void setTxtSpace(JTextField txtSpace) {
        this.txtSpace = txtSpace;
    }

    public int getImei() {
        return Imei;
    }

    public void setImei(int imei) {
        Imei = imei;
    }

    public JTextField getTxtQuantity() {
        return txtQuantity;
    }

    public void setTxtQuantity(JTextField txtQuantity) {
        this.txtQuantity = txtQuantity;
    }





    public JButton getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(JButton btnEdit) {
        this.btnEdit = btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JTable getBottomTable() {
        return bottomTable;
    }

    public void setBottomTable(JTable bottomTable) {
        this.bottomTable = bottomTable;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(JPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public JPanel getImporting_goods() {
        return importing_goods;
    }

    public void setImporting_goods(JPanel importing_goods) {
        this.importing_goods = importing_goods;
    }

    public JPanel getInner_price() {
        return inner_price;
    }

    public void setInner_price(JPanel inner_price) {
        this.inner_price = inner_price;
    }

    public JLabel getLbl_Id_import() {
        return lbl_Id_import;
    }


    public void setLbl_Id_import(JLabel lbl_Id_import) {
        this.lbl_Id_import = lbl_Id_import;
    }

    public JLabel getLbl_Employ() {
        return lbl_Employ;
    }

    public void setLbl_Employ(JLabel lbl_Employ) {
        this.lbl_Employ = lbl_Employ;
    }

    public JLabel getLbl_Supplier() {
        return lbl_Supplier;
    }

    public void setLbl_Supplier(JLabel lbl_Supplier) {
        this.lbl_Supplier = lbl_Supplier;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
    }

    public JLabel getLblTotalPrice() {
        return lblTotalPrice;
    }

    public void setLblTotalPrice(JLabel lblTotalPrice) {
        this.lblTotalPrice = lblTotalPrice;
    }

    public JTextField getTxtId_import() {
        return txtId_import;
    }

    public void setTxtId_import(JTextField txtId_import) {
        this.txtId_import = txtId_import;
    }

    public JTextField getTxt_Employ() {
        return txt_Employ;
    }

    public void setTxt_Employ(JTextField txt_Employ) {
        this.txt_Employ = txt_Employ;
    }

    public JTextArea getTxtImei() {
        return txtImei;
    }

    public void setTxtImei(JTextArea txtImei) {
        this.txtImei = txtImei;
    }

    public void setBottomPanel(JPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public void setButtonPanel_info(JPanel buttonPanel_info) {
        this.buttonPanel_info = buttonPanel_info;
    }

    public void setPrinPanel(JPanel prinPanel) {
        this.prinPanel = prinPanel;
    }

    public JTextField getComboBox_Config() {
        return comboBox_Config;
    }

    public void setComboBox_Config(JTextField comboBox_Config) {
        this.comboBox_Config = comboBox_Config;
    }

    public JButton getBtnImport_goods() {
        return btnImport_goods;
    }

    public void setBtnImport_goods(JButton btnImport_goods) {
        this.btnImport_goods = btnImport_goods;

    }





}

