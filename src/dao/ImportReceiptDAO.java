package dao;

import model.ImportReceiptDetailModel;
import model.ImportReceiptModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ImportReceiptDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private ImportReceiptDetailDAO detailDAO = new ImportReceiptDetailDAO();

    public List<ImportReceiptModel> getAllImportReceipts() {
        List<ImportReceiptModel> receipts = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNK ORDER BY NgayNK DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ImportReceiptModel receipt = extractReceiptFromResultSet(rs);
                receipts.add(receipt);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach phieu nhap kho: "+ e.getMessage());
        }

        return receipts;
    }

    public ImportReceiptModel getImportReceiptById(int id) {
        String sql = "SELECT * FROM PhieuNK WHERE ID_NK = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ImportReceiptModel receipt = extractReceiptFromResultSet(rs);

                    // Load details
                    List<ImportReceiptDetailModel> details = detailDAO.getDetailsByReceiptId(id);
                    receipt.setDetails(details);

                    return receipt;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach phieu nhap kho theo ID: "+ e.getMessage());
        }

        return null;
    }

    public boolean addImportReceipt(ImportReceiptModel receipt) {
        String sql = "INSERT INTO PhieuNK (ID_NK, ID_NV, NgayNK, Tongtien) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, receipt.getId_Import());
            stmt.setInt(2, receipt.getId_Employee());
            stmt.setDate(3, receipt.getDate_Import());
            stmt.setInt(4, receipt.getTotal());

            int rowsAffected = stmt.executeUpdate();

            // Add details if available
            if (rowsAffected > 0 && receipt.getDetails() != null) {
                for (ImportReceiptDetailModel detail : receipt.getDetails()) {
                    detailDAO.addDetail(detail);
                }
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them phieu nhap kho: "+ e.getMessage());
            return false;
        }
    }

    public boolean updateImportReceipt(ImportReceiptModel receipt) {
        String sql = "UPDATE PhieuNK SET ID_NV = ?, NgayNK = ?, Tongtien = ? WHERE ID_NK = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, receipt.getId_Employee());
            stmt.setDate(2, receipt.getDate_Import());
            stmt.setInt(3, receipt.getTotal());
            stmt.setInt(4, receipt.getId_Import());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua phieu nhap kho: "+ e.getMessage());
            return false;
        }
    }

    public boolean deleteImportReceipt(int id) {
        // First delete all details
        detailDAO.deleteDetailsByReceiptId(id);

        // Then delete the receipt
        String sql = "DELETE FROM PhieuNK WHERE ID_NK = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa phieu nhap kho: "+ e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_NK) AS MaxID FROM PhieuNK";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id phieu nhap kho tiep theo: "+ e.getMessage());
        }

        return 100; // Default starting ID
    }

    private ImportReceiptModel extractReceiptFromResultSet(ResultSet rs) throws SQLException {
        ImportReceiptModel receipt = new ImportReceiptModel();
        receipt.setId_Import(rs.getInt("ID_NK"));
        receipt.setId_Employee(rs.getInt("ID_NV"));
        receipt.setDate_Import(rs.getDate("NgayNK"));
        receipt.setTotal(rs.getInt("Tongtien"));
        return receipt;
    }
}
