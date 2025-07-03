package server.DAO;

import dataBase.Database;
import shared.models.cauHinhSanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cauHinhDAO {

    // Thêm cấu hình sản phẩm
    public static int themCauHinh(cauHinhSanPham cauhinh,int masp) {
        int rows = 0;
        String sql = "INSERT INTO `phienbansanpham`(`masp`, `rom`, `ram`, `mausac`, `gianhap`, `giaxuat`) VALUES (?,?,?,?,?,?)";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, masp);
            ps.setInt(2, cauhinh.getMarom());
            ps.setInt(3, cauhinh.getMaram());
            ps.setInt(4, cauhinh.getMamausac());
            ps.setDouble(5, cauhinh.getGianhap());
            ps.setDouble(6, cauhinh.getGiaxuat());

            rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    // Sửa cấu hình sản phẩm
    public static int suaCauHinh(cauHinhSanPham cauhinh,int masp) {
        int rows = 0;
        String sql = "UPDATE `phienbansanpham` SET `rom`=?, `ram`=?, `mausac`=?, `gianhap`=?, `giaxuat`=? WHERE `masp`=?";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cauhinh.getMarom());
            ps.setInt(2, cauhinh.getMaram());
            ps.setInt(3, cauhinh.getMamausac());
            ps.setDouble(4, cauhinh.getGianhap());
            ps.setDouble(5, cauhinh.getGiaxuat());
            ps.setInt(6,masp);

            rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static cauHinhSanPham getCauHinhsBySanPhamId(int masp) {
        String sql = "SELECT \n" +
                "       pb.gianhap  ,   \n" +
                "       pb.giaxuat  ,   \n" +
                "       ms.tenmau As tenmauu, \n" +
                "       ram.kichthuocram As Kichthuocram, \n" +
                "       rom.kichthuocrom As Kichthuocrom\n" +
                "FROM phienbansanpham pb\n" +
                "JOIN mausac ms ON pb.mausac = ms.mamau\n" +
                "JOIN dungluongram ram ON pb.ram = ram.madlram\n" +
                "JOIN dungluongrom rom ON pb.rom = rom.madlrom\n" +
                "WHERE pb.masp = ?\n";

        cauHinhSanPham ch = null;

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, masp);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ch = new cauHinhSanPham();
                ch.setMausac(rs.getString("tenmauu"));
                ch.setRam(rs.getString("Kichthuocram"));
                ch.setRom(rs.getString("Kichthuocrom"));
                ch.setGianhap(rs.getDouble("gianhap"));
                ch.setGiaxuat(rs.getDouble("giaxuat"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ch;
    }



    public static cauHinhSanPham getCauHinhByMaCauHinh(int masp) {
        cauHinhSanPham cauHinhSanPham = null;
        String sql = "SELECT pb.*, \n" +
                "       ms.tenmau, \n" +
                "       ram.kichthuocram, \n" +
                "       rom.kichthuocrom\n" +
                "FROM phienbansanpham pb\n" +
                "JOIN mausac ms ON pb.mausac = ms.mamau\n" +
                "JOIN dungluongram ram ON pb.ram = ram.madlram\n" +
                "JOIN dungluongrom rom ON pb.rom = rom.madlrom\n" +
                "WHERE pb.masp = ?\n";

        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, masp);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cauHinhSanPham = new cauHinhSanPham();
                    cauHinhSanPham.setGianhap(rs.getDouble("gianhap"));
                    cauHinhSanPham.setGiaxuat(rs.getDouble("giaxuat"));
                    cauHinhSanPham.setRam(rs.getString("kichthuocram"));
                    cauHinhSanPham.setRom(rs.getString("kichthuocrom"));
                    cauHinhSanPham.setMausac(rs.getString("tenmau"));
                    System.out.println(rs.getInt("soluongton"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cauHinhSanPham;

    }

    public static int getTotalQuantityBySanPham(int idSanPham) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(soluongton) AS total FROM sanpham WHERE masp = ?";
        try (Connection conn = Database.getConnected();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSanPham);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalQuantity = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalQuantity;
    }

}
