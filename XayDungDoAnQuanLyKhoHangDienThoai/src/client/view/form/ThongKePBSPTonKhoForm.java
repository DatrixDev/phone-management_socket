package client.view.form;

import client.view.components.HeaderTitle;
import shared.models.ThongKe.ThongKeTonKho;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public final class ThongKePBSPTonKhoForm extends JDialog{

    HeaderTitle titlePage;
    JPanel pnmain, pnmain_bottom;
    DefaultTableModel tblModel;
    JTable table;
    JScrollPane scrollTable;
    ArrayList<ThongKeTonKho> list_pb;

    public ThongKePBSPTonKhoForm(JFrame owner, String title, boolean modal, ArrayList<ThongKeTonKho> list_pb) {
        super(owner, title, modal);
        this.list_pb = list_pb;
        initComponent(title);
        loadDataTable(list_pb);
        this.setVisible(true);
    }

    public void initComponent(String title) {
        this.setSize(new Dimension(900, 460));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase() + ": " + list_pb.get(0).getTensanpham());

        pnmain = new JPanel(new BorderLayout());

        pnmain_bottom = new JPanel(new GridLayout(1, 1));
        pnmain_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnmain_bottom.setBackground(Color.WHITE);
        table = new JTable();
        JTableHeader headerTable = table.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getPreferredSize().width, 35)); // tăng chiều cao thanh header
        headerTable.setBackground(new Color(245, 245, 245)); // Màu nền header
        headerTable.setForeground(Color.BLACK);                      // Màu chữ
        headerTable.setFont(new Font("Arial", Font.BOLD, 13));       // Font chữ
        headerTable.setReorderingAllowed(false);  // Không cho kéo đổi vị trí cột
        headerTable.setResizingAllowed(false);    // Không cho resize
        headerTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setRowHeight(40); // tăng chiều cao các hàng
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chọn 1 hàng duy nhất
        table.setSelectionBackground(Color.decode("#D3D3D3"));
        table.setSelectionForeground(Color.black);
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã phiên bản", "RAM", "ROM", "Màu sắc", "Tồn đầu kỳ", "Nhập trong kỳ", "Xuất trong kỳ", "Tồn cuối kỳ"};
        tblModel.setColumnIdentifiers(header);

        table.setModel(tblModel);
        table.setFocusable(false);
        scrollTable.setViewportView(table);
        table.setDefaultEditor(Object.class, null);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setShowHorizontalLines(true);
        TableColumnModel columnModel = table.getColumnModel();

        pnmain_bottom.add(scrollTable);

        pnmain.add(pnmain_bottom, BorderLayout.CENTER);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    public void loadDataTable(ArrayList<ThongKeTonKho> result) {
        tblModel.setRowCount(0);
        for (ThongKeTonKho ctsp : result) {
            tblModel.addRow(new Object[]{
                    ctsp.getMaphienbansp(), ctsp.getRam(), ctsp.getRom(), ctsp.getMausac(), ctsp.getTondauky(),
                    ctsp.getNhaptrongky(),ctsp.getXuattrongky(),ctsp.getToncuoiky()
            });
        }
    }
}
