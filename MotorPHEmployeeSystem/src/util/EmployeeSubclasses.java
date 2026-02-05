package util;

import model.Employee;

class HREmployee extends Employee {
    
    public HREmployee(String employeeNumber, String lastName, String firstName,
                     String birthday, String address, String phoneNumber,
                     String sssNumber, String philhealthNumber, String tin,
                     String pagibigNumber, String status, String position,
                     String immediateSupervisor,
                     double basicSalary, double riceSubsidy, double phoneAllowance,
                     double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Human Resources";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages employee information, attendance records, and leave management";
    }
}

class FinanceEmployee extends Employee {
    
    public FinanceEmployee(String employeeNumber, String lastName, String firstName,
                          String birthday, String address, String phoneNumber,
                          String sssNumber, String philhealthNumber, String tin,
                          String pagibigNumber, String status, String position,
                          String immediateSupervisor,
                          double basicSalary, double riceSubsidy, double phoneAllowance,
                          double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Finance";
    }
    
    @Override
    public String getJobDescription() {
        return "Oversees payroll processing, tax calculations, and financial reporting";
    }
}

class ITEmployee extends Employee {
    
    public ITEmployee(String employeeNumber, String lastName, String firstName,
                     String birthday, String address, String phoneNumber,
                     String sssNumber, String philhealthNumber, String tin,
                     String pagibigNumber, String status, String position,
                     String immediateSupervisor,
                     double basicSalary, double riceSubsidy, double phoneAllowance,
                     double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Information Technology";
    }
    
    @Override
    public String getJobDescription() {
        return "Oversees system integration, maintenance, and data security";
    }
}

class AccountingEmployee extends Employee {
    
    public AccountingEmployee(String employeeNumber, String lastName, String firstName,
                             String birthday, String address, String phoneNumber,
                             String sssNumber, String philhealthNumber, String tin,
                             String pagibigNumber, String status, String position,
                             String immediateSupervisor,
                             double basicSalary, double riceSubsidy, double phoneAllowance,
                             double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Accounting";
    }
    
    @Override
    public String getJobDescription() {
        return "Manages financial records, accounts payable/receivable, and auditing";
    }
}

class OperationsEmployee extends Employee {
    
    public OperationsEmployee(String employeeNumber, String lastName, String firstName,
                             String birthday, String address, String phoneNumber,
                             String sssNumber, String philhealthNumber, String tin,
                             String pagibigNumber, String status, String position,
                             String immediateSupervisor,
                             double basicSalary, double riceSubsidy, double phoneAllowance,
                             double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
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
        return "Manages daily business operations, logistics, and process improvements";
    }
}

class ExecutiveEmployee extends Employee {
    
    public ExecutiveEmployee(String employeeNumber, String lastName, String firstName,
                            String birthday, String address, String phoneNumber,
                            String sssNumber, String philhealthNumber, String tin,
                            String pagibigNumber, String status, String position,
                            String immediateSupervisor,
                            double basicSalary, double riceSubsidy, double phoneAllowance,
                            double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "Executive Management";
    }
    
    @Override
    public String getJobDescription() {
        return "Provides strategic leadership and oversees company operations";
    }
}

class SalesEmployee extends Employee {
    
    public SalesEmployee(String employeeNumber, String lastName, String firstName,
                        String birthday, String address, String phoneNumber,
                        String sssNumber, String philhealthNumber, String tin,
                        String pagibigNumber, String status, String position,
                        String immediateSupervisor,
                        double basicSalary, double riceSubsidy, double phoneAllowance,
                        double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
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
        return "Drives revenue through customer acquisition and relationship management";
    }
}

class GeneralEmployee extends Employee {
    
    public GeneralEmployee(String employeeNumber, String lastName, String firstName,
                          String birthday, String address, String phoneNumber,
                          String sssNumber, String philhealthNumber, String tin,
                          String pagibigNumber, String status, String position,
                          String immediateSupervisor,
                          double basicSalary, double riceSubsidy, double phoneAllowance,
                          double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        super(employeeNumber, lastName, firstName, birthday, address, phoneNumber,
              sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
              immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
              clothingAllowance, semiMonthlyRate, hourlyRate);
    }
    
    @Override
    public String getDepartment() {
        return "General";
    }
    
    @Override
    public String getJobDescription() {
        return "Performs general duties and supports various business functions";
    }
}