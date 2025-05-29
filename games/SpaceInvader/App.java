package games.SpaceInvader;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // ✅ Dynamically retrieve the username (e.g., from login system)
        String username = JOptionPane.showInputDialog("Enter your username:"); // ✅ Ask user for input

        if (username == null || username.trim().isEmpty()) { // ✅ Handle empty username
            username = "Guest";
        }

        // Window variables
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns;
        int boardHeight = tileSize * rows;

        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders(username); // ✅ Pass username dynamically
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
    }
}
