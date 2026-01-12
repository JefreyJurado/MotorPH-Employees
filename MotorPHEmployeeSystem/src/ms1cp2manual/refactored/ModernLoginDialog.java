package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModernLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthenticationService authService;
    private boolean authenticated;
    
    // Modern Colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color WHITE = Color.WHITE;

    public ModernLoginDialog(JFrame parent, AuthenticationService authService) {
        super(parent, "Login - MotorPH", true);
        this.authService = authService;
        this.authenticated = false;
        initializeModernUI();
    }

        private void initializeModernUI() {
        setSize(450, 600);  // INCREASED HEIGHT
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(WHITE);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setPreferredSize(new Dimension(450, 180));
        headerPanel.setBackground(PRIMARY_COLOR);
        
        JLabel iconLabel = new JLabel("🏢");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBounds(0, 30, 450, 70);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(iconLabel);
        
        JLabel titleLabel = new JLabel("MotorPH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(WHITE);
        titleLabel.setBounds(0, 100, 450, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Employee Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(0, 140, 450, 25);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Welcome Label - CENTERED
        JLabel welcomeLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(DARK_COLOR);
        welcomeLabel.setBounds(0, 20, 430, 30);
        formPanel.add(welcomeLabel);
        
        // Instruction Label - CENTERED
        JLabel instructionLabel = new JLabel("Please login to continue", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(127, 140, 141));
        instructionLabel.setBounds(0, 50, 430, 20);
        formPanel.add(instructionLabel);
        
        // Username Label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(DARK_COLOR);
        usernameLabel.setBounds(40, 90, 350, 20);
        formPanel.add(usernameLabel);
        
        // Username Field
        usernameField = new JTextField();
        usernameField.setBounds(40, 115, 350, 45);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        formPanel.add(usernameField);
        
        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(DARK_COLOR);
        passwordLabel.setBounds(40, 175, 350, 20);
        formPanel.add(passwordLabel);
        
        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(40, 200, 350, 45);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        formPanel.add(passwordField);
        
        // Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        // LOGIN BUTTON - USING PANEL (GUARANTEED TO WORK)
        JPanel loginButtonPanel = new JPanel();
        loginButtonPanel.setBounds(40, 265, 350, 45);
        loginButtonPanel.setBackground(SUCCESS_COLOR);
        loginButtonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR.darker(), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        loginButtonPanel.setLayout(new GridBagLayout());
        loginButtonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginLabel.setForeground(Color.WHITE);
        loginButtonPanel.add(loginLabel);

        // Make it clickable
        loginButtonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleLogin();
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButtonPanel.setBackground(new Color(57, 230, 126));
                loginButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(40, 180, 99), 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButtonPanel.setBackground(SUCCESS_COLOR);
                loginButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SUCCESS_COLOR.darker(), 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        formPanel.add(loginButtonPanel);
        
        return formPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setPreferredSize(new Dimension(450, 60));
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel footerLabel = new JLabel("© 2025 MotorPH. All rights reserved.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(127, 140, 141));
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Login Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (authService.authenticate(username, password)) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Incorrect username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}