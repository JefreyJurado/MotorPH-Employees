package model;

import service.IFinanceOperations;

public class FinanceEmployee extends Employee implements IFinanceOperations {
    
    public FinanceEmployee(String employeeNumber, String lastName, String firstName,
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
        return "Finance";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages payroll, financial operations, and budget planning";
    }
    
    @Override
    public void processPayroll(String period) {
        System.out.println("[Finance] " + getFullName() + " is processing payroll for " + period);
    }
    
    @Override
    public void generatePayslip(Employee employee, String period) {
        System.out.println("[Finance] " + getFullName() + " is generating payslip for " + 
                         employee.getFullName() + " - " + period);
    }
    
    @Override
    public double calculateNetPay(Employee employee) {
        double grossPay = employee.getBasicSalary() + employee.getRiceSubsidy() +
                         employee.getPhoneAllowance() + employee.getClothingAllowance();
        double deductions = calculateTotalDeductions(employee);
        return grossPay - deductions;
    }
    
    @Override
    public void approvePayslip(Payslip payslip) {
        System.out.println("[Finance] " + getFullName() + " approved payslip for employee #" + 
                         payslip.getEmployeeNumber());
    }
    
    @Override
    public void generateFinancialReport() {
        System.out.println("[Finance] " + getFullName() + " is generating financial reports...");
    }
    
    @Override
    public double calculateTotalDeductions(Employee employee) {
        double sss = employee.getBasicSalary() * 0.045;
        double philhealth = employee.getBasicSalary() * 0.02;
        double pagibig = 200.0;
        double withholdingTax = employee.getBasicSalary() * 0.15;
        return sss + philhealth + pagibig + withholdingTax;
    }
}