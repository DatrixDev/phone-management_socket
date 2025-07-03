package client.BUS;

import server.DAO.ThongKeDAO;
import shared.models.ThongKe.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ThongKeBUS {

    ThongKeDAO thongkeDAO = new ThongKeDAO();
    ArrayList<ThongKeKhachHang> tkkh;
    HashMap<Integer, ArrayList<ThongKeTonKho>> listTonKho;

    public ThongKeBUS() {
        listTonKho = ThongKeDAO.getThongKeTonKho("",new Date(0), new Date(System.currentTimeMillis()));
    }

    public ArrayList<ThongKeKhachHang> getAllKhachHang() {
        this.tkkh = ThongKeDAO.getThongKeKhachHang("",new Date(0), new Date(System.currentTimeMillis()));
        return this.tkkh;
    }

    public ArrayList<ThongKeKhachHang> FilterKhachHang(String text,Date start, Date end) {
        this.tkkh = ThongKeDAO.getThongKeKhachHang(text,start, end);
        return this.tkkh;
    }



    public HashMap<Integer, ArrayList<ThongKeTonKho>> getTonKho() {
        return this.listTonKho;
    }

    public HashMap<Integer, ArrayList<ThongKeTonKho>> filterTonKho(String text, Date time_start, Date time_end) {
        HashMap<Integer, ArrayList<ThongKeTonKho>> result = ThongKeDAO.getThongKeTonKho(text, time_start, time_end);
        return result;
    }

    public int[] getSoluong(ArrayList<ThongKeTonKho> list) {
        int[] result = {0, 0, 0, 0};
        for (int i = 0; i < list.size(); i++) {
            result[0] += list.get(i).getTondauky();
            result[1] += list.get(i).getNhaptrongky();
            result[2] += list.get(i).getXuattrongky();
            result[3] += list.get(i).getToncuoiky();
        }
        return result;
    }

    public ArrayList<ThongKeDoanhThu> getDoanhThuTheoTungNam(int year_start, int year_end) {
        return this.thongkeDAO.getDoanhThuTheoTungNam(year_start, year_end);
    }

    public ArrayList<ThongKeTheoThang> getThongKeTheoThang(int nam){
        return thongkeDAO.getThongKeTheoThang(nam);
    }

    public ArrayList<ThongKeTungNgayTrongThang> getThongKeTungNgayTrongThang(int thang, int nam){
        return thongkeDAO.getThongKeTungNgayTrongThang(thang, nam);
    }

    public ArrayList<ThongKeTungNgayTrongThang> getThongKeTuNgayDenNgay(String start, String end){
        return thongkeDAO.getThongKeTuNgayDenNgay(start, end);
    }

    public ArrayList<ThongKeTungNgayTrongThang> getThongKe7NgayGanNhat(){
        return thongkeDAO.getThongKe7NgayGanNhat();
    }
}
