package shared.models;

import java.io.Serializable;

public class mausac implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mamau;
    private String tenmau;

    public mausac(int mamau, String tenmau) {
        this.mamau = mamau;
        this.tenmau = tenmau;
    }
    public mausac(){};

    public int getMamau() {
        return mamau;
    }

    public void setMamau(int mamau) {
        this.mamau = mamau;
    }

    public String getTenmau() {
        return tenmau;
    }

    public void setTenmau(String tenmau) {
        this.tenmau = tenmau;
    }


    @Override

    public String toString() {
        return tenmau;
    }
}
