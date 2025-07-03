package shared.request;

import shared.models.cauHinhSanPham;

import java.io.Serializable;
//
public class EditCauhinhRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private int productId;
    private cauHinhSanPham cauHinh;

    public EditCauhinhRequest(int productId,cauHinhSanPham cauHinh){
        this.cauHinh = cauHinh;
        this.productId = productId;

    }

    public int getProductId() {
        return productId;
    }



    public cauHinhSanPham getCauHinh() {
        return cauHinh;
    }


}
