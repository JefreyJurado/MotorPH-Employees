package ms1cp2manual.refactored;

public class Employee {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tin;
    private String pagibigNumber;
    private String position;
    private String status;
    private double basicSalary;
    private double riceSubsidy;
    private double clothingAllowance;
    private double semiMonthlyRate;
    private double hourlyRate;

    // Constructor - creates new employee object
    public Employee(String employeeNumber, String lastName, String firstName, 
                   String birthday, String address, String phoneNumber,
                   String sssNumber, String philhealthNumber, String tin,
                   String pagibigNumber, String position, String status,
                   double basicSalary, double riceSubsidy, double clothingAllowance,
                   double semiMonthlyRate, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tin = tin;
        this.pagibigNumber = pagibigNumber;
        this.position = position;
        this.status = status;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.clothingAllowance = clothingAllowance;
        this.semiMonthlyRate = semiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }

    // Getters - controlled access to read data
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTin() { return tin; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getPosition() { return position; }
    public String getStatus() { return status; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getSemiMonthlyRate() { return semiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    
    // Setters - controlled access to modify data
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilhealthNumber(String philhealthNumber) { this.philhealthNumber = philhealthNumber; }
    public void setTin(String tin) { this.tin = tin; }
    public void setPagibigNumber(String pagibigNumber) { this.pagibigNumber = pagibigNumber; }
    public void setPosition(String position) { this.position = position; }
    public void setStatus(String status) { this.status = status; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setSemiMonthlyRate(double semiMonthlyRate) { this.semiMonthlyRate = semiMonthlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    // Business method - employee knows how to calculate its own allowances
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public double calculateTotalAllowances() {
        return riceSubsidy + clothingAllowance;
    }

    public Object[] toTableRow() {
        return new Object[]{
            employeeNumber, lastName, firstName, birthday, address, phoneNumber,
            sssNumber, philhealthNumber, tin, pagibigNumber, position, status,
            String.valueOf(basicSalary), String.valueOf(riceSubsidy), 
            String.valueOf(clothingAllowance), String.valueOf(semiMonthlyRate), 
            String.valueOf(hourlyRate)
        };
    }
}