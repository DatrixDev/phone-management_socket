package client.view.form.SanPham;

import shared.models.SanPham;
import shared.models.cauHinhSanPham;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class sanPhamChitiet extends JDialog {
    private JTextField txtProductName, txtChip, txtBattery, txtScreenSize, txtRearCamera, txtFrontCamera;
    private JTextField txtHdh, txtWarranty, txtColor, txtImportPrice, txtExportPrice, SoLuong;
    private JTextField txtOrigin, txtOS, txtBrand, txtWarehouse, txtROM, txtRAM;
    private JLabel lblImagePreview;
    private JPanel panelTop, panelBottom, panelContainer, panelImage, panelContent;
    private String imagePath = null;

    public sanPhamChitiet() {
        setTitle("Chi tiết sản phẩm");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CHI TIẾT SẢN PHẨM");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        // Panel Image
        panelImage = new JPanel();
        lblImagePreview = new JLabel("Click để chọn ảnh", SwingConstants.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(200, 250));
        panelImage.setBorder(BorderFactory.createEmptyBorder(75 , 30, 10, 10));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelImage.add(lblImagePreview);

        // Panel Content
        panelContent = new JPanel(new GridLayout(10, 4, 20, 10));
        panelContent.setBorder(BorderFactory.createEmptyBorder(15, 23, 20, 20));

        panelContent.add(new JLabel("Tên sản phẩm:"));
        panelContent.add(new JLabel("Xuất xứ"));
        panelContent.add(new JLabel("Chip"));
        panelContent.add(new JLabel("Dung lượng pin"));

        txtProductName = new JTextField();
        panelContent.add(txtProductName);
        txtOrigin = new JTextField();
        panelContent.add(txtOrigin);
        txtChip = new JTextField();
        panelContent.add(txtChip);
        txtBattery = new JTextField();
        panelContent.add(txtBattery);

        panelContent.add(new JLabel("Kích thước màn hình"));
        panelContent.add(new JLabel("Camera sau"));
        panelContent.add(new JLabel("Camera trước"));
        panelContent.add(new JLabel("Hệ điều hành"));

        txtScreenSize = new JTextField();
        panelContent.add(txtScreenSize);
        txtRearCamera = new JTextField();
        panelContent.add(txtRearCamera);
        txtFrontCamera = new JTextField();
        panelContent.add(txtFrontCamera);
        txtOS = new JTextField();
        panelContent.add(txtOS);

        panelContent.add(new JLabel("Phiên bản hđh"));
        panelContent.add(new JLabel("Thời gian bảo hành"));
        panelContent.add(new JLabel("Thương hiệu"));
        panelContent.add(new JLabel("Khu vực kho"));

        txtHdh = new JTextField();
        panelContent.add(txtHdh);
        txtWarranty = new JTextField();
        panelContent.add(txtWarranty);
        txtBrand = new JTextField();
        panelContent.add(txtBrand);
        txtWarehouse = new JTextField();
        panelContent.add(txtWarehouse);

        panelContent.add(new JLabel("RAM"));
        panelContent.add(new JLabel("ROM"));
        panelContent.add(new JLabel("Giá nhập"));
        panelContent.add(new JLabel("Giá xuất"));

        txtRAM = new JTextField();
        panelContent.add(txtRAM);
        txtROM = new JTextField();
        panelContent.add(txtROM);
        txtImportPrice = new JTextField();
        panelContent.add(txtImportPrice);
        txtExportPrice = new JTextField();
        panelContent.add(txtExportPrice);

        panelContent.add(new JLabel("Màu sắc"));
        panelContent.add(new JLabel("Số lượng"));
        panelContent.add(new JLabel(""));
        panelContent.add(new JLabel(""));

        txtColor = new JTextField();
        panelContent.add(txtColor);
        SoLuong = new JTextField();
        panelContent.add(SoLuong);

        // Panel Container
        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(panelImage, BorderLayout.WEST);
        panelContainer.add(panelContent, BorderLayout.CENTER);

        // Buttons
        panelBottom = new JPanel();
        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        lblImagePreview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblImagePreview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "gif"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePath = selectedFile.getAbsolutePath();
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    Image image = imageIcon.getImage().getScaledInstance(200, 230, Image.SCALE_SMOOTH);
                    lblImagePreview.setIcon(new ImageIcon(image));
                    lblImagePreview.setText("");
                }
            }
        });
        JTextField[] fields = {
                txtProductName, txtChip, txtBattery, txtScreenSize, txtRearCamera, txtFrontCamera,
                txtHdh, txtWarranty, txtColor, txtImportPrice, txtExportPrice, SoLuong,
                txtOrigin, txtOS, txtBrand, txtWarehouse, txtROM, txtRAM
        };

        for (JTextField field : fields) {
            field.setEditable(false);
            field.setFocusable(false);

        }

    }

    public void loadSanPham(SanPham sp) {
        if (sp != null) {

            // Load dữ liệu từ SanPham
            txtProductName.setText(sp.getTenSanPham());
            txtChip.setText(sp.getChip());
            txtBattery.setText(String.valueOf(sp.getDungLuongPin()));
            txtScreenSize.setText(String.valueOf(sp.getKichThuocMan()));
            txtRearCamera.setText(sp.getCamSau());
            txtFrontCamera.setText(sp.getCamTruoc());
            txtHdh.setText(String.valueOf(sp.getPhienBanHDH()));
            txtWarranty.setText(String.valueOf(sp.getThoiGianBaoHanh()));


            txtOrigin.setText(sp.getXuatXu());
            txtOS.setText(sp.getHeDieuHanh());
            txtBrand.setText(sp.getThuongHieu());
            txtWarehouse.setText(sp.getKhuVucKho());

            cauHinhSanPham cauhinh= sp.getCauHinhs();

                txtROM.setText(cauhinh.getRom() + " GB");
                txtRAM.setText(cauhinh.getRam() + " GB");
                txtColor.setText(cauhinh.getMausac());
                txtImportPrice.setText(String.valueOf(cauhinh.getGianhap()));
                txtExportPrice.setText(String.valueOf(cauhinh.getGiaxuat()));
                SoLuong.setText(String.valueOf(sp.getSoLuong()));



            // Load hình ảnh
            imagePath = sp.getImage_path();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblImagePreview.setIcon(new ImageIcon(image));
                lblImagePreview.setText("");
            }
        } else {
            System.out.println("Không có dữ liệu");
        }
    }

    // Getters
    public String getImagePath() {
        return imagePath;
    }

    public JTextField getTxtProductName() {
        return txtProductName;
    }

    public JTextField getTxtChip() {
        return txtChip;
    }

    public JTextField getTxtBattery() {
        return txtBattery;
    }

    public JTextField getTxtScreenSize() {
        return txtScreenSize;
    }

    public JTextField getTxtRearCamera() {
        return txtRearCamera;
    }

    public JTextField getTxtFrontCamera() {
        return txtFrontCamera;
    }

    public JTextField getTxtHdh() {
        return txtHdh;
    }

    public JTextField getTxtWarranty() {
        return txtWarranty;
    }

    public JTextField getTxtColor() {
        return txtColor;
    }

    public JTextField getTxtImportPrice() {
        return txtImportPrice;
    }

    public JTextField getTxtExportPrice() {
        return txtExportPrice;
    }

    public JTextField getSoLuong() {
        return SoLuong;
    }

    public JLabel getLblImagePreview() {
        return lblImagePreview;
    }


    // Setters
    public void setTxtProductName(JTextField txtProductName) {
        this.txtProductName = txtProductName;
    }

    public void setTxtChip(JTextField txtChip) {
        this.txtChip = txtChip;
    }

    public void setTxtBattery(JTextField txtBattery) {
        this.txtBattery = txtBattery;
    }

    public void setTxtScreenSize(JTextField txtScreenSize) {
        this.txtScreenSize = txtScreenSize;
    }

    public void setTxtRearCamera(JTextField txtRearCamera) {
        this.txtRearCamera = txtRearCamera;
    }

    public void setTxtFrontCamera(JTextField txtFrontCamera) {
        this.txtFrontCamera = txtFrontCamera;
    }

    public void setTxtHdh(JTextField txtHdh) {
        this.txtHdh = txtHdh;
    }

    public void setTxtWarranty(JTextField txtWarranty) {
        this.txtWarranty = txtWarranty;
    }

    public void setTxtColor(JTextField txtColor) {
        this.txtColor = txtColor;
    }

    public void setTxtImportPrice(JTextField txtImportPrice) {
        this.txtImportPrice = txtImportPrice;
    }

    public void setTxtExportPrice(JTextField txtExportPrice) {
        this.txtExportPrice = txtExportPrice;
    }

    public void setSoLuong(JTextField SoLuong) {
        this.SoLuong = SoLuong;
    }

    public void setLblImagePreview(JLabel lblImagePreview) {
        this.lblImagePreview = lblImagePreview;
    }

    public JPanel getPanelContent() {
        return panelContent;
    }

    public JPanel getPanelImage() {
        return panelImage;
    }

    public JPanel getPanelContainer() {
        return panelContainer;
    }

    public JPanel getPanelBottom() {
        return panelBottom;
    }

    public JPanel getPanelTop() {
        return panelTop;
    }

    public JTextField getTxtRAM() {
        return txtRAM;
    }

    public void setTxtRAM(JTextField txtRAM) {
        this.txtRAM = txtRAM;
    }

    public JTextField getTxtROM() {
        return txtROM;
    }

    public void setTxtROM(JTextField txtROM) {
        this.txtROM = txtROM;
    }

    public JTextField getTxtWarehouse() {
        return txtWarehouse;
    }

    public void setTxtWarehouse(JTextField txtWarehouse) {
        this.txtWarehouse = txtWarehouse;
    }

    public JTextField getTxtBrand() {
        return txtBrand;
    }

    public void setTxtBrand(JTextField txtBrand) {
        this.txtBrand = txtBrand;
    }

    public JTextField getTxtOS() {
        return txtOS;
    }

    public void setTxtOS(JTextField txtOS) {
        this.txtOS = txtOS;
    }

    public JTextField getTxtOrigin() {
        return txtOrigin;
    }

    public void setTxtOrigin(JTextField txtOrigin) {
        this.txtOrigin = txtOrigin;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPanelTop(JPanel panelTop) {
        this.panelTop = panelTop;
    }

    public void setPanelBottom(JPanel panelBottom) {
        this.panelBottom = panelBottom;
    }

    public void setPanelContainer(JPanel panelContainer) {
        this.panelContainer = panelContainer;
    }

    public void setPanelImage(JPanel panelImage) {
        this.panelImage = panelImage;
    }

    public void setPanelContent(JPanel panelContent) {
        this.panelContent = panelContent;
    }


    public static void main(String[] args) {
        new sanPhamChitiet().setVisible(true);
    }
}