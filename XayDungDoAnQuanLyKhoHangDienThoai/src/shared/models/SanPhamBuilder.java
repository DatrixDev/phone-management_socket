package shared.models;

import java.io.Serializable;

public class SanPhamBuilder implements Serializable {
    public static SanPham sanPhamTam = new SanPham();

    public static void reset() {
        sanPhamTam = new SanPham();
    }
}