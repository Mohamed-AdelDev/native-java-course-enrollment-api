Course Enrollment Management System

A native Java HTTP API for managing students, courses, enrollments, payments, authentication, authorization, audit logs, and enrollment confirmation events.

Technologies

• Java
• Java HTTP Server (com.sun.net.httpserver.HttpServer)
• In-memory repositories
• JSON request/response processing
• HTTP REST-style endpoints
• Git and GitHub

Project Structure

```text
src/
└── com/coursemanagement/
    ├── dto/
    │   ├── request/
    │   └── response/
    ├── event/
    ├── exception/
    ├── handler/
    ├── listener/
    ├── model/
    │   └── enums/
    ├── notification/
    ├── repository/
    │   └── implentation/
    ├── security/
    ├── service/
    │   ├── discount/
    │   ├── payment/
    │   │   ├── command/
    │   │   └── processor/
    │   └── validation/
    └── util/
```

Running the Application

Run the main application class.

The server starts on:

```text
http://localhost:8080
```

Health check:

```http
GET /api/health
```

Example response:

```json
{
  "status": "UP",
  "application": "Course Enrollment API"
}
```

Authentication

Login:

```http
POST /api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "student@test.com",
  "password": "123456"
}
```

Admin test account:

```text
Email: admin@test.com
Password: 123456
```

Student test account:

```text
Email: student@test.com
Password: 123456
```

Authenticated requests use:

```text
Authorization: Bearer <accessToken>
```

API Endpoints

Students

```text
GET    /api/students
GET    /api/students/{id}
POST   /api/students
```

Create student:

```json
{
  "fullName": "John Doe",
  "email": "john@test.com",
  "password": "123456"
}
```

Courses

```text
GET    /api/courses
GET    /api/courses/{id}
POST   /api/courses
PUT    /api/courses/{id}
PATCH  /api/courses/{id}/status
DELETE /api/courses/{id}
```

Course filtering supports:

```text
status
title
minPrice
maxPrice
sort
```

Example:

```text
GET /api/courses?status=ACTIVE&minPrice=100&maxPrice=500&sort=price
```

Enrollments

```text
POST   /api/enrollments
GET    /api/enrollments
GET    /api/enrollments/{id}
GET    /api/students/{studentId}/enrollments
DELETE /api/enrollments/{id}
```

Create enrollment:

```json
{
  "studentId": 2,
  "courseId": 1,
  "discountType": "STUDENT"
}
```

Payments

```text
POST /api/enrollments/{enrollmentId}/payments
```

Example:

```json
{
  "paymentMethod": "CARD",
  "paymentReference": "TXN-1001"
}
```

Audit Logs

Admin access:

```text
GET /api/audit-logs
GET /api/audit-logs?entityType=ENROLLMENT
```

Error Handling

The API provides centralized error handling through GlobalExceptionHandler.

Supported responses include:

```text
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
405 Method Not Allowed
500 Internal Server Error
```

Example:

```json
{
  "timestamp": "2026-01-01T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid JSON",
  "path": "/api/enrollments",
  "details": null
}
```

Design Patterns

The project demonstrates several design patterns:

• Facade
• Observer
• Strategy
• Factory
• Adapter
• Command
• Template Method
• Singleton

Facade

EnrollmentFacade coordinates enrollment and payment operations.

Observer

EventPublisher publishes enrollment confirmation events to registered listeners.

Strategy

Discount calculation is handled through different discount strategies.

Factory

Factories select the appropriate discount and payment implementations.

Adapter

Payment gateway adapters provide a common interface for different payment methods.

Command

Payment commands encapsulate payment operations.

Template Method

Payment processors share common processing logic through an abstract processor.

Singleton

EventPublisher uses a singleton instance.

Validation

Enrollment validation includes:

• Student existence
• Course existence
• Course active status
• Duplicate enrollment checking
• Seat availability

Git Branches

The project uses feature branches for development.

Important branches:

```text
main
feature/project-setup
feature/error-handling
feature/observer-pattern
feature/enrollment-facade
feature/audit-logs
feature/api-documentation
```

Git History

The project includes commits for:

```text
Initialize native Java project
Add Java project gitignore
Update .gitignore
Implement repository layer with in-memory storage
Implement DTOs, mappers and service layer
Clean up Application class
Implement native HTTP server and JSON utilities
Complete Assignment 6: JSON request and response processing
Implement authentication and authorization
Implement global error handling
```

Notes

The application currently uses in-memory repositories, so data is reset whenever the application restarts.

The server is implemented using Java’s built-in HTTP server without Spring Boot or external web frameworks.