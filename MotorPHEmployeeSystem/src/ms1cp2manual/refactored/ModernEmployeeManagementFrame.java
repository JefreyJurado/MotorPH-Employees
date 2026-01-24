package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ModernEmployeeManagementFrame extends JFrame {
    private final EmployeeRepository employeeRepository;
    private final SalaryCalculator salaryCalculator;
    
    // Modern Color Scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JTextField tfEmpNum, tfLastName, tfFirstName, tfBDay, tfAdd;
    private JTextField tfPhone, tfSSS, tfPhealth, tfTin, tfPag;
    private JTextField tfPosition, tfStatus, tfSupervisor;
    private JTextField tfSalary, tfRice, tfPhoneAllow;
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
        setSize(1400, 950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(LIGHT_BG);
        
        createHeaderPanel(mainPanel);
        createEmployeeInfoPanel(mainPanel);
        createEmploymentDetailsPanel(mainPanel);
        createModernTable(mainPanel);
        
        setContentPane(mainPanel);
    }
    
    private void createHeaderPanel(JPanel parent) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1400, 80);
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("MotorPH Employee Management");
        titleLabel.setBounds(30, 15, 400, 50);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        int btnWidth = 90;
        int btnHeight = 35;
        int startX = 470;
        int spacing = 5;
        int yPos = 22;
        
        JButton queryBtn = createHeaderButton("Query", startX, yPos, btnWidth, btnHeight);
        queryBtn.addActionListener(e -> handleQuery());
        headerPanel.add(queryBtn);
        startX += btnWidth + spacing;
        
        JButton addBtn = createHeaderButton("Add", startX, yPos, btnWidth, btnHeight);
        addBtn.addActionListener(e -> handleAdd());
        headerPanel.add(addBtn);
        startX += btnWidth + spacing;
        
        updateButton = createHeaderButton("Update", startX, yPos, btnWidth, btnHeight);
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> handleUpdate());
        headerPanel.add(updateButton);
        startX += btnWidth + spacing;
        
        JButton deleteBtn = createHeaderButton("Delete", startX, yPos, btnWidth, btnHeight);
        deleteBtn.addActionListener(e -> handleDelete());
        headerPanel.add(deleteBtn);
        startX += btnWidth + spacing;
        
        JButton clearBtn = createHeaderButton("Clear", startX, yPos, btnWidth, btnHeight);
        clearBtn.addActionListener(e -> clearFields());
        headerPanel.add(clearBtn);
        startX += btnWidth + spacing;
        
        JButton saveBtn = createHeaderButton("Save", startX, yPos, btnWidth, btnHeight);
        saveBtn.addActionListener(e -> handleSave());
        headerPanel.add(saveBtn);
        startX += btnWidth + spacing;
        
        JButton viewBtn = createHeaderButton("View", startX, yPos, btnWidth, btnHeight);
        viewBtn.addActionListener(e -> openViewEmployeeDialog());
        headerPanel.add(viewBtn);
        startX += btnWidth + spacing;
        
        JButton leaveBtn = createHeaderButton("Leave", startX, yPos, btnWidth, btnHeight);
        leaveBtn.addActionListener(e -> openLeaveApplicationDialog());
        headerPanel.add(leaveBtn);
        
        parent.add(headerPanel);
    }

    private JButton createHeaderButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
        });
        
        return button;
    }

    private void createEmployeeInfoPanel(JPanel parent) {
        JPanel panel = createModernPanel("Personal Information", 30, 100, 650, 540);
        
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
            JPanel panel = createModernPanel("Employment Details", 700, 100, 650, 540);

            int yPos = 50;
            int labelX = 20;
            int fieldX = 200;
            int spacing = 45;

            addModernField(panel, "Position", tfPosition = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Status", tfStatus = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Immediate Supervisor", tfSupervisor = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Basic Salary", tfSalary = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Rice Subsidy", tfRice = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Phone Allowance", tfPhoneAllow = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Clothing Allowance", tfClothing = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Semi-Monthly Rate", tfSemiMo = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing;

            addModernField(panel, "Hourly Rate", tfHourlyR = createStyledTextField(), labelX, fieldX, yPos);
            yPos += spacing + 20;

            // UPDATED: Payroll Actions label and buttons on same line
            JLabel payrollLabel = new JLabel("Payroll Actions:");
            payrollLabel.setBounds(labelX, yPos + 7, 150, 25);  // +7 to align vertically with buttons
            payrollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            payrollLabel.setForeground(PRIMARY_COLOR);
            panel.add(payrollLabel);

            JButton weeklyPayslipBtn = createHeaderButton("Weekly Payslip", 170, yPos, 215, 35);
            weeklyPayslipBtn.addActionListener(e -> openWeeklyPayslipDialog());
            panel.add(weeklyPayslipBtn);

            JButton viewDeductionsBtn = createHeaderButton("View Deductions", 395, yPos, 215, 35);
            viewDeductionsBtn.addActionListener(e -> openViewDeductionsDialog());
            panel.add(viewDeductionsBtn);

            parent.add(panel);
        }

    private void createModernTable(JPanel parent) {
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(30, 650, 1320, 280);
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
            "TIN", "Pag-IBIG", "Status", "Position", "Supervisor", 
            "Salary", "Rice", "Phone Allow", "Clothing", "Semi-Monthly", "Hourly"};

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
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = employeeTable.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
        scrollPane.setPreferredSize(new Dimension(1300, 220));

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
                employeeRepository.saveToCSV();  // AUTO-SAVE
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

                    employeeRepository.saveToCSV();  // AUTO-SAVE
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
                    employeeRepository.saveToCSV();  // AUTO-SAVE
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
        tfSupervisor.setText(employee.getImmediateSupervisor());
        tfSalary.setText(String.valueOf(employee.getBasicSalary()));
        tfRice.setText(String.valueOf(employee.getRiceSubsidy()));
        tfPhoneAllow.setText(String.valueOf(employee.getPhoneAllowance()));
        tfClothing.setText(String.valueOf(employee.getClothingAllowance()));
        tfSemiMo.setText(String.valueOf(employee.getSemiMonthlyRate()));
        tfHourlyR.setText(String.valueOf(employee.getHourlyRate()));
    }

    private Employee createEmployeeFromFields() {
        return EmployeeFactory.createEmployee(
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
            tfStatus.getText(),
            tfPosition.getText(),
            tfSupervisor.getText(),
            Double.parseDouble(tfSalary.getText()),
            Double.parseDouble(tfRice.getText()),
            Double.parseDouble(tfPhoneAllow.getText()),
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
        tfSupervisor.setText("");
        tfSalary.setText("");
        tfRice.setText("");
        tfPhoneAllow.setText("");
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