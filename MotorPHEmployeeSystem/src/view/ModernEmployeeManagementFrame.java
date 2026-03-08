package view;

import model.User;
import model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import dao.EmployeeRepository;
import dao.PayslipRepository;
import service.SalaryCalculator;
import dao.AttendanceRepository;

public class ModernEmployeeManagementFrame extends JFrame {
    private final EmployeeRepository employeeRepository;
    private final PayslipRepository payslipRepository;
    private final SalaryCalculator salaryCalculator;
    private final User currentUser; 
    private final AttendanceRepository attendanceRepository;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private DefaultTableModel tableModel;
    private JTable employeeTable;
    private JButton updateButton, deleteButton;

    public ModernEmployeeManagementFrame(EmployeeRepository repository, User user) {
        this.employeeRepository = repository;
        this.payslipRepository = new PayslipRepository();
        this.salaryCalculator = new SalaryCalculator();
        this.currentUser = user;
        this.attendanceRepository = new AttendanceRepository();
        initializeModernUI();
        loadEmployeeData();
        applyAccessControl();
    }
    
    private void openAttendanceManagement() {
        new AttendanceManagementDialog(this, attendanceRepository, employeeRepository).setVisible(true);
    }
    
    private void applyAccessControl() {
        setTitle("MotorPH Employee Management System - " + currentUser.getRole() + ": " + currentUser.getUsername());
    }

    private void initializeModernUI() {
        setTitle("MotorPH Employee Management System");
        setSize(1400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
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
        
        JPanel tablePanel = createModernTable();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 22));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 80));
        
        JLabel titleLabel = new JLabel("MotorPH Employee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setPreferredSize(new Dimension(400, 40));
        headerPanel.add(titleLabel);
        
        // Add some spacing
        headerPanel.add(Box.createHorizontalStrut(20));
        
        String role = currentUser.getRole();
        
        // QUERY - Everyone with access can see
        if (role.equals("SystemAdmin") || role.equals("Owner") || role.equals("HR") || 
            role.equals("Finance") || role.equals("IT") || role.equals("Accounting") || 
            role.equals("Executive")) {
            JButton queryBtn = createHeaderButton("Query");
            queryBtn.addActionListener(e -> handleQuery());
            headerPanel.add(queryBtn);
        }
        
        // ADD - Only HR and Owner
        if (role.equals("HR") || role.equals("Owner")) {
            JButton addBtn = createHeaderButton("Add");
            addBtn.addActionListener(e -> handleAdd());
            headerPanel.add(addBtn);
        }
        
        // UPDATE - Only HR and Owner
        if (role.equals("HR") || role.equals("Owner")) {
            updateButton = createHeaderButton("Update");
            updateButton.setEnabled(false);
            updateButton.addActionListener(e -> handleUpdate());
            headerPanel.add(updateButton);
        } else {
            updateButton = new JButton(); 
            updateButton.setVisible(false);
        }
        
        // DELETE - Only HR and Owner
        if (role.equals("HR") || role.equals("Owner")) {
            deleteButton = createHeaderButton("Delete");
            deleteButton.setEnabled(false);
            deleteButton.addActionListener(e -> handleDelete());
            headerPanel.add(deleteButton);
        } else {
            deleteButton = new JButton();
            deleteButton.setVisible(false);
        }
        
        // CLEAR - Everyone
        if (role.equals("SystemAdmin") || role.equals("Owner") || role.equals("HR") || 
            role.equals("Finance") || role.equals("IT") || role.equals("Accounting") || 
            role.equals("Executive")) {
            JButton clearBtn = createHeaderButton("Clear");
            clearBtn.addActionListener(e -> clearSelection());
            headerPanel.add(clearBtn);
        }
        
        // SAVE - Everyone
        if (role.equals("SystemAdmin") || role.equals("Owner") || role.equals("HR") || 
            role.equals("Finance") || role.equals("IT") || role.equals("Accounting") || 
            role.equals("Executive")) {
            JButton saveBtn = createHeaderButton("Save");
            saveBtn.addActionListener(e -> handleSave());
            headerPanel.add(saveBtn);
        }
        
        // LEAVE - Only HR and Owner
        if (role.equals("HR") || role.equals("Owner")) {
            JButton leaveBtn = createHeaderButton("Leave");
            leaveBtn.addActionListener(e -> openApproveLeaveDialog());
            headerPanel.add(leaveBtn);
        }

        // ATTENDANCE - HR, Finance (for payroll), and Owner
        if (role.equals("HR") || role.equals("Finance") || role.equals("Owner")) {
            JButton attendanceBtn = createHeaderButton("Attendance");
            attendanceBtn.setPreferredSize(new Dimension(110, 35));
            attendanceBtn.addActionListener(e -> openAttendanceManagement());
            headerPanel.add(attendanceBtn);
        }
        
        // PROCESS PAYROLL - Only Finance, Accounting, Executive, Owner
        if (role.equals("Finance") || role.equals("Accounting") || 
            role.equals("Executive") || role.equals("Owner")) {
            JButton processPayrollBtn = createHeaderButton("Process Payroll");
            processPayrollBtn.setPreferredSize(new Dimension(130, 35));
            processPayrollBtn.addActionListener(e -> openProcessPayrollDialog());
            headerPanel.add(processPayrollBtn);
        }
        
        // BACK BUTTON - At the end, after all other buttons
        if (currentUser.getEmployeeNumber() != null && !currentUser.getEmployeeNumber().isEmpty()) {
            JButton backBtn = new JButton("← Back");
            backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            backBtn.setBackground(new Color(231, 76, 60));
            backBtn.setForeground(WHITE);
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.setPreferredSize(new Dimension(90, 35));
            backBtn.addActionListener(e -> {
                dispose();
                new EmployeeDashboardFrame(employeeRepository, currentUser).setVisible(true);
            });
            backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    backBtn.setBackground(new Color(192, 57, 43));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    backBtn.setBackground(new Color(231, 76, 60));
                }
            });
            headerPanel.add(backBtn);
        }
        
        return headerPanel;
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
        });
        
        return button;
    }

    private JPanel createModernTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel tableTitle = new JLabel("Employee Records - All Employees");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(TEXT_COLOR);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
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
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        employeeTable.setRowHeight(35);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setShowVerticalLines(true);
        employeeTable.setGridColor(new Color(189, 195, 199));
        employeeTable.setSelectionBackground(new Color(52, 152, 219, 80));
        employeeTable.setSelectionForeground(TEXT_COLOR);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths for better readability
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(70);   // Emp #
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Last Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // First Name
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Birthday
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(300);  // Address
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Phone
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // SSS
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(120);  // PhilHealth
        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(120);  // TIN
        employeeTable.getColumnModel().getColumn(9).setPreferredWidth(120);  // Pag-IBIG
        employeeTable.getColumnModel().getColumn(10).setPreferredWidth(100); // Status
        employeeTable.getColumnModel().getColumn(11).setPreferredWidth(200); // Position
        employeeTable.getColumnModel().getColumn(12).setPreferredWidth(150); // Supervisor
        employeeTable.getColumnModel().getColumn(13).setPreferredWidth(100); // Salary
        employeeTable.getColumnModel().getColumn(14).setPreferredWidth(80);  // Rice
        employeeTable.getColumnModel().getColumn(15).setPreferredWidth(100); // Phone Allow
        employeeTable.getColumnModel().getColumn(16).setPreferredWidth(100); // Clothing
        employeeTable.getColumnModel().getColumn(17).setPreferredWidth(110); // Semi-Monthly
        employeeTable.getColumnModel().getColumn(18).setPreferredWidth(90);  // Hourly

        JTableHeader header = employeeTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setOpaque(true);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                c.setBackground(PRIMARY_COLOR);
                c.setForeground(WHITE);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };
        headerRenderer.setOpaque(true);

        for (int i = 0; i < employeeTable.getColumnModel().getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

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

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleRowSelection();
            }
        });

        return tablePanel;
    }

    private void handleQuery() {
        String empNumber = JOptionPane.showInputDialog(this, 
            "Enter Employee Number:", 
            "Query Employee", 
            JOptionPane.QUESTION_MESSAGE);

        if (empNumber == null || empNumber.trim().isEmpty()) {
            return;
        }

        Employee employee = employeeRepository.findByEmployeeNumber(empNumber.trim());

        if (employee != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(empNumber.trim())) {
                    employeeTable.setRowSelectionInterval(i, i);
                    employeeTable.scrollRectToVisible(employeeTable.getCellRect(i, 0, true));
                    break;
                }
            }
            showModernDialog("Employee found!", "Query Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showModernDialog("Employee Number not found", "Query Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        AddEmployeeDialog dialog = new AddEmployeeDialog(this, employeeRepository);
        dialog.setVisible(true);
        
        // Refresh table after dialog closes
        if (dialog.isEmployeeAdded()) {
            loadEmployeeData();
            showModernDialog("Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleUpdate() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee employee = employeeRepository.getEmployee(selectedRow);
            
            UpdateEmployeeDialog dialog = new UpdateEmployeeDialog(this, employeeRepository, employee, selectedRow);
            dialog.setVisible(true);
            
            // Refresh table after dialog closes
            if (dialog.isEmployeeUpdated()) {
                loadEmployeeData();
                clearSelection();
                showModernDialog("Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void handleDelete() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee employee = employeeRepository.getEmployee(selectedRow);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this employee?\n\n" +
                "Employee: " + employee.getFullName() + "\n" +
                "Position: " + employee.getPosition(), 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    employeeRepository.deleteEmployee(selectedRow);
                    tableModel.removeRow(selectedRow);
                    employeeRepository.saveToCSV();
                    clearSelection();
                    showModernDialog("Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (RuntimeException e) {
                    showModernDialog("Error deleting employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            showModernDialog("Please select an employee to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSave() {
        try {
            employeeRepository.saveToCSV();
            showModernDialog("All data saved to CSV successfully!", "Save Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            showModernDialog("Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRowSelection() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }
    
    private void clearSelection() {
        employeeTable.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeRepository.getAllEmployees();
        for (Employee employee : employees) {
            tableModel.addRow(employee.toTableRow());
        }
    }

    private void openApproveLeaveDialog() {
        new ApproveLeaveDialog(this, currentUser).setVisible(true);
    }
    
    private void openProcessPayrollDialog() {
        new ProcessPayrollDialog(this, employeeRepository, salaryCalculator, currentUser).setVisible(true);
    }

    private void showModernDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);    
    }
}