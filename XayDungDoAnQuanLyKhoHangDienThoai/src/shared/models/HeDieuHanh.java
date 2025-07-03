package shared.models;


import java.io.Serializable;

public class HeDieuHanh implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maHeDieuHanh;
    private String tenHeDieuHanh;

    public HeDieuHanh(int maHeDieuHanh, String tenHeDieuHanh) {
        this.maHeDieuHanh = maHeDieuHanh;
        this.tenHeDieuHanh = tenHeDieuHanh;
    }

    public HeDieuHanh() {

    }

    public int getMaHeDieuHanh() {
        return maHeDieuHanh;
    }

    public void setMaHeDieuHanh(int maHeDieuHanh) {
        this.maHeDieuHanh = maHeDieuHanh;
    }

    public String getTenHeDieuHanh() {
        return tenHeDieuHanh;
    }

    public void setTenHeDieuHanh(String tenHeDieuHanh) {
        this.tenHeDieuHanh = tenHeDieuHanh;
    }

    @Override
    public String toString() {
        return tenHeDieuHanh; // Hiển thị tên trong ComboBox
    }
}
