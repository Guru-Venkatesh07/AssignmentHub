# Assignment & Submission Management System
> **Subtitle**: Web-Based Academic Assignment and Evaluation Platform  
> **Target Environment**: Apache Tomcat 10.1+, Java 17+, MySQL 8.0+, Jakarta EE 10, Maven

---

## 1. Project Overview

The **Assignment & Submission Management System** is a complete, enterprise-grade, full-stack college web application designed to digitize and streamline the academic assignment lifecycle between **Students** and **Faculty**.

The application strictly implements **ONLY TWO USER ROLES**:
1. **STUDENT**: View assignments, download attachments, submit deliverables (PDF, DOCX, PPTX, ZIP), track deadlines, monitor late statuses, view evaluation marks, and inspect faculty feedback.
2. **FACULTY**: Create, edit, and manage assignments with due dates and maximum marks; download student deliverables; award scores; provide constructive academic feedback; and monitor real-time class performance statistics.

---

## 2. Web Technology Laboratory Syllabus Alignment

This project natively demonstrates all 10 standard Web Technology laboratory experiments within a cohesive real-world academic platform:

| # | Lab Concept | Project Demonstration |
|---|---|---|
| **1** | **Client-Side Scripting (JavaScript)** | Real-time form validation, password strength matching, character counters, file extension and size checks, and modal dialogues. |
| **2** | **Java Servlet** | Jakarta HTTP Controllers (`@WebServlet`) routing requests, managing multipart uploads via `@MultipartConfig`, and rendering JSON/XML streams. |
| **3** | **JavaServer Pages (JSP)** | Modern JSP 3.1 views with JSTL 3.0 tags (`<c:forEach>`, `<c:if>`, `<c:choose>`) and reusable UI fragments (`<jsp:include>`). **Zero SQL queries or business logic in JSPs**. |
| **4** | **Cookies & Session Management** | `HttpSession` for role-based authentication and navigation security; `HttpOnly` cookie for the "Remember Me" username feature. |
| **5** | **JDBC Database Connectivity** | Thread-safe `DatabaseConnection` factory, `PreparedStatement` parameterized queries, and transaction batching. |
| **6** | **AJAX Form Validation** | Asynchronous live checking of student email and register number availability during registration (`fetch('/api/check-email')`) without full page reloads. |
| **7** | **XML-Based Data Exchange** | Server-side `/api/assignments.xml` and `/api/timetable.xml` generation and an interactive `/xml-viewer` page using JavaScript `DOMParser` to render responsive HTML tables. |
| **8** | **Database-Driven Web Application** | Normalized MySQL relational database with foreign key constraints, cascading deletions, and performance indexes. |
| **9** | **Workflow & Transaction Concepts** | Academic lifecycle states: Assignment Creation $\rightarrow$ Digital File Upload $\rightarrow$ Deadline & Late Evaluation $\rightarrow$ Faculty Grading $\rightarrow$ Result & Feedback Publishing. |
| **10** | **Web Application Testing** | Automated JUnit 5 unit test suite (34 test cases) and 30 comprehensive manual test case procedures. |

---

## 3. Technology Stack

- **Backend / Platform**: Java 17+, Jakarta Servlet API 6.0, JSP 3.1, JSTL 3.0, JDBC
- **Application Server**: Apache Tomcat 10.1+ (Jakarta EE 10 namespace)
- **Database**: MySQL Server 8.0+ & MySQL Workbench
- **Security & Utilities**: BCrypt (`jbcrypt:0.4`), Google Gson (`gson:2.10.1`)
- **Frontend**: HTML5, CSS3 (Modern Flexbox & CSS Grid, responsive design), Vanilla JavaScript (ES6+), Fetch API, Font Awesome 6.5
- **Build & Packaging**: Apache Maven (WAR packaging)
- **Testing**: JUnit 5 (Jupiter)

---

## 4. System Architecture

The application strictly follows the **Model-View-Controller (MVC)** design pattern:

```
Browser (HTML5, CSS3, JavaScript, Fetch API, DOMParser)
    |
    v
CharacterEncodingFilter & AuthenticationFilter (UTF-8 & Role Auth)
    |
    v
Jakarta Servlet Controllers (Login, Student, Faculty, APIs, XML)
    |
    v
Service Tier (Business Logic, Deadlines, Late Checks, Hashing, Notifications)
    |
    v
DAO Layer (PreparedStatements, try-with-resources, Transactions)
    |
    v
JDBC Driver (MySQL Connector/J)
    |
    v
MySQL 8.0 Database (assignment_manager)
```

---

## 5. Project Directory Structure

```
Assignment_Submission/
├── pom.xml                               # Maven Project Descriptor
├── README.md                             # Comprehensive Documentation
├── .gitignore                            # Git Exclusion Rules
│
├── database/
│   ├── schema.sql                        # MySQL Table DDL & Indexes
│   ├── seed.sql                          # Realistic College Demo Data
│   └── reset.sql                         # Database Reset & Re-seed Script
│
├── docs/
│   ├── architecture.md                   # MVC & Architecture Deep Dive
│   ├── database-design.md                # Relational ERD & Data Dictionary
│   ├── api-documentation.md              # AJAX & XML Endpoints Guide
│   ├── security.md                       # BCrypt, Sessions, & File Security
│   ├── testing.md                        # Comprehensive 30-Scenario Test Plan
│   ├── deployment.md                     # Tomcat & Eclipse Setup Guide
│   └── viva-questions.md                 # 55 Web Technology Viva Q&A
│
├── src/
│   ├── main/
│   │   ├── java/com/college/assignment/
│   │   │   ├── controller/               # Jakarta Servlets & API Endpoints
│   │   │   ├── dao/                      # JDBC Data Access Objects
│   │   │   ├── filter/                   # Authentication & Encoding Filters
│   │   │   ├── model/                    # Domain POJO Classes & DTOs
│   │   │   ├── service/                  # Business Logic & Workflow Engine
│   │   │   └── util/                     # Database, BCrypt, Dates, Files, XML
│   │   │
│   │   ├── resources/
│   │   │   ├── db.properties             # Database Connection Settings
│   │   │   └── db.properties.example     # Configuration Template
│   │   │
│   │   └── webapp/
│   │       ├── assets/
│   │       │   ├── css/main.css          # Responsive Modern UI Stylesheet
│   │       │   └── js/                   # app.js, validation.js, dashboard.js, xml-parser.js
│   │       │
│   │       ├── WEB-INF/
│   │       │   ├── web.xml               # Deployment Descriptor & Error Pages
│   │       │   └── views/
│   │       │       ├── errors/           # 403.jsp, 404.jsp, 500.jsp
│   │       │       ├── faculty/          # Dashboard, Assignments, Submissions, Evaluate
│   │       │       ├── student/          # Dashboard, Assignments, Submit, Submissions, Results
│   │       │       └── fragments/        # header, navbar, sidebar, footer, alerts
│   │       │
│   │       ├── index.jsp                 # Landing Page
│   │       ├── login.jsp                 # Login Page with Demo Autofill
│   │       ├── register.jsp              # Student Registration with AJAX
│   │       └── xml-viewer.jsp            # XML Lab Demonstration Page
│   │
│   └── test/java/com/college/assignment/ # Automated JUnit 5 Unit Tests
```

---

## 6. Quick Start & Setup Instructions

### Step 1: Database Setup in MySQL Workbench
1. Open **MySQL Workbench** and connect to your local MySQL instance.
2. Open `database/schema.sql` and execute it to create the `assignment_manager` database and all tables.
3. Open `database/seed.sql` and execute it to populate demo faculty, students, subjects, assignments, submissions, and evaluations.

### Step 2: Configure Database Credentials
Verify or update credentials in `src/main/resources/db.properties`:
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/assignment_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=YOUR_PASSWORD
```

### Step 3: Run Automated Unit Tests
```bash
mvn test
```
*(All 34 automated unit test cases will execute and verify hashing, validations, date math, and XML builders).*

### Step 4: Build the Deployable WAR Package
```bash
mvn package
```
Generates the deployable archive:
```
target/assignment-submission-manager.war
```

### Step 5: Deploy to Apache Tomcat 10.1+
- Copy `target/assignment-submission-manager.war` to your Tomcat `webapps/` folder.
- Start Tomcat (`bin/startup.bat` or `bin/startup.sh`).
- Access the application in your browser:
  ```
  http://localhost:8080/assignment-submission-manager/
  ```

---

## 7. Demo User Accounts

| Role | Email | Password | Details |
|---|---|---|---|
| **Student Demo** | `student@example.com` | `student123` | Guru V (Reg No: `23CS088`), 3rd Year CSE |
| **Faculty Demo** | `faculty@example.com` | `faculty123` | Dr. Rajesh Kumar, CSE Department |

*(Passwords are securely hashed in MySQL using BCrypt).*

---

## 8. Key Features Breakdown

### Student Capabilities
- Register account with live AJAX email/register number validation.
- Login and manage active HTTP session with "Remember Me" cookie.
- View interactive dashboard with statistics (total, pending, submitted, evaluated, late, average score).
- Search, filter, and sort assignments by subject and deadline status.
- View assignment instructions and download course resource attachments.
- Upload assignment submissions (PDF, DOCX, PPTX, ZIP up to 10MB) with automatic late submission detection.
- Resubmit/replace files before deadline cutoff.
- View evaluated marks, score bars, and qualitative faculty feedback.
- Receive in-app notifications on newly posted assignments and evaluation releases.

### Faculty Capabilities
- Create assignments with subject mapping, due dates, due times, max marks, and attachments.
- Edit and delete owned assignments (with cascade deletion of dependent submissions).
- Monitor class submission counts and pending evaluation queues.
- Download student submitted deliverables.
- Grade submissions with validation ($0 \le \text{marks} \le \text{max\_marks}$) and qualitative remarks.
- Receive alerts upon student submissions and late deliveries.

---

## 9. Verification & Code Quality

- **Clean Jakarta EE 10 compliance**: Built for Tomcat 10.1+ using `jakarta.servlet.*`.
- **Zero SQL in JSPs**: All database interactions are strictly encapsulated within DAO classes.
- **PreparedStatement**: Complete defense against SQL Injection.
- **XSS & File Security**: Content escaping, extension whitelisting, and unique filename generation.
- **100% Test Suite Pass**: Automated JUnit 5 tests covering core services and utilities.
