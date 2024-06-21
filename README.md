# Javengers Assemble Employee Management System

This project is a simple Employee and Time Management System designed to allow users to manage their profile information, record timestamps, and for administrators to manage employee data and timestamps.

## Table of Contents

- [Directory Structure](#directory-structure)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [API Documentation](#api-documentation)
- [Credentials Handling](#credentials-handling)

## Directory Structure

.  
├── .idea  
├── gradle  
├── project_documents  
├── src  
│   ├── frontend  
│   │   ├── css  
│   │   └── js  
│   │       ├── change_password.html  
│   │       ├── employee_management.html  
│   │       ├── home.html  
│   │       ├── login.html  
│   │       ├── profile.html  
│   │       └── time_entries.html  
│   ├── main  
│   │   ├── java/org/example  
│   │   └── resources  
│   │       ├── API Documentation  
│   │       └── UML Files  
│   │           └── log4j2.xml  
│   └── test/java/org/example  
├── .gitattributes  
├── .gitignore  
├── README.md  
├── build.gradle  
├── gradlew  
├── gradlew.bat  
└── settings.gradle  

## Features

### User Features
- **Login**: Users can log in with their credentials.
- **Profile Management**: Users can view and update their basic profile information.
- **Timestamp Recording**: Users can record their work timestamps.
- **View Timestamps**: Users can view their recorded timestamps.

### Admin Features
- **Employee Management**: Admins can view all employees, update their information, and create new employees.
- **Timestamp Management**: Admins can view and edit timestamps for any employee.
- **Profile Management**: Admins can update basic profile information for all employees.

## Technology Stack

### Backend
- **Java**: The backend is written in Java.
- **AWS Lambda**: The backend logic is implemented as AWS Lambda functions.
- **AWS API Gateway**: Used to expose the Lambda functions as RESTful APIs.
- **DynamoDB**: Used for storing employees, timestamps, and credentials.

### Frontend
- **HTML, CSS, JavaScript**: The frontend is built using standard web technologies.

### Tools
- **Gradle**: Build automation tool.
- **Log4j2**: Logging framework.

## API Documentation

API documentation is generated using OpenAPI specifications. The documentation can be found in the [API Documentation](src/main/resources/API_Documentation/employee-management-api-docs.html) file.

### Servers Configuration (JSON)
```json
"servers" : [
    {
        "url": "https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/Prod",
        "description": "Production Server"
    },
    {
        "url": "https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/Gamma",
        "description": "Pre-Production Test Server"
    }
]
```

## Credentials Handling

### EmployeeCredentials Class

#### Methods
- **Constructor**: Initializes new or existing employee credentials.
- **updatePassword**: Updates the employee's password.
- **verifyCredentials**: Verifies the provided password against stored credentials.
- **adminResetPassword**: Resets the password by an admin.

### CredentialsUtility Class

#### Methods
- **generateSalt**: Generates a secure random salt.
- **hashPassword**: Hashes a password using the Argon2 algorithm.
- **verifyPassword**: Verifies a password by comparing it to a previously computed hash.
