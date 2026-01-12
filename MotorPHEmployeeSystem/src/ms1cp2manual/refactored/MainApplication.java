package ms1cp2manual.refactored;

import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthenticationService authService = new AuthenticationService();
            
            ModernLoginDialog loginDialog = new ModernLoginDialog(null, authService);
            loginDialog.setVisible(true);

            if (loginDialog.isAuthenticated()) {
                EmployeeRepository repository = new EmployeeRepository();
                ModernEmployeeManagementFrame mainFrame = new ModernEmployeeManagementFrame(repository);
                mainFrame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}