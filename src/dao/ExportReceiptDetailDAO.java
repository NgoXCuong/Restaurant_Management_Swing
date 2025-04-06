package dao;

import model.ExportReceiptDetailModel;
import model.IngredientModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ExportReceiptDetailDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private IngredientDAO ingredientDAO = new IngredientDAO();

    public List<ExportReceiptDetailModel> getDetailsByReceiptId(int receiptId) {
        List<ExportReceiptDetailModel> details = new ArrayList<>();
        String sql = "SELECT * FROM CTXK WHERE ID_XK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receiptId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExportReceiptDetailModel detail = extractDetailFromResultSet(rs);

                    // Load ingredient information
                    IngredientModel ingredient = ingredientDAO.getIngredientById(detail.getId_Ingredient());
                    detail.setIngredient(ingredient);

                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach chi tiet phieu xuat kho: " + e.getMessage());
        }
        return details;
    }

    public boolean addDetail(ExportReceiptDetailModel detail) {
        String sql = "INSERT INTO CTXK (ID_XK, ID_NL, SoLuong) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getId_Export());
            ps.setInt(2, detail.getId_Ingredient());
            ps.setInt(3, detail.getQuantity());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them chi tiet phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDetail(ExportReceiptDetailModel detail) {
        String sql = "UPDATE CTXK SET SoLuong = ? WHERE ID_XK = ? AND ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getQuantity());
            ps.setInt(2, detail.getId_Export());
            ps.setInt(3, detail.getId_Ingredient());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua chi tiet phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDetail(int receiptId, int ingredientId) {
        String sql = "DELETE FROM CTXK WHERE ID_XK = ? AND ID_NL = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receiptId);
            ps.setInt(2, ingredientId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa chi tiet phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDetailsByReceiptId(int receiptId) {
        String sql = "DELETE FROM CTXK WHERE ID_XK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receiptId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa chi tiet phieu xuat kho theo id: " + e.getMessage());
            return false;
        }
    }
    
    private ExportReceiptDetailModel extractDetailFromResultSet(ResultSet rs) throws SQLException {
        ExportReceiptDetailModel detail = new ExportReceiptDetailModel();
        detail.setId_Export(rs.getInt("ID_XK"));
        detail.setId_Ingredient(rs.getInt("ID_NL"));
        detail.setQuantity(rs.getInt("SoLuong"));
        return detail;
    }
}
