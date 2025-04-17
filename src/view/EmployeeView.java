package view;

import dao.EmployeeDAO;
import dao.UserDao;
import model.EmployeeModel;
import model.UserModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarEntry;

public class EmployeeView extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn;
    private JTextField searchField;
    private JButton searchBtn;

    private EmployeeDAO employeeDAO;
    private UserDao userDAO;
    private boolean editMode = true;

    public EmployeeView() {
        employeeDAO = new EmployeeDAO();
        userDAO = new UserDao();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Title Panel with Styling
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 73, 94));
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel titleLabel = new JLabel("Quản lý nhân viên");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        // Search Panel with Enhanced Styling
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBtn = new JButton("Tìm");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setBackground(new Color(41, 128, 185));
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setFocusPainted(false);

        searchBtn.addActionListener(e -> searchEmployees());

        searchPanel.add(new JLabel("Tìm:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Create Table with Modern Styling
        createTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Button Panel with Styled Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addBtn = createButton("Thêm");
        editBtn = createButton("Sửa");
        deleteBtn = createButton("Xóa");
        refreshBtn = createButton("Làm mới");

        addBtn.addActionListener(e -> {
            if (editMode) {
                showAddEmployeeDialog();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Bạn không có quyền thêm nhân viên", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
                if (editMode) {
                    showEditEmployeeDialog(employeeId);
                } else {
                    showViewEmployeeDialog(employeeId);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Làm ơn lựa chọn nhân viên", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            if (editMode) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteEmployee(employeeId);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Làm ơn lựa chọn nhân viên để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Bạn không có quyền xóa nhân viên", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> loadEmployees());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        // Add Components to Main Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEmployees();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        updateButtonText();
    }

    private void updateButtonText() {
        if (!editMode) {
            addBtn.setText("View Employees");
            editBtn.setText("View Details");
            deleteBtn.setEnabled(false);
        } else {
            addBtn.setText("Add Employee");
            editBtn.setText("Edit Employee");
            deleteBtn.setEnabled(true);
        }
    }

    private void createTable() {
        String[] columns = {"ID", "Tên nhân viên", "Chức vụ", "SDT", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.setRowHeight(30);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(120);
//        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(80);
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        java.util.List<EmployeeModel> employees = employeeDAO.getAllEmployees();

        for (EmployeeModel employee : employees) {
            Object[] rowData = {
                    employee.getId_Employee(),
                    employee.getName(),
                    employee.getPosition(),
                    employee.getPhone(),
                    employee.getStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    private void searchEmployees() {
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            loadEmployees();
            return;
        }

        tableModel.setRowCount(0);

        java.util.List<EmployeeModel> employees = employeeDAO.getAllEmployees();

        for (EmployeeModel employee : employees) {
            if (employee.getName().toLowerCase().contains(searchTerm) ||
                    employee.getPosition().toLowerCase().contains(searchTerm) ||
                    employee.getPhone().toLowerCase().contains(searchTerm)) {
                Object[] rowData = {
                        employee.getId_Employee(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getPhone(),
                        employee.getStatus()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void showAddEmployeeDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        String[] positions = {"Quan Ly", "Thu Ngan", "Phuc Vu", "Dau Bep", "Bao Ve"};
        JComboBox<String> positionComboBox = new JComboBox<>(positions);
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField birthdayField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        JTextField joinDateField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        String[] statuses = {"Dang lam", "Da nghi"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);

        // User account fields
        JCheckBox createAccountCheckBox = new JCheckBox("Tạo tài khoản người dùng");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        createAccountCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = createAccountCheckBox.isSelected();
                usernameField.setEnabled(selected);
                passwordField.setEnabled(selected);
            }
        });

        usernameField.setEnabled(false);
        passwordField.setEnabled(false);

        formPanel.add(new JLabel("Tên:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Chức vụ:"));
        formPanel.add(positionComboBox);
        formPanel.add(new JLabel("SDT:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Địa chỉ:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Lương:"));
        formPanel.add(salaryField);
        formPanel.add(new JLabel("Ngày sinh (dd-MM-yyyy):"));
        formPanel.add(birthdayField);
        formPanel.add(new JLabel("Ngày tham gia (dd-MM-yyyy):"));
        formPanel.add(joinDateField);
        formPanel.add(new JLabel("Tình trạng:"));
        formPanel.add(statusComboBox);

        JPanel accountPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        accountPanel.setBorder(BorderFactory.createTitledBorder("Tài khoản người dùng"));

        accountPanel.add(createAccountCheckBox);
        accountPanel.add(new JLabel());
        accountPanel.add(new JLabel("Tài khoản:"));
        accountPanel.add(usernameField);
        accountPanel.add(new JLabel("Mật khẩu:"));
        accountPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String position = (String) positionComboBox.getSelectedItem();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String birthdayText = birthdayField.getText().trim();
                String joinDateText = joinDateField.getText().trim();
                String status = (String) statusComboBox.getSelectedItem();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() ||
                        salaryText.isEmpty() || birthdayText.isEmpty() || joinDateText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Làm ơn không bỏ trống", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int salary;
                try {
                    salary = Integer.parseInt(salaryText);
                    if (salary <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Lương phải lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Lương phải là số", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date birthday, joinDate;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    birthday = dateFormat.parse(birthdayText);
                    joinDate = dateFormat.parse(joinDateText);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập ngày hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create employee
                EmployeeModel employee = new EmployeeModel();
                employee.setId_Employee(employeeDAO.getNextId());
                employee.setName(name);
                employee.setPosition(position);
                employee.setPhone(phone);
                employee.setStatus(status);

                // Create user account if checked
                if (createAccountCheckBox.isSelected()) {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();

                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tài khoản và mật khẩu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create user
                    UserModel user = new UserModel();
                    user.setId_User(userDAO.getNextId());
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setRole("Nhan Vien");

                    if (userDAO.addUser(user)) {
                        employee.setId_User(user.getId_User());
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi tạo tài khoản", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (employeeDAO.addEmployee(employee)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadEmployees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(accountPanel, BorderLayout.CENTER);

        dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showViewEmployeeDialog(int employeeId) {
        EmployeeModel employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết nhân viên", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridLayout(9, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        JLabel nameLabel = new JLabel("<html><b>Tên:</b> " + employee.getName() + "</html>");
        JLabel positionLabel = new JLabel("<html><b>Chức vụ:</b> " + employee.getPosition() + "</html>");
        JLabel phoneLabel = new JLabel("<html><b>STD:</b> " + employee.getPhone() + "</html>");
        JLabel statusLabel = new JLabel("<html><b>Trạng thái:</b> " + employee.getStatus() + "</html>");

        detailsPanel.add(nameLabel);
        detailsPanel.add(positionLabel);
        detailsPanel.add(phoneLabel);
        detailsPanel.add(statusLabel);

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Đóng");

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(closeButton);

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditEmployeeDialog(int employeeId) {
        EmployeeModel employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa nhân viên", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(employee.getName());
        JComboBox<String> positionComboBox = new JComboBox<>(new String[] {"Quan Ly", "Thu Ngan", "Phuc Vu", "Dau Bep", "Bao Ve"});
        positionComboBox.setSelectedItem(employee.getPosition());
        JTextField phoneField = new JTextField(employee.getPhone());

        String[] statuses = {"Dang lam", "Da nghi"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setSelectedItem(employee.getStatus());

        formPanel.add(new JLabel("Tên:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Chức vụ:"));
        formPanel.add(positionComboBox);
        formPanel.add(new JLabel("SDT:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Trạng thái:"));
        formPanel.add(statusComboBox);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String position = (String) positionComboBox.getSelectedItem();
                String phone = phoneField.getText().trim();
                String status = (String) statusComboBox.getSelectedItem();

                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                employee.setName(name);
                employee.setPosition(position);
                employee.setPhone(phone);
                employee.setStatus(status);

                if (employeeDAO.updateEmployee(employee)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadEmployees();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi câp nhật nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteEmployee(int employeeId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn chắc chắn muốn xóa nhân viên?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            EmployeeModel employee = employeeDAO.getEmployeeById(employeeId);
            if (employee != null && employee.getId_User() > 0) {
                int deleteUser = JOptionPane.showConfirmDialog(this,
                        "Nhân viên này có một tài khoản người dùng. Hãy xóa luôn tài khoản người dùng đó?",
                        "Xóa tài khoản người dùng", JOptionPane.YES_NO_CANCEL_OPTION);

                if (deleteUser == JOptionPane.CANCEL_OPTION) {
                    return;
                }

                if (deleteUser == JOptionPane.YES_OPTION) {
                    userDAO.deleteUser(employee.getId_User());
                }
            }

            if (employeeDAO.deleteEmployee(employeeId)) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}