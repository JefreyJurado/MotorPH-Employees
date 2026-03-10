package view;

import model.LeaveApplication;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.LeaveRepository;

public class ApproveLeaveDialog extends JDialog {
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private final LeaveRepository leaveRepository;
    private final User currentUser;
    private DefaultTableModel tableModel;
    private JTable leaveTable;
    private JButton approveButton;
    private JButton rejectButton;
    private JComboBox<String> statusFilterCombo;
    private JLabel titleLabel;
    
    public ApproveLeaveDialog(Frame parent, User currentUser) {
        super(parent, "Leave Management", true);
        this.currentUser = currentUser;
        this.leaveRepository = new LeaveRepository();
        initializeUI();
        loadLeavesByStatus("Pending");
    }
    
    private void initializeUI() {
        setSize(1200, 650);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header Panel with title and filter
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title (left side)
        titleLabel = new JLabel("Pending Leave Applications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Filter panel (right side)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.setBackground(PRIMARY_COLOR);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(WHITE);
        filterPanel.add(filterLabel);
        
        String[] statuses = {"Pending", "Approved", "Rejected", "All"};
        statusFilterCombo = new JComboBox<>(statuses);
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilterCombo.setPreferredSize(new Dimension(130, 30));
        statusFilterCombo.addActionListener(e -> {
            String selectedStatus = (String) statusFilterCombo.getSelectedItem();
            loadLeavesByStatus(selectedStatus);
            updateButtonStates(selectedStatus);
            updateTitle(selectedStatus);
        });
        filterPanel.add(statusFilterCombo);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Leave ID", "Employee", "Type", "Start Date", "End Date", "Reason", "Submitted", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaveTable = new JTable(tableModel) {
            @Override
            public int getRowHeight(int row) {
                int baseHeight = 60;
                try {
                    Object reasonValue = getValueAt(row, 5); // Column 5 is Reason
                    if (reasonValue != null) {
                        String reasonText = reasonValue.toString();
                        int charsPerLine = 50;
                        int lines = (reasonText.length() / charsPerLine) + 1;
                        int calculatedHeight = Math.max(baseHeight, lines * 20 + 20);
                        return Math.min(calculatedHeight, 150);
                    }
                } catch (Exception e) {
                }
                return baseHeight;
            }
        };
        
        leaveTable = new JTable(tableModel);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leaveTable.setRowHeight(80);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setGridColor(new Color(189, 195, 199));

        // Column widths
        leaveTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        leaveTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        leaveTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        leaveTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        leaveTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        leaveTable.getColumnModel().getColumn(5).setPreferredWidth(350);
        leaveTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        leaveTable.getColumnModel().getColumn(7).setPreferredWidth(100);

        // Custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setOpaque(true);

        for (int i = 0; i < leaveTable.getColumnModel().getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Center renderer for most columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);

        for (int i = 0; i < leaveTable.getColumnCount(); i++) {
            if (i != 5) { 
                leaveTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        leaveTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value != null) {
                    String text = value.toString();
                    text = text.replace("&", "&amp;")
                               .replace("<", "&lt;")
                               .replace(">", "&gt;");

                    String html = "<html><table width='330'><tr><td align='center' valign='middle'>" 
                                + text 
                                + "</td></tr></table></html>";
                    setText(html);
                } else {
                    setText("");
                }

                setHorizontalAlignment(JLabel.CENTER);

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        approveButton = createStyledButton("Approve", SUCCESS_COLOR);
        approveButton.addActionListener(e -> handleApprove());
        buttonPanel.add(approveButton);
        
        rejectButton = createStyledButton("Reject", DANGER_COLOR);
        rejectButton.addActionListener(e -> handleReject());
        buttonPanel.add(rejectButton);
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> {
            String selectedStatus = (String) statusFilterCombo.getSelectedItem();
            loadLeavesByStatus(selectedStatus);
        });
        buttonPanel.add(refreshButton);
        
        JButton closeButton = createStyledButton("Close", new Color(52, 73, 94));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void loadLeavesByStatus(String status) {
        tableModel.setRowCount(0);
        List<LeaveApplication> leaves;
        
        switch (status) {
            case "Pending":
                leaves = leaveRepository.getPendingLeaves();
                break;
            case "Approved":
                leaves = leaveRepository.getApprovedLeaves();
                break;
            case "Rejected":
                leaves = leaveRepository.getRejectedLeaves();
                break;
            case "All":
                leaves = leaveRepository.getAllLeaves();
                break;
            default:
                leaves = leaveRepository.getPendingLeaves();
        }
        
        for (LeaveApplication leave : leaves) {
            tableModel.addRow(new Object[]{
                leave.getLeaveId(),
                leave.getEmployeeName(),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getSubmittedDate(),
                leave.getStatus()
            });
        }
        
        if (leaves.isEmpty()) {
            String message = status.equals("All") ? "No leave applications found" : "No " + status.toLowerCase() + " leave applications";
            JOptionPane.showMessageDialog(this,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateButtonStates(String status) {
        // Enable approve/reject buttons ONLY for Pending leaves
        boolean isPending = status.equals("Pending");
        approveButton.setEnabled(isPending);
        rejectButton.setEnabled(isPending);
        
        if (!isPending) {
            // Change button appearance when disabled
            approveButton.setBackground(new Color(200, 200, 200));
            rejectButton.setBackground(new Color(200, 200, 200));
        } else {
            approveButton.setBackground(SUCCESS_COLOR);
            rejectButton.setBackground(DANGER_COLOR);
        }
    }
    
    private void updateTitle(String status) {
        switch (status) {
            case "Pending":
                titleLabel.setText("Pending Leave Applications");
                break;
            case "Approved":
                titleLabel.setText("Approved Leave History");
                break;
            case "Rejected":
                titleLabel.setText("Rejected Leave History");
                break;
            case "All":
                titleLabel.setText("All Leave Applications");
                break;
        }
    }
    
    private void handleApprove() {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a leave application to approve",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String leaveId = (String) tableModel.getValueAt(selectedRow, 0);
        String employeeName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Approve leave application for " + employeeName + "?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            LeaveApplication leave = findLeaveById(leaveId);
            if (leave != null) {
                leave.approve(currentUser.getUsername());
                leaveRepository.updateLeave(leave);
                
                JOptionPane.showMessageDialog(this,
                    "Leave application approved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadLeavesByStatus("Pending");
            }
        }
    }
    
    private void handleReject() {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a leave application to reject",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String leaveId = (String) tableModel.getValueAt(selectedRow, 0);
        String employeeName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reject leave application for " + employeeName + "?",
            "Confirm Rejection",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            LeaveApplication leave = findLeaveById(leaveId);
            if (leave != null) {
                leave.reject(currentUser.getUsername());
                leaveRepository.updateLeave(leave);
                
                JOptionPane.showMessageDialog(this,
                    "Leave application rejected",
                    "Rejected",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadLeavesByStatus("Pending");
            }
        }
    }
    
    private LeaveApplication findLeaveById(String leaveId) {
        for (LeaveApplication leave : leaveRepository.getAllLeaves()) {
            if (leave.getLeaveId().equals(leaveId)) {
                return leave;
            }
        }
        return null;
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
                if (button.isEnabled()) {
                    button.setBackground(bgColor.brighter());
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor);
                }
            }
        });
        
        return button;
    }
}