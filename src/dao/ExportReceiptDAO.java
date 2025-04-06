package dao;

import model.ExportReceiptDetailModel;
import model.ExportReceiptModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ExportReceiptDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private ExportReceiptDetailDAO detailDAO = new ExportReceiptDetailDAO();


    public List<ExportReceiptModel> getAllExportReceipt() {
        List<ExportReceiptModel> exportReceiptList = new ArrayList<ExportReceiptModel>();
        String sql = "SELECT * FROM PhieuXK";

        try(PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()){
                ExportReceiptModel exportReceiptModel = extractReceiptResult(rs);
                exportReceiptList.add(exportReceiptModel);
            }
        }  catch (SQLException e) {
            System.err.println("Loi lay danh sach phieu xuat kho: " + e.getMessage());
        }
        return exportReceiptList;
    }

    public ExportReceiptModel getExportReceiptById(int id) {
        String sql = "SELECT * FROM PhieuXK WHERE ID_XK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    ExportReceiptModel exportReceiptModel = extractReceiptResult(rs);

                    List<ExportReceiptDetailModel> details = detailDAO.getDetailsByReceiptId(id);
                    exportReceiptModel.setDetailsExport(details);

                    return exportReceiptModel;
                }
            }
        } catch (SQLException e){
            System.err.println("Loi lay danh sach phieu xuat kho theo id: " + e.getMessage());
        }
        return null;
    }

    public boolean addExportReceipt(ExportReceiptModel exportReceipt) {
        String sql = "INSERT INTO PhieiXK(ID_XK, ID_NV, NgayXK) VALUES(?,?,?)";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, exportReceipt.getId_Export());
            ps.setInt(2, exportReceipt.getId_Employee());
            ps.setDate(3, exportReceipt.getDate_Export());

            int rows = ps.executeUpdate();

            if(rows > 0 && exportReceipt.getDetailsExport() != null){
                for(ExportReceiptDetailModel detail : exportReceipt.getDetailsExport()){
                    detailDAO.addDetail(detail);
                }
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi them phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public boolean updateExportReceipt(ExportReceiptModel receipt) {
        String sql = "UPDATE PhieuXK SET ID_NV = ?, NgayXK = ? WHERE ID_XK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receipt.getId_Employee());
            ps.setDate(2, receipt.getDate_Export());
            ps.setInt(3, receipt.getId_Export());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteExportReceipt(int id) {
        // First delete all details
        detailDAO.deleteDetailsByReceiptId(id);

        // Then delete the receipt
        String sql = "DELETE FROM PhieuXK WHERE ID_XK = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa phieu xuat kho: " + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_XK) AS MaxID FROM PhieuXK";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id phieu xuat kho tiep theo: " + e.getMessage());
        }

        return 100; // Default starting ID
    }

    private ExportReceiptModel extractReceiptResult(ResultSet rs) throws SQLException {
        ExportReceiptModel receipt = new ExportReceiptModel();
        receipt.setId_Export(rs.getInt("ID_XK"));
        receipt.setId_Employee(rs.getInt("ID_NV"));
        receipt.setDate_Export(rs.getDate("NgayXK"));
        return receipt;
    }
}
