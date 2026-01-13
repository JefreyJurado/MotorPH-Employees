# MotorPH Employee Management System

A modern Java Swing application for managing employee records with full CRUD operations and OOP architecture.

## Features

✅ **Complete CRUD Operations** - Create, Read, Update, Delete employees
✅ **CSV Data Persistence** - All data saved to CSV files
✅ **Modern UI Design** - Clean, professional interface
✅ **User Authentication** - Secure login system
✅ **Salary Calculator** - Compute employee salaries
✅ **Leave Management** - Submit and track leave applications
✅ **Search Functionality** - Query employees by number

## OOP Principles Applied

- **Encapsulation** - Private fields with getters/setters
- **Single Responsibility** - Each class has one clear purpose
- **Abstraction** - Interface-based repository pattern
- **Separation of Concerns** - Model-View-Service architecture

## Project Structure
```
ms1cp2manual.refactored/
├── Model Layer
│   ├── Employee.java
│   └── LeaveApplication.java
├── Service Layer
│   ├── AuthenticationService.java
│   ├── EmployeeRepository.java
│   ├── IEmployeeRepository.java
│   ├── SalaryCalculator.java
│   └── CSVFileHandler.java
└── UI Layer
    ├── ModernLoginDialog.java
    ├── ModernEmployeeManagementFrame.java
    ├── ModernViewEmployeeDialog.java
    └── ModernLeaveApplicationDialog.java
```

## Technologies Used

- Java SE
- Swing (GUI)
- CSV File Handling

## How to Run

1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
```

2. Open in NetBeans IDE

3. Run `MainApplication.java`

4. Login credentials:
   - Username: `aljohn` / Password: `aljohn123`
   - Username: `michael` / Password: `michael123`

## Screenshots

![Login Screen](screenshots/login.png)
![Main Dashboard](screenshots/dashboard.png)

## Author

Your Name - [GitHub Profile](https://github.com/YOUR_USERNAME)

## License

This project is for educational purposes.
