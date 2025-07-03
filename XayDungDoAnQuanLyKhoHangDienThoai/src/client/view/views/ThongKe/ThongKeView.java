package client.view.views.ThongKe;

import client.view.shared.BaseView;
import client.BUS.ThongKeBUS;


import javax.swing.*;
import java.awt.*;

public final class ThongKeView extends BaseView {
    private JTabbedPane tabbedPane;
    JPanel tongquan , doanhthu;
    ThongKeBUS thongkeBUS = new ThongKeBUS();

    public ThongKeView() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(new Color(240, 247, 250));

        tongquan = new ThongKeTongQuan(thongkeBUS);
        doanhthu = new ThongKeDoanhThu(thongkeBUS);

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Tổng quan", tongquan);
        tabbedPane.addTab("Doanh thu", doanhthu);

        getMainPanel().add(tabbedPane);
    }
}
