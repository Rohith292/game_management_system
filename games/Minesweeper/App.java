package games.Minesweeper;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // ✅ Dynamically retrieve username
        String username = JOptionPane.showInputDialog("Enter your username:");

        // ✅ If the user doesn't enter anything, default to "Guest"
        

        SwingUtilities.invokeLater(() -> new Minesweeper(username)); // ✅ Pass the username dynamically
    }
}
