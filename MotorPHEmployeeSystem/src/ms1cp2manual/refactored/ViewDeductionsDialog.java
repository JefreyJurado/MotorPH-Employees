package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewDeductionsDialog extends JDialog {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private JComboBox<String> employeeComboBox;
    private JTextArea deductionDetailsArea;
    private JTable deductionTable;
    private DefaultTableModel tableModel;
    
    public ViewDeductionsDialog(Frame parent, EmployeeRepository repository, SalaryCalculator calculator) {
        super(parent, "View Employee Deductions", true);
        this.employeeRepository = repository;
        this.salaryCalculator = calculator;
        
        initializeUI();
        loadEmployees();
    }
    
    private void initializeUI() {
        setSize(800, 750);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(LIGHT_BG);
        
        // Create a container to stack the Header and the Selection bar vertically
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setMaximumSize(new Dimension(2000, 60));
        headerPanel.setPreferredSize(new Dimension(800, 60));

        JLabel titleLabel = new JLabel("Monthly Deduction Breakdown");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // Selection Panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        selectionPanel.setBackground(WHITE);
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Select Employee"));
        selectionPanel.setMaximumSize(new Dimension(2000, 80));

        JLabel empLabel = new JLabel("Employee:");
        empLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectionPanel.add(empLabel);

        employeeComboBox = new JComboBox<>();
        employeeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        employeeComboBox.setPreferredSize(new Dimension(400, 30));
        selectionPanel.add(employeeComboBox);

        JButton viewBtn = createStyledButton("View Deductions", PRIMARY_COLOR);
        viewBtn.setPreferredSize(new Dimension(150, 35)); 
        viewBtn.addActionListener(e -> displayDeductions());
        selectionPanel.add(viewBtn);

        // Add both sub-panels into the top container
        topContainer.add(headerPanel);
        topContainer.add(selectionPanel);

        // Add the single container to the NORTH position
        add(topContainer, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Deduction Details Text Area
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(WHITE);
        textPanel.setBorder(BorderFactory.createTitledBorder("Deduction Summary"));
        
        deductionDetailsArea = new JTextArea();
        deductionDetailsArea.setEditable(false);
        deductionDetailsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        deductionDetailsArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane textScrollPane = new JScrollPane(deductionDetailsArea);
        textScrollPane.setPreferredSize(new Dimension(850, 300));
        textPanel.add(textScrollPane, BorderLayout.CENTER);
        
        contentPanel.add(textPanel, BorderLayout.NORTH);
        
        // Deduction Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Deduction Breakdown Table"));
        
        String[] columnNames = {"Deduction Type", "Monthly Amount", "Weekly Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        deductionTable = new JTable(tableModel);
        deductionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deductionTable.setRowHeight(35);
        deductionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        deductionTable.getTableHeader().setBackground(PRIMARY_COLOR);
        deductionTable.getTableHeader().setForeground(WHITE);
        
        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < deductionTable.getColumnCount(); i++) {
            deductionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane tableScrollPane = new JScrollPane(deductionTable);
        tableScrollPane.setPreferredSize(new Dimension(850, 200));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton printBtn = createStyledButton("Print", SUCCESS_COLOR);
        printBtn.addActionListener(e -> printDeductions());
        buttonPanel.add(printBtn);
        
        JButton closeBtn = createStyledButton("Close", DANGER_COLOR);
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
    
    private void displayDeductions() {
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
            SalaryCalculator.DeductionDetails details = 
                salaryCalculator.getDeductionBreakdown(employee);
            
            // Display text summary
            deductionDetailsArea.setText(details.generateDeductionReport());
            deductionDetailsArea.setCaretPosition(0);
            
            // Update table
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{
                "SSS Contribution",
                String.format("₱%,.2f", details.getSssDeduction()),
                String.format("₱%,.2f", details.getSssDeduction() / 4)
            });
            tableModel.addRow(new Object[]{
                "PhilHealth Contribution",
                String.format("₱%,.2f", details.getPhilHealthDeduction()),
                String.format("₱%,.2f", details.getPhilHealthDeduction() / 4)
            });
            tableModel.addRow(new Object[]{
                "Pag-IBIG Contribution",
                String.format("₱%,.2f", details.getPagIBIGDeduction()),
                String.format("₱%,.2f", details.getPagIBIGDeduction() / 4)
            });
            tableModel.addRow(new Object[]{
                "Withholding Tax",
                String.format("₱%,.2f", details.getTaxDeduction()),
                String.format("₱%,.2f", details.getTaxDeduction() / 4)
            });
            tableModel.addRow(new Object[]{
                "TOTAL DEDUCTIONS",
                String.format("₱%,.2f", details.getTotalDeductions()),
                String.format("₱%,.2f", details.getTotalDeductions() / 4)
            });
        }
    }
    
    private void printDeductions() {
        if (deductionDetailsArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please view deductions first", 
                "No Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            boolean complete = deductionDetailsArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, 
                    "Deduction details printed successfully!", 
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
        button.setPreferredSize(new Dimension(120, 35));
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