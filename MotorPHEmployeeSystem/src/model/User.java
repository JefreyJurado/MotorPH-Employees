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
    
    // Getters
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
    
    public boolean isSystemAdmin() {
        return "SystemAdmin".equalsIgnoreCase(role);
    }
    
    public boolean isOwner() {
        return "Owner".equalsIgnoreCase(role);
    }
    
    public boolean isHR() {
        return "HR".equalsIgnoreCase(role);
    }
    
    public boolean isFinance() {
        return "Finance".equalsIgnoreCase(role);
    }
    
    public boolean isIT() {
        return "IT".equalsIgnoreCase(role);
    }
    
    public boolean isAccounting() {
        return "Accounting".equalsIgnoreCase(role);
    }
    
    public boolean isExecutive() {
        return "Executive".equalsIgnoreCase(role);
    }
    
    public boolean isEmployee() {
        return "Employee".equalsIgnoreCase(role);
    }
    
    public boolean isAdmin() {
        // Now both SystemAdmin and Owner are considered "admin" for legacy code
        return isSystemAdmin() || isOwner();
    }
    
    // PERMISSION HELPERS
    
    public boolean canModifyEmployees() {
        return isHR() || isOwner();
    }
    
    public boolean canViewAllEmployees() {
        return isSystemAdmin() || isOwner() || isHR() || isFinance() || 
               isIT() || isAccounting() || isExecutive();
    }
    
    public boolean canProcessPayroll() {
        return isFinance() || isOwner();
    }
    
    public boolean canManageSystem() {
        return isSystemAdmin() || isIT();
    }
    
    public boolean canViewFinancialData() {
        return isFinance() || isAccounting() || isOwner() || isExecutive();
    }
    
    public boolean hasSpecialAccess() {
        return !isEmployee();
    }
}