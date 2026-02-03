package ms1cp2manual.refactored;

import javax.swing.*;
import java.awt.*;

public class ModernLoginDialog extends JDialog {
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final AuthenticationService authService;
    private User authenticatedUser; // Store authenticated user
    
    public ModernLoginDialog(Frame parent) {
        super(parent, "MotorPH Login", true);
        this.authService = new AuthenticationService();
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(450, 80));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        headerPanel.add(titleLabel, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(WHITE);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 40, 100, 25);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        contentPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(70, 70, 300, 35);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(usernameField);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 120, 100, 25);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        contentPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(70, 150, 300, 35);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(70, 210, 300, 40);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(SUCCESS_COLOR);
        loginButton.setForeground(WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        contentPanel.add(loginButton);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        authenticatedUser = authService.authenticate(username, password);
        
        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(this, 
                "Welcome, " + authenticatedUser.getUsername() + "!\nRole: " + authenticatedUser.getRole(),
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}