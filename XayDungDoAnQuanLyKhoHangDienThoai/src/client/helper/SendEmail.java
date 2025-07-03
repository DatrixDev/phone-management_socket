package client.helper;

import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
    private String verificationCode;
    public void sendMail(String emailNhan) {
        // Thông tin người gửi
        final String username = "konoma124@gmail.com";  // Email người gửi
        final String password = "oail orsu pnzm lfiy";  // Thay bằng mật khẩu ứng dụng

        // Thông tin người nhận
        //String emailNhan = "nguyenkyvy112az@gmail.com"; // Email người nhận

        // Tạo mã xác nhận ngẫu nhiên
        verificationCode = generateVerificationCode();
        // Cấu hình SMTP server
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        // Xác thực tài khoản
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo nội dung email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Email gửi
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailNhan) // Email nhận
            );
            message.setSubject("QUẢN LÝ KHO HÀNG ĐIỆN THOẠI THÔNG BÁO");
            message.setText("Xin chào, đây là mã xác nhận lấy lại mật khẩu của bạn: " + verificationCode);


            // Gửi email
            Transport.send(message);
            System.out.println("Email đã được gửi thành công!");

//            // Kiểm tra mã xác nhận người dùng nhập vào
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Vui lòng nhập mã xác nhận đã gửi vào email: ");
//            String userCode = scanner.nextLine();
//
//            if (userCode.equals(verificationCode)) {
//                System.out.println("Mã xác nhận đúng!");
//            } else {
//                System.out.println("Mã xác nhận không đúng. Vui lòng thử lại.");
//            }

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Không thể gửi email!");
        }
    }
    // Hàm tạo mã xác nhận ngẫu nhiên 6 chữ số
    private static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // Thêm số ngẫu nhiên vào mã
        }
        return code.toString();
    }
    public String getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }


}