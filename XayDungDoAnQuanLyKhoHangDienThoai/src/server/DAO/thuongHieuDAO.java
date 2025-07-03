package server.DAO;


import dataBase.Database;
import shared.models.thuonghieu;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class thuongHieuDAO implements Serializable {
    public static List<thuonghieu> getAllThuongHieu() {
        List<thuonghieu> list = new ArrayList<>();
        String sql = "SELECT mathuonghieu, tenthuonghieu FROM thuonghieu WHERE trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                thuonghieu th = new thuonghieu();
                th.setMathuonghieu(rs.getInt("mathuonghieu"));
                th.setTenthuonghieu(rs.getString("tenthuonghieu"));
                list.add(th);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themThuongHieu(thuonghieu th) {
        String sql = "INSERT INTO thuonghieu (tenthuonghieu, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, th.getTenthuonghieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaThuongHieu(thuonghieu th) {
        String sql = "UPDATE thuonghieu SET tenthuonghieu = ? WHERE mathuonghieu = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, th.getTenthuonghieu());
            ps.setInt(2, th.getMathuonghieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean xoaThuongHieu(int mathuonghieu) {
        String sql = "UPDATE thuonghieu SET trangthai = 0 WHERE mathuonghieu = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mathuonghieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTenThuongHieuTonTai(String name) {
        String sql = "SELECT COUNT(*) FROM thuonghieu WHERE tenthuonghieu = ? AND trangthai = 1";
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