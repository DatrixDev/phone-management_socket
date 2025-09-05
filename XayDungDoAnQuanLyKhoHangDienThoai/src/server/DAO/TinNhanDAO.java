package server.DAO;

import dataBase.Database;
import shared.request.ChatMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TinNhanDAO {

    /**
     * Lưu tin nhắn và trả về ID sinh ra (AUTO_INCREMENT).
     * Trả về null nếu thất bại.
     */
    public static Long luuTinNhan(ChatMessage msg) {
        String sql = "INSERT INTO tin_nhan (nguoi_gui, nguoi_nhan, la_nhom, noi_dung, thoi_gian_gui) " +
                "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = Database.getConnected();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, msg.getSender());
            stmt.setString(2, msg.getRecipient());        // username hoặc tên nhóm
            stmt.setBoolean(3, msg.isGroup());
            stmt.setString(4, msg.getMessage());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy lịch sử tin nhắn.
     * - Nếu laNhom = true: lấy theo tên nhóm (nguoi_nhan) và la_nhom = TRUE
     * - Nếu laNhom = false: lấy hội thoại 1-1 giữa 2 user (A,B) và la_nhom = FALSE
     * Trả về ChatMessage có gắn id để client xóa theo id.
     */
    public static List<ChatMessage> layTinNhan(String nguoiDung1, String nguoiDung2, boolean laNhom) {
        List<ChatMessage> ds = new ArrayList<>();
        String sql = laNhom
                ? "SELECT id, nguoi_gui, nguoi_nhan, la_nhom, noi_dung, thoi_gian_gui " +
                "FROM tin_nhan WHERE nguoi_nhan = ? AND la_nhom = TRUE ORDER BY thoi_gian_gui"
                : "SELECT id, nguoi_gui, nguoi_nhan, la_nhom, noi_dung, thoi_gian_gui " +
                "FROM tin_nhan " +
                "WHERE ((nguoi_gui = ? AND nguoi_nhan = ?) OR (nguoi_gui = ? AND nguoi_nhan = ?)) " +
                "AND la_nhom = FALSE ORDER BY thoi_gian_gui";

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

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String nguoiGui = rs.getString("nguoi_gui");
                    String nguoiNhan = rs.getString("nguoi_nhan");
                    boolean isGroup = rs.getBoolean("la_nhom");
                    String noiDung = rs.getString("noi_dung");

                    ChatMessage msg = new ChatMessage(
                            id,                       // <-- có id
                            nguoiGui,
                            "",                       // displayName không lưu DB → để trống
                            nguoiNhan,
                            noiDung,
                            isGroup,
                            isGroup ? nguoiNhan : null
                    );
                    ds.add(msg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Xóa tin nhắn CHO TẤT CẢ theo id.
     * Chỉ cho phép người gửi (requester) xóa.
     */
    public static boolean xoaTinNhanChoTatCa(long messageId, String requester) {
        String check = "SELECT nguoi_gui FROM tin_nhan WHERE id = ?";
        String del   = "DELETE FROM tin_nhan WHERE id = ?";

        try (Connection conn = Database.getConnected();
             PreparedStatement st1 = conn.prepareStatement(check)) {

            st1.setLong(1, messageId);
            try (ResultSet rs = st1.executeQuery()) {
                if (!rs.next()) return false;                 // không tồn tại
                String sender = rs.getString(1);
                if (!requester.equals(sender)) return false;  // không phải người gửi
            }

            try (PreparedStatement st2 = conn.prepareStatement(del)) {
                st2.setLong(1, messageId);
                return st2.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * (Tuỳ chọn) Xóa cho một người — chỉ nên dùng khi có cơ chế ẩn theo user (bảng khác).
     * Nếu xoá trực tiếp bảng tin_nhan thì đang xoá cho TẤT CẢ mọi người.
     * Giữ lại comment để bạn tham khảo, mặc định KHÔNG dùng:
     *
     * public static boolean xoaTinNhanChoMotNguoi(long messageId, String username) {
     *     // Cần thêm bảng phụ, ví dụ: tin_nhan_hidden(user, message_id)
     *     return false;
     * }
     */
}
