package ms1cp2manual.refactored;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaveApplication {
    private String leaveId;
    private String employeeName;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
    private String status; // "Pending", "Approved", "Rejected"
    private String submittedDate;
    private String approvedBy;
    
    // Constructor for new leave application
    public LeaveApplication(String employeeName, String leaveType, 
                          String startDate, String endDate, String reason) {
        this.leaveId = generateLeaveId();
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = "Pending";
        this.submittedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.approvedBy = "";
    }
    
    // Constructor for loading from CSV
    public LeaveApplication(String leaveId, String employeeName, String leaveType, 
                          String startDate, String endDate, String reason, 
                          String status, String submittedDate, String approvedBy) {
        this.leaveId = leaveId;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.submittedDate = submittedDate;
        this.approvedBy = approvedBy;
    }
    
    private String generateLeaveId() {
        return "LV" + System.currentTimeMillis();
    }
    
    public void approve(String approverName) {
        this.status = "Approved";
        this.approvedBy = approverName;
    }
    
    public void reject(String approverName) {
        this.status = "Rejected";
        this.approvedBy = approverName;
    }
    
    // Convert to CSV format
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,\"%s\",%s,%s,%s",
            leaveId, employeeName, leaveType, startDate, endDate, 
            reason, status, submittedDate, approvedBy);
    }
    
    // Getters
    public String getLeaveId() { return leaveId; }
    public String getEmployeeName() { return employeeName; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getSubmittedDate() { return submittedDate; }
    public String getApprovedBy() { return approvedBy; }
    
    public boolean isPending() { return "Pending".equals(status); }
    public boolean isApproved() { return "Approved".equals(status); }
    public boolean isRejected() { return "Rejected".equals(status); }
}