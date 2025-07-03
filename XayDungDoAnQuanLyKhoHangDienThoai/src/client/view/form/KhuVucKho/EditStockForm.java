package client.view.form.KhuVucKho;

import client.view.components.RoundedButton;
import shared.models.KhuVucKho;

import javax.swing.*;
import java.awt.*;

public class EditStockForm extends JDialog {
    private JLabel lblTitle;
    private JTextField txtName,txtNote,txtAddress;
    private JLabel lblName,lblNote,lblAddress;
    private JPanel panelTop,panelcontent;
    private JButton btnAdd,btnCancel;

    public EditStockForm(){
        setTitle("Chỉnh sửa khu vực kho");
        setSize(470,380);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());


        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CHỈNH SỬA KHU VỰC KHO");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        panelcontent = new JPanel(new GridLayout(4,1,0,0));
        panelcontent.setBorder(BorderFactory.createEmptyBorder(15, 23, 20, 20));
        lblName= new JLabel("Tên khu vực kho");
        lblName.setForeground(Color.DARK_GRAY);
        txtName = new JTextField();
        lblNote = new JLabel("Ghi chú");
        lblNote.setForeground(Color.DARK_GRAY);
        txtNote = new JTextField();
        lblAddress = new JLabel("Địa chỉ");
        lblAddress.setForeground(Color.DARK_GRAY);
        txtAddress = new JTextField();


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

        btnAdd = new RoundedButton("Lưu thông tin", 40);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(150, 40));
        btnAdd.setBackground(new Color(51, 142, 193));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setForeground(Color.WHITE);

        btnCancel = new RoundedButton("Hủy", 40);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(197, 79, 85));
        btnCancel.setForeground(Color.WHITE);



        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);

        panelcontent.add(lblName);
        panelcontent.add(txtName);
        panelcontent.add(lblNote);
        panelcontent.add(txtNote);
        panelcontent.add(lblAddress);
        panelcontent.add(txtAddress);
        add(buttonPanel,BorderLayout.SOUTH);
        add(panelTop,BorderLayout.NORTH);
        add(panelcontent,BorderLayout.CENTER);


    }
    public void khuVucEdit(KhuVucKho khuVucKho){
        if(khuVucKho!=null){
            System.out.println("Có dữ liệu");

            txtName.setText(khuVucKho.getTenkhuvuc().trim());
            txtNote.setText(khuVucKho.getGhichu().trim());
            txtAddress.setText(khuVucKho.getToado().trim());}
        else {
            System.out.println("ko có dữ liệu");
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditStockForm edit = new EditStockForm();
            edit.setVisible(true);
        });
    }


    public JButton getBtnAdd() {
        return btnAdd;
    }
    public JButton getBtnCancel() {
        return btnCancel;
    }
    public JTextField getTxtName() {
        return txtName;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public JPanel getPanelcontent() {
        return panelcontent;
    }

    public void setPanelcontent(JPanel panelcontent) {
        this.panelcontent = panelcontent;
    }

    public JPanel getPanelTop() {
        return panelTop;
    }

    public void setPanelTop(JPanel panelTop) {
        this.panelTop = panelTop;
    }

    public JLabel getLblAddress() {
        return lblAddress;
    }

    public void setLblAddress(JLabel lblAddress) {
        this.lblAddress = lblAddress;
    }

    public void setBtnCancel(JButton btnCancel) {
        this.btnCancel = btnCancel;
    }

    public JLabel getLblNote() {
        return lblNote;
    }

    public void setLblNote(JLabel lblNote) {
        this.lblNote = lblNote;
    }

    public JLabel getLblName() {
        return lblName;
    }

    public void setLblName(JLabel lblName) {
        this.lblName = lblName;
    }

    public JTextField getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(JTextField txtAddress) {
        this.txtAddress = txtAddress;
    }

    public void setTxtNote(JTextField txtNote) {
        this.txtNote = txtNote;
    }

    public void setTxtName(JTextField txtName) {
        this.txtName = txtName;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public JTextField getTxtNote() {
        return txtNote;
    }

}
