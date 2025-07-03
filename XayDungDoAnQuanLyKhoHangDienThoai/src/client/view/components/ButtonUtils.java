package client.view.components;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ButtonUtils {
    public static void setIconButton(JButton button, String imagePath, int width, int height) {
        URL url = ButtonUtils.class.getResource(imagePath);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        }
    }

}
