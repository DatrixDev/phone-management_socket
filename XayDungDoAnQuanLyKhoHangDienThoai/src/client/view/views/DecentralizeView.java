package client.view.views;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import client.view.shared.BaseView;
import client.view.shared.TopPanel;
import com.formdev.flatlaf.FlatIntelliJLaf;

import client.BUS.NhomQuyenBUS;
import client.controller.DecentralizeController;
import shared.models.NhomQuyen;

public class DecentralizeView extends BaseView {
    private DefaultTableModel model;
    private JTable table;
    private TopPanel topPanel;
    public NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
    public ArrayList<NhomQuyen> listNhomQuyen = nhomQuyenBUS.getAll();
    public DecentralizeView(){
        super();
        topPanel = new TopPanel();
        String[] searchOptions = {"Tất cả"};
        topPanel.getCbxChoose().setModel(new DefaultComboBoxModel<>(searchOptions));
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        new DecentralizeController(this);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(230, 230, 230));
        container.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
        container.add(topPanel,BorderLayout.NORTH);

        String[] columns = {"Mã nhóm quyền","Tên nhóm quyền"};
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
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35)); // chiều cao 35
        header.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        tablePanel.setBackground(Color.decode("#F0F7FA"));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        container.add(tablePanel,BorderLayout.CENTER);
        container.setBackground(Color.decode("#F0F7FA"));
        getMainPanel().add(container,BorderLayout.CENTER);

        loadDataTable(listNhomQuyen);
    }

    public void loadDataTable(ArrayList<NhomQuyen> list) {
        listNhomQuyen = list;
        model.setRowCount(0);
        for (NhomQuyen nhomQuyen : listNhomQuyen) {
            model.addRow(new Object[]{
                   nhomQuyen.getManhomquyen(), nhomQuyen.getTennhomquyen()
            });
        }
    }

    public int getRowSelected() {
        int index = table.getSelectedRow();
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền");
            return -1;
        }
        return index;
    }

    public static void main(String[] args) {
    	try {
    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
			new DecentralizeView().setVisible(true);
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
}
