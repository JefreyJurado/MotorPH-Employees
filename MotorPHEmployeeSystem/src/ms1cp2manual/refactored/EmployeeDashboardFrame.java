package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeDashboardFrame extends JFrame {
    private final EmployeeRepository employeeRepository;
    private final User currentUser;
    private final Employee currentEmployee;
    
    // Modern Color Scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color CARD_BG = Color.WHITE;
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JTextField addressField;
    private JTextField phoneField;
    private JLabel nameLabel;
    private JLabel positionLabel;
    private JLabel departmentLabel;
    private JLabel employeeNumLabel;
    private DefaultTableModel leaveTableModel;
    
    public EmployeeDashboardFrame(EmployeeRepository repository, User user) {
        this.employeeRepository = repository;
        this.currentUser = user;
        this.currentEmployee = repository.findByEmployeeNumber(user.getEmployeeNumber());
        
        initializeUI();
        loadEmployeeData();
    }
    
    private void initializeUI() {
        setTitle("Employee Dashboard - " + currentUser.getUsername());
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content (Scrollable)
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Left: Welcome message
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + (currentEmployee != null ? currentEmployee.getFullName() : currentUser.getUsername()) + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right: Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JButton logoutBtn = createHeaderButton("Logout", DANGER_COLOR);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });
        rightPanel.add(logoutBtn);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setPreferredSize(new Dimension(1150, 1700)); // INCREASED from 1400 to 1700
        
        int yPos = 20;
        
        // Row 1: Profile Card + Quick Actions
        JPanel profileCard = createProfileCard();
        profileCard.setBounds(20, yPos, 380, 280);
        contentPanel.add(profileCard);
        
        JPanel quickActionsCard = createQuickActionsCard();
        quickActionsCard.setBounds(420, yPos, 730, 280);
        contentPanel.add(quickActionsCard);
        
        yPos += 300;
        
        // Row 2: Editable Information
        JPanel editableInfoCard = createEditableInfoCard();
        editableInfoCard.setBounds(20, yPos, 1130, 180);
        contentPanel.add(editableInfoCard);
        
        yPos += 200;
        
        // Row 3: Employment Details (Read-only)
        JPanel employmentCard = createEmploymentDetailsCard();
        employmentCard.setBounds(20, yPos, 1130, 250); // INCREASED from 220 to 250
        contentPanel.add(employmentCard);
        
        yPos += 270; // INCREASED from 240 to 270
        
        // Row 4: Leave History
        JPanel leaveHistoryCard = createLeaveHistoryCard();
        leaveHistoryCard.setBounds(20, yPos, 1130, 400);
        contentPanel.add(leaveHistoryCard);
        
        return contentPanel;
    }
    
    private JPanel createProfileCard() {
        JPanel card = createCard("My Profile");
        card.setLayout(null);
        
        int yPos = 60;
        
        // Avatar placeholder
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBounds(120, yPos, 100, 100);
        avatarPanel.setBackground(PRIMARY_COLOR);
        avatarPanel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 3));
        
        JLabel avatarLabel = new JLabel("👤", SwingConstants.CENTER);
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        avatarLabel.setForeground(Color.WHITE);
        avatarPanel.add(avatarLabel);
        
        card.add(avatarPanel);
        yPos += 110;
        
        // Employee info
        employeeNumLabel = new JLabel();
        employeeNumLabel.setBounds(20, yPos, 340, 25);
        employeeNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        employeeNumLabel.setForeground(TEXT_COLOR);
        employeeNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(employeeNumLabel);
        yPos += 30;
        
        nameLabel = new JLabel();
        nameLabel.setBounds(20, yPos, 340, 25);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel);
        yPos += 25;
        
        positionLabel = new JLabel();
        positionLabel.setBounds(20, yPos, 340, 25);
        positionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        positionLabel.setForeground(TEXT_COLOR);
        positionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(positionLabel);
        yPos += 25;
        
        departmentLabel = new JLabel();
        departmentLabel.setBounds(20, yPos, 340, 25);
        departmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        departmentLabel.setForeground(TEXT_COLOR);
        departmentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(departmentLabel);
        
        return card;
    }
    
    private JPanel createQuickActionsCard() {
        JPanel card = createCard("Quick Actions");
        card.setLayout(new GridLayout(2, 2, 15, 15));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(60, 20, 20, 20)
        ));
        
        // Action buttons
        JButton leaveBtn = createActionButton("📅 File Leave", "Request time off", SUCCESS_COLOR);
        leaveBtn.addActionListener(e -> openLeaveApplicationDialog());
        card.add(leaveBtn);
        
        JButton payslipBtn = createActionButton("💰 My Payslip", "View weekly payslip", INFO_COLOR);
        payslipBtn.addActionListener(e -> openPayslipDialog());
        card.add(payslipBtn);
        
        JButton deductionsBtn = createActionButton("📊 Deductions", "View monthly deductions", WARNING_COLOR);
        deductionsBtn.addActionListener(e -> openDeductionsDialog());
        card.add(deductionsBtn);
        
        JButton salaryBtn = createActionButton("💵 Salary Details", "View salary computation", new Color(155, 89, 182));
        salaryBtn.addActionListener(e -> openSalaryDetailsDialog());
        card.add(salaryBtn);
        
        return card;
    }
    
    private JPanel createEditableInfoCard() {
        JPanel card = createCard("Personal Information (You can update these)");
        card.setLayout(null);
        
        int yPos = 60;
        int labelX = 30;
        int fieldX = 200;
        int btnX = 680;
        
        // Address
        JLabel addressLabel = new JLabel("📍 Address:");
        addressLabel.setBounds(labelX, yPos, 150, 30);
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addressLabel.setForeground(TEXT_COLOR);
        card.add(addressLabel);
        
        addressField = new JTextField();
        addressField.setBounds(fieldX, yPos, 450, 35);
        styleTextField(addressField);
        card.add(addressField);
        
        JButton updateAddressBtn = createSmallButton("Update", SUCCESS_COLOR);
        updateAddressBtn.setBounds(btnX, yPos, 100, 35);
        updateAddressBtn.addActionListener(e -> updateAddress());
        card.add(updateAddressBtn);
        
        yPos += 50;
        
        // Phone
        JLabel phoneLabel = new JLabel("📞 Phone:");
        phoneLabel.setBounds(labelX, yPos, 150, 30);
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        phoneLabel.setForeground(TEXT_COLOR);
        card.add(phoneLabel);
        
        phoneField = new JTextField();
        phoneField.setBounds(fieldX, yPos, 450, 35);
        styleTextField(phoneField);
        card.add(phoneField);
        
        JButton updatePhoneBtn = createSmallButton("Update", SUCCESS_COLOR);
        updatePhoneBtn.setBounds(btnX, yPos, 100, 35);
        updatePhoneBtn.addActionListener(e -> updatePhone());
        card.add(updatePhoneBtn);
        
        return card;
    }
    
    private JPanel createEmploymentDetailsCard() {
        JPanel card = createCard("Employment Details (Read-only)");
        card.setLayout(new GridLayout(3, 4, 15, 15));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(60, 20, 20, 20)
        ));
        
        if (currentEmployee != null) {
            addInfoLabel(card, "Status", currentEmployee.getStatus());
            addInfoLabel(card, "Supervisor", currentEmployee.getImmediateSupervisor());
            addInfoLabel(card, "Basic Salary", String.format("₱%,.2f", currentEmployee.getBasicSalary()));
            addInfoLabel(card, "Rice Subsidy", String.format("₱%,.2f", currentEmployee.getRiceSubsidy()));
            addInfoLabel(card, "Phone Allowance", String.format("₱%,.2f", currentEmployee.getPhoneAllowance()));
            addInfoLabel(card, "Clothing Allowance", String.format("₱%,.2f", currentEmployee.getClothingAllowance()));
            addInfoLabel(card, "Semi-Monthly Rate", String.format("₱%,.2f", currentEmployee.getSemiMonthlyRate()));
            addInfoLabel(card, "Hourly Rate", String.format("₱%,.2f", currentEmployee.getHourlyRate()));
        }
        
        return card;
    }
    
    private JPanel createLeaveHistoryCard() {
        JPanel card = createCard("My Leave Applications");
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(60, 20, 20, 20)
        ));
        
        String[] columnNames = {"Leave ID", "Type", "Start Date", "End Date", "Reason", "Status", "Submitted", "Approved By"};
        leaveTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable leaveTable = new JTable(leaveTableModel);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leaveTable.setRowHeight(30);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setOpaque(true);
        
        for (int i = 0; i < leaveTable.getColumnModel().getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaveTable.getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        card.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_BG);
        
        JButton refreshBtn = createSmallButton("Refresh", INFO_COLOR);
        refreshBtn.addActionListener(e -> loadLeaveHistory());
        buttonPanel.add(refreshBtn);
        
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void loadEmployeeData() {
        if (currentEmployee != null) {
            employeeNumLabel.setText("Employee #" + currentEmployee.getEmployeeNumber());
            nameLabel.setText("👤 " + currentEmployee.getFullName());
            positionLabel.setText("📋 " + currentEmployee.getPosition());
            departmentLabel.setText("🏢 " + currentEmployee.getDepartment());
            
            addressField.setText(currentEmployee.getAddress());
            phoneField.setText(currentEmployee.getPhoneNumber());
            
            loadLeaveHistory();
        }
    }
    
    // FIXED: Use employee NUMBER instead of name
    private void loadLeaveHistory() {
        leaveTableModel.setRowCount(0);
        
        LeaveRepository leaveRepo = new LeaveRepository();
        // CHANGED: Use getLeavesByEmployeeNumber instead of getLeavesByEmployee
        List<LeaveApplication> leaves = leaveRepo.getLeavesByEmployeeNumber(currentEmployee.getEmployeeNumber());
        
        for (LeaveApplication leave : leaves) {
            leaveTableModel.addRow(new Object[]{
                leave.getLeaveId(),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus(),
                leave.getSubmittedDate(),
                leave.getApprovedBy().isEmpty() ? "N/A" : leave.getApprovedBy()
            });
        }
    }
    
    private void updateAddress() {
        String newAddress = addressField.getText().trim();
        
        if (newAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Address cannot be empty",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentEmployee.setAddress(newAddress);
        
        // Find index and update in repository
        List<Employee> allEmployees = employeeRepository.getAllEmployees();
        for (int i = 0; i < allEmployees.size(); i++) {
            if (allEmployees.get(i).getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
                employeeRepository.updateEmployee(i, currentEmployee);
                employeeRepository.saveToCSV();
                break;
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "Address updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updatePhone() {
        String newPhone = phoneField.getText().trim();
        
        if (newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Phone number cannot be empty",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentEmployee.setPhoneNumber(newPhone);
        
        // Find index and update in repository
        List<Employee> allEmployees = employeeRepository.getAllEmployees();
        for (int i = 0; i < allEmployees.size(); i++) {
            if (allEmployees.get(i).getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
                employeeRepository.updateEmployee(i, currentEmployee);
                employeeRepository.saveToCSV();
                break;
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "Phone number updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FIXED: Pass currentEmployee to the dialog
    private void openLeaveApplicationDialog() {
        new ModernLeaveApplicationDialog(this, currentEmployee).setVisible(true);
        loadLeaveHistory(); // Refresh after closing
    }
    
    private void openPayslipDialog() {
        new EmployeeViewPayslipsDialog(this, currentEmployee).setVisible(true);
    }
    
    private void openDeductionsDialog() {
        SalaryCalculator calculator = new SalaryCalculator();
        new ViewDeductionsDialog(this, employeeRepository, calculator, currentUser).setVisible(true);
    }
    
    private void openSalaryDetailsDialog() {
        new EmployeeSalaryDetailsDialog(this, currentEmployee).setVisible(true);
    }
    
    // UI Helper Methods
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBounds(15, 10, 400, 30);
        card.add(titleLabel);
        
        return card;
    }
    
    private JButton createHeaderButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
    
    private JButton createActionButton(String title, String subtitle, Color bgColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 5));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.WHITE);
        
        button.add(titleLabel, BorderLayout.CENTER);
        button.add(subtitleLabel, BorderLayout.SOUTH);
        
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
    
    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
    
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void addInfoLabel(JPanel panel, String label, String value) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(5, 5));
        infoPanel.setBackground(CARD_BG);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelText.setForeground(new Color(127, 140, 141));
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueText.setForeground(TEXT_COLOR);
        
        infoPanel.add(labelText, BorderLayout.NORTH);
        infoPanel.add(valueText, BorderLayout.CENTER);
        
        panel.add(infoPanel);
    }
}