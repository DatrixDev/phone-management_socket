package client.view.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;

public class RevenueChartExample extends JPanel {

    public RevenueChartExample() {
    	setLayout(new BorderLayout());
    	
        // Dữ liệu biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Vốn
        dataset.addValue(400_000_000, "Vốn", "Năm 2023");
        dataset.addValue(20_000_000, "Vốn", "Năm 2024");

        // Doanh thu
        dataset.addValue(600_000_000, "Doanh thu", "Năm 2023");
        dataset.addValue(30_000_000, "Doanh thu", "Năm 2024");

        // Lợi nhuận
        dataset.addValue(100_000_000, "Lợi nhuận", "Năm 2023");
        dataset.addValue(5_000_000, "Lợi nhuận", "Năm 2024");

        // Tạo biểu đồ
        JFreeChart chart = ChartFactory.createBarChart(
                "Thống kê Vốn - Doanh thu - Lợi nhuận theo năm",
                "Năm",
                "Số tiền (VND)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Tùy chỉnh màu
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, new Color(255, 204, 153)); // Vốn - Cam nhạt
        renderer.setSeriesPaint(1, new Color(102, 178, 255)); // Doanh thu - Xanh
        renderer.setSeriesPaint(2, new Color(204, 153, 255)); // Lợi nhuận - Tím

        // Thêm biểu đồ vào JPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel, BorderLayout.CENTER);
    }

}
