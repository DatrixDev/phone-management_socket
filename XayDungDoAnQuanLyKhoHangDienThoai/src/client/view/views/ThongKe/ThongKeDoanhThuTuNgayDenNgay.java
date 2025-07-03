package client.view.views.ThongKe;

import client.helper.Formater;
import client.helper.JTableExporter;
import client.view.components.Chart.BarChart.Chart;
import client.view.components.PanelBorderRadius;
import com.toedter.calendar.JDateChooser;

import client.BUS.ThongKeBUS;
import shared.models.ThongKe.ThongKeTonKho;
import shared.models.ThongKe.ThongKeTungNgayTrongThang;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class ThongKeDoanhThuTuNgayDenNgay extends JPanel {

    PanelBorderRadius pnlChart;
    JPanel pnl_top;
    HashMap<Integer, ArrayList<ThongKeTonKho>> listSp;
    ThongKeBUS thongkeBUS;

    Chart chart;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton btnThongKe, btnReset, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;

    public ThongKeDoanhThuTuNgayDenNgay(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        listSp = thongkeBUS.getTonKho();
        initComponent();

    }

    public void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblFrom = new JLabel("Từ ngày");
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        JLabel lblTo = new JLabel("Đến ngày");
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        btnThongKe = new JButton("Thống kê");
        btnReset = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        pnl_top.add(lblFrom);
        pnl_top.add(dateFrom);
        pnl_top.add(lblTo);
        pnl_top.add(dateTo);
        pnl_top.add(btnThongKe);
        pnl_top.add(btnExport);
        pnl_top.add(btnReset);

        btnExport.addActionListener((ActionEvent e) -> {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dateFrom.addPropertyChangeListener("date", e -> {
            Date date = (Date) e.getNewValue();
            try {
                if (validateSelectDate()) {
                }
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        dateTo.addPropertyChangeListener("date", e -> {
            Date date = (Date) e.getNewValue();
            try {
                if (validateSelectDate()) {
                }
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

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
        String[] header = new String[]{"Ngày", "Chi phí", "Doanh thu", "Lợi nhuận"};
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
        scrollTableThongKe.setPreferredSize(new Dimension(0, 350));
        this.add(pnl_top, BorderLayout.NORTH);
        this.add(scrollTableThongKe, BorderLayout.CENTER);

        btnThongKe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (validateSelectDate()) {
                        if (dateFrom.getDate() != null && dateTo.getDate() != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String start = formatter.format(dateFrom.getDate());
                            String end = formatter.format(dateTo.getDate());
                            loadThongKeTungNgayTrongThang(start, end);
                        } else {
                            JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin");
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ThongKeDoanhThuTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateFrom.setDate(null);
                dateTo.setDate(null);
                tblModel.setRowCount(0);
            }

        });
    }

    public boolean validateSelectDate() throws ParseException {
        Date time_start = dateFrom.getDate();
        Date time_end = dateTo.getDate();

        Date current_date = new Date();
        if (time_start != null && time_start.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateFrom.setCalendar(null);
            return false;
        }
        if (time_end != null && time_end.after(current_date)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được lớn hơn ngày hiện tại", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateTo.setCalendar(null);
            return false;
        }
        if (time_start != null && time_end != null && time_start.after(time_end)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn ngày bắt đầu", "Lỗi !", JOptionPane.ERROR_MESSAGE);
            dateTo.setCalendar(null);
            return false;
        }
        return true;
    }

    public void loadThongKeTungNgayTrongThang(String start, String end) {
        ArrayList<ThongKeTungNgayTrongThang> list = thongkeBUS.getThongKeTuNgayDenNgay(start, end);
        tblModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            tblModel.addRow(new Object[]{
                    list.get(i).getNgay(), Formater.FormatVND(list.get(i).getChiphi()), Formater.FormatVND(list.get(i).getDoanhthu()), Formater.FormatVND(list.get(i).getLoinhuan())
            });
        }
    }
}
