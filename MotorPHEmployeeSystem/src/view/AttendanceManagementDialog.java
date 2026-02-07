package view;

import model.AttendanceRecord;
import model.Employee;
import repository.AttendanceRepository;
import repository.EmployeeRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AttendanceManagementDialog extends JDialog {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color WHITE = Color.WHITE;
    
    private JTextField tfEmployeeNum, tfDate, tfTimeIn, tfTimeOut, tfRemarks;
    private JComboBox<String> cbStatus;
    private DefaultTableModel tableModel;
    private JTable attendanceTable;
    private JButton btnSave, btnDelete, btnClear;
    
    public AttendanceManagementDialog(Frame parent, AttendanceRepository attendanceRepo, 
                                     EmployeeRepository employeeRepo) {
        super(parent, "Attendance Management", true);
        this.attendanceRepository = attendanceRepo;
        this.employeeRepository = employeeRepo;
        
        initializeUI();
        loadAttendanceData();
    }
    
    private void initializeUI() {
        setSize(1200, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Attendance Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(WHITE);
        
        // Input Form Panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Record Attendance",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: Employee Number, Date
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Employee #:"), gbc);
        
        gbc.gridx = 1;
        tfEmployeeNum = new JTextField(15);
        panel.add(tfEmployeeNum, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 3;
        tfDate = new JTextField(15);
        tfDate.setText(LocalDate.now().toString());
        panel.add(tfDate, gbc);
        
        // Row 2: Time In, Time Out
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Time In (HH:MM):"), gbc);
        
        gbc.gridx = 1;
        tfTimeIn = new JTextField(15);
        tfTimeIn.setText("08:00");
        panel.add(tfTimeIn, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Time Out (HH:MM):"), gbc);
        
        gbc.gridx = 3;
        tfTimeOut = new JTextField(15);
        tfTimeOut.setText("17:00");
        panel.add(tfTimeOut, gbc);
        
        // Row 3: Status, Remarks
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        String[] statuses = {"Present", "Absent", "Leave", "Holiday"};
        cbStatus = new JComboBox<>(statuses);
        panel.add(cbStatus, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Remarks:"), gbc);
        
        gbc.gridx = 3;
        tfRemarks = new JTextField(15);
        panel.add(tfRemarks, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Attendance Records",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        String[] columnNames = {"Employee #", "Date", "Time In", "Time Out", 
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
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        
        attendanceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleRowSelection();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(1150, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(WHITE);
        
        btnSave = createStyledButton("Save Attendance");
        btnSave.addActionListener(e -> handleSave());
        panel.add(btnSave);
        
        btnDelete = createStyledButton("Delete Selected");
        btnDelete.addActionListener(e -> handleDelete());
        btnDelete.setEnabled(false);
        panel.add(btnDelete);
        
        btnClear = createStyledButton("Clear Form");
        btnClear.addActionListener(e -> clearForm());
        panel.add(btnClear);
        
        JButton btnClose = createStyledButton("Close");
        btnClose.setBackground(new Color(231, 76, 60));
        btnClose.addActionListener(e -> dispose());
        panel.add(btnClose);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getText().equals("Close")) {
                    button.setBackground(SECONDARY_COLOR);
                } else {
                    button.setBackground(new Color(231, 76, 60));
                }
            }
        });
        
        return button;
    }
    
    private void handleSave() {
        try {
            // Validate inputs
            String empNum = tfEmployeeNum.getText().trim();
            if (empNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Employee Number", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verify employee exists
            Employee employee = employeeRepository.findByEmployeeNumber(empNum);
            if (employee == null) {
                JOptionPane.showMessageDialog(this, "Employee Number not found", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse date
            LocalDate date = LocalDate.parse(tfDate.getText().trim(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            // Parse times
            LocalTime timeIn = null;
            LocalTime timeOut = null;
            String status = (String) cbStatus.getSelectedItem();
            
            if ("Present".equals(status)) {
                if (tfTimeIn.getText().trim().isEmpty() || tfTimeOut.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Time In and Time Out required for Present status", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                timeIn = LocalTime.parse(tfTimeIn.getText().trim(), 
                    DateTimeFormatter.ofPattern("HH:mm"));
                timeOut = LocalTime.parse(tfTimeOut.getText().trim(), 
                    DateTimeFormatter.ofPattern("HH:mm"));
                
                if (timeOut.isBefore(timeIn)) {
                    JOptionPane.showMessageDialog(this, 
                        "Time Out cannot be before Time In", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            String remarks = tfRemarks.getText().trim();
            
            // Create attendance record
            AttendanceRecord record = new AttendanceRecord(empNum, date, timeIn, timeOut, 
                status, remarks);
            
            // Add to repository
            attendanceRepository.addAttendance(record);
            attendanceRepository.saveToCSV();
            
            // Refresh table
            loadAttendanceData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, 
                "Attendance record saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date or time format. Please use YYYY-MM-DD for date and HH:MM for time.", 
                "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving attendance: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleDelete() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this attendance record?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String empNum = (String) tableModel.getValueAt(selectedRow, 0);
                String dateStr = (String) tableModel.getValueAt(selectedRow, 1);
                LocalDate date = LocalDate.parse(dateStr, 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                
                attendanceRepository.deleteAttendance(empNum, date);
                attendanceRepository.saveToCSV();
                
                loadAttendanceData();
                clearForm();
                
                JOptionPane.showMessageDialog(this, 
                    "Attendance record deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting attendance: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleRowSelection() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow != -1) {
            tfEmployeeNum.setText((String) tableModel.getValueAt(selectedRow, 0));
            
            String dateStr = (String) tableModel.getValueAt(selectedRow, 1);
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            tfDate.setText(date.toString());
            
            String timeInStr = (String) tableModel.getValueAt(selectedRow, 2);
            String timeOutStr = (String) tableModel.getValueAt(selectedRow, 3);
            
            if (!"-".equals(timeInStr)) {
                LocalTime timeIn = LocalTime.parse(timeInStr, 
                    DateTimeFormatter.ofPattern("hh:mm a"));
                tfTimeIn.setText(timeIn.format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                tfTimeIn.setText("");
            }
            
            if (!"-".equals(timeOutStr)) {
                LocalTime timeOut = LocalTime.parse(timeOutStr, 
                    DateTimeFormatter.ofPattern("hh:mm a"));
                tfTimeOut.setText(timeOut.format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                tfTimeOut.setText("");
            }
            
            cbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 6));
            tfRemarks.setText((String) tableModel.getValueAt(selectedRow, 7));
            
            btnDelete.setEnabled(true);
        }
    }
    
    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        
        for (AttendanceRecord record : attendanceRepository.getAllAttendance()) {
            tableModel.addRow(record.toTableRow());
        }
    }
    
    private void clearForm() {
        tfEmployeeNum.setText("");
        tfDate.setText(LocalDate.now().toString());
        tfTimeIn.setText("08:00");
        tfTimeOut.setText("17:00");
        cbStatus.setSelectedIndex(0);
        tfRemarks.setText("");
        attendanceTable.clearSelection();
        btnDelete.setEnabled(false);
    }
}