package server.DAO;


import dataBase.Database;
import shared.models.phieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class phieuNhapDAO {
    public static phieuNhapDAO getInstance() {
        return new phieuNhapDAO();
    }

    public static int themPhieuNHap(phieuNhap phieuNhap) {
        int generatedId = -1;
        String sql = "INSERT INTO `phieunhap`(`maphieunhap`, `thoigian`, `manhacungcap`, `nguoitao`, `tongtien`, `trangthai`) VALUES (?,?,?,?,?,?)";

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, phieuNhap.getMaphieu());
            ps.setTimestamp(2, phieuNhap.getThoigiantao());
            ps.setInt(3, phieuNhap.getManhacungcap());
            ps.setInt(4, phieuNhap.getManguoitao());
            ps.setDouble(5, phieuNhap.getTongTien());
            ps.setInt(6, 1); // trạng thái mặc định là 1


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
    //
    public static int deletePhieuNHap(int id) {
        int result = -1;
        String sql = "UPDATE phieunhap SET trangthai = 0 WHERE maphieunhap = ?";

        try (Connection con = Database.getConnected();
             PreparedStatement pst = con.prepareStatement(sql)){

            pst.setInt(1, id);
            result = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<phieuNhap> layDanhSachPhieuNhap() {
        List<phieuNhap> list = new ArrayList<>();
        String sql = "SELECT \n" +
                "    pn.maphieunhap, \n" +
                "    pn.thoigian, \n" +
                "    ncc.tennhacungcap, \n" +
                "    nv.hoten AS nguoi_tao, \n" +
                "    pn.tongtien\n" +
                "FROM phieunhap pn \n" +
                "JOIN nhacungcap ncc ON pn.manhacungcap = ncc.manhacungcap \n" +
                "JOIN nhanvien nv ON nv.manv = pn.nguoitao\n"+
                "WHERE pn.trangthai = 1";
        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                phieuNhap pn = new phieuNhap();
                pn.setMaphieu(rs.getInt("maphieunhap"));
                pn.setThoigiantao(rs.getTimestamp("thoigian"));
                pn.setTennhacungcap(rs.getString("tennhacungcap"));
                pn.setTennhanvien(rs.getString("nguoi_tao"));
                pn.setTongTien(rs.getInt("tongtien"));

                list.add(pn);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static phieuNhap layPhieuNhapTheoID(int id) {
        phieuNhap pn = null;
        String sql = """
        SELECT 
            pn.maphieunhap,
            pn.thoigian,
            pn.tongtien,
            nv.hoten AS nguoi_tao,
            ncc.tennhacungcap
        FROM phieunhap pn
        JOIN nhacungcap ncc ON ncc.manhacungcap = pn.manhacungcap
        JOIN nhanvien nv ON nv.manv = pn.nguoitao
        WHERE pn.maphieunhap = ?
    """;

        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pn = new phieuNhap();
                pn.setMaphieu(rs.getInt("maphieunhap"));
                pn.setThoigiantao(rs.getTimestamp("thoigian"));
                pn.setTennhacungcap(rs.getString("tennhacungcap"));
                pn.setTennhanvien(rs.getString("nguoi_tao"));
                pn.setTongTien(rs.getInt("tongtien"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pn;
    }
    public boolean existsImportId(int  id) {
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


    public static List<phieuNhap> thongTinPhieuNhap() {
        List<phieuNhap> list = new ArrayList<>();

        return list;
    }





}
