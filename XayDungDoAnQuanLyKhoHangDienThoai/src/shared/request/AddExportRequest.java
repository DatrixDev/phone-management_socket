package shared.request;

import shared.models.phieuXuat;
import shared.models.ChiTietPhieuXuatDTO;

import java.io.Serializable;
import java.util.List;

public class AddExportRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private phieuXuat phieuXuat;
    private List<ChiTietPhieuXuatDTO> chiTietList;

    public AddExportRequest(phieuXuat phieuXuat, List<ChiTietPhieuXuatDTO> chiTietList) {
        this.phieuXuat = phieuXuat;
        this.chiTietList = chiTietList;
    }

    public phieuXuat getPhieuXuat() {
        return phieuXuat;
    }

    public List<ChiTietPhieuXuatDTO> getChiTietList() {
        return chiTietList;
    }
} 