package ms1cp2manual.refactored;

import javax.swing.*;

public class ViewEmployeeDialog extends JDialog {
    private EmployeeRepository employeeRepository;
    private SalaryCalculator salaryCalculator;

    public ViewEmployeeDialog(JFrame parent, EmployeeRepository repository, 
                            SalaryCalculator calculator) {
        super(parent, "View Employee Details", true);
        this.employeeRepository = repository;
        this.salaryCalculator = calculator;
        initializeUI();
    }

    private void initializeUI() {
        setSize(500, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel selectMonthLabel = new JLabel("Select Month:");
        selectMonthLabel.setBounds(20, 20, 100, 25);
        add(selectMonthLabel);

        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setBounds(120, 20, 150, 25);
        add(monthComboBox);

        JButton computeButton = new JButton("Compute");
        computeButton.setBounds(300, 20, 100, 25);
        add(computeButton);

        JTextArea employeeDetailsArea = new JTextArea();
        employeeDetailsArea.setBounds(20, 60, 440, 380);
        employeeDetailsArea.setEditable(false);
        add(employeeDetailsArea);

        computeButton.addActionListener(e -> {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            // Get first employee as example
            Employee employee = employeeRepository.getEmployee(0);
            if (employee != null) {
                String report = salaryCalculator.generateSalaryReport(employee, selectedMonth);
                employeeDetailsArea.setText(report);
            }
        });
    }
}