package client.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConfirmCodeView extends JFrame{
    private JTextField jTextField_xacNhan;
    private JLabel jLabel_icon;
    private JLabel jLabel_thongBao;
    private JButton jButton_quayLai;
    private JButton jButton_xacNhan;
    public ConfirmCodeView() {
        this.init();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void init() {
        this.setTitle("Xác nhận tài khoản");
        this.setSize(800, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        JLabel jLabel_tieuDe = new JLabel("Nhập mã xác nhận");
        jTextField_xacNhan = new JTextField();
        ImageIcon icon = new ImageIcon("C:\\Users\\Anh Vy\\Pictures\\Screenshots\\load.png");
        jLabel_icon = new JLabel(icon);
        jLabel_thongBao = new JLabel("Mã xác nhận không đúng!");
        jButton_xacNhan = new JButton("Xác nhận");
        jButton_xacNhan.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
        jButton_quayLai = new JButton("Quay lại");
        jButton_quayLai.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));

        jLabel_tieuDe.setBounds(200, 50, 200, 30);
        jLabel_tieuDe.setFont(new Font("Times New Roman", Font.BOLD, 18));
        jTextField_xacNhan.setBounds(200, 80, 400, 30);
        jTextField_xacNhan.setFont(new Font("Arial", Font.PLAIN, 15));
        jLabel_icon.setBounds(595, 80, 55, 30);
        jLabel_icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel_thongBao.setBounds(450, 115, 140, 20);
        jLabel_thongBao.setFont(new Font("Times New Roman", Font.ITALIC | Font.PLAIN, 12));
        jLabel_thongBao.setForeground(this.getBackground());
        Font font = new Font("Arial", Font.TRUETYPE_FONT | Font.BOLD, 12);
        jButton_xacNhan.setBounds(220, 130, 90, 25);
        jButton_xacNhan.setFont(font);
        jButton_quayLai.setBounds(330, 130, 80, 25);
        jButton_quayLai.setFont(font);

        this.add(jTextField_xacNhan);
        this.add(jLabel_tieuDe);
        this.add(jLabel_icon);
        this.add(jLabel_thongBao);
        this.add(jButton_xacNhan);
        this.add(jButton_quayLai);
    }

    public JTextField getjTextField_xacNhan() {
        return jTextField_xacNhan;
    }

    public JLabel getjLabel_icon() {
        return jLabel_icon;
    }

    public JLabel getjLabel_thongBao() {
        return jLabel_thongBao;
    }


    public JButton getjButton_quayLai() {
        return jButton_quayLai;
    }

    public JButton getjButton_xacNhan() {
        return jButton_xacNhan;
    }


}