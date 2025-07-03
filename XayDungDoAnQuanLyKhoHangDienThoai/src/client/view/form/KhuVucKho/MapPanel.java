package client.view.form.KhuVucKho;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;
import server.DAO.KhuVucKhoDAO;
import shared.models.KhuVucKho;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class  MapPanel extends JPanel {

    private final JXMapViewer mapViewer;
    private final GeoPosition truongVietHan = new GeoPosition(15.9755, 108.2531);
    private Map<DefaultWaypoint, KhuVucKho> waypointsInfo = new HashMap<>();
    private WaypointPainter<Waypoint> painter = new WaypointPainter<>();

    public MapPanel() {
        setLayout(new BorderLayout());

        mapViewer = new JXMapViewer();

        // Cấu hình bản đồ
        OSMTileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        mapViewer.setAddressLocation(truongVietHan);
        mapViewer.setZoom(3);

        updateMapFromDatabase();

        // Sự kiện click để hiển thị thông tin
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickedPoint = e.getPoint();

                for (DefaultWaypoint wp : waypointsInfo.keySet()) {
                    Point2D markerPosition = mapViewer.getTileFactory()
                            .geoToPixel(wp.getPosition(), mapViewer.getZoom());
                    Rectangle rect = mapViewer.getViewportBounds();

                    int markerX = (int) (markerPosition.getX() - rect.getX());
                    int markerY = (int) (markerPosition.getY() - rect.getY() - 30);

                    if (clickedPoint.distance(markerX, markerY) < 15) {
                        KhuVucKho kho = waypointsInfo.get(wp);

                        // Tạo giao diện popup tùy chỉnh
                        JPanel panel = new JPanel(new BorderLayout(10, 10));

                        // Thêm ảnh
                        try {
                            ImageIcon icon = new ImageIcon(kho.getHinhAnhPath());
                            Image scaledImage = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                            panel.add(imageLabel, BorderLayout.WEST);
                        } catch (Exception ex) {
                            panel.add(new JLabel("Không thể tải ảnh"), BorderLayout.WEST);
                        }

                        // Thêm thông tin
                        JPanel textPanel = new JPanel(new GridLayout(0, 1));
                        textPanel.add(new JLabel("Tên kho: " + kho.getTenkhuvuc()));
                        textPanel.add(new JLabel("Địa chỉ: " + kho.getToado()));
                        panel.add(textPanel, BorderLayout.CENTER);

                        JOptionPane.showMessageDialog(mapViewer, panel, "Thông tin vị trí", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        });


        // Sự kiện di chuột để thay đổi con trỏ
        mapViewer.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePt = e.getPoint();
                boolean onMarker = false;

                for (DefaultWaypoint wp : waypointsInfo.keySet()) {
                    Point2D markerPos = mapViewer.getTileFactory()
                            .geoToPixel(wp.getPosition(), mapViewer.getZoom());
                    Rectangle rect = mapViewer.getViewportBounds();

                    int markerX = (int) (markerPos.getX() - rect.getX());
                    int markerY = (int) (markerPos.getY() - rect.getY() - 30);

                    if (mousePt.distance(markerX, markerY) < 15) {
                        onMarker = true;
                        break;
                    }
                }

                mapViewer.setCursor(onMarker
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
            }
        });

        // Zoom & kéo
        MouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        // Nút làm mới
        JButton btnReset = new JButton("Làm mới");
        btnReset.addActionListener(e -> {
            mapViewer.setAddressLocation(truongVietHan);
            mapViewer.setZoom(3);
        });

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(btnReset);

        // Thêm vào panel
        add(controlPanel, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
    }
    public void updateMapFromDatabase() {
        List<KhuVucKho> danhSach = KhuVucKhoDAO.getAllkhuVuc();

        // Xóa dữ liệu cũ
        waypointsInfo.clear();

        for (KhuVucKho khu : danhSach) {
            try {
                String[] parts = khu.getToado().split(",");
                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());

                DefaultWaypoint wp = new DefaultWaypoint(lat, lon); // Tạo 1 waypoint duy nhất
                waypointsInfo.put(wp, khu); // Lưu key chính xác

            } catch (Exception e) {
                System.err.println("Lỗi tọa độ: " + khu.getToado());
            }
        }

        Set<Waypoint> waypoints = new HashSet<>(waypointsInfo.keySet());
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);

        mapViewer.repaint();
    }

}
