package shared.models;

import java.io.Serializable;

public class cauHinhSanPham implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maphienbansp;
    private int masp;
    private String ram;
    private int maram;
    private int marom;
    private String rom;
    private String mausac;
    private int mamausac;
    private Double gianhap;
    private Double giaxuat;
    public cauHinhSanPham(){}

    public cauHinhSanPham(int maram,int marom,int mamausac,int maphienbansp, int masp, String ram, String rom, String mausac, double gianhap, double giaxuat){


        this.maphienbansp = maphienbansp;
        this.masp = masp;
        this.ram = ram;
        this.rom = rom;
        this.mausac = mausac;
        this.gianhap = gianhap;
        this.giaxuat = giaxuat;
        this.maram = maram;
        this.marom = marom;
        this.mamausac = mamausac;
    }

    public int getMaphienbansp() {
        return maphienbansp;
    }

    public void setMaphienbansp(int maphienbansp) {
        this.maphienbansp = maphienbansp;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getMausac() {
        return mausac;
    }

    public void setMausac(String mausac) {
        this.mausac = mausac;
    }

    public Double getGianhap() {
        return gianhap;
    }

    public void setGianhap(Double gianhap) {
        this.gianhap = gianhap;
    }

    public Double getGiaxuat() {
        return giaxuat;
    }

    public void setMaram(int maram) {
        this.maram = maram;
    }

    public void setMarom(int marom) {
        this.marom = marom;
    }

    public void setMamausac(int mamausac) {
        this.mamausac = mamausac;
    }

    public int getMaram() {
        return maram;
    }

    public int getMarom() {
        return marom;
    }

    public int getMamausac() {
        return mamausac;
    }

    public void setGiaxuat(Double giaxuat) {
        this.giaxuat = giaxuat;
    }


}
