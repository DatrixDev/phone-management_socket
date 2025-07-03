package server.DAO;


import dataBase.Database;
import shared.models.TinNhan;
import shared.request.ChatMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TinNhanDAO
{

    public static boolean luuTinNhan(ChatMessage msg) {
        String sql = "INSERT INTO tin_nhan (nguoi_gui, nguoi_nhan, la_nhom, noi_dung, thoi_gian_gui) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = Database.getConnected();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, msg.getSender());
            stmt.setString(2, msg.getRecipient()); // Có thể là username hoặc tên nhóm
            stmt.setBoolean(3, msg.isGroup());
            stmt.setString(4, msg.getMessage());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<ChatMessage> layTinNhan(String nguoiDung1, String nguoiDung2, boolean laNhom) {
        List<ChatMessage> ds = new ArrayList<>();
        String sql = laNhom
                ? "SELECT * FROM tin_nhan WHERE nguoi_nhan = ? AND la_nhom = TRUE ORDER BY thoi_gian_gui"
                : "SELECT * FROM tin_nhan WHERE ((nguoi_gui = ? AND nguoi_nhan = ?) OR (nguoi_gui = ? AND nguoi_nhan = ?)) AND la_nhom = FALSE ORDER BY thoi_gian_gui";

        try (Connection conn = Database.getConnected();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (laNhom) {
                stmt.setString(1, nguoiDung2); // tên nhóm
            } else {
                stmt.setString(1, nguoiDung1);
                stmt.setString(2, nguoiDung2);
                stmt.setString(3, nguoiDung2);
                stmt.setString(4, nguoiDung1);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChatMessage msg = new ChatMessage(
                        rs.getString("nguoi_gui"),
                        "", // Không có displayName trong DB → để trống
                        rs.getString("nguoi_nhan"),
                        rs.getString("noi_dung"),
                        rs.getBoolean("la_nhom"),
                        rs.getBoolean("la_nhom") ? rs.getString("nguoi_nhan") : null
                );
                ds.add(msg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

}
