package client.view.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatIntelliJLaf;


public class RevenueView extends JPanel {
	
	private JTable table;

	public RevenueView() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
		JPanel panelOne = panelThongKeTheoNam();
		JPanel panelTwo = new JPanel();
		JPanel panelThree = new JPanel();
		JPanel panelFour = new JPanel();
		
		tabbedPane.addTab("Thống kê theo năm", panelOne);
		tabbedPane.addTab("Thống kê từng tháng trong năm", panelTwo);
		tabbedPane.addTab("Thống kê từng ngày trong tháng", panelThree);
		tabbedPane.addTab("Thống kê từ ngày đến ngày", panelFour);
		
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	private JPanel panelThongKeTheoNam() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.decode("#F0F7FA"));
		
		// Panel Container
		JPanel panelContainer = new JPanel(new BorderLayout());
		panelContainer.setBackground(Color.decode("#F0F7FA"));
		panelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Panel Container/Panel Top (biểu đồ)
		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setBackground(Color.decode("#F0F7FA"));
		
		// Tạo panel lọc trên đầu
		JPanel panelFilter = new JPanel();
		panelFilter.setBackground(Color.decode("#F0F7FA"));
		panelFilter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField txtFromYear = new JTextField(6);
		JTextField txtToYear = new JTextField(6);
		JButton btnThongKe = new JButton("Thống kê");
		JButton btnLamMoi = new JButton("Làm mới");
		JButton btnXuatExcel = new JButton("Xuất excel");

		panelFilter.add(new JLabel("Từ năm"));
		panelFilter.add(txtFromYear);
		panelFilter.add(new JLabel("Đến năm"));
		panelFilter.add(txtToYear);
		panelFilter.add(btnThongKe);
		panelFilter.add(btnLamMoi);
		panelFilter.add(btnXuatExcel);
		
		panelTop.add(panelFilter, BorderLayout.NORTH);
		panelTop.add(new RevenueChartExample(), BorderLayout.CENTER);
		
		// Panel Container/Panel Table (bảng)
		JPanel panelTable = new JPanel(new BorderLayout());
		panelTable.setBackground(Color.decode("#F0F7FA"));
		String[] colums = {"Năm","Vốn","Doanh thu","Lợi nhuận"};
		
		 Object[][] data = {};
		
		 table = new JTable(data,colums);
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
		 header.setFont(new Font("Arial", Font.BOLD, 13));
		 // Căn giữa các cột (nếu muốn)
		 DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		 centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		 for (int i = 0; i < table.getColumnCount(); i++) {
		     table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		 }
		 JScrollPane scrollPane = new JScrollPane(table);
		 scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Không viền
		 panelTable.add(scrollPane,BorderLayout.CENTER);
		
		panelContainer.add(panelTop, BorderLayout.CENTER);
		panelContainer.add(panelTable, BorderLayout.SOUTH);
		
		panel.add(panelContainer, BorderLayout.CENTER);
		return panel;
	}
	
}
