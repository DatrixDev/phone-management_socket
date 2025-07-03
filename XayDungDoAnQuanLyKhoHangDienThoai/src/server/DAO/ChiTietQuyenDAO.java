package server.DAO;


import dataBase.Database;
import shared.models.ChiTietQuyen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChiTietQuyenDAO implements ChiTietInterface<ChiTietQuyen>{
    public static ChiTietQuyenDAO getInstance() {
        return new ChiTietQuyenDAO();
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "DELETE FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
//            Database.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int insert(ArrayList<ChiTietQuyen> t) {
        int result = 0;
        for (int i = 0; i < t.size(); i++) {
            try {
                Connection con = (Connection) Database.getConnected();
                String sql = "INSERT INTO `ctquyen`(`manhomquyen`,`machucnang`,`hanhdong`) VALUES (?,?,?)";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setInt(1, t.get(i).getManhomquyen());
                pst.setString(2, t.get(i).getMachucnang());
                pst.setString(3, t.get(i).getHanhdong());
                result = pst.executeUpdate();
//                Database.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietQuyen> selectAll(String t) {
        ArrayList<ChiTietQuyen> result = new ArrayList<ChiTietQuyen>();
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int manhomquyen = rs.getInt("manhomquyen");
                String machucnang = rs.getString("machucnang");
                String hanhdong = rs.getString("hanhdong");
                ChiTietQuyen dvt = new ChiTietQuyen(manhomquyen, machucnang, hanhdong);
                result.add(dvt);
            }
//            Database.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietQuyen> t, String pk) {
        int result = this.delete(pk);
        if(result != 0) result = this.insert(t);
        return result;
    }
}
