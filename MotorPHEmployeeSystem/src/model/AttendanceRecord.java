package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AttendanceRecord {
    private final String employeeNumber;
    private final LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private double totalHours;
    private double overtimeHours;
    private String status; // Present, Absent, Leave, Holiday
    private String remarks;
    
    // Standard work hours per day
    private static final double STANDARD_HOURS = 8.0;
    
    // Constructor
    public AttendanceRecord(String employeeNumber, LocalDate date, LocalTime timeIn, 
                           LocalTime timeOut, String status, String remarks) {
        this.employeeNumber = employeeNumber;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
        this.remarks = remarks;
        calculateHours();
    }
    
    // Calculate hours worked and overtime
    private void calculateHours() {
        if (timeIn != null && timeOut != null && "Present".equals(status)) {
            // Calculate total hours between time in and time out
            long minutes = ChronoUnit.MINUTES.between(timeIn, timeOut);
            this.totalHours = minutes / 60.0;
            
            // Calculate overtime (hours beyond standard 8 hours)
            if (totalHours > STANDARD_HOURS) {
                this.overtimeHours = totalHours - STANDARD_HOURS;
            } else {
                this.overtimeHours = 0.0;
            }
        } else if ("Absent".equals(status)) {
            this.totalHours = 0.0;
            this.overtimeHours = 0.0;
        } else if ("Leave".equals(status) || "Holiday".equals(status)) {
            this.totalHours = STANDARD_HOURS; // Paid leave/holiday
            this.overtimeHours = 0.0;
        } else {
            this.totalHours = 0.0;
            this.overtimeHours = 0.0;
        }
    }
    
    // Getters
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public LocalTime getTimeIn() {
        return timeIn;
    }
    
    public LocalTime getTimeOut() {
        return timeOut;
    }
    
    public double getTotalHours() {
        return totalHours;
    }
    
    public double getOvertimeHours() {
        return overtimeHours;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public double getRegularHours() {
        if (totalHours <= STANDARD_HOURS) {
            return totalHours;
        }
        return STANDARD_HOURS;
    }
    
    // Setters
    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
        calculateHours();
    }
    
    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
        calculateHours();
    }
    
    public void setStatus(String status) {
        this.status = status;
        calculateHours();
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    // Convert to CSV format
    public String toCSV() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        String timeInStr = (timeIn != null) ? timeIn.format(timeFormatter) : "";
        String timeOutStr = (timeOut != null) ? timeOut.format(timeFormatter) : "";
        
        return String.format("%s,%s,%s,%s,%.2f,%.2f,%s,%s",
            employeeNumber,
            date.format(dateFormatter),
            timeInStr,
            timeOutStr,
            totalHours,
            overtimeHours,
            status,
            remarks != null ? remarks : ""
        );
    }
    
    // Convert to table row for JTable
    public Object[] toTableRow() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        String timeInStr = (timeIn != null) ? timeIn.format(timeFormatter) : "-";
        String timeOutStr = (timeOut != null) ? timeOut.format(timeFormatter) : "-";
        
        return new Object[] {
            employeeNumber,
            date.format(dateFormatter),
            timeInStr,
            timeOutStr,
            String.format("%.2f", totalHours),
            String.format("%.2f", overtimeHours),
            status,
            remarks != null ? remarks : ""
        };
    }
    
    // Parse from CSV line
    public static AttendanceRecord fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", -1); // -1 to include empty trailing fields
        
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid CSV format for AttendanceRecord");
        }
        
        String employeeNumber = parts[0];
        LocalDate date = LocalDate.parse(parts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        LocalTime timeIn = null;
        LocalTime timeOut = null;
        
        if (!parts[2].trim().isEmpty()) {
            timeIn = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
        }
        
        if (!parts[3].trim().isEmpty()) {
            timeOut = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
        }
        
        String status = parts[6];
        String remarks = parts[7];
        
        return new AttendanceRecord(employeeNumber, date, timeIn, timeOut, status, remarks);
    }
    
    @Override
    public String toString() {
        return String.format("Attendance[Employee=%s, Date=%s, Hours=%.2f, OT=%.2f, Status=%s]",
            employeeNumber, date, totalHours, overtimeHours, status);
    }
}