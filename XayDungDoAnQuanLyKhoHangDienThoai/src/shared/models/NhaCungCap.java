package shared.models;

import java.io.Serializable;
import java.util.Objects;

public class NhaCungCap implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mancc;
    private String tenncc;
    private String diachi;
    private String email;
    private String sdt;

    public NhaCungCap() {
    }

    public NhaCungCap(int mancc, String tenncc, String diachi, String email, String sdt) {
        this.mancc = mancc;
        this.tenncc = tenncc;
        this.diachi = diachi;
        this.email = email;
        this.sdt = sdt;
    }

    public int getMancc() {
        return mancc;
    }

    public void setMancc(int mancc) {
        this.mancc = mancc;
    }

    public String getTenncc() {
        return tenncc;
    }

    public void setTenncc(String tenncc) {
        this.tenncc = tenncc;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhaCungCap other = (NhaCungCap) obj;
        return mancc == other.mancc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mancc);
    }

    @Override
    public String toString() {
        return tenncc; // Hiển thị tên nhà cung cấp trong comboBox
    }
}
