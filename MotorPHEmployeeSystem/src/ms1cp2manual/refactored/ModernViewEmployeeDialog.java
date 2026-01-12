package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernViewEmployeeDialog extends JDialog {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public ModernViewEmployeeDialog(JFrame parent, EmployeeRepository repository, 
                                   SalaryCalculator calculator) {
        super(parent, "View Employee Salary Details", true);
        this.employeeRepository = repository;
        this.salaryCalculator = calculator;
        initializeModernUI();
    }

    private void initializeModernUI() {
        setSize(700, 650);
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
        
        JLabel titleLabel = new JLabel("💰 Salary Computation");
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
        JComboBox<String> employeeComboBox = new JComboBox<>();
        employeeComboBox.setBounds(15, 45, 300, 35);
        employeeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Populate with employees
        for (Employee emp : employeeRepository.getAllEmployees()) {
            employeeComboBox.addItem(emp.getEmployeeNumber() + " - " + emp.getFullName());
        }
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
        
        JButton computeButton = createModernButton("🔢 Compute", SUCCESS_COLOR);
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
                    String report = salaryCalculator.generateSalaryReport(employee, selectedMonth);
                    employeeDetailsArea.setText(report);
                }
            }
        });
        
        return contentPanel;
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