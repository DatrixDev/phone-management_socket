package client.view.form;

import javax.swing.*;


import client.helper.BCrypt;
import client.view.views.AccountView;
import client.BUS.TaiKhoanBUS;
import server.DAO.NhomQuyenDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditAccountForm extends JDialog {
    private AccountView account;
    private JPanel panelTop, panelBottom;
    private JPanel panelContainer, panelContent;
    private JButton btnUpdate, btnCancel;
    private JTextField txtAccountName;
    private JPasswordField txtAccountPass;
    private JComboBox txtAccountRights;
    private JComboBox txtAccountStatus;
    private ArrayList<NhomQuyen> listNq = NhomQuyenDAO.getInstance().selectAll();
    private NhanVien nhanVien;
    public TaiKhoanBUS taiKhoanBus = new TaiKhoanBUS();
    int manv;
    private JLabel lblPass;

    public EditAccountForm(AccountView account, int manv) {
        this.manv = manv;
        this.account = account;
        initComponent();
    }

    public EditAccountForm(AccountView account, TaiKhoan tk) {
        this.manv = tk.getManv();
        this.account = account;
        initComponent();
        txtAccountName.setText(tk.getUsername());
        txtAccountPass.setText(tk.getMatkhau());
        txtAccountRights.setSelectedItem(NhomQuyenDAO.getInstance().selectById(tk.getManhomquyen() + "").getTennhomquyen());
        txtAccountStatus.setSelectedIndex(tk.getTrangthai());
    }

    public void initComponent() {
        setTitle("Cập nhật tài khoản");
        setSize(500, 630);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());


        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("CẬP NHẬT TÀI KHOẢN");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelTop.setBackground(Color.decode("#187AC3"));
        panelTop.add(lblTitle, BorderLayout.CENTER);

        // Panel Content
        panelContent = new JPanel(new GridLayout(8, 1, 17, 0));
        panelContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));

        panelContent.add(new JLabel("Tên đăng nhập"));
        txtAccountName = new JTextField();
        panelContent.add(txtAccountName);

        lblPass = new JLabel("Mật khẩu");
        panelContent.add(lblPass);
        txtAccountPass = new JPasswordField();
        panelContent.add(txtAccountPass);

        panelContent.add(new JLabel("Nhóm quyền"));
        txtAccountRights = new JComboBox(getNhomQuyen());
        panelContent.add(txtAccountRights);

        panelContent.add(new JLabel("Trạng thái"));
        txtAccountStatus = new JComboBox(new String[]{"Ngưng hoạt động", "Hoạt động"});
        panelContent.add(txtAccountStatus);

        // Panel Container
        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(panelContent, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);
        add(panelContainer, BorderLayout.CENTER);

        // Buttons
        panelBottom = new JPanel();
        panelBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        btnUpdate = new JButton("Lưu thông tin");
        btnUpdate.setBorderPainted(false);
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 13));
        btnUpdate.setPreferredSize(new Dimension(150, 40));
        btnUpdate.setBackground(new Color(51, 142, 193));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Chức năng cho nút "Lưu thông tin"
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(txtAccountName.getText().length() == 0)) {
                    String tendangnhap = txtAccountName.getText();
                    String pass = BCrypt.hashpw(new String(getTxtAccountPass().getPassword()), BCrypt.gensalt(12));
                    int manhom = listNq.get(txtAccountRights.getSelectedIndex()).getManhomquyen();
                    int tt = txtAccountStatus.getSelectedIndex();
                    TaiKhoan tk = new TaiKhoan(manv, tendangnhap, pass, manhom, tt);
                    TaiKhoanDAO.getInstance().update(tk);
                    taiKhoanBus.updateAcc(account.getRowSelected(), tk);
                    account.loadTableData(taiKhoanBus.getTaiKhoanAll());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng không để trống tên");
                }
            }
        });

        btnCancel = new JButton("Hủy bỏ");
        btnCancel.setBorderPainted(false);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(197, 79, 85));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Chức năng cho nút "Hủy bỏ"
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panelBottom.add(btnUpdate);
        panelBottom.add(btnCancel);
        add(panelBottom, BorderLayout.SOUTH);
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public void setUsername(String username) {
        txtAccountName.setText(username);
    }

    public JTextField getTxtAccountName() {
        return txtAccountName;
    }

    public JPasswordField getTxtAccountPass() {
        return txtAccountPass;
    }

    public JComboBox getTxtAccountRights() {
        return txtAccountRights;
    }

    public JComboBox getTxtAccountStatus() {
        return txtAccountStatus;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public JPanel getPanelContent() {
        return panelContent;
    }

    public JLabel getLblPass() {
        return lblPass;
    }

    public String[] getNhomQuyen() {
        String[] listNhomQuyen = new String[listNq.size()];
        for (int i = 0; i < listNq.size(); i++) {
            listNhomQuyen[i] = listNq.get(i).getTennhomquyen();
        }
        return listNhomQuyen;
    }

    public boolean validateInput() {
        if (txtAccountName.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống tên đăng nhập");
            return false;
        } else if (txtAccountName.getText().length() < 6) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập ít nhất 6 kí tự");
            return false;
        } else if (txtAccountPass.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống mật khẩu");
            return false;
        } else if (txtAccountPass.getPassword().length < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu ít nhất 6 ký tự");
            return false;
        }
        return true;
    }


}
