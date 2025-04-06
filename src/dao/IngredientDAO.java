package dao;

import model.IngredientModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class IngredientDAO {
    private static final Connection con = DatabaseConnection.getConnection();

    public List<IngredientModel> getAllIngredients() {
        List<IngredientModel> ingredients = new ArrayList<>();
        String sql = "SELECT n.*, k.SLTon FROM NguyenLieu n LEFT JOIN Kho k ON n.ID_NL = k.ID_NL";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                IngredientModel ingredient = extractIngredientFromResultSet(rs);
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay dannh sach nguyen lieu: " + e.getMessage());
        }
        return ingredients;
    }

    public IngredientModel getIngredientById(int id) {
        String sql = "SELECT n.*, k.SLTon FROM NguyenLieu n LEFT JOIN Kho k ON n.ID_NL = k.ID_NL WHERE n.ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractIngredientFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay dannh sach nguyen lieu theo id: " + e.getMessage());
        }
        return null;
    }

    public boolean addIngredient(IngredientModel ingredient) {
        String sql = "INSERT INTO NguyenLieu (ID_NL, TenNL, Dongia, Donvitinh) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ingredient.getId_Ingredient());
            ps.setString(2, ingredient.getName());
            ps.setInt(3, ingredient.getPrice());
            ps.setString(4, ingredient.getUnit());

            int rowsAffected = ps.executeUpdate();

            // The trigger will automatically add the ingredient to the Kho table

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them nguyen lieu: " + e.getMessage());
            return false;
        }
    }

    public boolean updateIngredient(IngredientModel ingredient) {
        String sql = "UPDATE NguyenLieu SET TenNL = ?, Dongia = ?, Donvitinh = ? WHERE ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ingredient.getName());
            ps.setInt(2, ingredient.getPrice());
            ps.setString(3, ingredient.getUnit());
            ps.setInt(4, ingredient.getId_Ingredient());

            int rowsAffected = ps.executeUpdate();

            // Update stock if needed
//            if (rowsAffected > 0) {
//                updateStock(ingredient.getId_Ingredient(), ingredient.getStock());
//            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua nguyen lieu: " + e.getMessage());
            return false;
        }
    }

//    public boolean updateStock(int ingredientId, int stock) {
//        String sql = "UPDATE Kho SET SLTon = ? WHERE ID_NL = ?";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, stock);
//            ps.setInt(2, ingredientId);
//
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            logger.log(Level.SEVERE, "Error updating stock", e);
//            return false;
//        }
//    }

    public boolean deleteIngredient(int id) {
        // First delete from Kho
        String sqlKho = "DELETE FROM Kho WHERE ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sqlKho)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            // Then delete from NguyenLieu
            String sqlNL = "DELETE FROM NguyenLieu WHERE ID_NL = ?";
            try (PreparedStatement psNL = con.prepareStatement(sqlNL)) {
                psNL.setInt(1, id);
                int rowsAffected = psNL.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Loi xoa nguyen lieu: " + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_NL) AS MaxID FROM NguyenLieu";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id nguyen lieu tiep theo: " + e.getMessage());
        }

        return 100; // Default starting ID
    }

    private IngredientModel extractIngredientFromResultSet(ResultSet rs) throws SQLException {
        IngredientModel ingredient = new IngredientModel();
        ingredient.setId_Ingredient(rs.getInt("ID_NL"));
        ingredient.setName(rs.getString("TenNL"));
        ingredient.setPrice(rs.getInt("Dongia"));
        ingredient.setUnit(rs.getString("Donvitinh"));
//        ingredient.setStock(rs.getInt("SLTon"));
        return ingredient;
    }
}
