package view;

import dao.UserDao;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class RegisterView extends JDialog {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;

    private UserDao userDao;

    private Font fontS = new Font("Arial", Font.PLAIN, 20);
    private Color textColor = new Color(44, 62, 80);
    private Color buttonColor = new Color(39, 174, 96);

    public RegisterView(JFrame parent) {
        super(parent, "Đăng ký người dùng", true);
        userDao = new UserDao();

        setSize(555, 333);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("ĐĂNG KÝ NGƯỜI DÙNG", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(textColor);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        formPanel.setBackground(Color.WHITE);

        // Khởi tạo field trước
        emailField = new JTextField();
        passwordField = new JPasswordField();

        // Add panel chứa icon + field
        formPanel.add(createIconTextField("Email", "/icons/mail.png", emailField));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createIconPasswordField("Mật khẩu", "/icons/pass.png", passwordField));
        formPanel.add(Box.createVerticalStrut(25));

        // Panel chứa nút
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        registerButton = createButton("Đăng ký", buttonColor);
        registerButton.setFont(fontS); // font nhỏ hơn một chút
        registerButton.setForeground(textColor);
        registerButton.setPreferredSize(new Dimension(120, 15)); // Kích thước nhỏ gọn

        cancelButton = createButton("Hủy", new Color(231, 76, 60));
        cancelButton.setFont(fontS);
        cancelButton.setForeground(textColor);
        cancelButton.setPreferredSize(new Dimension(120, 15));


        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        // Add toàn bộ vào cửa sổ
        add(formPanel, BorderLayout.CENTER);

        // Sự kiện nút
        registerButton.addActionListener(e -> register());
        cancelButton.addActionListener(e -> dispose());
    }


    private JPanel createIconTextField(String title, String iconPath, JTextField field) {
        field.setFont(fontS);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setBackground(Color.WHITE);

        URL iconURL = getClass().getResource(iconPath);
        JLabel iconLabel = new JLabel(iconURL != null ? new ImageIcon(iconURL) : null);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createIconPasswordField(String title, String iconPath, JPasswordField field) {
        field.setFont(fontS);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setBackground(Color.WHITE);

        URL iconURL = getClass().getResource(iconPath);
        JLabel iconLabel = new JLabel(iconURL != null ? new ImageIcon(iconURL) : null);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }



    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(fontS);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void register() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "❌ Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDao.getUserByEmail(email) != null) {
            JOptionPane.showMessageDialog(this, "❌ Email đã được sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserModel newUser = new UserModel();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("Khach Hang");

        if (userDao.register(newUser)) {
            JOptionPane.showMessageDialog(this, "✅ Đăng ký thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Đăng ký thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
