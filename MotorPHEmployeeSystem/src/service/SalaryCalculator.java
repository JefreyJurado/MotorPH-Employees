package service;

import model.Employee;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import repository.AttendanceRepository;

public class SalaryCalculator {
    
    private AttendanceRepository attendanceRepository;
    
    public SalaryCalculator() {
        this.attendanceRepository = new AttendanceRepository();
    }
    
    public double calculateSSSDeduction(double monthlySalary) {
        // 2024 SSS CONTRIBUTION TABLE
        // Source: SSS Circular 2024-001
        
        if (monthlySalary < 4250) return 180.00;
        else if (monthlySalary < 4750) return 202.50;
        else if (monthlySalary < 5250) return 225.00;
        else if (monthlySalary < 5750) return 247.50;
        else if (monthlySalary < 6250) return 270.00;
        else if (monthlySalary < 6750) return 292.50;
        else if (monthlySalary < 7250) return 315.00;
        else if (monthlySalary < 7750) return 337.50;
        else if (monthlySalary < 8250) return 360.00;
        else if (monthlySalary < 8750) return 382.50;
        else if (monthlySalary < 9250) return 405.00;
        else if (monthlySalary < 9750) return 427.50;
        else if (monthlySalary < 10250) return 450.00;
        else if (monthlySalary < 10750) return 472.50;
        else if (monthlySalary < 11250) return 495.00;
        else if (monthlySalary < 11750) return 517.50;
        else if (monthlySalary < 12250) return 540.00;
        else if (monthlySalary < 12750) return 562.50;
        else if (monthlySalary < 13250) return 585.00;
        else if (monthlySalary < 13750) return 607.50;
        else if (monthlySalary < 14250) return 630.00;
        else if (monthlySalary < 14750) return 652.50;
        else if (monthlySalary < 15250) return 675.00;
        else if (monthlySalary < 15750) return 697.50;
        else if (monthlySalary < 16250) return 720.00;
        else if (monthlySalary < 16750) return 742.50;
        else if (monthlySalary < 17250) return 765.00;
        else if (monthlySalary < 17750) return 787.50;
        else if (monthlySalary < 18250) return 810.00;
        else if (monthlySalary < 18750) return 832.50;
        else if (monthlySalary < 19250) return 855.00;
        else if (monthlySalary < 19750) return 877.50;
        else return 900.00; // Maximum SSS contribution for highest bracket
    }
    
    public double calculatePhilHealthDeduction(double monthlySalary) {
        // Calculate 4% total premium (2024 rate)
        double totalPremium = monthlySalary * 0.04;
        
        // Employee pays half (2%)
        double employeeShare = totalPremium / 2;
        
        // Apply minimum and maximum caps (2024 rates)
        // Minimum contribution: ₱450.00
        if (employeeShare < 450.00) return 450.00;
        
        // Maximum contribution: ₱4,050.00 (based on ₱100,000 salary ceiling)
        if (employeeShare > 4050.00) return 4050.00;
        
        return employeeShare;
    }
    
    public double calculatePagIBIGDeduction(double monthlySalary) {
        // 2024 Pag-IBIG Contribution Schedule
        
        if (monthlySalary <= 1500) {
            // Low-income bracket: 1% contribution
            return monthlySalary * 0.01;
        } else if (monthlySalary <= 5000) {
            // Middle-income bracket: 2% contribution
            return monthlySalary * 0.02;
        } else {
            // High-income bracket: Fixed ₱100 contribution
            return 100.00;
        }
    }
    
    public double calculateWithholdingTax(double monthlySalary) {
        // Convert monthly to annual for tax bracket computation
        double annualSalary = monthlySalary * 12;
        double annualTax = 0;
        
        // Apply TRAIN Law 2024 Tax Brackets
        if (annualSalary <= 250000) {
            // Tax Exempt Bracket
            annualTax = 0;
        } else if (annualSalary <= 400000) {
            // 15% bracket
            annualTax = (annualSalary - 250000) * 0.15;
        } else if (annualSalary <= 800000) {
            // 20% bracket
            annualTax = 22500 + (annualSalary - 400000) * 0.20;
        } else if (annualSalary <= 2000000) {
            // 25% bracket
            annualTax = 102500 + (annualSalary - 800000) * 0.25;
        } else if (annualSalary <= 8000000) {
            // 30% bracket
            annualTax = 402500 + (annualSalary - 2000000) * 0.30;
        } else {
            // 35% bracket (highest income)
            annualTax = 2202500 + (annualSalary - 8000000) * 0.35;
        }
        
        // Convert annual tax to monthly withholding
        return annualTax / 12;
    }
    
    public double calculateTotalDeductions(Employee employee) {
        double monthlySalary = calculateGrossMonthlySalary(employee);
        return calculateSSSDeduction(monthlySalary) +
               calculatePhilHealthDeduction(monthlySalary) +
               calculatePagIBIGDeduction(monthlySalary) +
               calculateWithholdingTax(monthlySalary);
    }
    
    public double calculateGrossMonthlySalary(Employee employee) {
        return employee.getBasicSalary() + employee.calculateTotalAllowances();
    }
    
    public double calculateNetMonthlySalary(Employee employee) {
        return calculateGrossMonthlySalary(employee) - calculateTotalDeductions(employee);
    }

    public double calculateWeeklySalary(Employee employee) {
        return calculateNetMonthlySalary(employee) / 4;
    }
    
    public double calculateMonthlySalary(Employee employee) {
        return calculateGrossMonthlySalary(employee);
    }
    
    public double calculateAnnualSalary(Employee employee) {
        return calculateMonthlySalary(employee) * 12;
    }
    
    // ========================================================================
    // NEW METHODS FOR ATTENDANCE-BASED SALARY CALCULATION
    // ========================================================================
    
    /**
     * Calculate gross pay based on actual attendance records
     * @param employee The employee
     * @param startDate Start of pay period
     * @param endDate End of pay period
     * @return Gross pay for the period
     */
    public double calculateGrossPayWithAttendance(Employee employee, LocalDate startDate, LocalDate endDate) {
        // Get attendance records for the period
        double totalHours = attendanceRepository.calculateTotalHours(
            employee.getEmployeeNumber(), startDate, endDate);
        double overtimeHours = attendanceRepository.calculateTotalOvertime(
            employee.getEmployeeNumber(), startDate, endDate);
        
        // Calculate regular pay
        double regularHours = totalHours - overtimeHours;
        double regularPay = regularHours * employee.getHourlyRate();
        
        // Calculate overtime pay (1.25x for regular OT)
        double overtimePay = overtimeHours * employee.getHourlyRate() * 1.25;
        
        // Add allowances (pro-rated based on days worked vs standard days)
        int workingDaysInPeriod = getWorkingDaysBetween(startDate, endDate);
        double attendanceDays = totalHours / 8.0; // Assuming 8 hours per day
        double attendanceRatio = Math.min(1.0, attendanceDays / workingDaysInPeriod);
        
        double proratedRice = employee.getRiceSubsidy() * attendanceRatio;
        double proratedPhone = employee.getPhoneAllowance() * attendanceRatio;
        double proratedClothing = employee.getClothingAllowance() * attendanceRatio;
        
        double grossPay = regularPay + overtimePay + proratedRice + proratedPhone + proratedClothing;
        
        return Math.round(grossPay * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    /**
     * Helper method to count working days between two dates (excludes weekends)
     * @param startDate Start date
     * @param endDate End date
     * @return Number of working days
     */
    public int getWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            // Exclude weekends (Saturday = 6, Sunday = 7)
            if (current.getDayOfWeek().getValue() < 6) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        
        return workingDays;
    }
    
    /**
     * Get attendance summary for a pay period (for payslip display)
     * @param employeeNumber Employee number
     * @param startDate Start of pay period
     * @param endDate End of pay period
     * @return Formatted attendance summary string
     */
    public String getAttendanceSummary(String employeeNumber, LocalDate startDate, LocalDate endDate) {
        double totalHours = attendanceRepository.calculateTotalHours(employeeNumber, startDate, endDate);
        double overtimeHours = attendanceRepository.calculateTotalOvertime(employeeNumber, startDate, endDate);
        double regularHours = totalHours - overtimeHours;
        
        return String.format("Regular Hours: %.2f | Overtime Hours: %.2f | Total: %.2f", 
            regularHours, overtimeHours, totalHours);
    }
    
    /**
     * Get the AttendanceRepository instance (for use in other classes)
     * @return AttendanceRepository instance
     */
    public AttendanceRepository getAttendanceRepository() {
        return attendanceRepository;
    }
    
    // ========================================================================
    // END OF NEW ATTENDANCE METHODS
    // ========================================================================
    
    public String generateSalaryReport(Employee employee, String month) {
        double monthlySalary = calculateMonthlySalary(employee);
        
        return "Employee Details\n" +
               "----------------\n" +
               "Name: " + employee.getFullName() + "\n" +
               "Position: " + employee.getPosition() + "\n" +
               "Basic Salary: ₱" + String.format("%,.2f", employee.getBasicSalary()) + "\n" +
               "Rice Subsidy: ₱" + String.format("%,.2f", employee.getRiceSubsidy()) + "\n" +
               "Clothing Allowance: ₱" + String.format("%,.2f", employee.getClothingAllowance()) + "\n" +
               "\nSelected Month: " + month + "\n" +
               "Total Monthly Salary: ₱" + String.format("%,.2f", monthlySalary);
    }
    
    public WeeklyPayslip generateWeeklyPayslip(Employee employee, LocalDate weekStartDate, LocalDate weekEndDate) {
        return new WeeklyPayslip(employee, weekStartDate, weekEndDate, this);
    }

    public DeductionDetails getDeductionBreakdown(Employee employee) {
        return new DeductionDetails(employee, this);
    }
    
    public static class WeeklyPayslip {
        // Private fields (Encapsulation)
        private final Employee employee;
        private final LocalDate weekStartDate;
        private final LocalDate weekEndDate;
        private final double grossWeekly;
        private final double sssWeekly;
        private final double philHealthWeekly;
        private final double pagIBIGWeekly;
        private final double taxWeekly;
        private final double totalDeductionsWeekly;
        private final double netWeekly;
        private final double actualAllowances;  // NEW FIELD
        private final double actualBasicPay;    // NEW FIELD
        
        public WeeklyPayslip(Employee employee, LocalDate weekStart, LocalDate weekEnd, SalaryCalculator calculator) {
            this.employee = employee;
            this.weekStartDate = weekStart;
            this.weekEndDate = weekEnd;

            // TRY to calculate based on actual attendance
            double totalHours = calculator.attendanceRepository.calculateTotalHours(
                employee.getEmployeeNumber(), weekStart, weekEnd);

            if (totalHours > 0) {
                // ===== ATTENDANCE-BASED CALCULATION =====
                // Use actual hours worked from attendance records
                double overtimeHours = calculator.attendanceRepository.calculateTotalOvertime(
                    employee.getEmployeeNumber(), weekStart, weekEnd);
                double regularHours = totalHours - overtimeHours;

                // Calculate regular pay
                double regularPay = regularHours * employee.getHourlyRate();

                // Calculate overtime pay (1.25x for regular OT)
                double overtimePay = overtimeHours * employee.getHourlyRate() * 1.25;

                // Calculate pro-rated allowances based on days worked
                int workingDaysInPeriod = calculator.getWorkingDaysBetween(weekStart, weekEnd);
                double attendanceDays = totalHours / 8.0; // Assuming 8 hours per day
                double attendanceRatio = Math.min(1.0, attendanceDays / workingDaysInPeriod);

                double proratedRice = employee.getRiceSubsidy() * attendanceRatio / 4; // Weekly portion
                double proratedPhone = employee.getPhoneAllowance() * attendanceRatio / 4;
                double proratedClothing = employee.getClothingAllowance() * attendanceRatio / 4;

                // Store actual values for display
                this.actualAllowances = proratedRice + proratedPhone + proratedClothing;
                this.actualBasicPay = regularPay + overtimePay;

                // Total gross for the week
                this.grossWeekly = regularPay + overtimePay + proratedRice + proratedPhone + proratedClothing;

                // Calculate deductions based on monthly salary (standard practice)
                double monthlyGross = calculator.calculateGrossMonthlySalary(employee);
                this.sssWeekly = calculator.calculateSSSDeduction(monthlyGross) / 4;
                this.philHealthWeekly = calculator.calculatePhilHealthDeduction(monthlyGross) / 4;
                this.pagIBIGWeekly = calculator.calculatePagIBIGDeduction(monthlyGross) / 4;
                this.taxWeekly = calculator.calculateWithholdingTax(monthlyGross) / 4;

            } else {
                // ===== FALLBACK: NO ATTENDANCE DATA =====
                // Use old method (divide monthly by 4)
                System.out.println("⚠️ No attendance records found for " + employee.getEmployeeNumber() + 
                    " from " + weekStart + " to " + weekEnd + ". Using default calculation.");

                double monthlyGross = calculator.calculateGrossMonthlySalary(employee);
                this.grossWeekly = monthlyGross / 4;
                
                // Store fallback values
                this.actualBasicPay = employee.getBasicSalary() / 4;
                this.actualAllowances = (employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance()) / 4;
                
                this.sssWeekly = calculator.calculateSSSDeduction(monthlyGross) / 4;
                this.philHealthWeekly = calculator.calculatePhilHealthDeduction(monthlyGross) / 4;
                this.pagIBIGWeekly = calculator.calculatePagIBIGDeduction(monthlyGross) / 4;
                this.taxWeekly = calculator.calculateWithholdingTax(monthlyGross) / 4;
            }

            this.totalDeductionsWeekly = sssWeekly + philHealthWeekly + pagIBIGWeekly + taxWeekly;
            this.netWeekly = grossWeekly - totalDeductionsWeekly;
        }
        
        public String generatePayslipText() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("                    MOTORPH PAYROLL SYSTEM                      \n");
            sb.append("                     WEEKLY PAYSLIP                             \n");
            sb.append("═══════════════════════════════════════════════════════════════\n\n");
            
            sb.append("EMPLOYEE INFORMATION\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("Employee Number : %s\n", employee.getEmployeeNumber()));
            sb.append(String.format("Name            : %s\n", employee.getFullName()));
            sb.append(String.format("Position        : %s\n", employee.getPosition()));
            sb.append(String.format("Status          : %s\n\n", employee.getStatus()));
            
            sb.append("PAY PERIOD\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("Week Start      : %s\n", weekStartDate.format(formatter)));
            sb.append(String.format("Week End        : %s\n\n", weekEndDate.format(formatter)));
            
            sb.append("EARNINGS\n");
            sb.append("─────────────────────────────────────────────────────────────\n");

            // Show full weekly amounts for reference
            double fullBasic = employee.getBasicSalary() / 4;
            double fullRice = employee.getRiceSubsidy() / 4;
            double fullPhone = employee.getPhoneAllowance() / 4;
            double fullClothing = employee.getClothingAllowance() / 4;
            double fullAllowances = fullRice + fullPhone + fullClothing;

            sb.append(String.format("Basic Salary (Full Week)   : ₱%,12.2f\n", fullBasic));
            sb.append(String.format("Rice Subsidy (Full Week)   : ₱%,12.2f\n", fullRice));
            sb.append(String.format("Phone Allowance (Full Week): ₱%,12.2f\n", fullPhone));
            sb.append(String.format("Clothing Allow. (Full Week): ₱%,12.2f\n", fullClothing));
            sb.append(String.format("                             ─────────────\n"));
            sb.append(String.format("FULL WEEK POTENTIAL        : ₱%,12.2f\n\n", fullBasic + fullAllowances));

            sb.append(String.format("ACTUAL EARNINGS (Based on Attendance):\n"));
            sb.append(String.format("Basic Pay (Actual)         : ₱%,12.2f\n", actualBasicPay));
            sb.append(String.format("Allowances (Pro-rated)     : ₱%,12.2f\n", actualAllowances));
            sb.append(String.format("                             ─────────────\n"));
            sb.append(String.format("GROSS WEEKLY PAY           : ₱%,12.2f\n\n", grossWeekly));
            
            sb.append("DEDUCTIONS\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("SSS Contribution           : ₱%,12.2f\n", sssWeekly));
            sb.append(String.format("PhilHealth Contribution    : ₱%,12.2f\n", philHealthWeekly));
            sb.append(String.format("Pag-IBIG Contribution      : ₱%,12.2f\n", pagIBIGWeekly));
            sb.append(String.format("Withholding Tax            : ₱%,12.2f\n", taxWeekly));
            sb.append(String.format("                             ─────────────\n"));
            sb.append(String.format("TOTAL DEDUCTIONS           : ₱%,12.2f\n\n", totalDeductionsWeekly));
            
            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append(String.format("NET WEEKLY PAY             : ₱%,12.2f\n", netWeekly));
            sb.append("═══════════════════════════════════════════════════════════════\n\n");
            
            sb.append("This is a computer-generated payslip. No signature required.\n");
            sb.append("Generated on: " + LocalDate.now().format(formatter) + "\n");
            
            return sb.toString();
        }
        
        // Getters (Controlled access to private fields - Encapsulation)
        public Employee getEmployee() { return employee; }
        public LocalDate getWeekStartDate() { return weekStartDate; }
        public LocalDate getWeekEndDate() { return weekEndDate; }
        public double getGrossWeekly() { return grossWeekly; }
        public double getSssWeekly() { return sssWeekly; }
        public double getPhilHealthWeekly() { return philHealthWeekly; }
        public double getPagIBIGWeekly() { return pagIBIGWeekly; }
        public double getTaxWeekly() { return taxWeekly; }
        public double getTotalDeductionsWeekly() { return totalDeductionsWeekly; }
        public double getNetWeekly() { return netWeekly; }
    }
    
    public static class DeductionDetails {
        // Private fields (Encapsulation)
        private final Employee employee;
        private final double monthlySalary;
        private final double sssDeduction;
        private final double philHealthDeduction;
        private final double pagIBIGDeduction;
        private final double taxDeduction;
        private final double totalDeductions;
        private final double netSalary;
        
        public DeductionDetails(Employee employee, SalaryCalculator calculator) {
            this.employee = employee;
            this.monthlySalary = calculator.calculateGrossMonthlySalary(employee);
            this.sssDeduction = calculator.calculateSSSDeduction(monthlySalary);
            this.philHealthDeduction = calculator.calculatePhilHealthDeduction(monthlySalary);
            this.pagIBIGDeduction = calculator.calculatePagIBIGDeduction(monthlySalary);
            this.taxDeduction = calculator.calculateWithholdingTax(monthlySalary);
            this.totalDeductions = sssDeduction + philHealthDeduction + pagIBIGDeduction + taxDeduction;
            this.netSalary = monthlySalary - totalDeductions;
        }
        
        // Getters (Controlled access - Encapsulation)
        public Employee getEmployee() { return employee; }
        public double getMonthlySalary() { return monthlySalary; }
        public double getSssDeduction() { return sssDeduction; }
        public double getPhilHealthDeduction() { return philHealthDeduction; }
        public double getPagIBIGDeduction() { return pagIBIGDeduction; }
        public double getTaxDeduction() { return taxDeduction; }
        public double getTotalDeductions() { return totalDeductions; }
        public double getNetSalary() { return netSalary; }
        
        public String generateDeductionReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("                 MONTHLY DEDUCTION BREAKDOWN                    \n");
            sb.append("═══════════════════════════════════════════════════════════════\n\n");
            
            sb.append("EMPLOYEE INFORMATION\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("Name            : %s\n", employee.getFullName()));
            sb.append(String.format("Employee Number : %s\n", employee.getEmployeeNumber()));
            sb.append(String.format("Position        : %s\n\n", employee.getPosition()));
            
            sb.append("SALARY BREAKDOWN\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("Basic Salary              : ₱%,12.2f\n", employee.getBasicSalary()));
            sb.append(String.format("Rice Subsidy              : ₱%,12.2f\n", employee.getRiceSubsidy()));
            sb.append(String.format("Clothing Allowance        : ₱%,12.2f\n", employee.getClothingAllowance()));
            sb.append(String.format("                            ─────────────\n"));
            sb.append(String.format("GROSS MONTHLY SALARY      : ₱%,12.2f\n\n", monthlySalary));
            
            sb.append("MANDATORY DEDUCTIONS\n");
            sb.append("─────────────────────────────────────────────────────────────\n");
            sb.append(String.format("SSS Contribution          : ₱%,12.2f\n", sssDeduction));
            sb.append(String.format("PhilHealth Contribution   : ₱%,12.2f\n", philHealthDeduction));
            sb.append(String.format("Pag-IBIG Contribution     : ₱%,12.2f\n", pagIBIGDeduction));
            sb.append(String.format("Withholding Tax           : ₱%,12.2f\n", taxDeduction));
            sb.append(String.format("                            ─────────────\n"));
            sb.append(String.format("TOTAL DEDUCTIONS          : ₱%,12.2f\n\n", totalDeductions));
            
            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append(String.format("NET MONTHLY SALARY        : ₱%,12.2f\n", netSalary));
            sb.append("═══════════════════════════════════════════════════════════════\n");
            
            return sb.toString();
        }
    }
}