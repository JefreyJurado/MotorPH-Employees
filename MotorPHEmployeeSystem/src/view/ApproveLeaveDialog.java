package view;

import model.LeaveApplication;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;
import repository.LeaveRepository;

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
    
    public ApproveLeaveDialog(Frame parent, User currentUser) {
        super(parent, "Approve Leave Applications", true);
        this.currentUser = currentUser;
        this.leaveRepository = new LeaveRepository();
        initializeUI();
        loadLeaves();
    }
    
    private void initializeUI() {
        setSize(1200, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header Panel with centered title
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Pending Leave Applications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Leave ID", "Employee", "Type", "Start Date", "End Date", "Reason", "Submitted", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaveTable = new JTable(tableModel);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leaveTable.setRowHeight(60);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // column widths
        leaveTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        leaveTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        leaveTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        leaveTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        leaveTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        leaveTable.getColumnModel().getColumn(5).setPreferredWidth(350);
        leaveTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        leaveTable.getColumnModel().getColumn(7).setPreferredWidth(80);

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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaveTable.getColumnCount(); i++) {
            if (i != 5) {
                leaveTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        leaveTable.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JPanel outerPanel = new JPanel(new GridBagLayout());
                outerPanel.setOpaque(true);

                if (isSelected) {
                    outerPanel.setBackground(table.getSelectionBackground());
                } else {
                    outerPanel.setBackground(table.getBackground());
                }

                JTextPane textPane = new JTextPane();
                textPane.setText(value != null ? value.toString() : "");
                textPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                textPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                textPane.setOpaque(false);
                textPane.setEditable(false);

                StyledDocument doc = textPane.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), center, false);

                if (isSelected) {
                    textPane.setForeground(table.getSelectionForeground());
                } else {
                    textPane.setForeground(table.getForeground());
                }

                outerPanel.add(textPane);

                return outerPanel;
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton approveButton = createStyledButton("Approve", SUCCESS_COLOR);
        approveButton.addActionListener(e -> handleApprove());
        buttonPanel.add(approveButton);
        
        JButton rejectButton = createStyledButton("Reject", DANGER_COLOR);
        rejectButton.addActionListener(e -> handleReject());
        buttonPanel.add(rejectButton);
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> loadLeaves());
        buttonPanel.add(refreshButton);
        
        JButton closeButton = createStyledButton("Close", new Color(52, 73, 94));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void loadLeaves() {
        tableModel.setRowCount(0);
        List<LeaveApplication> pendingLeaves = leaveRepository.getPendingLeaves();
        
        for (LeaveApplication leave : pendingLeaves) {
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
        
        if (pendingLeaves.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No pending leave applications",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
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
                
                loadLeaves();
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
                
                loadLeaves();
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