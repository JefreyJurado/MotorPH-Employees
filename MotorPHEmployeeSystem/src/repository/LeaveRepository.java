package repository;

import model.LeaveApplication;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveRepository {
    private static final String LEAVE_FILE = "leaves.csv";
    private List<LeaveApplication> leaves;
    
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
            String header = br.readLine();
            String line;
            
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        leaves.add(parseCSVLine(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing leave line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createCSVFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEAVE_FILE))) {
            writer.println("LeaveID,EmployeeNumber,EmployeeName,LeaveType,StartDate,EndDate,Reason,Status,SubmittedDate,ApprovedBy");
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
            writer.println("LeaveID,EmployeeNumber,EmployeeName,LeaveType,StartDate,EndDate,Reason,Status,SubmittedDate,ApprovedBy");
            
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
    
    //Search by employee NUMBER instead of name
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