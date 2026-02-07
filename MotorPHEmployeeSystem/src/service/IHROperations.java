package service;

import model.Employee;
import model.LeaveApplication;

// Access restricted to HR personnel.
public interface IHROperations {  
    void addEmployee(Employee employee);  
    void updateEmployeeInfo(Employee employee);    
    void deleteEmployee(String employeeIdNumber);   
    void approveLeaveRequest(LeaveApplication leave);   
    void rejectLeaveRequest(LeaveApplication leave);   
    void generateEmployeeReport();
}
