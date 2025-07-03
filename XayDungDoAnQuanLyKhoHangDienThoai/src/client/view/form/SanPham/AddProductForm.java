package client.view.form.SanPham;

import com.formdev.flatlaf.FlatIntelliJLaf;
import server.DAO.KhuVucKhoDAO;
import server.DAO.heDieuHanhDAO;
import server.DAO.thuongHieuDAO;
import server.DAO.xuatXuDAO;
import shared.models.HeDieuHanh;
import shared.models.SanPham;
import shared.models.XuatXu;
import shared.models.thuonghieu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddProductForm extends JDialog {
    private JTextField txtProductName, txtChip, txtBattery, txtScreenSize, txtRearCamera, txtFrontCamera;
    private JTextField txtHdh, txtWarranty, txtColor, txtImportPrice, txtExportPrice;
    private JLabel lblImagePreview;
    private JPanel panelTop, panelBottom, panelContainer, panelImage, panelContent;
    private JButton btnSave, btnCancel;
    private JComboBox<XuatXu> txtOrigin;
    private JComboBox<HeDieuHanh> txtOS;
    private JComboBox<thuonghieu> txtBrand;
    private JComboBox<String> txtWarehouse;

    private String imagePath = null;

    public AddProductForm() {
        setTitle("Thêm sản phẩm");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("THÊM SẢN PHẨM");
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
        panelContent = new JPanel(new GridLayout(6, 4, 17, 0));
        panelContent.setBorder(BorderFactory.createEmptyBorder(15, 23, 20, 20));

        panelContent.add(new JLabel("Tên sản phẩm:"));
        panelContent.add(new JLabel("Xuất xứ:"));
        panelContent.add(new JLabel("Chip:"));
        panelContent.add(new JLabel("Dung lượng pin:"));

        txtProductName = new JTextField();
        panelContent.add(txtProductName);


        txtOrigin = new JComboBox<>();
        List<XuatXu> xuatxuList = new xuatXuDAO().getAllXuatXu();
        if (xuatxuList.isEmpty()) {
            System.out.println(xuatxuList);
        }
        if (xuatxuList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách xuất xứ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<XuatXu> xuatxuModel = new DefaultComboBoxModel<>();
            for (XuatXu xx : xuatxuList) {
                xuatxuModel.addElement(xx);
            }
            txtOrigin.setModel(xuatxuModel);
            txtOrigin.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof XuatXu) {
                        setText(((XuatXu) value).getTen());
                    }
                    return this;
                }
            });
        }
        panelContent.add(txtOrigin);

        txtChip = new JTextField();
        panelContent.add(txtChip);

        txtBattery = new JTextField();
        panelContent.add(txtBattery);

        panelContent.add(new JLabel("Kích thước màn hình:"));
        panelContent.add(new JLabel("Camera sau:"));
        panelContent.add(new JLabel("Camera trước:"));
        panelContent.add(new JLabel("Hệ điều hành:"));

        txtScreenSize = new JTextField();
        panelContent.add(txtScreenSize);

        txtRearCamera = new JTextField();
        panelContent.add(txtRearCamera);

        txtFrontCamera = new JTextField();
        panelContent.add(txtFrontCamera);

        // Lấy dữ liệu hệ điều hành từ DAO

        txtOS = new JComboBox<>();
        List<HeDieuHanh> hdhList = new heDieuHanhDAO().getAllHeDieuHanh();
        if (hdhList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách hệ điều hành!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<HeDieuHanh> hdhModel = new DefaultComboBoxModel<>();
            for (HeDieuHanh hdh : hdhList) {
                hdhModel.addElement(hdh);
            }
            txtOS.setModel(hdhModel);
            txtOS.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof HeDieuHanh) {
                        setText(((HeDieuHanh) value).getTenHeDieuHanh());
                    }
                    return this;
                }
            });
        }
        panelContent.add(txtOS);

        panelContent.add(new JLabel("Phiên bản HĐH:"));
        panelContent.add(new JLabel("Thời gian bảo hành:"));
        panelContent.add(new JLabel("Thương hiệu:"));
        panelContent.add(new JLabel("Khu vực kho:"));

        txtHdh = new JTextField();
        panelContent.add(txtHdh);

        txtWarranty = new JTextField();
        panelContent.add(txtWarranty);

        // Lấy dữ liệu thương hiệu từ DAO
        txtBrand = new JComboBox<>();
        List<thuonghieu> brandList = new thuongHieuDAO().getAllThuongHieu();
        System.out.println(brandList + "có brand");
        if (brandList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách thương hiệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel<thuonghieu> brandModel = new DefaultComboBoxModel<>();
            for (thuonghieu th : brandList) {
                brandModel.addElement(th);
            }
            txtBrand.setModel(brandModel);
            txtBrand.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof thuonghieu) {
                        setText(((thuonghieu) value).getTenthuonghieu());
                    }
                    return this;
                }
            });
        }
        panelContent.add(txtBrand);

        ArrayList<String> tenkhuvuc = new KhuVucKhoDAO().getKhuVucKhoFromDatabase();
        if(tenkhuvuc.isEmpty()){
            tenkhuvuc.add("Không có dữ liệu");
            JOptionPane.showMessageDialog(this,"Không thể tải dữ liệu");
        }

        txtWarehouse = new JComboBox<>(tenkhuvuc.toArray(new String[0]));
        panelContent.add(txtWarehouse);

        // Panel Container
        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(panelImage, BorderLayout.WEST);
        panelContainer.add(panelContent, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        btnSave = new JButton("Lưu thông tin");
        btnSave.setBorderPainted(false);
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.setBackground(new Color(51, 142, 193));
        btnSave.setForeground(Color.WHITE);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancel = new JButton("Hủy");
        btnCancel.setBorderPainted(false);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(197, 79, 85));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        lblImagePreview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblImagePreview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setCurrentDirectory(new File("D:\\HOCTAP\\DO_AN_CO_SO_1\\DACS1\\XayDungDoAnQuanLyKhoHangDienThoai\\src\\images"));

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

    }

    public void loadSanPhamTam(SanPham sp) {
        if (sp != null) {
            System.out.println("Có dữ liệu");

            String dungLuongPin = String.valueOf(sp.getDungLuongPin());
            String kichThuocMan = String.valueOf(sp.getKichThuocMan());
            String thoiGianBaoHanh = String.valueOf(sp.getThoiGianBaoHanh());
            String phienBanHDH = String.valueOf(sp.getPhienBanHDH());
            txtProductName.setText(sp.getTenSanPham());
            txtOrigin.setSelectedItem(sp.getXuatXu());
            txtChip.setText(sp.getChip());
            txtBattery.setText(dungLuongPin);
            txtScreenSize.setText(kichThuocMan);
            txtRearCamera.setText(sp.getCamSau());
            txtFrontCamera.setText(sp.getCamTruoc());
            txtOS.setSelectedItem(sp.getHeDieuHanh());
            txtHdh.setText(phienBanHDH);
            txtWarranty.setText(thoiGianBaoHanh);
            txtBrand.setSelectedItem(sp.getThuongHieu());
            txtWarehouse.setSelectedItem(sp.getKhuVucKho());

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

    // Getters và Setters (giữ nguyên, chỉ thêm chú thích cho rõ ràng)
    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public String getImagePath() {
        return imagePath;
    }

    public JTextField getTxtBattery() {
        return txtBattery;
    }

    public void setTxtBattery(JTextField txtBattery) {
        this.txtBattery = txtBattery;
    }

    public JTextField getTxtProductName() {
        return txtProductName;
    }

    public JTextField getTxtChip() {
        return txtChip;
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

    public void setTxtProductName(JTextField txtProductName) {
        this.txtProductName = txtProductName;
    }

    public void setTxtChip(JTextField txtChip) {
        this.txtChip = txtChip;
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

    public void setLblImagePreview(JLabel lblImagePreview) {
        this.lblImagePreview = lblImagePreview;
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

    public void setBtnSave(JButton btnSave) {
        this.btnSave = btnSave;
    }

    public void setBtnCancel(JButton btnCancel) {
        this.btnCancel = btnCancel;
    }

    public JPanel getPanelTop() {
        return panelTop;
    }

    public JPanel getPanelBottom() {
        return panelBottom;
    }

    public JPanel getPanelContainer() {
        return panelContainer;
    }

    public JPanel getPanelImage() {
        return panelImage;
    }

    public JPanel getPanelContent() {
        return panelContent;
    }

    public JComboBox<XuatXu> getTxtOrigin() {
        return txtOrigin;
    }

    public void setTxtOrigin(JComboBox<XuatXu> txtOrigin) {
        this.txtOrigin = txtOrigin;
    }

    public JComboBox<HeDieuHanh> getTxtOS() {
        return txtOS;
    }

    public void setTxtOS(JComboBox<HeDieuHanh> txtOS) {
        this.txtOS = txtOS;
    }

    public JComboBox<thuonghieu> getTxtBrand() {
        return txtBrand;
    }

    public void setTxtBrand(JComboBox<thuonghieu> txtBrand) {
        this.txtBrand = txtBrand;
    }

    public JComboBox<String> getTxtWarehouse() {
        return txtWarehouse;
    }



    public void setTxtWarehouse(JComboBox<String> txtWarehouse) {
        this.txtWarehouse = txtWarehouse;
    }


    public void setTxtHdh(JTextField txtHdh) {
        this.txtHdh = txtHdh;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public JTextField getTxtWarranty() {
        return txtWarranty;
    }


    public JTextField getTxtHdh() {
        return txtHdh;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            new AddProductForm().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}