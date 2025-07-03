package shared.models;


import java.sql.Timestamp;

public class phieuXuat {
    private int maphieuXuat;
    private int manguoitao;
    private Timestamp thoigiantao;
    private long tongTien;
    private int trangthai;
    private int maKhachHang;
    private String tenKhachHang;
    private String tennhanvien;

    public phieuXuat(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }


    public phieuXuat(String tenKhachHang, String tennhanvien) {
        this.tenKhachHang = tenKhachHang;
        this.tennhanvien = tennhanvien;
    }


    public phieuXuat() {
    }

    public phieuXuat(int maphieuXuat, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai, int maKhachHang, String tenKhachHang, String tennhanvien) {
        this.maphieuXuat = maphieuXuat;
        this.manguoitao = manguoitao;
        this.thoigiantao = thoigiantao;
        this.tongTien = tongTien;
        this.trangthai = trangthai;
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.tennhanvien = tennhanvien;
    }

    public int getMaphieuXuat() {
        return maphieuXuat;
    }

    public void setMaphieuXuat(int maphieuXuat) {
        this.maphieuXuat = maphieuXuat;
    }

    public int getManguoitao() {
        return manguoitao;
    }

    public void setManguoitao(int manguoitao) {
        this.manguoitao = manguoitao;
    }

    public Timestamp getThoigiantao() {
        return thoigiantao;
    }

    public void setThoigiantao(Timestamp thoigiantao) {
        this.thoigiantao = thoigiantao;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTennhanvien() {
        return tennhanvien;
    }

    public void setTennhanvien(String tennhanvien) {
        this.tennhanvien = tennhanvien;
    }
}
