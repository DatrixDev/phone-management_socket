package shared.models;

import java.util.Date;

public class TinNhan {
    private int id;
    private String nguoiGui;
    private String nguoiNhan;
    private boolean laNhom;
    private String noiDung;
    private Date thoiGianGui;

    public TinNhan(int id, String nguoiGui, String nguoiNhan, boolean laNhom, String noiDung, Date thoiGianGui) {
        this.id = id;
        this.nguoiGui = nguoiGui;
        this.nguoiNhan = nguoiNhan;
        this.laNhom = laNhom;
        this.noiDung = noiDung;
        this.thoiGianGui = thoiGianGui;
    }

    public TinNhan() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(String nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public boolean isLaNhom() {
        return laNhom;
    }

    public void setLaNhom(boolean laNhom) {
        this.laNhom = laNhom;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getThoiGianGui() {
        return thoiGianGui;
    }

    public void setThoiGianGui(Date thoiGianGui) {
        this.thoiGianGui = thoiGianGui;
    }
    // Getters & Setters
    // ...
}
