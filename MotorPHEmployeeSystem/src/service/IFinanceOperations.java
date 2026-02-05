package service;

import model.Employee;
import model.Payslip;

/**
 * Interface for Finance-specific operations
 * Only Finance employees can perform these operations
 */
public interface IFinanceOperations {
    
    /**
     * Processes payroll for the specified period
     * @param period The payroll period (e.g., "January 2025")
     */
    void processPayroll(String period);
    
    /**
     * Generates a payslip for an employee
     * @param employee The employee to generate payslip for
     * @param period The pay period
     */
    void generatePayslip(Employee employee, String period);
    
    /**
     * Calculates net pay after all deductions
     * @param employee The employee to calculate for
     * @return The net pay amount
     */
    double calculateNetPay(Employee employee);
    
    /**
     * Approves a generated payslip
     * @param payslip The payslip to approve
     */
    void approvePayslip(Payslip payslip);
    
    /**
     * Generates comprehensive financial reports
     */
    void generateFinancialReport();
    
    /**
     * Calculates total deductions for an employee
     * @param employee The employee to calculate deductions for
     * @return Total deduction amount
     */
    double calculateTotalDeductions(Employee employee);
}