package client.view.form.KhuVucKho;

import javax.swing.*;
import java.awt.*;
public class ProductListCellRenderer extends JPanel implements ListCellRenderer<String> {
    private static int hoverIndex = -1; // chỉ số dòng đang được hover
    private JLabel nameLabel;
    private JLabel quantityLabel;
    private JPanel cardPanel;

    public ProductListCellRenderer() {
        setLayout(new BorderLayout());
        setBackground(new Color(203, 225, 236));

        nameLabel = new JLabel();
        quantityLabel = new JLabel();

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 15f));
        quantityLabel.setFont(quantityLabel.getFont().deriveFont(Font.PLAIN, 13f));

        nameLabel.setForeground(Color.BLACK);
        quantityLabel.setForeground(Color.GRAY);

        cardPanel = new JPanel(new BorderLayout(0, 2));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(quantityLabel, BorderLayout.SOUTH);

        add(cardPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        int slIndex = value.lastIndexOf("(SL:");
        String name = slIndex > 0 ? value.substring(0, slIndex).trim() : value;
        String quantity = slIndex > 0 ? value.substring(slIndex).trim() : "";

        nameLabel.setText(name);
        quantityLabel.setText(quantity);

        if (isSelected) {
            cardPanel.setBackground(new Color(203, 225, 236));

        } else if (index == hoverIndex) {
            cardPanel.setBackground(new Color(203, 225, 236));
// màu hover
        } else {
            cardPanel.setBackground(Color.WHITE);
        }

        return this;
    }

    // Setter để thay đổi hoverIndex từ bên ngoài
    public static void setHoverIndex(int index) {
        hoverIndex = index;
    }
}
