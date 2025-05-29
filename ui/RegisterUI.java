package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import games.GameSelection.GameSelectionWindow;
import database.UserDAO;

public class RegisterUI extends JFrame {
    public RegisterUI() {
        setTitle("User Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login"); // New Login Button 🚀

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String email = emailField.getText();

            boolean success = UserDAO.registerUser(username, password, email);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
                dispose(); // Close window after registration
                new GameSelectionWindow(username).setVisible(true); // Redirect to GameSelectionWindow 🚀
            } else {
                JOptionPane.showMessageDialog(this, "❌ Registration Failed!");
            }
        });

        loginButton.addActionListener(e -> {
            dispose(); // Close this window
            new LoginUI().setVisible(true); // Open Login UI 🚀
        });

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(emailLabel); add(emailField);
        add(registerButton); add(loginButton); // Added Login button ✅
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterUI().setVisible(true));
    }
}
