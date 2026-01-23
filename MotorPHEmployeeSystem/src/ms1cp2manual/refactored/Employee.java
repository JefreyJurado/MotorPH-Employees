package ms1cp2manual.refactored;

public abstract class Employee {
    protected String employeeNumber;
    protected String lastName;
    protected String firstName;
    protected String birthday;
    protected String address;
    protected String phoneNumber;
    protected String sssNumber;
    protected String philhealthNumber;
    protected String tin;
    protected String pagibigNumber;
    protected String status;
    protected String position;
    protected String immediateSupervisor;  // NEW FIELD
    protected double basicSalary;
    protected double riceSubsidy;
    protected double phoneAllowance;  // NEW FIELD
    protected double clothingAllowance;
    protected double semiMonthlyRate;
    protected double hourlyRate;

    public Employee(String employeeNumber, String lastName, String firstName, 
                   String birthday, String address, String phoneNumber,
                   String sssNumber, String philhealthNumber, String tin,
                   String pagibigNumber, String status, String position,
                   String immediateSupervisor,  // NEW PARAMETER
                   double basicSalary, double riceSubsidy, double phoneAllowance,  // NEW PARAMETER
                   double clothingAllowance, double semiMonthlyRate, double hourlyRate) {
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
        this.status = status;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.semiMonthlyRate = semiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }

    // Getters
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
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }  // NEW
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }  // NEW
    public double getClothingAllowance() { return clothingAllowance; }
    public double getSemiMonthlyRate() { return semiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    
    // Setters
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
    public void setStatus(String status) { this.status = status; }
    public void setPosition(String position) { this.position = position; }
    public void setImmediateSupervisor(String immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; }  // NEW
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }  // NEW
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setSemiMonthlyRate(double semiMonthlyRate) { this.semiMonthlyRate = semiMonthlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public double calculateTotalAllowances() {
        return riceSubsidy + phoneAllowance + clothingAllowance;  // UPDATED
    }

    public Object[] toTableRow() {
        return new Object[]{
            employeeNumber, lastName, firstName, birthday, address, phoneNumber,
            sssNumber, philhealthNumber, tin, pagibigNumber, status, position,
            immediateSupervisor,  // NEW
            String.valueOf(basicSalary), String.valueOf(riceSubsidy), 
            String.valueOf(phoneAllowance),  // NEW
            String.valueOf(clothingAllowance), String.valueOf(semiMonthlyRate), 
            String.valueOf(hourlyRate)
        };
    }
    
    public abstract String getDepartment();
    public abstract String getJobDescription();
}