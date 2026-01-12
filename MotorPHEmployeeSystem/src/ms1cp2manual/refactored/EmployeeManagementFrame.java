package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeManagementFrame extends JFrame {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    
    private JTextField tfEmpNum, tfLastName, tfFirstName, tfBDay, tfAdd;
    private JTextField tfPhone, tfSSS, tfPhealth, tfTin, tfPag;
    private JTextField tfPosition, tfStatus, tfSalary, tfRice;
    private JTextField tfClothing, tfSemiMo, tfHourlyR;
    
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JButton updateButton;

    public EmployeeManagementFrame(EmployeeRepository repository) {
        this.employeeRepository = repository;
        this.salaryCalculator = new SalaryCalculator();
        initializeUI();
        loadEmployeeData();
    }

    private void initializeUI() {
        setTitle("MotorPH Employees");
        setSize(1000, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        createLabels();
        createTextFields();
        createTable();
        createButtons();
    }

    private void createLabels() {
        JLabel titleLabel = new JLabel("Employee Details");
        titleLabel.setBounds(20, 15, 200, 100);
        titleLabel.setFont(new Font("Nunito", Font.BOLD, 20));
        add(titleLabel);

        addLabel("Employee Number", 30, 70);
        addLabel("Last Name", 30, 110);
        addLabel("First Name", 30, 150);
        addLabel("Birthday", 30, 190);
        addLabel("Address", 30, 230);
        addLabel("Phone Number", 30, 320);
        addLabel("SSS Number", 30, 360);
        addLabel("Philhealth Number", 30, 400);
        addLabel("TIN", 30, 440);
        addLabel("Pagibig Number", 30, 480);
        addLabel("Position", 410, 70);
        addLabel("Status", 410, 110);
        addLabel("Basic Salary", 410, 150);
        addLabel("Rice Subsidy", 410, 190);
        addLabel("Clothing Allowance", 410, 230);
        addLabel("Semi Monthly Rate", 410, 270);
        addLabel("Hourly Rate", 410, 310);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 100);
        label.setFont(new Font("Nunito", Font.BOLD, 16));
        add(label);
    }

    private void createTextFields() {
        tfEmpNum = createTextField(180, 110);
        tfLastName = createTextField(180, 150);
        tfFirstName = createTextField(180, 190);
        tfBDay = createTextField(180, 230);
        tfAdd = createTextField(180, 270, 200, 75);
        tfPhone = createTextField(180, 360);
        tfSSS = createTextField(180, 400);
        tfPhealth = createTextField(180, 440);
        tfTin = createTextField(180, 480);
        tfPag = createTextField(180, 520);
        tfPosition = createTextField(510, 110);
        tfStatus = createTextField(510, 150);
        tfSalary = createTextField(510, 190);
        tfRice = createTextField(570, 230, 138, 25);
        tfClothing = createTextField(570, 270, 138, 25);
        tfSemiMo = createTextField(570, 310, 138, 25);
        tfHourlyR = createTextField(570, 350, 138, 25);
    }

    private JTextField createTextField(int x, int y) {
        return createTextField(x, y, 200, 25);
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        add(textField);
        return textField;
    }

    private void createTable() {
        String[] columnNames = {"Employee Number", "Last Name", "First Name", 
            "Birthday", "Address", "Phone Number", "SSS Number", "Philhealth Number", 
            "TIN", "Pagibig Number", "Position", "Status", "Basic Salary", 
            "Rice Subsidy", "Clothing Allowance", "Semi Monthly Rate", "Hourly Rate"};
        
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBounds(20, 600, 950, 200);
        add(scrollPane);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleRowSelection();
            }
        });
    }

    private void handleRowSelection() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee employee = employeeRepository.getEmployee(selectedRow);
            if (employee != null) {
                populateFields(employee);
                updateButton.setEnabled(true);
            }
        }
    }

    private void populateFields(Employee employee) {
        tfEmpNum.setText(employee.getEmployeeNumber());
        tfLastName.setText(employee.getLastName());
        tfFirstName.setText(employee.getFirstName());
        tfBDay.setText(employee.getBirthday());
        tfAdd.setText(employee.getAddress());
        tfPhone.setText(employee.getPhoneNumber());
        tfSSS.setText(employee.getSssNumber());
        tfPhealth.setText(employee.getPhilhealthNumber());
        tfTin.setText(employee.getTin());
        tfPag.setText(employee.getPagibigNumber());
        tfPosition.setText(employee.getPosition());
        tfStatus.setText(employee.getStatus());
        tfSalary.setText(String.valueOf(employee.getBasicSalary()));
        tfRice.setText(String.valueOf(employee.getRiceSubsidy()));
        tfClothing.setText(String.valueOf(employee.getClothingAllowance()));
        tfSemiMo.setText(String.valueOf(employee.getSemiMonthlyRate()));
        tfHourlyR.setText(String.valueOf(employee.getHourlyRate()));
    }

    private void createButtons() {
        JButton queryButton = new JButton("Query");
        queryButton.setBounds(363, 60, 100, 25);
        queryButton.addActionListener(e -> handleQuery());
        add(queryButton);

        JButton addButton = new JButton("Add");
        addButton.setBounds(462, 60, 100, 25);
        addButton.addActionListener(e -> handleAdd());
        add(addButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(560, 60, 100, 25);
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> handleUpdate());
        add(updateButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(655, 60, 100, 25);
        clearButton.addActionListener(e -> clearFields());
        add(clearButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(755, 60, 100, 25);
        deleteButton.addActionListener(e -> handleDelete());
        add(deleteButton);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(855, 60, 100, 25);
        saveButton.addActionListener(e -> handleSave());
        add(saveButton);

        JButton viewButton = new JButton("View Employee");
        viewButton.setBounds(755, 20, 200, 25);
        viewButton.addActionListener(e -> openViewEmployeeDialog());
        add(viewButton);

        JButton leaveButton = new JButton("Leave Application");
        leaveButton.setBounds(755, 100, 200, 25);
        leaveButton.addActionListener(e -> openLeaveApplicationDialog());
        add(leaveButton);
    }

    private void handleAdd() {
        try {
            Employee employee = createEmployeeFromFields();
            employeeRepository.addEmployee(employee);
            tableModel.addRow(employee.toTableRow());
            clearFields();
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Employee employee = createEmployeeFromFields();
                employeeRepository.updateEmployee(selectedRow, employee);
                
                // Update table
                Object[] rowData = employee.toTableRow();
                for (int i = 0; i < rowData.length; i++) {
                    tableModel.setValueAt(rowData[i], selectedRow, i);
                }
                
                clearFields();
                updateButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating employee: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDelete() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this employee?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                employeeRepository.deleteEmployee(selectedRow);
                tableModel.removeRow(selectedRow);
                clearFields();
                updateButton.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "No employee selected to delete", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private Employee createEmployeeFromFields() {
        return new Employee(
            tfEmpNum.getText(),
            tfLastName.getText(),
            tfFirstName.getText(),
            tfBDay.getText(),
            tfAdd.getText(),
            tfPhone.getText(),
            tfSSS.getText(),
            tfPhealth.getText(),
            tfTin.getText(),
            tfPag.getText(),
            tfPosition.getText(),
            tfStatus.getText(),
            Double.parseDouble(tfSalary.getText()),
            Double.parseDouble(tfRice.getText()),
            Double.parseDouble(tfClothing.getText()),
            Double.parseDouble(tfSemiMo.getText()),
            Double.parseDouble(tfHourlyR.getText())
        );
    }

    private void clearFields() {
        tfEmpNum.setText("");
        tfLastName.setText("");
        tfFirstName.setText("");
        tfBDay.setText("");
        tfAdd.setText("");
        tfPhone.setText("");
        tfSSS.setText("");
        tfPhealth.setText("");
        tfTin.setText("");
        tfPag.setText("");
        tfPosition.setText("");
        tfStatus.setText("");
        tfSalary.setText("");
        tfRice.setText("");
        tfClothing.setText("");
        tfSemiMo.setText("");
        tfHourlyR.setText("");
    }

    private void loadEmployeeData() {
        List<Employee> employees = employeeRepository.getAllEmployees();
        for (Employee employee : employees) {
            tableModel.addRow(employee.toTableRow());
        }
    }

    private void openViewEmployeeDialog() {
        new ViewEmployeeDialog(this, employeeRepository, salaryCalculator).setVisible(true);
    }

    private void openLeaveApplicationDialog() {
        new LeaveApplicationDialog(this).setVisible(true);
    }

    // NEW METHOD: Handle Query button functionality
    private void handleQuery() {
        String empNumber = tfEmpNum.getText().trim();
        
        if (empNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter an Employee Number", 
                "Query", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Employee employee = employeeRepository.findByEmployeeNumber(empNumber);
        
        if (employee != null) {
            populateFields(employee);
            
            // Find and select the row in the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(empNumber)) {
                    employeeTable.setRowSelectionInterval(i, i);
                    employeeTable.scrollRectToVisible(employeeTable.getCellRect(i, 0, true));
                    break;
                }
            }
            
            updateButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, 
                "Employee found!", 
                "Query Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Employee Number not found", 
                "Query Failed", 
                JOptionPane.ERROR_MESSAGE);
            clearFields();
        }
    }

    // NEW METHOD: Handle Save button functionality
    private void handleSave() {
        employeeRepository.saveToCSV();
        JOptionPane.showMessageDialog(this, 
            "All data saved to CSV successfully!", 
            "Save Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}