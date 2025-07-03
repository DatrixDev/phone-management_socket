package server.DAO;


import dataBase.Database;
import shared.models.ram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RamDAO {


    public List<ram> getRamm() {
        List<ram> list = new ArrayList<>();
        String query = "SELECT kichthuocram,madlram FROM dungluongram WHERE trangthai = 1 " ;
        try (Connection conn = Database.getConnected();

             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                ram ram = new ram();
                ram.setKichthuocram(rs.getInt("kichthuocram"));
                ram.setMadlram(rs.getInt("madlram"));
                list.add(ram);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themRam(ram r) {
        String sql = "INSERT INTO dungluongram (kichthuocram, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getKichthuocram());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean suaRam(ram r) {
        String sql = "UPDATE dungluongram SET kichthuocram = ? WHERE madlram = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getKichthuocram());
            ps.setInt(2, r.getMadlram());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaRam(int madlram) {
        String sql = "UPDATE dungluongram SET trangthai = 0 WHERE madlram = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, madlram);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isKichThuocRamTonTai(int kichthuoc) {
        String sql = "SELECT COUNT(*) FROM dungluongram WHERE kichthuocram = ? AND trangthai = 1";
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
