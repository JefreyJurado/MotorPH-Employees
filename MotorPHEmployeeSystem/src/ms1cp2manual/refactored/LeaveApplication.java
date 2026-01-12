package ms1cp2manual.refactored;

public class LeaveApplication {
    private String employeeNumber;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String status;

    public LeaveApplication(String employeeNumber, String leaveType, 
                          String startDate, String endDate) {
        this.employeeNumber = employeeNumber;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending";
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Leave Application:\n" +
               "Employee Number: " + employeeNumber + "\n" +
               "Leave Type: " + leaveType + "\n" +
               "Start Date: " + startDate + "\n" +
               "End Date: " + endDate + "\n" +
               "Status: " + status;
    }
}