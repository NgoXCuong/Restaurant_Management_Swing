package dao;

import model.UserModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class UserDao {
    private static final Connection con = DatabaseConnection.getConnection();
    
    public UserModel authenticate(String email, String password) {
        String sql = "SELECT * FROM NguoiDung WHERE Email = ? AND MatKhau = ? AND Trangthai = 'Verified'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserModel user = new UserModel();
                    user.setId_User(rs.getInt("ID_ND"));
                    user.setEmail(rs.getString("Email"));
                    user.setStatus(rs.getString("Trangthai"));
                    user.setRole(rs.getString("Vaitro"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi nguoi dung: " + e.getMessage());
        }
        return null;
    }

    public UserModel getUserById(int id) {
        String sql = "SELECT * FROM NguoiDung WHERE ID_ND = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserModel user = new UserModel();
                    user.setId_User(rs.getInt("ID_ND"));
                    user.setEmail(rs.getString("Email"));
                    user.setPassword(rs.getString("MatKhau"));
                    user.setVerifyCode(rs.getString("VerifyCode"));
                    user.setStatus(rs.getString("Trangthai"));
                    user.setRole(rs.getString("Vaitro"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach nguoi dung theo id: " + e.getMessage());
        }
        return null;
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        String sql = "SELECT * FROM NguoiDung";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId_User(rs.getInt("ID_ND"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("MatKhau"));
                user.setVerifyCode(rs.getString("VerifyCode"));
                user.setStatus(rs.getString("Trangthai"));
                user.setRole(rs.getString("Vaitro"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach nguoi dung: " + e.getMessage());
        }
        return users;
    }

    public boolean addUser(UserModel user) {
        String sql = "INSERT INTO NguoiDung (ID_ND, Email, MatKhau, VerifyCode, Trangthai, Vaitro) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, user.getId_User());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getVerifyCode());
            ps.setString(5, user.getStatus());
            ps.setString(6, user.getRole());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them dung: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(UserModel user) {
        String sql = "UPDATE NguoiDung SET Email = ?, MatKhau = ?, VerifyCode = ?, Trangthai = ?, Vaitro = ? WHERE ID_ND = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getVerifyCode());
            ps.setString(4, user.getStatus());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getId_User());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua nguoi dung: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM NguoiDung WHERE ID_ND = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa nguoi dung: " + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_ND) AS MaxID FROM NguoiDung";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id nguoi dung tiep theo: " + e.getMessage());
        }
        return 100; // Default starting ID
    }
}
