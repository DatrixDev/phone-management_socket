package shared.request;

import shared.models.phieuNhap;
import shared.models.ChiTietPhieuNhapDTO;

import java.io.Serializable;
import java.util.List;

public class AddImportRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private phieuNhap phieuNhap;
    private List<ChiTietPhieuNhapDTO> chiTietList;

    public AddImportRequest(phieuNhap phieuNhap, List<ChiTietPhieuNhapDTO> chiTietList) {
        this.phieuNhap = phieuNhap;
        this.chiTietList = chiTietList;
    }

    public phieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public List<ChiTietPhieuNhapDTO> getChiTietList() {
        return chiTietList;
    }
} 