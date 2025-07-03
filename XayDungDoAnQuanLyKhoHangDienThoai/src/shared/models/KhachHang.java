package shared.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class KhachHang implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String tenKhachHang;
	private String soDienThoai;
	private String diaChi;
	private Date ngayThamGia;
	
	public KhachHang() {}

	public KhachHang(int id, String tenKhachHang, String soDienThoai, String diaChi) {
		this.id = id;
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
	}

	public KhachHang(int id, String tenKhachHang, String soDienThoai, String diaChi, Date ngayThamGia) {
		this.id = id;
		this.tenKhachHang = tenKhachHang;
		this.soDienThoai = soDienThoai;
		this.diaChi = diaChi;
		this.ngayThamGia = ngayThamGia;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public Date getNgayThamGia() {
		return ngayThamGia;
	}

	public void setNgayThamGia(Date ngayThamGia) {
		this.ngayThamGia = ngayThamGia;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + Objects.hashCode(this.id);
		hash = 79 * hash + Objects.hashCode(this.tenKhachHang);
		hash = 79 * hash + Objects.hashCode(this.soDienThoai);
		hash = 79 * hash + Objects.hashCode(this.diaChi);
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
		final KhachHang other = (KhachHang) obj;
		return true;
	}

	@Override
	public String toString() {
		return "KhachHang{" +
				"id=" + id +
				", tenKhachHang='" + tenKhachHang + '\'' +
				", soDienThoai='" + soDienThoai + '\'' +
				", diaChi='" + diaChi + '\'' +
				'}';
	}
}
