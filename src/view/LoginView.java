package view;

import dao.UserDao;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JPanel {
    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private Font fontXl = new Font("Segoe UI", Font.BOLD, 32);
    private Font fontL = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontS = new Font("Segoe UI", Font.PLAIN, 18);

    private Color bg = new Color(52, 73, 94);
    private Color bgF = new Color(149, 165, 166);
    private Color textColor = new Color(44, 62, 80);

    private UserDao userDao;

    public LoginView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDao = new UserDao();
        initComponents();
    }

    public LoginView() {
        this.userDao = new UserDao();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(bg);

        JLabel logoLabel = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ HÀNG");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(fontXl);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
        logoPanel.add(logoLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(80, 250, 80, 250),
                BorderFactory.createLineBorder(bgF, 2)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(fontL);
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        formPanel.add(createLabelWithIcon("Email:", "/icons/mail.png"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(fontS);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabelWithIcon("Mật khẩu:", "/icons/pass.png"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(fontS);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        loginButton = createStyledButton("Đăng nhập", new Color(39, 174, 96));
        loginButton.setForeground(textColor);
        registerButton = createStyledButton("Đăng ký", new Color(41, 128, 185));
        registerButton.setForeground(textColor);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> showRegisterDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formPanel);

        add(logoPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel createLabelWithIcon(String text, String iconPath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        JLabel label = new JLabel(text, icon, JLabel.LEFT);
        label.setFont(fontS);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(fontS);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

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

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Vui lòng nhập đầy đủ email và mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserModel user = userDao.authenticate(email, password);
        if (user != null) {
            if (mainFrame != null) mainFrame.loginSuccessful(user);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Email hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showRegisterDialog() {
        RegisterView dialog = new RegisterView(mainFrame != null ? mainFrame : new JFrame());
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Đăng nhập hệ thống");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new LoginView());
            frame.setVisible(true);
        });
    }
}