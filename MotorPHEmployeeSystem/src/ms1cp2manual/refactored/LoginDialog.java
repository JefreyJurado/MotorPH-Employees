package ms1cp2manual.refactored;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthenticationService authService;
    private boolean authenticated;

    public LoginDialog(JFrame parent, AuthenticationService authService) {
        super(parent, "Provide Username and Password", true);
        this.authService = authService;
        this.authenticated = false;
        initializeUI();
    }

    private void initializeUI() {
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 165, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 60, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 165, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 100, 80, 25);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (authService.authenticate(username, password)) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Incorrect login credentials", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}