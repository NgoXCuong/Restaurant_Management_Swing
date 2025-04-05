package dao;

import model.BillDetailModel;
import model.DishModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDetailDAO {
    private static final Connection con = DatabaseConnection.getConnection();
    private DishDAO dishDAO = new DishDAO();

    public List<BillDetailModel> getBillDetailsByBillID(int id_Bill) {
        List<BillDetailModel> billDetails = new ArrayList<BillDetailModel>();
        String sql = "SELECT * FROM CTHD WHERE ID_HoaDon = ?";

        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_Bill);
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    BillDetailModel billDetail = new BillDetailModel();
                    billDetail.setId_Bill(rs.getInt("ID_HoaDon"));
                    billDetail.setId_Dish(rs.getInt("ID_MonAn"));
                    billDetail.setQuantity(rs.getInt("SoLuong"));
                    billDetail.setSubTotal(rs.getInt("Thanhtien"));

                    DishModel dish = new DishModel();
                    billDetail.setDish(dish);

                    billDetails.add(billDetail);
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Lỗi lấy hóa đơn theo ID" + e.getMessage());
        }
        return billDetails;
    }

    public boolean addBillDetail(BillDetailModel billDetail) throws SQLException{
        String sql = "INSERT INTO CTHD(ID_HoaDon, ID_MonAn, SoLuong) VALUES(?,?,?)";

        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billDetail.getId_Bill());
            ps.setInt(2, billDetail.getId_Dish());
            ps.setInt(3, billDetail.getQuantity());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Thêm chi tiết hóa đơn thất bại" + e.getMessage());
            return false;
        }
    }

    public boolean updateBillDetail(BillDetailModel billDetail) throws SQLException{
        String sql = "UPDATE CTHD SET SoLuong = ? WHERE ID_HoaDon = ? AND ID_MonAn = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billDetail.getQuantity());
            ps.setInt(2, billDetail.getId_Bill());
            ps.setInt(3, billDetail.getId_Dish());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Sửa chi tiết hóa đơn thất bại" + e.getMessage());
            return false;
        }
    }

    public boolean deleteBillDetail(int id_Bill, int id_Dish) throws SQLException{
        String sql = "DELETE FROM CTHD WHERE ID_HoaDon = ? AND ID_MonAn = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_Bill);
            ps.setInt(2, id_Dish);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Xóa chi tiết hóa đơn theo ID_HoaDon và ID_MonAn thất bại" + e.getMessage());
            return false;
        }
    }

    public boolean deleteBillDetailByID(int id_Bill) throws SQLException{
        String sql = "DELETE FROM CTHD WHERE ID_HoaDon = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_Bill);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Xóa chi tiết hóa đơn theo ID_HoaDon thất bại" + e.getMessage());
            return false;
        }
    }
}
