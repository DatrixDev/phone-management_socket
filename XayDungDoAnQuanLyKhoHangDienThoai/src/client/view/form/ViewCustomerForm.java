package client.view.form;

import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;


import java.awt.*;


public class ViewCustomerForm extends JDialog {
    private JPanel panelTop, panelBottom;
    private JPanel panelContainer, panelContent;
    private JButton btnCancel;
	private JTextField txtCustomerName;
	private JTextField txtCustomerPhone;
	private JTextField txtCustomerAddress;

    public ViewCustomerForm() {
        setTitle("Xem khách hàng");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        

        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("XEM KHÁCH HÀNG");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        // Panel Content
        panelContent = new JPanel(new GridLayout(6, 1, 17, 0));
        panelContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));

        panelContent.add(new JLabel("Tên khách hàng"));
        txtCustomerName = new JTextField();
        panelContent.add(txtCustomerName);
        
        panelContent.add(new JLabel("Số điện thoại"));
        txtCustomerPhone = new JTextField();
        panelContent.add(txtCustomerPhone);
        
        panelContent.add(new JLabel("Địa chỉ"));
        txtCustomerAddress = new JTextField();
        panelContent.add(txtCustomerAddress);

        // Panel Container
        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(panelContent, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);

        // Buttons
        panelBottom = new JPanel();
        panelBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	    
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

    public JButton getBtnCancel() {
        return btnCancel;
    }

	public static void main(String[] args) {
    	try {
    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
			new ViewCustomerForm().setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
	public void setCustomerData(String ten, String sdt, String diaChi) {
	    txtCustomerName.setText(ten);
	    txtCustomerPhone.setText(sdt);
	    txtCustomerAddress.setText(diaChi);

	    // Không cho chỉnh sửa
	    txtCustomerName.setEnabled(false);
//	    txtCustomerName.setBackground(Color.decode("#C3C3C3"));
	    txtCustomerPhone.setEnabled(false);
//	    txtCustomerPhone.setBackground(Color.decode("#C3C3C3"));
	    txtCustomerAddress.setEnabled(false);
//	    txtCustomerAddress.setBackground(Color.decode("#C3C3C3"));
	}


}
