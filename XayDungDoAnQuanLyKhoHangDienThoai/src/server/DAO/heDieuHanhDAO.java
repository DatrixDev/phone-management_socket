package server.DAO;

import dataBase.Database;
import shared.models.HeDieuHanh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class heDieuHanhDAO {
    public List<HeDieuHanh> getAllHeDieuHanh() {
        List<HeDieuHanh> list = new ArrayList<>();
        String sql = "SELECT mahedieuhanh, tenhedieuhanh FROM hedieuhanh WHERE trangthai = 1 ";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HeDieuHanh hdh = new HeDieuHanh();
                hdh.setMaHeDieuHanh(rs.getInt("mahedieuhanh"));
                hdh.setTenHeDieuHanh(rs.getString("tenhedieuhanh"));
                list.add(hdh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themHeDieuHanh(HeDieuHanh hdh) {
        String sql = "INSERT INTO hedieuhanh (tenhedieuhanh, trangthai) VALUES (?, 1)";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdh.getTenHeDieuHanh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaHeDieuHanh(HeDieuHanh hdh) {
        String sql = "UPDATE hedieuhanh SET tenhedieuhanh = ? WHERE mahedieuhanh = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hdh.getTenHeDieuHanh());
            ps.setInt(2, hdh.getMaHeDieuHanh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaHeDieuHanh(int mahedieuhanh) {
        String sql = "UPDATE hedieuhanh SET trangthai = 0 WHERE mahedieuhanh = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mahedieuhanh);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTenHeDieuHanhTonTai(String tenhdh) {
        String sql = "SELECT COUNT(*) FROM hedieuhanh WHERE tenhedieuhanh = ? AND trangthai = 1";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenhdh);
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