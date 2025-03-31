package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://Mr-Cuong\\SQLEXPRESS:1433;databaseName=DbRestaurant;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
//          Đăng ký driver (Không cần thiết từ JDBC 4.0 trở lên)
//          Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi: Không thể kết nối đến CSDL!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // Kiểm tra kết nối
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
