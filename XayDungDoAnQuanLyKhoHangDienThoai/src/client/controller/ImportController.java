package client.controller;

import client.view.components.TextFieldUtils;
import client.view.form.PhieuNhap.DetailsImportView;
import client.view.form.PhieuNhap.ExportExcel;
import client.view.form.PhieuNhap.ExportPDF;
import client.view.form.PhieuNhap.InformationImportForm;
import client.view.form.phieuXuat.DetailsExportView;
import client.view.shared.Toast;
import client.view.views.ImportView;
import server.DAO.*;
import shared.models.*;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



    public class ImportController {
        private ImportView view;
        private InformationImportForm informationImportForm;
        private DetailsImportView detailsImportView;
        private DetailsExportView detailsExportView;
        private SanPhamDAO sanPhamDAO = new SanPhamDAO();
        private NhanVien nhanVien;
        private ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/!.png"));
        private ImageIcon successIcon2 = new ImageIcon(getClass().getResource("/images/success.png"));
        private ImageIcon successIcon3 = new ImageIcon(getClass().getResource("/images/Warring.png"));
        private boolean isAddPhieuNhapInitialized = false;
        private List<phieuNhap> fullList = new ArrayList<>();

        public ImportController(ImportView view, DetailsImportView detailsImportView, NhanVien nhanVien, DetailsExportView detailsExportView) {
            this.view = view;
            this.detailsImportView = detailsImportView;
            this.detailsExportView = detailsExportView;
            this.nhanVien = nhanVien;
            initController();
        }

        public ImportController() {}

        public void initController() {
            fullList = phieuNhapDAO.layDanhSachPhieuNhap();
            view.updateTable(fullList);
            setupComboBoxData();
            setupMainActions();
            setupFilterListeners();
        }

        private void setupComboBoxData() {
            // Nhà cung cấp
            NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
            List<String> suppliers = new ArrayList<>();
            suppliers.add("Tất cả");
            nhaCungCapDAO.selectAll().forEach(ncc -> suppliers.add(ncc.getTenncc()));
            view.getSupplierComboBox().setModel(new DefaultComboBoxModel<>(suppliers.toArray(new String[0])));

            // Nhân viên
            NhanVienDAO nhanVienDAO = new NhanVienDAO();
            List<String> staff = new ArrayList<>();
            staff.add("Tất cả");
            nhanVienDAO.selectAll().forEach(nv -> staff.add(nv.getHoten()));
            view.getStaffComboBox().setModel(new DefaultComboBoxModel<>(staff.toArray(new String[0])));
        }

        private void setupMainActions() {
            view.getTopPanel().getBtnAdd().addActionListener(e -> openAddPhieuNhap());
            view.getTopPanel().getBtnDelete().addActionListener(e -> deleteImport());
            view.getTopPanel().getBtnDetail().addActionListener(e -> openPhieuNhapChiTiet());

            view.getTopPanel().getBtnExport().addActionListener(e -> {
                int[] selectedRows = view.getTable().getSelectedRows();
                if (selectedRows.length == 0) {
                    new Toast(detailsImportView, "Please", "Vui lòng chọn phiếu nhập để xuất !", 1500, successIcon);
                    return;
                }
                List<phieuNhap> listPn = new ArrayList<>();
                for (int row : selectedRows) {
                    int idPhieuNhap = (int) view.getTable().getValueAt(row, 1);
                    phieuNhap pn = phieuNhapDAO.layPhieuNhapTheoID(idPhieuNhap);
                    if (pn != null) {
                        listPn.add(pn);
                    }
                }
                ExportExcel.ExportExcel(listPn);
            });

            view.getTopPanel().getBtnReload().addActionListener(e -> {
                view.getSupplierComboBox().setSelectedIndex(0);
                view.getStaffComboBox().setSelectedIndex(0);
                view.getFromDateChooser().setDate(null);
                view.getToDateChooser().setDate(null);
                view.getFromAmountField().setText("");
                view.getToAmountField().setText("");
                view.resetSearchField();
                view.getTopPanel().getTxtSearch().setText("");
                TextFieldUtils.setPlaceholder(view.getTopPanel().getTxtSearch(), "Nhập nội dung tìm kiếm..");


                fullList = phieuNhapDAO.layDanhSachPhieuNhap();
                view.updateTable(fullList);
            });
        }

        private void setupFilterListeners() {
            view.getTopPanel().getTxtSearch().getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { applyAllFilters(); }
                public void removeUpdate(DocumentEvent e) { applyAllFilters(); }
                public void changedUpdate(DocumentEvent e) { applyAllFilters(); }
            });

            view.getSupplierComboBox().addActionListener(e -> applyAllFilters());
            view.getStaffComboBox().addActionListener(e -> applyAllFilters());
            view.getFromDateChooser().addPropertyChangeListener("date", e -> applyAllFilters());
            view.getToDateChooser().addPropertyChangeListener("date", e -> applyAllFilters());

            view.getFromAmountField().getDocument().addDocumentListener(new FilterListener());
            view.getToAmountField().getDocument().addDocumentListener(new FilterListener());
        }

        private void applyAllFilters() {
            String supplier = (String) view.getSupplierComboBox().getSelectedItem();
            String staff = (String) view.getStaffComboBox().getSelectedItem();
            Date fromDate = view.getFromDateChooser().getDate();
            Date toDate = view.getToDateChooser().getDate();
            String fromAmountStr = view.getFromAmountField().getText().trim();
            String toAmountStr = view.getToAmountField().getText().trim();
            String searchKeyword = view.getSearchText();

            List<phieuNhap> filtered = fullList.stream().filter(p -> {
                boolean matchSupplier = supplier.equals("Tất cả") || p.getTennhacungcap().equalsIgnoreCase(supplier);
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
                boolean matchSearch = String.valueOf(p.getMaphieu()).toLowerCase().contains(searchKeyword) ||
                        p.getTennhacungcap().toLowerCase().contains(searchKeyword) ||
                        p.getTennhanvien().toLowerCase().contains(searchKeyword);

                return matchSupplier && matchStaff && matchDate && matchAmount && matchSearch;
            }).toList();

            view.updateTable(filtered);
        }

        private class FilterListener implements DocumentListener {
            public void insertUpdate(DocumentEvent e) { applyAllFilters(); }
            public void removeUpdate(DocumentEvent e) { applyAllFilters(); }
            public void changedUpdate(DocumentEvent e) { applyAllFilters(); }
        }



    private void deleteImport() {

        int[] selectedRows = view.getTable().getSelectedRows();
        if (selectedRows.length == 0) {
            new Toast(detailsImportView, "Please", "Vui lòng chọn phiếu nhập để xóa !", 1500,successIcon);


            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa phiếu nhập này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            for (int row : selectedRows) {

                System.out.println("Size sau khi xóa: " + phieuNhapDAO.layDanhSachPhieuNhap().size());

                int maphieu = (int) view.getTable().getValueAt(row, 1);
                int rows = phieuNhapDAO.deletePhieuNHap(maphieu);
                System.out.println("Xóa phiếu nhập ID: " + maphieu + ", kết quả: " + rows);
                if(rows <= 0){
                    JOptionPane.showMessageDialog(view,"Xóa không thành công phiếu nhập ID: " + maphieu);
                }
            }

            List<phieuNhap> ds = phieuNhapDAO.layDanhSachPhieuNhap();
            System.out.println("Size sau khi xóa: " + ds.size());
            view.updateTable(ds);

            new Toast(detailsImportView, "Success", "Xóa thành công !", 1500,successIcon2);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
        }



    }

    private void openPhieuNhapChiTiet() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            new Toast(detailsImportView, "Please", "Chọn phiếu nhập cần hiển thị !", 1500,successIcon);

            return;
        }

        try {
            int phieuNhapID = (int) view.getTable().getValueAt(selectedRow, 1);
            if (phieuNhapID <= 0) {
                JOptionPane.showMessageDialog(view, "ID phiếu nhập không hợp lệ");
                return;
            }

            phieuNhap pn = phieuNhapDAO.layPhieuNhapTheoID(phieuNhapID);
            List<ChiTietPhieuNhapDTO> list = chitietPhieuNhapDAO.thongTinPhieuNhap(phieuNhapID);

            InformationImportForm form = new InformationImportForm();
            form.loadPhieuNhap(pn, list);
            form.getTable().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow =   form.getTable().getSelectedRow();
                    if (selectedRow != -1) {
                        int maSP = (int) form.getTableModel().getValueAt(selectedRow, 1);

                        form.loadImei(maSP,phieuNhapID);
                    }
                }
            });

            form.getBtnClose().addActionListener(e -> form.dispose());

            form.getBtnExport().addActionListener(e -> {
                ExportPDF.ExportPDF(pn.getMaphieu(), pn.getTennhacungcap(), pn.getTennhanvien(), pn.getThoigiantao(), list);
            });

            form.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hiển thị: " + ex.getMessage());
        }
    }

    private void openAddPhieuNhap() {
        if (isAddPhieuNhapInitialized) return;
        isAddPhieuNhapInitialized = true;
        JTable table = detailsImportView.getTable_Product();

        // Bắt sự kiện chọn sản phẩm từ bảng
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

                        detailsImportView.getTxtID().setText(String.valueOf(productId));
                        detailsImportView.getTxtName().setText(sanPham.getTenSanPham());
                        detailsImportView.getComboBox_Config().setText(config);
                        DecimalFormat df = new DecimalFormat("#,###");
                        detailsImportView.getTxtPrice().setText(df.format(cauhinh.getGianhap()));
                        detailsImportView.getTxtImei().setText(" ");
                        detailsImportView.getTxtQuantity().setText(" ");


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(detailsImportView, "Lỗi: " + ex.getMessage());
                    }
                    updateButtonStates(true);
                    detailsImportView.getComboBox_Method().setEnabled(true);


                }
                else {
                    System.out.println("loi");
                }
            }
        });
        detailsImportView.getBottomTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = detailsImportView.getBottomTable().getSelectedRow();
                if (selectedRow != -1) {
                    updateButtonStates(false);

                    // Bổ sung đoạn này để đổ dữ liệu dòng được chọn về form
                    DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();

                    int id = (int) model.getValueAt(selectedRow, 1); // ID sản phẩm
                    String name = (String) model.getValueAt(selectedRow, 2);
                    String ram = (String) model.getValueAt(selectedRow, 3);
                    String rom = (String) model.getValueAt(selectedRow, 4);
                    String color = (String) model.getValueAt(selectedRow, 5);
                    double price = (double) model.getValueAt(selectedRow, 6);
                    int quantity = (int) model.getValueAt(selectedRow, 7);
                    String imei = (String) model.getValueAt(selectedRow, 8);
                    int method = (int) model.getValueAt(selectedRow, 9);
                    detailsImportView.getComboBox_Method().setSelectedIndex(method);

                    // Đổ lên form
                    detailsImportView.getTxtID().setText(String.valueOf(id));
                    detailsImportView.getTxtName().setText(name);
                    detailsImportView.getComboBox_Config().setText(rom + " - " + ram + " - " + color);
                    DecimalFormat df = new DecimalFormat("#,###");
                    detailsImportView.getTxtPrice().setText(df.format(price));
                    detailsImportView.getTxtQuantity().setText(String.valueOf(quantity));
                    detailsImportView.getTxtImei().setText(imei);
                    detailsImportView.getComboBox_Method().setEnabled(false);
                    if (method == 0) {
                        detailsImportView.getLblImei().setText("Mã Imei");
                        detailsImportView.getLblQuantity().setVisible(false);
                        detailsImportView.getTxtQuantity().setVisible(false);
                    } else {
                        detailsImportView.getLblImei().setText("Mã Imei bắt đầu");
                        detailsImportView.getLblQuantity().setVisible(true);
                        detailsImportView.getTxtQuantity().setVisible(true);
                    }

                }
            }
        });




        detailsImportView.getComboBox_Method().addItemListener(e -> {
            int index = detailsImportView.getComboBox_Method().getSelectedIndex();
            if (index == 0) {
                detailsImportView.getLblImei().setText("Mã Imei");
                detailsImportView.getLblQuantity().setVisible(false);
                detailsImportView.getTxtQuantity().setVisible(false);
            } else {
                detailsImportView.getLblImei().setText("Mã Imei bắt đầu");
                detailsImportView.getLblQuantity().setVisible(true);
                detailsImportView.getTxtQuantity().setVisible(true);
            }
        });

        // Xử lý nút "Thêm"

        detailsImportView.getBtnAdd().addActionListener(e -> {
            String imei = detailsImportView.getTxtImei().getText().trim();



            int index = detailsImportView.getComboBox_Method().getSelectedIndex();
            String idText = detailsImportView.getTxtID().getText().trim();
            if (idText.isEmpty()) {
                new Toast(detailsImportView, "Please", "Vui lòng chọn sản phẩm !", 1500,successIcon);
                return;
            }

            if (!imei.matches("\\d{15}")) {
                new Toast(detailsImportView, "Warning", "IMEI đúng 15 chữ số !", 1500,successIcon3);
                return;
            }
            int productId = Integer.parseInt(idText);

            String productName = detailsImportView.getTxtName().getText();
            String config = detailsImportView.getComboBox_Config().getText();
            String[] parts = config.split(" - ");
            String rom = parts.length > 0 ? parts[0] : "";
            String ram = parts.length > 1 ? parts[1] : "";
            String color = parts.length > 2 ? parts[2] : "";
            String priceText = detailsImportView.getTxtPrice().getText().trim().replace(".", "").replace(",", "");
            double price = Double.parseDouble(priceText);
            ;


            int quantity = index == 0 ? 1 : -1;
            System.out.println(quantity);
            if (index != 0) {
                String quantityText = detailsImportView.getTxtQuantity().getText().trim();
                if (quantityText.isEmpty()) {
                    new Toast(detailsImportView, "Warning", "Số lượng phải lớn hơn 0 !", 1500,successIcon3);

                    return;
                }
                try {
                    quantity = Integer.parseInt(quantityText);
                    if (quantity <= 0 ) {
                        new Toast(detailsImportView, "Warning", "Số lượng phải lớn hơn 0 !", 1500,successIcon3);

                        return;
                    }
                    System.out.println(quantity);

                } catch (NumberFormatException ex) {
                    new Toast(detailsImportView, "Warning", "Số lượng phải là số nguyên !", 1500,successIcon3);
                    return;
                }
            }


            DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();
            int rowCountBefore = model.getRowCount();

            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    productId,
                    productName,
                    ram,
                    rom,
                    color,
                    price,
                    quantity,
                    imei,
                    index
            });


            System.out.println("Thêm sản phẩm");
            int rowCountAfter = model.getRowCount();

            if (rowCountAfter > rowCountBefore) {
                detailsImportView.getComboBox_Method().setEnabled(false);
                detailsImportView.getLblTotalPrice().setText(getFormattedTongTienNhap());
                new Toast(detailsImportView, "Success", "Thêm sản phẩm thành công!", 1500,successIcon2);



            } else {
                ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/Warring.png"));
                new Toast(detailsImportView, "Warning", "Thêm sản phẩm thất bại !", 1500,successIcon3);
            }

        });

        detailsImportView.getBtnEdit().addActionListener(e -> {
            int selectedRow = detailsImportView.getBottomTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(detailsImportView, "Vui lòng chọn dòng cần sửa trong bảng dưới!");
                return;
            }

            String imei = detailsImportView.getTxtImei().getText().trim();
            if (!imei.matches("\\d{15}")) {
                JOptionPane.showMessageDialog(detailsImportView, "IMEI phải gồm đúng 15 chữ số");
                return;
            }

            int index = detailsImportView.getComboBox_Method().getSelectedIndex();

            DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();

            if (index == 0) {
                model.setValueAt(imei, selectedRow, 8);
                new Toast(detailsImportView, "Success", "Đã sửa IMEI thành công!", 1500,successIcon2);
            }
            else {
                String idText = detailsImportView.getTxtID().getText().trim();
                String quantity = detailsImportView.getTxtQuantity().getText().trim();
                if (quantity.isEmpty()) {
                    JOptionPane.showMessageDialog(detailsImportView, "Số lượng phải lớn hơn 0!");
                    return;
                }
                try {
                    int  quantityy = Integer.parseInt(quantity);
                    if (quantityy <= 0 ) {
                        JOptionPane.showMessageDialog(detailsImportView, "Số lượng phải lớn hơn 0!");
                        return;
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(detailsImportView, "Số lượng phải là số nguyên!");
                    return;
                }
                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(detailsImportView, "Vui lòng chọn sản phẩm để sửa ID!");
                    return;
                }

                try {
                    int productId = Integer.parseInt(idText);
                    model.setValueAt(productId, selectedRow, 1);
                    model.setValueAt(imei, selectedRow, 8);
                    model.setValueAt(quantity, selectedRow, 7);
                    detailsImportView.getLblTotalPrice().setText(getFormattedTongTienNhap());
                    new Toast(detailsImportView, "Success", "Sửa thành công!", 1500,successIcon2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(detailsImportView, "ID phải là số nguyên!");
                }
            }


        });
        detailsImportView.getBtnDelete().addActionListener(e -> {
            int[] selectedRows = detailsImportView.getBottomTable().getSelectedRows();
            if (selectedRows.length == 0) {
                new Toast(detailsImportView, "Please", "Vui lòng chọn sản phẩm để xóa !", 1500,successIcon);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa các sản phẩm đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    model.removeRow(selectedRows[i]);
                }
                clearForm();

                detailsImportView.getLblTotalPrice().setText(getFormattedTongTienNhap());


            }
        });
        detailsImportView.getBtnImport_goods().addActionListener(e -> {
            nhaphang();

        });




    }
    private int nhaphang() {
        JTable table = detailsImportView.getBottomTable();

        if (table.getRowCount() == 0) {
            new Toast(detailsImportView, "Please", "Vui lòng thêm sản phẩm trước khi nhập hàng!", 1500,successIcon);
            return 0;
        }

        try {
            NhaCungCap nhaCungCap = (NhaCungCap) detailsImportView.getComboBox_Supplier().getSelectedItem();
            if (nhaCungCap == null) {
                JOptionPane.showMessageDialog(detailsImportView, "Vui lòng chọn nhà cung cấp!");
                return 0;
            }

            // Tạo phiếu nhập mới
            phieuNhap phieuNhap = new phieuNhap();
            int maNhaCungCap = nhaCungCap.getMancc();

            String idText = detailsImportView.getTxtId_import().getText();
            String numericPart = idText.replaceAll("\\D+", "");
            int id = Integer.parseInt(numericPart);

            String tongTienText = getFormattedTongTienNhap();
            String cleaned = tongTienText.replaceAll("[^\\d]", "");
            int tongtien = Integer.parseInt(cleaned);

            phieuNhap.setMaphieu(id);
            phieuNhap.setManhacungcap(maNhaCungCap);
            phieuNhap.setThoigiantao(generateImport());
            phieuNhap.setManguoitao(nhanVien.getManv());
            phieuNhap.setTongTien(tongtien);

            int result = phieuNhapDAO.themPhieuNHap(phieuNhap);

            DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                int maSanPham = (int) model.getValueAt(i, 1);
                int maPhienBanSP = chitietPhieuNhapDAO.getMaPhienBanSPTheoMaSP(maSanPham);
                int soLuong = (int) model.getValueAt(i, 7);
                double donGia = (double) model.getValueAt(i, 6);
                int methodIndex = (int) model.getValueAt(i, 9);
                String hinhThucNhap = (methodIndex == 0) ? "Nhập từng máy" : "Nhập theo lô";

                if (!chitietPhieuNhapDAO.isChiTietTonTai(id, maPhienBanSP)) {
                    chitietPhieuNhapDAO.insertCTPhieuNhap(id, maPhienBanSP, soLuong, donGia, hinhThucNhap);
                    SanPhamDAO.capNhatSoluongKhiNhap(maSanPham, soLuong);
                    detailsImportView.updateTable(sanPhamDAO.getAllSanPham());

                    String imeiStr = (String) model.getValueAt(i, 8);
                    if (methodIndex == 0) {
                        if (imeiStr != null && imeiStr.matches("\\d{15}")) {
                            if (chiTietSanPhamDAO.isImeiTonTai(imeiStr)) {
                                new Toast(detailsImportView, "Warning ", "IMEI đã tồn tại !" + imeiStr, 1500,successIcon3);
                                continue;
                            }
                            chiTietSanPhamDAO.insertImei(maPhienBanSP, imeiStr, id);
                        }
                    } else {
                        if (imeiStr != null && imeiStr.matches("\\d{15}")) {
                            long baseImei = Long.parseLong(imeiStr);
                            for (int j = 0; j < soLuong; j++) {
                                String imeiGenerated = String.format("%015d", baseImei + j);
                                if (chiTietSanPhamDAO.isImeiTonTai(imeiGenerated)) {
                                    new Toast(detailsImportView, "Warning", "IMEI đã tồn tại !" + imeiGenerated, 1500,successIcon3);
                                    continue;
                                }
                                chiTietSanPhamDAO.insertImei(maPhienBanSP, imeiGenerated, id);
                            }
                        }
                    }

                } else {
                    System.out.println("Chi tiết đã tồn tại, bỏ qua sản phẩm mã: " + maSanPham);
                }
            }


            if (result > 0) {
                new Toast(detailsImportView, "Success", "Thêm phiếu nhập thành công!", 1500,successIcon2);
                List<phieuNhap> ds = phieuNhapDAO.layDanhSachPhieuNhap();
                view.updateTable(ds);
                model.setRowCount(0);
                detailsExportView.updateTable(sanPhamDAO.getAllSanPham());
                clearForm();
                detailsImportView.resetImportId();
            } else {
                new Toast(detailsImportView, "Warning", "Lỗi khi thêm phiếu nhập!", 1500,successIcon3);
            }

            return result;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(detailsImportView, "Định dạng số không hợp lệ! " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(detailsImportView, "Đã xảy ra lỗi hi hi : " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }




    private void clearForm() {
        detailsImportView.getTxtID().setText("");
        detailsImportView.getTxtName().setText("");
        detailsImportView.getComboBox_Config().setText("");
        detailsImportView.getTxtPrice().setText("");
        detailsImportView.getTxtImei().setText("");
        detailsImportView.getTxtQuantity().setText("");
    }

    private void updateButtonStates(boolean isAddMode) {
        detailsImportView.getBtnAdd().setEnabled(isAddMode);
        detailsImportView.getBtnEdit().setEnabled(!isAddMode);
        detailsImportView.getBtnDelete().setEnabled(!isAddMode);

        if (isAddMode) {
            detailsImportView.getBtnEdit().setBackground(Color.LIGHT_GRAY);
            detailsImportView.getBtnDelete().setBackground(Color.LIGHT_GRAY);
            detailsImportView.getBtnAdd().setBackground(new Color(96, 138, 104));
        } else {
            detailsImportView.getBtnAdd().setBackground(new Color(216, 215, 215));
            detailsImportView.getBtnEdit().setBackground(new Color(204, 172, 61));
            detailsImportView.getBtnDelete().setBackground(new Color(211, 91, 91));
        }

    }


    public String getFormattedTongTienNhap() {
        DefaultTableModel model = (DefaultTableModel) detailsImportView.getBottomTable().getModel();
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
