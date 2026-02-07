package service;

import model.Employee;

// Only Accounting employees can perform these operations
public interface IAccountingOperations {
    void recordTransaction(String transactionType, double amount);
    void reconcileAccounts();
    void generateTaxReport();
    void auditPayroll(Employee employee);
    void manageAccountsPayable();
    void manageAccountsReceivable();
}
