package server.DAO;


import dataBase.Database;
import shared.models.phieuXuat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class phieuXuatdao {
    public static phieuXuatdao getInstance() {
        return new phieuXuatdao();
    }

    public static int themPhieuXuat(phieuXuat phieuXuat) {
        int generatedId = -1;
        String sql = "INSERT INTO `phieuxuat`(`maphieuxuat`, `thoigian`, `makh`, `nguoitaophieuxuat`, `tongtien`, `trangthai`) VALUES (?,?,?,?,?,?)";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, phieuXuat.getMaphieuXuat());
            ps.setTimestamp(2, phieuXuat.getThoigiantao());
            ps.setInt(3, phieuXuat.getMaKhachHang());
            ps.setInt(4, phieuXuat.getManguoitao());
            ps.setDouble(5, phieuXuat.getTongTien());
            ps.setInt(6, 1);


            int rows = ps.executeUpdate(); if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;


    }

    public static int deletePhieuXuat(int id) {
        int result = -1;
        String sql = "UPDATE phieuxuat SET trangthai = 0 WHERE maphieuxuat = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement pst = con.prepareStatement(sql)){

            pst.setInt(1, id);
            result = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<phieuXuat> layDanhSachPhieuXuat() {
        List<phieuXuat> list = new ArrayList<>();
        String sql = "SELECT " +
                "    px.maphieuxuat, " +
                "    px.thoigian, " +
                "    kh.tenkhachhang, " +
                "    nv.hoten AS nguoi_tao, " +
                "    px.tongtien " +
                "FROM phieuxuat px " +
                "JOIN khachhang kh ON px.makh = kh.makh " +  // ← thêm khoảng trắng ở cuối
                "JOIN nhanvien nv ON nv.manv = px.nguoitaophieuxuat " +
                "WHERE px.trangthai = 1";


        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                phieuXuat px = new phieuXuat();
                px.setMaphieuXuat(rs.getInt("maphieuxuat"));
                px.setThoigiantao(rs.getTimestamp("thoigian"));
                px.setTenKhachHang(rs.getString("tenkhachhang"));
                px.setTennhanvien(rs.getString("nguoi_tao"));
                px.setTongTien(rs.getInt("tongtien"));

                list.add(px);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static phieuXuat layPhieuXuatTheoID(int id) {
        phieuXuat px = null;
        String sql = """
        SELECT 
            px.maphieuxuat,
            px.thoigian,
            px.tongtien,
            nv.hoten AS nguoi_tao,
            kh.tenkhachhang
        FROM phieuxuat px
        JOIN khachhang kh ON kh.makh = px.makh
        JOIN nhanvien nv ON nv.manv = px.nguoitaophieuxuat
        WHERE px.maphieuxuat = ?
    """;

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                px = new phieuXuat();
                px.setMaphieuXuat(rs.getInt("maphieuxuat"));
                px.setThoigiantao(rs.getTimestamp("thoigian"));
                px.setTenKhachHang(rs.getString("tenkhachhang"));
                px.setTennhanvien(rs.getString("nguoi_tao"));
                px.setTongTien(rs.getInt("tongtien"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return px;
    }

    public boolean existsExportId(int id) {
        try (Connection conn = Database.getConnected();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM phieunhap WHERE maphieunhap = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public static List<phieuXuat> thongTinPhieuXuat() {
        List<phieuXuat> list = new ArrayList<>();

        return list;
    }





}
