package client.helper;

import network.SocketManager;
import shared.models.phieuNhap;
import shared.models.phieuXuat;
import shared.models.ChiTietPhieuNhapDTO;
import shared.models.ChiTietPhieuXuatDTO;
import shared.request.*;

import java.util.List;

/**
 * Helper class để demo cách sử dụng cấu trúc server-client cho phiếu nhập/xuất
 * Thay thế cho việc gọi trực tiếp DAO từ Controller
 */
public class ImportExportClientHelper {
    private SocketManager socketManager;

    public ImportExportClientHelper() throws Exception {
        this.socketManager = SocketManager.getInstance();
    }

    /**
     * Thêm phiếu nhập mới thông qua server
     */
    public String addImport(phieuNhap phieuNhap, List<ChiTietPhieuNhapDTO> chiTietList) {
        try {
            socketManager.setProcessingRequest(true);
            
            // Gửi request đến server
            AddImportRequest request = new AddImportRequest(phieuNhap, chiTietList);
            socketManager.send(request);
            
            // Nhận phản hồi từ server
            Object response = socketManager.receive();
            socketManager.setProcessingRequest(false);
            
            if (response instanceof String) {
                return (String) response;
            } else {
                return "fail: Phản hồi không hợp lệ từ server";
            }
            
        } catch (Exception e) {
            socketManager.setProcessingRequest(false);
            return "fail: " + e.getMessage();
        }
    }

    /**
     * Thêm phiếu xuất mới thông qua server
     */
    public String addExport(phieuXuat phieuXuat, List<ChiTietPhieuXuatDTO> chiTietList) {
        try {
            socketManager.setProcessingRequest(true);
            
            // Gửi request đến server
            AddExportRequest request = new AddExportRequest(phieuXuat, chiTietList);
            socketManager.send(request);
            
            // Nhận phản hồi từ server
            Object response = socketManager.receive();
            socketManager.setProcessingRequest(false);
            
            if (response instanceof String) {
                return (String) response;
            } else {
                return "fail: Phản hồi không hợp lệ từ server";
            }
            
        } catch (Exception e) {
            socketManager.setProcessingRequest(false);
            return "fail: " + e.getMessage();
        }
    }

    /**
     * Lấy danh sách phiếu nhập từ server
     */
    @SuppressWarnings("unchecked")
    public List<phieuNhap> getImportList() {
        try {
            socketManager.setProcessingRequest(true);
            
            // Gửi request đến server
            GetImportListRequest request = new GetImportListRequest();
            socketManager.send(request);
            
            // Nhận phản hồi từ server
            Object response = socketManager.receive();
            socketManager.setProcessingRequest(false);
            
            if (response instanceof List<?>) {
                return (List<phieuNhap>) response;
            } else {
                System.err.println("Phản hồi không hợp lệ từ server: " + response);
                return null;
            }
            
        } catch (Exception e) {
            socketManager.setProcessingRequest(false);
            System.err.println("Lỗi khi lấy danh sách phiếu nhập: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy danh sách phiếu xuất từ server
     */
    @SuppressWarnings("unchecked")
    public List<phieuXuat> getExportList() {
        try {
            socketManager.setProcessingRequest(true);
            
            // Gửi request đến server
            GetExportListRequest request = new GetExportListRequest();
            socketManager.send(request);
            
            // Nhận phản hồi từ server
            Object response = socketManager.receive();
            socketManager.setProcessingRequest(false);
            
            if (response instanceof List<?>) {
                return (List<phieuXuat>) response;
            } else {
                System.err.println("Phản hồi không hợp lệ từ server: " + response);
                return null;
            }
            
        } catch (Exception e) {
            socketManager.setProcessingRequest(false);
            System.err.println("Lỗi khi lấy danh sách phiếu xuất: " + e.getMessage());
            return null;
        }
    }
} 