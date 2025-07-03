package client.view.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import client.VKULogin;
import com.mysql.cj.log.Log;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;
import java.util.Scanner;


public class ForgotPasswordView extends JFrame{
    private String verificationCode;
    private String Email;
    private JButton btnSendCode;
    private JTextField txtEmail;
    private JLabel lblNote;
    private JFrame frame;
    private JFrame frame2;
    private JFrame frame3;
    private JTextField txtCode2;
    private JLabel lblNote2;
    private JButton btnVerify;
    private JButton btnSubmit;
    private JLabel lblNote3;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    public void showEmailInputForm() {

        frame = new JFrame("Quên mật khẩu");

        JButton btn1 = new JButton("←");
        btn1.setFont(new Font("Time new Roman", Font.BOLD, 29));
        btn1.setFocusPainted(false);
        btn1.setContentAreaFilled(false);
        btn1.setBorderPainted(false);
        btn1.setBounds(3, 1, 60, 25);
        frame.add(btn1);
        btn1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn1.setForeground(Color.decode("#0000FF")); // Đổi màu chữ khi di chuột vào
            }
        });
        btn1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                btn1.setForeground(Color.black); // Màu chữ ban đầu
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new VKULogin();
            }
        });

        lblNote = new JLabel("",SwingConstants.CENTER);
        lblNote.setForeground(Color.RED);
        lblNote.setBounds(20, 270, 300, 30);


        Color color = frame.getBackground();

        JLabel lblTitle = new JLabel("Quên mật khẩu");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(20, 40, 300, 30);

        JLabel lblInstruction = new JLabel("<html>Nhập địa chỉ email có tài khoản của bạn và chúng tôi sẽ gửi email xác nhận để thiết lập lại</html>");
        lblInstruction.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruction.setForeground(Color.gray);
        lblInstruction.setBounds(20, 70, 300, 40);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(20, 135, 300, 35);
        txtEmail.setBackground(color);


        TitledBorder border = BorderFactory.createTitledBorder(" Email");
        border.setTitleFont(new Font("Arial", Font.PLAIN, 10)); // Phông chữ cho tiêu đề
        border.setTitleColor(Color.gray);
        txtEmail.setBorder(border);

        btnSendCode = new JButton("Gửi code");
        btnSendCode.setBounds(20, 200, 300, 30);
        btnSendCode.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));


        frame.add(lblTitle);
        frame.add(lblInstruction);
        frame.add(txtEmail);
        frame.add(btnSendCode);
        frame.add(lblNote);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

    }

    // Giao diện 2: Kiểm tra email
    public void showCheckMailForm() {
        frame2 = new JFrame("Kiểm tra email");

        JButton btn2 = new JButton("←");
        btn2.setFont(new Font("Time new Roman", Font.BOLD, 29));
        btn2.setFocusPainted(false);
        btn2.setContentAreaFilled(false);
        btn2.setBorderPainted(false);
        btn2.setBounds(3, 1, 60, 25);
        frame2.add(btn2);
        btn2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn2.setForeground(Color.decode("#0000FF")); // Đổi màu chữ khi di chuột vào
            }
        });
        btn2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                btn2.setForeground(Color.black); // Màu chữ ban đầu
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame2.dispose();
                showEmailInputForm();
            }
        });

        Color color2 = frame2.getBackground();

        JLabel lblTitle2 = new JLabel("Vui lòng kiểm tra email của bạn");
        lblTitle2.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle2.setBounds(20, 40, 300, 30);

        JLabel lblInstruction2 = new JLabel("<html>Chúng tôi đã gửi mã xác nhận đến email của bạn.Vui lòng nhập mã để tiếp tục.</html>");
        lblInstruction2.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruction2.setForeground(Color.gray);
        lblInstruction2.setBounds(20, 70, 300, 40);

        txtCode2 = new JTextField();
        txtCode2.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCode2.setBounds(20, 135, 300, 35);
        txtCode2.setBackground(color2);


        TitledBorder border2 = BorderFactory.createTitledBorder(" Code");
        border2.setTitleFont(new Font("Arial", Font.PLAIN, 10)); // Phông chữ cho tiêu đề
        border2.setTitleColor(Color.gray);
        txtCode2.setBorder(border2);

        lblNote2 = new JLabel("",SwingConstants.CENTER);
        lblNote2.setForeground(Color.RED);
        lblNote2.setBounds(20, 270, 300, 30);



        btnVerify = new JButton("Xác nhận");
        btnVerify.setBounds(20, 200, 300, 30);
        btnVerify.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));

        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtCode2.getText().equals(verificationCode) ) {
                    lblNote2.setText("Note: Mã chính xác");
                    frame2.dispose(); // Đóng form hiện tại
                    showNewPasswordForm(); // Mở form tạo mật khẩu mới
                }
                else
                    lblNote2.setText("Note: Mã không chính xác ");
            }
        });


        frame2.add(lblTitle2);
        frame2.add(lblInstruction2);
        frame2.add(txtCode2);
        frame2.add(btnVerify);
        frame2.add(lblNote2);

        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.setSize(350, 450);
        frame2.setLayout(null);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setResizable(false);

    }

    // Giao diện 3: Tạo mật khẩu mới
    public void showNewPasswordForm() {
        frame3 = new JFrame("Tạo mật khẩu mới");

        // Nút quay lại
        JButton btnBack = new JButton("←");
        btnBack.setFont(new Font("Time new Roman", Font.BOLD, 29));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setBounds(3, 1, 60, 25);
        frame3.add(btnBack);

        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setForeground(Color.decode("#0000FF"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setForeground(Color.black);
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame3.dispose();
                showCheckMailForm();
            }
        });

        Color backgroundColor = frame3.getBackground();

        // Tiêu đề
        JLabel lblTitle = new JLabel("Tạo mật khẩu mới", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(20, 40, 300, 30);

        JLabel lblInstruction = new JLabel("Mật khẩu phải khác với mật khẩu trước đó", SwingConstants.LEFT);
        lblInstruction.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruction.setForeground(Color.GRAY);
        lblInstruction.setBounds(22, 70, 300, 20);

        // Ô nhập mật khẩu mới
        txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNewPassword.setBounds(20, 110, 300, 35);
        txtNewPassword.setBackground(backgroundColor);
        txtNewPassword.setBorder(createTitledBorder("Mật khẩu mới"));

        // Ô nhập lại mật khẩu mới
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConfirmPassword.setBounds(20, 160, 300, 35);
        txtConfirmPassword.setBackground(backgroundColor);
        txtConfirmPassword.setBorder(createTitledBorder("Xác nhận mật khẩu"));



        // Nút xác nhận
        btnSubmit = new JButton("Xác nhận");
        btnSubmit.setBounds(20, 220, 300, 30);
        btnSubmit.setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));

        // Label thông báo lỗi
        lblNote3 = new JLabel("", SwingConstants.CENTER);
        lblNote3.setForeground(Color.RED);
        lblNote3.setBounds(20, 270, 300, 30);

        // Thêm các thành phần vào frame
        frame3.add(btnBack);
        frame3.add(lblTitle);
        frame3.add(lblInstruction);
        frame3.add(txtNewPassword);
        frame3.add(txtConfirmPassword);
        frame3.add(lblNote3);
        frame3.add(btnSubmit);

        frame3.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Khi cửa sổ bị đóng, quay lại giao diện đăng nhập
//                new MainController();
            }
        });


        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame3.setSize(350, 380);
        frame3.setLayout(null);
        frame3.setLocationRelativeTo(null);
        frame3.setVisible(true);
        frame3.setResizable(false);
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("Arial", Font.PLAIN, 10));
        border.setTitleColor(Color.GRAY);
        return border;
    }

    public JButton getBtnSendCode() {
        return btnSendCode;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JLabel getLblNote() {
        return lblNote;
    }

    public JButton getBtnVerify() {
        return btnVerify;
    }

    public JTextField getTxtCode2() {
        return txtCode2;
    }

    public JLabel getLblNote2() {
        return lblNote2;
    }

    public JButton getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(JButton btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public JLabel getLblNote3() {
        return lblNote3;
    }

    public void setLblNote3(JLabel lblNote3) {
        this.lblNote3 = lblNote3;
    }

    public JPasswordField getTxtNewPassword() {
        return txtNewPassword;
    }

    public void setTxtNewPassword(JPasswordField txtNewPassword) {
        this.txtNewPassword = txtNewPassword;
    }

    public JPasswordField getTxtConfirmPassword() {
        return txtConfirmPassword;
    }

    public void setTxtConfirmPassword(JPasswordField txtConfirmPassword) {
        this.txtConfirmPassword = txtConfirmPassword;
    }

    public JFrame getFrameEmailInput() {
        return frame;
    }

    public JFrame getFrameCheckMail() {
        return frame2;
    }

    public JFrame getFrameNewPassword() {
        return frame3;
    }

}