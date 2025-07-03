package client.controller;

import client.view.components.TextFieldUtils;
import client.view.form.PhieuNhap.DetailsImportView;
import client.view.form.phieuXuat.*;
import client.view.views.ExportView;
import server.DAO.*;
import shared.models.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static com.sun.glass.ui.Cursor.setVisible;

public class ExportController {
    private ExportView view;
    private informationExportViewForm informationExportViewForm;
    private DetailsExportView detailsExportView;
    private DetailsImportView detailsImportView;
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private NhanVien nhanVien;
    private boolean isImeiInitialized = false;
    private boolean isBtnAddListenerInitialized = false;
    private boolean isBtnImportListenerInitialized = false;
    private List<phieuXuat> fullList = new ArrayList<>();



    public ExportController(ExportView view, DetailsExportView detailsExportView, NhanVien nhanVien ,DetailsImportView detailsImportView) {
        this.view = view;
        this.detailsExportView = detailsExportView;
        this.detailsImportView = detailsImportView;
        initController();
        this.nhanVien = nhanVien;
    }

    public ExportController() {

    }

    public void initController() {

        fullList = phieuXuatdao.layDanhSachPhieuXuat();
        view.updateTable(fullList);
        view.getTopPanel().getBtnDelete().addActionListener(e ->{deleteImport();});
        view.getTopPanel().getBtnDetail().addActionListener(e->{openPhieuXuatChiTiet();});
        view.getTopPanel().getBtnAdd().addActionListener(e->  {openAddPhieuXuat();});
        view.getTopPanel().getBtnExport().addActionListener(e->  {
            int[] selectedRows = view.getTable().getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu xuất !");
                return;
            }
            List<phieuXuat> listPx = new ArrayList<>();

            for (int row : selectedRows) {
                int idPhieuXuat= (int) view.getTable().getValueAt(row, 1);
                phieuXuat pn = phieuXuatdao.layPhieuXuatTheoID(idPhieuXuat);
                if (pn != null) {
                    listPx.add(pn);
                }
            }

            ExportExcelPhieuXuat.ExportExcelPhieuXuat(view,                                    listPx);
        });

        KhachHangDAO khachHangDAO = new KhachHangDAO();
        List<KhachHang> khachHangs = khachHangDAO.selectAll();
        List<String> customerNames = new ArrayList<>();
        customerNames.add("Tất cả");
        for (KhachHang kh : khachHangs) customerNames.add(kh.getTenKhachHang());
        view.getCustomerComboBox().setModel(new DefaultComboBoxModel<>(customerNames.toArray(new String[0])));

        // Load combobox nhân viên
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        List<NhanVien> nhanVienList = nhanVienDAO.selectAll();
        List<String> tenNVList = new ArrayList<>();
        tenNVList.add("Tất cả");
        for (NhanVien nv : nhanVienList) tenNVList.add(nv.getHoten());
        view.getStaffComboBox().setModel(new DefaultComboBoxModel<>(tenNVList.toArray(new String[0])));

        // Gắn sự kiện filter
        view.getCustomerComboBox().addActionListener(e -> applyFilter());
        view.getStaffComboBox().addActionListener(e -> applyFilter());
        view.getFromDateChooser().getDateEditor().addPropertyChangeListener("date" ,e -> applyFilter());
        view.getToDateChooser().getDateEditor().addPropertyChangeListener("date",e -> applyFilter());
        view.getFromAmountField().getDocument().addDocumentListener(new FilterListener());
        view.getToAmountField().getDocument().addDocumentListener(new FilterListener());
        view.getTopPanel().getTxtSearch().getDocument().addDocumentListener(new FilterListener());

        // Reload
        view.getTopPanel().getBtnReload().addActionListener(e -> {
            view.getCustomerComboBox().setSelectedIndex(0);
            view.getStaffComboBox().setSelectedIndex(0);
            view.getFromDateChooser().setDate(null);
            view.getToDateChooser().setDate(null);
            view.getFromAmountField().setText("");
            view.getToAmountField().setText("");
            view.getTopPanel().getTxtSearch().setText("");
            TextFieldUtils.setPlaceholder(view.getTopPanel().getTxtSearch(), "Nhập nội dung tìm kiếm..");


            fullList = phieuXuatdao.layDanhSachPhieuXuat();
            view.updateTable(fullList);
        });
    }

    private void applyFilter() {
        String customer = (String) view.getCustomerComboBox().getSelectedItem();
        String staff = (String) view.getStaffComboBox().getSelectedItem();
        Date fromDate = view.getFromDateChooser().getDate();
        Date toDate = view.getToDateChooser().getDate();
        String fromAmountStr = view.getFromAmountField().getText().trim();
        String toAmountStr = view.getToAmountField().getText().trim();
        String searchKeyword = view.getTopPanel().getTxtSearch().getText().trim().toLowerCase();

        List<phieuXuat> filtered = fullList.stream().filter(p -> {
            boolean matchCustomer = customer.equals("Tất cả") || p.getTenKhachHang().equalsIgnoreCase(customer);
            boolean matchStaff = staff.equals("Tất cả") || p.getTennhanvien().equalsIgnoreCase(staff);
            boolean matchDate = (fromDate == null || !p.getThoigiantao().before(fromDate)) &&
                    (toDate == null || !p.getThoigiantao().after(toDate));
            boolean matchAmount = true;
            try {
                if (!fromAmountStr.isEmpty() && p.getTongTien() < Double.parseDouble(fromAmountStr)) matchAmount = false;
                if (!toAmountStr.isEmpty() && p.getTongTien() > Double.parseDouble(toAmountStr)) matchAmount = false;
            } catch (NumberFormatException e) {
                matchAmount = false;
            }
            boolean matchSearch = String.valueOf(p.getMaphieuXuat()).toLowerCase().contains(searchKeyword) ||
                    p.getTenKhachHang().toLowerCase().contains(searchKeyword) ||
                    p.getTennhanvien().toLowerCase().contains(searchKeyword);

            return matchCustomer && matchStaff && matchDate && matchAmount && matchSearch;
        }).toList();

        view.updateTable(filtered);
    }

    private class FilterListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) { applyFilter(); }
        public void removeUpdate(DocumentEvent e) { applyFilter(); }
        public void changedUpdate(DocumentEvent e) { applyFilter(); }
    }

    private void deleteImport() {

        int[] selectedRows = view.getTable().getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu xuất để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa phiếu xuất này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            for (int row : selectedRows) {

                System.out.println("Size sau khi xóa: " + phieuNhapDAO.layDanhSachPhieuNhap().size());

                int maphieu = (int) view.getTable().getValueAt(row, 1);
                int rows = phieuXuatdao.deletePhieuXuat(maphieu);
                System.out.println("Xóa phiếu xuất ID: " + maphieu + ", kết quả: " + rows);
                if(rows <= 0){
                    JOptionPane.showMessageDialog(view,"Xóa không thành công phiếu xuất ID: " + maphieu);
                }
            }

            List<phieuXuat> ds = phieuXuatdao.layDanhSachPhieuXuat();
            System.out.println("Size sau khi xóa: " + ds.size());
            view.updateTable(ds);

            JOptionPane.showMessageDialog(view,"Xóa thành công ");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa : " + ex.getMessage());
        }



    }

    private void openPhieuXuatChiTiet() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Chọn phiếu xuất cần hiển thị");
            return;
        }

        try {
            int phieuXuatID = (int) view.getTable().getValueAt(selectedRow, 1);
            if (phieuXuatID <= 0) {
                JOptionPane.showMessageDialog(view, "ID phiếu xuất không hợp lệ");
                return;
            }

            phieuXuat px = phieuXuatdao.layPhieuXuatTheoID(phieuXuatID);
            List<ChiTietPhieuXuatDTO> list = chitietPhieuXuatDAO.thongTinPhieuXuat(phieuXuatID);

            informationExportViewForm form = new informationExportViewForm();
            form.loadPhieuXuat(px, list);
            form.getTable().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow =   form.getTable().getSelectedRow();
                    if (selectedRow != -1) {
                        int maSP = (int) form.getTableModel().getValueAt(selectedRow, 1);

                        form.loadImei(maSP,phieuXuatID);
                    }
                }
            });

            form.getBtnClose().addActionListener(e -> form.dispose());

            form.getBtnExport().addActionListener(e -> {
                ExportPDFPhieuXuat.ExportPDFPhieuXuat(px.getMaphieuXuat(), px.getTenKhachHang(), px.getTennhanvien(), px.getThoigiantao(), list);
            });

            form.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hiển thị: " + ex.getMessage());
        }
    }
    private void openAddPhieuXuat() {
        JTable table = detailsExportView.getTable_Product();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object value = table.getValueAt(selectedRow, 0);
                    int productId = Integer.parseInt(value.toString());

                    try {
                        SanPham sanPham = sanPhamDAO.getSanPhamById(productId);
                        cauHinhSanPham cauhinh = sanPham.getCauHinhs();
                        String config = cauhinh.getRom() + " - " + cauhinh.getRam() + " - " + cauhinh.getMausac();

                        detailsExportView.getTxtID().setText(String.valueOf(productId));
                        detailsExportView.getTxtName().setText(sanPham.getTenSanPham());
                        detailsExportView.getComboBox_Config().setText(config);
                        DecimalFormat df = new DecimalFormat("#,###");
                        detailsExportView.getTxtPrice().setText(df.format(cauhinh.getGiaxuat()));
                        detailsExportView.getTxtQuantity().setText(String.valueOf(sanPham.getSoLuong()));

                        DefaultTableModel exportModel = (DefaultTableModel) detailsExportView.getBottomTable().getModel();
                        String imeiInExport = "";
                        for (int i = 0; i < exportModel.getRowCount(); i++) {
                            int existingId = (int) exportModel.getValueAt(i, 1);
                            if (existingId == productId) {
                                imeiInExport = (String) exportModel.getValueAt(i, 8);
                                break;
                            }
                        }

                        if (!imeiInExport.isEmpty()) {
                            detailsExportView.getTxtImei().setText(imeiInExport);
                        } else {
                            detailsExportView.getTxtImei().setText("");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(detailsExportView, "Lỗi: " + ex.getMessage());
                    }

                    updateButtonStates(true);
                }
            }
        });

        detailsExportView.getBottomTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = detailsExportView.getBottomTable().getSelectedRow();
                if (selectedRow != -1) {
                    updateButtonStates(false);

                    DefaultTableModel model = (DefaultTableModel) detailsExportView.getBottomTable().getModel();

                    int id = (int) model.getValueAt(selectedRow, 1); // Mã SP
                    String name = (String) model.getValueAt(selectedRow, 2);
                    String ram = (String) model.getValueAt(selectedRow, 3);
                    String rom = (String) model.getValueAt(selectedRow, 4);
                    String color = (String) model.getValueAt(selectedRow, 5);
                    double price = (double) model.getValueAt(selectedRow, 6);
                    int quantity = (int) model.getValueAt(selectedRow, 7);
                    String imei = (String) model.getValueAt(selectedRow, 8);

                    detailsExportView.getTxtID().setText(String.valueOf(id));
                    detailsExportView.getTxtName().setText(name);
                    detailsExportView.getComboBox_Config().setText(rom + " - " + ram + " - " + color);

                    DecimalFormat df = new DecimalFormat("#,###");
                    detailsExportView.getTxtPrice().setText(df.format(price));
                    detailsExportView.getTxtQuantity().setText(String.valueOf(quantity));

                    detailsExportView.getTxtImei().setText(imei);
                }
            }
        });

        if (!isImeiInitialized) {
            isImeiInitialized = true;

            detailsExportView.getBtnImei().addActionListener(e -> {
                String idText = detailsExportView.getTxtID().getText().trim();
                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(detailsExportView, "Vui lòng chọn sản phẩm trước!");
                    return;
                }

                int productId = Integer.parseInt(idText);
                List<String> list = chiTietSanPhamDAO.getImeimasp(productId, 1);
                SelectImei listImei = new SelectImei(list);

                listImei.getBtnConfirm().addActionListener(evt -> {
                    List<String> selectedImeis = listImei.getSelectedImeis();
                    detailsExportView.getTxtImei().setText(String.join("\n", selectedImeis));
                    System.out.println("IMEI đã chọn: " + selectedImeis);
                    listImei.dispose();
                });

                listImei.setVisible(true);
            });
        }


        if (!isBtnAddListenerInitialized) {
            isBtnAddListenerInitialized = true;
        detailsExportView.getBtnAdd().addActionListener(e -> {
            String idText = detailsExportView.getTxtID().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(detailsExportView, "Vui lòng chọn sản phẩm trước!");
                return;
            }

            int productId = Integer.parseInt(idText);
            String productName = detailsExportView.getTxtName().getText();
            String config = detailsExportView.getComboBox_Config().getText();

            String[] parts = config.split(" - ");
            String rom = parts.length > 0 ? parts[0] : "";
            String ram = parts.length > 1 ? parts[1] : "";
            String color = parts.length > 2 ? parts[2] : "";

            String priceText = detailsExportView.getTxtPrice().getText().trim().replace(".", "").replace(",", "");
            double price = Double.parseDouble(priceText);

            String imeiText = detailsExportView.getTxtImei().getText().trim();
            String[] imeisToAdd = imeiText.split("\n");

            List<String> cleanImeis = new ArrayList<>();
            for (String imei : imeisToAdd) {
                if (!imei.trim().isEmpty()) {
                    cleanImeis.add(imei.trim());
                }
            }

            if (cleanImeis.isEmpty()) {
                JOptionPane.showMessageDialog(detailsExportView, "Vui lòng chọn IMEI!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) detailsExportView.getBottomTable().getModel();
            Set<String> existingImeis = new HashSet<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String existingImeiText = (String) model.getValueAt(i, 8);
                if (existingImeiText != null && !existingImeiText.isEmpty()) {
                    String[] imeis = existingImeiText.split("\n");
                    for (String imei: imeis) {
                        existingImeis.add(imei.trim());
                    }
                }
            }

            for (String imei : cleanImeis) {
                if (existingImeis.contains(imei)) {
                    JOptionPane.showMessageDialog(detailsExportView, "IMEI '" + imei + "' đã tồn tại trong danh sách!");
                    return;
                }
            }

            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    productId,
                    productName,
                    ram,
                    rom,
                    color,
                    price,
                    cleanImeis.size(),
                    String.join("\n", cleanImeis)
            });

            detailsExportView.getLblTotalPrice().setText(getFormattedTongTienNhap());
            JOptionPane.showMessageDialog(detailsExportView, "Đã thêm sản phẩm vào danh sách xuất");
        });}


        detailsExportView.getBtnEdit().addActionListener(e -> {
            int selectedRow = detailsExportView.getBottomTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(detailsExportView, "Vui lòng chọn dòng cần sửa trong bảng dưới!");
                return;
            }

            String imeiText = detailsExportView.getTxtImei().getText().trim();
            String[] imeiLines = imeiText.split("\n");

            int quantity = 0;
            for (String line : imeiLines) {
                if (!line.trim().isEmpty()) {
                    quantity++;
                }
            }

            DefaultTableModel model = (DefaultTableModel) detailsExportView.getBottomTable().getModel();
            model.setValueAt(quantity, selectedRow, 7);
            model.setValueAt(imeiText, selectedRow, 8);

            JOptionPane.showMessageDialog(detailsExportView, "Đã cập nhật IMEI cho sản phẩm!");

            detailsExportView.getLblTotalPrice().setText(getFormattedTongTienNhap());
        });



        detailsExportView.getBtnDelete().addActionListener(e -> {
            int[] selectedRows = detailsExportView.getBottomTable().getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm để xóa !");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa các sản phẩm đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) detailsExportView.getBottomTable().getModel();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    model.removeRow(selectedRows[i]);
                }
                clearForm();

                detailsExportView.getLblTotalPrice().setText(getFormattedTongTienNhap());


            }
        });
        if (!isBtnImportListenerInitialized) {
            isBtnImportListenerInitialized = true;
            detailsExportView.getBtnImport_goods().addActionListener(e -> {
                xuatHang();
            });
        }



    }
    private int xuatHang() {


        JTable table = detailsExportView.getBottomTable();

        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(detailsExportView, "Vui lòng thêm sản phẩm trước khi xuất hàng!");
            return 0;
        }

        try {

            KhachHang khachHang = (KhachHang) detailsExportView.getComboBox_KhachHang().getSelectedItem();
            if (khachHang == null) {
                JOptionPane.showMessageDialog(detailsExportView, "Vui lòng chọn nhà cung cấp!");
                return 0;
            }

            // Tạo phiếu xuất
            phieuXuat phieuXuat = new phieuXuat();
            int maKh = khachHang.getId();

            String idText = detailsExportView.getTxtId_import().getText();
            String numericPart = idText.replaceAll("\\D+", "");
            int id = Integer.parseInt(numericPart);

            String tongTienText = getFormattedTongTienNhap();
            String cleaned = tongTienText.replaceAll("[^\\d]", "");
            int tongtien = Integer.parseInt(cleaned);

            phieuXuat.setMaphieuXuat(id);
            phieuXuat.setThoigiantao(generateImport());
            phieuXuat.setManguoitao(nhanVien.getManv());
            phieuXuat.setMaKhachHang(maKh);
            phieuXuat.setTongTien(tongtien);

            int result = phieuXuatdao.themPhieuXuat(phieuXuat);

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                int maSanPham = (int) model.getValueAt(i, 1);
                int maPhienBanSP = chitietPhieuNhapDAO.getMaPhienBanSPTheoMaSP(maSanPham);
                int soLuong = (int) model.getValueAt(i, 7);
                double donGia = (double) model.getValueAt(i, 6);
                String imeiStr = (String) model.getValueAt(i, 8);

                if (!chitietPhieuXuatDAO.isChiTietTonTai(id, maPhienBanSP)) {
                    chitietPhieuXuatDAO.insertCTPhieuXuat(id, maPhienBanSP, soLuong, donGia);



                    String[] imeis = imeiStr.split("\n");
                    for (String imei : imeis) {
                        System.out.println(imei);
                        String trimmed = imei.trim();
                        if (!trimmed.isEmpty()) {
                            chiTietSanPhamDAO.updatePhieuXuat(trimmed, id);
                        }
                    }
                    SanPhamDAO.capNhatSoluongKhiXuat(maSanPham, soLuong);
                    detailsExportView.updateTable(sanPhamDAO.getAllSanPham());



                } else {
                    System.out.println("Chi tiết đã tồn tại, bỏ qua sản phẩm mã: " + maSanPham);
                }
            }
            List<phieuXuat> ds = phieuXuatdao.layDanhSachPhieuXuat();
            view.updateTable(ds);
            if (result > 0) {
                JOptionPane.showMessageDialog(detailsExportView, "Thêm phiếu xuất thành công!");
                model.setRowCount(0);
                detailsImportView.updateTable(sanPhamDAO.getAllSanPham());
                detailsExportView.resetExportId();
                clearForm();


            } else {
                JOptionPane.showMessageDialog(detailsExportView, "Lỗi khi thêm phiếu xuất!");
            }

            return result;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(detailsExportView, "Định dạng số không hợp lệ! " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(detailsExportView, "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }


    private void clearForm() {
        detailsExportView.getTxtID().setText("");
        detailsExportView.getTxtName().setText("");
        detailsExportView.getComboBox_Config().setText("");
        detailsExportView.getTxtPrice().setText("");
        detailsExportView.getTxtImei().setText("");
        detailsExportView.getTxtQuantity().setText("");
    }

    private void updateButtonStates(boolean isAddMode) {
        detailsExportView.getBtnAdd().setEnabled(isAddMode);
        detailsExportView.getBtnEdit().setEnabled(!isAddMode);
        detailsExportView.getBtnDelete().setEnabled(!isAddMode);

        if (isAddMode) {
            detailsExportView.getBtnEdit().setBackground(Color.LIGHT_GRAY);
            detailsExportView.getBtnDelete().setBackground(Color.LIGHT_GRAY);
            detailsExportView.getBtnAdd().setBackground(new Color(96, 138, 104));
        } else {
            detailsExportView.getBtnAdd().setBackground(new Color(216, 215, 215));
            detailsExportView.getBtnEdit().setBackground(new Color(204, 172, 61));
            detailsExportView.getBtnDelete().setBackground(new Color(211, 91, 91));
        }

    }


    public String getFormattedTongTienNhap() {
        DefaultTableModel model = (DefaultTableModel) detailsExportView.getBottomTable().getModel();
        double tongTien = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object donGiaObj = model.getValueAt(i, 6);
            Object soLuongObj = model.getValueAt(i, 7);

            if (donGiaObj != null && soLuongObj != null) {
                try {
                    double donGia = Double.parseDouble(donGiaObj.toString());
                    int soLuong = Integer.parseInt(soLuongObj.toString());
                    tongTien += donGia * soLuong;
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(tongTien) + " VND";
    }


    public Timestamp generateImport() {
        return new Timestamp(System.currentTimeMillis());
    }


}
