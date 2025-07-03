package shared.models;

import java.io.Serializable;

public class rom implements Serializable {
    private static final long serialVersionUID = 1L;
    private int madlrom;
    private int kicthuocrom;

    public rom(int madlrom, int kicthuocrom) {
        this.madlrom = madlrom;
        this.kicthuocrom = kicthuocrom;
    }

    public rom () {}

    public int getMadlrom() {
        return madlrom;
    }

    public void setMadlrom(int madlrom) {
        this.madlrom = madlrom;
    }

    public int getKicthuocrom() {
        return kicthuocrom;
    }

    public void setKicthuocrom(int kicthuocrom) {
        this.kicthuocrom = kicthuocrom;
    }

    @Override
    public String toString() {
        return kicthuocrom + " GB";
    }
}
