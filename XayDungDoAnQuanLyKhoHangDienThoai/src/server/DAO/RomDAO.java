package server.DAO;


import dataBase.Database;
import shared.models.rom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RomDAO {

    public List<rom> getRomm() {
        List<rom> list = new ArrayList<>();
        String query = "SELECT kichthuocrom,madlrom FROM dungluongrom  WHERE trangthai =1 ";
        try (Connection conn = Database.getConnected();

             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                rom r = new rom();
                r.setKicthuocrom(rs.getInt("kichthuocrom"));
                r.setMadlrom(rs.getInt("madlrom"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themRom(rom r) {
        String sql = "INSERT INTO dungluongrom (kichthuocrom, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getKicthuocrom());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean suaRom(rom r) {
        String sql = "UPDATE dungluongrom SET kichthuocrom = ? WHERE madlrom = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getKicthuocrom());
            ps.setInt(2, r.getMadlrom());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaRom(int madlrom) {
        String sql = "UPDATE dungluongrom SET trangthai = 0 WHERE madlrom = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, madlrom);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isKichThuocRomTonTai(int kichthuoc) {
        String sql = "SELECT COUNT(*) FROM dungluongrom WHERE kichthuocrom = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kichthuoc);
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
