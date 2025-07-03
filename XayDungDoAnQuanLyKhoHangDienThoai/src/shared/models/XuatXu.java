package shared.models;

import java.io.Serializable;

public class XuatXu implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String ten;

    public XuatXu(int id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public XuatXu() {

    }

    public int getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return ten;
    }
}
