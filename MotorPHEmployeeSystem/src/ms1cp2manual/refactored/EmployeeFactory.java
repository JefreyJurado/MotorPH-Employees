package ms1cp2manual.refactored;

public class EmployeeFactory {
    
    public static Employee createEmployee(String employeeNumber, String lastName, String firstName,
                                         String birthday, String address, String phoneNumber,
                                         String sssNumber, String philhealthNumber, String tin,
                                         String pagibigNumber, String status, String position,
                                         String immediateSupervisor,
                                         double basicSalary, double riceSubsidy, double phoneAllowance,
                                         double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
        
        String positionLower = position.toLowerCase();
        
        // EXECUTIVE (CEO, COO, CFO, CMO)
        if (positionLower.contains("chief") ||
            positionLower.contains("ceo") || 
            positionLower.contains("coo") ||
            positionLower.contains("cfo") ||
            positionLower.contains("cmo")) {
            return new ExecutiveEmployee(employeeNumber, lastName, firstName, birthday, address,
                                       phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                       status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                       phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // HR DEPARTMENT
        else if (positionLower.contains("hr ") || 
                 positionLower.contains("human resource")) {
            return new HREmployee(employeeNumber, lastName, firstName, birthday, address,
                                phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // ACCOUNTING
        else if (positionLower.contains("accounting") ||
                 positionLower.contains("account ")) {
            return new AccountingEmployee(employeeNumber, lastName, firstName, birthday, address,
                                        phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                        status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                        phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // FINANCE / PAYROLL
        else if (positionLower.contains("finance") ||
                 positionLower.contains("payroll")) {
            return new FinanceEmployee(employeeNumber, lastName, firstName, birthday, address,
                                     phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                     status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                     phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // IT DEPARTMENT
        else if (positionLower.contains("it ") || 
                 positionLower.contains("information technology") ||
                 positionLower.contains("system")) {
            return new ITEmployee(employeeNumber, lastName, firstName, birthday, address,
                                phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // SALES & MARKETING
        else if (positionLower.contains("sales") ||
                 positionLower.contains("marketing")) {
            return new SalesEmployee(employeeNumber, lastName, firstName, birthday, address,
                                   phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                   status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                   phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // OPERATIONS / SUPPLY CHAIN / LOGISTICS
        else if (positionLower.contains("operation") ||
                 positionLower.contains("logistics") ||
                 positionLower.contains("supply chain")) {
            return new OperationsEmployee(employeeNumber, lastName, firstName, birthday, address,
                                        phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                        status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                        phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
        
        // DEFAULT: GENERAL
        else {
            return new GeneralEmployee(employeeNumber, lastName, firstName, birthday, address,
                                     phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                     status, position, immediateSupervisor, basicSalary, riceSubsidy,
                                     phoneAllowance, clothingAllowance, semiMonthlyRate, hourlyRate);
        }
    }
}