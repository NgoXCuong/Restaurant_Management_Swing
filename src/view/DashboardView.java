package view;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class DashboardView extends JPanel {
    private MainFrame mainFrame;
    private BillDAO billDao;
    private DishDAO dishDao;
    private TableDAO tableDao;
    private CustomerDAO customerDao;
    private IngredientDAO ingredientDao;

    public DashboardView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        billDao = new BillDAO();
        dishDao = new DishDAO();
        tableDao = new TableDAO();
        customerDao = new CustomerDAO();
        ingredientDao = new IngredientDAO();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createWelcomePanel(), BorderLayout.NORTH);
        add(new JScrollPane(createContentPanel()), BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(getWidth(), 80));

        UserModel currentUser = mainFrame.getCurrentUser();
        String welcomeMessage = "Xin chào! ";

        if (currentUser != null) {
            if ("Nhan Vien".equals(currentUser.getRole()) || "Quan Ly".equals(currentUser.getRole())) {
                EmployeeModel emp = mainFrame.getCurrentEmployee();
                welcomeMessage += emp != null ? emp.getName() : currentUser.getEmail();
            } else {
                CustomerModel cus = customerDao.getCustomerByIdND(currentUser.getId_User());
                welcomeMessage += cus != null ? cus.getName() : currentUser.getEmail();
            }
        }

        JLabel welcomeLabel = new JLabel(welcomeMessage);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN")).format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(120, 120, 120));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(dateLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weighty = 0;
        panel.add(createStatsPanel(), gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weighty = 0.5;
        panel.add(createRecentOrdersPanel(), gbc);

        gbc.gridx = 1;
        panel.add(createPopularDishesPanel(), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(createLowStockPanel(), gbc);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(Color.WHITE);

        List<BillModel> bills = billDao.getAllBills();
        List<TableModel> tables = tableDao.getAllTables();
        List<CustomerModel> customers = customerDao.getAllCustomers();

        int totalSales = 0, pendingOrders = 0, availableTables = 0;
        for (BillModel bill : bills) {
            if ("Da thanh toan".equals(bill.getStatus())) totalSales += bill.getTotalAmount();
            if ("Chua thanh toan".equals(bill.getStatus())) pendingOrders++;
        }
        for (TableModel t : tables) {
            if ("Trong".equals(t.getStatus())) availableTables++;
        }

        panel.add(createStatCard("Doanh thu", formatCurrency(totalSales), new Color(41, 128, 185)));
        panel.add(createStatCard("Đơn chờ", String.valueOf(pendingOrders), new Color(231, 76, 60)));
        panel.add(createStatCard("Bàn trống", availableTables + "/" + tables.size(), new Color(39, 174, 96)));
        panel.add(createStatCard("Khách hàng", String.valueOf(customers.size()), new Color(142, 68, 173)));

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color, Icon icon) {
        JPanel panel = new RoundedPanel(20);
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel iconLabel = new JLabel(icon);
        panel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createRecentOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Đơn hàng gần đây"));

        String[] cols = {"Mã HĐ", "Bàn", "Ngày", "Tổng", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<BillModel> bills = billDao.getAllBills();
        bills.sort((a, b) -> b.getDateBill().compareTo(a.getDateBill()));

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (int i = 0; i < Math.min(5, bills.size()); i++) {
            BillModel b = bills.get(i);
            model.addRow(new Object[]{b.getId(), b.getId_Table(), df.format(b.getDateBill()), formatCurrency(b.getTotalAmount()), b.getStatus()});
        }

        JTable table = new JTable(model);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPopularDishesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Món ăn được gọi nhiều"));

        String[] cols = {"Tên món", "Loại", "Giá", "Lượt gọi"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        Map<Integer, Integer> dishCount = new HashMap<>();
        for (BillModel b : billDao.getAllBills()) {
            if (b.getDetails() != null) {
                for (BillDetailModel d : b.getDetails()) {
                    dishCount.put(d.getId_Dish(), dishCount.getOrDefault(d.getId_Dish(), 0) + d.getQuantity());
                }
            }
        }

        List<DishModel> dishes = dishDao.getAllDishes();
        dishes.sort((d1, d2) -> Integer.compare(dishCount.getOrDefault(d2.getId_Dish(), 0), dishCount.getOrDefault(d1.getId_Dish(), 0)));

        for (int i = 0; i < Math.min(5, dishes.size()); i++) {
            DishModel d = dishes.get(i);
            model.addRow(new Object[]{d.getName_Dish(), d.getCategory(), formatCurrency(d.getPrice()), dishCount.getOrDefault(d.getId_Dish(), 0)});
        }

        JTable table = new JTable(model);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Nguyên liệu sắp hết"));

        String[] cols = {"Mã", "Tên",  "Đơn vị", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new RoundedPanel(20);
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
    }

    private String formatCurrency(int amount) {
        return String.format("%,d VND", amount);
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
    }
}