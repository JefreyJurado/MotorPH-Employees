package model;

public class User {
    private final String username;
    private final String password;
    private final String role;
    private final String employeeNumber;
    
    public User(String username, String password, String role, String employeeNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.employeeNumber = employeeNumber;
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
    
    public String getEmployeeNumber() {
        return employeeNumber;
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
    
    public boolean canViewAllEmployees() {
        return isAdmin() || isHR();
    }
}