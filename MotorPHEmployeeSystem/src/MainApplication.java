
import repository.EmployeeRepository;
import view.EmployeeDashboardFrame;
import view.ModernEmployeeManagementFrame;
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
                
                if (authenticatedUser.isEmployee()) {
                    // Show Employee Dashboard
                    EmployeeDashboardFrame empFrame = 
                        new EmployeeDashboardFrame(repository, authenticatedUser);
                    empFrame.setVisible(true);
                } else {
                    // Show Admin/HR Dashboard
                    ModernEmployeeManagementFrame frame = 
                        new ModernEmployeeManagementFrame(repository, authenticatedUser);
                    frame.setVisible(true);
                }
            } else {
                System.exit(0);
            }
        });
    }
}