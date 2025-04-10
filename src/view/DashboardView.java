package view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {

    public DashboardView() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Chào mừng đến Dashboard!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        add(label, BorderLayout.CENTER);
    }
}
