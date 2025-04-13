package view;

import dao.EmployeeDAO;
import model.EmployeeModel;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private Font fontXl = new Font("Segoe UI", Font.BOLD, 32);
    private Font fontL = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontS = new Font("Segoe UI", Font.PLAIN, 18);

    private Color bg = new Color(52, 73, 94);

    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;

    private UserModel userModel;
    private EmployeeModel employeeModel;

    public MainFrame() {
        setTitle("HỆ THỐNG QUẢN LÝ NHÀ HÀNG");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        LoginView loginView = new LoginView(this); // đúng cách
        contentPanel.add(loginView, "Login");

        cardLayout.show(contentPanel, "Login");

        add(contentPanel);
        setVisible(true);
    }

    public void loginSuccessful(UserModel user) {
        this.userModel = user;

        if ("Nhan Vien".equals(userModel.getRole()) || "Quan Ly".equals(userModel.getRole())) {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeModel = employeeDAO.getEmployeeByIdUser(userModel.getId_User());
        }

        initializePanels();
        createSidebar();

        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        cardLayout.show(contentPanel, "dashboard");

        revalidate();
        repaint();
    }

    private void initializePanels() {
        DashboardView dashboardView = new DashboardView(this); // Tạo view mẫu
        contentPanel.add(dashboardView, "dashboard");
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(bg);

        JLabel logoLabel = new JLabel("Quản lý nhà hàng");
        logoLabel.setFont(fontS);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(logoLabel);
        sidebarPanel.add(Box.createVerticalStrut(20));

        addMenuItem("Dashboard", "dashboard", true);
        addMenuItem("Đăng xuất", "logout", false);
    }

    private void addMenuItem(String text, String panelName, boolean isSwitchPanel) {
        JPanel menuItemPanel = new JPanel(new BorderLayout());
        menuItemPanel.setMaximumSize(new Dimension(250, 50));
        menuItemPanel.setBackground(bg);
        menuItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel menuLabel = new JLabel(text);
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setFont(fontS);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        menuItemPanel.add(menuLabel, BorderLayout.CENTER);

        menuItemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("logout".equals(panelName)) {
                    logout();
                } else {
                    cardLayout.show(contentPanel, panelName);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuItemPanel.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItemPanel.setBackground(bg);
            }
        });

        sidebarPanel.add(menuItemPanel);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            userModel = null;
            employeeModel = null;

            getContentPane().removeAll();
            contentPanel = new JPanel(cardLayout);
            LoginView loginView = new LoginView(this);
            contentPanel.add(loginView, "Login");
            cardLayout.show(contentPanel, "Login");

            getContentPane().add(contentPanel);
            revalidate();
            repaint();
        }
    }

    public UserModel getCurrentUser() {
        return userModel;
    }

    public EmployeeModel getCurrentEmployee() {
        return employeeModel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
