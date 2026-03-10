package view;

import model.LeaveApplication;
import model.Employee;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import dao.LeaveRepository;

public class ModernLeaveApplicationDialog extends JDialog {
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    private final Employee currentEmployee;
    private JTextField employeeNameField;
    private JComboBox<String> leaveTypeCombo;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JTextArea reasonArea;
    private final LeaveRepository leaveRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // Constructor to accept Employee
    public ModernLeaveApplicationDialog(Frame parent, Employee employee) {
        super(parent, "Leave Application", true);
        this.currentEmployee = employee;
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
        
        // Employee Name (Read-only, pre-filled)
        JLabel nameLabel = new JLabel("Employee Name:");
        nameLabel.setBounds(20, yPos, labelWidth, 25);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(nameLabel);
        
        employeeNameField = new JTextField();
        employeeNameField.setBounds(fieldX, yPos, fieldWidth, 30);
        employeeNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        employeeNameField.setText(currentEmployee.getFullName());
        employeeNameField.setEditable(false); 
        employeeNameField.setBackground(new Color(240, 240, 240)); 
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
        
        JLabel startLabel = new JLabel("Start Date:");
        startLabel.setBounds(20, yPos, labelWidth, 25);
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(startLabel);
        
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        startDateChooser.setBounds(fieldX, yPos, fieldWidth, 30);
        contentPanel.add(startDateChooser);
        yPos += spacing;
        
        JLabel endLabel = new JLabel("End Date:");
        endLabel.setBounds(20, yPos, labelWidth, 25);
        endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(endLabel);
        
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        endDateChooser.setBounds(fieldX, yPos, fieldWidth, 30);
        contentPanel.add(endDateChooser);
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
    
    // Validation Method
    private boolean validateLeaveFields() {
        
        // 1. Check Employee (should always be set, but validate anyway)
        if (currentEmployee == null || currentEmployee.getEmployeeNumber() == null) {
            JOptionPane.showMessageDialog(this,
                "Employee information is missing",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 2. Check Leave Type
        String leaveType = (String) leaveTypeCombo.getSelectedItem();
        if (leaveType == null || leaveType.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a leave type",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 3. Check Start Date (from JDateChooser)
        Date startDate = startDateChooser.getDate();
        if (startDate == null) {
            JOptionPane.showMessageDialog(this,
                "Start Date is required!\n\n" +
                "Please select a date from the calendar.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 4. Check End Date (from JDateChooser)
        Date endDate = endDateChooser.getDate();
        if (endDate == null) {
            JOptionPane.showMessageDialog(this,
                "End Date is required!\n\n" +
                "Please select a date from the calendar.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 5. NEW VALIDATION - Check that Start Date is not in the PAST (CRITICAL!)
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date todayDate = today.getTime();
        
        if (startDate.before(todayDate)) {
            JOptionPane.showMessageDialog(this,
                "Invalid Start Date!\n\n" +
                "Start Date cannot be in the past.\n\n" +
                "Selected Start Date: " + dateFormat.format(startDate) + "\n" +
                "Today's Date: " + dateFormat.format(todayDate) + "\n\n" +
                "Please select today or a future date.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 6. NEW VALIDATION - Check that End Date is not in the PAST (CRITICAL!)
        if (endDate.before(todayDate)) {
            JOptionPane.showMessageDialog(this,
                "Invalid End Date!\n\n" +
                "End Date cannot be in the past.\n\n" +
                "Selected End Date: " + dateFormat.format(endDate) + "\n" +
                "Today's Date: " + dateFormat.format(todayDate) + "\n\n" +
                "Please select today or a future date.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 7. Check that End Date is not before Start Date
        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(this,
                "Invalid Date Range!\n\n" +
                "End Date cannot be before Start Date.\n\n" +
                "Start Date: " + dateFormat.format(startDate) + "\n" +
                "End Date: " + dateFormat.format(endDate),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 8. Calculate and display leave duration
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long days = (diffInMillies / (1000 * 60 * 60 * 24)) + 1; 
        
        // 9. Warn if leave duration is very long (more than 30 days)
        if (days > 30) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Leave duration is very long (" + days + " days).\n\n" +
                "Start: " + dateFormat.format(startDate) + "\n" +
                "End: " + dateFormat.format(endDate) + "\n\n" +
                "This exceeds the typical maximum leave period.\n\n" +
                "Do you want to continue anyway?",
                "Long Leave Duration Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        // 10. Check Reason (Must not be empty)
        if (reasonArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Reason for leave is required",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            reasonArea.requestFocus();
            return false;
        }
        
        // 11. Check Reason Length (At least 10 characters for meaningful explanation)
        if (reasonArea.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(this,
                "Please provide a more detailed reason (at least 10 characters)\n" +
                "Current length: " + reasonArea.getText().trim().length() + " characters",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            reasonArea.requestFocus();
            return false;
        }
        
        // 12. Warn if Reason is very long (more than 500 characters)
        if (reasonArea.getText().trim().length() > 500) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Your reason is very long (" + reasonArea.getText().trim().length() + " characters).\n" +
                "Consider making it more concise. Continue anyway?",
                "Long Reason Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                reasonArea.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private void handleSubmit() {
        // Validation Check - Must be first
        if (!validateLeaveFields()) {
            return;  // Stop if validation fails
        }
        
        String leaveType = (String) leaveTypeCombo.getSelectedItem();
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
        
        String reason = reasonArea.getText().trim();
        
        // CREATE leave with employee NUMBER
        LeaveApplication leave = new LeaveApplication(
            currentEmployee.getEmployeeNumber(),
            currentEmployee.getFullName(),
            leaveType, 
            startDateStr, 
            endDateStr,  
            reason
        );
        
        leaveRepository.addLeave(leave);
        
        // Calculate duration for display
        long diffInMillies = endDate.getTime() - startDate.getTime();
        long days = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;
        
        JOptionPane.showMessageDialog(this,
            "Leave application submitted successfully!\n\n" +
            "Leave ID: " + leave.getLeaveId() + "\n" +
            "Type: " + leaveType + "\n" +
            "Duration: " + days + " day(s)\n" +
            "From: " + startDateStr + "\n" +
            "To: " + endDateStr + "\n" +
            "Status: Pending",
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