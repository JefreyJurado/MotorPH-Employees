package ms1cp2manual.refactored;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;

public class WeeklyPayslipDialog extends JDialog {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private JComboBox<String> employeeComboBox;
    private JSpinner weekStartSpinner;
    private JSpinner weekEndSpinner;
    private JTextArea payslipArea;
    
    public WeeklyPayslipDialog(Frame parent, EmployeeRepository repository, SalaryCalculator calculator) {
        super(parent, "Generate Weekly Payslip", true);
        this.employeeRepository = repository;
        this.salaryCalculator = calculator;
        
        initializeUI();
        loadEmployees();
    }
    
    private void initializeUI() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(LIGHT_BG);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(900, 60));
        
        JLabel titleLabel = new JLabel("Generate Weekly Payslip");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder("Payslip Details")
        ));
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Employee Selection
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel empLabel = new JLabel("Select Employee:");
        empLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(empLabel, gbc);
        
        gbc.gridx = 1;
        employeeComboBox = new JComboBox<>();
        employeeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        employeeComboBox.setPreferredSize(new Dimension(400, 30));
        inputPanel.add(employeeComboBox, gbc);
        
        // Week Start Date
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel startLabel = new JLabel("Week Start Date:");
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(startLabel, gbc);
        
        gbc.gridx = 1;
        // Create date spinner for start date (default: 7 days ago)
        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.DAY_OF_MONTH, -7);
        SpinnerDateModel startModel = new SpinnerDateModel(startCal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        weekStartSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(weekStartSpinner, "yyyy-MM-dd");
        weekStartSpinner.setEditor(startEditor);
        weekStartSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weekStartSpinner.setPreferredSize(new Dimension(400, 30));
        inputPanel.add(weekStartSpinner, gbc);
        
        // Week End Date
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel endLabel = new JLabel("Week End Date:");
        endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(endLabel, gbc);
        
        gbc.gridx = 1;
        // Create date spinner for end date (default: today)
        SpinnerDateModel endModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        weekEndSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(weekEndSpinner, "yyyy-MM-dd");
        weekEndSpinner.setEditor(endEditor);
        weekEndSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weekEndSpinner.setPreferredSize(new Dimension(400, 30));
        inputPanel.add(weekEndSpinner, gbc);
        
        // Generate Button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JButton generateBtn = createStyledButton("Generate Payslip", PRIMARY_COLOR);
        generateBtn.addActionListener(e -> generatePayslip());
        inputPanel.add(generateBtn, gbc);
        
        add(inputPanel, BorderLayout.NORTH);
        
        // Payslip Display Area
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(WHITE);
        displayPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder("Weekly Payslip")
        ));
        
        payslipArea = new JTextArea();
        payslipArea.setEditable(false);
        payslipArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        payslipArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(payslipArea);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        displayPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(displayPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton printBtn = createStyledButton("Print Payslip", new Color(46, 204, 113));
        printBtn.addActionListener(e -> printPayslip());
        buttonPanel.add(printBtn);
        
        JButton closeBtn = createStyledButton("Close", new Color(231, 76, 60));
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadEmployees() {
        employeeComboBox.removeAllItems();
        for (Employee emp : employeeRepository.getAllEmployees()) {
            employeeComboBox.addItem(emp.getEmployeeNumber() + " - " + emp.getFullName());
        }
    }
    
    private void generatePayslip() {
        if (employeeComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an employee", 
                "No Employee Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedItem = (String) employeeComboBox.getSelectedItem();
        String empNumber = selectedItem.split(" - ")[0];
        Employee employee = employeeRepository.findByEmployeeNumber(empNumber);
        
        if (employee != null) {
            // Get dates from spinners
            Date startDate = (Date) weekStartSpinner.getValue();
            Date endDate = (Date) weekEndSpinner.getValue();
            
            // Convert Date to LocalDate
            LocalDate startLocalDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = endDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Validate date range
            if (startLocalDate.isAfter(endLocalDate)) {
                JOptionPane.showMessageDialog(this, 
                    "Start date must be before end date", 
                    "Invalid Date Range", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            SalaryCalculator.WeeklyPayslip payslip = 
                salaryCalculator.generateWeeklyPayslip(employee, startLocalDate, endLocalDate);
            
            payslipArea.setText(payslip.generatePayslipText());
            payslipArea.setCaretPosition(0);
        }
    }
    
    private void printPayslip() {
        if (payslipArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please generate a payslip first", 
                "No Payslip", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            boolean complete = payslipArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, 
                    "Payslip printed successfully!", 
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Printing was cancelled", 
                    "Print Cancelled", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error printing: " + ex.getMessage(), 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
}