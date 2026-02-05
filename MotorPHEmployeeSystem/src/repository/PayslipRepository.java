package repository;

import model.Payslip;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PayslipRepository {
    private static final String PAYSLIP_FILE = "payslips.csv";
    private final List<Payslip> payslips;
    
    public PayslipRepository() {
        this.payslips = new ArrayList<>();
        loadFromCSV();
    }
    
    private void loadFromCSV() {
        File file = new File(PAYSLIP_FILE);
        if (!file.exists()) {
            createCSVFile();
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { 
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        payslips.add(Payslip.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing payslip line: " + line);
                    }
                }
            }
        } catch (IOException e) {
        }
    }
    
    private void createCSVFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYSLIP_FILE))) {
            writer.println("PayslipID,EmployeeNumber,EmployeeName,PayPeriodStart,PayPeriodEnd,GeneratedDate,GeneratedBy,GrossPay,NetPay,TotalDeductions,PayslipContent");
        } catch (IOException e) {
        }
    }
    
    public void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PAYSLIP_FILE))) {
            writer.println("PayslipID,EmployeeNumber,EmployeeName,PayPeriodStart,PayPeriodEnd,GeneratedDate,GeneratedBy,GrossPay,NetPay,TotalDeductions,PayslipContent");
            
            for (Payslip payslip : payslips) {
                writer.println(payslip.toCSV());
            }
        } catch (IOException e) {
        }
    }
    
    public void addPayslip(Payslip payslip) {
        payslips.add(payslip);
        saveToCSV();
    }
    
    public List<Payslip> getAllPayslips() {
        return new ArrayList<>(payslips);
    }
    
    public List<Payslip> getPayslipsByEmployee(String employeeNumber) {
        return payslips.stream()
            .filter(p -> p.getEmployeeNumber().equals(employeeNumber))
            .sorted((p1, p2) -> p2.getGeneratedDate().compareTo(p1.getGeneratedDate()))
            .collect(Collectors.toList());
    }
    
    public Payslip getPayslipById(String payslipId) {
        return payslips.stream()
            .filter(p -> p.getPayslipId().equals(payslipId))
            .findFirst()
            .orElse(null);
    }
    
    public void deletePayslip(String payslipId) {
        payslips.removeIf(p -> p.getPayslipId().equals(payslipId));
        saveToCSV();
    }
}