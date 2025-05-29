package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.cj.jdbc.Driver;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/company"; // MySQL URL
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "Rohith@123"; // Fixed the typo here

    public static Connection getConnection() {
    try {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("✅ Connected to MySQL!");
        return conn;
    }  catch (SQLException e) {
        System.out.println("❌ Database Connection Failed!");
        e.printStackTrace();
    }
    return null;
}

    public static void main(String[] args) {
        Connection conn = getConnection(); // Call connection method
        if (conn != null) {
            System.out.println("✅ Database connection is successful!");
        } else {
            System.out.println("❌ Unable to connect to MySQL.");
        }
    }
}
