package client.view.form;

import javax.swing.*;


import client.helper.BCrypt;
import client.view.views.AccountView;
import client.BUS.TaiKhoanBUS;
import server.DAO.NhanVienDAO;
import server.DAO.NhomQuyenDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddAccountForm extends JDialog {
    private AccountView account;
    private JPanel panelTop, panelBottom;
    private JPanel panelContainer, panelContent;
    private JButton btnAdd, btnCancel;
	private JTextField txtAccountName;
	private JPasswordField txtAccountPass;
	private JComboBox txtAccountRights;
	private JComboBox txtAccountStatus;
    private ArrayList<NhomQuyen> listNq = NhomQuyenDAO.getInstance().selectAll();
    private ArrayList<TaiKhoan> listTK = TaiKhoanDAO.getInstance().selectAll();
    private NhanVien nhanVien;
    public TaiKhoanBUS taiKhoanBus = new TaiKhoanBUS();
    int manv;

    public AddAccountForm(AccountView account, int manv) {
        this.manv = manv;
        this.account = account;
        initComponent();
    }

    public AddAccountForm(AccountView account, TaiKhoan tk) {
        this.manv = tk.getManv();
        this.account = account;
        txtAccountName.setText(tk.getUsername());
        txtAccountPass.setText(tk.getMatkhau());
        txtAccountRights.setSelectedItem(NhomQuyenDAO.getInstance().selectById(tk.getManhomquyen() + "").getTennhomquyen());
        txtAccountStatus.setSelectedIndex(tk.getTrangthai());
        initComponent();
    }

    public void initComponent() {
        setTitle("Thêm tài khoản");
        setSize(500, 630);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());


        // Panel Top
        panelTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("THÊM TÀI KHOẢN");
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

        panelContent.add(new JLabel("Mật khẩu"));
        txtAccountPass = new JPasswordField();
        txtAccountPass.putClientProperty("JPasswordField.showRevealButton", true);
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
        btnAdd = new JButton("Thêm tài khoản");
        btnAdd.setBorderPainted(false);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 13));
        btnAdd.setPreferredSize(new Dimension(150, 40));
        btnAdd.setBackground(new Color(51, 142, 193));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String tendangnhap = txtAccountName.getText();
                    boolean isDupdicate = listTK.stream().anyMatch(taiKhoan -> taiKhoan.getUsername().equals(tendangnhap));

                    if (!isDupdicate) {
                        String pass = BCrypt.hashpw(new String(getTxtAccountPass().getPassword()), BCrypt.gensalt(12));
                        int manhom = listNq.get(txtAccountRights.getSelectedIndex()).getManhomquyen();
                        int tt = txtAccountStatus.getSelectedIndex();
                        TaiKhoan tk = new TaiKhoan(manv, tendangnhap, pass, manhom, tt);
                        NhanVien nv = NhanVienDAO.getInstance().selectById(manv + "");
                        if (nv == null) {
                            JOptionPane.showMessageDialog(null, "Nhân viên không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        TaiKhoanDAO.getInstance().insert(tk);
                        taiKhoanBus.addAcc(tk);
                        account.loadTableData(taiKhoanBus.getTaiKhoanAll());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại. Vui lòng đổi tên khác!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        txtAccountName.getFocusCycleRootAncestor();
                    }

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

        panelBottom.add(btnAdd);
        panelBottom.add(btnCancel);
        add(panelBottom, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnAdd() {
		return btnAdd;
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
