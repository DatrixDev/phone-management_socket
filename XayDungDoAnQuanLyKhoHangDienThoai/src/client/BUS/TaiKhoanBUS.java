package client.BUS;

import server.DAO.NhomQuyenDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;

import java.util.ArrayList;

public class TaiKhoanBUS {
    private ArrayList<TaiKhoan> listTaiKhoan;
    private ArrayList<NhomQuyen> listNhomQuyen;
    public TaiKhoanDAO taiKhoanDAO = TaiKhoanDAO.getInstance();
    private NhomQuyenDAO nhomQuyenDAO = NhomQuyenDAO.getInstance();

    public TaiKhoanBUS(){
        this.listTaiKhoan  = TaiKhoanDAO.getInstance().selectAll();
        this.listNhomQuyen = NhomQuyenDAO.getInstance().selectAll();
    }

    public ArrayList<TaiKhoan> getTaiKhoanAll(){
        return listTaiKhoan;
    }

    public TaiKhoan getTaiKhoan(int index){
        return listTaiKhoan.get(index);
    }
    public int getTaiKhoanByMaNV(int manv){
        int i = 0;
        int vitri = -1;
        while (i < this.listTaiKhoan.size() && vitri == -1) {
            if (listTaiKhoan.get(i).getManv()== manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public NhomQuyen getNhomQuyenDTO(int manhom){
        return nhomQuyenDAO.selectById(manhom+"");
    }

    public void addAcc(TaiKhoan tk){
        listTaiKhoan.add(tk);
    }

    public void updateAcc(int index, TaiKhoan tk){
        listTaiKhoan.set(index, tk);
    }

    public void deleteAcc(int manv){

    }
    public ArrayList<TaiKhoan> search(String txt, String type) {
        ArrayList<TaiKhoan> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (TaiKhoan i : listTaiKhoan) {
                    if (Integer.toString(i.getManv()).contains(txt) || i.getUsername().contains(txt) ) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhân viên" -> {
                for (TaiKhoan i : listTaiKhoan) {
                    if (Integer.toString(i.getManv()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Username" -> {
                for (TaiKhoan i : listTaiKhoan) {
                    if (i.getUsername().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public void reloadFromDB() {
        this.listTaiKhoan = taiKhoanDAO.selectAll();
    }

}
