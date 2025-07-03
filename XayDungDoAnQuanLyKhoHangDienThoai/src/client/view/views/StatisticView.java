package client.view.views;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import client.helper.Formater;
import client.view.components.Chart.CurveChart.CurveChart;
import client.view.components.Chart.CurveChart.ModelChart2;
import client.view.components.TableSorter;
import client.view.shared.BaseView;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.toedter.calendar.JDateChooser;

import server.DAO.KhachHangDAO;
import server.DAO.NhanVienDAO;
import shared.models.ThongKe.ThongKeTungNgayTrongThang;

public class StatisticView extends BaseView {
	private JPanel panelCenter;
	private JPanel panelFooter;
	private JTable table;
	private JPanel panelGird;
	private JPanel panelButtons;
	public boolean isSelected;
	private JPanel panelBox2, panelBox1, panelBox3;
	private JPanel pnlChart;
	private CurveChart chart;
	ArrayList<ThongKeTungNgayTrongThang> dataset;
	private DefaultTableModel model;

	public StatisticView() {

		this.dataset = new ArrayList<>();
//		loadDataTalbe(dataset);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Arial", Font.BOLD, 12)); 
		JPanel panelTongQuan = createPanelTongQuan();
		JPanel panelTonKho = createPanelTonKho();
		JPanel panelDoanhThu = createPanelDoanhThu();
		JPanel panelNhaCungCap = createTabPanel("Nhà cung cấp");
		JPanel panelKhachHang = createPanelKhachHang();
		
		tabbedPane.addTab("Tổng quan", panelTongQuan);
        tabbedPane.addTab("Tồn kho", panelTonKho);
        tabbedPane.addTab("Doanh thu", panelDoanhThu);
        tabbedPane.addTab("Nhà cung cấp", panelNhaCungCap);
        tabbedPane.addTab("Khách hàng", panelKhachHang);
        
        getMainPanel().add(tabbedPane, BorderLayout.CENTER);
        
        
	}
	
	private JPanel createTabPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 32));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
	
	private JPanel createPanelTongQuan() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.decode("#F0F7FA"));

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
		
		panelBox1 = createInfoBox("/images/iconSmartphone.png", "15", "Sản phẩm hiện có trong kho");
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
		panelFooter = new JPanel(new BorderLayout());
		table = new JTable();
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
		
		panel.add(panelContainer, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createPanelTonKho() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(Color.decode("#F0F7FA"));
		panel.setOpaque(true);
		panel.repaint();
		
		// Panel Left
		JPanel panelLeft = new JPanel(new GridLayout(2, 1, 0, 0));
		panelLeft.setPreferredSize(new Dimension(300, 0));
		// Panel Left/Panel Grid
		JPanel panelGrid1 = new JPanel(new BorderLayout());
		JPanel panelGrid = new JPanel(new GridLayout(6, 1, 0, 0));
		panelGrid.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		panelGrid.setBackground(Color.WHITE);
		panelGrid.add(new JLabel("Tìm kiếm sản phẩm"));
		JTextField txtSearchProduct = new JTextField();
		panelGrid.add(txtSearchProduct);
		panelGrid.add(new JLabel("Từ ngày"));
		JDateChooser dateChooserFromDate = new JDateChooser();
		dateChooserFromDate.setDateFormatString("dd/MM/yyyy"); // định dạng hiển thị
		panelGrid.add(dateChooserFromDate);
		panelGrid.add(new JLabel("Đến ngày"));
		JDateChooser dateChooserToDate = new JDateChooser();
		dateChooserToDate.setDateFormatString("dd/MM/yyyy"); // định dạng hiển thị
		panelGrid.add(dateChooserToDate);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panelButtons.setBackground(Color.WHITE);
		
		JButton btnExportExcel = new JButton("Xuất Excel");
		btnExportExcel.setFocusPainted(false);
		btnExportExcel.setBorderPainted(false);
		btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnExportExcel.setPreferredSize(new Dimension(130, 35));
		btnExportExcel.setBackground(new Color(91, 167, 107)); // màu xanh lá
		btnExportExcel.setForeground(Color.WHITE);
		btnExportExcel.setBorder(BorderFactory.createLineBorder(new Color(91, 167, 107), 1, true)); // bo tròn

		
		JButton btnReset = new JButton("Làm mới");
		btnReset.setFocusPainted(false);
		btnReset.setBorderPainted(false);
		btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnReset.setPreferredSize(new Dimension(130, 35));
		btnReset.setBackground(new Color(220, 88, 95)); // màu xanh lá
		btnReset.setForeground(Color.WHITE);
		btnReset.setBorder(BorderFactory.createLineBorder(new Color(91, 167, 107), 1, true)); // bo tròn

		
		panelButtons.add(btnExportExcel);
		panelButtons.add(btnReset);
		
		
		panelGrid1.add(panelButtons, BorderLayout.SOUTH);
		panelGrid1.add(panelGrid, BorderLayout.CENTER);
		
		
		JPanel panelGrid2 = new JPanel();
		panelGrid2.setBackground(Color.WHITE);
		
		panelLeft.add(panelGrid1);
		panelLeft.add(panelGrid2);
		
		// Panel Right
		JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.setBackground(Color.decode("#F0F7FA"));
        panelRight.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10)); 
		panelRight.setOpaque(true);

		String[] colums = {"STT","Mã SP","Tên sản phẩm","Tồn đầu kỳ","Nhập trong kỳ","Xuất trong kỳ", "Tồn cuối kỳ"};

        Object[][] data = {};

        JTable table = new JTable(data,colums);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        // Tắt viền ngoài
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0)); // Không có khoảng cách giữa các ô
        table.setBackground(Color.WHITE);
        // Tùy chỉnh header
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(239, 239, 239)); // Màu nền nhạt cho header
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        
        panelRight.add(scrollPane,BorderLayout.CENTER);
		
		panel.add(panelLeft, BorderLayout.WEST);
		panel.add(panelRight, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createPanelDoanhThu() {
		RevenueView panel = new RevenueView();
		
		return panel;
	}
	
	private JPanel createPanelKhachHang() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(Color.decode("#F0F7FA"));
		panel.setOpaque(true);
		panel.repaint();
		
		// Panel Left
		JPanel panelLeft = new JPanel(new GridLayout(2, 1, 0, 0));
		panelLeft.setPreferredSize(new Dimension(300, 0));
		// Panel Left/Panel Grid
		JPanel panelGrid1 = new JPanel(new BorderLayout());
		JPanel panelGrid = new JPanel(new GridLayout(6, 1, 0, 0));
		panelGrid.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		panelGrid.setBackground(Color.WHITE);
		panelGrid.add(new JLabel("Tìm kiếm khách hàng"));
		JTextField txtSearchProduct = new JTextField();
		panelGrid.add(txtSearchProduct);
		panelGrid.add(new JLabel("Từ ngày"));
		JDateChooser dateChooserFromDate = new JDateChooser();
		dateChooserFromDate.setDateFormatString("dd/MM/yyyy"); // định dạng hiển thị
		panelGrid.add(dateChooserFromDate);
		panelGrid.add(new JLabel("Đến ngày"));
		JDateChooser dateChooserToDate = new JDateChooser();
		dateChooserToDate.setDateFormatString("dd/MM/yyyy"); // định dạng hiển thị
		panelGrid.add(dateChooserToDate);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panelButtons.setBackground(Color.WHITE);
		
		JButton btnExportExcel = new JButton("Xuất Excel");
		btnExportExcel.setFocusPainted(false);
		btnExportExcel.setBorderPainted(false);
		btnExportExcel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnExportExcel.setPreferredSize(new Dimension(130, 35));
		btnExportExcel.setBackground(new Color(91, 167, 107)); // màu xanh lá
		btnExportExcel.setForeground(Color.WHITE);
		btnExportExcel.setBorder(BorderFactory.createLineBorder(new Color(91, 167, 107), 1, true)); // bo tròn

		
		JButton btnReset = new JButton("Làm mới");
		btnReset.setFocusPainted(false);
		btnReset.setBorderPainted(false);
		btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnReset.setPreferredSize(new Dimension(130, 35));
		btnReset.setBackground(new Color(220, 88, 95)); // màu xanh lá
		btnReset.setForeground(Color.WHITE);
		btnReset.setBorder(BorderFactory.createLineBorder(new Color(91, 167, 107), 1, true)); // bo tròn

		
		panelButtons.add(btnExportExcel);
		panelButtons.add(btnReset);
		
		
		panelGrid1.add(panelButtons, BorderLayout.SOUTH);
		panelGrid1.add(panelGrid, BorderLayout.CENTER);
		
		
		JPanel panelGrid2 = new JPanel();
		panelGrid2.setBackground(Color.WHITE);
		
		panelLeft.add(panelGrid1);
		panelLeft.add(panelGrid2);
		
		// Panel Right
		JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.setBackground(Color.decode("#F0F7FA"));
        panelRight.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10)); 
		panelRight.setOpaque(true);

		String[] colums = {"STT","Mã khách hàng","Tên khách hàng","Số lượng phiếu","Tổng số tiền"};

        Object[][] data = {};

        JTable table = new JTable(data,colums);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        // Tắt viền ngoài
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0)); // Không có khoảng cách giữa các ô
        table.setBackground(Color.WHITE);
        // Tùy chỉnh header
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(239, 239, 239)); // Màu nền nhạt cho header
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // Căn giữa các cột (nếu muốn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        
        panelRight.add(scrollPane,BorderLayout.CENTER);
		
		panel.add(panelLeft, BorderLayout.WEST);
		panel.add(panelRight, BorderLayout.CENTER);
		return panel;
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

	public static void main(String[] args) {
		try {
    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
			new StatisticView().setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
