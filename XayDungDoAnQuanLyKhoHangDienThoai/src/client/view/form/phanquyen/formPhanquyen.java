package client.view.form.phanquyen;

import javax.swing.*;
import java.awt.*;

public class formPhanquyen extends JFrame {
    private JRadioButton rbHome;
    private JRadioButton rbProduct;
    private JRadioButton rbProductInfo;
    private JRadioButton rbImport;
    private JRadioButton rbExport;
    private JButton btnConfirm;
    private ButtonGroup group;

    public formPhanquyen() {
        setTitle("Phân quyền người dùng");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel radioPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        radioPanel.setBorder(BorderFactory.createTitledBorder("Chọn 1 quyền truy cập"));

        rbHome = new JRadioButton("Quản lý");
        rbProduct = new JRadioButton("Nhân viên kho");
        rbProductInfo = new JRadioButton("Nhân viên xuất hàng");
        rbImport = new JRadioButton("Nhân viên giám sát");
        rbExport = new JRadioButton("CSKH");

        // Group radio buttons
        group = new ButtonGroup();
        group.add(rbHome);
        group.add(rbProduct);
        group.add(rbProductInfo);
        group.add(rbImport);
        group.add(rbExport);

        radioPanel.add(rbHome);
        radioPanel.add(rbProduct);
        radioPanel.add(rbProductInfo);
        radioPanel.add(rbImport);
        radioPanel.add(rbExport);

        btnConfirm = new JButton("Xác nhận");

        add(radioPanel, BorderLayout.CENTER);
        add(btnConfirm, BorderLayout.SOUTH);

    }

    // === Getter ===
    public JRadioButton getRbHome() {
        return rbHome;
    }

    public JRadioButton getRbProduct() {
        return rbProduct;
    }

    public JRadioButton getRbProductInfo() {
        return rbProductInfo;
    }

    public JRadioButton getRbImport() {
        return rbImport;
    }

    public JRadioButton getRbExport() {
        return rbExport;
    }

    public JButton getBtnConfirm() {
        return btnConfirm;
    }

    public String getSelectedPermission() {
        if (rbHome.isSelected()) return "Quản lý";
        if (rbProduct.isSelected()) return "Nhân viên kho";
        if (rbProductInfo.isSelected()) return "Nhân viên xuất hàng";
        if (rbImport.isSelected()) return "Nhân viên giám sát";
        if (rbExport.isSelected()) return "CSKH";
        return null;
    }

    public static void main(String[] args) {
        formPhanquyen form = new formPhanquyen();
        form.getBtnConfirm().addActionListener(e -> {
            String selected = form.getSelectedPermission();
            JOptionPane.showMessageDialog(form, "Quyền được chọn: " + selected);
        });
    }
}
