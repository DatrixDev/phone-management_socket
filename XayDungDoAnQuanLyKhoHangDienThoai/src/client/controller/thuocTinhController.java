package client.controller;

import client.view.form.thuocTinh.*;
import client.view.views.thuocTinh;
import server.DAO.*;
import shared.models.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class thuocTinhController {
    private thuocTinh view;

    public thuocTinhController(thuocTinh view) {
        this.view = view;
        initController();

    }

    public void initController() {
        view.getBtnThuongHieu().addActionListener(e -> {
            openThuongHieu(new thuongHieu());
        });
        view.getBtnxuatxu().addActionListener(e -> {
            openXuatXu(new xuatXu());
        });
        view.getBtnHeDieuHanh().addActionListener(e -> {
            openHeDieuHanh(new heDieuHanh());
        });
        view.getBtnRom().addActionListener(e -> {
            openROM(new romView() );
        });
        view.getBtnRam().addActionListener(e -> {
            openRam(new RamView());
        });
        view.getBtnMausac().addActionListener(e -> {
            openMauSac(new mausacView());
        });
    }

    public void openThuongHieu(thuongHieu formThuonghieu) {
        JTable table = formThuonghieu.getTable();
        thuongHieuDAO dao = new thuongHieuDAO();

        // CLICK TABLE ĐỂ SET TEXT
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formThuonghieu.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        // THÊM
        formThuonghieu.getBtnThem().addActionListener(e -> {
            String name = formThuonghieu.getTxtROM().getText().trim();
            if (!name.isEmpty()) {
                if (dao.isTenThuongHieuTonTai(name)) {
                    JOptionPane.showMessageDialog(null, "Tên thương hiệu đã tồn tại!");
                    return;
                }
                thuonghieu th = new thuonghieu();
                th.setTenthuonghieu(name);
                if (dao.themThuongHieu(th)) {
                    formThuonghieu.loadThuongHieuToTable();
                    formThuonghieu.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tên thương hiệu không được để trống!");
            }
        });

        // SỬA
        formThuonghieu.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                String newName = formThuonghieu.getTxtROM().getText().trim();
                if (!newName.isEmpty()) {
                    String currentName = table.getValueAt(row, 1).toString();
                    if (!newName.equalsIgnoreCase(currentName) && dao.isTenThuongHieuTonTai(newName)) {
                        JOptionPane.showMessageDialog(null, "Tên thương hiệu đã tồn tại!");
                        return;
                    }
                    thuonghieu th = new thuonghieu();
                    th.setMathuonghieu(id);
                    th.setTenthuonghieu(newName);
                    if (dao.suaThuongHieu(th)) {
                        formThuonghieu.loadThuongHieuToTable();
                        formThuonghieu.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tên mới không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });


        // XÓA
        formThuonghieu.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaThuongHieu(id)) {
                    formThuonghieu.loadThuongHieuToTable();
                    formThuonghieu.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(formThuonghieu, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(formThuonghieu, "Chọn dòng cần xóa!");
            }
        });

        formThuonghieu.setVisible(true);
    }



    public void openXuatXu(xuatXu formXuatXu) {
        JTable table = formXuatXu.getTable();
        xuatXuDAO dao = new xuatXuDAO();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formXuatXu.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        formXuatXu.getBtnThem().addActionListener(e -> {
            String name = formXuatXu.getTxtROM().getText().trim();
            if (!name.isEmpty()) {
                if (dao.isTenXuatXuTonTai(name)) {
                    JOptionPane.showMessageDialog(null, "Tên xuất xứ đã tồn tại!");
                    return;
                }
                XuatXu xx = new XuatXu();
                xx.setTen(name);
                if (dao.themXuatXu(xx)) {
                    formXuatXu.loadXuatXu();
                    formXuatXu.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tên xuất xứ không được để trống!");
            }
        });

        // Sửa
        formXuatXu.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                String newName = formXuatXu.getTxtROM().getText().trim();
                String currentName = table.getValueAt(row, 1).toString();
                if (!newName.isEmpty()) {
                    if (!newName.equalsIgnoreCase(currentName) && dao.isTenXuatXuTonTai(newName)) {
                        JOptionPane.showMessageDialog(null, "Tên xuất xứ đã tồn tại!");
                        return;
                    }
                    XuatXu xx = new XuatXu();
                    xx.setId(id);
                    xx.setTen(newName);
                    if (dao.suaXuatXu(xx)) {
                        formXuatXu.loadXuatXu();
                        formXuatXu.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tên xuất xứ không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });

        // Xóa
        formXuatXu.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaXuatXu(id)) {
                    formXuatXu.loadXuatXu();
                    formXuatXu.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần xóa!");
            }
        });

        formXuatXu.setVisible(true);
    }

    public void openROM(romView formRom) {
        JTable table = formRom.getTable();
        RomDAO dao = new RomDAO();

        // Gán dữ liệu khi click vào bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formRom.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        // Thêm
        formRom.getBtnThem().addActionListener(e -> {
            String text = formRom.getTxtROM().getText().trim();
            if (!text.isEmpty()) {
                try {
                    int kichthuoc = Integer.parseInt(text);
                    if (dao.isKichThuocRomTonTai(kichthuoc)) {
                        JOptionPane.showMessageDialog(null, "Kích thước ROM đã tồn tại!");
                        return;
                    }
                    rom r = new rom();
                    r.setKicthuocrom(kichthuoc);
                    if (dao.themRom(r)) {
                        formRom.loadROM();
                        formRom.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số nguyên hợp lệ!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Không được để trống!");
            }
        });

        // Sửa
        formRom.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String text = formRom.getTxtROM().getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int kichthuoc = Integer.parseInt(text);
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        int currentValue = Integer.parseInt(table.getValueAt(row, 1).toString());

                        if (kichthuoc != currentValue && dao.isKichThuocRomTonTai(kichthuoc)) {
                            JOptionPane.showMessageDialog(null, "Kích thước ROM đã tồn tại!");
                            return;
                        }

                        rom r = new rom();
                        r.setMadlrom(id);
                        r.setKicthuocrom(kichthuoc);
                        if (dao.suaRom(r)) {
                            formRom.loadROM();
                            formRom.getTxtROM().setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Nhập số nguyên hợp lệ!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });

        // Xóa
        formRom.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaRom(id)) {
                    formRom.loadROM();
                    formRom.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần xóa!");
            }
        });

        formRom.setVisible(true);
    }

    public void openRam(RamView formRam) {
        JTable table = formRam.getTable();
        RamDAO dao = new RamDAO();

        // Gán dữ liệu từ table lên input khi click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formRam.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        // Thêm
        formRam.getBtnThem().addActionListener(e -> {
            String text = formRam.getTxtROM().getText().trim();
            if (!text.isEmpty()) {
                try {
                    int kichthuoc = Integer.parseInt(text);
                    if (dao.isKichThuocRamTonTai(kichthuoc)) {
                        JOptionPane.showMessageDialog(null, "Dung lượng RAM đã tồn tại!");
                        return;
                    }
                    ram r = new ram();
                    r.setKichthuocram(kichthuoc);
                    if (dao.themRam(r)) {
                        formRam.loadRAM();
                        formRam.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số nguyên hợp lệ!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Không được để trống!");
            }
        });

        // Sửa
        formRam.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String text = formRam.getTxtROM().getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int kichthuoc = Integer.parseInt(text);
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        int currentValue = Integer.parseInt(table.getValueAt(row, 1).toString());

                        if (kichthuoc != currentValue && dao.isKichThuocRamTonTai(kichthuoc)) {
                            JOptionPane.showMessageDialog(null, "Dung lượng RAM đã tồn tại!");
                            return;
                        }

                        ram r = new ram();
                        r.setMadlram(id);
                        r.setKichthuocram(kichthuoc);
                        if (dao.suaRam(r)) {
                            formRam.loadRAM();
                            formRam.getTxtROM().setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Nhập số nguyên hợp lệ!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });

        // Xóa
        formRam.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaRam(id)) {
                    formRam.loadRAM();
                    formRam.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần xóa!");
            }
        });

        formRam.setVisible(true);
    }

    public void openMauSac(mausacView formMausac) {
        JTable table = formMausac.getTable();
        MauSacDAO dao = new MauSacDAO();

        // Click table => đổ dữ liệu lên input
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formMausac.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        // ===== THÊM =====
        formMausac.getBtnThem().addActionListener(e -> {
            String name = formMausac.getTxtROM().getText().trim();
            if (!name.isEmpty()) {
                if (dao.isTenMauTonTai(name)) {
                    JOptionPane.showMessageDialog(null, "Tên màu sắc đã tồn tại!");
                    return;
                }
                mausac ms = new mausac();
                ms.setTenmau(name);
                if (dao.themMauSac(ms)) {
                    formMausac.loadMauSac();
                    formMausac.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tên màu không được để trống!");
            }
        });

        // ===== SỬA =====
        formMausac.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String newName = formMausac.getTxtROM().getText().trim();
                if (!newName.isEmpty()) {
                    String currentName = table.getValueAt(row, 1).toString();
                    if (!newName.equalsIgnoreCase(currentName) && dao.isTenMauTonTai(newName)) {
                        JOptionPane.showMessageDialog(null, "Tên màu đã tồn tại!");
                        return;
                    }
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    mausac ms = new mausac();
                    ms.setMamau(id);
                    ms.setTenmau(newName);
                    if (dao.suaMauSac(ms)) {
                        formMausac.loadMauSac();
                        formMausac.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tên màu không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });

        // ===== XÓA =====
        formMausac.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaMauSac(id)) {
                    formMausac.loadMauSac();
                    formMausac.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần xóa!");
            }
        });

        formMausac.setVisible(true);
    }


    public void openHeDieuHanh(heDieuHanh formHedieuHanh) {
        JTable table = formHedieuHanh.getTable();
        heDieuHanhDAO dao = new heDieuHanhDAO();

        // Click table => load dữ liệu lên TextField
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    formHedieuHanh.getTxtROM().setText(table.getValueAt(row, 1).toString());
                }
            }
        });

        // ===== THÊM =====
        formHedieuHanh.getBtnThem().addActionListener(e -> {
            String name = formHedieuHanh.getTxtROM().getText().trim();
            if (!name.isEmpty()) {
                if (dao.isTenHeDieuHanhTonTai(name)) {
                    JOptionPane.showMessageDialog(null, "Tên hệ điều hành đã tồn tại!");
                    return;
                }
                HeDieuHanh hdh = new HeDieuHanh();
                hdh.setTenHeDieuHanh(name);
                if (dao.themHeDieuHanh(hdh)) {
                    formHedieuHanh.loadHDH();
                    formHedieuHanh.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tên không được để trống!");
            }
        });

        // ===== SỬA =====
        formHedieuHanh.getBtnSua().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String newName = formHedieuHanh.getTxtROM().getText().trim();
                if (!newName.isEmpty()) {
                    String currentName = table.getValueAt(row, 1).toString();
                    if (!newName.equalsIgnoreCase(currentName) && dao.isTenHeDieuHanhTonTai(newName)) {
                        JOptionPane.showMessageDialog(null, "Tên hệ điều hành đã tồn tại!");
                        return;
                    }
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    HeDieuHanh hdh = new HeDieuHanh();
                    hdh.setMaHeDieuHanh(id);
                    hdh.setTenHeDieuHanh(newName);
                    if (dao.suaHeDieuHanh(hdh)) {
                        formHedieuHanh.loadHDH();
                        formHedieuHanh.getTxtROM().setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tên không được để trống!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!");
            }
        });

        // ===== XÓA =====
        formHedieuHanh.getBtnXoa().addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                if (dao.xoaHeDieuHanh(id)) {
                    formHedieuHanh.loadHDH();
                    formHedieuHanh.getTxtROM().setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Chọn dòng cần xóa!");
            }
        });

        formHedieuHanh.setVisible(true);
    }

}