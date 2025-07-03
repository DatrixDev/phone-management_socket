package client.BUS;

import server.DAO.ChiTietQuyenDAO;
import server.DAO.NhomQuyenDAO;
import shared.models.ChiTietQuyen;
import shared.models.NhomQuyen;

import java.util.ArrayList;

public class NhomQuyenBUS {

    private final ChiTietQuyenDAO chitietquyenDAO = new ChiTietQuyenDAO();
    private final NhomQuyenDAO nhomquyenDAO = new NhomQuyenDAO();
    private ArrayList<NhomQuyen> listNhomQuyen = new ArrayList<>();


    public NhomQuyenBUS() {
        listNhomQuyen = nhomquyenDAO.selectAll();
    }

    public ArrayList<NhomQuyen> getAll() {
        return this.listNhomQuyen;
    }

    public NhomQuyen getByIndex(int index) {
        return this.listNhomQuyen.get(index);
    }

    public boolean add(String nqdto, ArrayList<ChiTietQuyen> ctquyen) {
        NhomQuyen nq = new NhomQuyen(nhomquyenDAO.getAutoIncrement(), nqdto);
        boolean check = nhomquyenDAO.insert(nq) != 0;
        if (check) {
            this.listNhomQuyen.add(nq);
            this.addChiTietQuyen(ctquyen);
        }
        return check;
    }

    public boolean update(NhomQuyen nhomquyen, ArrayList<ChiTietQuyen> chitietquyen,int index) {
        boolean check = nhomquyenDAO.update(nhomquyen) != 0;
        if (check) {
            this.removeChiTietQuyen(Integer.toString(nhomquyen.getManhomquyen()));
            this.addChiTietQuyen(chitietquyen);
            this.listNhomQuyen.set(index, nhomquyen);
        }
        return check;
    }

    public boolean delete(NhomQuyen nqdto) {
        boolean check = nhomquyenDAO.delete(Integer.toString(nqdto.getManhomquyen())) != 0;
        if (check) {
            this.listNhomQuyen.remove(nqdto);
        }
        return check;
    }

    public ArrayList<ChiTietQuyen> getChiTietQuyen(String manhomquyen) {
        return chitietquyenDAO.selectAll(manhomquyen);
    }

    public boolean addChiTietQuyen(ArrayList<ChiTietQuyen> listctquyen) {
        boolean check = chitietquyenDAO.insert(listctquyen) != 0;
        return check;
    }

    public boolean removeChiTietQuyen(String manhomquyen) {
        boolean check = chitietquyenDAO.delete(manhomquyen) != 0;
        return check;
    }

    public boolean checkPermisson(int maquyen, String chucnang, String hanhdong) {
        ArrayList<ChiTietQuyen> ctquyen = this.getChiTietQuyen(Integer.toString(maquyen));
        boolean check = false;
        int i = 0;
        while (i < ctquyen.size() && check==false) {
            if(ctquyen.get(i).getMachucnang().equals(chucnang) && ctquyen.get(i).getHanhdong().equals(hanhdong)) {
                check = true;
            } else {
                i++;
            }
        }
        return check;
    }

    public ArrayList<NhomQuyen> search(String text) {
        ArrayList<NhomQuyen> result = new ArrayList<>();
        for(NhomQuyen i : this.listNhomQuyen) {
            if(Integer.toString(i.getManhomquyen()).contains(text) || i.getTennhomquyen().toLowerCase().contains(text.toLowerCase())) {
                result.add(i);
            }
        }
        return result;
    }

    public void reloadFromDB() {
        this.listNhomQuyen = nhomquyenDAO.selectAll();
    }
}
