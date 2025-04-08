package view;

import dao.EmployeeDAO;
import model.EmployeeModel;
import model.UserModel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Font fontXl = new Font("Arial", Font.BOLD, 30);
    private Font fontL = new Font("Arial", Font.BOLD, 25);
    private Font fontS = new Font("Arial", Font.PLAIN, 20);

    private Color bg = new Color(2, 69, 156);
    private Color bgF = new Color(82, 80, 80);

    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;

    private UserModel userModel;
    private EmployeeModel employeeModel;

    // Khai bao giao dien
    private LoginView loginView;
//    private DashboardView dashboardView;

    public MainFrame() {
        setTitle("HỆ THỐNG QUẢN LÝ NHÀ HÀNG");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        loginView = new LoginView();
        contentPanel.add(loginView, "Login");

        cardLayout.show(contentPanel, "Login");

        add(contentPanel);
        setVisible(true);
    }


    public void loginSuccessful(UserModel user){
        this.userModel = user;

        if("Nhan Vien".equals(userModel.getRole()) || "Quan Ly".equals(userModel.getRole())){
            EmployeeDAO  employeeDAO = new EmployeeDAO();
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

        // Refresh
        revalidate();
        repaint();
    }

    private void initializePanels() {

    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(100, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(bg);

        JLabel logoLabel = new JLabel("Quản lý nhà hàng");
        logoLabel.setFont(fontL);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sidebarPanel.add(logoLabel);


    }
}
