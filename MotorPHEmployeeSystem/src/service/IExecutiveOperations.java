package service;

// Only Executive employees can perform these operations
public interface IExecutiveOperations {
    
    void approveStrategicDecisions();
    void viewAllDepartmentReports();
    void accessFinancialData();
    void authorizeHighValueTransactions(double amount);
    void setCompanyPolicies();
    void reviewPerformanceMetrics();
}
