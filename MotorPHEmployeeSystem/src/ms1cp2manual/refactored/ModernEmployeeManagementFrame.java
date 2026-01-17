package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ModernEmployeeManagementFrame extends JFrame {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    
    // Modern Color Scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Blue
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);    // Light Blue
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);      // Green
    private final Color DANGER_COLOR = new Color(231, 76, 60);        // Red
    private final Color WARNING_COLOR = new Color(241, 196, 15);      // Yellow
    private final Color DARK_COLOR = new Color(44, 62, 80);           // Dark Blue-Gray
    private final Color LIGHT_BG = new Color(236, 240, 241);          // Light Gray
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JTextField tfEmpNum, tfLastName, tfFirstName, tfBDay, tfAdd;
    private JTextField tfPhone, tfSSS, tfPhealth, tfTin, tfPag;
    private JTextField tfPosition, tfStatus, tfSalary, tfRice;
    private JTextField tfClothing, tfSemiMo, tfHourlyR;
    
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JButton updateButton;

    public ModernEmployeeManagementFrame(EmployeeRepository repository) {
        this.employeeRepository = repository;
        this.salaryCalculator = new SalaryCalculator();
        initializeModernUI();
        loadEmployeeData();
    }

    private void initializeModernUI() {
        setTitle("MotorPH Employee Management System");
        setSize(1400, 950);  // INCREASED from 850 to 950
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(LIGHT_BG);
        
        // Header Panel
        createHeaderPanel(mainPanel);
        
        // Left Panel - Employee Information
        createEmployeeInfoPanel(mainPanel);
        
        // Right Panel - Employment Details (now includes payroll buttons)
        createEmploymentDetailsPanel(mainPanel);
        
        // Action Buttons Panel
        createActionButtonsPanel(mainPanel);
        
        // Table Panel
        createModernTable(mainPanel);
        
        setContentPane(mainPanel);
    }
    

    private void createHeaderPanel(JPanel parent) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1400, 80);
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("MotorPH Employee Management");
        titleLabel.setBounds(30, 15, 500, 50);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        parent.add(headerPanel);
    }

    private void createEmployeeInfoPanel(JPanel parent) {
        JPanel panel = createModernPanel("Personal Information", 30, 100, 650, 480);
        
        int yPos = 50;
        int labelX = 20;
        int fieldX = 200;
        int spacing = 45;
        
        addModernField(panel, "Employee Number", tfEmpNum = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Last Name", tfLastName = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "First Name", tfFirstName = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Birthday", tfBDay = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(labelX, yPos, 150, 25);
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressLabel.setForeground(TEXT_COLOR);
        panel.add(addressLabel);
        
        tfAdd = new JTextField();
        tfAdd.setBounds(fieldX, yPos, 420, 60);
        styleTextField(tfAdd);
        panel.add(tfAdd);
        yPos += 75;
        
        addModernField(panel, "Phone Number", tfPhone = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "SSS Number", tfSSS = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "PhilHealth Number", tfPhealth = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "TIN", tfTin = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Pag-IBIG Number", tfPag = createStyledTextField(), labelX, fieldX, yPos);
        
        parent.add(panel);
    }

    private void createEmploymentDetailsPanel(JPanel parent) {
        JPanel panel = createModernPanel("Employment Details", 700, 100, 650, 480);
        
        int yPos = 50;
        int labelX = 20;
        int fieldX = 200;
        int spacing = 45;
        
        addModernField(panel, "Position", tfPosition = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Status", tfStatus = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Basic Salary", tfSalary = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Rice Subsidy", tfRice = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Clothing Allowance", tfClothing = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Semi-Monthly Rate", tfSemiMo = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing;
        
        addModernField(panel, "Hourly Rate", tfHourlyR = createStyledTextField(), labelX, fieldX, yPos);
        yPos += spacing + 20;  // Extra space before buttons
        
        // Payroll Actions Buttons - Inside Employment Details panel
        JLabel payrollLabel = new JLabel("Payroll Actions:");
        payrollLabel.setBounds(labelX, yPos, 150, 25);
        payrollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        payrollLabel.setForeground(PRIMARY_COLOR);
        panel.add(payrollLabel);
        yPos += 35;
        
        JButton weeklyPayslipBtn = createModernButton("Generate Weekly Payslip", new Color(155, 89, 182));
        weeklyPayslipBtn.setBounds(labelX, yPos, 280, 40);
        weeklyPayslipBtn.addActionListener(e -> openWeeklyPayslipDialog());
        panel.add(weeklyPayslipBtn);
        
        JButton viewDeductionsBtn = createModernButton("View Deductions", new Color(26, 188, 156));
        viewDeductionsBtn.setBounds(labelX + 300, yPos, 280, 40);
        viewDeductionsBtn.addActionListener(e -> openViewDeductionsDialog());
        panel.add(viewDeductionsBtn);
        
        parent.add(panel);
    }

    private void createActionButtonsPanel(JPanel parent) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(30, 590, 1320, 60);  // Back to original position
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        // Create modern buttons
        JButton queryBtn = createModernButton("Query", PRIMARY_COLOR);
        queryBtn.addActionListener(e -> handleQuery());
        buttonPanel.add(queryBtn);
        
        JButton addBtn = createModernButton("Add", SUCCESS_COLOR);
        addBtn.addActionListener(e -> handleAdd());
        buttonPanel.add(addBtn);
        
        updateButton = createModernButton("Update", WARNING_COLOR);
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> handleUpdate());
        buttonPanel.add(updateButton);
        
        JButton deleteBtn = createModernButton("Delete", DANGER_COLOR);
        deleteBtn.addActionListener(e -> handleDelete());
        buttonPanel.add(deleteBtn);
        
        JButton clearBtn = createModernButton("Clear", SECONDARY_COLOR);
        clearBtn.addActionListener(e -> clearFields());
        buttonPanel.add(clearBtn);
        
        JButton saveBtn = createModernButton("Save", new Color(142, 68, 173));
        saveBtn.addActionListener(e -> handleSave());
        buttonPanel.add(saveBtn);
        
        JButton viewBtn = createModernButton("View Employee", new Color(52, 73, 94));
        viewBtn.addActionListener(e -> openViewEmployeeDialog());
        buttonPanel.add(viewBtn);
        
        JButton leaveBtn = createModernButton("Leave Application", new Color(230, 126, 34));
        leaveBtn.addActionListener(e -> openLeaveApplicationDialog());
        buttonPanel.add(leaveBtn);

        parent.add(buttonPanel);
    }

    private void createModernTable(JPanel parent) {
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(30, 660, 1320, 180);  // Moved up to make visible
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel tableTitle = new JLabel("Employee Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_COLOR);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columnNames = {"Emp #", "Last Name", "First Name", 
            "Birthday", "Address", "Phone", "SSS", "PhilHealth", 
            "TIN", "Pag-IBIG", "Position", "Status", "Salary", 
            "Rice", "Clothing", "Semi-Monthly", "Hourly"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(30);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setShowVerticalLines(true);
        employeeTable.setGridColor(new Color(189, 195, 199));
        employeeTable.setSelectionBackground(new Color(52, 152, 219, 50));
        employeeTable.setSelectionForeground(TEXT_COLOR);
        employeeTable.setFillsViewportHeight(true);

        // Style table header
        JTableHeader header = employeeTable.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setOpaque(true);

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Make scrolling smooth
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setBlockIncrement(60);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleRowSelection();
            }
        });

        parent.add(tablePanel);
    }

    private JPanel createModernPanel(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setLayout(null);
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(15, 10, 400, 30);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel);
        
        return panel;
    }

    private void addModernField(JPanel panel, String labelText, JTextField textField, 
                                int labelX, int fieldX, int yPos) {
        JLabel label = new JLabel(labelText);
        label.setBounds(labelX, yPos, 170, 25);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label);
        
        textField.setBounds(fieldX, yPos, 420, 30);
        panel.add(textField);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        styleTextField(textField);
        return textField;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // Action Handlers
    private void handleQuery() {
        String empNumber = tfEmpNum.getText().trim();
        
        if (empNumber.isEmpty()) {
            showModernDialog("Please enter an Employee Number", "Query", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Employee employee = employeeRepository.findByEmployeeNumber(empNumber);
        
        if (employee != null) {
            populateFields(employee);
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(empNumber)) {
                    employeeTable.setRowSelectionInterval(i, i);
                    employeeTable.scrollRectToVisible(employeeTable.getCellRect(i, 0, true));
                    break;
                }
            }
            
            updateButton.setEnabled(true);
            showModernDialog("Employee found!", "Query Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showModernDialog("Employee Number not found", "Query Failed", JOptionPane.ERROR_MESSAGE);
            clearFields();
        }
    }

    private void handleAdd() {
        try {
            Employee employee = createEmployeeFromFields();
            employeeRepository.addEmployee(employee);
            tableModel.addRow(employee.toTableRow());
            clearFields();
            showModernDialog("Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showModernDialog("Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Employee employee = createEmployeeFromFields();
                employeeRepository.updateEmployee(selectedRow, employee);
                
                Object[] rowData = employee.toTableRow();
                for (int i = 0; i < rowData.length; i++) {
                    tableModel.setValueAt(rowData[i], selectedRow, i);
                }
                
                clearFields();
                updateButton.setEnabled(false);
                showModernDialog("Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showModernDialog("Error updating employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                showModernDialog("Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showModernDialog("No employee selected to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSave() {
        employeeRepository.saveToCSV();
        showModernDialog("All data saved to CSV successfully!", "Save Success", JOptionPane.INFORMATION_MESSAGE);
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
        new ModernViewEmployeeDialog(this, employeeRepository, salaryCalculator).setVisible(true);
    }

    private void openLeaveApplicationDialog() {
        new ModernLeaveApplicationDialog(this).setVisible(true);
    }

    // NEW METHODS for Payroll Actions
    private void openWeeklyPayslipDialog() {
        new WeeklyPayslipDialog(this, employeeRepository, salaryCalculator).setVisible(true);
    }

    private void openViewDeductionsDialog() {
        new ViewDeductionsDialog(this, employeeRepository, salaryCalculator).setVisible(true);
    }

    private void showModernDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);    
    }
}