package server.DAO;


import dataBase.Database;
import shared.models.mausac;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MauSacDAO {


    public List<mausac> getMausac() {
        List<mausac> list = new ArrayList<>();
        String query = "SELECT mamau,tenmau FROM mausac WHERE trangthai =1 ";
        try (Connection conn = Database.getConnected();

             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mausac ms = new mausac();
                ms.setTenmau(rs.getString("tenmau"));
                ms.setMamau(rs.getInt("mamau"));
                list.add(ms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themMauSac(mausac ms) {
        String sql = "INSERT INTO mausac (tenmau, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ms.getTenmau());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean suaMauSac(mausac ms) {
        String sql = "UPDATE mausac SET tenmau = ? WHERE mamau = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ms.getTenmau());
            ps.setInt(2, ms.getMamau());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaMauSac(int mamau) {
        String sql = "UPDATE mausac SET trangthai = 0 WHERE mamau = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mamau);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTenMauTonTai(String tenmau) {
        String sql = "SELECT COUNT(*) FROM mausac WHERE tenmau = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenmau);
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
