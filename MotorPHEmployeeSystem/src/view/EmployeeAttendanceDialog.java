package view;

import model.AttendanceRecord;
import model.Employee;
import repository.AttendanceRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EmployeeAttendanceDialog extends JDialog {
    private final Employee employee;
    private final AttendanceRepository attendanceRepository;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color WHITE = Color.WHITE;
    
    private DefaultTableModel tableModel;
    private JTable attendanceTable;
    private JLabel lblTotalHours, lblOvertimeHours, lblDaysPresent;
    
    public EmployeeAttendanceDialog(Frame parent, Employee employee, AttendanceRepository attendanceRepo) {
        super(parent, "My Attendance Records", true);
        this.employee = employee;
        this.attendanceRepository = attendanceRepo;
        
        initializeUI();
        loadAttendanceData();
        updateSummary();
    }
    
    private void initializeUI() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setPreferredSize(new Dimension(900, 80));
        titlePanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("My Attendance Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel empLabel = new JLabel("Employee: " + employee.getFullName() + 
            " (" + employee.getEmployeeNumber() + ")", SwingConstants.CENTER);
        empLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        empLabel.setForeground(WHITE);
        titlePanel.add(empLabel, BorderLayout.SOUTH);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main Content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(WHITE);
        
        // Summary Panel
        JPanel summaryPanel = createSummaryPanel();
        contentPanel.add(summaryPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(WHITE);
        
        JButton btnClose = createStyledButton("Close");
        btnClose.setBackground(new Color(231, 76, 60));
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "This Month Summary",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        lblTotalHours = createSummaryLabel("Total Hours: 0.00");
        lblOvertimeHours = createSummaryLabel("Overtime: 0.00");
        lblDaysPresent = createSummaryLabel("Days Present: 0");
        
        panel.add(lblTotalHours);
        panel.add(lblOvertimeHours);
        panel.add(lblDaysPresent);
        
        return panel;
    }
    
    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        String[] columnNames = {"Date", "Time In", "Time Out", 
                               "Total Hours", "OT Hours", "Status", "Remarks"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        attendanceTable.setRowHeight(30);
        attendanceTable.setShowVerticalLines(true);
        attendanceTable.setGridColor(new Color(189, 195, 199));
        
        JTableHeader header = attendanceTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < attendanceTable.getColumnCount(); i++) {
            attendanceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        
        List<AttendanceRecord> records = attendanceRepository.getAttendanceByEmployee(
            employee.getEmployeeNumber());
        
        for (AttendanceRecord record : records) {
            Object[] row = record.toTableRow();
            // Remove employee number from display (column 0)
            Object[] displayRow = new Object[row.length - 1];
            System.arraycopy(row, 1, displayRow, 0, row.length - 1);
            tableModel.addRow(displayRow);
        }
    }
    
    private void updateSummary() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        
        double totalHours = attendanceRepository.calculateTotalHours(
            employee.getEmployeeNumber(), startOfMonth, endOfMonth);
        double overtimeHours = attendanceRepository.calculateTotalOvertime(
            employee.getEmployeeNumber(), startOfMonth, endOfMonth);
        
        List<AttendanceRecord> monthRecords = attendanceRepository.getAttendanceByEmployeeAndDateRange(
            employee.getEmployeeNumber(), startOfMonth, endOfMonth);
        
        long daysPresent = monthRecords.stream()
            .filter(r -> "Present".equals(r.getStatus()))
            .count();
        
        lblTotalHours.setText(String.format("Total Hours: %.2f", totalHours));
        lblOvertimeHours.setText(String.format("Overtime: %.2f", overtimeHours));
        lblDaysPresent.setText(String.format("Days Present: %d", daysPresent));
    }
}