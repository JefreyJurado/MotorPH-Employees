package view;

import model.Payslip;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import dao.PayslipRepository;
import com.toedter.calendar.JDateChooser;

public class PayslipHistoryDialog extends JDialog {
    private final PayslipRepository payslipRepository;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    
    private JDateChooser filterStartDateChooser;
    private JDateChooser filterEndDateChooser;
    private DefaultTableModel tableModel;
    private JTable payslipTable;
    private JLabel totalPayslipsLabel;
    private JLabel totalGrossLabel;
    private JLabel totalNetLabel;

    public PayslipHistoryDialog(JDialog parent, PayslipRepository payslipRepo) {
        super(parent, "Payslip History Viewer", true);
        this.payslipRepository = payslipRepo;
        
        initializeUI();
        loadAllPayslips(); // Load all on open
    }
    
    private void initializeUI() {
        setSize(1400, 800);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1400, 60));
        
        JLabel titleLabel = new JLabel("PAYSLIP HISTORY VIEWER");
        titleLabel.setBounds(20, 15, 500, 30);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setPreferredSize(new Dimension(1400, 700));
        
        // Filter Panel
        createFilterPanel(contentPanel);
        
        // Summary Panel
        createSummaryPanel(contentPanel);
        
        // Payslip Table
        createPayslipTable(contentPanel);
        
        // Action Buttons
        createActionButtons(contentPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private void createFilterPanel(JPanel parent) {
        JLabel filterLabel = new JLabel("Filter by Pay Period");
        filterLabel.setBounds(20, 10, 250, 25);
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        filterLabel.setForeground(PRIMARY_COLOR);
        parent.add(filterLabel);
        
        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setBounds(20, 45, 100, 25);
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(startLabel);
        
        filterStartDateChooser = new JDateChooser();
        filterStartDateChooser.setBounds(120, 45, 180, 30);
        filterStartDateChooser.setDateFormatString("yyyy-MM-dd");
        filterStartDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(filterStartDateChooser);
        
        JLabel endLabel = new JLabel("End Date:");
        endLabel.setBounds(320, 45, 100, 25);
        endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(endLabel);
        
        filterEndDateChooser = new JDateChooser();
        filterEndDateChooser.setBounds(410, 45, 180, 30);
        filterEndDateChooser.setDateFormatString("yyyy-MM-dd");
        filterEndDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        parent.add(filterEndDateChooser);
        
        JButton filterBtn = createSmallButton("Apply Filter", 610, 45, 130, 30);
        filterBtn.setBackground(SUCCESS_COLOR);
        filterBtn.setForeground(WHITE);
        filterBtn.addActionListener(e -> filterByDateRange());
        parent.add(filterBtn);
        
        JButton clearFilterBtn = createSmallButton("Clear Filter", 750, 45, 130, 30);
        clearFilterBtn.addActionListener(e -> {
            filterStartDateChooser.setDate(null);
            filterEndDateChooser.setDate(null);
            loadAllPayslips();
        });
        parent.add(clearFilterBtn);
        
        JButton searchEmployeeBtn = createSmallButton("Search by Employee #", 900, 45, 200, 30);
        searchEmployeeBtn.addActionListener(e -> searchByEmployee());
        parent.add(searchEmployeeBtn);
    }
    
    private void createSummaryPanel(JPanel parent) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBounds(20, 90, 1340, 70);
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel summaryTitle = new JLabel("Summary");
        summaryTitle.setBounds(15, 5, 150, 25);
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setForeground(PRIMARY_COLOR);
        summaryPanel.add(summaryTitle);
        
        totalPayslipsLabel = new JLabel("Total Payslips: 0");
        totalPayslipsLabel.setBounds(15, 35, 400, 25);
        totalPayslipsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(totalPayslipsLabel);
        
        totalGrossLabel = new JLabel("Total Gross Pay: ₱0.00");
        totalGrossLabel.setBounds(450, 35, 400, 25);
        totalGrossLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(totalGrossLabel);
        
        totalNetLabel = new JLabel("Total Net Pay: ₱0.00");
        totalNetLabel.setBounds(900, 35, 400, 25);
        totalNetLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalNetLabel.setForeground(SUCCESS_COLOR);
        summaryPanel.add(totalNetLabel);
        
        parent.add(summaryPanel);
    }
    
    private void createPayslipTable(JPanel parent) {
        JLabel tableLabel = new JLabel("Generated Payslips");
        tableLabel.setBounds(20, 175, 300, 25);
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableLabel.setForeground(PRIMARY_COLOR);
        parent.add(tableLabel);
        
        String[] columnNames = {
            "Emp #", "Employee Name", "Period Start", "Period End", 
            "Gross Pay", "Net Pay", "Generated Date", "Generated By"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        payslipTable = new JTable(tableModel);
        payslipTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        payslipTable.setRowHeight(28);
        payslipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        payslipTable.setShowVerticalLines(true);
        payslipTable.setGridColor(new Color(189, 195, 199));
        payslipTable.setSelectionBackground(new Color(52, 152, 219, 50));
        payslipTable.setSelectionForeground(TEXT_COLOR);
        payslipTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        payslipTable.getTableHeader().setBackground(WHITE);
        payslipTable.getTableHeader().setForeground(TEXT_COLOR);
        payslipTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) payslipTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        payslipTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // Emp #
        payslipTable.getColumnModel().getColumn(1).setPreferredWidth(250);  // Name
        payslipTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Start
        payslipTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // End
        payslipTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Gross
        payslipTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Net
        payslipTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Generated
        payslipTable.getColumnModel().getColumn(7).setPreferredWidth(150);  // Generated By
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < payslipTable.getColumnCount(); i++) {
            payslipTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(payslipTable);
        scrollPane.setBounds(20, 205, 1340, 440);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        parent.add(scrollPane);
        
        payslipTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewSelectedPayslip();
                }
            }
        });
    }
    
    private void createActionButtons(JPanel parent) {
        JButton viewBtn = createStyledButton("View Selected Payslip", SUCCESS_COLOR, 20, 660, 220, 40);
        viewBtn.addActionListener(e -> viewSelectedPayslip());
        parent.add(viewBtn);
        
        JButton deleteBtn = createStyledButton("Delete Selected Payslip", DANGER_COLOR, 250, 660, 220, 40);
        deleteBtn.addActionListener(e -> deleteSelectedPayslip());
        parent.add(deleteBtn);
        
        JButton removeDuplicatesBtn = createStyledButton("Remove Duplicates", new Color(230, 126, 34), 480, 660, 220, 40);
        removeDuplicatesBtn.addActionListener(e -> removeDuplicates());
        parent.add(removeDuplicatesBtn);
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(52, 152, 219), 710, 660, 180, 40);
        refreshBtn.addActionListener(e -> loadAllPayslips());
        parent.add(refreshBtn);
        
        JButton closeBtn = createStyledButton("Close", new Color(149, 165, 166), 900, 660, 180, 40);
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
    
    private void loadAllPayslips() {
        tableModel.setRowCount(0);
        
        List<Payslip> allPayslips = payslipRepository.getAllPayslips();
        
        // Sort by generated date (most recent first)
        allPayslips.sort((p1, p2) -> p2.getGeneratedDate().compareTo(p1.getGeneratedDate()));
        
        displayPayslips(allPayslips);
    }
    
    private void filterByDateRange() {
        Date startDate = filterStartDateChooser.getDate();
        Date endDate = filterEndDateChooser.getDate();
        
        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both Start Date and End Date!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        tableModel.setRowCount(0);
        
        List<Payslip> filteredPayslips = payslipRepository.findByPeriod(start, end);
        displayPayslips(filteredPayslips);
    }
    
    private void searchByEmployee() {
        String empNumber = JOptionPane.showInputDialog(this, 
            "Enter Employee Number:", 
            "Search by Employee", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (empNumber == null || empNumber.trim().isEmpty()) {
            return;
        }
        
        tableModel.setRowCount(0);
        
        List<Payslip> employeePayslips = payslipRepository.findByEmployee(empNumber.trim());
        displayPayslips(employeePayslips);
    }
    
    private void displayPayslips(List<Payslip> payslips) {
        tableModel.setRowCount(0);
        
        double totalGross = 0;
        double totalNet = 0;
        
        for (Payslip payslip : payslips) {
            tableModel.addRow(new Object[]{
                payslip.getEmployeeNumber(),
                payslip.getEmployeeName(),
                payslip.getPayPeriodStart().toString(),
                payslip.getPayPeriodEnd().toString(),
                String.format("₱%,.2f", payslip.getGrossPay()),
                String.format("₱%,.2f", payslip.getNetPay()),
                payslip.getGeneratedDate().toString(),
                payslip.getGeneratedBy()
            });
            
            totalGross += payslip.getGrossPay();
            totalNet += payslip.getNetPay();
        }
        
        // Update summary
        totalPayslipsLabel.setText("Total Payslips: " + payslips.size());
        totalGrossLabel.setText(String.format("Total Gross Pay: ₱%,.2f", totalGross));
        totalNetLabel.setText(String.format("Total Net Pay: ₱%,.2f", totalNet));
    }
    
    private void viewSelectedPayslip() {
        int selectedRow = payslipTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a payslip from the table!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String periodStart = (String) tableModel.getValueAt(selectedRow, 2);
        String periodEnd = (String) tableModel.getValueAt(selectedRow, 3);
        
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
        
        // Display payslip content
        JTextArea textArea = new JTextArea(payslip.getPayslipContent());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 600));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Payslip - " + payslip.getEmployeeName() + " - " + payslip.getPeriodDisplay(),
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removeDuplicates() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "This will remove duplicate payslips for the same employee and period.\n\n" +
            "Only the most recently generated payslip will be kept.\n\n" +
            "Continue?",
            "Remove Duplicates",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        int duplicatesRemoved = payslipRepository.removeDuplicates();
        
        if (duplicatesRemoved > 0) {
            JOptionPane.showMessageDialog(this,
                "Successfully removed " + duplicatesRemoved + " duplicate payslip(s)!\n\n" +
                "The payslip history has been cleaned up.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            loadAllPayslips(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this,
                "No duplicates found!\n\n" +
                "Your payslip history is already clean.",
                "No Duplicates",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteSelectedPayslip() {
        int selectedRow = payslipTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a payslip to delete!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String empNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String empName = (String) tableModel.getValueAt(selectedRow, 1);
        String periodStart = (String) tableModel.getValueAt(selectedRow, 2);
        String periodEnd = (String) tableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this payslip?\n\n" +
            "Employee: " + empName + " (#" + empNumber + ")\n" +
            "Period: " + periodStart + " to " + periodEnd + "\n\n" +
            "This action cannot be undone!",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        LocalDate start = LocalDate.parse(periodStart);
        LocalDate end = LocalDate.parse(periodEnd);
        
        payslipRepository.delete(empNumber, start, end);
        
        JOptionPane.showMessageDialog(this,
            "Payslip deleted successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadAllPayslips();
    }
}