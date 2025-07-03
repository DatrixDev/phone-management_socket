package server.DAO;

import dataBase.Database;
import shared.models.ChiTietPhieuNhapDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class chitietPhieuNhapDAO {
    public static boolean insertCTPhieuNhap(int maPhieuNhap, int maPhienBanSP, int soLuong, double donGia, String hinhThucNhap) {
        String sql = "INSERT INTO ctphieunhap(maphieunhap, maphienbansp, soluong, dongia, hinhthucnhap) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE soluong = ?, dongia = ?, hinhthucnhap = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhieuNhap);
            ps.setInt(2, maPhienBanSP);
            ps.setInt(3, soLuong);
            ps.setDouble(4, donGia);
            ps.setString(5, hinhThucNhap);

            ps.setInt(6, soLuong);
            ps.setDouble(7, donGia);
            ps.setString(8, hinhThucNhap);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isChiTietTonTai(int maphieu, int maphienban) {
        String sql = "SELECT COUNT(*) FROM ctphieunhap WHERE maphieunhap = ? AND maphienbansp = ?";
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

    public static List<ChiTietPhieuNhapDTO> thongTinPhieuNhap(int maPhieuNhap) {
        List<ChiTietPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT sp.masp, sp.tensp, pb.ram, pb.rom, pb.mausac, ct.dongia, ct.soluong " +
                "FROM ctphieunhap ct " +
                "JOIN phienbansanpham pb ON ct.maphienbansp = pb.maphienbansp " +
                "JOIN sanpham sp ON pb.masp = sp.masp " +
                "WHERE ct.maphieunhap = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO();
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

