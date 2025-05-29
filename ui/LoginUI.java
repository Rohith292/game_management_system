package ui;

import database.UserDAO;
import javax.swing.*;
import java.awt.*;
import games.GameSelection.GameSelectionWindow;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("User Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 30, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 150, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 110, 100, 30);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Please enter both username and password!");
                return;
            }

            // Authenticate user
            if (UserDAO.validateLogin(username, password)) {
                JOptionPane.showMessageDialog(null, "✅ Login Successful!");
                dispose(); // Close Login UI
                new GameSelectionWindow(username).setVisible(true); // Redirect 🚀
            } else {
                JOptionPane.showMessageDialog(null, "❌ Invalid Credentials!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Proper UI thread handling
    }
}
