package server.DAO;


import dataBase.Database;
import shared.models.KhuVucKho;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuVucKhoDAO {
    public static int themKhuVucKho(KhuVucKho khuVucKho)  {
        int rows = 0;
        String sql = "INSERT INTO khuvuckho (tenkhuvuc,ghichu,toado,trangthai) VALUES (?,?,?,?)";
        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, khuVucKho.getTenkhuvuc().trim());
            ps.setString(2, khuVucKho.getGhichu().trim());
            ps.setString(3, khuVucKho.getToado().trim());
            ps.setNull(4, java.sql.Types.INTEGER);

            rows = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rows;

    }

    public static int xoaKhuVuc(int  MaKho) {
        int rows = 0;
        String sql = "DELETE FROM khuvuckho WHERE makhuvuc=?";
        try (Connection con = Database.getConnected();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, MaKho);
            rows = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public static int suaKhuVuc(KhuVucKho khuVucKho, int  MaKho){
        int rows = 0;
        String sql = "UPDATE KhuVucKho SET tenkhuvuc=? , ghichu=? ,toado = ?  WHERE makhuvuc = ?";
        try(Connection con = Database.getConnected();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, khuVucKho.getTenkhuvuc().trim());
            ps.setString(2, khuVucKho.getGhichu().trim());
            ps.setString(3, khuVucKho.getToado().trim());
            ps.setInt(4, MaKho);


            rows = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    public static List<KhuVucKho> getAllkhuVuc()  {
        List<KhuVucKho> list  = new ArrayList<>();
        String sql = "SELECT makhuvuc,tenkhuvuc,ghichu,toado,hinhAnhPath FROM khuvuckho";
        try(Connection con = Database.getConnected();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()){
                KhuVucKho kv = new KhuVucKho();
                kv.setMakhuvuc(rs.getInt("makhuvuc"));
                kv.setTenkhuvuc(rs.getString("tenkhuvuc").trim());
                kv.setGhichu(rs.getString("ghichu").trim());
                kv.setToado(rs.getString("toado").trim());
                kv.setHinhAnhPath(rs.getString("hinhAnhPath"));

                list.add(kv);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public static KhuVucKho getKhuVucKhoByID(int id){
        String sql = "SELECT * FROM khuvuckho WHERE makhuvuc = ?";
        KhuVucKho kv = null;

        try(Connection con = Database.getConnected();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                kv = new KhuVucKho();
                kv.setMakhuvuc(rs.getInt("makhuvuc"));
                kv.setTenkhuvuc(rs.getString("tenkhuvuc"));
                kv.setGhichu(rs.getString("ghichu"));
                kv.setToado(rs.getString("toado"));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return kv;
    }

    public boolean kiemTraTrungMaKho(int  maKho) {
        String query = "SELECT COUNT(*) FROM khuvuckho WHERE makhuvuc = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, maKho);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getKhuVucKhoFromDatabase() {
        ArrayList<String> khuvuc = new ArrayList<>();
        String query = "SELECT tenkhuvuc FROM khuvuckho";
        try (Connection conn = Database.getConnected();

             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                khuvuc.add(rs.getString("tenkhuvuc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khuvuc;
    }


}
