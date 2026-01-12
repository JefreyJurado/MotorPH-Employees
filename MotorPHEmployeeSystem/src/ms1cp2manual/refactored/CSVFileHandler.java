package ms1cp2manual.refactored;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileHandler {
    private static final String EMPLOYEE_CSV_FILE = "employees.csv";
    private static final String CSV_HEADER = "Employee Number,Last Name,First Name,Birthday,Address,Phone Number,SSS Number,Philhealth Number,TIN,Pagibig Number,Position,Status,Basic Salary,Rice Subsidy,Clothing Allowance,Semi Monthly Rate,Hourly Rate";

    public void saveEmployeesToCSV(List<Employee> employees) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_CSV_FILE))) {
            // Write header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write employee data
            for (Employee emp : employees) {
                String line = String.join(",",
                    emp.getEmployeeNumber(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getBirthday(),
                    "\"" + emp.getAddress() + "\"", // Quote address in case it contains commas
                    emp.getPhoneNumber(),
                    emp.getSssNumber(),
                    emp.getPhilhealthNumber(),
                    emp.getTin(),
                    emp.getPagibigNumber(),
                    emp.getPosition(),
                    emp.getStatus(),
                    String.valueOf(emp.getBasicSalary()),
                    String.valueOf(emp.getRiceSubsidy()),
                    String.valueOf(emp.getClothingAllowance()),
                    String.valueOf(emp.getSemiMonthlyRate()),
                    String.valueOf(emp.getHourlyRate())
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public List<Employee> loadEmployeesFromCSV() throws IOException {
        List<Employee> employees = new ArrayList<>();
        File file = new File(EMPLOYEE_CSV_FILE);

        if (!file.exists()) {
            return employees; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                Employee employee = parseEmployeeFromCSV(line);
                if (employee != null) {
                    employees.add(employee);
                }
            }
        }

        return employees;
    }

    private Employee parseEmployeeFromCSV(String line) {
        try {
            // Handle quoted fields (addresses with commas)
            List<String> values = parseCSVLine(line);

            if (values.size() < 17) {
                return null;
            }

            return new Employee(
                values.get(0),  // employeeNumber
                values.get(1),  // lastName
                values.get(2),  // firstName
                values.get(3),  // birthday
                values.get(4),  // address
                values.get(5),  // phoneNumber
                values.get(6),  // sssNumber
                values.get(7),  // philhealthNumber
                values.get(8),  // tin
                values.get(9),  // pagibigNumber
                values.get(10), // position
                values.get(11), // status
                Double.parseDouble(values.get(12)), // basicSalary
                Double.parseDouble(values.get(13)), // riceSubsidy
                Double.parseDouble(values.get(14)), // clothingAllowance
                Double.parseDouble(values.get(15)), // semiMonthlyRate
                Double.parseDouble(values.get(16))  // hourlyRate
            );
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    private List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString().trim());

        return values;
    }
}