package model;

import service.IExecutiveOperations;

public class ExecutiveEmployee extends Employee implements IExecutiveOperations {
    
    public ExecutiveEmployee(String employeeNumber, String lastName, String firstName,
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
        return "Executive Management";
    }
    
    @Override
    public String getJobDescription() {
        return "Provides strategic leadership and oversees company operations";
    }
    
    @Override
    public void approveStrategicDecisions() {
        System.out.println("[Executive] " + getFullName() + " is approving strategic decisions...");
    }
    
    @Override
    public void viewAllDepartmentReports() {
        System.out.println("[Executive] " + getFullName() + " is viewing department reports...");
    }
    
    @Override
    public void accessFinancialData() {
        System.out.println("[Executive] " + getFullName() + " is accessing financial data...");
    }
    
    @Override
    public void authorizeHighValueTransactions(double amount) {
        System.out.println("[Executive] " + getFullName() + " authorized transaction of ₱" + amount);
    }
    
    @Override
    public void setCompanyPolicies() {
        System.out.println("[Executive] " + getFullName() + " is setting company policies...");
    }
    
    @Override
    public void reviewPerformanceMetrics() {
        System.out.println("[Executive] " + getFullName() + " is reviewing performance metrics...");
    }
}