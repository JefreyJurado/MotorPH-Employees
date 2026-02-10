MotorPH Employee Management System

A comprehensive Role-Based Access Control (RBAC) employee management system demonstrating advanced Object-Oriented Programming principles through a production-quality Java application.

Project Overview

This system implements a sophisticated two-layer RBAC architecture managing MotorPH company operations including employee records, payroll processing, leave management, and attendance tracking. The application demonstrates mastery of all four OOP principles while following industry-standard 4-layer architecture pattern.

Core Features

Employee Management
- Complete CRUD operations for employee records
- Dynamic employee classification using Factory Pattern
- Support for 8 different employee types with role-specific capabilities
- 34 real MotorPH employees with comprehensive data

Role-Based Access Control (RBAC)
Two-Layer RBAC Implementation:
1. User Role Layer (8 roles)
   - SystemAdmin, Owner, HR, Finance, IT, Accounting, Executive, Employee
   - Each role has specific dashboard interface and operational permissions

2. Employee Type Layer (8 types)
   - ExecutiveEmployee, HREmployee, FinanceEmployee, ITEmployee
   - AccountingEmployee, OperationsEmployee, SalesEmployee, GeneralEmployee
   - Each type implements department-specific interfaces

Payroll Management
- Automated salary calculations with Philippine tax compliance
- SSS, PhilHealth, Pag-IBIG deductions (2024 rates)
- TRAIN Law withholding tax computation
- Weekly payslip generation with attendance integration
- Method overloading for flexible salary calculations

Leave Management
- Leave application filing with multiple leave types
- Approval workflow for HR personnel
- Leave balance tracking and history
- Status management (Pending, Approved, Rejected)

Attendance Tracking
- Time-in/time-out recording
- Overtime calculation
- Attendance-based salary computation
- Working days calculation (excludes weekends)

Modern User Interface
- Professional blue and white color scheme
- Card-based layouts with modern styling
- Role-based button visibility
- Responsive design with proper spacing

4-Layer Architecture

This system follows industry-standard 4-layer architecture pattern as specified in course guidelines:

Layer 1: Model (Domain Layer)
- Purpose: Pure domain entities with business logic
- Rule: NO file I/O, NO UI components
- Classes: Employee hierarchy, User, LeaveApplication, AttendanceRecord, Payslip
- Example: `calculateTotalAllowances()` business method

Layer 2: DAO (Data Access Object)
- Purpose: ALL file operations and data persistence
- Rule: ONLY data storage, NO business logic
- Classes: Repository classes, CSVFileHandler
- Example: `loadEmployeesFromCSV()` data operation

Layer 3: Service (Business Logic)
- Purpose: Business rules, calculations, validation
- Rule: NO file I/O, NO UI components
- Classes: SalaryCalculator, AuthenticationService, Operational Interfaces
- Example: `calculateWithholdingTax()` business calculation

Layer 4: UI (User Interface)
- Purpose: User interaction and display
- Rule: Calls SERVICE layer, NEVER directly calls DAO
- Classes: JFrame and JDialog components
- Example: User clicks button → calls Service method → Service calls DAO

Data Flow: UI → Service → DAO → Files

Architecture & OOP Principles

1. Encapsulation
- Private fields with public getters/setters - All 19 Employee fields are private with controlled access
- Repository Pattern - Data access logic encapsulated in repository classes
- Service Layer - Business logic encapsulated in dedicated service classes

2. Abstraction
- Abstract Employee Class - Defines template with abstract methods `getDepartment()` and `getJobDescription()`
- Interfaces - 6 interfaces (IEmployeeRepository, IHROperations, IFinanceOperations, IITOperations, IAccountingOperations, IExecutiveOperations)
- Layer Separation - Abstract interfaces between architectural layers

3. Inheritance
- 8 Employee Subclasses - Each inherits from abstract Employee base class
- Code Reusability - Subclasses inherit 19 fields and common methods
- IS-A Relationship - HREmployee IS-A Employee, FinanceEmployee IS-A Employee, etc.

4. Polymorphism
- Method Overriding (Runtime) - `getDepartment()`, `getJobDescription()` implemented differently by each subclass
- Method Overloading (Compile-Time) - `calculateSalary()` with 3 versions, `calculateDeductions()` with 2 versions
- Interface Polymorphism - Employee references can hold any subclass, role-based operations using instanceof
- Factory Pattern - EmployeeFactory returns Employee type, actual object can be any subclass

This project demonstrates:
- ✅ All 4 OOP Principles- Encapsulation, Abstraction, Inheritance, Polymorphism
- ✅ Multiple Polymorphism Types - Method Overriding, Method Overloading, Interface Polymorphism, Factory Pattern
- ✅ Design Patterns - Factory, Repository, DAO, Service Layer, Template Method, MVC
- ✅ 4-Layer Architecture - Complete separation of concerns (Model, DAO, Service, UI)
