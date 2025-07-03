package client.view.views;

import client.view.shared.BaseView;
import client.view.shared.TopPanel;
import server.DAO.SanPhamDAO;
import client.controller.ProductController;
import shared.models.SanPham;

import javax.swing.*;
import javax.swing.table.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import network.SocketManager;

import java.awt.*;

public class ProductView extends BaseView {
    private JTable table;
    private final TopPanel topPanel;
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private volatile boolean isProcessingRequest = false;
    SocketManager sm = SocketManager.getInstance();


    public ProductView() throws Exception {
        super();

        topPanel = new TopPanel();

        String[] searchOptions = {"Tất cả","Tên sản phẩm","Thương hiệu","Khu vực kho"};
        topPanel.getCbxChoose().setModel(new DefaultComboBoxModel<>(searchOptions));
        new ProductController(this);


        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(230, 230, 230));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        containerPanel.add(topPanel, BorderLayout.NORTH);
        setupConnection();

        String[] columns = {
                "Mã SP", "Tên sản phẩm", "Số lượng tồn", "Thương hiệu",
                "Hệ điều hành", "Kích thước màn", "Chip xử lý",
                "Dung lượng pin", "Xuất xứ", "Khu vực kho"
        };
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Giả sử cột 0 là Mã SP
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table = new JTable(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        sorter.toggleSortOrder(0);


        // Tắt viền ngoài
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(245, 245, 245)); // Màu nền nhạt cho header
        header.setFont(new Font("Arial", Font.BOLD, 13));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 230, 230));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        containerPanel.add(tablePanel, BorderLayout.CENTER);

        getMainPanel().add(containerPanel, BorderLayout.CENTER);
        table.setBackground(Color.WHITE);
        updateTable(sanPhamDAO.getAllSanPham());
        sm.setProductListListener(list -> updateTable(list));



    }



 public JPanel getContentPanel() {

        return mainPanel;
    }

    private void setupConnection() {
        try {
            SocketManager manager = SocketManager.getInstance();
            socket = manager.getSocket();
            oos = manager.getOutputStream();
            ois = manager.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối tới server!");
        }

    }

    public void reconnect() {
        try {
            closeResources();
            setupConnection();
            if (socket != null && socket.isConnected()) {
                System.out.println("Đã kết nối lại tới server");

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối lại tới server!");
        }
    }


//
//    private void startListening() {
//        new Thread(() -> {
//            while (socket != null && !socket.isClosed()) {
//                try {
//                    if (!isProcessingRequest) {
//                        Object obj;
//                        synchronized (ois) {
//                            obj = ois.readObject();
//                        }
//
//                        if (obj instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof SanPham) {
//                            SwingUtilities.invokeLater(() -> updateTable((List<SanPham>) list));
//                        }
//                    } else {
//                        Thread.sleep(100);
//                    }
//                } catch (SocketException e) {
//                    System.out.println("Connection lost, attempting to reconnect...");
//                    SwingUtilities.invokeLater(this::reconnect);
//                    break;
//                } catch (StreamCorruptedException e) {
//                    System.out.println("Stream corrupted, resetting connection...");
//                    SwingUtilities.invokeLater(this::reconnect);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//            }
//        }).start();
//    }



    private void closeResources() {
        try {
            if (oos != null) oos.close();
            if (ois != null) ois.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void updateTable(List<SanPham> list) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (SanPham sp : list) {
            model.addRow(new Object[]{
                    sp.getId(),
                    sp.getTenSanPham(),
                    sp.getSoLuong(),
                    sp.getThuongHieu(),
                    sp.getHeDieuHanh(),
                    sp.getKichThuocMan(),
                    sp.getChip(),
                    sp.getDungLuongPin(),
                    sp.getXuatXu(),
                    sp.getKhuVucKho()
            });
        }
    }
    public ObjectOutputStream getOutputStream() {
        return oos;
    }

    public ObjectInputStream getInputStream() {
        return ois;
    }

    public void setProcessingRequest(boolean processing) {
        this.isProcessingRequest = processing;
    }

    public JTable getTable() {
        return table;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }




}