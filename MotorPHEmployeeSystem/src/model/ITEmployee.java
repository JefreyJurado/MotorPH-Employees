package model;

import service.IITOperations;

public class ITEmployee extends Employee implements IITOperations {
    
    public ITEmployee(String employeeNumber, String lastName, String firstName,
                     String birthday, String address, String phoneNumber,
                     String sssNumber, String philhealthNumber, String tin,
                     String pagibigNumber, String status, String position,
                     String immediateSupervisor, double basicSalary, double riceSubsidy,
                     double phoneAllowance, double clothingAllowance,
                     double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Information Technology";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages IT infrastructure, systems, and technical support";
    }
    
    @Override
    public void manageSystemSecurity() {
        System.out.println("[IT] " + getFullName() + " is managing system security...");
    }
    
    @Override
    public void performSystemMaintenance() {
        System.out.println("[IT] " + getFullName() + " is performing system maintenance...");
    }
    
    @Override
    public void backupDatabase() {
        System.out.println("[IT] " + getFullName() + " is backing up database...");
    }
    
    @Override
    public void resetUserPassword(User user) {
        System.out.println("[IT] " + getFullName() + " is resetting password for user: " + 
                         user.getUsername());
    }
    
    @Override
    public void configureSystemSettings() {
        System.out.println("[IT] " + getFullName() + " is configuring system settings...");
    }
    
    @Override
    public void monitorSystemHealth() {
        System.out.println("[IT] " + getFullName() + " is monitoring system health...");
    }
}