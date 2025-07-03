package shared.models;

import java.io.Serializable;

public class KhuVucKho  implements Serializable {
    private static final long serialVersionUID = 1L;

    private int  makhuvuc;
    private String tenkhuvuc;
    private String toado;
    private String ghichu;
    private String hinhAnhPath;
    public KhuVucKho(){

    }

    public KhuVucKho(String ghichu,String hinhAnhPath,  String toado, String tenkhuvuc, int  makhuvuc) {
        this.ghichu = ghichu;
        this.toado = toado;
        this.tenkhuvuc = tenkhuvuc;
        this.makhuvuc = makhuvuc;
        this.hinhAnhPath = hinhAnhPath;
    }

    public int getMakhuvuc() {
        return makhuvuc;
    }

    public void setMakhuvuc(int  makhuvuc) {
        this.makhuvuc = makhuvuc;
    }

    public String getTenkhuvuc() {
        return tenkhuvuc;
    }

    public void setTenkhuvuc(String tenkhuvuc) {
        this.tenkhuvuc = tenkhuvuc;
    }

    public String getToado() {
        return toado;
    }

    public void setToado(String toado) {
        this.toado = toado;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getHinhAnhPath() {
        return hinhAnhPath;
    }

    public void setHinhAnhPath(String hinhAnhPath) {
        this.hinhAnhPath = hinhAnhPath;
    }
}
