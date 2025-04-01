package dao;

import model.BillDetailModel;
import model.BillModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private BillDetailDAO billDetailDAO = new BillDetailDAO();

    private BillModel extractBill(ResultSet rs) throws SQLException {
        BillModel bill = new BillModel();
        bill.setId(rs.getInt("ID_HoaDon"));
        bill.setId_Customer(rs.getInt("ID_KH"));
        bill.setId_Table(rs.getInt("ID_Ban"));
        bill.setDateBill(rs.getDate("NgayHD"));
        bill.setFoodMoney(rs.getInt("TienMonAn"));
        bill.setCodeVoucher(rs.getString("Code_Voucher"));
        bill.setDiscount(rs.getInt("TienGiam"));
        bill.setTotalAmount(rs.getInt("Tongtien"));
        bill.setStatus(rs.getString("Trangthai"));

        return bill;
    }

    public List<BillModel> getAllBills() throws SQLException {
        List<BillModel> bills = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BillModel bill = extractBill(rs);
                bills.add(bill);
            }
        } catch (SQLException e) {
             System.err.println("Lỗi lấy tất cả hóa đơn:" + e.getMessage());
        }
        return bills;
    }

    public  List<BillModel> getBillById_Cus(int id_Customer) throws SQLException{
        List<BillModel> bills = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE ID_KH = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_Customer);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillModel bill = extractBill(rs);
                    bills.add(bill);
                }
            }
            catch (SQLException e) {
                System.err.println("Lỗi lấy hóa đơn khách hàng theo ID" + e.getMessage());
            }
        }
        return bills;
    }

    public BillModel getBillById(int id) throws SQLException{
        String sql = "SELECT * FROM HoaDon WHERE ID_HoaDon = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    BillModel bill = extractBill(rs);
                    List<BillDetailModel> billDetails = billDetailDAO.getBillDetailsByBillID(id);
                    bill.setDetails(billDetails);
                    return bill;
                }
            }
            catch (SQLException e) {
                System.err.println("Lỗi lấy hóa đơn theo ID" + e.getMessage());
            }
            return null;
        }
    }

    public boolean addBill(BillModel bill) throws SQLException{
        String sql = "INSERT INTO HoaDon(ID_HD, ID_KH, ID_Ban, NgayHD, TienMonAn, " +
                "Code_Voucher, TienGiam, Tongtien, Trangthai) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bill.getId());
            ps.setInt(2, bill.getId_Customer());
            ps.setInt(3, bill.getId_Table());
            ps.setDate(4, bill.getDateBill());
            ps.setInt(5, bill.getFoodMoney());
            ps.setString(6, bill.getCodeVoucher());
            ps.setInt(7, bill.getDiscount());
            ps.setInt(8, bill.getTotalAmount());
            ps.setString(5, bill.getStatus());

            int rows = ps.executeUpdate();
            if(rows > 0 && bill.getDetails() != null) {
                for(BillDetailModel billDetail : bill.getDetails()) {
                    billDetailDAO.addBillDetail(billDetail);
                }
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Thêm hóa đơn thất bại " + e.getMessage());
            return false;
        }
    }

    public boolean updateBill(BillModel bill) throws SQLException{
        String sql = "UPDATE HoaDon SET ID_KH = ?, ID_Ban = ?, NgayHD = ?" +
                "TienMonAn = ?, Code_Voucher = ?, TienGiam = ?, Tongtien = ?, Trangthai = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bill.getId_Customer());
            ps.setInt(2, bill.getId_Table());
            ps.setDate(3, bill.getDateBill());
            ps.setInt(4, bill.getFoodMoney());
            ps.setString(5, bill.getCodeVoucher());
            ps.setInt(6, bill.getDiscount());
            ps.setInt(7, bill.getTotalAmount());
            ps.setString(8, bill.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Sửa hóa đơn thấ bại" + e.getMessage());
            return false;
        }
    }

    public boolean deleteBill(int id) throws SQLException{
        billDetailDAO.deleteBillDetailsByBillId(id);
        String sql = "DELETE FROM HoaDon WHERE ID_HoaDon = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }  catch (SQLException e) {
            System.err.println("Xóa hóa đơn thất bại" + e.getMessage());
            return false;
        }
    }

    public int getNextID() throws SQLException{
        String sql = "SELECT MAX(ID_HoaDon) AS MaxID FROM HoaDon";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("MaxID") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thêm ID tiếp theo" + e.getMessage());
        }
        return 1;
    }
}
