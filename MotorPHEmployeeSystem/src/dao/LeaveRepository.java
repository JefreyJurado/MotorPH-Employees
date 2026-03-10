package dao;

import model.LeaveApplication;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveRepository {
    private static final String LEAVE_FILE = "leaves.csv";
    private final List<LeaveApplication> leaves;
    
    public LeaveRepository() {
        this.leaves = new ArrayList<>();
        loadFromCSV();
    }
    
    private void loadFromCSV() {
        File file = new File(LEAVE_FILE);
        if (!file.exists()) {
            createCSVFile();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine(); // Skip header
            System.out.println("Header skipped: " + header);

            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty line at " + lineNumber);
                    continue; // Skip empty lines
                }

                // Skip if line looks like a header (starts with "LeaveID")
                if (line.startsWith("LeaveID")) {
                    System.out.println("Skipping duplicate header at line " + lineNumber);
                    continue;
                }

                try {
                    LeaveApplication leave = parseCSVLine(line);
                    leaves.add(leave);
                    System.out.println("Loaded: " + leave.getLeaveId() + " - " + leave.getEmployeeName());
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    e.printStackTrace();
                }
            }

            System.out.println("Total leaves loaded: " + leaves.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createCSVFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_FILE))) {
            writer.println("LeaveID,EmployeeNumber,EmployeeName,LeaveType,StartDate,EndDate,Reason,Status,SubmittedDate,ProcessedBy");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private LeaveApplication parseCSVLine(String line) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        String leaveId = parts[0];
        String employeeNumber = parts[1];
        String employeeName = parts[2];
        String leaveType = parts[3];
        String startDate = parts[4];
        String endDate = parts[5];
        String reason = parts[6].replace("\"\"", "\"").replaceAll("^\"|\"$", "");
        String status = parts[7];
        String submittedDate = parts[8];
        String approvedBy = parts[9].equals("N/A") ? "" : parts[9];
        
        return new LeaveApplication(leaveId, employeeNumber, employeeName, leaveType, 
                                   startDate, endDate, reason, status, submittedDate, approvedBy);
    }
    
    public void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_FILE))) {
            // Write header ONCE
            writer.println("LeaveID,EmployeeNumber,EmployeeName,LeaveType,StartDate,EndDate,Reason,Status,SubmittedDate,ProcessedBy");

            // Write data
            for (LeaveApplication leave : leaves) {
                writer.println(leave.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addLeave(LeaveApplication leave) {
        leaves.add(leave);
        saveToCSV();
    }
    
    public void updateLeave(LeaveApplication updatedLeave) {
        for (int i = 0; i < leaves.size(); i++) {
            if (leaves.get(i).getLeaveId().equals(updatedLeave.getLeaveId())) {
                leaves.set(i, updatedLeave);
                saveToCSV();
                return;
            }
        }
    }
    
    public List<LeaveApplication> getAllLeaves() {
        return new ArrayList<>(leaves);
    }
    
    public List<LeaveApplication> getPendingLeaves() {
        return leaves.stream()
            .filter(leave -> "Pending".equals(leave.getStatus()))
            .collect(Collectors.toList());
    }
    
    //Get Approved Leaves for History
    public List<LeaveApplication> getApprovedLeaves() {
        return leaves.stream()
            .filter(leave -> "Approved".equals(leave.getStatus()))
            .collect(Collectors.toList());
    }
    
    // Get Rejected Leaves for History
    public List<LeaveApplication> getRejectedLeaves() {
        return leaves.stream()
            .filter(leave -> "Rejected".equals(leave.getStatus()))
            .collect(Collectors.toList());
    }
    
    // Search by employee NUMBER instead of name
    public List<LeaveApplication> getLeavesByEmployeeNumber(String employeeNumber) {
        return leaves.stream()
            .filter(leave -> leave.getEmployeeNumber().equals(employeeNumber))
            .collect(Collectors.toList());
    }
   
    public List<LeaveApplication> getLeavesByEmployee(String employeeName) {
        return leaves.stream()
            .filter(leave -> leave.getEmployeeName().equalsIgnoreCase(employeeName.trim()))
            .collect(Collectors.toList());
    }
}