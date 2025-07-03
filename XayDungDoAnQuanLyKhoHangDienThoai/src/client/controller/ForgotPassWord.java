package client.controller;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;


public class ForgotPassWord extends JFrame{
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyThuVien;encrypt=false;trustServerCertificate=true";
    private static final String USER = "quocdat233";
    private static final String PASS = "dat23326";
    private String verificationCode;
    private String Email;

    public static void main(String[] args) {
        new ForgotPassWord().showEmailInputForm();}


    public void showEmailInputForm() {

        JFrame frame = new JFrame("Quên mật khẩu");
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
            }
        });

        JLabel lblNote = new JLabel("",SwingConstants.CENTER);
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

        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(20, 135, 300, 35);
        txtEmail.setBackground(color);


        TitledBorder border = BorderFactory.createTitledBorder(" Email");
        border.setTitleFont(new Font("Arial", Font.PLAIN, 10)); // Phông chữ cho tiêu đề
        border.setTitleColor(Color.gray);
        txtEmail.setBorder(border);

        JButton btnSendCode = new JButton("Gửi code");
        btnSendCode.setBounds(20, 200, 300, 30);
        btnSendCode.setBackground(Color.lightGray);

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

        btnSendCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String email = txtEmail.getText();
                Email = email;
                if(! kiemTraMail(email)){
                    lblNote.setText("Note: Vui lòng nhập đúng định dạng email !");

                }
                else
                if(isEmailExistsInDatabase(email)){
                    System.out.println("");
                    guiCode(email);
                    lblNote.setText("Note: đang gửi mail...");
                    frame.dispose();
                    showCheckMailForm();
                }
                else {
                    lblNote.setText("Note: Email này chưa được xác nhận..");

                }
            }
        });
    }
    public void showCheckMailForm() {
        JFrame frame2 = new JFrame("Kiểm tra email");
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
                btn2.setForeground(Color.decode("#0000FF"));
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
        JTextField txtCode2 = new JTextField();
        txtCode2.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCode2.setBounds(20, 135, 300, 35);
        txtCode2.setBackground(color2);
        TitledBorder border2 = BorderFactory.createTitledBorder(" Code");
        border2.setTitleFont(new Font("Arial", Font.PLAIN, 10)); // Phông chữ cho tiêu đề
        border2.setTitleColor(Color.gray);
        txtCode2.setBorder(border2);
        JLabel lblNote2 = new JLabel("",SwingConstants.CENTER);
        lblNote2.setForeground(Color.RED);
        lblNote2.setBounds(20, 270, 300, 30);
        JButton btnVerify = new JButton("Xác nhân");
        btnVerify.setBounds(20, 200, 300, 30);
        btnVerify.setBackground(Color.lightGray);
        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtCode2.getText().equals(verificationCode) ) {
                    lblNote2.setText("Note: Mã chính xác");
                    frame2.dispose();
                    showNewPasswordForm();
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



        frame2.setVisible(true);
    }

    public void showNewPasswordForm() {
        JFrame frame = new JFrame("Tạo mật khẩu mới");

        // Nút quay lại
        JButton btnBack = new JButton("←");
        btnBack.setFont(new Font("Time new Roman", Font.BOLD, 29));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setBounds(3, 1, 60, 25);
        frame.add(btnBack);

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
                frame.dispose();
                showCheckMailForm();
            }
        });

        Color backgroundColor = frame.getBackground();

        JLabel lblTitle = new JLabel("Tạo mật khẩu mới", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(20, 40, 300, 30);

        JLabel lblInstruction = new JLabel("Mật khẩu phải khác với mật khẩu trước đó", SwingConstants.LEFT);
        lblInstruction.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInstruction.setForeground(Color.GRAY);
        lblInstruction.setBounds(22, 70, 300, 20);

        JPasswordField txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNewPassword.setBounds(20, 110, 300, 35);
        txtNewPassword.setBackground(backgroundColor);
        txtNewPassword.setBorder(createTitledBorder("Mật khẩu mới"));

        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConfirmPassword.setBounds(20, 160, 300, 35);
        txtConfirmPassword.setBackground(backgroundColor);
        txtConfirmPassword.setBorder(createTitledBorder("Xác nhận mật khẩu"));

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.setBounds(20, 220, 300, 30);
        btnSubmit.setBackground(Color.LIGHT_GRAY);

        JLabel lblNote = new JLabel("", SwingConstants.CENTER);
        lblNote.setForeground(Color.RED);
        lblNote.setBounds(20, 270, 300, 30);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(String.valueOf(txtNewPassword.getPassword()));
                String confirmPassword = new String(String.valueOf(txtConfirmPassword.getPassword()));
                if(!checkPass(newPassword)){
                    lblNote.setText("Note: Mật khẩu không được nhỏ hơn 6 kí tự !");
                }
                else if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    lblNote.setText("Note: Vui lòng nhập đầy đủ thông tin !");
                } else if (!newPassword.equals(confirmPassword)) {
                    lblNote.setText("Note: Mật khẩu không khớp !");
                } else {
                    lblNote.setText("Mật khẩu đã được cập nhật thành công !");

                    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                        String query = "UPDATE sinhVienDaDangKi SET Mat_khau = ? WHERE Email = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, newPassword);
                            stmt.setString(2, Email); // email của người dùng cần được lưu từ bước trước đó
                            int rowsUpdated = stmt.executeUpdate();

                            if (rowsUpdated > 0) {
                                lblNote.setText("Mật khẩu đã được cập nhật thành công !");
                            } else {
                                lblNote.setText("Note: Không thể cập nhật mật khẩu. Vui lòng thử lại !");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Lỗi kết nối cơ sở dữ liệu !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        frame.add(btnBack);
        frame.add(lblTitle);
        frame.add(lblInstruction);
        frame.add(txtNewPassword);
        frame.add(txtConfirmPassword);
        frame.add(lblNote);
        frame.add(btnSubmit);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 380);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("Arial", Font.PLAIN, 10));
        border.setTitleColor(Color.GRAY);
        return border;
    }
    public boolean kiemTraMail(String email) {
        if (email.isEmpty()) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        int atIndex = email.indexOf("@");
        String domain = email.substring(atIndex + 1); // Lấy phần domain sau '@'
        if (!domain.contains(".")) {
            return false;
        }
        if (domain.charAt(domain.length() - 1) == '.') {
            return false;
        }
        if (atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }
        String chuoi = email.substring(email.indexOf('@') + 1);
        for (int i = 0; i < chuoi.length(); i++) {
            char c = chuoi.charAt(i);
            if (Character.isDigit(c))
                return false;
        }
        return true;
    }
    public boolean isEmailExistsInDatabase(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String query = "SELECT * FROM sinhVienDaDangKi WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public void guiCode(String email){
        String maiGui = "thuvienvku@gmail.com";
        String passMail = "ixwf dqle pphr lrgw";
        verificationCode = generateVerificationCode();
        System.out.println(verificationCode);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(maiGui, passMail);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(maiGui));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("🔑 KHÔI PHỤC MẬT KHẨU - THƯ VIỆN VKU");

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 15px; border: 1px solid #ddd; border-radius: 10px;'>"
                    + "<h2 style='color: #2b6cb0;'>📢 Thư viện VKU thông báo</h2>"
                    + "<p>Xin chào,</p>"
                    + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu của bạn. Vui lòng sử dụng mã xác nhận sau để tiếp tục:</p>"
                    + "<h3 style='color: #d9534f; font-size: 34px; text-align: center;'>" + verificationCode + "</h3>"
                    + "<p style='font-size: 12px; color: #777;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>"
                    + "<p>Trân trọng,<br><strong>Thư viện VKU</strong></p>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ForgotPassWord.this, "Không thể gửi email!");
        }
    }

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // Thêm số ngẫu nhiên vào mã
        }
        return code.toString();
    }
    public boolean checkPass(String str){
        boolean check = true;
        if(str.length()<6)
            check = false;
        return check;
    }

}