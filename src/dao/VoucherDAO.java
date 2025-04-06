package dao;

import model.VoucherModel;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class VoucherDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    
    public List<VoucherModel> getAllVouchers() {
        List<VoucherModel> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM Voucher";

        try (Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                VoucherModel voucher = extractVoucherFromResultSet(rs);
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach voucher: "+ e.getMessage());
        }
        return vouchers;
    }

    public VoucherModel getVoucherByCode(String code) {
        String sql = "SELECT * FROM Voucher WHERE Code_Voucher = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractVoucherFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay voucher theo code: "+ e.getMessage());
        }
        return null;
    }

    public List<VoucherModel> getVouchersByCategory(String category) {
        List<VoucherModel> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM Voucher WHERE LoaiMA = ? OR LoaiMA = 'All'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherModel voucher = extractVoucherFromResultSet(rs);
                    vouchers.add(voucher);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay voucher theo mon an: "+ e.getMessage());
        }

        return vouchers;
    }

    public boolean addVoucher(VoucherModel voucher) {
        String sql = "INSERT INTO Voucher (Code_Voucher, Mota, Phantram, LoaiMA, SoLuong, Diem) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, voucher.getCodeVoucher());
            ps.setString(2, voucher.getDescription());
            ps.setInt(3, voucher.getDiscountPercent());
            ps.setString(4, voucher.getDishCategory());
            ps.setInt(5, voucher.getQuantity());
            ps.setInt(6, voucher.getPoints());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi them voucher: "+ e.getMessage());
            return false;
        }
    }

    public boolean updateVoucher(VoucherModel voucher) {
        String sql = "UPDATE Voucher SET Mota = ?, Phantram = ?, LoaiMA = ?, SoLuong = ?, Diem = ? WHERE Code_Voucher = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, voucher.getDescription());
            ps.setInt(2, voucher.getDiscountPercent());
            ps.setString(3, voucher.getDishCategory());
            ps.setInt(4, voucher.getQuantity());
            ps.setInt(5, voucher.getPoints());
            ps.setString(6, voucher.getCodeVoucher());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua voucher: "+ e.getMessage());
            return false;
        }
    }

    public boolean deleteVoucher(String code) {
        String sql = "DELETE FROM Voucher WHERE Code_Voucher = ?";

        try ( PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa voucher: "+ e.getMessage());
            return false;
        }
    }

    public String generateVoucherCode() {
        // Generate a random 4-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private VoucherModel extractVoucherFromResultSet(ResultSet rs) throws SQLException {
        VoucherModel voucher = new VoucherModel();
        voucher.setCodeVoucher(rs.getString("Code_Voucher"));
        voucher.setDescription(rs.getString("Mota"));
        voucher.setDiscountPercent(rs.getInt("Phantram"));
        voucher.setDishCategory(rs.getString("LoaiMA"));
        voucher.setQuantity(rs.getInt("SoLuong"));
        voucher.setPoints(rs.getInt("Diem"));
        return voucher;
    }
}
