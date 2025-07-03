package client.view.views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import client.view.shared.BaseView;
import client.view.shared.TopPanel;
import com.formdev.flatlaf.FlatIntelliJLaf;

import client.BUS.NhanVienBUS;
import client.controller.EmployeeController;
import shared.models.NhanVien;

import java.awt.*;
import java.util.ArrayList;

public class EmployeeView extends BaseView {


    private DefaultTableModel model;
    private JTable table;
    private TopPanel topPanel;
    public NhanVienBUS nhanVienBUS = new NhanVienBUS();
    public ArrayList<NhanVien> listnv = nhanVienBUS.getAll();
    public EmployeeView(){
        super();
        topPanel = new TopPanel();
        String[] searchOptions = {"Tất cả", "Họ tên", "Email"};
        topPanel.getCbxChoose().setModel(new DefaultComboBoxModel<>(searchOptions));


        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(230, 230, 230));
        container.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
        container.add(topPanel,BorderLayout.NORTH);

        String[] columns = {"Mã nhân viên","Họ tên","Giới tính","Ngày sinh","SDT","Email"};
        model = new DefaultTableModel(columns, 0); // 0 dòng ban đầu
        table = new JTable(model);
        table.setRowHeight(40); // tăng chiều cao hàng
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chọn 1 hàng duy nhất
        table.setSelectionBackground(Color.decode("#D3D3D3"));
        table.setSelectionForeground(Color.black);
        // Tắt viền ngoài
        table.setFocusable(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0)); // Không có khoảng cách giữa các ô
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        // Tùy chỉnh header
        JTableHeader header = table.getTableHeader();
        header.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35)); // chiều cao 35
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245)); // Màu nền nhạt cho header
        header.setFont(new Font("Arial", Font.BOLD, 13));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 230, 230));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        container.add(tablePanel,BorderLayout.CENTER);
        getMainPanel().add(container,BorderLayout.CENTER);

        loadDataTable(listnv);

        EmployeeController controller = new EmployeeController(this);
        controller.setupRightClickMenu();


    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            new EmployeeView().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public JPanel getContentPanel() {

        return mainPanel;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public JTable getTable() {
        return table;
    }

    public int getRow() {
        return table.getSelectedRow();
    }

    public NhanVien getNhanVien() {
        return listnv.get(table.getSelectedRow());
    }

    public void loadDataTable(ArrayList<NhanVien> list) {
        listnv = list;
        model.setRowCount(0);
        for (NhanVien nhanVien : listnv) {
            model.addRow(new Object[]{
                    nhanVien.getManv(), nhanVien.getHoten(), nhanVien.getGioitinh() == 1 ? "Nam" : "Nữ", nhanVien.getNgaysinh(), nhanVien.getSdt(), nhanVien.getEmail()
            });
        }
    }
}
