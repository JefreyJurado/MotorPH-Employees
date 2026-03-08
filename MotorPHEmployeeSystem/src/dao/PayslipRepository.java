package dao;

import model.Payslip;
import java.io.*;
import java.time.LocalDate;
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
                    continue; 
                }
                
                // Append current line to record
                if (currentRecord.length() > 0) {
                    currentRecord.append("\n");
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
    
    // Save a payslip with duplicate checking
    // Removes any existing payslip for same employee and period before adding

    public void save(Payslip payslip) {
        // Remove any existing payslip for same employee and period
        payslips.removeIf(p -> 
            p.getEmployeeNumber().equals(payslip.getEmployeeNumber()) &&
            p.getPayPeriodStart().equals(payslip.getPayPeriodStart()) &&
            p.getPayPeriodEnd().equals(payslip.getPayPeriodEnd())
        );
        
        payslips.add(payslip);
        saveToCSV();
    }
    
    // Check if a payslip exists for a specific employee and period
    
    public boolean exists(String employeeNumber, LocalDate periodStart, LocalDate periodEnd) {
        return payslips.stream().anyMatch(p ->
            p.getEmployeeNumber().equals(employeeNumber) &&
            p.getPayPeriodStart().equals(periodStart) &&
            p.getPayPeriodEnd().equals(periodEnd)
        );
    }
    
    // Find all payslips for a specific pay period
    
    public List<Payslip> findByPeriod(LocalDate periodStart, LocalDate periodEnd) {
        return payslips.stream()
            .filter(p -> p.getPayPeriodStart().equals(periodStart) && 
                        p.getPayPeriodEnd().equals(periodEnd))
            .collect(Collectors.toList());
    }
      
    // Find all payslips for a specific employee (sorted by date, most recent first)
    
    public List<Payslip> findByEmployee(String employeeNumber) {
        return payslips.stream()
            .filter(p -> p.getEmployeeNumber().equals(employeeNumber))
            .sorted((p1, p2) -> p2.getPayPeriodStart().compareTo(p1.getPayPeriodStart()))
            .collect(Collectors.toList());
    }
    
    // Find a specific payslip by employee and period
   
    public Payslip findByEmployeeAndPeriod(String employeeNumber, LocalDate periodStart, LocalDate periodEnd) {
        return payslips.stream()
            .filter(p -> 
                p.getEmployeeNumber().equals(employeeNumber) &&
                p.getPayPeriodStart().equals(periodStart) &&
                p.getPayPeriodEnd().equals(periodEnd))
            .findFirst()
            .orElse(null);
    }
    
    // Delete all payslips for an employee
    
    public void deleteByEmployee(String employeeNumber) {
        payslips.removeIf(p -> p.getEmployeeNumber().equals(employeeNumber));
        saveToCSV();
    }
    
     // Delete a specific payslip
    
    public void delete(String employeeNumber, LocalDate periodStart, LocalDate periodEnd) {
        payslips.removeIf(p ->
            p.getEmployeeNumber().equals(employeeNumber) &&
            p.getPayPeriodStart().equals(periodStart) &&
            p.getPayPeriodEnd().equals(periodEnd)
        );
        saveToCSV();
    }
    
    // Get count of payslips
    
    public int getCount() {
        return payslips.size();
    }
    
    // Reload from file (useful after external changes)

    public void reload() {
        loadFromCSV();
    }
    
    // Remove duplicate payslips (same employee + same period)
    // Keeps the most recently generated one
    
    public int removeDuplicates() {
        int duplicatesRemoved = 0;
        
        // Group payslips by employee number and period
        java.util.Map<String, List<Payslip>> grouped = new java.util.HashMap<>();
        
        for (Payslip payslip : payslips) {
            String key = payslip.getEmployeeNumber() + "|" + 
                        payslip.getPayPeriodStart() + "|" + 
                        payslip.getPayPeriodEnd();
            
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(payslip);
        }
        
        // Find duplicates and keep only the most recent one
        payslips.clear();
        
        for (List<Payslip> group : grouped.values()) {
            if (group.size() > 1) {
                // Sort by generated date (most recent first)
                group.sort((p1, p2) -> p2.getGeneratedDate().compareTo(p1.getGeneratedDate()));
                
                // Keep the first one (most recent), discard the rest
                payslips.add(group.get(0));
                duplicatesRemoved += (group.size() - 1);
            } else {
                payslips.add(group.get(0));
            }
        }
        
        if (duplicatesRemoved > 0) {
            saveToCSV();
            System.out.println("✓ Removed " + duplicatesRemoved + " duplicate payslips");
        }
        
        return duplicatesRemoved;
    }
}