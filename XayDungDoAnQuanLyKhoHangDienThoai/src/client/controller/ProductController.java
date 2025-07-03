package client.controller;

import client.view.form.ExcelExporter;
import client.view.form.SanPham.*;
import client.view.shared.Toast;
import client.view.views.ProductView;
import com.mysql.cj.xdevapi.Client;
import network.SocketManager;
import server.DAO.SanPhamDAO;
import server.DAO.cauHinhDAO;
import shared.models.*;
import shared.request.AddFullProductRequest;
import shared.request.DeleteProductRequest;
import shared.request.EditCauhinhRequest;
import shared.request.EditSanPhamRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ProductController {
    private ProductView view;
    private AddProductCauHinhForm addProductCauHinhForm;
    private ArrayList<SanPham> listSp = new ArrayList<>();
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private   SocketManager sm = SocketManager.getInstance();
    private  ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/!.png"));
    private  ImageIcon successIcon2 = new ImageIcon(getClass().getResource("/images/success.png"));
    private  ImageIcon successIcon3 = new ImageIcon(getClass().getResource("/images/Warring.png"));




    public ProductController(ProductView view) throws Exception {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getTopPanel().getBtnAdd().addActionListener(e -> openAddProductForm(new AddProductForm()));
        view.getTopPanel().getBtnEdit().addActionListener(e -> openEditProductForm(new EditProductForm()));
        view.getTopPanel().getBtnDelete().addActionListener(e -> deleteSelectedProduct());
        view.getTopPanel().getTxtSearch().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                openSearch();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                openSearch();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                openSearch();
            }
        });
        view.getTopPanel().getBtnReload().addActionListener(e -> reloadTable());
        view.getTopPanel().getBtnDetail().addActionListener(e -> openSanPhamChitiet(new sanPhamChitiet()));
        view.getTopPanel().getBtnExport().addActionListener(e -> {
            int[] selectedRows = view.getTable().getSelectedRows();
            if (selectedRows.length == 0) {
                new Toast(view, "Please", "Vui lòng chọn sản phẩm !", 1500,successIcon);

                return;
            }
            List<SanPham> selectedSanPhams = new ArrayList<>();

            for (int row : selectedRows) {
                int sanPhamId = (int) view.getTable().getValueAt(row, 0);
                SanPham sp = sanPhamDAO.getSanPhamById(sanPhamId);
                if (sp != null) {
                    selectedSanPhams.add(sp);
                }
            }

            ExcelExporter.exportSanPhamListToExcel(selectedSanPhams);
        });
    }


    private void openAddProductForm(AddProductForm form) {
        form.getBtnSave().addActionListener(e -> saveProduct(form));
        form.getBtnCancel().addActionListener(e -> form.dispose());
        form.setVisible(true);
    }


    private void openAddProductForm2(AddProductForm form) {
        form.getBtnSave().addActionListener(e -> saveProduct(form));
        form.getBtnCancel().addActionListener(e -> form.dispose());
    }

    private void openSanPhamChitiet(sanPhamChitiet sanPhamChitiet) {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Chọn sản phẩm cần hiển thị!");
            return;
        }
        try {
            int productId = (int) view.getTable().getValueAt(selectedRow, 0);
            System.out.println(productId);
            if (productId <= 0) {
                JOptionPane.showMessageDialog(view, "ID sản phẩm không hợp lệ!");
                return;
            }
            sanPhamChitiet.loadSanPham(sanPhamDAO.getSanPhamById(productId));
            ;
            sanPhamChitiet.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hiển thị sản phẩm: " + ex.getMessage());
        }
    }

    private void saveProduct(AddProductForm form) {
        String tenSp = form.getTxtProductName().getText().trim();
        XuatXu xuatXu= (XuatXu) form.getTxtOrigin().getSelectedItem();
        int idXuatXu = xuatXu.getId();
        String Chip = form.getTxtChip().getText().trim();
        String DungLuongPin = form.getTxtBattery().getText().trim();
        String KichThuocMan = form.getTxtScreenSize().getText().trim();
        String CamSau = form.getTxtRearCamera().getText().trim();
        String CamTruoc = form.getTxtFrontCamera().getText().trim();
        HeDieuHanh hdh = (HeDieuHanh) form.getTxtOS().getSelectedItem();
        int idHeDieuHanh = hdh.getMaHeDieuHanh();
        String phienBanHDH = form.getTxtHdh().getText().toString().trim();
        String thoiGianBaoHanh = form.getTxtWarranty().getText().trim();
        thuonghieu th = (thuonghieu) form.getTxtBrand().getSelectedItem();
        int idThuongHieu = th.getMathuonghieu();
        String khuVucKho = form.getTxtWarehouse().getSelectedItem().toString().trim();
        String image_path = form.getImagePath().trim();

        try {
            if(tenSp.length() > 2){
                JOptionPane.showMessageDialog(form,"tên trên 2 kí tự");
                return;
            }
            int dungLuongPin = Integer.parseInt(DungLuongPin);
            int ThoiGianBaoHanh = Integer.parseInt(thoiGianBaoHanh);
            int PhienBanHDH = Integer.parseInt(phienBanHDH);
            double kichThuocMan = Double.parseDouble(KichThuocMan);

            SanPham sp = new SanPham();
            sp.setTenSanPham(tenSp);
            sp.setMaxuatxu(idXuatXu);
            sp.setChip(Chip);
            sp.setDungLuongPin(dungLuongPin);
            sp.setKichThuocMan(kichThuocMan);
            sp.setCamSau(CamSau);
            sp.setCamTruoc(CamTruoc);
            sp.setMahedieuhanh(idHeDieuHanh);
            sp.setPhienBanHDH(PhienBanHDH);
            sp.setThoiGianBaoHanh(ThoiGianBaoHanh);
            sp.setMathuonghieu(idThuongHieu);
            sp.setKhuVucKho(khuVucKho);
            sp.setImage_path(image_path);

            SanPhamBuilder.reset();
            SanPhamBuilder.sanPhamTam = sp;
            form.setVisible(false);
            System.out.println( SanPhamBuilder.sanPhamTam + "Đã lưu vô tạm");

            addProductCauHinhForm = new AddProductCauHinhForm();
            openAddProductCauHinhForm(addProductCauHinhForm);
            addProductCauHinhForm.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(form, "Nhập đúng thông tin");
        }
    }

    private void openAddProductCauHinhForm(AddProductCauHinhForm form) {
        form.getBtnAddSp().addActionListener(e -> {
            try {
                rom rom = (rom) form.getTxtROM().getSelectedItem();
                ram ram = (ram) form.getTxtRAM().getSelectedItem();
                mausac mausac = (mausac) form.getTxtColor().getSelectedItem();

                if (rom == null || ram == null || mausac == null) {
                    JOptionPane.showMessageDialog(form, "Vui lòng chọn đầy đủ ROM, RAM và Màu sắc.");
                    return;
                }

                double giaNhap, giaXuat;
                try {
                    giaNhap = Double.parseDouble(form.getTxtImportPrice().getText().trim());
                    giaXuat = Double.parseDouble(form.getTxtExportPrice().getText().trim());

                    if (giaNhap <= 0 || giaXuat <= 0) {
                        JOptionPane.showMessageDialog(form, "Giá phải lớn hơn 0");
                        return;
                    }

                    if (giaXuat < giaNhap) {
                        JOptionPane.showMessageDialog(form, "Giá xuất không được nhỏ hơn giá nhập");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(form, "Vui lòng nhập đúng định dạng số cho giá.");
                    return;
                }

                cauHinhSanPham cauHinh = new cauHinhSanPham();
                cauHinh.setMaram(ram.getMadlram());
                cauHinh.setMarom(rom.getMadlrom());
                cauHinh.setMamausac(mausac.getMamau());
                cauHinh.setGiaxuat(giaXuat);
                cauHinh.setGianhap(giaNhap);

                view.setProcessingRequest(true);

                AddFullProductRequest request = new AddFullProductRequest(SanPhamBuilder.sanPhamTam, cauHinh);
                sm.send(request);

                sm.setProductListListener(list -> {
                    updateTable(list);
                    JOptionPane.showMessageDialog(form, "Thêm thành công!" + request.getSanPham().getTenSanPham());

                    form.dispose();
                });

            } catch (SocketException se) {
                JOptionPane.showMessageDialog(form, "Mất kết nối đến server. Đang thử kết nối lại...");
                view.reconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(form, "Lỗi khi thêm sản phẩm: " + ex.getMessage());
            } finally {
                view.setProcessingRequest(false);
            }
        });

        form.getBtnBack().addActionListener(e -> {
            form.dispose();
            AddProductForm newForm = new AddProductForm();
            openAddProductForm2(newForm);
            newForm.loadSanPhamTam(SanPhamBuilder.sanPhamTam);
            newForm.setVisible(true);
        });
    }




    private void deleteSelectedProduct() {
        int selectedRow = view.getTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int productId = (int) view.getTable().getValueAt(selectedRow, 0);
            if (productId <= 0) {
                JOptionPane.showMessageDialog(view, "ID sản phẩm không hợp lệ!");
                return;
            }

            if (!sm.isConnected()) {
                JOptionPane.showMessageDialog(view, "Không thể kết nối tới server!");
                view.reconnect();
                return;
            }

            view.setProcessingRequest(true);

            DeleteProductRequest deleteRequest = new DeleteProductRequest("deleteProduct", productId);
            System.out.println("Đang gửi yêu cầu xóa: " + deleteRequest.getProductId());
            sm.send(deleteRequest);

            sm.setProductListListener(list -> {
                System.out.println("Danh sách sau khi xóa:");
                for (SanPham sp : list) {
                    System.out.println(sp);
                }
                updateTable(list);
                JOptionPane.showMessageDialog(view, "Xóa thành công!");
            });

        } catch (SocketException se) {
            JOptionPane.showMessageDialog(view, "Mất kết nối đến server. Đang thử kết nối lại...");
            view.reconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
        } finally {
            view.setProcessingRequest(false);
        }
    }


    private void openEditProductForm(EditProductForm form) {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Chọn sản phẩm cần sửa!");
            return;
        }
        int productId = (int) view.getTable().getValueAt(selectedRow, 0);
        form.sanPhamEdit(sanPhamDAO.getSanPhamById(productId));
        form.getBtnCancel().addActionListener(e -> form.dispose());
        form.getBtnSave().addActionListener(e -> editProduct(form));
        form.getBtnUp().addActionListener(e -> {
            form.dispose();
            EditProductCauHinhForm editProductCauHinhForm = new EditProductCauHinhForm();
            editProductCauHinhForm.cauhinhEdit(cauHinhDAO.getCauHinhByMaCauHinh(productId));
            openEditProductFormCauhinhForm(editProductCauHinhForm);
        });
        form.setVisible(true);

    }

    private void openEditProductFormCauhinhForm(EditProductCauHinhForm form) {
        int selectedRow = view.getTable().getSelectedRow();
        int productId = (int) view.getTable().getValueAt(selectedRow, 0);

        form.getBtnBack().addActionListener(e -> form.dispose());
        form.getBtnAddSp().addActionListener(e -> editProductCauHinhForm(form));
        form.setVisible(true);

    }

    private void editProduct(EditProductForm form) {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm để sửa!");
            return;
        }

        int productId = (int) view.getTable().getValueAt(selectedRow, 0);
        if (productId <= 0) {
            JOptionPane.showMessageDialog(view, "ID sản phẩm không hợp lệ!");
            return;
        }

        // Lấy dữ liệu từ form
        String tenSp = form.getTxtProductName().getText().trim();
        if (tenSp.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Tên sản phẩm không được để trống!");
            return;
        }

        XuatXu xuatXu = (XuatXu) form.getTxtOrigin().getSelectedItem();
        if (xuatXu == null) {
            JOptionPane.showMessageDialog(form, "Vui lòng chọn xuất xứ!");
            return;
        }

        thuonghieu th = (thuonghieu) form.getTxtBrand().getSelectedItem();
        if (th == null) {
            JOptionPane.showMessageDialog(form, "Vui lòng chọn thương hiệu!");
            return;
        }

        HeDieuHanh hdh = (HeDieuHanh) form.getTxtOS().getSelectedItem();
        if (hdh == null) {
            JOptionPane.showMessageDialog(form, "Vui lòng chọn hệ điều hành!");
            return;
        }

        String chip = form.getTxtChip().getText().trim();
        String dungLuongPin = form.getTxtBattery().getText().trim();
        String kichThuocMan = form.getTxtScreenSize().getText().trim();
        String camSau = form.getTxtRearCamera().getText().trim();
        String camTruoc = form.getTxtFrontCamera().getText().trim();
        String phienBanHDH = form.getTxtHdh().getText().trim();
        String thoiGianBaoHanh = form.getTxtWarranty().getText().trim();
        String imagePath = form.getImagePath().trim();
        String khuVucKho = form.getTxtWarehouse().getSelectedItem().toString().trim();

        // Kiểm tra và parse các trường số
        int dungLuongPinValue, phienBanheDH, thoiGianBaoHanhh;
        double kichThuocManValue;
        try {
            dungLuongPinValue = Integer.parseInt(dungLuongPin);
            kichThuocManValue = Double.parseDouble(kichThuocMan);
            phienBanheDH = Integer.parseInt(phienBanHDH);
            thoiGianBaoHanhh = Integer.parseInt(thoiGianBaoHanh);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(form, "Vui lòng nhập đúng định dạng số cho các trường số!");
            return;
        }

        // Tạo đối tượng sản phẩm
        SanPham sp = new SanPham();
        sp.setId(productId);
        sp.setTenSanPham(tenSp);
        sp.setMaxuatxu(xuatXu.getId());
        sp.setMathuonghieu(th.getMathuonghieu());
        sp.setMahedieuhanh(hdh.getMaHeDieuHanh());
        sp.setKhuVucKho(khuVucKho);
        sp.setChip(chip);
        sp.setDungLuongPin(dungLuongPinValue);
        sp.setKichThuocMan(kichThuocManValue);
        sp.setCamSau(camSau);
        sp.setCamTruoc(camTruoc);
        sp.setPhienBanHDH(phienBanheDH);
        sp.setThoiGianBaoHanh(thoiGianBaoHanhh);
        sp.setImage_path(imagePath);

        try {
            view.setProcessingRequest(true);

            // Gửi request
            EditSanPhamRequest request = new EditSanPhamRequest(sp, productId);
            System.out.println("Đang gửi yêu cầu sửa: " + request.getProductID());
            sm.send(request);

            sm.setProductListListener(list -> {
                for (SanPham s : list) {
                    System.out.println(s);
                }

                updateTable(list);
//                JOptionPane.showMessageDialog(form, "Sửa thành công!");
                form.dispose();
            });

        } catch (SocketException se) {
            JOptionPane.showMessageDialog(form, "Mất kết nối đến server. Đang thử kết nối lại...");
            view.reconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Lỗi khi sửa sản phẩm: " + ex.getMessage());
        } finally {
            view.setProcessingRequest(false);
        }

    }


    private void editProductCauHinhForm(EditProductCauHinhForm form) {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(form, "Vui lòng chọn sản phẩm để sửa!");
            return;
        }

        try {
            int productId = (int) view.getTable().getValueAt(selectedRow, 0);

            ram ram = (ram) form.getTxtRAM().getSelectedItem();
            rom rom = (rom) form.getTxtROM().getSelectedItem();
            mausac mausac = (mausac) form.getTxtColor().getSelectedItem();

            if (ram == null || rom == null || mausac == null) {
                JOptionPane.showMessageDialog(form, "Vui lòng chọn đầy đủ RAM, ROM và màu sắc!");
                return;
            }

            String giaNhapStr = form.getTxtImportPrice().getText().trim();
            String giaXuatStr = form.getTxtExportPrice().getText().trim();

            double giaNhap, giaXuat;
            try {
                giaNhap = Double.parseDouble(giaNhapStr);
                giaXuat = Double.parseDouble(giaXuatStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập đúng định dạng số cho giá nhập và giá xuất!");
                return;
            }

            // Gửi request
            cauHinhSanPham sp = new cauHinhSanPham();
            sp.setMamausac(mausac.getMamau());
            sp.setGianhap(giaNhap);
            sp.setGiaxuat(giaXuat);
            sp.setMaram(ram.getMadlram());
            sp.setMarom(rom.getMadlrom());

            view.setProcessingRequest(true);
            EditCauhinhRequest request = new EditCauhinhRequest(productId, sp);
            sm.send(request);

            sm.setProductListListener(list -> {
                System.out.println("Danh sách sau khi sửa cấu hình:");
                for (SanPham p : list) {
                    System.out.println(p);
                }
                updateTable(list);
                JOptionPane.showMessageDialog(form, "Sửa cấu hình thành công!");
                form.dispose();
            });

        } catch (SocketException se) {
            JOptionPane.showMessageDialog(form, "Mất kết nối đến server. Đang thử kết nối lại...");
            view.reconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Lỗi khi gửi request sửa cấu hình: " + ex.getMessage());
        } finally {
            view.setProcessingRequest(false);
        }
    }

    private void openSearch() {
        String keyword = view.getTopPanel().getTxtSearch().getText().trim().toLowerCase();
        String type = view.getTopPanel().getCbxChoose().getSelectedItem().toString();

        if (keyword.isEmpty()) {
//            view.updateTable(SanPhamDAO.getInstance().selectAll()); // <- Đúng đối tượng
            return;
        }

        ArrayList<SanPham> filtered = search(keyword, type);
//        view.updateTable(filtered);
    }

    public ArrayList<SanPham> search(String txt, String type) {
        ArrayList<SanPham> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (SanPham i : listSp) {
                    if (Integer.toString(i.getId()).contains(txt) || i.getTenSanPham().contains(txt) ||String.valueOf(i.getKichThuocMan()).contains(txt)||String.valueOf(i.getDungLuongPin()).contains(txt)||String.valueOf(i.getChip()).contains(txt)
                            || i.getThuongHieu().contains(txt) || i.getXuatXu().contains(txt) || i.getHeDieuHanh().contains(txt) || i.getKhuVucKho().contains(txt)) {
                        result.add(i);
                    }
                }
            }

            case "Tên sản phẩm" -> {
                for (SanPham sp : listSp) {
                    if (sp.getTenSanPham().toLowerCase().contains(txt)) {
                        result.add(sp);
                    }
                }
            }

            case "Thương hiệu" -> {
                for (SanPham sp : listSp) {
                    if (sp.getThuongHieu().toLowerCase().contains(txt)) {
                        result.add(sp);
                    }
                }
            }

            case "Khu vực kho" -> {
                for (SanPham sp : listSp) {
                    if (sp.getKhuVucKho().toLowerCase().contains(txt)) {
                        result.add(sp);
                    }
                }
            }
        }
        return result;
    }
    private void updateTable(List<?> list) {
        if (list.isEmpty()) return;
        if (!(list.get(0) instanceof SanPham)) return;

        List<SanPham> sanPhamList = (List<SanPham>) list;

        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel(); // SỬA Ở ĐÂY
        model.setRowCount(0); // Clear table

        for (SanPham sp : sanPhamList) {
            model.addRow(new Object[]{
                    sp.getId(),
                    sp.getTenSanPham(),
                    sp.getSoLuong(),
                    sp.getThuongHieu(),
                    sp.getHeDieuHanh(),
                    sp.getKichThuocMan(),
                    sp.getChip(),
                    sp.getDungLuongPin(),
                    sp.getXuatXu(),
                    sp.getKhuVucKho()
            });
        }
    }
    private void reloadTable() {
        view.getTopPanel().getTxtSearch().setText("");

        ArrayList<SanPham> sanPham = SanPhamDAO.getInstance().selectAll();

    }

}