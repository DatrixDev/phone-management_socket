package server.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dataBase.Database;
import server.DAO.cauHinhDAO;
import shared.models.SanPham;


import javax.swing.*;

public class SanPhamDAO {

    public static SanPhamDAO getInstance() {
        return new SanPhamDAO();
    }

    public static int themSanPham(SanPham sp) {
        int  generatedId = -1;
        String sql = "INSERT INTO sanpham(tensp, hinhanh, xuatxu, chipxuly, dungluongpin, kichthuocman, hedieuhanh, phienbanhdh, camerasau, cameratruoc, thoigianbaohanh, thuonghieu, khuvuckho) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sp.getTenSanPham());
            ps.setString(2, sp.getImage_path());
            ps.setInt(3, sp.getMaxuatxu());
            ps.setString(4, sp.getChip());
            ps.setInt(5, sp.getDungLuongPin());
            ps.setDouble(6, sp.getKichThuocMan());
            ps.setInt(7, sp.getMahedieuhanh());
            ps.setInt(8, sp.getPhienBanHDH());
            ps.setString(9, sp.getCamSau());
            ps.setString(10, sp.getCamTruoc());
            ps.setInt(11, sp.getThoiGianBaoHanh());
            ps.setInt(12, sp.getMathuonghieu());
            ps.setString(13, sp.getKhuVucKho());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public static int suaSanPham(SanPham sp, int ID) {
        int rows = 0;
        String sql = "UPDATE `sanpham` SET `tensp`=?, `hinhanh`=?, `xuatxu`=?, `chipxuly`=?, `dungluongpin`=?, `kichthuocman`=?, `hedieuhanh`=?, `phienbanhdh`=?, `camerasau`=?, `cameratruoc`=?, `thoigianbaohanh`=?, `thuonghieu`=?, `khuvuckho`=? WHERE `masp`=?";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSanPham());
            ps.setString(2, sp.getImage_path());
            ps.setInt(3, sp.getMaxuatxu());
            ps.setString(4, sp.getChip());
            ps.setInt(5, sp.getDungLuongPin());
            ps.setDouble(6, sp.getKichThuocMan());
            ps.setInt(7, sp.getMahedieuhanh());
            ps.setInt(8, sp.getPhienBanHDH());
            ps.setString(9, sp.getCamSau());
            ps.setString(10, sp.getCamTruoc());
            ps.setInt(11, sp.getThoiGianBaoHanh());
            ps.setInt(12, sp.getMathuonghieu());
            ps.setString(13, sp.getKhuVucKho());        // khuvuckho

            ps.setInt(14, ID);                                      // WHERE masp=?

            rows = ps.executeUpdate();
            System.out.println("Sửa sản phẩm thành công: " + rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static int  xoaSanPham(int id) {
        int rows = 0;
        String sql = "DELETE FROM sanpham WHERE masp = ?";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            rows = ps.executeUpdate();

            System.out.println("Xóa hàng thành công: " + rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  rows;
    }


    public static List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT \n" +
                "    sp.masp, \n" +
                "    sp.tensp, \n" +
                "    th.tenthuonghieu AS thuonghieu, \n" +
                "    hd.tenhedieuhanh AS hedieuhanh, \n" +
                "    sp.kichthuocman, \n" +
                "    sp.chipxuly, \n" +
                "    sp.dungluongpin, \n" +
                "    xx.tenxuatxu AS xuatxu, \n" +
                "    sp.khuvuckho,\n" +
                "    sp.soluongton\n "+
                "FROM sanpham sp\n" +
                "JOIN thuonghieu th ON sp.thuonghieu = th.mathuonghieu\n" +
                "JOIN hedieuhanh hd ON sp.hedieuhanh = hd.mahedieuhanh\n" +
                "JOIN xuatxu xx ON sp.xuatxu = xx.maxuatxu\n" +
                "JOIN phienbansanpham pb ON pb.masp = sp.masp";

        try (Connection conn = Database.getConnected();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt("masp"));
                sp.setSoLuong(rs.getInt("soluongton"));
                sp.setTenSanPham(rs.getString("tensp"));
                sp.setThuongHieu(rs.getString("thuonghieu"));
                sp.setHeDieuHanh(rs.getString("hedieuhanh"));
                sp.setKichThuocMan(rs.getDouble("kichthuocman"));
                sp.setChip(rs.getString("chipxuly"));
                sp.setDungLuongPin(rs.getInt("dungluongpin"));
                sp.setXuatXu(rs.getString("xuatxu"));
                sp.setKhuVucKho(rs.getString("khuvuckho"));

                list.add(sp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<SanPham> selectAll() {
        ArrayList<SanPham> result = new ArrayList<SanPham>();
        try {
            Connection con = (Connection) Database.getConnected();
            String sql = "SELECT * FROM sanpham ";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int madm = rs.getInt("masp");
                String tendm = rs.getString("tensp");
                String hinhanh = rs.getString("hinhanh");
                int xuatxu = rs.getInt("xuatxu");
                String chipxuly = rs.getString("chipxuly");
                int dungluongpin = rs.getInt("dungluongpin");
                double kichthuocman = rs.getDouble("kichthuocman");
                int hedieuhanh = rs.getInt("hedieuhanh");
                int phienbanhdh = rs.getInt("phienbanhdh");
                String camerasau = rs.getString("camerasau");
                String cameratruoc = rs.getString("cameratruoc");
                int thoigianbaohanh = rs.getInt("thoigianbaohanh");
                int thuonghieu = rs.getInt("thuonghieu");
                int khuvuckho = rs.getInt("khuvuckho");
                int soluongton = rs.getInt("soluongton");
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static List<SanPham> getSanPhamByTenKhuVuc(String tenKhuVuc) {
        List<SanPham> products = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE khuvuckho = ?";
        try (Connection conn = Database.getConnected();

             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenKhuVuc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setId(rs.getInt("masp"));
                sp.setTenSanPham(rs.getString("tensp"));
                sp.setKhuVucKho(rs.getString("khuvuckho"));
                products.add(sp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public static SanPham getSanPhamById(int id) {
        String sql = "SELECT \n" +
                "    sp.masp, \n" +
                "    sp.tensp, \n" +
                "    sp.hinhanh, \n" +
                "    sp.soluongton, \n" +
                "    th.tenthuonghieu AS thuonghieu, \n" +
                "    hd.tenhedieuhanh AS hedieuhanh, \n" +
                "    sp.kichthuocman, \n" +
                "    sp.chipxuly, \n" +
                "    sp.dungluongpin, \n" +
                "    xx.tenxuatxu AS xuatxu, \n" +
                "    sp.phienbanhdh, \n" +
                "    sp.thoigianbaohanh, \n" +
                "    sp.camerasau, \n" +
                "    sp.cameratruoc, \n" +
                "    sp.khuvuckho \n" +
                "FROM sanpham sp \n" +
                "JOIN thuonghieu th ON sp.thuonghieu = th.mathuonghieu \n" +
                "JOIN hedieuhanh hd ON sp.hedieuhanh = hd.mahedieuhanh \n" +
                "JOIN xuatxu xx ON sp.xuatxu = xx.maxuatxu \n" +
                "WHERE sp.masp = ?";

        SanPham sp = null;

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    sp = new SanPham();
                    sp.setId(rs.getInt("masp"));
                    sp.setTenSanPham(rs.getString("tensp"));
                    sp.setImage_path(rs.getString("hinhanh"));
                    sp.setSoLuong(rs.getInt("soluongton"));
                    sp.setThuongHieu(rs.getString("thuonghieu"));
                    sp.setHeDieuHanh(rs.getString("hedieuhanh"));
                    sp.setXuatXu(rs.getString("xuatxu"));
                    sp.setKichThuocMan(rs.getDouble("kichthuocman"));
                    sp.setChip(rs.getString("chipxuly"));
                    sp.setDungLuongPin(rs.getInt("dungluongpin"));
                    sp.setPhienBanHDH(rs.getInt("phienbanhdh"));
                    sp.setThoiGianBaoHanh(rs.getInt("thoigianbaohanh"));
                    sp.setCamSau(rs.getString("camerasau"));
                    sp.setCamTruoc(rs.getString("cameratruoc"));
                    sp.setKhuVucKho(rs.getString("khuvuckho"));
                    // Fetch configurations
                    sp.setCauHinhs(cauHinhDAO.getCauHinhsBySanPhamId(id));
                    System.out.println(sp.getCauHinhs());
                } else {
                    System.out.println("Không tìm thấy sản phẩm với ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sp;
    }

    public static int  capNhatSoluongKhiNhap(int masp,int soluong){
        int result = 0;
        String sql = "UPDATE sanpham SET soluongton = soluongton+ ?  WHERE masp=? ";
        try(Connection connection = Database.getConnected();
            PreparedStatement psms = connection.prepareStatement(sql)){
            psms.setInt(1,soluong);
            psms.setInt(2,masp);
            result = psms.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }
    public static int capNhatSoluongKhiXuat(int masp, int soluong) {
        int result = 0;
        String sql = "UPDATE sanpham SET soluongton = soluongton - ? WHERE masp = ?";
        try (Connection connection = Database.getConnected();
             PreparedStatement psms = connection.prepareStatement(sql)) {
            psms.setInt(1, soluong);
            psms.setInt(2, masp);
            result = psms.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }









}
