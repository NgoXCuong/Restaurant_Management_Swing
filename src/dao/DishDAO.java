package dao;

import model.DishModel;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO {
    private static final Connection con = DatabaseConnection.getConnection();

    public List<DishModel>  getAllDishes(){
        List<DishModel> dishes = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";

        try (PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DishModel dish = new DishModel();
                dish.setId_Dish(rs.getInt("ID_MonAn"));
                dish.setName_Dish(rs.getString("TenMon"));
                dish.setPrice(rs.getInt("DonGia"));
                dish.setCategory(rs.getString("Loai"));
                dish.setStatus(rs.getString("TrangThai"));

                dishes.add(dish);
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach mon an:" + e.getMessage());
        }
        return dishes;
    }

    public List<DishModel> getDishById(int id){
        List<DishModel> dishes = new ArrayList<>();
        String sql = "SELECT * FROM MonAn WHERE ID_MonAn = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DishModel dish = new DishModel();
                    dish.setId_Dish(rs.getInt("ID_MonAn"));
                    dish.setName_Dish(rs.getString("TenMon"));
                    dish.setPrice(rs.getInt("DonGia"));
                    dish.setCategory(rs.getString("Loai"));
                    dish.setStatus(rs.getString("TrangThai"));
                    dishes.add(dish);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach mon an theo id:" + e.getMessage());
        }
        return dishes;
    }

    public List<DishModel> getDishesByCategory(String category){
        List<DishModel> dishes = new ArrayList<>();
        String sql = "SELECT * FROM MonAn WHERE Loai = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DishModel dish = new DishModel();
                    dish.setId_Dish(rs.getInt("ID_MonAn"));
                    dish.setName_Dish(rs.getString("TenMon"));
                    dish.setPrice(rs.getInt("DonGia"));
                    dish.setCategory(rs.getString("Loai"));
                    dish.setStatus(rs.getString("TrangThai"));
                    dishes.add(dish);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach mon an theo loai:" + e.getMessage());
        }
        return dishes;
    }

    public List<DishModel> getActiveDishes(String category ){
        List<DishModel> dishes = new ArrayList<>();
        String sql = "SELECT * MonAn Where Loai = ? AND TrangThai = 'Dang kinh doanh'";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DishModel dish = new DishModel();
                    dish.setId_Dish(rs.getInt("ID_MonAn"));
                    dish.setName_Dish(rs.getString("TenMon"));
                    dish.setPrice(rs.getInt("DonGia"));
                    dish.setCategory(rs.getString("Loai"));
                    dish.setStatus(rs.getString("TrangThai"));
                    dishes.add(dish);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi lay danh sach mon an theo loai voi trang thai dang hoat dong:" + e.getMessage());
        }
        return dishes;
    }

    public boolean addDish(DishModel dish){
        String sql = "INSERT INTO MonAn(ID_MonAn, TenMon, DonGia, Loai, TrangThai) VALUES(?,?,?,?,?)";

        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dish.getId_Dish());
            ps.setString(2, dish.getName_Dish());
            ps.setInt(3, dish.getPrice());
            ps.setString(4, dish.getCategory());
            ps.setString(5, dish.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi them mon an:" + e.getMessage());
            return false;
        }
    }

    public boolean updateDish(DishModel dish){
        String sql = "UPDATE MonAn SET TenMon = ?, DonGia = ?, Loai = ?, TrangThai = ? WHERE ID_MonAn = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, dish.getId_Dish());
            ps.setString(2, dish.getName_Dish());
            ps.setInt(3, dish.getPrice());
            ps.setString(4, dish.getCategory());
            ps.setString(5, dish.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi sua mon an:" + e.getMessage());
            return false;
        }
    }

    public boolean deleteDish(int id){
        String sql = "DELETE FROM MonAn WHERE ID_MonAn = ?";

        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Loi xoa mon an:" + e.getMessage());
            return false;
        }
    }

    public int getNextId() {
        String sql = "SELECT MAX(ID_MonAn) AS MaxId FROM MonAn";

        try(PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("MaxId") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Loi lay id mon an tiep:" + e.getMessage());
        }
        return 0;
    }
}
