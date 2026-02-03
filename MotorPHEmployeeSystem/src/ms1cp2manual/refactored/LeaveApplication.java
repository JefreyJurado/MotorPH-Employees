package ms1cp2manual.refactored;

import java.time.LocalDate;

public class LeaveApplication {
    private String leaveId;
    private String employeeNumber;  // ADD THIS
    private String employeeName;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;
    private String submittedDate;
    private String approvedBy;
    
    // Constructor for NEW leave application
    public LeaveApplication(String employeeNumber, String employeeName, String leaveType, 
                          String startDate, String endDate, String reason) {
        this.leaveId = "LV" + System.currentTimeMillis();
        this.employeeNumber = employeeNumber;  // ADD THIS
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = "Pending";
        this.submittedDate = LocalDate.now().toString();
        this.approvedBy = "";
    }
    
    // Constructor for loading from CSV
    public LeaveApplication(String leaveId, String employeeNumber, String employeeName, 
                          String leaveType, String startDate, String endDate, 
                          String reason, String status, String submittedDate, String approvedBy) {
        this.leaveId = leaveId;
        this.employeeNumber = employeeNumber;  // ADD THIS
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.submittedDate = submittedDate;
        this.approvedBy = approvedBy;
    }
    
    // Getters
    public String getLeaveId() { return leaveId; }
    public String getEmployeeNumber() { return employeeNumber; }  // ADD THIS
    public String getEmployeeName() { return employeeName; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getSubmittedDate() { return submittedDate; }
    public String getApprovedBy() { return approvedBy; }
    
    // Status management
    public void approve(String approverName) {
        this.status = "Approved";
        this.approvedBy = approverName;
    }
    
    public void reject(String approverName) {
        this.status = "Rejected";
        this.approvedBy = approverName;
    }
    
    // CSV conversion
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,\"%s\",%s,%s,%s",
            leaveId,
            employeeNumber,  // ADD THIS
            employeeName,
            leaveType,
            startDate,
            endDate,
            reason.replace("\"", "\"\""),
            status,
            submittedDate,
            approvedBy.isEmpty() ? "N/A" : approvedBy
        );
    }
}