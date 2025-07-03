package client.view.views.ThongKe;

import client.helper.Formater;
import client.view.components.Chart.CurveChart.CurveChart;
import client.view.components.Chart.CurveChart.ModelChart2;
import client.view.components.TableSorter;
import client.BUS.ThongKeBUS;
import server.DAO.KhachHangDAO;
import server.DAO.NhanVienDAO;
import server.DAO.SanPhamDAO;
import shared.models.ThongKe.ThongKeTungNgayTrongThang;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ThongKeTongQuan extends JPanel {
    ThongKeBUS thongKeBUS;
    ArrayList<ThongKeTungNgayTrongThang> dataset;
    CurveChart chart;
    private Component panelBox1, panelBox2, panelBox3;
    private boolean isSelected;
    private JPanel pnlChart;
    private JTable table;
    private DefaultTableModel model;

    public ThongKeTongQuan(ThongKeBUS thongKeBUS) {
        this.thongKeBUS = thongKeBUS;
        this.dataset = thongKeBUS.getThongKe7NgayGanNhat();
        initComponent();
        loadDataTalbe(this.dataset);
    }

    public void loadDataChart() {
        for (ThongKeTungNgayTrongThang i : dataset) {
            chart.addData(new ModelChart2(i.getNgay() + "", new double[]{i.getChiphi(), i.getDoanhthu(), i.getLoinhuan()}));
        }
    }

    public void loadDataTalbe(ArrayList<ThongKeTungNgayTrongThang> list) {
        model.setRowCount(0);
        for (ThongKeTungNgayTrongThang i : dataset) {
            model.addRow(new Object[]{
                    i.getNgay(), Formater.FormatVND(i.getChiphi()), Formater.FormatVND(i.getDoanhthu()), Formater.FormatVND(i.getLoinhuan())
            });
        }
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
//        panel.setBackground(Color.decode("#F0F7FA"));

        // Panel Container
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelContainer.setBackground(Color.decode("#F0F7FA"));

        // Panel Container/Panel Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Panel Container/Panel Header/Panel Grid
        JPanel panelGrid = new JPanel(new GridLayout(1, 3, 17, 0));
        panelGrid.setOpaque(false);

        panelBox1 = createInfoBox("/images/iconSmartphone.png", Integer.toString(SanPhamDAO.getAllSanPham().size()), "Sản phẩm hiện có trong kho");
        panelBox2 = createInfoBox("/images/customer.png", Integer.toString(KhachHangDAO.getInstance().selectAll().size()), "Khách từ trước đến nay");
        panelBox3 = createInfoBox("/images/employee.png", Integer.toString(NhanVienDAO.getInstance().selectAll().size()), "Nhân viên đang hoạt động");

        // Thêm các thành phần
        panelGrid.add(panelBox1);
        panelGrid.add(panelBox2);
        panelGrid.add(panelBox3);

        panelHeader.add(panelGrid, BorderLayout.CENTER);


        // Panel Container/Panel Section
        JPanel panelSection = new JPanel();
        panelSection.setLayout(new BoxLayout(panelSection, BoxLayout.Y_AXIS));
        panelSection.setBackground(Color.decode("#F0F7FA"));


        // Panel Container/Panel Section/Panel Center (Biểu đồ)
        JPanel panelCenter = new JPanel(new BorderLayout());
        JPanel panelCenter_top = new JPanel(new FlowLayout());
        panelCenter_top.setBorder(new EmptyBorder(10, 0, 0, 10));
        panelCenter_top.setOpaque(false);
        JLabel txtChartName = new JLabel("Thống kê doanh thu 8 ngày gần nhất");
        txtChartName.putClientProperty("FlatLaf.style", "font: 200% $medium.font");
        panelCenter_top.add(txtChartName);

        pnlChart = new JPanel();
        pnlChart.setOpaque(false);
        pnlChart.setLayout(new BorderLayout(0, 0));
        chart = new CurveChart();
        chart.addLegend("Vốn", new Color(12, 84, 175), new Color(0, 108, 247));
        chart.addLegend("Doanh thu", new Color(54, 4, 143), new Color(104, 49, 200));
        chart.addLegend("Lợi nhuận", new Color(211, 84, 0), new Color(230, 126, 34));
        loadDataChart();
        chart.start();
        pnlChart.add(chart, BorderLayout.CENTER);

        panelCenter.add(panelCenter_top, BorderLayout.NORTH);
        panelCenter.add(pnlChart, BorderLayout.CENTER);

        // Panel Container/Panel Section/Panel Footer (Bảng)
        JPanel panelFooter = new JPanel(new BorderLayout());
        table = new JTable();
        JTableHeader headerTable = table.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getPreferredSize().width, 35)); // tăng chiều cao thanh header
        headerTable.setBackground(new Color(245, 245, 245)); // Màu nền header
        headerTable.setForeground(Color.BLACK);                      // Màu chữ
        headerTable.setFont(new Font("Arial", Font.BOLD, 13));       // Font chữ
        headerTable.setReorderingAllowed(false);  // Không cho kéo đổi vị trí cột
        headerTable.setResizingAllowed(false);    // Không cho resize
        headerTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setRowHeight(40); // tăng chiều cao các hàng
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chọn 1 hàng duy nhất
        table.setSelectionBackground(Color.decode("#D3D3D3"));
        table.setSelectionForeground(Color.black);
        JScrollPane scrollPane = new JScrollPane();
        model = new DefaultTableModel();
        String[] header = new String[]{"Ngày", "Vốn", "Doanh thu", "Lợi nhuận"};
        model.setColumnIdentifiers(header);
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        scrollPane.setViewportView(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setFocusable(false);
        table.setShowHorizontalLines(true);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.DATE_COMPARATOR);
        TableSorter.configureTableColumnSorter(table, 1, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(table, 2, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(table, 3, TableSorter.VND_CURRENCY_COMPARATOR);

        panelFooter.add(scrollPane, BorderLayout.CENTER);

        panelHeader.setBackground(Color.decode("#F0F7FA"));
        panelGrid.setBackground(Color.decode("#F0F7FA"));
        panelCenter.setBackground(Color.decode("#F0F7FA"));
        panelFooter.setBackground(Color.decode("#F0F7FA")); // hoặc giữ màu khác nếu cần phân biệt

        panelSection.add(panelCenter);
        panelSection.add(Box.createVerticalStrut(10));
        panelSection.add(panelFooter);

        panelContainer.add(panelHeader, BorderLayout.NORTH);
        panelContainer.add(panelSection, BorderLayout.CENTER);

        this.add(panelContainer, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);

    }

    private JPanel createInfoBox(String iconPath, String numberText, String descriptionText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(220, 100)); // Kích thước đẹp

        // Panel Container
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24)); // Padding
        panelContainer.setBackground(Color.WHITE);

        // Panel Container/Panel Icon
        JPanel panelIcon = new JPanel(new BorderLayout());

        // Panel Container/Panel Icon/Label Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Bo nhẹ viền trắng

        // Load icon
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledIcon = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(scaledIcon));

        panelIcon.add(iconLabel, BorderLayout.CENTER);

        // Panel Text
        JPanel panelText = new JPanel();
        panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
        panelText.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelText.setBackground(Color.white);

        // Panel Text/Label numberLabel
        JLabel numberLabel = new JLabel(numberText);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 32));
        numberLabel.setForeground(Color.decode("#708081")); // Xám đậm

        // Panel Text/Label descriptionLabel
        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.decode("#708081")); // Xám nhạt

        panelText.add(numberLabel);
        panelText.add(Box.createVerticalStrut(4));
        panelText.add(descriptionLabel);


        panelContainer.add(panelIcon, BorderLayout.WEST);
        panelContainer.add(panelText, BorderLayout.CENTER);

        panel.add(panelContainer, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    setBackground(new Color(235, 237, 240));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    setBackground(new Color(255, 255, 255));
                }
            }
        });

        return panel;
    }
}
