package ms1cp2manual.refactored;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EmployeeRepository implements IEmployeeRepository {
    private List<Employee> employees; // Stores all employees in memory
    private CSVFileHandler csvFileHandler; // Handles file operations

    public EmployeeRepository() {
        employees = new ArrayList<>();
        csvFileHandler = new CSVFileHandler();
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            List<Employee> loadedEmployees = csvFileHandler.loadEmployeesFromCSV();
            if (loadedEmployees.isEmpty()) {
                // If CSV is empty, initialize with default employees
                initializeDefaultEmployees();
                saveToCSV(); // Save default data to CSV
            } else {
                employees.addAll(loadedEmployees);
            }
        } catch (IOException e) {
            System.err.println("Error loading employees from CSV: " + e.getMessage());
            initializeDefaultEmployees();
        }
    }

    public void saveToCSV() {
        try {
            csvFileHandler.saveEmployeesToCSV(employees);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error saving to CSV: " + e.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeDefaultEmployees() {
        employees.add(new Employee("101", "Lobo-on", "Aljohn", "01/01/1980", 
            "123 Street", "1234567890", "SSS123", "PH123", "TIN123", "PAG123", 
            "Manager", "Active", 50000, 2000, 1000, 25000, 250));
        employees.add(new Employee("102", "Sudara", "Michael", "02/02/1990", 
            "456 Avenue", "0987654321", "SSS456", "PH456", "TIN456", "PAG456", 
            "Software Engineer", "Active", 40000, 1500, 900, 20000, 200));
        employees.add(new Employee("103", "Jurado", "Jefrey", "01/03/1992", 
            "245 Makati", "09179990987", "SSS246", "PH478", "TIN999", "PAG378", 
            "Cybersecurity Head", "Active", 44999, 1900, 300, 20555, 244));
        employees.add(new Employee("104", "Garido", "Mel", "06/07/1980", 
            "678 Boracay", "09996667777", "SSS466", "PH888", "TIN000", "PAG456", 
            "Web Developer", "Active", 46666, 1577, 988, 20999, 245));
        employees.add(new Employee("105", "Bautista", "Marlon", "05/07/1970", 
            "678 Ireland", "09667775555", "SSS222", "PH333", "TIN444", "PAG555", 
            "Network Engineer", "Active", 46777, 1888, 978, 20999, 260));
    }

    @Override
    public void addEmployee(Employee employee) {
        employees.add(employee); // Add to memory
        saveToCSV(); // Save to file
    }

    @Override
    public void updateEmployee(int index, Employee employee) {
        if (index >= 0 && index < employees.size()) {
            employees.set(index, employee);
            saveToCSV();
        }
    }

    @Override
    public void deleteEmployee(int index) {
        if (index >= 0 && index < employees.size()) {
            employees.remove(index);
            saveToCSV();
        }
    }

    @Override
    public Employee getEmployee(int index) {
        if (index >= 0 && index < employees.size()) {
            return employees.get(index);
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    // Search through employees list
    // Return matching employee
    @Override
    public Employee findByEmployeeNumber(String employeeNumber) {
        return employees.stream()
            .filter(emp -> emp.getEmployeeNumber().equals(employeeNumber))
            .findFirst()
            .orElse(null);
    }
}