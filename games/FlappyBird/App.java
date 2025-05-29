package games.FlappyBird;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // ✅ Dynamically retrieve username from input
        String username = JOptionPane.showInputDialog("Enter your username:");

        if (username == null || username.trim().isEmpty()) {
            username = "Guest"; // ✅ Default to Guest if empty
        }

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird(username); // ✅ Pass username properly
        frame.add(flappyBird);
        frame.pack();
        frame.setVisible(true);
    }
}
