package client.view.views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class AreaChartPanel extends JPanel {

    public AreaChartPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F0F7FA"));

        // Tạo dataset
        DefaultCategoryDataset dataset = createDataset();

        // Tạo chart
        JFreeChart chart = ChartFactory.createAreaChart(
                "Thống kê doanh thu 8 ngày gần nhất",
                "Ngày", "Số tiền (VNĐ)",
                dataset
        );

        // Tùy chỉnh chart
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(220, 220, 220)); // Màu lưới nhạt
        plot.setDomainGridlinesVisible(false);
        plot.setOutlineVisible(false);
        plot.setInsets(new RectangleInsets(10, 10, 10, 10)); // Bo góc nhẹ

        // Renderer để set màu cho từng đường
        AreaRenderer renderer = new AreaRenderer();
        renderer.setSeriesPaint(0, new Color(0x3F51B5, true)); // Vốn - Xanh
        renderer.setSeriesPaint(1, new Color(0x9C27B0, true)); // Doanh thu - Tím
        renderer.setSeriesPaint(2, new Color(0xFF9800, true)); // Lợi nhuận - Cam

        plot.setRenderer(renderer);

        // Tạo ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(Color.decode("#F0F7FA"));
        chartPanel.setPopupMenu(null); // Không cho chuột phải menu
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);

        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dữ liệu mẫu cho 8 ngày
        String[] days = {"2024-11-22", "2024-11-23", "2024-11-24", "2024-11-25",
                         "2024-11-26", "2024-11-27", "2024-11-28", "2024-11-29"};

        int[] von = {0, 0, 0, 0, 0, 0, 10000000, 9500000};
        int[] doanhThu = {0, 0, 0, 0, 0, 0, 25000000, 24000000};
        int[] loiNhuan = {0, 0, 0, 0, 0, 0, 5000000, 4500000};

        for (int i = 0; i < days.length; i++) {
            dataset.addValue(von[i], "Vốn", days[i]);
            dataset.addValue(doanhThu[i], "Doanh thu", days[i]);
            dataset.addValue(loiNhuan[i], "Lợi nhuận", days[i]);
        }

        return dataset;
    }
}
