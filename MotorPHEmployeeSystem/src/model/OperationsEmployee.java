package model;

public class OperationsEmployee extends Employee {
    
    public OperationsEmployee(String employeeNumber, String lastName, String firstName,
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
        return "Operations";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages logistics, supply chain, and operational processes";
    }
}