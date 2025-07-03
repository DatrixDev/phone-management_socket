package client.view.form;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


import client.view.views.AccountView;
import server.DAO.NhanVienDAO;
import shared.models.NhanVien;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SelectAccountForm extends JDialog implements MouseListener {
    private AccountView account;
    private DefaultTableModel model;
    private JPanel panelTop;
    private JPanel panelContainer;
	private JPanel panelHeader;
	private JTextField txtSearch;
	private JButton btnSelect;
	private JTable table;
    private ArrayList<NhanVien> listNhanVien = NhanVienDAO.getInstance().selectAllNV();

    public SelectAccountForm(AccountView account) {
        this.account = account;
        initComponent();
    }

    public void initComponent() {
        setTitle("Chọn tài khoản");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Panel Top
        panelTop = new JPanel(new BorderLayout());

        // Panel Top/Panel Content
        panelHeader = new JPanel();
        panelHeader.setBackground(Color.decode("#DDDDDD"));
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.X_AXIS));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblSearch = new JLabel("Tìm kiếm");
        lblSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSearch = new JTextField();
        txtSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String txt = txtSearch.getText();
                listNhanVien = search(txt);
                loadTableData(listNhanVien);
            }
        });
        btnSelect = new JButton("Chọn nhân viên");
        btnSelect.setBorderPainted(false);
        btnSelect.setFont(new Font("Arial", Font.BOLD, 13));
        btnSelect.setPreferredSize(new Dimension(150, 40));
        btnSelect.setMaximumSize(new Dimension(150, 40)); // thêm dòng này để khóa lại
        btnSelect.setBackground(new Color(51, 142, 193));
        btnSelect.setForeground(Color.WHITE);
        btnSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Chức năng cho nút "Chọn nhân viên"
        btnSelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getRow()<0){
                    int input = JOptionPane.showConfirmDialog(null,
                            "Vui lòng chọn nhân viên!:)", "Thông báo", JOptionPane.DEFAULT_OPTION);
                } else{
                    dispose();
                   AddAccountForm tkd = new AddAccountForm(account, listNhanVien.get(getRow()).getManv());
                }
            }

        });

        panelHeader.add(lblSearch);
        panelHeader.add(Box.createHorizontalStrut(6));
        panelHeader.add(txtSearch);
        panelHeader.add(Box.createHorizontalStrut(6));
        panelHeader.add(btnSelect);

        panelTop.add(panelHeader, BorderLayout.CENTER);



        // Panel Table
        String[] columns = {"MNV","Họ tên","Giới tính","Ngày sinh","SDT","Email"};
        model = new DefaultTableModel(columns, 0); // 0 dòng ban đầu
        table = new JTable(model);
        table.setRowHeight(40); // tăng chiều cao hàng
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chọn 1 hàng duy nhất
        table.setSelectionBackground(Color.decode("#E6E6E6"));
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
        header.setBackground(Color.decode("#D6D6D6"));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền

        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(scrollPane, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);

        loadTableData(listNhanVien);
    }
    
	public JButton getBtnSelect() {
		return btnSelect;
	}

    public int getRow(){
        return table.getSelectedRow();
    }

    public void loadTableData(ArrayList<NhanVien> list) {
        listNhanVien = list;
        model.setRowCount(0);
        for (NhanVien nhanVien : listNhanVien) {
            model.addRow(new Object[]{
                    nhanVien.getManv(),nhanVien.getHoten(),nhanVien.getGioitinh()==1?"Nam":"Nữ",nhanVien.getNgaysinh(),nhanVien.getSdt(),nhanVien.getEmail()
            });
        }
    }

    public ArrayList<NhanVien> search(String text) {
        if(text.length()>0){
            text = text.toLowerCase();
            ArrayList<NhanVien> result = new ArrayList<>();
            System.out.println(text);
            for(NhanVien i : listNhanVien) {
                if(i.getHoten().toLowerCase().contains(text) || i.getEmail().toLowerCase().contains(text)
                        || i.getSdt().toLowerCase().contains(text)){
                    result.add(i);
                }
            }
            return result;
        } else {
            return NhanVienDAO.getInstance().selectAll();
        }

    }


    public NhanVien getSelectedNhanVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0 && listNhanVien != null && selectedRow < listNhanVien.size()) {
            return listNhanVien.get(selectedRow);
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//    public static void main(String[] args) {
//    	try {
//    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
//			new SelectAccountForm().setVisible(true);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//    }

}
