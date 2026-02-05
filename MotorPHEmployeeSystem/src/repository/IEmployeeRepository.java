package repository;

import model.Employee;
import java.util.List;

public interface IEmployeeRepository {
    void addEmployee(Employee employee);
    void updateEmployee(int index, Employee employee);
    void deleteEmployee(int index);
    Employee getEmployee(int index);
    List<Employee> getAllEmployees();
    Employee findByEmployeeNumber(String employeeNumber);
    void saveToCSV();   
}