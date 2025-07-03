package shared.models;


public class ChiTietPhieuNhapDTO {
    private int maSP;
    private String tenSP;
    private String ram;
    private String rom;
    private String mausac;
    private double donGia;
    private int soLuong;

    public ChiTietPhieuNhapDTO() {
    }

    public ChiTietPhieuNhapDTO(int maSP, String tenSP, String ram, String rom, String mausac, double donGia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.ram = ram;
        this.rom = rom;
        this.mausac = mausac;
        this.donGia = donGia;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
