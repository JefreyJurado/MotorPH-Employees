package view;

import model.LeaveApplication;
import model.User;
import model.Employee;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import repository.EmployeeRepository;
import repository.LeaveRepository;
import repository.AttendanceRepository;
import service.SalaryCalculator;
import service.IHROperations;
import service.IFinanceOperations;
import service.IITOperations;
import service.IAccountingOperations;
import service.IExecutiveOperations;

public class EmployeeDashboardFrame extends JFrame {
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final User currentUser;
    private final Employee currentEmployee;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    
    private JTextArea addressField;
    private JTextField phoneField;
    private DefaultTableModel leaveTableModel;
    
    public EmployeeDashboardFrame(EmployeeRepository repository, User user) {
        this.employeeRepository = repository;
        this.attendanceRepository = new AttendanceRepository();
        this.currentUser = user;
        this.currentEmployee = repository.findByEmployeeNumber(user.getEmployeeNumber());
        
        initializeUI();
        loadEmployeeData();
    }
    
    private void initializeUI() {
        setTitle("Employee Dashboard");
        setSize(1400, 900);
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
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
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
        headerPanel.setPreferredSize(new Dimension(1400, 100));
        headerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + 
            (currentEmployee != null ? currentEmployee.getFullName() : currentUser.getUsername()));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutBtn.setBackground(WHITE);
        logoutBtn.setForeground(PRIMARY_COLOR);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 45));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel topSection = createTopSection();
        contentPanel.add(topSection, BorderLayout.NORTH);
        
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BorderLayout(0, 20));
        centerWrapper.setBackground(LIGHT_BG);
        
        JPanel quickActionsPanel = createQuickActionsPanel();
        centerWrapper.add(quickActionsPanel, BorderLayout.NORTH);
        
        JPanel leaveTablePanel = createLeaveTablePanel();
        centerWrapper.add(leaveTablePanel, BorderLayout.CENTER);
        
        contentPanel.add(centerWrapper, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createTopSection() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        topPanel.setBackground(WHITE);
        topPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        topPanel.setPreferredSize(new Dimension(1360, 280));

        // Left: Profile box
        JPanel profileBox = new JPanel();
        profileBox.setLayout(new GridBagLayout());
        profileBox.setBackground(PRIMARY_COLOR);
        profileBox.setPreferredSize(new Dimension(300, 280));
        
        JLabel profileLabel = new JLabel("Profile");
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        profileLabel.setForeground(WHITE);
        profileBox.add(profileLabel);
        
        topPanel.add(profileBox, BorderLayout.WEST);
        
        // Center: Employee info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(WHITE);
        infoPanel.setBorder(new EmptyBorder(0, 40, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        if (currentEmployee != null) {
            JLabel nameLabel = new JLabel(currentEmployee.getFullName().toUpperCase());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            nameLabel.setForeground(Color.BLACK);
            infoPanel.add(nameLabel, gbc);
            
            gbc.gridy++;
            gbc.insets = new Insets(5, 0, 0, 0);
            JLabel positionLabel = new JLabel(currentEmployee.getPosition());
            positionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            positionLabel.setForeground(TEXT_COLOR);
            infoPanel.add(positionLabel, gbc);
            
            gbc.gridy++;
            JLabel departmentLabel = new JLabel(currentEmployee.getDepartment());
            departmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            departmentLabel.setForeground(TEXT_COLOR);
            infoPanel.add(departmentLabel, gbc);
        }
        
        topPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new GridBagLayout());
        updatePanel.setBackground(WHITE);
        updatePanel.setPreferredSize(new Dimension(550, 280));
        updatePanel.setBorder(new EmptyBorder(20, 20, 20, 40));
        
        GridBagConstraints ugbc = new GridBagConstraints();
        ugbc.fill = GridBagConstraints.HORIZONTAL;
        ugbc.weightx = 1.0;
        ugbc.gridx = 0;
        
        // Address label
        ugbc.gridy = 0;
        ugbc.insets = new Insets(0, 0, 5, 0);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        addressLabel.setForeground(Color.BLACK);
        updatePanel.add(addressLabel, ugbc);
        
        // Address field (Large box)
        ugbc.gridy = 1;
        ugbc.weighty = 1.0; 
        ugbc.fill = GridBagConstraints.BOTH;
        addressField = new JTextArea();
        addressField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressField);
        addressScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        updatePanel.add(addressScroll, ugbc);
        
        // Phone label
        ugbc.gridy = 2;
        ugbc.weighty = 0;
        ugbc.fill = GridBagConstraints.HORIZONTAL;
        ugbc.insets = new Insets(10, 0, 5, 0);
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        phoneLabel.setForeground(Color.BLACK);
        updatePanel.add(phoneLabel, ugbc);
        
        // Phone field
        ugbc.gridy = 3;
        phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(0, 35));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        updatePanel.add(phoneField, ugbc);
        
        // Update button
        ugbc.gridy = 4;
        ugbc.insets = new Insets(15, 0, 0, 0);
        JButton updateBtn = new JButton("Update Phone and Address");
        updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        updateBtn.setBackground(PRIMARY_COLOR);
        updateBtn.setForeground(WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setPreferredSize(new Dimension(0, 38));
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> updatePhoneAndAddress());
        updatePanel.add(updateBtn, ugbc);
        
        topPanel.add(updatePanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createQuickActionsPanel() {
        // Count how many buttons needed
        int buttonCount = 5; // Standard buttons (Leave, Payslip, Salary, Deductions, My Attendance)
        
        // Check what role-specific buttons to add
        boolean hasHRAccess = currentEmployee instanceof IHROperations;
        boolean hasFinanceAccess = currentEmployee instanceof IFinanceOperations;
        boolean hasITAccess = currentEmployee instanceof IITOperations;
        boolean hasAccountingAccess = currentEmployee instanceof IAccountingOperations;
        boolean hasExecutiveAccess = currentEmployee instanceof IExecutiveOperations;
        
        if (hasHRAccess) buttonCount++;
        if (hasFinanceAccess) buttonCount++;
        if (hasITAccess) buttonCount++;
        if (hasAccountingAccess) buttonCount++;
        if (hasExecutiveAccess) buttonCount++;
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, buttonCount, 20, 0));
        panel.setBackground(LIGHT_BG);
        panel.setPreferredSize(new Dimension(1360, 110));
        
        // STANDARD BUTTONS - All employees get these
        JButton leaveBtn = createActionButton("File Leave", "Request time off");
        leaveBtn.addActionListener(e -> openLeaveApplicationDialog());
        panel.add(leaveBtn);
        
        JButton payslipBtn = createActionButton("My Payslip", "View weekly payslip");
        payslipBtn.addActionListener(e -> openPayslipDialog());
        panel.add(payslipBtn);
        
        JButton salaryBtn = createActionButton("Salary Details", "View salary computation");
        salaryBtn.addActionListener(e -> openSalaryDetailsDialog());
        panel.add(salaryBtn);
        
        JButton deductionsBtn = createActionButton("Deductions", "View monthly deductions");
        deductionsBtn.addActionListener(e -> openDeductionsDialog());
        panel.add(deductionsBtn);
        
        // MY ATTENDANCE BUTTON - ALL EMPLOYEES GET THIS
        JButton attendanceBtn = createActionButton("My Attendance", "View attendance records");
        attendanceBtn.setBackground(new Color(156, 89, 182)); // Purple color
        attendanceBtn.addActionListener(e -> openMyAttendance());
        panel.add(attendanceBtn);
        
        // ROLE-SPECIFIC BUTTONS - Only certain employees get these
        if (hasHRAccess) {
            JButton hrBtn = createActionButton("HR Operations", "Manage employees & leave");
            hrBtn.setBackground(new Color(46, 204, 113)); // Green
            hrBtn.addActionListener(e -> openHROperationsDialog());
            panel.add(hrBtn);
        }
        
        if (hasFinanceAccess) {
            JButton financeBtn = createActionButton("Finance Operations", "Payroll & reports");
            financeBtn.setBackground(new Color(155, 89, 182)); // Purple
            financeBtn.addActionListener(e -> openFinanceOperationsDialog());
            panel.add(financeBtn);
        }
        
        if (hasITAccess) {
            JButton itBtn = createActionButton("IT Operations", "System management");
            itBtn.setBackground(new Color(52, 73, 94)); // Dark blue
            itBtn.addActionListener(e -> openITOperationsDialog());
            panel.add(itBtn);
        }
        
        if (hasAccountingAccess) {
            JButton accountingBtn = createActionButton("Accounting Operations", "Financial records");
            accountingBtn.setBackground(new Color(230, 126, 34)); // Orange
            accountingBtn.addActionListener(e -> openAccountingOperationsDialog());
            panel.add(accountingBtn);
        }
        
        if (hasExecutiveAccess) {
            JButton executiveBtn = createActionButton("Executive Operations", "Strategic decisions");
            executiveBtn.setBackground(new Color(231, 76, 60)); // Red
            executiveBtn.addActionListener(e -> openExecutiveOperationsDialog());
            panel.add(executiveBtn);
        }
        
        return panel;
    }
    
    private JButton createActionButton(String title, String subtitle) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 10, 3, 10));
        
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(WHITE);
        subtitleLabel.setBorder(new EmptyBorder(0, 10, 20, 10));
        
        button.add(titleLabel, BorderLayout.CENTER);
        button.add(subtitleLabel, BorderLayout.SOUTH);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private JPanel createLeaveTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 15));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("My Leave Applications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(PRIMARY_COLOR);
        refreshBtn.setForeground(WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setPreferredSize(new Dimension(100, 32));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadLeaveHistory());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"Leave ID", "Type", "Start Date", "End Date", "Reason", "Submitted", "Status", "Approved by"};
        leaveTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable leaveTable = new JTable(leaveTableModel);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leaveTable.setRowHeight(35);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setGridColor(BORDER_COLOR);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setOpaque(true);
        
        for (int i = 0; i < leaveTable.getColumnModel().getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaveTable.getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadEmployeeData() {
        if (currentEmployee != null) {
            addressField.setText(currentEmployee.getAddress());
            phoneField.setText(currentEmployee.getPhoneNumber());
            loadLeaveHistory();
        }
    }
    
    private void loadLeaveHistory() {
        leaveTableModel.setRowCount(0);
        LeaveRepository leaveRepo = new LeaveRepository();
        List<LeaveApplication> leaves = leaveRepo.getLeavesByEmployeeNumber(currentEmployee.getEmployeeNumber());
        
        for (LeaveApplication leave : leaves) {
            leaveTableModel.addRow(new Object[]{
                leave.getLeaveId(),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getSubmittedDate(),
                leave.getStatus(),
                leave.getApprovedBy().isEmpty() ? "N/A" : leave.getApprovedBy()
            });
        }
    }
    
    private void updatePhoneAndAddress() {
        String newAddress = addressField.getText().trim();
        String newPhone = phoneField.getText().trim();
        
        if (newAddress.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Address and phone number cannot be empty",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentEmployee.setAddress(newAddress);
        currentEmployee.setPhoneNumber(newPhone);
        
        List<Employee> allEmployees = employeeRepository.getAllEmployees();
        for (int i = 0; i < allEmployees.size(); i++) {
            if (allEmployees.get(i).getEmployeeNumber().equals(currentEmployee.getEmployeeNumber())) {
                employeeRepository.updateEmployee(i, currentEmployee);
                employeeRepository.saveToCSV();
                break;
            }
        }
        
        JOptionPane.showMessageDialog(this,
            "Phone number and address updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openLeaveApplicationDialog() {
        new ModernLeaveApplicationDialog(this, currentEmployee).setVisible(true);
        loadLeaveHistory();
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
    
    // NEW METHOD - MY ATTENDANCE
    private void openMyAttendance() {
        new EmployeeAttendanceDialog(this, currentEmployee, attendanceRepository).setVisible(true);
    }
    
    //ROLE-SPECIFIC OPERATION DIALOGS
    
    private void openHROperationsDialog() {
        if (currentEmployee instanceof IHROperations) {
            IHROperations hrOps = (IHROperations) currentEmployee;

            JDialog dialog = new JDialog(this, "HR Operations", true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(6, 1, 10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(WHITE);

            JLabel titleLabel = new JLabel("HR Operations Panel", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(46, 204, 113));

            JButton generateReportBtn = createOperationButton("Generate Employee Report");
            generateReportBtn.addActionListener(e -> {
                hrOps.generateEmployeeReport();
                JOptionPane.showMessageDialog(dialog, "Employee report generated successfully!");
            });

            JButton approveLeaveBtn = createOperationButton("Approve Leave Requests");
            approveLeaveBtn.addActionListener(e -> {
                openApproveLeaveDialog();
                dialog.dispose();
            });

            JButton manageEmployeesBtn = createOperationButton("Manage Employees");
            manageEmployeesBtn.addActionListener(e -> {
                dialog.dispose(); // Close the HR Operations dialog
                dispose(); // Close the current dashboard
                // Open the Management Frame
                new ModernEmployeeManagementFrame(employeeRepository, currentUser).setVisible(true);
            });

            JButton closeBtn = createOperationButton("Close");
            closeBtn.setBackground(new Color(231, 76, 60));
            closeBtn.addActionListener(e -> dialog.dispose());

            contentPanel.add(titleLabel);
            contentPanel.add(generateReportBtn);
            contentPanel.add(approveLeaveBtn);
            contentPanel.add(manageEmployeesBtn);
            contentPanel.add(new JLabel()); // Spacer
            contentPanel.add(closeBtn);

            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    }
    
    private void openFinanceOperationsDialog() {
        if (currentEmployee instanceof IFinanceOperations) {
            IFinanceOperations financeOps = (IFinanceOperations) currentEmployee;
            
            JDialog dialog = new JDialog(this, "Finance Operations", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(7, 1, 10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(WHITE);
            
            JLabel titleLabel = new JLabel("Finance Operations Panel", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(155, 89, 182));
            
            JButton processPayrollBtn = createOperationButton("Process Payroll");
            processPayrollBtn.addActionListener(e -> {
                String period = JOptionPane.showInputDialog(dialog, "Enter payroll period (e.g., February 2025):");
                if (period != null && !period.trim().isEmpty()) {
                    financeOps.processPayroll(period);
                    JOptionPane.showMessageDialog(dialog, "Payroll processed for: " + period);
                }
            });
            
            JButton generateFinancialReportBtn = createOperationButton("Generate Financial Report");
            generateFinancialReportBtn.addActionListener(e -> {
                financeOps.generateFinancialReport();
                JOptionPane.showMessageDialog(dialog, "Financial report generated successfully!");
            });
            
            JButton calculateNetPayBtn = createOperationButton("Calculate Net Pay");
            calculateNetPayBtn.addActionListener(e -> {
                double netPay = financeOps.calculateNetPay(currentEmployee);
                JOptionPane.showMessageDialog(dialog, 
                    "Net Pay Calculated:\n" +
                    "Employee: " + currentEmployee.getFullName() + "\n" +
                    "Net Pay: ₱" + String.format("%.2f", netPay),
                    "Net Pay Calculation", JOptionPane.INFORMATION_MESSAGE);
            });
            
            JButton viewDeductionsBtn = createOperationButton("View Deductions Breakdown");
            viewDeductionsBtn.addActionListener(e -> {
                double deductions = financeOps.calculateTotalDeductions(currentEmployee);
                JOptionPane.showMessageDialog(dialog, 
                    "Total Deductions: ₱" + String.format("%.2f", deductions),
                    "Deductions", JOptionPane.INFORMATION_MESSAGE);
            });
            
            JButton closeBtn = createOperationButton("Close");
            closeBtn.setBackground(new Color(231, 76, 60));
            closeBtn.addActionListener(e -> dialog.dispose());
            
            contentPanel.add(titleLabel);
            contentPanel.add(processPayrollBtn);
            contentPanel.add(generateFinancialReportBtn);
            contentPanel.add(calculateNetPayBtn);
            contentPanel.add(viewDeductionsBtn);
            contentPanel.add(new JLabel()); // Spacer
            contentPanel.add(closeBtn);
            
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    }
    
    private void openITOperationsDialog() {
        if (currentEmployee instanceof IITOperations) {
            IITOperations itOps = (IITOperations) currentEmployee;
            
            JDialog dialog = new JDialog(this, "IT Operations", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(7, 1, 10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(WHITE);
            
            JLabel titleLabel = new JLabel("IT Operations Panel", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(52, 73, 94));
            
            JButton securityBtn = createOperationButton("Manage System Security");
            securityBtn.addActionListener(e -> {
                itOps.manageSystemSecurity();
                JOptionPane.showMessageDialog(dialog, "System security settings updated!");
            });
            
            JButton maintenanceBtn = createOperationButton("Perform System Maintenance");
            maintenanceBtn.addActionListener(e -> {
                itOps.performSystemMaintenance();
                JOptionPane.showMessageDialog(dialog, "System maintenance completed!");
            });
            
            JButton backupBtn = createOperationButton("Backup Database");
            backupBtn.addActionListener(e -> {
                itOps.backupDatabase();
                JOptionPane.showMessageDialog(dialog, "Database backup completed successfully!");
            });
            
            JButton monitorBtn = createOperationButton("Monitor System Health");
            monitorBtn.addActionListener(e -> {
                itOps.monitorSystemHealth();
                JOptionPane.showMessageDialog(dialog, 
                    "System Health Status:\n" +
                    "CPU Usage: 45%\n" +
                    "Memory: 62%\n" +
                    "Disk Space: 78%\n" +
                    "Status: HEALTHY",
                    "System Health", JOptionPane.INFORMATION_MESSAGE);
            });
            
            JButton configBtn = createOperationButton("Configure System Settings");
            configBtn.addActionListener(e -> {
                itOps.configureSystemSettings();
                JOptionPane.showMessageDialog(dialog, "System settings configured!");
            });
            
            JButton closeBtn = createOperationButton("Close");
            closeBtn.setBackground(new Color(231, 76, 60));
            closeBtn.addActionListener(e -> dialog.dispose());
            
            contentPanel.add(titleLabel);
            contentPanel.add(securityBtn);
            contentPanel.add(maintenanceBtn);
            contentPanel.add(backupBtn);
            contentPanel.add(monitorBtn);
            contentPanel.add(configBtn);
            contentPanel.add(closeBtn);
            
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    }
    
    private void openAccountingOperationsDialog() {
        if (currentEmployee instanceof IAccountingOperations) {
            IAccountingOperations accountingOps = (IAccountingOperations) currentEmployee;
            
            JDialog dialog = new JDialog(this, "Accounting Operations", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(7, 1, 10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(WHITE);
            
            JLabel titleLabel = new JLabel("Accounting Operations Panel", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(230, 126, 34));
            
            JButton recordTransactionBtn = createOperationButton("Record Transaction");
            recordTransactionBtn.addActionListener(e -> {
                String[] options = {"INCOME", "EXPENSE"};
                String type = (String) JOptionPane.showInputDialog(dialog, "Select transaction type:",
                    "Record Transaction", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (type != null) {
                    String amountStr = JOptionPane.showInputDialog(dialog, "Enter amount:");
                    if (amountStr != null) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            accountingOps.recordTransaction(type, amount);
                            JOptionPane.showMessageDialog(dialog, type + " of ₱" + amount + " recorded!");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            JButton reconcileBtn = createOperationButton("Reconcile Accounts");
            reconcileBtn.addActionListener(e -> {
                accountingOps.reconcileAccounts();
                JOptionPane.showMessageDialog(dialog, "Accounts reconciled successfully!");
            });
            
            JButton taxReportBtn = createOperationButton("Generate Tax Report");
            taxReportBtn.addActionListener(e -> {
                accountingOps.generateTaxReport();
                JOptionPane.showMessageDialog(dialog, "Tax report generated!");
            });
            
            JButton payableBtn = createOperationButton("Manage Accounts Payable");
            payableBtn.addActionListener(e -> {
                accountingOps.manageAccountsPayable();
                JOptionPane.showMessageDialog(dialog, "Accounts payable managed!");
            });
            
            JButton receivableBtn = createOperationButton("Manage Accounts Receivable");
            receivableBtn.addActionListener(e -> {
                accountingOps.manageAccountsReceivable();
                JOptionPane.showMessageDialog(dialog, "Accounts receivable managed!");
            });
            
            JButton closeBtn = createOperationButton("Close");
            closeBtn.setBackground(new Color(231, 76, 60));
            closeBtn.addActionListener(e -> dialog.dispose());
            
            contentPanel.add(titleLabel);
            contentPanel.add(recordTransactionBtn);
            contentPanel.add(reconcileBtn);
            contentPanel.add(taxReportBtn);
            contentPanel.add(payableBtn);
            contentPanel.add(receivableBtn);
            contentPanel.add(closeBtn);
            
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    }
    
    private void openExecutiveOperationsDialog() {
        if (currentEmployee instanceof IExecutiveOperations) {
            IExecutiveOperations execOps = (IExecutiveOperations) currentEmployee;
            
            JDialog dialog = new JDialog(this, "Executive Operations", true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(7, 1, 10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(WHITE);
            
            JLabel titleLabel = new JLabel("Executive Operations Panel", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(new Color(231, 76, 60));
            
            JButton strategicBtn = createOperationButton("Approve Strategic Decisions");
            strategicBtn.addActionListener(e -> {
                execOps.approveStrategicDecisions();
                JOptionPane.showMessageDialog(dialog, "Strategic decision approved!");
            });
            
            JButton reportsBtn = createOperationButton("View All Department Reports");
            reportsBtn.addActionListener(e -> {
                execOps.viewAllDepartmentReports();
                JOptionPane.showMessageDialog(dialog, "Department reports loaded!");
            });
            
            JButton financialBtn = createOperationButton("Access Financial Data");
            financialBtn.addActionListener(e -> {
                execOps.accessFinancialData();
                JOptionPane.showMessageDialog(dialog, "Financial data accessed!");
            });
            
            JButton authorizeBtn = createOperationButton("Authorize High-Value Transaction");
            authorizeBtn.addActionListener(e -> {
                String amountStr = JOptionPane.showInputDialog(dialog, "Enter transaction amount:");
                if (amountStr != null) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        execOps.authorizeHighValueTransactions(amount);
                        JOptionPane.showMessageDialog(dialog, "Transaction of ₱" + amount + " authorized!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            JButton policiesBtn = createOperationButton("Set Company Policies");
            policiesBtn.addActionListener(e -> {
                execOps.setCompanyPolicies();
                JOptionPane.showMessageDialog(dialog, "Company policies updated!");
            });
            
            JButton closeBtn = createOperationButton("Close");
            closeBtn.setBackground(new Color(231, 76, 60));
            closeBtn.addActionListener(e -> dialog.dispose());
            
            contentPanel.add(titleLabel);
            contentPanel.add(strategicBtn);
            contentPanel.add(reportsBtn);
            contentPanel.add(financialBtn);
            contentPanel.add(authorizeBtn);
            contentPanel.add(policiesBtn);
            contentPanel.add(closeBtn);
            
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    }
    
    private JButton createOperationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(0, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private void openApproveLeaveDialog() {
        new ApproveLeaveDialog(this, currentUser).setVisible(true);
        loadLeaveHistory();
    }
}