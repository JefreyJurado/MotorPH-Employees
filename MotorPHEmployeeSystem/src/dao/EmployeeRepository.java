package dao;
import model.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class EmployeeRepository implements IEmployeeRepository {
    private final List<Employee> employees;
    private final CSVFileHandler csvFileHandler;
    
    public EmployeeRepository() {
        employees = new ArrayList<>();
        csvFileHandler = new CSVFileHandler();
        loadFromCSV();
    }
    
    private void loadFromCSV() {
        try {
            List<Employee> loadedEmployees = csvFileHandler.loadEmployeesFromCSV();
            if (loadedEmployees.isEmpty()) {
                initializeDefaultEmployees();
                saveToCSV();
            } else {
                employees.addAll(loadedEmployees);
            }
        } catch (IOException e) {
            System.err.println("Error loading employees from CSV: " + e.getMessage());
            initializeDefaultEmployees();
        }
    }
    
        @Override
        public void saveToCSV() {
            try {
                csvFileHandler.saveEmployeesToCSV(employees);
            } catch (IOException e) {
                System.err.println("ERROR: Failed to save employees to CSV - " + e.getMessage());
                e.printStackTrace();
                // Throw runtime exception to let View layer handle error display
                throw new RuntimeException("Error saving to CSV: " + e.getMessage(), e);
            }
        }
    
    // Empty method - real data loaded from CSV
    private void initializeDefaultEmployees() {
    // Real MotorPH employees (34 total) will be loaded from employees.csv
    }
    
    @Override
    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveToCSV();
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
    
    @Override
    public Employee findByEmployeeNumber(String employeeNumber) {
        return employees.stream()
            .filter(emp -> emp.getEmployeeNumber().equals(employeeNumber))
            .findFirst()
            .orElse(null);
    }
}