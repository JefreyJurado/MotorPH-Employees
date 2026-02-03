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
        // Admin user - full access
        users.put("admin", new User("admin", "admin123", "Admin"));
        
        // HR users - can modify employees
        users.put("aljohn", new User("aljohn", "aljohn123", "HR"));
        users.put("michael", new User("michael", "michael123", "HR"));
        
        // Regular employees - read only
        users.put("employee1", new User("employee1", "emp123", "Employee"));
        users.put("employee2", new User("employee2", "emp123", "Employee"));
    }
    
    // Returns User object if authenticated, null otherwise
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public void addUser(String username, String password, String role) {
        users.put(username, new User(username, password, role));
    }
}