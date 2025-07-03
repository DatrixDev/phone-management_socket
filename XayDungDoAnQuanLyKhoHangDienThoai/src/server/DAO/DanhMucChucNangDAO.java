package server.DAO;


import dataBase.Database;
import shared.models.DanhMucChucNang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DanhMucChucNangDAO {

    public static DanhMucChucNangDAO getInstance() {
        return new DanhMucChucNangDAO();
    }

    public ArrayList<DanhMucChucNang> selectAll() {
        ArrayList<DanhMucChucNang> result = new ArrayList<>();
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM danhmucchucnang";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String machucnang = rs.getString("machucnang");
                String tenchucnang = rs.getString("tenchucnang");
                DanhMucChucNang dvt = new DanhMucChucNang(machucnang, tenchucnang);
                result.add(dvt);
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
}
