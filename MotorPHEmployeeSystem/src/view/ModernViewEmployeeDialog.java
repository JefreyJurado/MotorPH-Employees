package view;

import model.User;
import model.Employee;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import repository.EmployeeRepository;
import service.SalaryCalculator;

public class ModernViewEmployeeDialog extends JDialog {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    private User currentUser;
    private JComboBox<String> employeeComboBox;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public ModernViewEmployeeDialog(Frame parent, EmployeeRepository repository, 
                                   SalaryCalculator calculator, User currentUser) {
        super(parent, "View Employee Details", true);
        this.employeeRepository = repository;
        this.salaryCalculator = calculator;
        this.currentUser = currentUser;
        initializeModernUI();
    }

    private void initializeModernUI() {
        setSize(700, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(700, 80));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(null);
        
        JLabel titleLabel = new JLabel("Salary Computation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setBounds(30, 20, 400, 40);
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Employee Selection Panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setBounds(20, 20, 640, 100);
        selectionPanel.setLayout(null);
        selectionPanel.setBackground(WHITE);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel empLabel = new JLabel("Select Employee:");
        empLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        empLabel.setForeground(DARK_COLOR);
        empLabel.setBounds(15, 15, 150, 25);
        selectionPanel.add(empLabel);
        
        // Create employee combo box
        employeeComboBox = new JComboBox<>();
        employeeComboBox.setBounds(15, 45, 300, 35);
        employeeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Load employees with filtering
        loadEmployees();
        
        selectionPanel.add(employeeComboBox);
        
        JLabel monthLabel = new JLabel("Select Month:");
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        monthLabel.setForeground(DARK_COLOR);
        monthLabel.setBounds(330, 15, 150, 25);
        selectionPanel.add(monthLabel);
        
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(330, 45, 200, 35);
        monthComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectionPanel.add(monthComboBox);
        
        JButton computeButton = createModernButton("Compute", SUCCESS_COLOR);
        computeButton.setBounds(540, 45, 80, 35);
        selectionPanel.add(computeButton);
        
        contentPanel.add(selectionPanel);
        
        // Results Panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBounds(20, 140, 640, 400);
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBackground(WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel resultsTitleLabel = new JLabel("Salary Details");
        resultsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsTitleLabel.setForeground(PRIMARY_COLOR);
        resultsTitleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        resultsPanel.add(resultsTitleLabel, BorderLayout.NORTH);
        
        JTextArea employeeDetailsArea = new JTextArea();
        employeeDetailsArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        employeeDetailsArea.setEditable(false);
        employeeDetailsArea.setBackground(new Color(250, 250, 250));
        employeeDetailsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        employeeDetailsArea.setText("Select an employee and click 'Compute' to view salary details.");
        
        JScrollPane scrollPane = new JScrollPane(employeeDetailsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(resultsPanel);
        
        // Compute button action
        computeButton.addActionListener(e -> {
            String selectedItem = (String) employeeComboBox.getSelectedItem();
            if (selectedItem != null) {
                String empNumber = selectedItem.split(" - ")[0];
                Employee employee = employeeRepository.findByEmployeeNumber(empNumber);
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                
                if (employee != null) {
                    // Build complete employee report with polymorphic methods
                    StringBuilder fullReport = new StringBuilder();
                    
                    // Employee Information Section
                    fullReport.append("╔════════════════════════════════════════════════════════════╗\n");
                    fullReport.append("║              EMPLOYEE INFORMATION                          ║\n");
                    fullReport.append("╚════════════════════════════════════════════════════════════╝\n\n");
                    
                    fullReport.append(String.format("Employee Number : %s\n", employee.getEmployeeNumber()));
                    fullReport.append(String.format("Full Name       : %s\n", employee.getFullName()));
                    fullReport.append(String.format("Position        : %s\n", employee.getPosition()));
                    fullReport.append(String.format("Department      : %s\n", employee.getDepartment()));
                    fullReport.append(String.format("Job Description : %s\n\n", employee.getJobDescription()));
                    
                    // Salary Computation Section
                    fullReport.append("╔════════════════════════════════════════════════════════════╗\n");
                    fullReport.append("║              SALARY COMPUTATION - ").append(selectedMonth).append("\n");
                    fullReport.append("╚════════════════════════════════════════════════════════════╝\n\n");
                    
                    String salaryReport = salaryCalculator.generateSalaryReport(employee, selectedMonth);
                    fullReport.append(salaryReport);
                    
                    employeeDetailsArea.setText(fullReport.toString());
                    employeeDetailsArea.setCaretPosition(0);
                }
            }
        });
        
        return contentPanel;
    }

    private void loadEmployees() {
        List<Employee> employees = employeeRepository.getAllEmployees();
        
        for (Employee employee : employees) {
            if (currentUser.isEmployee()) {
                // Employee can only see their own record
                if (currentUser.getEmployeeNumber() != null && 
                    employee.getEmployeeNumber().equals(currentUser.getEmployeeNumber())) {
                    employeeComboBox.addItem(employee.getEmployeeNumber() + " - " + employee.getFullName());
                }
            } else {
                // Admin/HR can see all
                employeeComboBox.addItem(employee.getEmployeeNumber() + " - " + employee.getFullName());
            }
        }
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
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