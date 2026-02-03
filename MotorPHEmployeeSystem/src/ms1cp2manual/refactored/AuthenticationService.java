package ms1cp2manual.refactored;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, User> users;
    
    public AuthenticationService() {
        users = new HashMap<>();
        initializeUsers();
    }
    
    private void initializeUsers() {
        // Admin user - full access (no specific employee number needed)
        users.put("admin", new User("admin", "admin123", "Admin", null));
        
        // HR users - can view all employees (example: link to HR employee numbers)
        users.put("aljohn", new User("aljohn", "aljohn123", "HR", "10001")); // HR Manager
        users.put("michael", new User("michael", "michael123", "HR", "10002")); // HR Staff
        
        // Regular employees - can only view their own data
        // Link to actual employee numbers in your CSV
        users.put("employee1", new User("employee1", "emp123", "Employee", "1")); // Links to Emp #1
        users.put("employee2", new User("employee2", "emp123", "Employee", "2")); // Links to Emp #2
    }
    
    // Returns User object if authenticated, null otherwise
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public void addUser(String username, String password, String role, String employeeNumber) {
        users.put(username, new User(username, password, role, employeeNumber));
    }
}