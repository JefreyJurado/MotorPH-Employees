package ms1cp2manual.refactored;

import javax.swing.*;
import java.awt.*;

public class LeaveApplicationDialog extends JDialog {
  public LeaveApplicationDialog(JFrame parent) {
    super(parent, "Leave Application", true);
    initializeUI();
}

private void initializeUI() {
    setSize(800, 450);
    setLayout(new GridLayout(6, 2, 10, 10));
//    setLayout(new BorderLayout(10, 10)); 
    setLocationRelativeTo(null);

    JLabel empNumLabel = new JLabel("Employee Number:");
    JTextField empNumField = new JTextField();

    JLabel leaveTypeLabel = new JLabel("Leave Type:");
    String[] leaveTypes = {"Vacation Leave", "Sick Leave", "Emergency Leave"};
    JComboBox<String> leaveTypeComboBox = new JComboBox<>(leaveTypes);

    JLabel startDateLabel = new JLabel("Start Date:");
    JTextField startDateField = new JTextField();

    JLabel endDateLabel = new JLabel("End Date:");
    JTextField endDateField = new JTextField();

    JButton submitButton = new JButton("Submit");

    add(empNumLabel);
    add(empNumField);
    add(leaveTypeLabel);
    add(leaveTypeComboBox);
    add(startDateLabel);
    add(startDateField);
    add(endDateLabel);
    add(endDateField);
    add(new JLabel());
    add(submitButton);

    submitButton.addActionListener(e -> {
        LeaveApplication leave = new LeaveApplication(
            empNumField.getText(),
            (String) leaveTypeComboBox.getSelectedItem(),
            startDateField.getText(),
            endDateField.getText()
        );

        JOptionPane.showMessageDialog(this, leave.toString(), 
            "Submission Successful", JOptionPane.INFORMATION_MESSAGE);

        empNumField.setText("");
        leaveTypeComboBox.setSelectedIndex(0);
        startDateField.setText("");
        endDateField.setText("");
    });
}  
}