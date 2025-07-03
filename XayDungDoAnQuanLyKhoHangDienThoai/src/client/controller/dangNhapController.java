package client.controller;

import client.VKULogin;
import client.view.shared.Toast;
import com.formdev.flatlaf.FlatIntelliJLaf;
import network.SocketManager;
import shared.TransparentLoadingSpinner;
import shared.models.TaiKhoan;
import shared.models.NhanVien;
import shared.request.LoginRequest;
import shared.response.LoginResponse;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dangNhapController {
    private VKULogin loginView;
    private final ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/success.png"));
    private final ImageIcon warningIcon = new ImageIcon(getClass().getResource("/images/Warring.png"));

    public dangNhapController(VKULogin loginView) {
        this.loginView = loginView;
        initController();
    }

    private void initController() {
        loginView.getPnlDangNhap().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(loginView);
                TransparentLoadingSpinner spinner = new TransparentLoadingSpinner(parentFrame);
                SwingUtilities.invokeLater(() -> {
                    spinner.setVisible(true);
                    spinner.timer.start();
                });

                new Thread(() -> {
                    try {
                        checkLogin();
                    } catch (UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(VKULogin.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        SwingUtilities.invokeLater(() -> {
                            spinner.setVisible(false);
                            spinner.dispose();
                            spinner.timer.stop();
                        });
                    }
                }).start();
            }
        });
    }



    private void checkLogin() throws UnsupportedLookAndFeelException {
        String username = loginView.getTxtTaiKhoan().getText().trim();
        String password = new String(loginView.getTxtMatKhau().getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(loginView, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", "Cảnh báo!", JOptionPane.WARNING_MESSAGE)
            );
            return;
        }

        try {
            SocketManager sm = SocketManager.getInstance();
            if (!sm.isConnected()) {
                SocketManager.resetInstance();
                sm = SocketManager.getInstance();
                sm.startListening();
            }

            sm.send(new LoginRequest(username, password));
            Object response = sm.receive();

            if (response instanceof LoginResponse loginResponse) {
                if (loginResponse.isSuccess()) {
                    TaiKhoan tk = loginResponse.getTaiKhoan();
                    NhanVien nv = loginResponse.getNhanVien();

                    if (tk.getTrangthai() == 0) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(loginView, "Tài khoản đang bị khóa. Vui lòng liên hệ quản trị viên.", "Cảnh báo!", JOptionPane.WARNING_MESSAGE)
                        );
                        return;
                    }

                    SwingUtilities.invokeLater(() ->
                            new Toast(loginView, "Success", "Chào " + nv.getHoten() + " !", 1500, successIcon)
                    );

                    Timer timer = new Timer(300, e -> {
                        try {
                            UIManager.setLookAndFeel(new FlatIntelliJLaf());
                            new AppController(nv, tk);
                            loginView.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                } else {
                    SwingUtilities.invokeLater(() ->
                            new Toast(loginView, "Warning", "Sai tài khoản hoặc mật khẩu", 1500, warningIcon)
                    );

                    SocketManager.resetInstance();
                }
            } else {
                SwingUtilities.invokeLater(() ->
                        new Toast(loginView, "Warning", "Phản hồi không hợp lệ từ sever", 1500, warningIcon)

                );
                SocketManager.resetInstance();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    new Toast(loginView, "Warning", "Không thể kết nối với sever", 1500, warningIcon)

            );
            SocketManager.resetInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}