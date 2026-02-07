package model;

import service.IAccountingOperations;

public class AccountingEmployee extends Employee implements IAccountingOperations {
    
    public AccountingEmployee(String employeeNumber, String lastName, String firstName,
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
        return "Accounting";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages financial records, tax compliance, and accounting operations";
    }
    
    @Override
    public void recordTransaction(String transactionType, double amount) {
        System.out.println("[Accounting] " + getFullName() + " recorded " + transactionType + 
                         " of ₱" + amount);
    }
    
    @Override
    public void reconcileAccounts() {
        System.out.println("[Accounting] " + getFullName() + " is reconciling accounts...");
    }
    
    @Override
    public void generateTaxReport() {
        System.out.println("[Accounting] " + getFullName() + " is generating tax reports...");
    }
    
    @Override
    public void auditPayroll(Employee employee) {
        System.out.println("[Accounting] " + getFullName() + " is auditing payroll for " + 
                         employee.getFullName());
    }
    
    @Override
    public void manageAccountsPayable() {
        System.out.println("[Accounting] " + getFullName() + " is managing accounts payable...");
    }
    
    @Override
    public void manageAccountsReceivable() {
        System.out.println("[Accounting] " + getFullName() + " is managing accounts receivable...");
    }
}