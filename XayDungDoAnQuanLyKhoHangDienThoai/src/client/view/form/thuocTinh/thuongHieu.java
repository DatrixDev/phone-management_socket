package client.view.form.thuocTinh;



import server.DAO.thuongHieuDAO;
import shared.models.thuonghieu;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class thuongHieu extends JDialog {
    private JTextField txt;
    private JTable table;
    private JButton btnThem, btnXoa, btnSua;

    public thuongHieu() {
        setTitle("Quản lý thương hiệu");
        setSize(420, 580);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // ===== Title =====
        JLabel lblTitle = new JLabel("THƯƠNG HIỆU", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(74, 112, 81));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 23));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Center Form =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Nhập ROM
        JPanel inputPanel = new JPanel(new BorderLayout(5, 7));
        JLabel lbl = new JLabel("Tên thương hiệu");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        txt = new JTextField();
        txt.setFont(new Font("Arial", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(lbl, BorderLayout.NORTH);
        inputPanel.add(txt, BorderLayout.CENTER);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        centerPanel.add(inputPanel, BorderLayout.NORTH);

        // ===== Table =====
        String[] columnNames = {"Mã THƯƠNG HIỆU", "Tên THƯƠNG HIỆU"};
        Object[][] data = {

        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setBackground(new Color(221, 223, 225));
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(74, 112, 81));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 15));
        btnThem.setPreferredSize(new Dimension(100, 33));

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(220, 53, 69));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 15));
        btnXoa.setPreferredSize(new Dimension(100, 33));


        btnSua = new JButton("Sửa");
        btnSua.setBackground(new Color(23, 136, 184));
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Arial", Font.BOLD, 15));
        btnSua.setPreferredSize(new Dimension(100, 33));


        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);

        add(buttonPanel, BorderLayout.SOUTH);

        loadThuongHieuToTable();

    }


    public void loadThuongHieuToTable() {
        thuongHieuDAO dao = new thuongHieuDAO();
        List<thuonghieu> list = dao.getAllThuongHieu();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (thuonghieu th : list) {
            model.addRow(new Object[]{
                    th.getMathuonghieu(),
                    th.getTenthuonghieu()
            });
        }
    }

    public JTextField getTxtROM() {
        return txt;
    }

    public void setTxtROM(JTextField txtROM) {
        this.txt = txtROM;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public void setBtnThem(JButton btnThem) {
        this.btnThem = btnThem;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public void setBtnXoa(JButton btnXoa) {
        this.btnXoa = btnXoa;
    }

    public JButton getBtnSua() {
        return btnSua;
    }

    public void setBtnSua(JButton btnSua) {
        this.btnSua = btnSua;
    }
}