package ms1cp2manual.refactored;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveRepository {
    private static final String CSV_FILE = "leaves.csv";
    private static final String CSV_HEADER = "LeaveID,EmployeeName,LeaveType,StartDate,EndDate,Reason,Status,SubmittedDate,ApprovedBy";
    private final List<LeaveApplication> leaves;
    
    public LeaveRepository() {
        leaves = new ArrayList<>();
        loadFromCSV();
    }
    
    private void loadFromCSV() {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            createCSVFile();
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                LeaveApplication leave = parseLeaveFromCSV(line);
                if (leave != null) {
                    leaves.add(leave);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading leaves from CSV: " + e.getMessage());
        }
    }
    
    private LeaveApplication parseLeaveFromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            
            if (parts.length >= 9) {
                String leaveId = parts[0].trim();
                String employeeName = parts[1].trim();
                String leaveType = parts[2].trim();
                String startDate = parts[3].trim();
                String endDate = parts[4].trim();
                String reason = parts[5].trim().replace("\"", "");
                String status = parts[6].trim();
                String submittedDate = parts[7].trim();
                String approvedBy = parts[8].trim();
                
                return new LeaveApplication(leaveId, employeeName, leaveType, 
                    startDate, endDate, reason, status, submittedDate, approvedBy);
            }
        } catch (Exception e) {
            System.err.println("Error parsing leave: " + e.getMessage());
        }
        return null;
    }
    
    private void createCSVFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(CSV_HEADER + "\n");
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
        }
    }
    
    private void saveToCSV() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write(CSV_HEADER + "\n");
            for (LeaveApplication leave : leaves) {
                writer.write(leave.toCSV() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving leaves to CSV: " + e.getMessage());
        }
    }
    
    public void addLeave(LeaveApplication leave) {
        leaves.add(leave);
        saveToCSV();
    }
    
    public void updateLeave(LeaveApplication leave) {
        saveToCSV();
    }
    
    public List<LeaveApplication> getAllLeaves() {
        return new ArrayList<>(leaves);
    }
    
    public List<LeaveApplication> getPendingLeaves() {
        List<LeaveApplication> pending = new ArrayList<>();
        for (LeaveApplication leave : leaves) {
            if (leave.isPending()) {
                pending.add(leave);
            }
        }
        return pending;
    }
    
    public List<LeaveApplication> getLeavesByEmployee(String employeeName) {
        List<LeaveApplication> employeeLeaves = new ArrayList<>();
        for (LeaveApplication leave : leaves) {
            if (leave.getEmployeeName().equalsIgnoreCase(employeeName)) {
                employeeLeaves.add(leave);
            }
        }
        return employeeLeaves;
    }
}