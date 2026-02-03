package ms1cp2manual.refactored;

public class User {
    private String username;
    private String password;
    private String role;
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }
    
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }
    
    public boolean isHR() {
        return "HR".equalsIgnoreCase(role);
    }
    
    public boolean isEmployee() {
        return "Employee".equalsIgnoreCase(role);
    }
    
    public boolean canModifyEmployees() {
        return isAdmin() || isHR();
    }
}