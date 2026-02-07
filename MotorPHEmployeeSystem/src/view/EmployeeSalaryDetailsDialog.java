package view;

import model.Employee;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import service.SalaryCalculator;

public class EmployeeSalaryDetailsDialog extends JDialog {
    private final Employee employee;
    private final SalaryCalculator salaryCalculator;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public EmployeeSalaryDetailsDialog(Frame parent, Employee employee) {
        super(parent, "My Salary Details", true);
        this.employee = employee;
        this.salaryCalculator = new SalaryCalculator();
        initializeUI();
    }

    private void initializeUI() {
        setSize(700, 700);
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
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setPreferredSize(new Dimension(700, 80));
        headerPanel.setBackground(PRIMARY_COLOR);
        
        JLabel titleLabel = new JLabel("My Salary Computation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Employee Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(20, 20, 640, 100);
        infoPanel.setLayout(null);
        infoPanel.setBackground(WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel empInfoLabel = new JLabel("Employee Information");
        empInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        empInfoLabel.setForeground(PRIMARY_COLOR);
        empInfoLabel.setBounds(15, 10, 200, 25);
        infoPanel.add(empInfoLabel);
        
        JLabel empNumLabel = new JLabel("Employee #: " + employee.getEmployeeNumber());
        empNumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        empNumLabel.setForeground(DARK_COLOR);
        empNumLabel.setBounds(15, 40, 300, 25);
        infoPanel.add(empNumLabel);
        
        JLabel nameLabel = new JLabel("Name: " + employee.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(DARK_COLOR);
        nameLabel.setBounds(15, 65, 300, 25);
        infoPanel.add(nameLabel);
        
        JLabel positionLabel = new JLabel("Position: " + employee.getPosition());
        positionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        positionLabel.setForeground(DARK_COLOR);
        positionLabel.setBounds(330, 40, 300, 25);
        infoPanel.add(positionLabel);
        
        JLabel deptLabel = new JLabel("Department: " + employee.getDepartment());
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deptLabel.setForeground(DARK_COLOR);
        deptLabel.setBounds(330, 65, 300, 25);
        infoPanel.add(deptLabel);
        
        contentPanel.add(infoPanel);
        
        // Results Panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBounds(20, 140, 640, 440);
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBackground(WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Get current month name
        String currentMonth = LocalDate.now().getMonth()
            .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        
        JLabel resultsTitleLabel = new JLabel("Salary Computation - " + currentMonth);
        resultsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsTitleLabel.setForeground(PRIMARY_COLOR);
        resultsTitleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        resultsPanel.add(resultsTitleLabel, BorderLayout.NORTH);
        
        JTextArea salaryDetailsArea = new JTextArea();
        salaryDetailsArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        salaryDetailsArea.setEditable(false);
        salaryDetailsArea.setBackground(new Color(250, 250, 250));
        salaryDetailsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(salaryDetailsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(resultsPanel);
        
        // Auto-load salary details for current month - FIXED: Removed duplicate headers
        StringBuilder fullReport = new StringBuilder();
        
        // Employee Information
        fullReport.append("Employee Number : ").append(employee.getEmployeeNumber()).append("\n");
        fullReport.append("Full Name       : ").append(employee.getFullName()).append("\n");
        fullReport.append("Position        : ").append(employee.getPosition()).append("\n");
        fullReport.append("Department      : ").append(employee.getDepartment()).append("\n");
        fullReport.append("Job Description : ").append(employee.getJobDescription()).append("\n\n");

        fullReport.append("─────────────────────────────────────────────────────────────\n\n");

        // Salary Details
        fullReport.append("Employee Details\n");
        fullReport.append("----------------\n");
        fullReport.append("Name: ").append(employee.getFullName()).append("\n");
        fullReport.append("Position: ").append(employee.getPosition()).append("\n");
        fullReport.append("Basic Salary: ₱").append(String.format("%,.2f", employee.getBasicSalary())).append("\n");
        fullReport.append("Rice Subsidy: ₱").append(String.format("%,.2f", employee.getRiceSubsidy())).append("\n");
        fullReport.append("Clothing Allowance: ₱").append(String.format("%,.2f", employee.getClothingAllowance())).append("\n\n");

        fullReport.append("Selected Month: ").append(currentMonth).append("\n");

        double monthlySalary = salaryCalculator.calculateMonthlySalary(employee);
        fullReport.append("Total Monthly Salary: ₱").append(String.format("%,.2f", monthlySalary));

        salaryDetailsArea.setText(fullReport.toString());
        salaryDetailsArea.setCaretPosition(0);
        
        return contentPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton closeBtn = createStyledButton("Close", DANGER_COLOR);
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        
        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
}