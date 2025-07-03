package shared.models;


public class ChiTietPhieuXuatDTO {
    private int maSP;
    private String tenSP;
    private String ram;
    private String rom;
    private String mausac;
    private double giaxuat;
    private int soLuong;

    public ChiTietPhieuXuatDTO() {
    }

    public ChiTietPhieuXuatDTO(int maSP, String tenSP, String ram, String rom, String mausac, double giaxuat, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.ram = ram;
        this.rom = rom;
        this.mausac = mausac;
        this.giaxuat = giaxuat;
        this.soLuong = soLuong;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
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

    public double getGiaxuat() {
        return giaxuat;
    }

    public void setDonGia(double donGia) {
        this.giaxuat = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
