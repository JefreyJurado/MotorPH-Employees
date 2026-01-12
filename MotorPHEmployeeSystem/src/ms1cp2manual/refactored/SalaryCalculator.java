package ms1cp2manual.refactored;

public class SalaryCalculator {
    public double calculateMonthlySalary(Employee employee) {
        return employee.getBasicSalary() + employee.calculateTotalAllowances();
    }

    public double calculateAnnualSalary(Employee employee) {
        return calculateMonthlySalary(employee) * 12;
    }

    public String generateSalaryReport(Employee employee, String month) {
        double monthlySalary = calculateMonthlySalary(employee);
        
        return "Employee Details\n" +
               "----------------\n" +
               "Name: " + employee.getFullName() + "\n" +
               "Position: " + employee.getPosition() + "\n" +
               "Basic Salary: " + employee.getBasicSalary() + "\n" +
               "Rice Subsidy: " + employee.getRiceSubsidy() + "\n" +
               "Clothing Allowance: " + employee.getClothingAllowance() + "\n" +
               "\nSelected Month: " + month + "\n" +
               "Total Monthly Salary: " + monthlySalary;
    }
}