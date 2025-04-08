package view;

import com.sun.tools.javac.Main;
import dao.UserDao;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JPanel {
    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private Font fontXl = new Font("Arial", Font.BOLD, 30);
    private Font fontL = new Font("Arial", Font.BOLD, 25);
    private Font fontS = new Font("Arial", Font.PLAIN, 20);

    private Color bg = new Color(2, 69, 156);
    private Color bgF = new Color(82, 80, 80);

    private UserDao userDao;

    public LoginView() {
        userDao = new UserDao();

        setLayout(new BorderLayout());

        // Tao tieu de logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(bg);

        JLabel logoLabel = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ HÀNG");
        logoLabel.setForeground(Color.WHITE); // Sửa chỗ này, setForeground cho label
        logoLabel.setFont(fontXl);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(50, 5, 50, 5));

        logoPanel.add(logoLabel);

        // Tao form Login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(150, 300, 150, 300),
                BorderFactory.createLineBorder(bgF)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(fontL);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);

        ImageIcon emailIcon = new ImageIcon(getClass().getResource("/icons/mail.png"));
        JLabel emailLable = new JLabel("Email: ", emailIcon, JLabel.LEFT);
        emailLable.setFont(fontS);
        emailField = new JTextField(20);
        emailField.setFont(fontS);

        ImageIcon passIcon = new ImageIcon(getClass().getResource("/icons/pass.png"));
        JLabel passwordLable = new JLabel("Password: ", passIcon, JLabel.LEFT);
        passwordLable.setFont(fontS);
        passwordField = new JPasswordField(20);
        passwordField.setFont(fontS);

        loginButton = createStyledButton("Đăng nhập", new Color(39, 174, 96));
        registerButton = createStyledButton("Đăng ký", new Color(161, 98, 3));

        // Them su kien vao nut
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {login();}
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {showRegisterDialog();}
        });


        // Them giao dien vao form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(emailLable, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLable, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        JPanel btPanel = new JPanel(new GridLayout(1,  2, 10, 10));
        btPanel.setBackground(Color.WHITE);
        btPanel.add(loginButton);
        btPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btPanel, gbc);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(logoPanel, BorderLayout.NORTH);

//        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        JPanel wrapperPanel = new JPanel(new GridLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(formPanel);

        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
    
    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if(email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng không để trống email hoặc password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserModel user = userDao.authenticate(email, password);
        if(user != null) {
            mainFrame.loginSuccessful(user);
//            JOptionPane.showMessageDialog(this, "Thanh cong", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Email hoặc mật khẩu không hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showRegisterDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đăng ký", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        // Tiêu đề
        JLabel titleLabel = new JLabel("REGISTER");
        titleLabel.setFont(fontL);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Các trường nhập liệu
        emailField = new JTextField(20);
        emailField.setFont(fontS);
        passwordField = new JPasswordField(20);
        passwordField.setFont(fontS);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(fontS);

        // Icon và Label
        ImageIcon emailIcon = new ImageIcon(getClass().getResource("/icons/mail.png"));
        JLabel emailLabel = new JLabel("Email: ", emailIcon, JLabel.LEFT);
        emailLabel.setFont(fontS);

        ImageIcon passIcon = new ImageIcon(getClass().getResource("/icons/pass.png"));
        JLabel passwordLabel = new JLabel("Mật khẩu:", passIcon, JLabel.LEFT);
        passwordLabel.setFont(fontS);
        JLabel confirmPasswordLabel = new JLabel("Nhập lại mật khẩu:", passIcon, JLabel.LEFT);
        confirmPasswordLabel.setFont(fontS);

        // Thêm vào form với padding
        formPanel.add(emailLabel);
        formPanel.add(createPaddedField(emailField));
        formPanel.add(passwordLabel);
        formPanel.add(createPaddedField(passwordField));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(createPaddedField(confirmPasswordField));
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Tạo nút với style riêng
        JButton regisBt = createStyledButton("Đăng ký", new Color(39, 174, 96));
        JButton cancelBt = createStyledButton("Hủy", new Color(192, 57, 43));

        btPanel.add(regisBt);
        btPanel.add(cancelBt);


        regisBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "⚠️ Vui lòng không để trống!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog, "❌ Mật khẩu không trùng khớp!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserModel user = new UserModel();
                user.setId_User(userDao.getNextId());
                user.setEmail(email);
                user.setPassword(password);
                user.setRole("Khach Hang");

                if(userDao.addUser(user)) {
                    JOptionPane.showMessageDialog(dialog, "✅ Đăng ký thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ Đăng ký thất bại!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBt.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {dialog.dispose();}
        });

        btPanel.add(regisBt);
        btPanel.add(cancelBt);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createPaddedField(JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // đẩy field xuống
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // Tao nut
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(fontS);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding cho nút đẹp hơn

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });


        return button;
    }

















    public static void main(String[] args) {
        // Thiết lập giao diện hệ thống
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy giao diện trên luồng sự kiện
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test LoginView");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new LoginView());
            frame.setVisible(true);
        });
    }
}
