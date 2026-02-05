package service;

/**
 * Interface for Executive-specific operations
 * Only Executive employees can perform these operations
 */
public interface IExecutiveOperations {
    
    /**
     * Approves strategic business decisions
     */
    void approveStrategicDecisions();
    
    /**
     * Views reports from all departments
     */
    void viewAllDepartmentReports();
    
    /**
     * Accesses sensitive financial data
     */
    void accessFinancialData();
    
    /**
     * Authorizes high-value transactions
     * @param amount The transaction amount
     */
    void authorizeHighValueTransactions(double amount);
    
    /**
     * Sets company-wide policies
     */
    void setCompanyPolicies();
    
    /**
     * Reviews executive-level performance metrics
     */
    void reviewPerformanceMetrics();
}