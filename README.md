MotorPH Employee Management System

A comprehensive Java-based Employee Management System demonstrating advanced Object-Oriented Programming principles with role-based access control, payroll processing, and complete employee lifecycle management.

Project Overview
This enterprise-grade application manages MotorPH company's 34 employees across 8 departments with a sophisticated two-layer Role-Based Access Control (RBAC) system, comprehensive payroll calculations following Philippine tax and benefits regulations, and complete MVC architecture.

Core Features

Employee Management
- Complete CRUD operations (Create, Read, Update, Delete)
- 19-field employee records with government IDs
- 8 specialized employee types with department-specific capabilities
- Factory pattern for automatic employee type determination
- CSV-based data persistence

Role-Based Access Control (Two-Layer)
- Layer 1: User roles (SystemAdmin, Owner, HR, Finance, IT, Accounting, Executive, Employee)
- Layer 2: Employee interface implementations (IHROperations, IFinanceOperations, etc.)
- Dynamic dashboard based on combined permissions
- Role-specific operational capabilities

Payroll & Compensation
- SSS, PhilHealth, Pag-IBIG contributions (2024 rates)
- Withholding tax calculations (2024 tax brackets)
- Weekly payslip generation
- Monthly salary reports with deduction breakdowns
- Allowances management (rice, phone, clothing)

Leave Management
- Leave application submission (Sick, Vacation, Emergency)
- Approval workflow for HR/SystemAdmin
- Leave history tracking
- Status management (Pending, Approved, Rejected)

Attendance Tracking
- Time-in/time-out recording
- Hours worked and overtime calculation
- Attendance status management
- Personal and management views

Modern UI/UX
- Professional blue and white color scheme
- Card-based responsive layouts
- Role-specific dashboards
- Intuitive navigation
- Real-time data validation

Architecture & OOP Principles

Encapsulation
- All data fields private across 25+ classes
- Controlled access through public getters/setters
- Business logic encapsulated in service classes
- Repository pattern hides data access implementation

Abstraction
- Abstract `Employee` base class with abstract methods
- 6 operational interfaces (IEmployeeRepository, IHROperations, IFinanceOperations, IITOperations, IAccountingOperations, IExecutiveOperations)
- Clear separation between interface and implementation
- Complex calculations hidden behind simple method calls

Inheritance
- 8 employee subclasses extend abstract `Employee` class
- All dialogs extend `JDialog` for consistent behavior
- Main frame extends `JFrame` with domain-specific features
- Code reuse through 19 inherited fields per subclass

Polymorphism
- Factory pattern returns abstract `Employee` type
- Method overriding (`getDepartment()`, `getJobDescription()`)
- Interface polymorphism (`instanceof` checks for RBAC)
- Runtime type determination for dynamic behavior
- ActionListener interface for event handling

Project Structure

MotorPHEmployeeSystem/
│
├── src/
│   ├── model/                          # Domain Entities
│   │   ├── Employee.java              # Abstract base class
│   │   ├── ExecutiveEmployee.java     # CEO, COO, CFO, CMO
│   │   ├── HREmployee.java            # HR department
│   │   ├── FinanceEmployee.java       # Finance/Payroll
│   │   ├── ITEmployee.java            # IT operations
│   │   ├── AccountingEmployee.java    # Accounting team
│   │   ├── OperationsEmployee.java    # Operations/Logistics
│   │   ├── SalesEmployee.java         # Sales & Marketing
│   │   ├── GeneralEmployee.java       # General staff
│   │   ├── User.java                  # Authentication entity
│   │   ├── LeaveApplication.java      # Leave requests
│   │   ├── AttendanceRecord.java      # Attendance tracking
│   │   └── Payslip.java               # Payroll records
│   │
│   ├── service/                        # Business Logic
│   │   ├── AuthenticationService.java
│   │   ├── SalaryCalculator.java
│   │   ├── IHROperations.java
│   │   ├── IFinanceOperations.java
│   │   ├── IITOperations.java
│   │   ├── IAccountingOperations.java
│   │   └── IExecutiveOperations.java
│   │
│   ├── repository/                     # Data Access Layer
│   │   ├── EmployeeRepository.java
│   │   ├── UserRepository.java
│   │   ├── LeaveRepository.java
│   │   ├── PayslipRepository.java
│   │   ├── AttendanceRepository.java
│   │   ├── IEmployeeRepository.java   # Interface
│   │   └── CSVFileHandler.java        # File I/O
│   │
│   ├── util/                           # Utilities
│   │   └── EmployeeFactory.java       # Factory pattern
│   │
│   └── view/                           # UI Layer
│       ├── ApproveLeaveDialog.java
│       ├── AttendanceManagementDialog.java
│       ├── EmployeeAttendanceDialog.java
│       ├── EmployeeDashboardFrame.java
│       ├── EmployeeSalaryDetailsDialog.java
│       ├── EmployeeViewPayslipsDialog.java
│       ├── ModernEmployeeManagementFrame.java
│       ├── ModernLeaveApplicationDialog.java
│       ├── ModernLoginDialog.java
│       ├── ModernViewEmployeeDialog.java
│       └── ViewDeductionsDialog.java
|       └── WeeklyPayslipDialog.java
│
└── data/                               # CSV Data Files
    ├── employees.csv                  # Employee records
    ├── users.csv                      # Authentication data
    ├── leaves.csv                     # Leave applications
    ├── payslips.csv                   # Payroll records
    └── attendance.csv                 # Attendance records

Technologies Used

- Java SE 8+
- Swing - GUI framework
- CSV - Data persistence
- Factory Pattern - Object creation
- Repository Pattern - Data access abstraction
- MVC Architecture - Application structure

Installation & Setup

Prerequisites
- Java JDK 8 or higher
- NetBeans IDE (recommended) or any Java IDE

Steps
1. Clone the repository
   ```bash
   git clone https://github.com/YOUR_USERNAME/MotorPHEmployeeSystem.git
   ```

2. Open in NetBeans
   - File → Open Project
   - Select `MotorPHEmployeeSystem` folder

3. Clean and Build
   - Right-click project → Clean and Build

4. Run the application
   - Right-click project → Run
   - Or run `MainApplication.java`

User Accounts

System Admin
- Username: `admin`
- Password: `admin123`
- Access: Leave approval + Employee #1 profile (CEO)

Owner
- Username: `owner_manuel`
- Password: `owner123`
- Access: Full system access

Department Heads
- HR: `hr_andrea` / `pass123`
- Finance: `finance_anthony` / `pass123`
- IT: `it_eduard` / `pass123`
- Accounting: `acct_roderick` / `pass123`

Executives
- CFO: `cfo_bianca` / `pass123`
- COO: `coo_antonio` / `pass123`

Regular Employees
- Employee: `employee_brad` / `pass123`
- And more... (see `data/users.csv`)

Database Schema

Employees (19 fields)
- Employee Number, Name, Birthday, Address
- Contact Info (Phone)
- Government IDs (SSS, PhilHealth, TIN, Pag-IBIG)
- Employment (Position, Status, Supervisor)
- Compensation (Basic Salary, Allowances, Rates)

Users (4 fields)
- Username, Password, Role, Employee Number

Leave Applications (8 fields)
- Leave ID, Employee Info, Type, Dates, Reason, Status

Attendance (8 fields)
- Employee Number, Date, Time In/Out, Hours, OT, Status

Payslips (11 fields)
- Payslip ID, Employee Info, Period, Gross Pay, Deductions, Net Pay

This project demonstrates:
- All 4 OOP Principles (Encapsulation, Abstraction, Inheritance, Polymorphism)
- Design Patterns (Factory, Repository, MVC)
- SOLID Principles (Single Responsibility, Interface Segregation)
- Enterprise Architecture (Layered architecture, Separation of concerns)
- Real-world Business Logic (Philippine payroll system)
- Professional UI/UX (Modern, responsive design)

Documentation

Complete documentation includes:
- Method Dictionary - 250+ methods across 25 classes
- CRC Cards - Class Responsibility Collaborator cards for all classes
- Architecture Overview - Detailed explanation of OOP principles implementation

This is an academic project. For improvements or issues, please create a pull request.
This project is for educational purposes as part of an Object-Oriented Programming course.

Author: Jefrey Jurado
- Course: Object-Oriented Programming (MO-IT110)
- Institution: Mapúa Malayan Digital College
