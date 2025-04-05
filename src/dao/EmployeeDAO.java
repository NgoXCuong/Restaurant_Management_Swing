package dao;

import model.EmployeeModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EmployeeDAO {
    private static final Connection con = DatabaseConnection.getConnection();

    public List<EmployeeModel> getAllEmployees() {
        List<EmployeeModel> employees = new ArrayList<EmployeeModel>();
        String sql = "SELECT * FROM NhanVien";

        try(PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                EmployeeModel emp = extractEmployeeFromResultSet(rs);
                employees.add(emp);
            }
        } catch (SQLException e){
            System.err.println("Loi lay danh sach nhan vien:" + e.getMessage());
        }
        return employees;
    }

    public EmployeeModel getEmployeeById(int id) {
        String sql = "SELECT * FROM NhanVien WHERE ID_NV = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return extractEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach nhan vien theo ID:" + e.getMessage());
        }
        return null;
    }

    public EmployeeModel getEmployeeByIdUser(int idUser) {
        String sql = "SELECT * FROM NhanVien WHERE ID_USER = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, idUser);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return extractEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach nhan vien theo id nguoi dung:" + e.getMessage());
        }
        return null;
    }

    public boolean addEmployee(EmployeeModel employee) {
        String sql = "INSERT INTO NhanVien VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, employee.getId_Employee());
            ps.setString(2, employee.getName());
            ps.setDate(3, employee.getHireDate());
            ps.setString(4, employee.getPhone());
            ps.setString(5, employee.getPosition());

            if (employee.getId_User() != null) {
                ps.setInt(6, employee.getId_User());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            if (employee.getId_Manager() != null) {
                ps.setInt(7, employee.getId_Manager());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setString(8, employee.getStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them nhan vien: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(EmployeeModel employee) {
        String sql = "UPDATE NhanVien SET TenNV = ?, NgayVL = ?, SDT = ?, Chucvu = ?, ID_ND = ?, ID_NQL = ?, Tinhtrang = ? WHERE ID_NV = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employee.getName());
            ps.setDate(2, new java.sql.Date(employee.getHireDate().getTime()));
            ps.setString(3, employee.getPhone());
            ps.setString(4, employee.getPosition());

            if (employee.getId_User() != null) {
                ps.setInt(5, employee.getId_User());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            if (employee.getId_Manager() != null) {
                ps.setInt(6, employee.getId_Manager());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.setString(7, employee.getStatus());
            ps.setInt(8, employee.getId_Employee());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua nhan vien: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM NhanVien WHERE ID_NV = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa nhan vien: " + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_NV) AS MaxID FROM NhanVien";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id nhan vien tiep theo: " + e.getMessage());
        }
        return 0; // Default starting ID
    }


    private EmployeeModel extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        EmployeeModel employee = new EmployeeModel();
        employee.setId_Employee(rs.getInt("ID_NV"));
        employee.setName(rs.getString("TenNV"));
        employee.setHireDate(rs.getDate("NgayVL"));
        employee.setPhone(rs.getString("SDT"));
        employee.setPosition(rs.getString("Chucvu"));

        // Handle nullable fields
        int userId = rs.getInt("ID_ND");
        if (!rs.wasNull()) {
            employee.setId_User(userId);
        }

        int managerId = rs.getInt("ID_NQL");
        if (!rs.wasNull()) {
            employee.setId_Manager(managerId);
        }

        employee.setStatus(rs.getString("Tinhtrang"));
        return employee;
    }
}
