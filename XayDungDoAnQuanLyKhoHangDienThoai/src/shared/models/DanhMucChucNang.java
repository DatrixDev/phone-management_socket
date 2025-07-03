package shared.models;

import java.io.Serializable;
import java.util.Objects;

public class DanhMucChucNang  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String machucnang;
    private String tenchucnang;

    public DanhMucChucNang() {
    }

    public DanhMucChucNang(String machucnang, String tenchucnang) {
        this.machucnang = machucnang;
        this.tenchucnang = tenchucnang;
    }

    public String getMachucnang() {
        return machucnang;
    }

    public void setMachucnang(String machucnang) {
        this.machucnang = machucnang;
    }

    public String getTenchucnang() {
        return tenchucnang;
    }

    public void setTenchucnang(String tenchucnang) {
        this.tenchucnang = tenchucnang;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.machucnang);
        hash = 37 * hash + Objects.hashCode(this.tenchucnang);
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
        final DanhMucChucNang other = (DanhMucChucNang) obj;
        if (!Objects.equals(this.machucnang, other.machucnang)) {
            return false;
        }
        return Objects.equals(this.tenchucnang, other.tenchucnang);
    }

    @Override
    public String toString() {
        return "DanhMucChucNang{" + "machucnang=" + machucnang + ", tenchucnang=" + tenchucnang + '}';
    }
}
