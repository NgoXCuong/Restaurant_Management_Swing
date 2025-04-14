package view;

import dao.CustomerDAO;
import dao.UserDao;
import model.CustomerModel;
import model.UserModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerView extends JPanel {
    private Font fontXl = new Font("Segoe UI", Font.BOLD, 32);
    private Font fontL = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontS = new Font("Segoe UI", Font.PLAIN, 18);

    private Color bg = new Color(52, 73, 94);

    private  JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn;
    private JTextField searchField;
    private JButton searchBtn;

    private CustomerDAO cusDao;
    private UserDao userDao;

    public CustomerView() {
        cusDao = new CustomerDAO();
        userDao = new UserDao();

        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(bg);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("Khách hàng");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(fontL);
        titlePanel.add(titleLabel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchBtn);
        searchPanel.add(searchField);

        createTable();
        JScrollPane scrollPane = new JScrollPane(customerTable);

        JPanel btnPanel = new JPanel();
        addBtn = new JButton("Thêm");
        editBtn = new JButton("Sửa");
        deleteBtn = new JButton("Xóa");
        refreshBtn = new JButton("Làm mới");

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCustomerDialog();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int cusId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
                    showEditCustomerDialog(cusId);
                } else {
                    JOptionPane.showMessageDialog(CustomerView.this,
                            "Hãy lựa chọn khách hàng để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int cusId = Integer.parseInt(customerTable.getValueAt(selectedRow, 0).toString());
                    deleteCustomer(cusId);
                } else {
                    JOptionPane.showMessageDialog(CustomerView.this,
                            "Hãy lựa chọn khách hàng để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCus();
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        add(titlePanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadCus();
    }

    private void createTable() {
        String[] col = {"ID", "TêN", "Ngày tham gia", "Tổng tiền", "Điển tích lũy", "ID_Người dùng"};
        tableModel = new DefaultTableModel(col, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);
    }

    private void loadCus(){
        tableModel.setRowCount(0);
        List<CustomerModel> cus = cusDao.getAllCustomers();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for(CustomerModel customer : cus){
            Object[] rowData ={
                    customer.getId_Customer(),
                    customer.getName(),
                    dateFormat.format(customer.getJoinDate()),
                    customer.getRevenue(),
                    customer.getPoints(),
                    customer.getId_User()
            };
            tableModel.addRow(rowData);
        }
    }

    private void searchCustomers(){
        String search = searchField.getText().trim().toLowerCase();

        if(search.isEmpty()){
            loadCus();
            return;
        }

        tableModel.setRowCount(0);

        List<CustomerModel> cus = cusDao.getAllCustomers();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for(CustomerModel customer : cus){
            if(customer.getName().toLowerCase().contains(search) ||
                String.valueOf(customer.getId_Customer()).contains(search)){
                Object[] rowData = {
                        customer.getId_Customer(),
                        customer.getName(),
                        dateFormat.format(customer.getJoinDate()),
                        customer.getRevenue(),
                        customer.getPoints(),
                        customer.getId_User()
                };
                tableModel.addRow(rowData);
            }
        }
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
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn chắc chắn muốn xóa khách hàng?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            CustomerModel customer = cusDao.getCustomerById(customerId);
            if (customer != null) {
                int userId = customer.getId_Customer();

                if (cusDao.deleteCus(customerId)) {
                    // Also delete the user
                    userDao.deleteUser(userId);
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành cônng", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCus();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
