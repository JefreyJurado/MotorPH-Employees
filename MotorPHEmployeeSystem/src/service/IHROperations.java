package service;

import model.Employee;
import model.LeaveApplication;

/**
 * Interface for HR-specific operations
 * Only HR employees can perform these operations
 */
public interface IHROperations {
    
    /**
     * Adds a new employee to the system
     * @param employee The employee to be added
     */
    void addEmployee(Employee employee);
    
    /**
     * Updates existing employee information
     * @param employee The employee with updated information
     */
    void updateEmployeeInfo(Employee employee);
    
    /**
     * Removes an employee from the system
     * @param employeeNumber The employee number to delete
     */
    void deleteEmployee(String employeeNumber);
    
    /**
     * Approves an employee's leave request
     * @param leave The leave application to approve
     */
    void approveLeaveRequest(LeaveApplication leave);
    
    /**
     * Rejects an employee's leave request
     * @param leave The leave application to reject
     */
    void rejectLeaveRequest(LeaveApplication leave);
    
    /**
     * Generates a comprehensive employee report
     */
    void generateEmployeeReport();
}