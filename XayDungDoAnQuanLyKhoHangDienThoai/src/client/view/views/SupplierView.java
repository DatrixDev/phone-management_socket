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

import client.BUS.NhaCungCapBUS;
import client.controller.SupplierController;
import shared.models.NhaCungCap;

public class SupplierView extends BaseView {

    private DefaultTableModel model;
    private JTable table;
    private TopPanel topPanel;
    public NhaCungCapBUS nhaCungCapBUS = new NhaCungCapBUS();
    public ArrayList<NhaCungCap> listncc = nhaCungCapBUS.getAll();
     public SupplierView(){
         super();
         topPanel = new TopPanel();
         String[] searchOptions = {"Tất cả", "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại"};
         topPanel.getCbxChoose().setModel(new DefaultComboBoxModel<>(searchOptions));
         new SupplierController(this);

         JPanel container = new JPanel(new BorderLayout());
         container.setBackground(new Color(230, 230, 230));
         container.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
         container.add(topPanel,BorderLayout.NORTH);



         String[] columns = {"Mã NCC","Tên nhà cung cấp","Địa chỉ","Email","Số điện thoại"};
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

         DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
         centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
         for (int i = 0; i < table.getColumnCount(); i++) {
             table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
         }
         JScrollPane scrollPane = new JScrollPane(table);
         scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
         JPanel tablePanal = new JPanel(new BorderLayout());
         tablePanal.setBackground(new Color(230, 230, 230));
         tablePanal.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
         tablePanal.add(scrollPane,BorderLayout.CENTER);

         container.add(tablePanal,BorderLayout.CENTER);
         getMainPanel().add(container,BorderLayout.CENTER);
         table.setBackground(Color.WHITE);

         loadDataTable(listncc);

     }
     public static void main(String[] args) {
     	try {
     		UIManager.setLookAndFeel(new FlatIntelliJLaf());
 			new SupplierView().setVisible(true);
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

    public void loadDataTable(ArrayList<NhaCungCap> result) {
        model.setRowCount(0);
        for (NhaCungCap nhaCungCap : result) {
            model.addRow(new Object[]{
                    nhaCungCap.getMancc(), nhaCungCap.getTenncc(), nhaCungCap.getDiachi(), nhaCungCap.getEmail(), nhaCungCap.getSdt()
            });
        }
    }
}
