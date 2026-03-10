MotorPH Employee Management System

Project Information
Student: Jefrey Jurado  
Section: S2101  
Course: MO-IT110 (Object-Oriented Programming)  
Milestone: 2 - OOP Implementation  
Repository: https://github.com/JefreyJurado/MotorPH-Employees

Project Description
Enterprise-grade Employee Management System for MotorPH featuring role-based access control (RBAC), payroll processing, leave management, and attendance tracking. This system was refactored from procedural CP2 code to a complete Object-Oriented Programming (OOP) architecture using the MVC (Model-View-Controller) pattern.

Included Libraries
- JCalendar 1.4 (included in `lib/jcalendar-1/jcalendar-1.4.jar`)
  - Used for date selection in leave applications
  - Already configured in NetBeans project
  - No additional download required

Setup Instructions

Step 1: Clone the Repository
```bash
git clone https://github.com/JefreyJurado/MotorPH-Employees.git
cd MotorPHEmployeeSystem
```

Step 2: Open in NetBeans
1. Open NetBeans IDE
2. Go to File → Open Project
3. Navigate to the cloned `MotorPHEmployeeSystem` folder
4. Select the project (you should see the NetBeans icon)
5. Click Open Project

Step 3: Verify Libraries
1. In the Projects tab, expand Libraries
2. Verify `jcalendar-1.4.jar` is listed
3. If missing or showing error:
   - Right-click Libraries → Add JAR/Folder
   - Navigate to `lib/jcalendar-1/jcalendar-1.4.jar`
   - Click **Open**

Step 4: Verify CSV Files
Ensure these files are in the project root directory:
- ✅ `employees.csv` - 35 employee records
- ✅ `users.csv` - Login credentials (35+ accounts)
- ✅ `leaves.csv` - Leave application history
- ✅ `payslips.csv` - Payroll history
- ✅ `attendance.csv` - Attendance records

Step 5: Build and Run
1. Right-click project → Clean and Build
2. Wait for "BUILD SUCCESSFUL" message
3. Right-click project → Run
4. Login screen will appear

Test Accounts

Admin/Owner Access (Full System Access)
| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | SystemAdmin | Full access to all operations |
| `owner` | `owner123` | Owner | Full access to all operations |

Department Heads
| Username | Password | Role | Department | Access |
|----------|----------|------|------------|--------|
| `villanuevaandrea` | `emp006` | HR | Human Resources | Employee CRUD, Leave Management |
| `salcedoanthony` | `emp011` | Finance | Finance | Payroll Processing |
| `hernandezeduard` | `emp005` | IT | IT | System Access |
| `alvaroderick` | `emp010` | Accounting | Accounting | Financial Records |
| `garciamanuel` | `emp001` | Executive | Executive (CEO) | Executive Dashboard |

Regular Employees
| Username | Password | Employee # | Full Name |
|----------|----------|------------|-----------|
| `martinezcarlos` | `emp033` | 33 | Carlos Ian Martinez |
| `castrojohn` | `emp032` | 32 | John Rafael Castro |
| `delacruzjuan` | `emp035` | 35 | Juan Dela Cruz |

*Note: All 35 employees have accounts. Format: `lastnamefirstname` (lowercase, no spaces)*

Features by Role

SystemAdmin / Owner
- Full access to ALL operations
- Employee CRUD (Create, Read, Update, Delete)
- Leave approval/rejection with history
- Payroll processing and management
- Attendance tracking
- System configuration

HR Department
- Employee management (Add, Update, Delete)
- Leave application review and approval
- Leave history tracking (Pending/Approved/Rejected/All)
- Attendance management
- Employee records query

Finance Department
- Payroll processing
- Automated salary calculations (hourly + overtime)
- Government deductions (SSS, PhilHealth, Pag-IBIG, Tax)
- Payslip generation
- Deduction management

Employees
- View personal information
- File leave applications (with past date validation)
- View leave status and history
- Check weekly/monthly payslips
- View attendance records
- Update contact information (phone, address)

Project Architecture

Package Structure
```
src/
├── dao/                    # Data Access Layer
│   ├── AttendanceRepository.java
│   ├── CSVFileHandler.java
│   ├── EmployeeRepository.java
│   ├── IEmployeeRepository.java
│   ├── LeaveRepository.java
│   ├── PayslipRepository.java
│   └── UserRepository.java
│
├── model/                  # Business Logic Layer
│   ├── AccountingEmployee.java
│   ├── AttendanceRecord.java
│   ├── Employee.java (abstract)
│   ├── ExecutiveEmployee.java
│   ├── FinanceEmployee.java
│   ├── GeneralEmployee.java
│   ├── HREmployee.java
│   ├── ITEmployee.java
│   ├── LeaveApplication.java
│   ├── OperationsEmployee.java
│   ├── Payslip.java
│   ├── SalesEmployee.java
│   └── User.java
│
├── service/                # Service Layer
│   ├── AuthenticationService.java
│   ├── IAccountingOperations.java
│   ├── IExecutiveOperations.java
│   ├── IFinanceOperations.java
│   ├── IHROperations.java
│   ├── IITOperations.java
│   └── SalaryCalculator.java
│
├── util/                   # Utility Classes
│   └── EmployeeFactory.java
│
├── view/                   # UI Layer (Swing)
│   ├── AddEmployeeDialog.java
│   ├── ApproveLeaveDialog.java
│   ├── AttendanceManagementDialog.java
│   ├── EmployeeAttendanceDialog.jav
│   ├── EmployeeDashboardFrame.java
│   ├── EmployeeSalaryDetialsDialog.java
│   ├── EmployeeViewPayslipsDialog.java
│   ├── ModernEmployeeManagementFrame.java
│   ├── ModernLeaveApplicationDialog.java
│   ├── ModernLoginDialog.java
│   ├── ModernViewEmployeeDialog.java
│   ├── PayslipHistoryDialog.java
│   ├── ProcessPayrollDialog.java
│   ├── UpdateEmployeeDialog.java
│   ├── ViewDeductionsDialog.java
│   ├──WeeklyPayslipDialog.java
│
└── MainApplication.java    # Application Entry Point
```

Design Patterns Used
- MVC Pattern - Separation of concerns
- Factory Pattern - EmployeeFactory for object creation
- Repository Pattern - Data access abstraction
- Strategy Pattern - Role-based operations
- Singleton Pattern - Authentication service

Key Features

Security & Validation
- Role-Based Access Control (RBAC) with 7 roles
- Context-based button visibility (HR vs Finance operations)
- Government ID validation (SSS, PhilHealth, TIN, Pag-IBIG)
- Phone number validation (Philippine mobile format)
- Past date validation - Prevents backdating leave applications
- Date range validation - End date cannot be before start date

Leave Management
- Complete leave application workflow
- 12-point validation system for leave submissions
- Leave history tracking with status filter (Pending/Approved/Rejected/All)
- "Processed by" column showing HR approver/rejector
- Text wrapping for long leave reasons
- Audit trail for compliance and accountability

Data Integrity
- CSV-based persistence with proper parsing
- Employee number uniqueness validation
- Duplicate header detection and prevention
- Automatic CSV generation if files missing
- Transaction-safe data updates

User Experience
- Modern Swing UI with consistent color scheme
- Centered and bold table headers
- Mouse-enabled row selection
- Calendar widget for date selection (JCalendar)
- Responsive layouts with proper sizing

Troubleshooting

Issue: "Cannot find employees.csv"
Solution: 
- Verify all CSV files are in project root (same level as `src/` and `build.xml`)
- Files should be: `employees.csv`, `users.csv`, `leaves.csv`, `payslips.csv`, `attendance.csv`

Issue: "JCalendar library not found"
Solution:
1. Right-click project → Properties
2. Go to Libraries → Compile tab
3. Click "Add JAR/Folder"
4. Navigate to `lib/jcalendar-1/jcalendar-1.4.jar`
5. Click Open → OK

Issue: "Build failed - package does not exist"
Solution:
- Right-click project → Clean and Build
- Verify all packages (dao, model, service, util, view) exist in `src/`

Issue: "Login fails with correct credentials"
Solution: 
- Verify `users.csv` is present and not corrupted
- Check for duplicate header rows in CSV
- Ensure username/password are lowercase (case-sensitive)

Issue: "Date picker not showing"
Solution:  
- Verify JCalendar library is added to project
- Check `lib/jcalendar-1/jcalendar-1.4.jar` exists
- Rebuild project after adding library

Objective: Demonstrate mastery of Object-Oriented Programming principles:
- Encapsulation - Private fields with public getters/setters
- Abstraction - Abstract Employee class, Interface-based services
- Inheritance - Employee subclasses (HR, Finance, IT, etc.)
- Polymorphism - Role-based operation interfaces
