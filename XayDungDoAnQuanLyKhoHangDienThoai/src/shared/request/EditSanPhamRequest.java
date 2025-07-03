package shared.request;

import shared.models.SanPham;

import java.io.Serializable;

public class EditSanPhamRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private  SanPham sanPham;
    private int productID;


    public EditSanPhamRequest(SanPham sanPham,int productID){
        this.productID = productID;
        this.sanPham = sanPham;
    }

    public SanPham getSanPham() {
        return sanPham;
    }



    public int getProductID() {
        return productID;
    }


}
