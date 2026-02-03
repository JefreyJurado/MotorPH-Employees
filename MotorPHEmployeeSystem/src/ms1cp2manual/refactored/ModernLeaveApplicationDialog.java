package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernLeaveApplicationDialog extends JDialog {
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private JTextField employeeNameField;
    private JComboBox<String> leaveTypeCombo;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextArea reasonArea;
    private LeaveRepository leaveRepository;
    
    public ModernLeaveApplicationDialog(Frame parent) {
        super(parent, "Leave Application", true);
        this.leaveRepository = new LeaveRepository();
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        
        JPanel headerPanel = new JPanel(new GridBagLayout()); 
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(600, 60));

        JLabel titleLabel = new JLabel("File Leave Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel); 

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        int yPos = 20;
        int labelWidth = 150;
        int fieldX = 160;
        int fieldWidth = 350;
        int spacing = 60;
        
        // Employee Name
        JLabel nameLabel = new JLabel("Employee Name:");
        nameLabel.setBounds(20, yPos, labelWidth, 25);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(nameLabel);
        
        employeeNameField = new JTextField();
        employeeNameField.setBounds(fieldX, yPos, fieldWidth, 30);
        employeeNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(employeeNameField);
        yPos += spacing;
        
        // Leave Type
        JLabel typeLabel = new JLabel("Leave Type:");
        typeLabel.setBounds(20, yPos, labelWidth, 25);
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(typeLabel);
        
        String[] leaveTypes = {"Sick Leave", "Vacation Leave", "Emergency Leave", "Personal Leave"};
        leaveTypeCombo = new JComboBox<>(leaveTypes);
        leaveTypeCombo.setBounds(fieldX, yPos, fieldWidth, 30);
        leaveTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(leaveTypeCombo);
        yPos += spacing;
        
        // Start Date
        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setBounds(20, yPos, labelWidth, 25);
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(startLabel);
        
        startDateField = new JTextField("YYYY-MM-DD");
        startDateField.setBounds(fieldX, yPos, fieldWidth, 30);
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(startDateField);
        yPos += spacing;
        
        // End Date
        JLabel endLabel = new JLabel("End Date:");
        endLabel.setBounds(20, yPos, labelWidth, 25);
        endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(endLabel);
        
        endDateField = new JTextField("YYYY-MM-DD");
        endDateField.setBounds(fieldX, yPos, fieldWidth, 30);
        endDateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentPanel.add(endDateField);
        yPos += spacing;
        
        // Reason
        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setBounds(20, yPos, labelWidth, 25);
        reasonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(reasonLabel);
        
        reasonArea = new JTextArea();
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setBounds(fieldX, yPos, fieldWidth, 100);
        contentPanel.add(scrollPane);
        yPos += 120;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBounds(20, yPos, 520, 50);
        
        JButton submitButton = createStyledButton("Submit", SUCCESS_COLOR);
        submitButton.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitButton);
        
        JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        contentPanel.add(buttonPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private void handleSubmit() {
        String employeeName = employeeNameField.getText().trim();
        String leaveType = (String) leaveTypeCombo.getSelectedItem();
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();
        String reason = reasonArea.getText().trim();
        
        if (employeeName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LeaveApplication leave = new LeaveApplication(employeeName, leaveType, startDate, endDate, reason);
        leaveRepository.addLeave(leave);
        
        JOptionPane.showMessageDialog(this,
            "Leave application submitted successfully!\nStatus: Pending\nLeave ID: " + leave.getLeaveId(),
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
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