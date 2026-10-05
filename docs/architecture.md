# System Architecture & Technical Design

## 1. Architectural Overview

The **Assignment & Submission Management System** is engineered following the enterprise **Model-View-Controller (MVC) Architectural Pattern**, adhering to Jakarta EE 10 web standards.

```
+-------------------------------------------------------------------------+
|                              CLIENT TIER                                |
|   HTML5 / CSS3 / Vanilla JavaScript / Fetch API / DOMParser / XML View  |
+-------------------------------------------------------------------------+
                                     |
                          HTTP / HTTPS Requests
                                     |
                                     v
+-------------------------------------------------------------------------+
|                              FILTER TIER                                |
|   CharacterEncodingFilter (UTF-8) -> AuthenticationFilter (Role Auth)    |
+-------------------------------------------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                           CONTROLLER TIER                               |
|        Jakarta Servlets (Login, Student, Faculty, APIs, XML)            |
+-------------------------------------------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                             SERVICE TIER                                |
|   Business Logic (Deadlines, Late Checks, Auth, Grades, Notifications)   |
+-------------------------------------------------------------------------+
                                     |
                                     v
+-------------------------------------------------------------------------+
|                               DAO TIER                                  |
|     Data Access Objects (PreparedStatements, Transactions, Closeable)   |
+-------------------------------------------------------------------------+
                                     |
                                    JDBC
                                     |
                                     v
+-------------------------------------------------------------------------+
|                              DATA TIER                                  |
|             MySQL 8.0+ Database (InnoDB, UTF-8mb4, Indexes)             |
+-------------------------------------------------------------------------+
```

---

## 2. Layer Separation of Responsibilities

### Model Tier (`com.college.assignment.model`)
- Plain Old Java Objects (POJOs) with private fields, standard constructors, getters, setters, and `Serializable` interface.
- Holds in-memory representations of domain entities (`User`, `Subject`, `Assignment`, `Submission`, `Evaluation`, `Notification`, and dashboard statistic containers).

### View Tier (`src/main/webapp/WEB-INF/views`)
- JavaServer Pages (JSP 3.1) and Jakarta Standard Tag Library (JSTL 3.0).
- Pure presentation layer: **Strictly 0 SQL queries, 0 direct database connections, and 0 raw business logic**.
- Reusable UI fragments (`header.jsp`, `navbar.jsp`, `sidebar.jsp`, `footer.jsp`, `alerts.jsp`) included via `<jsp:include>`.
- Client-side DOM parsing and asynchronous AJAX calls.

### Controller Tier (`com.college.assignment.controller`)
- Jakarta HTTP Servlets annotated with `@WebServlet` and `@MultipartConfig`.
- Receives HTTP `GET`/`POST` requests, extracts and validates request parameters, calls the appropriate Service methods, sets request/session attributes, and forwards to JSPs or returns JSON/XML.

### Service Tier (`com.college.assignment.service`)
- Centralized business logic, application rules, and cross-cutting workflow orchestration:
  - Late submission detection by evaluating server timestamps against assignment deadlines.
  - Password hashing and verification using BCrypt.
  - Marks range validation (`0 <= marks <= maxMarks`).
  - Automated notification dispatching across students and faculty.
  - Multipart file uploading with extension and size safety checks.

### DAO Tier (`com.college.assignment.dao`)
- Data Access Objects handling all persistent CRUD operations against MySQL.
- Strict use of `java.sql.PreparedStatement` to prevent SQL Injection attacks.
- Robust resource management using try-with-resources.

### Utility Tier (`com.college.assignment.util`)
- `DatabaseConnection`: Thread-safe JDBC connection factory supporting `db.properties` and environment variables.
- `PasswordUtil`: BCrypt password hashing and salt round enforcement.
- `ValidationUtil`: Server-side input sanitization, regex matching, and XSS protection.
- `FileUtil`: Safe server filename generation and file format validation.
- `DateUtil`: Deadline calculations, overdue determinations, and human-friendly time labels.
- `XmlUtil`: XML generation and entity escaping.

---

## 3. Web Technology Laboratory Concepts Demonstrated

| Lab Concept | Implementation in this Project |
|---|---|
| **1. JavaScript** | Live form validation, dynamic confirmation modals, character counting, client-side file size and extension checks. |
| **2. Jakarta Servlet** | Controller routing, file upload processing via `@MultipartConfig`, JSON/XML output streams. |
| **3. JSP & JSTL** | Presentation templates using `<c:forEach>`, `<c:choose>`, `<c:if>`, `<jsp:include>`, and Expression Language (`${}`). |
| **4. Cookies & Sessions** | `HttpSession` for role-based authentication state; `rememberedUsername` cookie with `HttpOnly` flags for Remember Me. |
| **5. JDBC Connectivity** | Direct JDBC `DriverManager` connectivity with `PreparedStatement`, transaction batching, and `ResultSet` mapping. |
| **6. AJAX Form Validation** | Live verification for student registration (email and register number availability) without full page reload. |
| **7. XML Data Exchange** | Server-side `/api/assignments.xml` generation & client-side XML DOMParser HTML table visualization. |
| **8. Database Application** | Full relational MySQL database with foreign keys, cascading deletions, and performance indexes. |
| **9. E-Commerce Style Workflow** | Lifecycle state transitions: Course Selection -> Assignment Assignment -> Student Submission -> Faculty Grading -> Result Publishing. |
| **10. Web Testing** | JUnit 5 unit tests for core utilities and 25+ comprehensive manual test procedures. |
