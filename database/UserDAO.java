package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    // Register a new user with plain-text password storage
    public static boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)"; // No password hashing

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Stores password in plain text 🚨(NOT RECOMMENDED)
            stmt.setString(3, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Registration failed!");
            e.printStackTrace();
            return false;
        }
    }

    // Validate login with plain-text password comparison
    public static boolean validateLogin(String username, String password) {
        String query = "SELECT password FROM Users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password); // Direct comparison of plain-text passwords
            }
        } catch (SQLException e) {
            System.out.println("❌ Login validation failed!");
            e.printStackTrace();
        }
        return false;
    }
}
