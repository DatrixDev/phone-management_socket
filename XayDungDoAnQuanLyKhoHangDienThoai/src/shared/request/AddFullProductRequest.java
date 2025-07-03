package shared.request;

import shared.models.SanPham;
import shared.models.cauHinhSanPham;

import java.io.Serializable;

public class AddFullProductRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private SanPham sanPham;
    private cauHinhSanPham cauHinh;

    public AddFullProductRequest(SanPham sanPham, cauHinhSanPham cauHinh) {
        this.sanPham = sanPham;
        this.cauHinh = cauHinh;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public cauHinhSanPham getCauHinh() {
        return cauHinh;
    }
}
