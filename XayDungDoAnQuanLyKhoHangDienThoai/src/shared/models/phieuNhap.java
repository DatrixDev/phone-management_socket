package shared.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class phieuNhap implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maphieu;
    private int manguoitao;
    private Timestamp thoigiantao;
    private long tongTien;
    private int trangthai;
    private int manhacungcap;
    private String tennhacungcap;
    private String tennhanvien;


    public phieuNhap(int manhacungcap) {
        this.manhacungcap = manhacungcap;
    }

    public phieuNhap(String tennhacungcap, String tennhanvien) {
        this.tennhacungcap = tennhacungcap;
        this.tennhanvien = tennhanvien;
    }


    public phieuNhap() {
    }

    public phieuNhap(int maphieu, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai) {
        this.maphieu = maphieu;
        this.manguoitao = manguoitao;
        this.thoigiantao = thoigiantao;
        this.tongTien = tongTien;
        this.trangthai = trangthai;
    }

    public int getMaphieu() {
        return maphieu;
    }

    public void setMaphieu(int maphieu) {
        this.maphieu = maphieu;
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

    public int getManhacungcap() {
        return manhacungcap;
    }

    public void setManhacungcap(int manhacungcap) {
        this.manhacungcap = manhacungcap;
    }

    public String getTennhacungcap() {
        return tennhacungcap;
    }

    public String getTennhanvien() {
        return tennhanvien;
    }

    public void setTennhanvien(String tennhanvien) {
        this.tennhanvien = tennhanvien;
    }

    public void setTennhacungcap(String tennhacungcap) {
        this.tennhacungcap = tennhacungcap;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
}
