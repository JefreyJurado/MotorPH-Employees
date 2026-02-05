package service;

import model.Employee;

/**
 * Interface for Accounting-specific operations
 * Only Accounting employees can perform these operations
 */
public interface IAccountingOperations {
    
    /**
     * Records a financial transaction
     * @param transactionType Type of transaction (e.g., "EXPENSE", "INCOME")
     * @param amount Transaction amount
     */
    void recordTransaction(String transactionType, double amount);
    
    /**
     * Reconciles financial accounts
     */
    void reconcileAccounts();
    
    /**
     * Generates tax reports for compliance
     */
    void generateTaxReport();
    
    /**
     * Audits payroll calculations for accuracy
     * @param employee The employee whose payroll to audit
     */
    void auditPayroll(Employee employee);
    
    /**
     * Manages accounts payable
     */
    void manageAccountsPayable();
    
    /**
     * Manages accounts receivable
     */
    void manageAccountsReceivable();
}