package model;

public class SalesEmployee extends Employee {
    
    public SalesEmployee(String employeeNumber, String lastName, String firstName,
                        String birthday, String address, String phoneNumber,
                        String sssNumber, String philhealthNumber, String tin,
                        String pagibigNumber, String status, String position,
                        String immediateSupervisor, double basicSalary, double riceSubsidy,
                        double phoneAllowance, double clothingAllowance,
                        double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Sales & Marketing";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages sales operations and customer relationships";
    }
}