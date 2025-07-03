package server.handler;

import server.Server;
import server.DAO.SanPhamDAO;
import server.DAO.cauHinhDAO;
import shared.models.SanPham;
import shared.request.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ProductHandler {
    private static final SanPhamDAO dao = new SanPhamDAO();

    public static void sendAllProducts(ObjectOutputStream oos) {
        try {
            oos.reset();
            oos.writeObject(dao.getAllSanPham());
            oos.flush();
        } catch (IOException e) {
            System.err.println("[ProductHandler] Error sending products: " + e.getMessage());
        }
    }

    public static synchronized void handleAddFullProduct(AddFullProductRequest request, ObjectOutputStream oos) {
        try {
            int masp = dao.themSanPham(request.getSanPham());
            cauHinhDAO.themCauHinh(request.getCauHinh(), masp);
            oos.writeObject("success");
            oos.flush();
            broadcastProducts();
        } catch (Exception ex) {
            sendFail(oos, ex.getMessage());
        }
    }

    public static synchronized void handleDeleteProduct(DeleteProductRequest request, ObjectOutputStream oos) {
        try {
            dao.xoaSanPham(request.getProductId());
            oos.writeObject("success");
            oos.flush();
            broadcastProducts();
        } catch (Exception ex) {
            sendFail(oos, ex.getMessage());
        }
    }

    public static synchronized void handleEditSanPham(EditSanPhamRequest request, ObjectOutputStream oos) {
        try {
            dao.suaSanPham(request.getSanPham(), request.getProductID());
            oos.writeObject("success");
            oos.flush();
            broadcastProducts();
        } catch (Exception ex) {
            sendFail(oos, ex.getMessage());
        }
    }

    public static synchronized void handleEditCauHinh(EditCauhinhRequest request, ObjectOutputStream oos) {
        try {
            cauHinhDAO.suaCauHinh(request.getCauHinh(), request.getProductId());
            oos.writeObject("success");
            oos.flush();
            broadcastProducts();
        } catch (Exception ex) {
            sendFail(oos, ex.getMessage());
        }
    }

    private static void broadcastProducts() {
        try {
            List<SanPham> list = dao.getAllSanPham();
            for (ClientHandler handler : Server.clientHandlers) {
                handler.send(list);
            }
        } catch (Exception e) {
            System.err.println("[ProductHandler] broadcast failed: " + e.getMessage());
        }
    }

    private static void sendFail(ObjectOutputStream oos, String message) {
        try {
            oos.writeObject("fail: " + message);
            oos.flush();
        } catch (IOException e) {
            System.err.println("[ProductHandler] Error sending fail: " + e.getMessage());
        }
    }
}
