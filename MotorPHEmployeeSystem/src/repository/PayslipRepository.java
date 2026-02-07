package repository;

import model.Payslip;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PayslipRepository {
    private final String CSV_FILE = "payslips.csv";
    private final List<Payslip> payslips;
    
    public PayslipRepository() {
        this.payslips = new ArrayList<>();
        loadFromCSV();
    }
    
    public void loadFromCSV() {
        payslips.clear();
        File file = new File(CSV_FILE);
        
        if (!file.exists()) {
            System.out.println("Payslips CSV not found. Creating new file.");
            createDefaultCSV();
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            StringBuilder currentRecord = new StringBuilder();
            int quoteCount = 0;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                // Append current line to record
                if (currentRecord.length() > 0) {
                    currentRecord.append("\n"); // Restore newline removed by readLine()
                }
                currentRecord.append(line);
                
                // Count quotes in current line
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        quoteCount++;
                    }
                }
                
                // If quote count is even, the record is complete
                // (all quoted fields are closed)
                if (quoteCount % 2 == 0) {
                    try {
                        Payslip payslip = Payslip.fromCSV(currentRecord.toString());
                        payslips.add(payslip);
                    } catch (Exception e) {
                        System.err.println("Error parsing payslip: " + e.getMessage());
                    }
                    
                    // Reset for next record
                    currentRecord = new StringBuilder();
                    quoteCount = 0;
                }
            }
            
            System.out.println("Loaded " + payslips.size() + " payslips");
            
        } catch (IOException e) {
            System.err.println("Error reading payslips CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write header
            writer.println("PayslipID,EmployeeNumber,EmployeeName,PayPeriodStart,PayPeriodEnd,GeneratedDate,GeneratedBy,GrossPay,NetPay,TotalDeductions,PayslipContent");
            
            // Write all payslips
            for (Payslip payslip : payslips) {
                writer.println(payslip.toCSV());
            }
            
            System.out.println("Saved " + payslips.size() + " payslips");
            
        } catch (IOException e) {
            System.err.println("Error writing payslips CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createDefaultCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("PayslipID,EmployeeNumber,EmployeeName,PayPeriodStart,PayPeriodEnd,GeneratedDate,GeneratedBy,GrossPay,NetPay,TotalDeductions,PayslipContent");
            System.out.println("Created new payslips.csv file");
        } catch (IOException e) {
            System.err.println("Error creating payslips CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addPayslip(Payslip payslip) {
        payslips.add(payslip);
        saveToCSV();
    }
    
    public List<Payslip> getAllPayslips() {
        return new ArrayList<>(payslips);
    }
    
    public List<Payslip> getPayslipsByEmployeeNumber(String employeeNumber) {
        return payslips.stream()
            .filter(p -> p.getEmployeeNumber().equals(employeeNumber))
            .collect(Collectors.toList());
    }
    
    public Payslip getPayslipById(String payslipId) {
        return payslips.stream()
            .filter(p -> p.getPayslipId().equals(payslipId))
            .findFirst()
            .orElse(null);
    }
}