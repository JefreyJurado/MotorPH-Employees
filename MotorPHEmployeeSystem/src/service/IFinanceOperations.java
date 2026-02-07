package service;

import model.Employee;
import model.Payslip;


// Restricted to Finance department employees.
public interface IFinanceOperations {
    void processPayroll(String period);
    void generatePayslip(Employee employee, String period);
    double calculateNetPay(Employee employee);
    void approvePayslip(Payslip payslip);
    void generateFinancialReport();
    double calculateTotalDeductions(Employee employee);
}
