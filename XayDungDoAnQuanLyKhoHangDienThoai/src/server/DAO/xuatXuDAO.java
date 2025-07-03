package server.DAO;



import dataBase.Database;
import shared.models.XuatXu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class xuatXuDAO {
    public List<XuatXu> getAllXuatXu() {
        List<XuatXu> list = new ArrayList<>();
        String sql = "SELECT maxuatxu, tenxuatxu FROM xuatxu WHERE trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                XuatXu xx = new XuatXu();
                xx.setId(rs.getInt("maxuatxu"));
                xx.setTen(rs.getString("tenxuatxu"));
                list.add(xx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themXuatXu(XuatXu xx) {
        String sql = "INSERT INTO xuatxu (tenxuatxu, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, xx.getTen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean suaXuatXu(XuatXu xx) {
        String sql = "UPDATE xuatxu SET tenxuatxu = ? WHERE maxuatxu = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, xx.getTen());
            ps.setInt(2, xx.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaXuatXu(int maxuatxu) {
        String sql = "UPDATE xuatxu SET trangthai = 0 WHERE maxuatxu = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maxuatxu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTenXuatXuTonTai(String name) {
        String sql = "SELECT COUNT(*) FROM xuatxu WHERE tenxuatxu = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}