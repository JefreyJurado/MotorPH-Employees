package ms1cp2manual.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernLeaveApplicationDialog extends JDialog {
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public ModernLeaveApplicationDialog(JFrame parent) {
        super(parent, "Leave Application", true);
        initializeModernUI();
    }

    private void initializeModernUI() {
        setSize(600, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(600, 100));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(null);
        
        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setBounds(30, 25, 50, 50);
        headerPanel.add(iconLabel);
        
        JLabel titleLabel = new JLabel("Leave Application Form");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(WHITE);
        titleLabel.setBounds(90, 25, 400, 35);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Submit your leave request");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(90, 60, 400, 20);
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(LIGHT_BG);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Form Container
        JPanel formContainer = new JPanel();
        formContainer.setBounds(30, 20, 520, 460);
        formContainer.setLayout(null);
        formContainer.setBackground(WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        int yPos = 20;
        int labelHeight = 20;
        int fieldHeight = 40;
        int spacing = 75;
        
        // Employee Number
        JLabel empNumLabel = new JLabel("Employee Number *");
        empNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        empNumLabel.setForeground(DARK_COLOR);
        empNumLabel.setBounds(20, yPos, 460, labelHeight);
        formContainer.add(empNumLabel);
        
        JTextField empNumField = new JTextField();
        empNumField.setBounds(20, yPos + 25, 460, fieldHeight);
        styleTextField(empNumField);
        formContainer.add(empNumField);
        yPos += spacing;
        
        // Leave Type
        JLabel leaveTypeLabel = new JLabel("Leave Type *");
        leaveTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        leaveTypeLabel.setForeground(DARK_COLOR);
        leaveTypeLabel.setBounds(20, yPos, 460, labelHeight);
        formContainer.add(leaveTypeLabel);
        
        String[] leaveTypes = {"Vacation Leave", "Sick Leave", "Emergency Leave", 
                              "Maternity Leave", "Paternity Leave"};
        JComboBox<String> leaveTypeComboBox = new JComboBox<>(leaveTypes);
        leaveTypeComboBox.setBounds(20, yPos + 25, 460, fieldHeight);
        leaveTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leaveTypeComboBox.setBackground(WHITE);
        formContainer.add(leaveTypeComboBox);
        yPos += spacing;
        
        // Start Date
        JLabel startDateLabel = new JLabel("Start Date * (MM/DD/YYYY)");
        startDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startDateLabel.setForeground(DARK_COLOR);
        startDateLabel.setBounds(20, yPos, 460, labelHeight);
        formContainer.add(startDateLabel);
        
        JTextField startDateField = new JTextField();
        startDateField.setBounds(20, yPos + 25, 460, fieldHeight);
        styleTextField(startDateField);
        formContainer.add(startDateField);
        yPos += spacing;
        
        // End Date
        JLabel endDateLabel = new JLabel("End Date * (MM/DD/YYYY)");
        endDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        endDateLabel.setForeground(DARK_COLOR);
        endDateLabel.setBounds(20, yPos, 460, labelHeight);
        formContainer.add(endDateLabel);
        
        JTextField endDateField = new JTextField();
        endDateField.setBounds(20, yPos + 25, 460, fieldHeight);
        styleTextField(endDateField);
        formContainer.add(endDateField);
        yPos += spacing;
        
        // Reason
        JLabel reasonLabel = new JLabel("Reason");
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        reasonLabel.setForeground(DARK_COLOR);
        reasonLabel.setBounds(20, yPos, 460, labelHeight);
        formContainer.add(reasonLabel);
        
        JTextArea reasonArea = new JTextArea();
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane reasonScrollPane = new JScrollPane(reasonArea);
        reasonScrollPane.setBounds(20, yPos + 25, 460, 80);
        reasonScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        formContainer.add(reasonScrollPane);
        
        formPanel.add(formContainer);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBounds(30, 490, 520, 50);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(LIGHT_BG);
        
        JButton cancelButton = createModernButton("Cancel", new Color(149, 165, 166));
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
        
        JButton submitButton = createModernButton("Submit Application", SUCCESS_COLOR);
        submitButton.setPreferredSize(new Dimension(180, 45));
        submitButton.addActionListener(e -> {
            if (empNumField.getText().trim().isEmpty() || 
                startDateField.getText().trim().isEmpty() || 
                endDateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all required fields (*)", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LeaveApplication leave = new LeaveApplication(
                empNumField.getText(),
                (String) leaveTypeComboBox.getSelectedItem(),
                startDateField.getText(),
                endDateField.getText()
            );
            
            JOptionPane.showMessageDialog(this, 
                "Leave Application Submitted Successfully!\n\n" + leave.toString(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            empNumField.setText("");
            leaveTypeComboBox.setSelectedIndex(0);
            startDateField.setText("");
            endDateField.setText("");
            reasonArea.setText("");
        });
        buttonsPanel.add(submitButton);
        
        formPanel.add(buttonsPanel);
        
        return formPanel;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
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