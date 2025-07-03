package shared.models.ThongKe;

import java.util.Objects;

public class ThongKeKhachHang {
    int makh;
    String tenkh;
    int soluongphieu;
    long tongtien;

    public ThongKeKhachHang() {
    }

    public ThongKeKhachHang(int makh, String tenkh, int soluongphieu, long tongtien) {
        this.makh = makh;
        this.tenkh = tenkh;
        this.soluongphieu = soluongphieu;
        this.tongtien = tongtien;
    }

    public int getMakh() {
        return makh;
    }

    public void setMakh(int makh) {
        this.makh = makh;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public int getSoluongphieu() {
        return soluongphieu;
    }

    public void setSoluongphieu(int soluongphieu) {
        this.soluongphieu = soluongphieu;
    }

    public long getTongtien() {
        return tongtien;
    }

    public void setTongtien(long tongtien) {
        this.tongtien = tongtien;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.makh;
        hash = 29 * hash + Objects.hashCode(this.tenkh);
        hash = 29 * hash + this.soluongphieu;
        hash = 29 * hash + (int) (this.tongtien ^ (this.tongtien >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThongKeKhachHang other = (ThongKeKhachHang) obj;
        return true;
    }

    @Override
    public String toString() {
        return "ThongKeKhachHangDTO{" + "makh=" + makh + ", tenkh=" + tenkh + ", soluongphieu=" + soluongphieu + ", tongtien=" + tongtien + '}';
    }


}
