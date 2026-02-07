import repository.EmployeeRepository;
import view.EmployeeDashboardFrame;
import view.ModernLoginDialog;
import model.User;
import javax.swing.SwingUtilities;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModernLoginDialog loginDialog = new ModernLoginDialog(null);
            loginDialog.setVisible(true);
            
            User authenticatedUser = loginDialog.getAuthenticatedUser();
            
            if (authenticatedUser != null) {
                EmployeeRepository repository = new EmployeeRepository();
                
                // OPTION B: EVERYONE GOES TO EMPLOYEE DASHBOARD
                // Role-based buttons will appear based on their role
                EmployeeDashboardFrame empFrame = 
                    new EmployeeDashboardFrame(repository, authenticatedUser);
                empFrame.setVisible(true);
                
            } else {
                System.exit(0);
            }
        });
    }
}