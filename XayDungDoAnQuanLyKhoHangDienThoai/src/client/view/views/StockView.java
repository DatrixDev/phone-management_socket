package client.view.views;

import client.controller.AppController;
import client.controller.StockController;
import client.view.components.ButtonUtils;
import client.view.components.RoundedPanel;
import client.view.components.TextFieldUtils;
import client.view.form.KhuVucKho.MapPanel;
import client.view.form.KhuVucKho.ProductListCellRenderer;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import server.DAO.KhuVucKhoDAO;
import server.DAO.SanPhamDAO;
import server.DAO.cauHinhDAO;
import shared.models.KhuVucKho;
import shared.models.SanPham;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.List;

public class StockView extends JPanel {
    private JTable table;
    private JPanel containerPanel,mapPanel,container;
    private JPanel leftPanel, rightPanel;
    public JTextField txtSearch;
    public JComboBox<String> cbFilter;
    public JButton btnAdd, btnEdit, btnDelete, btnImport, btnExport, btnReload;
    private JList<String> productList; // danh sách sản phẩm
    private DefaultListModel<String> listModel;
    private RoundedPanel topPanel;
    private MapPanel map;
    private JLabel productTitle;
    AppController main;


    private StockController controller;  // Thêm controller

    public StockView(AppController main) {
        this.main = main;
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());
        topPanel = new RoundedPanel(20);
        topPanel.setLayout(new BorderLayout());

        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, -1, 6));

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);

        btnAdd = createButton("Thêm", "/images/accountt.svg.png");
        btnAdd.setIcon(new FlatSVGIcon("images/aded.svg", 41, 41));

        btnEdit = createButton("Sửa", "/images/fixed.svg.png");
        btnEdit.setIcon(new FlatSVGIcon("images/fixed.svg", 42, 42));
        btnDelete = createButton("Xóa", "/images/delete.svg.png");
        btnDelete.setIcon(new FlatSVGIcon("images/delete.svg", 42, 42));
        btnImport = createButton("Nhập Excel", "/images/excel.png");
        btnImport.setIcon(new FlatSVGIcon("images/import.svg", 42, 42));

        btnExport = createButton("Xuất Excel", "/images/expot.svg.png");
        btnExport.setIcon(new FlatSVGIcon("images/export_excel.svg", 42, 42));

        leftPanel.add(btnAdd);
        leftPanel.add(btnEdit);
        leftPanel.add(btnDelete);
        leftPanel.add(btnImport);
        leftPanel.add(btnExport);
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 13, 25));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));

        cbFilter = new JComboBox<>(new String[]{"Tất cả", "Samsung", "Apple", "Vivo", "Realme"});
        cbFilter.setPreferredSize(new Dimension(120, 30));
        cbFilter.setBackground(Color.WHITE);
        cbFilter.setFocusable(false);
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
        rightPanel.add(cbFilter);
        rightPanel.add(txtSearch);
        rightPanel.add(btnReload);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(230, 230, 230));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        containerPanel.add(topPanel, BorderLayout.NORTH);

        //MYSQL
        String[] columns = {"Mã kho", "Tên khu vực", "Ghi chú" ,"Tọa Độ"};
        Object[][] data = {};
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);

        table = new JTable(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.toggleSortOrder(1);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("Arial", Font.BOLD, 13));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        listModel = new DefaultListModel<>();
        productList = new JList<>(listModel);
        productList.setCellRenderer(new ProductListCellRenderer());

        productList.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane listScrollPane = new JScrollPane(productList);
        listScrollPane.setPreferredSize(new Dimension(200, 0));

        container = new JPanel(new BorderLayout());
        mapPanel = new JPanel(new BorderLayout());
        mapPanel.setPreferredSize(new Dimension(40, 380));
        map = new MapPanel();
        mapPanel.add(map);
        productTitle = new JLabel("DANH SÁCH SẢN PHẨM HIỆN TRONG KHO", SwingConstants.CENTER);
        productTitle.setFont(new Font("Arial", Font.BOLD, 16));
        productTitle.setForeground(Color.BLUE);
        productTitle.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));
        container.add(productTitle, BorderLayout.NORTH);
        container.add(listScrollPane, BorderLayout.CENTER);
        container.add(mapPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, container);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(1.0);
        splitPane.setBorder(null);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 230, 230));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(splitPane);

        containerPanel.add(tablePanel, BorderLayout.CENTER);
        this.add(containerPanel, BorderLayout.CENTER);

        // Khởi tạo controller và truyền StockView cho controller
        controller = new StockController(this);
        updateTable(KhuVucKhoDAO.getAllkhuVuc());

        productList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = productList.locationToIndex(e.getPoint());
                if (index != -1 && productList.getCellBounds(index, index).contains(e.getPoint())) {
                    ProductListCellRenderer.setHoverIndex(index);
                } else {
                    ProductListCellRenderer.setHoverIndex(-1);
                }
                productList.repaint(); // Cập nhật giao diện
            }
        });

        productList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ProductListCellRenderer.setHoverIndex(-1); // Khi rời khỏi list thì bỏ hover
                productList.repaint();
            }
        });
    }


    public JTable getTable() {
        return table;
    }

    public void updateProductList(String tenKhuVuc) {
        System.out.println(tenKhuVuc);
        listModel.clear();
        List<SanPham> products = SanPhamDAO.getSanPhamByTenKhuVuc(tenKhuVuc);
        System.out.println(products);
        if (products.isEmpty()) {
            if (productTitle != null) {
                productTitle.setText("DANH SÁCH SẢN PHẨM HIỆN TRONG KHO (0 sản phẩm)");
            }
        } else {
            for (SanPham sp : products) {
                int totalQuantity = cauHinhDAO.getTotalQuantityBySanPham(sp.getId());
                listModel.addElement(sp.getTenSanPham() + " (SL: " + totalQuantity + ")");

            }
            if (productTitle != null) {
                productTitle.setText("DANH SÁCH SẢN PHẨM HIỆN TRONG KHO (" + products.size() + " sản phẩm)");
            }
        }
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(112, 80));
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


    public void updateTable(List<?> list) {
        if (list.isEmpty()) return;
        if (!(list.get(0) instanceof KhuVucKho)) return;

        List<KhuVucKho> KhuVucKhoList = (List<KhuVucKho>) list;

        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0);

        for (KhuVucKho sp : KhuVucKhoList) {
            model.addRow(new Object[]{
                    sp.getMakhuvuc(),
                    sp.getTenkhuvuc(),
                    sp.getGhichu(),
                    sp.getToado()

            });
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new StockView().setVisible(true);
//        });
//    }

    public RoundedPanel getTopPanel() {
        return topPanel;
    }
    public JButton getBtnAdd(){
        return btnAdd;
    }
    public JButton getBtnEdit(){
        return btnEdit;
    }
    public JComboBox<String> getCbFilter() {
        return cbFilter;
    }
    public JTextField getTxtSearch() {
        return txtSearch;
    }
    public JList<String> getProductList() {
        return productList;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public StockController getController() {
        return controller;
    }

    public void setController(StockController controller) {
        this.controller = controller;
    }

    public MapPanel getMap() {
        return map;
    }

    public void setMap(MapPanel map) {
        this.map = map;
    }

    public void setTopPanel(RoundedPanel topPanel) {
        this.topPanel = topPanel;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel<String> listModel) {
        this.listModel = listModel;
    }

    public void setProductList(JList<String> productList) {
        this.productList = productList;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(JPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(JPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public JPanel getMapPanel() {
        return mapPanel;
    }

    public void setMapPanel(JPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    public JPanel getContainerPanel() {
        return containerPanel;
    }

    public void setContainerPanel(JPanel containerPanel) {
        this.containerPanel = containerPanel;
    }

    public void setTxtSearch(JTextField txtSearch) {
        this.txtSearch = txtSearch;
    }

    public void setCbFilter(JComboBox<String> cbFilter) {
        this.cbFilter = cbFilter;
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

    public void setBtnImport(JButton btnImport) {
        this.btnImport = btnImport;
    }

    public void setBtnExport(JButton btnExport) {
        this.btnExport = btnExport;
    }

    public void setBtnReload(JButton btnReload) {
        this.btnReload = btnReload;
    }

    public JButton getBtnImport() {
        return btnImport;
    }

    public JButton getBtnExport() {
        return btnExport;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

}