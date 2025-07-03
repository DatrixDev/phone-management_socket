package server.DAO;


import dataBase.Database;
import shared.models.ChiTietPhieuXuatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class chitietPhieuXuatDAO {
    public static boolean insertCTPhieuXuat(int maPhieuXuat, int maPhienBanSP, int soLuong, double donGia) {
        String sql = "INSERT INTO ctphieuxuat(maphieuxuat, maphienbansp, soluong, dongia) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE soluong = ?, dongia = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhieuXuat);
            ps.setInt(2, maPhienBanSP);
            ps.setInt(3, soLuong);
            ps.setDouble(4, donGia);

            ps.setInt(5, soLuong);
            ps.setDouble(6, donGia);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isChiTietTonTai(int maphieu, int maphienban) {
        String sql = "SELECT COUNT(*) FROM ctphieuxuat WHERE maphieuxuat = ? AND maphienbansp = ?";
        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maphieu);
            ps.setInt(2, maphienban);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getMaPhienBanSPTheoMaSP(int maSanPham) {
        String sql = "SELECT maphienbansp FROM phienbansanpham WHERE masp = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maSanPham);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("maphienbansp");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static List<ChiTietPhieuXuatDTO> thongTinPhieuXuat(int maPhieuXuat) {
        List<ChiTietPhieuXuatDTO> list = new ArrayList<>();
        String sql = "SELECT sp.masp, sp.tensp, pb.ram, pb.rom, pb.mausac, ct.dongia, ct.soluong " +
                "FROM ctphieuxuat ct " +
                "JOIN phienbansanpham pb ON ct.maphienbansp = pb.maphienbansp " +
                "JOIN sanpham sp ON pb.masp = sp.masp " +
                "WHERE ct.maphieuxuat = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhieuXuat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuXuatDTO ct = new ChiTietPhieuXuatDTO();
                    ct.setMaSP(rs.getInt("masp"));
                    ct.setTenSP(rs.getString("tensp"));
                    ct.setRam(rs.getString("ram"));
                    ct.setRom(rs.getString("rom"));
                    ct.setMausac(rs.getString("mausac"));
                    ct.setDonGia(rs.getDouble("dongia"));
                    ct.setSoLuong(rs.getInt("soluong"));
                    list.add(ct);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
