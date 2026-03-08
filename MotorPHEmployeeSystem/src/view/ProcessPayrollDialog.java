package view;

import model.User;
import model.Employee;
import model.Payslip;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import dao.EmployeeRepository;
import dao.PayslipRepository;
import dao.AttendanceRepository;
import service.SalaryCalculator;
import com.toedter.calendar.JDateChooser;

public class ProcessPayrollDialog extends JDialog {
    private final EmployeeRepository employeeRepository;
    private final PayslipRepository payslipRepository;
    private final SalaryCalculator salaryCalculator;
    private final User currentUser;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private DefaultTableModel tableModel;
    private DefaultTableModel historyTableModel;
    private JTable payrollTable;
    private JTable historyTable;
    private JLabel employeeCountLabel;
    private JLabel totalGrossLabel;
    private JLabel totalDeductionsLabel;
    private JLabel totalNetLabel;

    public ProcessPayrollDialog(JFrame parent, EmployeeRepository empRepo, 
                                 SalaryCalculator calculator, User user) {
        super(parent, "Payroll Processing Center", true);
        this.employeeRepository = empRepo;
        this.payslipRepository = new PayslipRepository();
        this.salaryCalculator = calculator;
        this.currentUser = user;
        
        // Auto-remove any duplicates on startup
        int duplicatesRemoved = payslipRepository.removeDuplicates();
        if (duplicatesRemoved > 0) {
            System.out.println("⚠️ Found and removed " + duplicatesRemoved + " duplicate payslips on startup");
        }
        
        initializeUI();
        setThisWeek();
        
        // AUTO-CALCULATE PAYROLL ON OPEN
        SwingUtilities.invokeLater(() -> {
            calculatePayroll();
            loadPayslipHistory();
        });
    }
    
    private void initializeUI() {
        setSize(1400, 850);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 60));
        
        JLabel titleLabel = new JLabel("WEEKLY PAYROLL PROCESSING CENTER");
        titleLabel.setBounds(20, 15, 700, 30);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setPreferredSize(new Dimension(1400, 750));
        
        createDateSelectionPanel(contentPanel);
        
        createSummaryPanel(contentPanel);
        
        createPayrollTable(contentPanel);
        
        createPayslipHistoryTable(contentPanel);
        
        createActionButtons(contentPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private void createDateSelectionPanel(JPanel parent) {
        JLabel sectionLabel = new JLabel("Pay Period Selection");
        sectionLabel.setBounds(20, 10, 300, 25);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(PRIMARY_COLOR);
        parent.add(sectionLabel);
        
        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setBounds(20, 45, 100, 25);
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(startLabel);
        
        startDateChooser = new JDateChooser();
        startDateChooser.setBounds(120, 45, 200, 30);
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(startDateChooser);
        
        JLabel endLabel = new JLabel("End Date:");
        endLabel.setBounds(340, 45, 100, 25);
        endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(endLabel);
        
        endDateChooser = new JDateChooser();
        endDateChooser.setBounds(430, 45, 200, 30);
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(endDateChooser);
        
        JButton thisWeekBtn = createSmallButton("This Week", 650, 45, 120, 30);
        thisWeekBtn.addActionListener(e -> {
            setThisWeek();
            calculatePayroll();
            loadPayslipHistory();
        });
        parent.add(thisWeekBtn);
        
        JButton lastWeekBtn = createSmallButton("Last Week", 780, 45, 120, 30);
        lastWeekBtn.addActionListener(e -> {
            setLastWeek();
            calculatePayroll();
            loadPayslipHistory();
        });
        parent.add(lastWeekBtn);
        
        JButton recalcBtn = createSmallButton("Refresh Data", 910, 45, 140, 30);
        recalcBtn.setBackground(SUCCESS_COLOR);
        recalcBtn.setForeground(WHITE);
        recalcBtn.addActionListener(e -> {
            calculatePayroll();
            loadPayslipHistory();
        });
        parent.add(recalcBtn);
    }
    
    private void createSummaryPanel(JPanel parent) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBounds(20, 90, 1340, 80);
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel summaryTitle = new JLabel("Payroll Summary");
        summaryTitle.setBounds(15, 5, 200, 25);
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setForeground(PRIMARY_COLOR);
        summaryPanel.add(summaryTitle);
        
        employeeCountLabel = new JLabel("Total Employees: 0");
        employeeCountLabel.setBounds(15, 35, 300, 25);
        employeeCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(employeeCountLabel);
        
        totalGrossLabel = new JLabel("Total Gross Pay: ₱0.00");
        totalGrossLabel.setBounds(350, 35, 300, 25);
        totalGrossLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(totalGrossLabel);
        
        totalDeductionsLabel = new JLabel("Total Deductions: ₱0.00");
        totalDeductionsLabel.setBounds(700, 35, 300, 25);
        totalDeductionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(totalDeductionsLabel);
        
        totalNetLabel = new JLabel("Total Net Pay: ₱0.00");
        totalNetLabel.setBounds(1050, 35, 300, 25);
        totalNetLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalNetLabel.setForeground(SUCCESS_COLOR);
        summaryPanel.add(totalNetLabel);
        
        parent.add(summaryPanel);
    }
    
    private void createPayrollTable(JPanel parent) {
        JLabel tableLabel = new JLabel("Employee Payroll Details");
        tableLabel.setBounds(20, 185, 400, 25);
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableLabel.setForeground(PRIMARY_COLOR);
        parent.add(tableLabel);
        
        String[] columnNames = {
            "Emp #", "Name", "Position", 
            "Gross Pay", "SSS", "PhilHealth", "Pag-IBIG", "Tax",
            "Total Deductions", "Net Pay"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        payrollTable = new JTable(tableModel);
        payrollTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        payrollTable.setRowHeight(28);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payrollTable.setShowVerticalLines(true);
        payrollTable.setGridColor(new Color(189, 195, 199));
        payrollTable.setSelectionBackground(new Color(52, 152, 219, 50));
        payrollTable.setSelectionForeground(TEXT_COLOR);
        payrollTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        payrollTable.getTableHeader().setBackground(WHITE);
        payrollTable.getTableHeader().setForeground(TEXT_COLOR);
        payrollTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) payrollTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        payrollTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // Emp #
        payrollTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Name
        payrollTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Position
        payrollTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Gross Pay
        payrollTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // SSS
        payrollTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // PhilHealth
        payrollTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Pag-IBIG
        payrollTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Tax
        payrollTable.getColumnModel().getColumn(8).setPreferredWidth(120); // Total Deductions
        payrollTable.getColumnModel().getColumn(9).setPreferredWidth(100); // Net Pay
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < payrollTable.getColumnCount(); i++) {
            payrollTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(payrollTable);
        scrollPane.setBounds(20, 215, 1340, 200);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        parent.add(scrollPane);
    }
    
    private void createPayslipHistoryTable(JPanel parent) {
        JLabel historyLabel = new JLabel("Generated Payslips History (All Periods)");
        historyLabel.setBounds(20, 430, 600, 25);
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyLabel.setForeground(PRIMARY_COLOR);
        parent.add(historyLabel);
        
        String[] historyColumns = {
            "Emp #", "Employee Name", "Period Start", "Period End", 
            "Gross Pay", "Net Pay", "Generated Date", "Generated By"
        };
        
        historyTableModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(28);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.setShowVerticalLines(true);
        historyTable.setGridColor(new Color(189, 195, 199));
        historyTable.setSelectionBackground(new Color(52, 152, 219, 50));
        historyTable.setSelectionForeground(TEXT_COLOR);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.getTableHeader().setBackground(WHITE);
        historyTable.getTableHeader().setForeground(TEXT_COLOR);
        historyTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, SUCCESS_COLOR));
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) historyTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // Emp #
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Name
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Start
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // End
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Gross
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Net
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Generated
        historyTable.getColumnModel().getColumn(7).setPreferredWidth(120);  // Generated By
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBounds(20, 460, 1340, 180);
        historyScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        parent.add(historyScrollPane);
        
        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { 
                    viewPayslipFromHistory();
                }
            }
        });
    }
    
    private void createActionButtons(JPanel parent) {
        JButton exportBtn = createStyledButton("Export to CSV", WARNING_COLOR, 20, 660, 180, 40);
        exportBtn.addActionListener(e -> exportPayrollToCSV());
        parent.add(exportBtn);
        
        JButton generateAllBtn = createStyledButton("Generate ALL Employee Payslips", PRIMARY_COLOR, 210, 660, 280, 40);
        generateAllBtn.addActionListener(e -> generateAllPayslips());
        parent.add(generateAllBtn);
        
        JButton generateSelectedBtn = createStyledButton("Generate SELECTED Employee Only", SUCCESS_COLOR, 500, 660, 300, 40);
        generateSelectedBtn.addActionListener(e -> generateSelectedPayslip());
        parent.add(generateSelectedBtn);
        
        JButton historyDialogBtn = createStyledButton("View Payslip History", new Color(142, 68, 173), 810, 660, 200, 40);
        historyDialogBtn.addActionListener(e -> openPayslipHistoryDialog());
        parent.add(historyDialogBtn);
        
        JButton closeBtn = createStyledButton("Close", DANGER_COLOR, 1020, 660, 160, 40);
        closeBtn.addActionListener(e -> dispose());
        parent.add(closeBtn);
    }
    
    private JButton createSmallButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private void setThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);
        
        startDateChooser.setDate(Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        endDateChooser.setDate(Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    
    private void setLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonday = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastSunday = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
        
        startDateChooser.setDate(Date.from(lastMonday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        endDateChooser.setDate(Date.from(lastSunday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    
    private void calculatePayroll() {
        // Validate dates
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        
        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both Start Date and End Date!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(this,
                "End Date cannot be before Start Date!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Clear existing table
        tableModel.setRowCount(0);
        
        // Get all employees
        List<Employee> employees = employeeRepository.getAllEmployees();
        
        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;
        
        // Process each employee
        for (Employee employee : employees) {
            // Generate weekly payslip for this employee
            SalaryCalculator.WeeklyPayslip payslip = 
                salaryCalculator.generateWeeklyPayslip(employee, start, end);
            
            double gross = payslip.getGrossWeekly();
            double sss = payslip.getSssWeekly();
            double philHealth = payslip.getPhilHealthWeekly();
            double pagibig = payslip.getPagIBIGWeekly();
            double tax = payslip.getTaxWeekly();
            double deductions = payslip.getTotalDeductionsWeekly();
            double net = payslip.getNetWeekly();
            
            // Add to table
            tableModel.addRow(new Object[]{
                employee.getEmployeeNumber(),
                employee.getFullName(),
                employee.getPosition(),
                String.format("₱%,.2f", gross),
                String.format("₱%,.2f", sss),
                String.format("₱%,.2f", philHealth),
                String.format("₱%,.2f", pagibig),
                String.format("₱%,.2f", tax),
                String.format("₱%,.2f", deductions),
                String.format("₱%,.2f", net)
            });
            
            totalGross += gross;
            totalDeductions += deductions;
            totalNet += net;
        }
        
        // Update summary labels
        employeeCountLabel.setText("Total Employees: " + employees.size());
        totalGrossLabel.setText(String.format("Total Gross Pay: ₱%,.2f", totalGross));
        totalDeductionsLabel.setText(String.format("Total Deductions: ₱%,.2f", totalDeductions));
        totalNetLabel.setText(String.format("Total Net Pay: ₱%,.2f", totalNet));
    }
    
    private void loadPayslipHistory() {
        historyTableModel.setRowCount(0);
        
        // Get ALL payslips
        List<Payslip> allPayslips = payslipRepository.getAllPayslips();
        
        // Sort by generated date (most recent first)
        allPayslips.sort((p1, p2) -> p2.getGeneratedDate().compareTo(p1.getGeneratedDate()));
        
        // Display ALL payslips (no limit - will be divisible by number of employees)
        for (Payslip payslip : allPayslips) {
            historyTableModel.addRow(new Object[]{
                payslip.getEmployeeNumber(),
                payslip.getEmployeeName(),
                payslip.getPayPeriodStart().toString(),
                payslip.getPayPeriodEnd().toString(),
                String.format("₱%,.2f", payslip.getGrossPay()),
                String.format("₱%,.2f", payslip.getNetPay()),
                payslip.getGeneratedDate().toString(),
                payslip.getGeneratedBy()
            });
        }
    }
    
    private String generatePayslipContent(Employee employee, SalaryCalculator.WeeklyPayslip weeklyPayslip, 
                                          LocalDate start, LocalDate end) {
        StringBuilder content = new StringBuilder();
        
        content.append("════════════════════════════════════════════════════\n");
        content.append("                  MOTORPH PAYSLIP                   \n");
        content.append("════════════════════════════════════════════════════\n\n");
        
        content.append("EMPLOYEE INFORMATION\n");
        content.append("────────────────────────────────────────────────────\n");
        content.append("Name: ").append(employee.getFullName()).append("\n");
        content.append("Employee #: ").append(employee.getEmployeeNumber()).append("\n");
        content.append("Position: ").append(employee.getPosition()).append("\n");
        content.append("Employment Status: ").append(employee.getStatus()).append("\n");
        content.append("\n");
        
        content.append("PAY PERIOD\n");
        content.append("────────────────────────────────────────────────────\n");
        content.append("Period: ").append(start).append(" to ").append(end).append("\n");
        content.append("\n");
        
        content.append("ATTENDANCE SUMMARY\n");
        content.append("────────────────────────────────────────────────────\n");
        
        // Get attendance data from repository
        AttendanceRepository attendanceRepo = new AttendanceRepository();
        double totalHours = attendanceRepo.calculateTotalHours(employee.getEmployeeNumber(), start, end);
        double overtimeHours = attendanceRepo.calculateTotalOvertime(employee.getEmployeeNumber(), start, end);
        
        if (totalHours > 0) {
            // Has attendance records
            double regularHours = totalHours - overtimeHours;
            int expectedDays = 5; // Standard work week
            double actualDays = totalHours / 8.0;
            int absences = (int) Math.max(0, expectedDays - actualDays);
            
            content.append("Total Hours Worked: ").append(String.format("%.2f", totalHours)).append(" hours\n");
            content.append("Regular Hours: ").append(String.format("%.2f", regularHours)).append(" hours\n");
            
            if (overtimeHours > 0) {
                content.append("Overtime Hours: ").append(String.format("%.2f", overtimeHours)).append(" hours\n");
            } else {
                content.append("Overtime Hours: None\n");
            }
            
            double undertime = Math.max(0, (expectedDays * 8) - totalHours);
            if (undertime > 0 && overtimeHours == 0) {
                content.append("Undertime: ").append(String.format("%.2f", undertime)).append(" hours\n");
            }
            
            if (absences == 0 && undertime == 0) {
                content.append("Attendance Status: ✓ PERFECT ATTENDANCE\n");
            } else if (absences > 0) {
                content.append("Absences: ").append(absences).append(" day(s)\n");
            }
        } else {
            // No attendance records - using fallback calculation
            content.append("Attendance Records: Not available\n");
            content.append("Calculation Method: Monthly salary basis (÷4)\n");
        }
        content.append("\n");
        
        // Earnings
        content.append("EARNINGS\n");
        content.append("────────────────────────────────────────────────────\n");
        content.append("Gross Pay: ").append(String.format("₱%,.2f", weeklyPayslip.getGrossWeekly())).append("\n");
        content.append("\n");
        
        // Deductions
        content.append("DEDUCTIONS\n");
        content.append("────────────────────────────────────────────────────\n");
        content.append("SSS Contribution: ").append(String.format("₱%,.2f", weeklyPayslip.getSssWeekly())).append("\n");
        content.append("PhilHealth Contribution: ").append(String.format("₱%,.2f", weeklyPayslip.getPhilHealthWeekly())).append("\n");
        content.append("Pag-IBIG Contribution: ").append(String.format("₱%,.2f", weeklyPayslip.getPagIBIGWeekly())).append("\n");
        content.append("Withholding Tax: ").append(String.format("₱%,.2f", weeklyPayslip.getTaxWeekly())).append("\n");
        content.append("────────────────────────────────────────────────────\n");
        content.append("Total Deductions: ").append(String.format("₱%,.2f", weeklyPayslip.getTotalDeductionsWeekly())).append("\n");
        content.append("\n");
        
        // Net Pay
        content.append("════════════════════════════════════════════════════\n");
        content.append("NET PAY: ").append(String.format("₱%,.2f", weeklyPayslip.getNetWeekly())).append("\n");
        content.append("════════════════════════════════════════════════════\n");
        
        return content.toString();
    }
    
    private void generateAllPayslips() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        
        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both Start Date and End Date first!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Confirm before generating
        int confirm = JOptionPane.showConfirmDialog(this,
            "Generate WEEKLY payslips for ALL " + employeeRepository.getAllEmployees().size() + " employees?\n\n" +
            "Period: " + start + " to " + end + " (1 WEEK)\n\n" +
            "⚠️ This is a WEEKLY payroll system.\n" +
            "Each employee should only have ONE payslip per week.\n\n" +
            "Payslips will be sent to employee dashboards.\n" +
            "Duplicates will be automatically prevented.",
            "Confirm Weekly Payroll Generation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        int generated = 0;
        int skipped = 0;
        List<Employee> employees = employeeRepository.getAllEmployees();
        
        for (Employee employee : employees) {
            // Check for duplicate
            if (payslipRepository.exists(employee.getEmployeeNumber(), start, end)) {
                skipped++;
                continue;
            }
            
            // Generate payslip data
            SalaryCalculator.WeeklyPayslip weeklyPayslip = 
                salaryCalculator.generateWeeklyPayslip(employee, start, end);
            
            // Generate unique payslip ID
            String payslipId = "PS-" + System.currentTimeMillis() + "-" + employee.getEmployeeNumber();
            
            // Generate payslip content
            String content = generatePayslipContent(employee, weeklyPayslip, start, end);
            
            // Create Payslip object
            Payslip payslip = new Payslip(
                payslipId,
                employee.getEmployeeNumber(),
                employee.getFullName(),
                start,
                end,
                LocalDate.now(),
                currentUser.getUsername(),
                weeklyPayslip.getGrossWeekly(),
                weeklyPayslip.getNetWeekly(),
                weeklyPayslip.getTotalDeductionsWeekly(),
                content
            );
            
            payslipRepository.save(payslip);
            generated++;
        }
        
        // Refresh history
        loadPayslipHistory();
        
        // Show result
        JOptionPane.showMessageDialog(this,
            "Payslip Generation Complete!\n\n" +
            "Generated: " + generated + " payslips\n" +
            "Skipped (Duplicates): " + skipped + " payslips\n\n" +
            "Employees can now view their payslips in their dashboards.",
            "Generation Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generateSelectedPayslip() {
        int selectedRow = payrollTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an employee from the table first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String empName = (String) tableModel.getValueAt(selectedRow, 1);
        
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Check for duplicate
        if (payslipRepository.exists(empNumber, start, end)) {
            // Find the existing payslip to show details
            Payslip existingPayslip = payslipRepository.findByEmployeeAndPeriod(empNumber, start, end);
            
            String message = "⚠️ DUPLICATE PAYSLIP DETECTED!\n\n" +
                           "A WEEKLY payslip already exists for this employee and period:\n\n" +
                           "Employee: " + empName + " (#" + empNumber + ")\n" +
                           "Period: " + start + " to " + end + "\n";
            
            if (existingPayslip != null) {
                message += "Previously Generated: " + existingPayslip.getGeneratedDate() + "\n" +
                          "Generated By: " + existingPayslip.getGeneratedBy() + "\n" +
                          "Net Pay: " + String.format("₱%,.2f", existingPayslip.getNetPay()) + "\n";
            }
            
            message += "\nThis is a WEEKLY payroll system. Each employee should only\n" +
                      "have ONE payslip per week.\n\n" +
                      "Check the 'Generated Payslips History' table below to see\n" +
                      "the existing payslip, or use 'View Payslip History' to\n" +
                      "manage duplicates.";
            
            JOptionPane.showMessageDialog(this,
                message,
                "Duplicate Weekly Payslip",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm before generating
        int confirm = JOptionPane.showConfirmDialog(this,
            "Generate WEEKLY payslip for Employee #" + empNumber + " only?\n\n" +
            "Employee: " + empName + "\n" +
            "Period: " + start + " to " + end + " (1 WEEK)\n\n" +
            "⚠️ This is a WEEKLY payroll system.\n\n" +
            "Payslip will be sent to employee dashboard.",
            "Confirm Weekly Payslip Generation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Find employee
        Employee employee = employeeRepository.findByEmployeeNumber(empNumber);
        
        // Generate payslip data
        SalaryCalculator.WeeklyPayslip weeklyPayslip = 
            salaryCalculator.generateWeeklyPayslip(employee, start, end);
        
        // Generate unique payslip ID
        String payslipId = "PS-" + System.currentTimeMillis() + "-" + employee.getEmployeeNumber();
        
        // Generate payslip content
        String content = generatePayslipContent(employee, weeklyPayslip, start, end);
        
        // Create Payslip object
        Payslip payslip = new Payslip(
            payslipId,
            employee.getEmployeeNumber(),
            employee.getFullName(),
            start,
            end,
            LocalDate.now(),
            currentUser.getUsername(),
            weeklyPayslip.getGrossWeekly(),
            weeklyPayslip.getNetWeekly(),
            weeklyPayslip.getTotalDeductionsWeekly(),
            content
        );
        
        payslipRepository.save(payslip);
        
        // Refresh history
        loadPayslipHistory();
        
        // Show success
        JOptionPane.showMessageDialog(this,
            "Payslip generated successfully!\n\n" +
            "Employee: " + empName + " (#" + empNumber + ")\n" +
            "Net Pay: " + String.format("₱%,.2f", weeklyPayslip.getNetWeekly()) + "\n\n" +
            "Employee can now view payslip in their dashboard.",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openPayslipHistoryDialog() {
        new PayslipHistoryDialog(this, payslipRepository).setVisible(true);
        // Refresh history table when dialog closes
        loadPayslipHistory();
    }
    
    private void viewPayslipFromHistory() {
        int selectedRow = historyTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a payslip from the history table!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empNumber = (String) historyTableModel.getValueAt(selectedRow, 0);
        String periodStart = (String) historyTableModel.getValueAt(selectedRow, 2);
        String periodEnd = (String) historyTableModel.getValueAt(selectedRow, 3);
        
        // Find the payslip
        LocalDate start = LocalDate.parse(periodStart);
        LocalDate end = LocalDate.parse(periodEnd);
        Payslip payslip = payslipRepository.findByEmployeeAndPeriod(empNumber, start, end);
        
        if (payslip == null) {
            JOptionPane.showMessageDialog(this,
                "Payslip not found!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Display payslip content in a dialog
        JTextArea textArea = new JTextArea(payslip.getPayslipContent());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Payslip - " + payslip.getEmployeeName() + " - " + payslip.getPeriodDisplay(),
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportPayrollToCSV() {
        JOptionPane.showMessageDialog(this,
            "CSV Export feature coming soon!\n\n" +
            "This will export all payroll data to a CSV file.",
            "Feature Coming Soon",
            JOptionPane.INFORMATION_MESSAGE);
    }
}