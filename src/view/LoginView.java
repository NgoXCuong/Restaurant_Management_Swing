package view;

import com.sun.tools.javac.Main;
import dao.UserDao;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private Main mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

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
        logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(50, 5, 50, 5));

        logoPanel.add(logoLabel);

//        add(logoPanel, BorderLayout.NORTH); // Thêm dòng này để add panel vào giao diện

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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);

        ImageIcon emailIcon = new ImageIcon(getClass().getResource("/icons/mail.png"));
        JLabel usernameLabel = new JLabel("Email: ", emailIcon, JLabel.LEFT);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));

        ImageIcon passIcon = new ImageIcon(getClass().getResource("/icons/pass.png"));
        JLabel passwordLable = new JLabel("Password: ", passIcon, JLabel.LEFT);
        passwordLable.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));

        loginButton = new JButton("Đăng nhập");
        loginButton.setBackground(new Color(39, 174, 96));
        loginButton.setForeground(Color.BLUE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 20));

        registerButton = new JButton("Đăng ký");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 20));
        registerButton.setBackground(new Color(161, 98, 3));
        registerButton.setForeground(Color.RED);
        registerButton.setFocusPainted(false);

        // Them su kien vao nut


        // Them giao dien vao form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

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
