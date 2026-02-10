package dao;

import model.AttendanceRecord;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceRepository {
    private final String CSV_FILE = "attendance.csv";
    private final List<AttendanceRecord> attendanceRecords;
    
    public AttendanceRepository() {
        this.attendanceRecords = new ArrayList<>();
        loadFromCSV();
    }
    
    // Load attendance records from CSV
    public void loadFromCSV() {
        attendanceRecords.clear();
        File file = new File(CSV_FILE);
        
        if (!file.exists()) {
            System.out.println("Attendance CSV not found. Creating new file.");
            createDefaultCSV();
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    AttendanceRecord record = AttendanceRecord.fromCSV(line);
                    attendanceRecords.add(record);
                } catch (Exception e) {
                    System.err.println("Error parsing attendance record: " + line);
                    e.printStackTrace();
                }
            }
            
            System.out.println("Loaded " + attendanceRecords.size() + " attendance records");
            
        } catch (IOException e) {
            System.err.println("Error reading attendance CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Save attendance records to CSV
    public void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write header
            writer.println("EmployeeNumber,Date,TimeIn,TimeOut,TotalHours,OvertimeHours,Status,Remarks");
            
            // Write all records
            for (AttendanceRecord record : attendanceRecords) {
                writer.println(record.toCSV());
            }
            
            System.out.println("Saved " + attendanceRecords.size() + " attendance records");
            
        } catch (IOException e) {
            System.err.println("Error writing attendance CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Create default CSV file with header
    private void createDefaultCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("EmployeeNumber,Date,TimeIn,TimeOut,TotalHours,OvertimeHours,Status,Remarks");
            System.out.println("Created new attendance.csv file");
        } catch (IOException e) {
            System.err.println("Error creating attendance CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Add new attendance record
    public void addAttendance(AttendanceRecord record) {
        // Check if record already exists for this employee and date
        AttendanceRecord existing = findByEmployeeAndDate(record.getEmployeeNumber(), record.getDate());
        if (existing != null) {
            // Update existing record
            existing.setTimeIn(record.getTimeIn());
            existing.setTimeOut(record.getTimeOut());
            existing.setStatus(record.getStatus());
            existing.setRemarks(record.getRemarks());
        } else {
            // Add new record
            attendanceRecords.add(record);
        }
    }
    
    // Find attendance by employee and date
    public AttendanceRecord findByEmployeeAndDate(String employeeNumber, LocalDate date) {
        return attendanceRecords.stream()
            .filter(r -> r.getEmployeeNumber().equals(employeeNumber) && r.getDate().equals(date))
            .findFirst()
            .orElse(null);
    }
    
    // Get all attendance for a specific employee
    public List<AttendanceRecord> getAttendanceByEmployee(String employeeNumber) {
        return attendanceRecords.stream()
            .filter(r -> r.getEmployeeNumber().equals(employeeNumber))
            .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate())) // Newest first
            .collect(Collectors.toList());
    }
    
    // Get attendance for employee within date range
    public List<AttendanceRecord> getAttendanceByEmployeeAndDateRange(String employeeNumber, 
                                                                       LocalDate startDate, 
                                                                       LocalDate endDate) {
        return attendanceRecords.stream()
            .filter(r -> r.getEmployeeNumber().equals(employeeNumber) &&
                        !r.getDate().isBefore(startDate) &&
                        !r.getDate().isAfter(endDate))
            .sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate())) // Oldest first
            .collect(Collectors.toList());
    }
    
    // Get all attendance records
    public List<AttendanceRecord> getAllAttendance() {
        return new ArrayList<>(attendanceRecords);
    }
    
    // Get attendance for a specific date (all employees)
    public List<AttendanceRecord> getAttendanceByDate(LocalDate date) {
        return attendanceRecords.stream()
            .filter(r -> r.getDate().equals(date))
            .collect(Collectors.toList());
    }
    
    // Calculate total hours for employee in date range
    public double calculateTotalHours(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        return getAttendanceByEmployeeAndDateRange(employeeNumber, startDate, endDate).stream()
            .mapToDouble(AttendanceRecord::getTotalHours)
            .sum();
    }
    
    // Calculate total overtime for employee in date range
    public double calculateTotalOvertime(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        return getAttendanceByEmployeeAndDateRange(employeeNumber, startDate, endDate).stream()
            .mapToDouble(AttendanceRecord::getOvertimeHours)
            .sum();
    }
    
    // Delete attendance record
    public void deleteAttendance(String employeeNumber, LocalDate date) {
        attendanceRecords.removeIf(r -> r.getEmployeeNumber().equals(employeeNumber) && 
                                        r.getDate().equals(date));
    }
    
    // Delete all attendance for an employee
    public void deleteAllAttendanceForEmployee(String employeeNumber) {
        attendanceRecords.removeIf(r -> r.getEmployeeNumber().equals(employeeNumber));
    }
}