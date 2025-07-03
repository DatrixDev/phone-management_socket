package client.view.form;

import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;


import java.awt.*;


public class EditSupplierForm extends JDialog {
    private JPanel panelTop, panelBottom;
    private JPanel panelContainer, panelContent;
    private JButton btnCancel;
	private JTextField txtSupplierName;
	private JTextField txtSupplierPhone;
	private JTextField txtSupplierAddress;
	private JTextField txtSupplierEmail;
	private JButton btnSave;
    private int supplierId;

    public EditSupplierForm() {
        setTitle("Chỉnh sửa nhà cung cấp");
        setSize(900, 360);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        

        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CHỈNH SỬA NHÀ CUNG CẤP");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);
       
        // Panel Content
        panelContent = new JPanel(new GridLayout(4, 2, 30, 0));
        panelContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        panelContent.add(new JLabel("Tên nhà cung cấp"));
        panelContent.add(new JLabel("Địa chỉ"));
        txtSupplierName = new JTextField();
        panelContent.add(txtSupplierName);
        txtSupplierAddress = new JTextField();
        panelContent.add(txtSupplierAddress);
        
        panelContent.add(new JLabel("Email"));
        panelContent.add(new JLabel("Số điện thoại"));
        txtSupplierEmail = new JTextField();
        panelContent.add(txtSupplierEmail); 
        txtSupplierPhone = new JTextField();
        panelContent.add(txtSupplierPhone); 


        // Panel Container
        panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBackground(Color.red);
        panelContainer.add(panelContent, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);

        // Buttons
        panelBottom = new JPanel();
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
	    btnSave = new JButton("Lưu thông tin");
	    btnSave.setBorderPainted(false);
	    btnSave.setFont(new Font("Arial", Font.BOLD, 13));
	    btnSave.setPreferredSize(new Dimension(150, 40));
	    btnSave.setBackground(new Color(51, 142, 193));
	    btnSave.setForeground(Color.WHITE);
	    btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    btnCancel = new JButton("Hủy bỏ");
	    btnCancel.setBorderPainted(false);
	    btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
	    btnCancel.setPreferredSize(new Dimension(150, 40));
	    btnCancel.setBackground(new Color(197, 79, 85));
	    btnCancel.setForeground(Color.WHITE);
	    btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    panelBottom.add(btnSave);
	    panelBottom.add(btnCancel);
        add(panelBottom, BorderLayout.SOUTH);

    }

    public void setSupplierData(int id, String ten, String diaChi, String email, String sdt) {
        this.supplierId = id; // gán ID cần sửa
        txtSupplierName.setText(ten);
        txtSupplierAddress.setText(diaChi);
        txtSupplierEmail.setText(email);
        txtSupplierPhone.setText(sdt);

    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnSave() {
		return btnSave;
	}

    public int getSupplierId() {
        return supplierId;
    }

    public JTextField getTxtSupplierName() {
        return txtSupplierName;
    }

    public JTextField getTxtSupplierPhone() {
        return txtSupplierPhone;
    }

    public JTextField getTxtSupplierAddress() {
        return txtSupplierAddress;
    }

    public JTextField getTxtSupplierEmail() {
        return txtSupplierEmail;
    }

    public static void main(String[] args) {
    	try {
    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
			new EditSupplierForm().setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

}
