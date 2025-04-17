package view;

import dao.CustomerDAO;
import dao.UserDao;
import model.CustomerModel;
import model.UserModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerView extends JPanel {
    private final Font fontTitle = new Font("Segoe UI", Font.BOLD, 28);
    private final Font fontChu = new Font("Segoe UI", Font.BOLD, 18);
    private final Font fontButton = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font fontTable = new Font("Segoe UI", Font.PLAIN, 14);

    private final Color primaryColor = new Color(41, 128, 185);
    private final Color backgroundColor = new Color(236, 240, 241);
    private final Color buttonColor = new Color(52, 152, 219);

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private final CustomerDAO cusDao = new CustomerDAO();
    private final UserDao userDao = new UserDao();

    public CustomerView() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản lý Khách hàng", SwingConstants.CENTER);
        titleLabel.setFont(fontTitle);
        titleLabel.setForeground(Color.WHITE);
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(primaryColor);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(backgroundColor);
        searchField = new JTextField(25);
        JButton searchBtn = createStyledButton("Tìm kiếm");
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setBackground(Color.MAGENTA);
        searchBtn.addActionListener(e -> searchCustomers());

        // Label "Tìm:" to và đậm
        JLabel searchLabel = new JLabel("Tìm:");
        searchLabel.setForeground(Color.BLACK);
        searchLabel.setFont(fontChu); // Chữ to hơn
        searchPanel.add(searchLabel);

        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Bảng dữ liệu
        createTable();
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryColor, 1));

        // Các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(backgroundColor);

        JButton addBtn = createStyledButton("Thêm");
        addBtn.setForeground(Color.BLACK);

        JButton editBtn = createStyledButton("Sửa");
        editBtn.setForeground(Color.BLACK);

        JButton deleteBtn = createStyledButton("Xóa");
        deleteBtn.setForeground(Color.BLACK);

        JButton refreshBtn = createStyledButton("Làm mới");
        refreshBtn.setForeground(Color.BLACK);

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        addBtn.addActionListener(e -> showAddCustomerDialog());
        editBtn.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int cusId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
                showEditCustomerDialog(cusId);
            } else {
                JOptionPane.showMessageDialog(this, "Chọn khách hàng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int cusId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
                deleteCustomer(cusId);
            } else {
                JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> loadCus());

        // Panel chính giữa
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadCus();
    }

    private void createTable() {
        String[] columns = {"ID", "Tên", "Ngày tham gia", "Tổng tiền", "Điểm tích lũy", "ID Người dùng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(fontTable);
        customerTable.setRowHeight(24);
        customerTable.getTableHeader().setFont(fontButton);
        customerTable.getTableHeader().setBackground(new Color(189, 195, 199));
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadCus() {
        tableModel.setRowCount(0);
        List<CustomerModel> list = cusDao.getAllCustomers();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (CustomerModel c : list) {
            Object[] row = {
                    c.getId_Customer(),
                    c.getName(),
                    df.format(c.getJoinDate()),
                    c.getRevenue(),
                    c.getPoints(),
                    c.getId_User()
            };
            tableModel.addRow(row);
        }
    }

    private void searchCustomers() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadCus();
            return;
        }

        tableModel.setRowCount(0);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (CustomerModel c : cusDao.getAllCustomers()) {
            if (c.getName().toLowerCase().contains(keyword)
                    || String.valueOf(c.getId_Customer()).contains(keyword)) {
                Object[] row = {
                        c.getId_Customer(),
                        c.getName(),
                        df.format(c.getJoinDate()),
                        c.getRevenue(),
                        c.getPoints(),
                        c.getId_User()
                };
                tableModel.addRow(row);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(fontButton);
        btn.setFocusPainted(false);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return btn;
    }

        private void showAddCustomerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Customer", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        formPanel.add(new JLabel("Tên: "));
        formPanel.add(nameField);
        formPanel.add(new JLabel("E-mail: "));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Mật khẩu: "));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Nhập lại mật khẩu: "));
        formPanel.add(confirmPasswordField);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Lưu");
        JButton cancelBtn = new JButton("Hủy");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = String.valueOf(passwordField.getPassword());
                String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    JOptionPane.showMessageDialog(dialog, "Không được bỏ trống", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!password.equals(confirmPassword)){
                    JOptionPane.showMessageDialog(dialog, "Mật khẩu không đúng", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserModel user = new UserModel();
                user.setId_User(userDao.getNextId());
                user.setEmail(email);
                user.setPassword(password);
                user.setStatus("Verified");
                user.setRole("Khach Hang");

                if(userDao.addUser(user)){
                    CustomerModel cus =new CustomerModel();
                    cus.setId_Customer(cusDao.getNextId());
                    cus.setName(name);
                    cus.setJoinDate(new Date());
                    cus.setRevenue(0);
                    cus.setPoints(0);
                    cus.setId_User(user.getId_User());

                    if(cusDao.addCus(cus)){
                        JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadCus();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi thêm khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        userDao.deleteUser(user.getId_User());
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi thêm khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showEditCustomerDialog(int  idCus){
        CustomerModel cus = cusDao.getCustomerById(idCus);
        if(cus == null){
            JOptionPane.showMessageDialog(this,  "Không tìm thấy khách hàng",  "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserModel user = userDao.getUserById(cus.getId_User());
        if(user == null){
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Dialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Customer", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(cus.getName());
        JTextField emailField = new JTextField(user.getEmail());
        JTextField pointsField = new JTextField(String.valueOf(cus.getPoints()));

        formPanel.add(new JLabel("Tên:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Điểm tích lũy:"));
        formPanel.add(pointsField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String pointsText = pointsField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || pointsText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Không được bỏ trống", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int points;
                try {
                    points = Integer.parseInt(pointsText);
                    if (points < 0) {
                        JOptionPane.showMessageDialog(dialog, "Điểm tích lũy không hợp lệ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Điểm tích lũy phải là số", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update user
                user.setEmail(email);

                if (userDao.updateUser(user)) {
                    // Update customer
                    cus.setName(name);
                    cus.setPoints(points);

                    if (cusDao.updateCus(cus)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa khách hàng thành công", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadCus();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi sửa khách hàng", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi sửa khách hàng", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteCustomer(int customerId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            CustomerModel cus = cusDao.getCustomerById(customerId);
            if (cus != null) {
                int userId = cus.getId_User();
                if (cusDao.deleteCus(customerId)) {
                    userDao.deleteUser(userId);
                    JOptionPane.showMessageDialog(this, "Xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadCus();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
