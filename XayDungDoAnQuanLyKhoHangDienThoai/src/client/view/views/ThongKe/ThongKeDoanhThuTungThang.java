package client.view.views.ThongKe;

import client.helper.Formater;
import client.helper.JTableExporter;
import client.view.components.Chart.BarChart.Chart;
import client.view.components.Chart.BarChart.ModelChart;
import client.view.components.PanelBorderRadius;
import client.view.components.TableSorter;
import com.toedter.calendar.JYearChooser;

import client.BUS.ThongKeBUS;
import shared.models.ThongKe.ThongKeTheoThang;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class ThongKeDoanhThuTungThang extends JPanel implements ActionListener{

    PanelBorderRadius pnlChart;
    JPanel pnl_top;
    ThongKeBUS thongkeBUS;
    JYearChooser yearchooser;
    Chart chart;
    JButton export;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;

    public ThongKeDoanhThuTungThang(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
        loadThongKeThang(yearchooser.getYear());
    }

    public void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblChonNam = new JLabel("Chọn năm thống kê");
        yearchooser = new JYearChooser();
        yearchooser.addPropertyChangeListener("year", (PropertyChangeEvent e) -> {
            int year = (Integer) e.getNewValue();
            loadThongKeThang(year);
        });

        export = new JButton("Xuất Excel");
        export.addActionListener(this);
        pnl_top.add(lblChonNam);
        pnl_top.add(yearchooser);
        pnl_top.add(export);

        pnlChart = new PanelBorderRadius();
        BoxLayout boxly = new BoxLayout(pnlChart, BoxLayout.Y_AXIS);
        pnlChart.setLayout(boxly);
        chart = new Chart();
        chart.addLegend("Vốn", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        pnlChart.add(chart);
        tableThongKe = new JTable();
        JTableHeader headerTable = tableThongKe.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getPreferredSize().width, 35)); // tăng chiều cao thanh header
        headerTable.setBackground(new Color(245, 245, 245)); // Màu nền header
        headerTable.setForeground(Color.BLACK);                      // Màu chữ
        headerTable.setFont(new Font("Arial", Font.BOLD, 13));       // Font chữ
        headerTable.setReorderingAllowed(false);  // Không cho kéo đổi vị trí cột
        headerTable.setResizingAllowed(false);    // Không cho resize
        headerTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tableThongKe.setRowHeight(40); // tăng chiều cao các hàng
        tableThongKe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // chọn 1 hàng duy nhất
        tableThongKe.setSelectionBackground(Color.decode("#D3D3D3"));
        tableThongKe.setSelectionForeground(Color.black);
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Tháng", "Chi phí", "Doanh thu", "Lợi nhuận"};
        tblModel.setColumnIdentifiers(header);
        tableThongKe.setModel(tblModel);
        tableThongKe.setAutoCreateRowSorter(true);
        tableThongKe.setDefaultEditor(Object.class, null);
        scrollTableThongKe.setViewportView(tableThongKe);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tableThongKe.setShowHorizontalLines(true);
        tableThongKe.setFocusable(false);
        scrollTableThongKe.setPreferredSize(new Dimension(0, 300));

        TableSorter.configureTableColumnSorter(tableThongKe, 0, TableSorter.STRING_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 1, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 2, TableSorter.VND_CURRENCY_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableThongKe, 3, TableSorter.VND_CURRENCY_COMPARATOR);

        this.add(pnl_top, BorderLayout.NORTH);
        this.add(pnlChart, BorderLayout.CENTER);
        this.add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public void loadThongKeThang(int nam) {
        ArrayList<ThongKeTheoThang> list = thongkeBUS.getThongKeTheoThang(nam);
        pnlChart.remove(chart);
        chart = new Chart();
        chart.addLegend("Vốn", new Color(245, 189, 135));
        chart.addLegend("Doanh thu", new Color(135, 189, 245));
        chart.addLegend("Lợi nhuận", new Color(189, 135, 245));
        for (int i = 0; i < list.size(); i++) {
            chart.addData(new ModelChart("Tháng " + (i + 1), new double[]{list.get(i).getChiphi(), list.get(i).getDoanhthu(), list.get(i).getLoinhuan()}));
        }
        chart.repaint();
        chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint();
        pnlChart.validate();
        tblModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            tblModel.addRow(new Object[]{
                    "Tháng " + (i + 1), Formater.FormatVND(list.get(i).getChiphi()), Formater.FormatVND(list.get(i).getDoanhthu()), Formater.FormatVND(list.get(i).getLoinhuan())
            });
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == export) {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDoanhThuTungThang.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
