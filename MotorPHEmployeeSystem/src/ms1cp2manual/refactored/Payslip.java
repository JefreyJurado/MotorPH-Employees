package ms1cp2manual.refactored;

import java.time.LocalDate;

public class Payslip {
    private final String payslipId;
    private final String employeeNumber;
    private final String employeeName;
    private final LocalDate payPeriodStart;
    private final LocalDate payPeriodEnd;
    private final LocalDate generatedDate;
    private final String generatedBy;
    private final double grossPay;
    private final double netPay;
    private final double totalDeductions;
    private final String payslipContent; // Full text content
    
    public Payslip(String payslipId, String employeeNumber, String employeeName,
                   LocalDate payPeriodStart, LocalDate payPeriodEnd, LocalDate generatedDate,
                   String generatedBy, double grossPay, double netPay, double totalDeductions,
                   String payslipContent) {
        this.payslipId = payslipId;
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.generatedDate = generatedDate;
        this.generatedBy = generatedBy;
        this.grossPay = grossPay;
        this.netPay = netPay;
        this.totalDeductions = totalDeductions;
        this.payslipContent = payslipContent;
    }
    
    // Getters
    public String getPayslipId() { return payslipId; }
    public String getEmployeeNumber() { return employeeNumber; }
    public String getEmployeeName() { return employeeName; }
    public LocalDate getPayPeriodStart() { return payPeriodStart; }
    public LocalDate getPayPeriodEnd() { return payPeriodEnd; }
    public LocalDate getGeneratedDate() { return generatedDate; }
    public String getGeneratedBy() { return generatedBy; }
    public double getGrossPay() { return grossPay; }
    public double getNetPay() { return netPay; }
    public double getTotalDeductions() { return totalDeductions; }
    public String getPayslipContent() { return payslipContent; }
    
    public String getPeriodDisplay() {
        return payPeriodStart + " to " + payPeriodEnd;
    }
    
    // CSV format
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,\"%s\"",
            payslipId,
            employeeNumber,
            employeeName,
            payPeriodStart,
            payPeriodEnd,
            generatedDate,
            generatedBy,
            grossPay,
            netPay,
            totalDeductions,
            payslipContent.replace("\"", "\"\"")
        );
    }
    
    public static Payslip fromCSV(String csvLine) {
        // Simple CSV parser - handle quoted content
        String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        return new Payslip(
            parts[0], // payslipId
            parts[1], // employeeNumber
            parts[2], // employeeName
            LocalDate.parse(parts[3]), // payPeriodStart
            LocalDate.parse(parts[4]), // payPeriodEnd
            LocalDate.parse(parts[5]), // generatedDate
            parts[6], // generatedBy
            Double.parseDouble(parts[7]), // grossPay
            Double.parseDouble(parts[8]), // netPay
            Double.parseDouble(parts[9]), // totalDeductions
            parts[10].replace("\"\"", "\"").replaceAll("^\"|\"$", "") // payslipContent
        );
    }
}