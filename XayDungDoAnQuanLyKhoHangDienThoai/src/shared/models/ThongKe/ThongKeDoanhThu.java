package shared.models.ThongKe;

import java.util.Objects;

public class ThongKeDoanhThu {
    int thoigian; // nam, thang, ngay
    Long von;
    Long doanhthu;
    Long loinhuan;

    public ThongKeDoanhThu() {
    }

    public ThongKeDoanhThu(int thoigian, Long von, Long doanhthu, Long loinhuan) {
        this.thoigian = thoigian;
        this.von = von;
        this.doanhthu = doanhthu;
        this.loinhuan = loinhuan;
    }

    public int getThoigian() {
        return thoigian;
    }

    public void setThoigian(int thoigian) {
        this.thoigian = thoigian;
    }

    public Long getVon() {
        return von;
    }

    public void setVon(Long von) {
        this.von = von;
    }

    public Long getDoanhthu() {
        return doanhthu;
    }

    public void setDoanhthu(Long doanhthu) {
        this.doanhthu = doanhthu;
    }

    public Long getLoinhuan() {
        return loinhuan;
    }

    public void setLoinhuan(Long loinhuan) {
        this.loinhuan = loinhuan;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.thoigian;
        hash = 97 * hash + Objects.hashCode(this.von);
        hash = 97 * hash + Objects.hashCode(this.doanhthu);
        hash = 97 * hash + Objects.hashCode(this.loinhuan);
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
        final ThongKeDoanhThu other = (ThongKeDoanhThu) obj;
        if (this.thoigian != other.thoigian) {
            return false;
        }
        if (!Objects.equals(this.von, other.von)) {
            return false;
        }
        if (!Objects.equals(this.doanhthu, other.doanhthu)) {
            return false;
        }
        return Objects.equals(this.loinhuan, other.loinhuan);
    }

    @Override
    public String toString() {
        return "ThongKeDoanhThu{" + "thoigian=" + thoigian + ", von=" + von + ", doanhthu=" + doanhthu + ", loinhuan=" + loinhuan + '}';
    }
}
