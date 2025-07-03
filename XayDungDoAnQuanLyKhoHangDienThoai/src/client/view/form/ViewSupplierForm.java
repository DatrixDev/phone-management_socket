package client.view.form;

import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;


import java.awt.*;


public class ViewSupplierForm extends JDialog {
    private JPanel panelTop, panelBottom;
    private JPanel panelContainer, panelContent;
    private JButton btnCancel;
	private JTextField txtSupplierName;
	private JTextField txtSupplierPhone;
	private JTextField txtSupplierAddress;
	private JTextField txtSupplierEmail;

    public ViewSupplierForm() {
        setTitle("Xem nhà cung cấp");
        setSize(900, 360);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        

        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("XEM NHÀ CUNG CẤP");
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
	    
	    btnCancel = new JButton("Hủy bỏ");
	    btnCancel.setBorderPainted(false);
	    btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
	    btnCancel.setPreferredSize(new Dimension(150, 40));
	    btnCancel.setBackground(new Color(197, 79, 85));
	    btnCancel.setForeground(Color.WHITE);
	    btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    panelBottom.add(btnCancel);
        add(panelBottom, BorderLayout.SOUTH);

    }

    public void setSupplierData(String ten, String diaChi, String email, String sdt) {
        txtSupplierName.setText(ten);
        txtSupplierAddress.setText(diaChi);
        txtSupplierEmail.setText(email);
        txtSupplierPhone.setText(sdt);

        // Không cho chỉnh sửa
        txtSupplierName.setEnabled(false);
//        txtSupplierName.setBackground(Color.decode("#C3C3C3"));
        txtSupplierAddress.setEnabled(false);
//        txtSupplierAddress.setBackground(Color.decode("#C3C3C3"));
        txtSupplierEmail.setEnabled(false);
//        txtSupplierEmail.setBackground(Color.decode("#C3C3C3"));
        txtSupplierPhone.setEnabled(false);
//        txtSupplierPhone.setBackground(Color.decode("#C3C3C3"));
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

	public static void main(String[] args) {
    	try {
    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
			new ViewSupplierForm().setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

}
