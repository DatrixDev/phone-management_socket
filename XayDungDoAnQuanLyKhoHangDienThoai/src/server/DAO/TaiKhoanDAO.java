package server.DAO;


import dataBase.Database;
import shared.models.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaiKhoanDAO implements DAOInterface<TaiKhoan>{

    public static TaiKhoanDAO getInstance(){
        return new TaiKhoanDAO();
    }

    @Override
    public int insert(TaiKhoan t) {
        int result = 0 ;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "INSERT INTO `taikhoan`(`manv`,`tendangnhap`,`matkhau`,`manhomquyen`,`trangthai`) VALUES (?,?,?,?,?)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getManv());
            pst.setString(2, t.getUsername());
            pst.setString(3, t.getMatkhau());
            pst.setInt(4, t.getManhomquyen());
            pst.setInt(5, t.getTrangthai());
            result = pst.executeUpdate();
//            Database.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(TaiKhoan t) {
        int result = 0 ;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "UPDATE `taikhoan` SET `tendangnhap`=?,`trangthai`=?,`manhomquyen`=? WHERE manv=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getUsername());
            pst.setInt(2, t.getTrangthai());
            pst.setInt(3, t.getManhomquyen());
            pst.setInt(4, t.getManv());
            result = pst.executeUpdate();
//            Database.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean updatePass(String email, String password) {
        String sql = "UPDATE taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv SET tk.matkhau = ? WHERE nv.email = ?";
        try (Connection con = Database.getConnected();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, password);
            pst.setString(2, email);
            int result = pst.executeUpdate();

            return result > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public TaiKhoan selectByEmail(String t) {
        TaiKhoan tk = null;
        try {
            Connection con = Database.getConnected();
            String sql = "SELECT * FROM taikhoan tk join nhanvien nv on tk.manv=nv.manv where nv.email = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1,t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                tk = new TaiKhoan(manv, tendangnhap, matkhau, manhomquyen, trangthai);
                return tk;
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return tk;
    }

    public void sendOpt(String email, String opt){
        int result;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "UPDATE taikhoan tk join nhanvien nv on tk.manv=nv.manv SET `otp`=? WHERE email=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, opt);
            pst.setString(2, email);
            result = pst.executeUpdate();
//            Database.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkOtp(String email, String otp){
        boolean check = false;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM taikhoan tk join nhanvien nv on tk.manv=nv.manv where nv.email = ? and tk.otp = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, otp);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                check = true;
                return check;
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
        }
        return check;
    }

    @Override
    public int delete(String t) {
        int result = 0 ;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "UPDATE `taikhoan` SET `trangthai`='-1' where manv = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(t));
            result = pst.executeUpdate();
//            Database.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<TaiKhoan> selectAll() {
        ArrayList<TaiKhoan> result = new ArrayList<TaiKhoan>();
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM taikhoan WHERE trangthai = '0' OR trangthai = '1'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int manv = rs.getInt("manv");
                String username = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int manhomquyen = rs.getInt("manhomquyen");
                int trangthai = rs.getInt("trangthai");
                TaiKhoan tk = new TaiKhoan(manv, username, matkhau, manhomquyen, trangthai);
                result.add(tk);
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public TaiKhoan selectById(String t) {
        TaiKhoan result = null;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM taikhoan WHERE manv=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                TaiKhoan tk = new TaiKhoan(manv, tendangnhap, matkhau, manhomquyen, trangthai);
                return result;
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    public TaiKhoan selectByUser(String t) {
        TaiKhoan result = null;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM taikhoan WHERE tendangnhap=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                TaiKhoan tk = new TaiKhoan(manv, tendangnhap, matkhau, manhomquyen, trangthai);
                result = tk;
            }
//            Database.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    public boolean updateQuyen(int manv, int manhomquyen) {
        String sql = "UPDATE taikhoan SET manhomquyen = ? WHERE manv = ?";

        try (Connection connection = Database.getConnected();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, manhomquyen);
            pst.setInt(2, manv);

            return pst.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }



    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlykhohangdienthoai' AND   TABLE_NAME   = 'taikhoan'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst() ) {
                System.out.println("No data");
            } else {
                while ( rs2.next() ) {
                    result = rs2.getInt("AUTO_INCREMENT");

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}