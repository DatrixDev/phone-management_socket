package shared.models;

import java.io.Serializable;

public class ram  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int madlram;
    private int kichthuocram;

    public ram(int madlram, int kichthuocram) {
        this.madlram = madlram;
        this.kichthuocram = kichthuocram;
    }
    public ram(){}

    public int getMadlram() {
        return madlram;
    }

    public void setMadlram(int madlram) {
        this.madlram = madlram;
    }

    public int getKichthuocram() {
        return kichthuocram;
    }

    public void setKichthuocram(int kichthuocram) {
        this.kichthuocram = kichthuocram;
    }

    @Override
    public String toString() {
        return kichthuocram + " GB";
    }
}
