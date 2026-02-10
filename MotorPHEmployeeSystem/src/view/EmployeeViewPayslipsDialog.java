package view;

import model.Payslip;
import model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.PayslipRepository;

public class EmployeeViewPayslipsDialog extends JDialog {
    private final Employee employee;
    private final PayslipRepository payslipRepository;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private DefaultTableModel tableModel;
    private JTable payslipTable;
    private JTextArea payslipContentArea;
    
    public EmployeeViewPayslipsDialog(Frame parent, Employee employee) {
        super(parent, "My Payslips", true);
        this.employee = employee;
        this.payslipRepository = new PayslipRepository();
        initializeUI();
        loadPayslips();
    }
    
    private void initializeUI() {
        setSize(1000, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(LIGHT_BG);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        
        JLabel titleLabel = new JLabel("My Payslips");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel with Split
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top: Payslip List
        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        listPanel.setBackground(WHITE);
        listPanel.setBorder(BorderFactory.createTitledBorder("Available Payslips"));
        
        String[] columnNames = {"Payslip ID", "Pay Period", "Generated Date", "Net Pay"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        payslipTable = new JTable(tableModel);
        payslipTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        payslipTable.setRowHeight(30);
        payslipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setOpaque(true);
        
        for (int i = 0; i < payslipTable.getColumnModel().getColumnCount(); i++) {
            payslipTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < payslipTable.getColumnCount(); i++) {
            payslipTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Selection listener
        payslipTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedPayslip();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(payslipTable);
        listPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        splitPane.setTopComponent(listPanel);
        
        // Bottom: Payslip Content
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Payslip Details"));
        
        payslipContentArea = new JTextArea();
        payslipContentArea.setEditable(false);
        payslipContentArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        payslipContentArea.setBackground(new Color(250, 250, 250));
        payslipContentArea.setText("Select a payslip from the list above to view details.");
        
        JScrollPane contentScrollPane = new JScrollPane(payslipContentArea);
        contentPanel.add(contentScrollPane, BorderLayout.CENTER);
        
        splitPane.setBottomComponent(contentPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton printBtn = createStyledButton("Print Payslip", SUCCESS_COLOR);
        printBtn.addActionListener(e -> printPayslip());
        buttonPanel.add(printBtn);
        
        JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshBtn.addActionListener(e -> loadPayslips());
        buttonPanel.add(refreshBtn);
        
        JButton closeBtn = createStyledButton("Close", DANGER_COLOR);
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadPayslips() {
        tableModel.setRowCount(0);
        List<Payslip> payslips = payslipRepository.getPayslipsByEmployeeNumber(employee.getEmployeeNumber());

        if (payslips.isEmpty()) {
            payslipContentArea.setText("No payslips available yet.\n\nPayslips are generated by Finance on payday.\nPlease check back later.");
            return;
        }

        for (Payslip payslip : payslips) {
            tableModel.addRow(new Object[]{
                payslip.getPayslipId(),
                payslip.getPeriodDisplay(),
                payslip.getGeneratedDate(),
                String.format("₱%,.2f", payslip.getNetPay())
            });
        }
    }
    
    private void displaySelectedPayslip() {
        int selectedRow = payslipTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String payslipId = (String) tableModel.getValueAt(selectedRow, 0);
        Payslip payslip = payslipRepository.getPayslipById(payslipId);
        
        if (payslip != null) {
            payslipContentArea.setText(payslip.getPayslipContent());
            payslipContentArea.setCaretPosition(0);
        }
    }
    
    private void printPayslip() {
        if (payslipContentArea.getText().isEmpty() || 
            payslipContentArea.getText().contains("Select a payslip")) {
            JOptionPane.showMessageDialog(this, 
                "Please select a payslip first", 
                "No Payslip Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            boolean complete = payslipContentArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, 
                    "Payslip printed successfully!", 
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
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
        button.setPreferredSize(new Dimension(130, 35));
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