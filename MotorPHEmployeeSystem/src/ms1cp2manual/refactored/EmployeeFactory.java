package ms1cp2manual.refactored;

public class EmployeeFactory {
    
    public static Employee createEmployee(String employeeNumber, String lastName, String firstName,
                                         String birthday, String address, String phoneNumber,
                                         String sssNumber, String philhealthNumber, String tin,
                                         String pagibigNumber, String position, String status,
                                         double basicSalary, double riceSubsidy, double clothingAllowance,
                                         double semiMonthlyRate, double hourlyRate) {
        
        String positionLower = position.toLowerCase();
        
        if (positionLower.contains("ceo") || positionLower.contains("chief executive") ||
            positionLower.contains("coo") || positionLower.contains("cfo")) {
            return new ExecutiveEmployee(employeeNumber, lastName, firstName, birthday, address,
                                       phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                       position, status, basicSalary, riceSubsidy, clothingAllowance,
                                       semiMonthlyRate, hourlyRate);
        }
        else if (positionLower.contains("hr ") || positionLower.contains("human resource")) {
            return new HREmployee(employeeNumber, lastName, firstName, birthday, address,
                                phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                position, status, basicSalary, riceSubsidy, clothingAllowance,
                                semiMonthlyRate, hourlyRate);
        }
        else if (positionLower.contains("accountant") || positionLower.contains("accounting")) {
            return new AccountingEmployee(employeeNumber, lastName, firstName, birthday, address,
                                        phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                        position, status, basicSalary, riceSubsidy, clothingAllowance,
                                        semiMonthlyRate, hourlyRate);
        }
        else if (positionLower.contains("finance") || positionLower.contains("payroll")) {
            return new FinanceEmployee(employeeNumber, lastName, firstName, birthday, address,
                                     phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                     position, status, basicSalary, riceSubsidy, clothingAllowance,
                                     semiMonthlyRate, hourlyRate);
        }
        else if (positionLower.contains("it ") || positionLower.contains("developer") ||
                 positionLower.contains("engineer") || positionLower.contains("programmer")) {
            return new ITEmployee(employeeNumber, lastName, firstName, birthday, address,
                                phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                position, status, basicSalary, riceSubsidy, clothingAllowance,
                                semiMonthlyRate, hourlyRate);
        }
        else if (positionLower.contains("operation") || positionLower.contains("logistics")) {
            return new OperationsEmployee(employeeNumber, lastName, firstName, birthday, address,
                                        phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                        position, status, basicSalary, riceSubsidy, clothingAllowance,
                                        semiMonthlyRate, hourlyRate);
        }
        else {
            return new GeneralEmployee(employeeNumber, lastName, firstName, birthday, address,
                                     phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber,
                                     position, status, basicSalary, riceSubsidy, clothingAllowance,
                                     semiMonthlyRate, hourlyRate);
        }
    }
}