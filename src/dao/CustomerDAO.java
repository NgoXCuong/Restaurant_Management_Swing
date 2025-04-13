package dao;

import model.CustomerModel;
import model.DishModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final Connection con = DatabaseConnection.getConnection();

    public List<CustomerModel> getAllCustomers() {
        List<CustomerModel> customers = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try(PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CustomerModel customer = extractCustomerFromResultSet(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println("Loi lay danh sach khach hang: " + e.getMessage());
        }
        return customers;
    }

    public CustomerModel getCustomerById(int id) {
        String sql = "SELECT * FROM KhachHang WHERE ID_KH = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id khach hang theo id: " + e.getMessage());
        }
        return null;
    }

    public CustomerModel getCustomerByIdND(int id_use) {
        String sql = "SELECT * FROM KhachHang WHERE ID_ND = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id_use);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id nguoi dung: " + e.getMessage());
        }
        return null;
    }

    public boolean addCus (CustomerModel customer) {
        String sql = "INSERT INTO KhachHang VALUES (?,?,?,?,?,?)";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, customer.getId_Customer());
            ps.setString(2, customer.getName());
            ps.setDate(3, customer.getJoinDate());
            ps.setInt(4, customer.getRevenue());
            ps.setInt(5, customer.getPoints());
            ps.setInt(6, customer.getId_User());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi them nguoi dung: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCus(CustomerModel cus){
        String sql = "UPDATE KhachHang SET TenKH = ?, Ngaythamgia = ?, Doanhso = ?, Diemtichluy = ?, ID_ND WHERE ID_KH = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, cus.getId_Customer());
            ps.setString(2, cus.getName());
            ps.setDate(3, cus.getJoinDate());
            ps.setInt(4, cus.getRevenue());
            ps.setInt(5, cus.getPoints());
            ps.setInt(6, cus.getId_User());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua khach hang:" + e.getMessage());
            return false;
        }
    }

    public boolean deleteCus(int id){
        String sql = "DELETE FROM KhachHang WHERE ID_KH = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa khach hang:" + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_MonAn) AS MaxId FROM KhachHang";

        try(PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("MaxId") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id khach hang tiep:" + e.getMessage());
        }
        return 0;
    }

    private CustomerModel extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        CustomerModel customer = new CustomerModel();
        customer.setId_Customer(rs.getInt("ID_KH"));
        customer.setName(rs.getString("TenKH"));
        customer.setJoinDate(rs.getDate("Ngaythamgia"));
        customer.setRevenue(rs.getInt("Doanhso"));
        customer.setPoints(rs.getInt("Diemtichluy"));
        customer.setId_User(rs.getInt("ID_ND"));
        return customer;
    }
}
