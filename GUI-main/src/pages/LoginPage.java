package pages;

import dal.admins.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private final AdminDAO adminDao = new AdminDAO();
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton signInButton;

    public LoginPage() {
        setTitle("Admin Login");
        setSize(420, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 112)); // Dark blue text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(usernameLabel, gbc);

        // Username input
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameField, gbc);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        // Password input
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // Sign In button
        signInButton = new JButton("Sign In");
        signInButton.setBackground(new Color(65, 105, 225)); // Royal blue
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);
        signInButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signInButton, gbc);

        // Action listener
        signInButton.addActionListener(e -> handleLogin());

        setLocationRelativeTo(null); // Center screen
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean valid = adminDao.checkIfAdminExists(username, password);
        if (valid) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new StudentPage(); // Make sure this class exists
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
