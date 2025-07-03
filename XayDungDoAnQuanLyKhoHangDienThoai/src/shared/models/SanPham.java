package shared.models;

import java.io.Serializable;

public class SanPham implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String image_path;
	private String tenSanPham;
	private String xuatXu;
	private int maxuatxu;
	private String chip;
	private int dungLuongPin;
	private double kichThuocMan;
	private String heDieuHanh;
	private int mahedieuhanh;
	private int phienBanHDH;
	private String camSau;
	private String camTruoc;
	private int thoiGianBaoHanh;
	private String thuongHieu;
	private int mathuonghieu;
	private String khuVucKho;
	private int soLuong;
	private cauHinhSanPham cauHinh;

	// Constructor rỗng
	public SanPham() {
	}

	// Constructor đầy đủ với đúng kiểu dữ liệu
	public SanPham(int id,int mahedieuhanh,int maxuatxu,int mathuonghieu, int soLuong, String image_path, String tenSanPham, String xuatXu, String chip,
				   int dungLuongPin, double kichThuocMan, String heDieuHanh, int phienBanHDH,
				   String camSau, String camTruoc, int thoiGianBaoHanh,
				   String thuongHieu, String khuVucKho , cauHinhSanPham cauHinh) {
		this.id = id;
		this.soLuong = soLuong;
		this.image_path = image_path;
		this.tenSanPham = tenSanPham;
		this.xuatXu = xuatXu;
		this.chip = chip;
		this.mahedieuhanh = mahedieuhanh;
		this.mathuonghieu = mathuonghieu;
		this.maxuatxu = maxuatxu;
		this.dungLuongPin = dungLuongPin;
		this.kichThuocMan = kichThuocMan;
		this.heDieuHanh = heDieuHanh;
		this.phienBanHDH = phienBanHDH;
		this.camSau = camSau;
		this.camTruoc = camTruoc;
		this.thoiGianBaoHanh = thoiGianBaoHanh;
		this.thuongHieu = thuongHieu;
		this.khuVucKho = khuVucKho;
		this.cauHinh = cauHinh ;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public int getMaxuatxu() {
		return maxuatxu;
	}

	public void setMaxuatxu(int maxuatxu) {
		this.maxuatxu = maxuatxu;
	}

	public int getMahedieuhanh() {
		return mahedieuhanh;
	}

	public void setMahedieuhanh(int mahedieuhanh) {
		this.mahedieuhanh = mahedieuhanh;
	}

	public int getMathuonghieu() {
		return mathuonghieu;
	}

	public void setMathuonghieu(int mathuonghieu) {
		this.mathuonghieu = mathuonghieu;
	}

	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public String getXuatXu() {
		return xuatXu;
	}

	public void setXuatXu(String xuatXu) {
		this.xuatXu = xuatXu;
	}

	public String getChip() {
		return chip;
	}

	public void setChip(String chip) {
		this.chip = chip;
	}

	public int getDungLuongPin() {
		return dungLuongPin;
	}

	public void setDungLuongPin(int dungLuongPin) {
		this.dungLuongPin = dungLuongPin;
	}

	public double getKichThuocMan() {
		return kichThuocMan;
	}

	public void setKichThuocMan(double kichThuocMan) {
		this.kichThuocMan = kichThuocMan;
	}

	public String getHeDieuHanh() {
		return heDieuHanh;
	}

	public void setHeDieuHanh(String heDieuHanh) {
		this.heDieuHanh = heDieuHanh;
	}

	public int getPhienBanHDH() {
		return phienBanHDH;
	}

	public void setPhienBanHDH(int phienBanHDH) {
		this.phienBanHDH = phienBanHDH;
	}

	public String getCamSau() {
		return camSau;
	}

	public void setCamSau(String camSau) {
		this.camSau = camSau;
	}

	public String getCamTruoc() {
		return camTruoc;
	}

	public void setCamTruoc(String camTruoc) {
		this.camTruoc = camTruoc;
	}

	public int getThoiGianBaoHanh() {
		return thoiGianBaoHanh;
	}

	public void setThoiGianBaoHanh(int thoiGianBaoHanh) {
		this.thoiGianBaoHanh = thoiGianBaoHanh;
	}

	public String getThuongHieu() {
		return thuongHieu;
	}

	public void setThuongHieu(String thuongHieu) {
		this.thuongHieu = thuongHieu;
	}

	public String getKhuVucKho() {
		return khuVucKho;
	}

	public void setKhuVucKho(String khuVucKho) {
		this.khuVucKho = khuVucKho;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public cauHinhSanPham getCauHinhs() {
		return cauHinh;
	}

	public void setCauHinhs(cauHinhSanPham cauHinhs) {
		this.cauHinh = cauHinhs;
	}

	@Override
	public String toString() {
		return "SanPham{" +
				"id=" + id +
				", image_path='" + image_path + '\'' +
				", tenSanPham='" + tenSanPham + '\'' +
				", xuatXu='" + xuatXu + '\'' +
				", maxuatxu=" + maxuatxu +
				", chip='" + chip + '\'' +
				", dungLuongPin=" + dungLuongPin +
				", kichThuocMan=" + kichThuocMan +
				", heDieuHanh='" + heDieuHanh + '\'' +
				", mahedieuhanh=" + mahedieuhanh +
				", phienBanHDH=" + phienBanHDH +
				", camSau='" + camSau + '\'' +
				", camTruoc='" + camTruoc + '\'' +
				", thoiGianBaoHanh=" + thoiGianBaoHanh +
				", thuongHieu='" + thuongHieu + '\'' +
				", mathuonghieu=" + mathuonghieu +
				", khuVucKho='" + khuVucKho + '\'' +
				", soLuong=" + soLuong +
				", cauHinh=" + cauHinh +
				'}';
	}
	// Thêm cấu hình
}

