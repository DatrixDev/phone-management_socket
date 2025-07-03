package shared.models;

import java.io.Serializable;

public class thuonghieu implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mathuonghieu;
    private String tenthuonghieu;

    public thuonghieu(int mathuonghieu, String tenthuonghieu) {
        this.mathuonghieu = mathuonghieu;
        this.tenthuonghieu = tenthuonghieu;
    }

    public thuonghieu() {

    }

    public String getTenthuonghieu() {
        return tenthuonghieu;
    }

    public int getMathuonghieu() {
        return mathuonghieu;
    }

    public void setMathuonghieu(int mathuonghieu) {
        this.mathuonghieu = mathuonghieu;
    }

    public void setTenthuonghieu(String tenthuonghieu) {
        this.tenthuonghieu = tenthuonghieu;
    }

    @Override
    public String toString() {
        return tenthuonghieu;
    }
}
