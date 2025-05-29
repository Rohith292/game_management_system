package games.GameSelection;

import database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GameSelectionWindow extends JFrame {
    private String username;

    public GameSelectionWindow(String username) {
        this.username = username;
        setTitle("Game Selection - Welcome " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);

        JButton spaceInvaderButton = new JButton("Launch Space Invader");
        JButton pacmanButton = new JButton("Launch Pacman");
        JButton flappyBirdButton = new JButton("Launch Flappy Bird");
        JButton minesweeperButton = new JButton("Launch Minesweeper");
        JButton viewScoresButton = new JButton("View Scores");

        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        spaceInvaderButton.setFont(buttonFont);
        pacmanButton.setFont(buttonFont);
        flappyBirdButton.setFont(buttonFont);
        minesweeperButton.setFont(buttonFont);
        viewScoresButton.setFont(buttonFont);

        spaceInvaderButton.addActionListener(e -> launchGame("games.SpaceInvader.App"));
        pacmanButton.addActionListener(e -> launchGame("games.PacMan.App"));
        flappyBirdButton.addActionListener(e -> launchGame("games.FlappyBird.App"));
        minesweeperButton.addActionListener(e -> launchGame("games.Minesweeper.App"));
        viewScoresButton.addActionListener(e -> showScores());

        setLayout(new GridLayout(5, 1, 10, 10));
        add(spaceInvaderButton);
        add(pacmanButton);
        add(flappyBirdButton);
        add(minesweeperButton);
        add(viewScoresButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void launchGame(String gameClass) {
        dispose();
        try {
            Process process = new ProcessBuilder("java", "-cp",
                "D:\\java_game\\java_game\\lib\\mysql-connector-j-9.3.0.jar;D:\\java_game\\java_game\\src",
                gameClass).start();
            process.waitFor();
            new GameSelectionWindow(username).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error launching " + gameClass);
        }
    }

    private void showScores() {
        String query = "SELECT u.username, g.game_name, s.score, s.timestamp " +
                       "FROM Scores s " +
                       "JOIN Users u ON s.user_id = u.id " +
                       "JOIN Games g ON s.game_id = g.id " +
                       "WHERE u.username = ? ORDER BY s.timestamp DESC;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(this, "❌ No scores found for " + username);
                return;
            }

            String[] columnNames = {"Game Name", "Score", "Time Played"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            JTable scoreTable = new JTable(model);

            while (rs.next()) {
                String gameName = rs.getString("game_name");
                int score = rs.getInt("score");
                String timePlayed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                   .format(rs.getTimestamp("timestamp"));

                model.addRow(new Object[]{gameName, score, timePlayed});
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(scoreTable), "Your Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error retrieving scores.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameSelectionWindow("testuser").setVisible(true));
    }
}
