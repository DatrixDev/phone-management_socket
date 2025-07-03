package client.BUS;

import server.DAO.KhachHangDAO;
import shared.models.KhachHang;

import java.util.ArrayList;

public class KhachHangBUS {

    private final KhachHangDAO khDAO = new KhachHangDAO();
    public ArrayList<KhachHang> listKhachHang = new ArrayList<>();

    public KhachHangBUS() {
        listKhachHang = khDAO.selectAll();
    }

    public ArrayList<KhachHang> getAll() {
        return this.listKhachHang;
    }

    public KhachHang getByIndex(int index) {
        return this.listKhachHang.get(index);
    }

    public int getIndexByMaDV(int makhachhang) {
        int i = 0;
        int vitri = -1;
        while (i < this.listKhachHang.size() && vitri == -1) {
            if (listKhachHang.get(i).getId() == makhachhang) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(KhachHang kh) {
        boolean check = khDAO.insert(kh) != 0;
        if (check) {
            this.listKhachHang.add(kh);
        }
        return check;
    }

    public Boolean delete(KhachHang kh) {
        boolean check = khDAO.delete(Integer.toString(kh.getId())) != 0;
        if (check) {
            this.listKhachHang.remove(getIndexByMaDV(kh.getId()));
        }
        return check;
    }

    public Boolean update(KhachHang kh) {
        boolean check = khDAO.update(kh) != 0;
        if (check) {
            this.listKhachHang.set(getIndexByMaDV(kh.getId()), kh);
        }
        return check;
    }

    public ArrayList<KhachHang> search(String keyword, String type) {
        ArrayList<KhachHang> result = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (KhachHang kh : listKhachHang) {
            switch (type) {
                case "Tất cả" -> {
                    if (String.valueOf(kh.getId()).toLowerCase().contains(keyword) ||
                            kh.getTenKhachHang().toLowerCase().contains(keyword) ||
                            kh.getDiaChi().toLowerCase().contains(keyword) ||
                            kh.getSoDienThoai().toLowerCase().contains(keyword)) {
                        result.add(kh);
                    }
                }
                case "Mã khách hàng" -> {
                    if (String.valueOf(kh.getId()).toLowerCase().contains(keyword)) {
                        result.add(kh);
                    }
                }
                case "Tên khách hàng" -> {
                    if (kh.getTenKhachHang().toLowerCase().contains(keyword)) {
                        result.add(kh);
                    }
                }
                case "Địa chỉ" -> {
                    if (kh.getDiaChi().toLowerCase().contains(keyword)) {
                        result.add(kh);
                    }
                }
                case "Số điện thoại" -> {
                    if (kh.getSoDienThoai().toLowerCase().contains(keyword)) {
                        result.add(kh);
                    }
                }
            }
        }
        return result;
    }


    public void reloadFromDB() {
        this.listKhachHang = khDAO.selectAll();
    }


    public String getTenKhachHang(int makh) {
        String name = "";
        for (KhachHang khachHangDTO : listKhachHang) {
            if (khachHangDTO.getId() == makh) {
                name = khachHangDTO.getTenKhachHang();
            }
        }
        return name;
    }

    public String[] getArrTenKhachHang() {
        int size = listKhachHang.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listKhachHang.get(i).getTenKhachHang();
        }
        return result;
    }

    public KhachHang selectKh(int makh) {
        return khDAO.selectById(makh + "");
    }

}
