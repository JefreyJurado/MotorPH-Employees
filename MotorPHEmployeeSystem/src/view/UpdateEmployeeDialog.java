package view;

import model.Employee;
import dao.EmployeeRepository;
import util.EmployeeFactory;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateEmployeeDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(230, 126, 34);
    private static final Color WHITE = Color.WHITE;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private final EmployeeRepository employeeRepository;
    private final Employee originalEmployee;
    private final int employeeIndex;
    private boolean employeeUpdated = false;
    
    // Form fields - Personal Information
    private JTextField empNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JDateChooser birthdayDateChooser;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField sssField;
    private JTextField philhealthField;
    private JTextField tinField;
    private JTextField pagibigField;
    
    // Form fields - Employment Details
    private JTextField positionField;
    private JComboBox<String> statusCombo;
    private JTextField immediateSupervisorField;
    private JTextField basicSalaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;
    private JTextField semiMonthlyRateField;
    private JTextField hourlyRateField;
    
    public UpdateEmployeeDialog(JFrame parent, EmployeeRepository repository, 
                                Employee employee, int index) {
        super(parent, "Update Employee", true);
        this.employeeRepository = repository;
        this.originalEmployee = employee;
        this.employeeIndex = index;
        
        setSize(900, 780);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(900, 60));
        JLabel titleLabel = new JLabel("Update Employee: " + employee.getFullName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with two sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Personal Information
        JPanel personalInfoPanel = createPersonalInfoPanel();
        mainPanel.add(personalInfoPanel);
        
        // Right panel - Employment Details
        JPanel employmentPanel = createEmploymentDetailsPanel();
        mainPanel.add(employmentPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Pre-fill with employee data
        populateFields();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton updateButton = new JButton("Update Employee");
        updateButton.setBackground(PRIMARY_COLOR);
        updateButton.setForeground(WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setFocusPainted(false);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        updateButton.addActionListener(e -> handleUpdate());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Personal Information",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        
        empNumberField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        
        birthdayDateChooser = new JDateChooser();
        birthdayDateChooser.setDateFormatString("MM/dd/yyyy");
        birthdayDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        addressField = new JTextField();
        phoneNumberField = new JTextField();
        sssField = new JTextField();
        philhealthField = new JTextField();
        tinField = new JTextField();
        pagibigField = new JTextField();
        
        panel.add(createFieldPanel("Employee Number:", empNumberField));
        panel.add(createFieldPanel("Last Name:", lastNameField));
        panel.add(createFieldPanel("First Name:", firstNameField));
        panel.add(createFieldPanel("Birthday:", birthdayDateChooser));
        panel.add(createFieldPanel("Address:", addressField));
        panel.add(createFieldPanel("Phone Number:", phoneNumberField));
        panel.add(createFieldPanel("SSS Number:", sssField));
        panel.add(createFieldPanel("PhilHealth Number:", philhealthField));
        panel.add(createFieldPanel("TIN:", tinField));
        panel.add(createFieldPanel("Pag-IBIG Number:", pagibigField));
        
        return panel;
    }
    
    private JPanel createEmploymentDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Employment Details",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        
        positionField = new JTextField();
        statusCombo = new JComboBox<>(new String[]{"Regular", "Probationary"});
        immediateSupervisorField = new JTextField();
        basicSalaryField = new JTextField();
        riceSubsidyField = new JTextField();
        phoneAllowanceField = new JTextField();
        clothingAllowanceField = new JTextField();
        semiMonthlyRateField = new JTextField();
        hourlyRateField = new JTextField();
        
        panel.add(createFieldPanel("Position:", positionField));
        panel.add(createFieldPanel("Status:", statusCombo));
        panel.add(createFieldPanel("Immediate Supervisor:", immediateSupervisorField));
        panel.add(createFieldPanel("Basic Salary:", basicSalaryField));
        panel.add(createFieldPanel("Rice Subsidy:", riceSubsidyField));
        panel.add(createFieldPanel("Phone Allowance:", phoneAllowanceField));
        panel.add(createFieldPanel("Clothing Allowance:", clothingAllowanceField));
        panel.add(createFieldPanel("Semi-Monthly Rate:", semiMonthlyRateField));
        panel.add(createFieldPanel("Hourly Rate:", hourlyRateField));
        
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        return panel;
    }
    
    private void populateFields() {
        empNumberField.setText(originalEmployee.getEmployeeNumber());
        lastNameField.setText(originalEmployee.getLastName());
        firstNameField.setText(originalEmployee.getFirstName());
        
        // Parse birthday string and set to date chooser
        try {
            String birthdayStr = originalEmployee.getBirthday();
            Date birthday = DATE_FORMAT.parse(birthdayStr);
            birthdayDateChooser.setDate(birthday);
        } catch (Exception e) {
            // If parsing fails, leave date chooser empty
            System.err.println("Error parsing birthday: " + e.getMessage());
        }
        
        addressField.setText(originalEmployee.getAddress());
        phoneNumberField.setText(originalEmployee.getPhoneNumber());
        sssField.setText(originalEmployee.getSssNumber());
        philhealthField.setText(originalEmployee.getPhilhealthNumber());
        tinField.setText(originalEmployee.getTin());
        pagibigField.setText(originalEmployee.getPagibigNumber());
        positionField.setText(originalEmployee.getPosition());
        statusCombo.setSelectedItem(originalEmployee.getStatus());
        immediateSupervisorField.setText(originalEmployee.getImmediateSupervisor());
        basicSalaryField.setText(String.valueOf(originalEmployee.getBasicSalary()));
        riceSubsidyField.setText(String.valueOf(originalEmployee.getRiceSubsidy()));
        phoneAllowanceField.setText(String.valueOf(originalEmployee.getPhoneAllowance()));
        clothingAllowanceField.setText(String.valueOf(originalEmployee.getClothingAllowance()));
        semiMonthlyRateField.setText(String.valueOf(originalEmployee.getSemiMonthlyRate()));
        hourlyRateField.setText(String.valueOf(originalEmployee.getHourlyRate()));
        
        // Make employee number read-only
        empNumberField.setEditable(false);
        empNumberField.setBackground(new Color(240, 240, 240));
    }
    
    private void handleUpdate() {
        // Validate all fields
        if (!validateEmployeeFields()) {
            return;
        }
        
        try {
            Date selectedDate = birthdayDateChooser.getDate();
            String birthday = DATE_FORMAT.format(selectedDate);
            
            // Create updated employee using factory
            Employee updatedEmployee = EmployeeFactory.createEmployee(
                empNumberField.getText().trim(),
                lastNameField.getText().trim(),
                firstNameField.getText().trim(),
                birthday,  
                addressField.getText().trim(),
                phoneNumberField.getText().trim(),
                sssField.getText().trim(),
                philhealthField.getText().trim(),
                tinField.getText().trim(),
                pagibigField.getText().trim(),
                (String) statusCombo.getSelectedItem(),
                positionField.getText().trim(),
                immediateSupervisorField.getText().trim(),
                Double.parseDouble(basicSalaryField.getText().trim()),
                Double.parseDouble(riceSubsidyField.getText().trim()),
                Double.parseDouble(phoneAllowanceField.getText().trim()),
                Double.parseDouble(clothingAllowanceField.getText().trim()),
                Double.parseDouble(semiMonthlyRateField.getText().trim()),
                Double.parseDouble(hourlyRateField.getText().trim())
            );
            
            // Update in repository
            employeeRepository.updateEmployee(employeeIndex, updatedEmployee);
            employeeRepository.saveToCSV();
            
            employeeUpdated = true;
            JOptionPane.showMessageDialog(this,
                "Employee updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error updating employee: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
   // COMPLETE VALIDATION METHOD

    private boolean validateEmployeeFields() {
        // Get field values
        String empNumber = empNumberField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        Date birthdayDate = birthdayDateChooser.getDate();
        String position = positionField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        String phoneNumber = phoneNumberField.getText().trim();
        String sss = sssField.getText().trim();
        String philhealth = philhealthField.getText().trim();
        String tin = tinField.getText().trim();
        String pagibig = pagibigField.getText().trim();
        
        // VALIDATION 1: Required Fields Check

        if (empNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Employee Number is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Last Name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }
        
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "First Name is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }
        
        if (birthdayDate == null) {
            JOptionPane.showMessageDialog(this,
                "Birthday is required!\n\n" +
                "Please select a date from the calendar.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (position.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Position is required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            positionField.requestFocus();
            return false;
        }
        
        // VALIDATION 2: SSS Format Validation

        if (!sss.isEmpty() && !sss.matches("^\\d{2}-\\d{7}-\\d{1}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid SSS Number format!\n\n" +
                "Required format: XX-XXXXXXX-X\n" +
                "Example: 34-1234567-8\n\n" +
                "Current value: " + sss,
                "SSS Validation Error",
                JOptionPane.ERROR_MESSAGE);
            sssField.requestFocus();
            return false;
        }
        
        // VALIDATION 3: PhilHealth Format Validation

        if (!philhealth.isEmpty() && !philhealth.matches("^\\d{2}-\\d{9}-\\d{1}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid PhilHealth Number format!\n\n" +
                "Required format: XX-XXXXXXXXX-X\n" +
                "Example: 12-345678901-2\n\n" +
                "Current value: " + philhealth,
                "PhilHealth Validation Error",
                JOptionPane.ERROR_MESSAGE);
            philhealthField.requestFocus();
            return false;
        }
        
        // VALIDATION 4: TIN Format Validation

        if (!tin.isEmpty() && !tin.matches("^\\d{3}-\\d{3}-\\d{3}-\\d{3}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid TIN format!\n\n" +
                "Required format: XXX-XXX-XXX-XXX\n" +
                "Example: 123-456-789-000\n\n" +
                "Current value: " + tin,
                "TIN Validation Error",
                JOptionPane.ERROR_MESSAGE);
            tinField.requestFocus();
            return false;
        }
        
        // VALIDATION 5: Pag-IBIG Format Validation

        if (!pagibig.isEmpty() && !pagibig.matches("^\\d{4}-\\d{4}-\\d{4}$")) {
            JOptionPane.showMessageDialog(this,
                "Invalid Pag-IBIG Number format!\n\n" +
                "Required format: XXXX-XXXX-XXXX\n" +
                "Example: 1234-5678-9012\n\n" +
                "Current value: " + pagibig,
                "Pag-IBIG Validation Error",
                JOptionPane.ERROR_MESSAGE);
            pagibigField.requestFocus();
            return false;
        }
        
        // VALIDATION 6: Numeric Format Check

        try {
            Double.parseDouble(basicSalaryField.getText().trim());
            Double.parseDouble(riceSubsidyField.getText().trim());
            Double.parseDouble(phoneAllowanceField.getText().trim());
            Double.parseDouble(clothingAllowanceField.getText().trim());
            Double.parseDouble(semiMonthlyRateField.getText().trim());
            Double.parseDouble(hourlyRateField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for all salary and allowance fields!\n\n" +
                "Use digits only, no letters or special characters (except decimal point).",
                "Numeric Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // VALIDATION 7: Non-Negative Values Check

        double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
        double riceSubsidy = Double.parseDouble(riceSubsidyField.getText().trim());
        double phoneAllowance = Double.parseDouble(phoneAllowanceField.getText().trim());
        double clothingAllowance = Double.parseDouble(clothingAllowanceField.getText().trim());
        double semiMonthlyRate = Double.parseDouble(semiMonthlyRateField.getText().trim());
        double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
        
        if (basicSalary < 0) {
            JOptionPane.showMessageDialog(this,
                "Basic Salary cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            basicSalaryField.requestFocus();
            return false;
        }
        
        if (riceSubsidy < 0) {
            JOptionPane.showMessageDialog(this,
                "Rice Subsidy cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            riceSubsidyField.requestFocus();
            return false;
        }
        
        if (phoneAllowance < 0) {
            JOptionPane.showMessageDialog(this,
                "Phone Allowance cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            phoneAllowanceField.requestFocus();
            return false;
        }
        
        if (clothingAllowance < 0) {
            JOptionPane.showMessageDialog(this,
                "Clothing Allowance cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            clothingAllowanceField.requestFocus();
            return false;
        }
        
        if (semiMonthlyRate < 0) {
            JOptionPane.showMessageDialog(this,
                "Semi-Monthly Rate cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            semiMonthlyRateField.requestFocus();
            return false;
        }
        
        if (hourlyRate < 0) {
            JOptionPane.showMessageDialog(this,
                "Hourly Rate cannot be negative!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            hourlyRateField.requestFocus();
            return false;
        }
        
        // VALIDATION 8: Status Check

        if (!status.equals("Regular") && !status.equals("Probationary")) {
            JOptionPane.showMessageDialog(this,
                "Status must be either 'Regular' or 'Probationary'!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // VALIDATION 9: Philippine Phone Format Check

        if (!phoneNumber.isEmpty() && 
            !phoneNumber.matches("^(09\\d{2}-\\d{3}-\\d{4}|09\\d{9})$")) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Phone number format should be Philippine mobile format:\n" +
                "  • 09XX-XXX-XXXX (example: 0917-123-4567)\n" +
                "  • Or 09XXXXXXXXX (example: 09171234567)\n\n" +
                "Current value: " + phoneNumber + "\n\n" +
                "Do you want to continue anyway?",
                "Phone Format Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                phoneNumberField.requestFocus();
                return false;
            }
        }
        
        // VALIDATION 10: Low Salary Confirmation

        if (basicSalary < 10000) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Basic salary is below ₱10,000.\n" +
                "Current value: ₱" + String.format("%.2f", basicSalary) + "\n\n" +
                "Do you want to continue?",
                "Low Salary Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                basicSalaryField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    public boolean isEmployeeUpdated() {
        return employeeUpdated;
    }
}