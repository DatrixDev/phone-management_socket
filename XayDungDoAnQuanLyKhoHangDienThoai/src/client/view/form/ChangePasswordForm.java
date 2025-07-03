package client.view.form;

import client.BUS.NhanVienBUS;
import client.BUS.TaiKhoanBUS;
import client.helper.BCrypt;
import client.helper.Validation;
import client.view.shared.SidebarMenu;
import client.view.views.MainView;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import server.DAO.NhanVienDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import shared.models.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ChangePasswordForm extends JDialog implements ActionListener {
    private JPanel top;
    private JPanel panelCenter;
    private JPanel top_center, main_center;
    private JRadioButton[] jbr;
    private JPanel[] panel;
    private JLabel lblPhone;
    private JTextField txtPhone, txtEmail;
    NhanVien nv;
    TaiKhoanBUS tkbus;
    NhanVienBUS nvbus;
    SidebarMenu sidebarMenu;
    MainView mainView; // Reference to MainView for refreshing
    private JPanel grid1;
    private JPanel grid2;
    private JLabel lblEmail;
    private JPasswordField current_pwd, new_pwd, confirm_pwd;
    private JPanel grid3;
    private JPanel bottom;
    private JButton btnCancel, btnSave;

    public ChangePasswordForm(SidebarMenu sidebarMenu, NhanVien nv, MainView mainView) {
        this.nv = nv;
        this.mainView = mainView; // Store reference to MainView
        initComponent(sidebarMenu);
    }

    // Backward compatibility constructor
    public ChangePasswordForm(SidebarMenu sidebarMenu, NhanVien nv) {
        this.nv = nv;
        this.mainView = null;
        initComponent(sidebarMenu);
    }

    private void initComponent(SidebarMenu sidebarMenu) {
        tkbus = new TaiKhoanBUS();
        nvbus = new NhanVienBUS();
        this.sidebarMenu = sidebarMenu;
        this.setTitle("Chỉnh sửa thông tin");
        this.setSize(400, 300);
        this.setBackground(Color.white);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        top = new JPanel();
        JLabel lblTitle = new JLabel("CHỈNH SỬA THÔNG TIN");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        top.setBackground(Color.decode("#187AC3"));
        top.add(lblTitle, BorderLayout.CENTER);

        top_center = new JPanel(new FlowLayout(1, 40, 0));
        top_center.setBorder(new EmptyBorder(20, 0, 0, 0));
        top_center.setBackground(Color.WHITE);
        main_center = new JPanel();
        main_center.setBorder(new EmptyBorder(0, 20, 0, 20));
        main_center.setBackground(Color.WHITE);

        ButtonGroup bg = new ButtonGroup();
        String opt[] = {"Số điện thoại", "Email", "Mật khẩu"};
        jbr = new JRadioButton[3];
        for (int i = 0; i < jbr.length; i++) {
            jbr[i] = new JRadioButton();
            jbr[i].addActionListener(this);
            jbr[i].setText(opt[i]);
            top_center.add(jbr[i]);
            bg.add(jbr[i]);
        }
        jbr[0].setSelected(true);

        panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        panelCenter.add(top_center, BorderLayout.NORTH);
        panelCenter.add(main_center, BorderLayout.CENTER);

        panel = new JPanel[3];

        panel[0] = new JPanel(new GridLayout(1, 1));
        panel[0].setPreferredSize(new Dimension(400, 100));
        grid1 = new JPanel(new GridLayout(2, 1));
        grid1.setBackground(Color.white);
        grid1.setBorder(new EmptyBorder(0, 10, 5, 10));
        grid1.setPreferredSize(new Dimension(100, 100));
        lblPhone = new JLabel("Số điện thoại");
        txtPhone = new JTextField();
        txtPhone.setText(nv.getSdt());
        System.out.println(nv.getSdt());
        System.out.println(nv.getManv());
        System.out.println(nv.getGioitinh());
        System.out.println(nv.getEmail());
        grid1.add(lblPhone);
        grid1.add(txtPhone);
        panel[0].add(grid1);
        main_center.add(panel[0]);

        panel[1] = new JPanel(new GridLayout(1, 1));
        panel[1].setPreferredSize(new Dimension(400, 100));
        grid2 = new JPanel(new GridLayout(2, 1));
        grid2.setBackground(Color.white);
        grid2.setBorder(new EmptyBorder(0, 10, 5, 10));
        grid2.setPreferredSize(new Dimension(100, 100));
        lblEmail = new JLabel("Email");
        txtEmail = new JTextField();
        txtEmail.setText(nv.getEmail());
        grid2.add(lblEmail);
        grid2.add(txtEmail);
        panel[1].add(grid2);
        main_center.add(panel[1]);

        panel[2] = new JPanel(new GridLayout(1, 1));
        panel[2].setPreferredSize(new Dimension(400, 300));
        grid3 = new JPanel(new GridLayout(6, 1));
        grid3.setBackground(Color.white);
        grid3.setBorder(new EmptyBorder(0, 10, 5, 10));
        grid3.setPreferredSize(new Dimension(100, 100));
        JLabel lblCurrent = new JLabel("Mật khẩu hiện tại");
        current_pwd = new JPasswordField();
        JLabel lblNew = new JLabel("Mật khẩu mới");
        new_pwd = new JPasswordField();
        JLabel lblConfirm = new JLabel("Nhập lại mật khẩu mới");
        confirm_pwd = new JPasswordField();
        grid3.add(lblCurrent);
        grid3.add(current_pwd);
        grid3.add(lblNew);
        grid3.add(new_pwd);
        grid3.add(lblConfirm);
        grid3.add(confirm_pwd);
        panel[2].add(grid3);
        main_center.add(panel[2]);

        bottom = new JPanel(new FlowLayout(1, 20, 10));
        bottom.setBackground(Color.WHITE);

        btnCancel = new JButton("Hủy");
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        btnCancel.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 0, 15));
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.putClientProperty("JButton.buttonType", "roundRect");
        btnCancel.putClientProperty("JButton.buttonType", "borderless");
        btnCancel.addActionListener(this);
        bottom.add(btnCancel);

        btnSave = new JButton("Lưu");
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
        btnSave.setFont(new java.awt.Font(FlatRobotoFont.FAMILY, 0, 15));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.putClientProperty("JButton.buttonType", "roundRect");
        btnSave.putClientProperty("JButton.buttonType", "borderless");
        btnSave.addActionListener(this);
        bottom.add(btnSave);

        this.add(top, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel) {
            this.dispose();
            return;
        }

        for (int i = 0; i < 3; i++) {
            if (e.getSource() == jbr[i]) {
                if (i == 2) {
                    this.setSize(new Dimension(400, 500));
                    this.setLocationRelativeTo(null);
                } else {
                    this.setSize(400, 300);
                    this.setLocationRelativeTo(null);
                }
                main_center.removeAll();
                main_center.add(panel[i]);
                main_center.repaint();
                main_center.validate();
                return;
            }
        }

        if (e.getSource() == btnSave) {
            if (jbr[0].isSelected()) {
                if (Validation.isEmpty(txtPhone.getText()) || txtPhone.getText().length() != 10) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không được rỗng và phải có 10 ký tự số", "Chỉnh sửa số điện thoại", JOptionPane.WARNING_MESSAGE);
                } else {
                    String sdt = txtPhone.getText();

                    int result = NhanVienDAO.getInstance().updatePhone(nv.getManv(), sdt);
                    if (result > 0) {
                        nv.setSdt(sdt);
                        JOptionPane.showMessageDialog(this, "Cập nhật số điện thoại thành công");
                        if (mainView != null) {
                            mainView.refreshEmployeeInfo();
                        }
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else if (jbr[1].isSelected()) {
                // Update email
                if (Validation.isEmpty(txtEmail.getText()) || !Validation.isEmail(txtEmail.getText())) {
                    JOptionPane.showMessageDialog(this, "Email không được rỗng và phải đúng định dạng", "Chỉnh sửa email", JOptionPane.WARNING_MESSAGE);
                } else {
                    String emailString = txtEmail.getText();

                    int result = NhanVienDAO.getInstance().updateEmail(nv.getManv(), emailString);
                    if (result > 0) {
                        nv.setEmail(emailString);
                        JOptionPane.showMessageDialog(this, "Cập nhật email thành công");
                        if (mainView != null) {
                            mainView.refreshEmployeeInfo();
                        }
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else if (jbr[2].isSelected()) {
                TaiKhoan tkdto = tkbus.getTaiKhoan(tkbus.getTaiKhoanByMaNV(nv.getManv()));
                String current_pass = new String(current_pwd.getPassword());
                String new_pass = new String(new_pwd.getPassword());
                String confirm_pass = new String(confirm_pwd.getPassword());
                if (Validation.isEmpty(current_pass)) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không được rỗng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                } else if (Validation.isEmpty(new_pass) || new_pass.length()<6) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu mới không được rỗng và có ít nhất 6 ký tự", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                } else if (Validation.isEmpty(confirm_pass)) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không được rỗng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (new_pass.equals(confirm_pass) == false) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp với mật khẩu mới", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    if (BCrypt.checkpw(current_pass, tkdto.getMatkhau())) {
                        String pass = BCrypt.hashpw(confirm_pass, BCrypt.gensalt(12));
                        TaiKhoanDAO.getInstance().updatePass(nv.getEmail(), pass);
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                        current_pwd.setText("");
                        new_pwd.setText("");
                        confirm_pwd.setText("");
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}