package client.view.views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import client.helper.BCrypt;
import client.helper.Validation;
import client.view.shared.BaseView;
import client.view.shared.TopPanel;
import client.BUS.NhanVienBUS;
import client.BUS.NhomQuyenBUS;
import client.BUS.TaiKhoanBUS;
import server.DAO.TaiKhoanDAO;
import client.controller.AccountController;
import shared.models.NhanVien;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;

public class AccountView extends BaseView implements ItemListener {

    private DefaultTableModel model;
    private JTable table;
    public TaiKhoanBUS taiKhoanBus = new TaiKhoanBUS();
    public ArrayList<TaiKhoan> listTk = taiKhoanBus.getTaiKhoanAll();
        private TopPanel topPanel;
        public AccountView(){
            super();
            topPanel = new TopPanel();
            String[] searchOptions = {"Tất cả", "Mã nhân viên", "Username"};
            topPanel.getCbxChoose().setModel(new DefaultComboBoxModel<>(searchOptions));
            new AccountController(this);

            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(new Color(230, 230, 230));
            container.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
            container.add(topPanel,BorderLayout.NORTH);



            String[] columns = {"Mã nhân viên","Tên đăng nhập","Nhóm quyền","Trạng thái"};
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
            JPanel tablePanal = new JPanel(new BorderLayout());
            tablePanal.setBackground(new Color(230, 230, 230));
            tablePanal.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            tablePanal.add(scrollPane,BorderLayout.CENTER);

            container.add(tablePanal,BorderLayout.CENTER);
            getMainPanel().add(container,BorderLayout.CENTER);
            table.setBackground(Color.WHITE);

            loadTableData(listTk);

        }

        public void loadTableData(ArrayList<TaiKhoan> list) {
            model.setRowCount(0);
            for (TaiKhoan taiKhoan : list) {
                int tt = taiKhoan.getTrangthai();
                String trangthaiString = "";
                switch (tt) {
                    case 1 -> {
                        trangthaiString = "Hoạt động";
                    }
                    case 0 -> {
                        trangthaiString = "Ngưng hoạt động";
                    }
                }
                model.addRow(new Object[]{
                        taiKhoan.getManv(), taiKhoan.getUsername(), taiKhoanBus.getNhomQuyenDTO(taiKhoan.getManhomquyen()).getTennhomquyen(), trangthaiString
                });
            }
        }

        public void openFile(String file) {
            try {
                File path = new File(file);
                Desktop.getDesktop().open(path);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        public int getRowSelected() {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản");
            }
            return index;
        }

    public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
        jf.setDialogTitle("Open file");
        Workbook workbook = null;
        int k = 0;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);
                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    Cell cell0=excelRow.getCell(0);
                    int manv = (int)excelRow.getCell(0).getNumericCellValue();
                    String tendangnhap = excelRow.getCell(1).getStringCellValue();
                    String matkhau = excelRow.getCell(2).getStringCellValue();
                    String nhomquyen = excelRow.getCell(3).getStringCellValue();
                    int check1 = 0, check2 = 0, check3 = 0, check4 = 0;
                    if (Validation.isEmpty(String.valueOf(manv)) || Validation.isEmpty(tendangnhap)
                            || Validation.isEmpty(matkhau)
                            || Validation.isEmpty(nhomquyen)) {
                        check1 = 1;
                    }
                    int manhomquyen = 0;
                    NhanVienBUS nvbus = new NhanVienBUS();
                    ArrayList<NhanVien> nvlist = nvbus.getAll();
                    for (NhanVien nv : nvlist) {
                        if (nv.getManv() == manv) {
                            check2 = 0;
                            break;
                        } else {
                            check2 = 1;
                        }
                    }
                    ArrayList<TaiKhoan> curlist = taiKhoanBus.getTaiKhoanAll();
                    for (TaiKhoan tk : curlist) {
                        if (tk.getUsername().equals(tendangnhap)) {
                            check3 = 1;
                            break;
                        } else {
                            check3 = 0;
                        }
                    }
                    NhomQuyenBUS nhomquyenbus = new NhomQuyenBUS();
                    ArrayList<NhomQuyen> quyenlist = nhomquyenbus.getAll();
                    for (NhomQuyen quyen : quyenlist) {
                        if (quyen.getTennhomquyen().trim().equals(nhomquyen.trim())) {
                            check4 = 0;
                            manhomquyen = quyen.getManhomquyen();
                            break;
                        } else {
                            check4 = 1;
                        }
                    }
                    System.out.println(manv + ":" + tendangnhap + ":" + matkhau + ":" + manhomquyen);
                    System.out.println(check1 + " " + check2 + " " + check3 + " " + check4);
                    if (check1 != 0 || check2 != 0 || check3 != 0 || check4 != 0) {
                        k += 1;
                    } else {
                        System.out.println(manv + ":" + tendangnhap + ":" + matkhau + ":" + manhomquyen);
                        String pass = BCrypt.hashpw(matkhau, BCrypt.gensalt(12));
                        TaiKhoan newaccount = new TaiKhoan(manv, tendangnhap, pass, manhomquyen, 1);
                        TaiKhoanDAO.getInstance().insert(newaccount);
                        listTk.add(newaccount);
                    }
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Lỗi đọc file");
            } catch (IOException ex) {
                System.out.println("Lỗi đọc file");
            }
        }
        if (k != 0) {
            JOptionPane.showMessageDialog(this, "Những dữ liệu không chuẩn không được thêm vào");
        } else {
            JOptionPane.showMessageDialog(this, "Nhập dữ liệu thành công");
        }

        loadTableData(listTk);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) topPanel.getCbxChoose().getSelectedItem();
        String txt = topPanel.txtSearch.getText();
        listTk = taiKhoanBus.search(txt, type);
        loadTableData(listTk);
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


