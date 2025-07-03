package client.view.form.phieuXuat;

import client.view.components.RoundedButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectImei extends JDialog {
    private JTextField txtSearch;
    private JPanel listPanel;
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private JButton btnConfirm;

    public SelectImei(List<String> imeiList) {
        setSize(310, 500);
        setLayout(new BorderLayout());
        setTitle("Chọn IMEI");
        setModal(true);

        // Tạo phần tìm kiếm
        txtSearch = new JTextField();
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Tìm kiếm mã IMEI ...");

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("Tìm kiếm mã IMEI ...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("Tìm kiếm mã IMEI ...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        txtSearch.setPreferredSize(new Dimension(340, 35));
        add(txtSearch, BorderLayout.NORTH);

        // Panel chứa danh sách checkbox
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Thêm các checkbox
        for (String imei : imeiList) {
            JCheckBox cb = new JCheckBox(imei);
            checkBoxes.add(cb);
            listPanel.add(cb);
        }

        // Tìm kiếm động
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterList(); }
            public void removeUpdate(DocumentEvent e) { filterList(); }
            public void changedUpdate(DocumentEvent e) {}

            private void filterList() {
                String keyword = txtSearch.getText().toLowerCase();
                for (JCheckBox cb : checkBoxes) {
                    cb.setVisible(cb.getText().toLowerCase().contains(keyword));
                }
                listPanel.revalidate();
                listPanel.repaint();
            }
        });

        // Nút xác nhận
        btnConfirm = new RoundedButton("Xác nhận",40);
        btnConfirm.setPreferredSize(new Dimension(140, 34));
        btnConfirm.setBackground(new Color(96, 138, 104));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnConfirm);
        add(bottomPanel, BorderLayout.SOUTH);

        // Sự kiện nút xác nhận


        SwingUtilities.invokeLater(() -> {
            getRootPane().requestFocusInWindow();
            txtSearch.select(0, 0);
        });

        setLocationRelativeTo(null);
    }

    public List<String> getSelectedImeis() {
        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText());
            }
        }
        return selected;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(JTextField txtSearch) {
        this.txtSearch = txtSearch;
    }

    public JPanel getListPanel() {
        return listPanel;
    }

    public void setListPanel(JPanel listPanel) {
        this.listPanel = listPanel;
    }

    public List<JCheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(List<JCheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public JButton getBtnConfirm() {
        return btnConfirm;
    }

    public void setBtnConfirm(JButton btnConfirm) {
        this.btnConfirm = btnConfirm;
    }
}
