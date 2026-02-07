package model;

import service.IHROperations;

public class HREmployee extends Employee implements IHROperations {
    
    public HREmployee(String employeeNumber, String lastName, String firstName,
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
        return "Human Resources";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages employee relations, recruitment, and HR policies";
    }
    
    @Override
    public void addEmployee(Employee employee) {
        System.out.println("[HR] " + getFullName() + " is adding new employee: " + employee.getFullName());
    }
    
    @Override
    public void updateEmployeeInfo(Employee employee) {
        System.out.println("[HR] " + getFullName() + " is updating employee info for: " + employee.getFullName());
    }
    
    @Override
    public void deleteEmployee(String employeeNumber) {
        System.out.println("[HR] " + getFullName() + " is deleting employee #" + employeeNumber);
    }
    
    @Override
    public void approveLeaveRequest(LeaveApplication leave) {
        System.out.println("[HR] " + getFullName() + " approved leave request: " + leave.getLeaveId());
    }
    
    @Override
    public void rejectLeaveRequest(LeaveApplication leave) {
        System.out.println("[HR] " + getFullName() + " rejected leave request: " + leave.getLeaveId());
    }
    
    @Override
    public void generateEmployeeReport() {
        System.out.println("[HR] " + getFullName() + " is generating employee report...");
    }
}