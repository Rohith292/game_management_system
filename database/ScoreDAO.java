package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreDAO {
    public static String getScores(String username) {
        StringBuilder scores = new StringBuilder();
        String query = "SELECT u.username, COALESCE(g.game_name, 'Unknown Game') AS game_name, " +
               "s.score, s.timestamp FROM Scores s " +
               "LEFT JOIN Games g ON s.game_id = g.id " +
               "JOIN Users u ON s.user_id = u.id " +
               "WHERE u.username = ? ORDER BY s.timestamp DESC;";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { // ✅ Handle empty results properly
                return "No scores found for this user.";
            }

            while (rs.next()) {
                scores.append(rs.getString("game_name")).append(": ")
                      .append(rs.getInt("score")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error retrieving scores. Please try again!";
        }

        return scores.toString();
    }

    public static void saveScore(String username, String gameName, int score) {
        String userQuery = "SELECT id FROM Users WHERE username = ?";
        String gameQuery = "SELECT id FROM Games WHERE game_name = ?";
        int userId = -1;
        int gameId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(userQuery);
             PreparedStatement stmtGame = conn.prepareStatement(gameQuery)) {
            
            // Fetch user_id
            stmtUser.setString(1, username);
            ResultSet rsUser = stmtUser.executeQuery();
            if (rsUser.next()) {
                userId = rsUser.getInt("id");
            }

            // Fetch game_id
            stmtGame.setString(1, gameName);
            ResultSet rsGame = stmtGame.executeQuery();
            if (rsGame.next()) {
                gameId = rsGame.getInt("id");
            }

            // Validate that both user_id and game_id exist
            if (userId == -1) {
                System.out.println("❌ ERROR: User '" + username + "' not found, skipping score save!");
                return;
            }
            if (gameId == -1) {
                System.out.println("❌ ERROR: Game '" + gameName + "' not found, skipping score save!");
                return;
            }

            System.out.println("✅ Saving Score for User ID: " + userId + ", Game ID: " + gameId + ", Score: " + score);

            String insertQuery = "INSERT INTO Scores (user_id, game_id, score) VALUES (?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                stmtInsert.setInt(1, userId);
                stmtInsert.setInt(2, gameId);
                stmtInsert.setInt(3, score);
                stmtInsert.executeUpdate();
                System.out.println("✅ Score successfully inserted into MySQL!");
            } catch (SQLException e) {
                System.out.println("❌ SQL Error while inserting score!");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
