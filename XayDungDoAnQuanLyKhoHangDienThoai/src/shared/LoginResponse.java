package shared;

import client.helper.BCrypt;
import server.DAO.dangNhapDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import shared.models.TaiKhoan;
import shared.request.LoginRequest;

public class LoginResponse implements java.io.Serializable {
    private boolean success;
    private String message;
    private NhanVien nhanVien;
    private TaiKhoan taiKhoan;

    public LoginResponse(boolean success, String message, NhanVien nhanVien, TaiKhoan taiKhoan) {
        this.success = success;
        this.message = message;
        this.nhanVien = nhanVien;
        this.taiKhoan = taiKhoan;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public NhanVien getNhanVien() { return nhanVien; }
    public TaiKhoan getTaiKhoan() { return taiKhoan; }

    // 👇 THÊM phương thức xử lý đăng nhập
    public static LoginResponse fromLoginRequest(LoginRequest loginRequest, TaiKhoanDAO taiKhoanDao) {
        var account = taiKhoanDao.selectByUser(loginRequest.getStaffId());

        if (account == null) {
            System.out.println("[Login] Không tìm thấy tài khoản cho: " + loginRequest.getStaffId());
            return new LoginResponse(false, "Tài khoản không tồn tại", null, null);
        }

        boolean passwordMatch = BCrypt.checkpw(loginRequest.getPassword(), account.getMatkhau());
        System.out.println("[Login] Mật khẩu DB (hash): " + account.getMatkhau());
        System.out.println("[Login] Mật khẩu đúng không? " + passwordMatch);

        if (!passwordMatch) {
            return new LoginResponse(false, "Sai mật khẩu", null, null);
        }

        if (account.getTrangthai() == 0) {
            return new LoginResponse(false, "Tài khoản bị khóa", null, null);
        }

        NhanVien nhanVien = dangNhapDAO.layThongTinNhanVien(account.getManv());
        return new LoginResponse(true, "Đăng nhập thành công", nhanVien, account);
    }
}