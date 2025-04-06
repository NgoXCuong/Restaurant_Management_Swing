package dao;

import model.TableModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TableDAO {
    private static final Connection con = DatabaseConnection.getConnection();

    public List<TableModel> getAllTables() {
        List<TableModel> tables = new ArrayList<>();
        String sql = "SELECT * FROM Ban";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                TableModel table = new TableModel();
                table.setId_Table(rs.getInt("ID_Ban"));
                table.setName(rs.getString("TenBan"));
                table.setLocation(rs.getString("Vitri"));
                table.setStatus(rs.getString("Trangthai"));
                tables.add(table);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach ban: " + e.getMessage());
        }
        return tables;
    }

    public List<TableModel> getAvailableTables() {
        List<TableModel> tables = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE Trangthai = 'Con trong'";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                TableModel table = new TableModel();
                table.setId_Table(rs.getInt("ID_Ban"));
                table.setName(rs.getString("TenBan"));
                table.setLocation(rs.getString("Vitri"));
                table.setStatus(rs.getString("Trangthai"));
                tables.add(table);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach ban con trong: " + e.getMessage());
        }
        return tables;
    }

    public TableModel getTableById(int id) {
        String sql = "SELECT * FROM Ban WHERE ID_Ban = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TableModel table = new TableModel();
                    table.setId_Table(rs.getInt("ID_Ban"));
                    table.setName(rs.getString("TenBan"));
                    table.setLocation(rs.getString("Vitri"));
                    table.setStatus(rs.getString("Trangthai"));
                    return table;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach ban theo id: " + e.getMessage());
        }

        return null;
    }

    public boolean addTable(TableModel table) {
        String sql = "INSERT INTO Ban (ID_Ban, TenBan, Vitri, Trangthai) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, table.getId_Table());
            ps.setString(2, table.getName());
            ps.setString(3, table.getLocation());
            ps.setString(4, table.getStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them ban: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTable(TableModel table) {
        String sql = "UPDATE Ban SET TenBan = ?, Vitri = ?, Trangthai = ? WHERE ID_Ban = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, table.getName());
            ps.setString(2, table.getLocation());
            ps.setString(3, table.getStatus());
            ps.setInt(4, table.getId_Table());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua ban: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTable(int id) {
        String sql = "DELETE FROM Ban WHERE ID_Ban = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa ban: " + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_Ban) AS MaxID FROM Ban";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id ban tiep theo: " + e.getMessage());
        }

        return 100; // Default starting ID
    }
}
