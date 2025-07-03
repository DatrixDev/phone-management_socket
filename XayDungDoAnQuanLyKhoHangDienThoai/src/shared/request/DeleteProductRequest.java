package shared.request;

import java.io.Serializable;

public class DeleteProductRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String action;
    private int productId;

    public DeleteProductRequest(String action, int productId) {
        this.action = action;
        this.productId = productId;
    }

    public String getAction() {
        return action;
    }

    public int getProductId() {
        return productId;
    }
}