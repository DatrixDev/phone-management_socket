package client.view.views;

import client.view.shared.BaseView;
import client.view.shared.TopPanel_Two;
import com.toedter.calendar.JDateChooser;
import server.DAO.phieuXuatdao;
import shared.models.phieuXuat;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportView extends BaseView {
    private TopPanel_Two topPanel;
    private JTable table;
    private JPanel leftPanel;
    private JPanel container, footer;

    private JComboBox<String> customerComboBox;
    private JComboBox<String> staffComboBox;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JTextField fromAmountField;
    private JTextField toAmountField;

    private List<phieuXuat> fullList = new ArrayList<>();

    public ExportView() {
        super();
        topPanel = new TopPanel_Two();

        container = new JPanel(new BorderLayout());
        container.setBackground(new Color(230, 230, 230));
        container.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        container.add(topPanel, BorderLayout.NORTH);

        // Bảng
        String[] columns = {"STT", "Mã phiếu xuất", "Khách hàng", "Nhân viên xuất", "Thời gian", "Tổng tiền"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return java.sql.Timestamp.class;
                if (columnIndex == 5) return Double.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setSortKeys(List.of(new RowSorter.SortKey(4, SortOrder.DESCENDING)));

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(String.valueOf(row + 1));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245));
        header.setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 230, 230));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        initLeftPanel();

        footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        footer.setBackground(Color.decode("#E6E6E6"));
        footer.add(leftPanel, BorderLayout.WEST);
        footer.add(tablePanel, BorderLayout.CENTER);

        container.add(footer, BorderLayout.CENTER);
        getMainPanel().add(container, BorderLayout.CENTER);

        fullList = phieuXuatdao.layDanhSachPhieuXuat();
        updateTable(fullList);
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setLayout(new GridLayout(16, 1, 40, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        leftPanel.add(createLabel("Khách hàng"));
        customerComboBox = new JComboBox<>(new String[]{"Tất cả", "Khách hàng A", "Khách hàng B"});
        leftPanel.add(customerComboBox);

        leftPanel.add(createLabel("Nhân viên xuất"));
        staffComboBox = new JComboBox<>(new String[]{"Tất cả", "Nhân viên A", "Nhân viên B"});
        leftPanel.add(staffComboBox);

        leftPanel.add(createLabel("Từ ngày"));
        fromDateChooser = new JDateChooser();
        fromDateChooser.setDateFormatString("dd/MM/yyyy");
        leftPanel.add(fromDateChooser);

        leftPanel.add(createLabel("Đến ngày"));
        toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("dd/MM/yyyy");
        leftPanel.add(toDateChooser);

        leftPanel.add(createLabel("Từ số tiền (VND)"));
        fromAmountField = new JTextField();
        leftPanel.add(fromAmountField);

        leftPanel.add(createLabel("Đến số tiền (VND)"));
        toAmountField = new JTextField();
        leftPanel.add(toAmountField);
    }


    public void updateTable(List<phieuXuat> list) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (phieuXuat sp : list) {
            model.addRow(new Object[]{
                    null,
                    sp.getMaphieuXuat(),
                    sp.getTenKhachHang(),
                    sp.getTennhanvien(),
                    sp.getThoigiantao(),
                    sp.getTongTien()
            });
        }
        System.out.println("[DEBUG] Bảng cập nhật: " + list.size() + " kết quả");
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return label;
    }

    public JPanel getContenPanel() {
        return mainPanel;
    }

    public TopPanel_Two getTopPanel() {
        return topPanel;
    }

    public JTable getTable() {
        return table;
    }

    public JComboBox<String> getCustomerComboBox() {
        return customerComboBox;
    }

    public void setCustomerComboBox(JComboBox<String> customerComboBox) {
        this.customerComboBox = customerComboBox;
    }

    public JComboBox<String> getStaffComboBox() {
        return staffComboBox;
    }
    public JDateChooser getFromDateChooser() { return fromDateChooser; }
    public JDateChooser getToDateChooser() { return toDateChooser; }

    public JTextField getFromAmountField() {
        return fromAmountField;
    }

    public JTextField getToAmountField() {
        return toAmountField;
    }

    public void setStaffComboBox(JComboBox<String> staffComboBox) {
        this.staffComboBox = staffComboBox;
    }
}
