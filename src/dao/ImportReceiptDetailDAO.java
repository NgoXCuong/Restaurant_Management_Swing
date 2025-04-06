package dao;

import model.ImportReceiptDetailModel;
import model.IngredientModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ImportReceiptDetailDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private IngredientDAO ingredientDAO = new IngredientDAO();

    public List<ImportReceiptDetailModel> getDetailsByReceiptId(int id_Import) {
        List<ImportReceiptDetailModel> details = new ArrayList<>();
        String sql = "SELECT * FROM CTNK WHERE ID_NK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_Import);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportReceiptDetailModel detail = extractDetailFromResultSet(rs);

                    // Load ingredient information
                    IngredientModel ingredient = ingredientDAO.getIngredientById(detail.getId_Ingredients());
                    detail.setIngredient(ingredient);

                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach phieu nhap kho: " + e.getMessage());
        }

        return details;
    }

    public boolean addDetail(ImportReceiptDetailModel detail) {
        String sql = "INSERT INTO CTNK (ID_NK, ID_NL, SoLuong) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getId_Import());
            ps.setInt(2, detail.getId_Ingredients());
            ps.setInt(3, detail.getQuantity());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them phieu nhap kho: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDetail(ImportReceiptDetailModel detail) {
        String sql = "UPDATE CTNK SET SoLuong = ? WHERE ID_NK = ? AND ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getQuantity());
            ps.setInt(2, detail.getId_Import());
            ps.setInt(3, detail.getId_Ingredients());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua phieu nhap kho: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDetail(int id_Import, int id_Ingredients) {
        String sql = "DELETE FROM CTNK WHERE ID_NK = ? AND ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_Import);
            ps.setInt(2, id_Ingredients);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa phieu nhap kho: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDetailsByReceiptId(int id_Import) {
        String sql = "DELETE FROM CTNK WHERE ID_NK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_Import);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa phieu nhap kho theo id: " + e.getMessage());
            return false;
        }
    }

    private ImportReceiptDetailModel extractDetailFromResultSet(ResultSet rs) throws SQLException {
        ImportReceiptDetailModel detail = new ImportReceiptDetailModel();
        detail.setId_Import(rs.getInt("ID_NK"));
        detail.setId_Ingredients(rs.getInt("ID_NL"));
        detail.setQuantity(rs.getInt("SoLuong"));
        detail.setSubTotal(rs.getInt("Thanhtien"));
        return detail;
    }
}
