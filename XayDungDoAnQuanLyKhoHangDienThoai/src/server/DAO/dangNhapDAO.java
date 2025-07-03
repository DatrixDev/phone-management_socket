package server.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataBase.Database;
import shared.models.NhanVien;

public class dangNhapDAO {

    public static NhanVien layThongTinNhanVien(int maNV) {
        Connection conn = Database.getConnected();
        String sql = "SELECT * FROM nhanvien WHERE manv = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maNV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setManv(rs.getInt("manv"));
                nv.setHoten(rs.getString("hoten"));
                nv.setEmail(rs.getString("email"));
                nv.setSdt(rs.getString("sdt"));

                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
