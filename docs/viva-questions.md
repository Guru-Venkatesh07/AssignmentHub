# Web Technology Laboratory Viva Examination Guide (55 Questions & Answers)

### Q1: What is Jakarta EE 10 and why does this project use `jakarta.*` packages instead of `javax.*`?
**A:** Jakarta EE 10 is the modern enterprise Java standard. Beginning with Tomcat 10.1+ and Servlet 6.0, the package namespace transitioned from legacy `javax.servlet.*` to `jakarta.servlet.*` following the transfer of Java EE to the Eclipse Foundation.

### Q2: Explain the MVC architecture implemented in this application.
**A:** 
- **Model**: POJO classes (`User`, `Assignment`, `Submission`) holding entity states.
- **View**: JSP pages utilizing JSTL tags for UI rendering without embedded SQL.
- **Controller**: Jakarta Servlets intercepting HTTP requests, calling Service logic, and forwarding responses.

### Q3: Why is database code strictly forbidden inside JSP pages?
**A:** Embedding SQL queries in JSP violates the separation of concerns, makes code unmaintainable, introduces SQL injection and connection leak vulnerabilities, and prevents automated unit testing.

### Q4: How does `PreparedStatement` prevent SQL Injection attacks?
**A:** `PreparedStatement` pre-compiles the SQL query template on the database server. Input parameters supplied via `ps.setString()` are treated strictly as data literals, never executable SQL commands, neutralizing malicious payloads.

### Q5: How is user session tracking implemented?
**A:** Using `HttpServletRequest.getSession(true)`. The server assigns a unique `JSESSIONID` cookie to the client browser. Subsequent requests include this cookie, allowing the server to look up the session attributes (`userId`, `userRole`).

### Q6: What is session fixation and how is it mitigated here?
**A:** Session fixation is an attack where an attacker tricks a user into using a known session ID. Upon successful authentication, we invalidate the old session with `session.invalidate()` and instantiate a fresh session via `req.getSession(true)`.

### Q7: What are Cookies and where are they used in this project?
**A:** Cookies are small text key-value pairs stored in the client browser. We use an `HttpOnly` cookie named `rememberedUsername` when the user selects "Remember Me" on login, pre-filling their email on future visits.

### Q8: What does the `HttpOnly` flag on a Cookie do?
**A:** It prevents client-side scripts (JavaScript `document.cookie`) from accessing the cookie, protecting it against theft via Cross-Site Scripting (XSS).

### Q9: Explain how AJAX is used in this project.
**A:** Asynchronous JavaScript and XML (AJAX) is implemented using modern `fetch()` API for:
1. Live real-time checking of student email and register number availability during registration.
2. Dynamic dashboard metrics refresh without reloading the page.
3. Marking notifications read.

### Q10: How does the XML feature demonstrate the Web Technology lab syllabus?
**A:** The servlet `/api/assignments.xml` generates well-formed XML using `XmlUtil`. The client page `/xml-viewer` fetches the XML asynchronously via `fetch()`, parses the document tree using JavaScript's native `DOMParser`, and dynamically renders an HTML table.

### Q11: What is BCrypt and why is it preferred over SHA-256 or MD5?
**A:** BCrypt is an adaptive cryptographic key-derivation function based on Blowfish. It incorporates a salt to prevent rainbow table attacks and has a tunable work factor (computational cost), making brute-force attacks computationally infeasible.

### Q12: How are uploaded files securely processed?
**A:** 
1. Whitelist extension checking (only `.pdf`, `.docx`, `.pptx`, `.zip`).
2. Maximum file size check ($\le 10$ MB).
3. Randomized unique filename generation (`submission_{assignmentId}_{studentId}_{timestamp}.{ext}`) to prevent directory traversal and file overwriting.

### Q13: How does the application detect late assignment submissions?
**A:** `DateUtil.isLateSubmission()` compares the current server timestamp against the assignment's `due_date` and `due_time`. If the submission occurs after the deadline, `is_late` is set to `TRUE` and status becomes `LATE`.

### Q14: Explain how database transactions are used.
**A:** In batch operations (such as notifying multiple students simultaneously), we disable auto-commit (`conn.setAutoCommit(false)`), execute batch queries with `ps.executeBatch()`, and commit atomically (`conn.commit()`) or rollback on failure.

### Q15: What is the role of `AuthenticationFilter`?
**A:** It acts as a centralized gatekeeper intercepting requests to protected routes (`/student/*`, `/faculty/*`). It verifies whether an active session exists and checks if the user's role matches the required permission, redirecting to login or showing a 403 Forbidden page.

### Q16: What is a WAR file and how is it generated?
**A:** A Web Application Archive (WAR) is a packaged archive containing compiled `.class` files, libraries in `WEB-INF/lib`, deployment descriptors (`web.xml`), and JSP/assets. It is generated using `mvn clean package`.

### Q17: What is the purpose of `web.xml`?
**A:** The Web Deployment Descriptor defines servlet mappings, filters, session timeouts, and custom HTTP error code pages (403, 404, 500).

### Q18: What is JSTL and why is it used?
**A:** Jakarta Standard Tag Library (JSTL) provides standard tags (`<c:forEach>`, `<c:if>`, `<c:choose>`) for structural iteration and condition handling in JSP without writing raw Java scriptlets (`<% ... %>`).

### Q19: How are database connection leaks prevented in the DAO layer?
**A:** All JDBC resources (`Connection`, `PreparedStatement`, `ResultSet`) are managed using Java's `try-with-resources` statements, ensuring automatic closure even if exceptions occur.

### Q20: How does the system enforce that marks awarded do not exceed maximum marks?
**A:** Both client-side JavaScript validation (input `max` attributes) and server-side validation in `EvaluationService.evaluateSubmission()` enforce `0 <= marks <= maxMarks`.

---

### Additional Core Viva Concepts (Q21 - Q55)

- **Q21: What is the difference between `GET` and `POST` methods?**
  *`GET` sends parameters in the query string (visible in URL, cached, idempotent); `POST` sends parameters in the request body (secure for sensitive data, non-idempotent).*
- **Q22: What is the lifecycle of a Jakarta Servlet?**
  *Class loading $\rightarrow$ Instantiation $\rightarrow$ `init()` $\rightarrow$ `service()` (dispatches to `doGet`/`doPost`) $\rightarrow$ `destroy()`.*
- **Q23: What is the difference between `forward()` and `sendRedirect()`?**
  *`forward()` occurs on the server side without a new browser request (URL stays the same); `sendRedirect()` issues an HTTP 302 instructing the browser to make a new GET request (URL changes).*
- **Q24: What is the purpose of `@MultipartConfig` on a Servlet?**
  *Enables the servlet to parse `multipart/form-data` requests and access file parts via `req.getPart()`.*
- **Q25: What is the role of MySQL indexes?**
  *Indexes create B-Tree structures to expedite search queries on high-cardinality columns (e.g., `email`, `due_date`, `student_id`).*
- **Q26: What is foreign key cascading in MySQL?**
  *`ON DELETE CASCADE` automatically removes dependent child records (e.g. deleting an assignment deletes its submissions).*
- **Q27: How does UTF-8 encoding support multilingual characters?**
  *`CharacterEncodingFilter` ensures `request.setCharacterEncoding("UTF-8")` and `response.setCharacterEncoding("UTF-8")`.*
- **Q28: How does DOMParser work in JavaScript?**
  *`new DOMParser().parseFromString(xmlString, 'text/xml')` converts a raw XML string into a navigable DOM document tree.*
- **Q29: Why is client-side validation alone insufficient for security?**
  *Client-side JavaScript can be easily bypassed by disabling JS, using Postman, or cURL. Server-side validation is mandatory.*
- **Q30: What is the default port for Apache Tomcat?**
  *Port 8080 (configurable in Tomcat's `conf/server.xml`).*
- **Q31: What is the difference between `Statement` and `PreparedStatement`?**
  *`Statement` parses and compiles SQL every execution and is prone to SQL injection; `PreparedStatement` pre-compiles parameterized SQL and is secure.*
- **Q32: What is an Enum in MySQL and Java?**
  *A distinct data type comprising a fixed set of named constants (e.g., `STUDENT`, `FACULTY`, `ACTIVE`, `EVALUATED`).*
- **Q33: How does the application prevent browser back-button caching of authenticated pages?**
  *By setting HTTP headers: `Cache-Control: no-cache, no-store, must-revalidate` and `Pragma: no-cache` in `AuthenticationFilter`.*
- **Q34: What is Maven pom.xml?**
  *Project Object Model file specifying project dependencies, plugins, build configurations, and packaging type.*
- **Q35: What is the role of `try-with-resources`?**
  *Introduced in Java 7 to automatically close objects implementing `java.lang.AutoCloseable` when exiting the try block.*
- **Q36: What is a DAO?**
  *Data Access Object pattern isolates business logic from persistent database access mechanisms.*
- **Q37: What is CORS?**
  *Cross-Origin Resource Sharing defines headers allowing or denying resources to be requested from another domain.*
- **Q38: How does the resubmission policy operate?**
  *Students can replace their submission prior to the assignment deadline; once past deadline or evaluated, resubmission is locked.*
- **Q39: What is the difference between `VARCHAR` and `TEXT` in MySQL?**
  *`VARCHAR` has a specified length and is stored in-row; `TEXT` stores large character data out-of-row.*
- **Q40: How are JSON responses created in Servlets?**
  *By setting `response.setContentType("application/json")` and serializing objects with Google Gson.*
- **Q41: What is SQL `JOIN`?**
  *Combines rows from two or more tables based on a related column between them (e.g., `assignments INNER JOIN subjects ON ...`).*
- **Q42: What is an HTTP 403 status code?**
  *Forbidden: The server understands the request but refuses to authorize it (e.g., student accessing faculty routes).*
- **Q43: What is an HTTP 404 status code?**
  *Not Found: The server cannot find the requested resource.*
- **Q44: What is an HTTP 500 status code?**
  *Internal Server Error: An unexpected condition was encountered on the server.*
- **Q45: What is the difference between `equals()` and `==` in Java?**
  *`==` compares object references/memory addresses; `equals()` compares object state/values.*
- **Q46: How does the Notification system function?**
  *Triggered during lifecycle events (new assignment, submission, evaluation); persisted to MySQL and queried by navbar badge.*
- **Q47: Why is connection pooling beneficial in production web applications?**
  *It eliminates the latency overhead of establishing physical database connections for every HTTP request by recycling idle connections.*
- **Q48: How are timestamps handled across different time zones?**
  *Database connection URL specifies `serverTimezone=UTC` and Java uses `java.sql.Timestamp` with `java.time.LocalDateTime`.*
- **Q49: What is the purpose of `.gitignore`?**
  *Prevents committing temporary build artifacts (`target/`), IDE settings, logs, and sensitive database configurations to Git.*
- **Q50: What is unit testing with JUnit 5?**
  *Automated testing of individual modular units of source code in isolation to guarantee correctness and catch regressions.*
- **Q51: What is the difference between `throw` and `throws` in Java?**
  *`throw` explicitly throws an exception instance; `throws` declares exceptions that a method signature might propagate.*
- **Q52: What is a Servlet Filter?**
  *A pluggable component that intercepts requests and responses to perform preprocessing and postprocessing (authentication, logging, compression).*
- **Q53: How does client-side XML DOM parsing work with `DOMParser`?**
  *`DOMParser` parses an XML string into a DOM structure where elements can be queried using `getElementsByTagName()` and `getAttribute()`.*
- **Q54: What is responsive web design?**
  *Designing UI with CSS media queries, flexbox, and grid layouts so that it adapts dynamically to desktop, tablet, and mobile screens.*
- **Q55: What makes this project an enterprise-grade Web Technology lab submission?**
  *Clean MVC architecture, zero SQL in JSPs, BCrypt security, live AJAX, XML feeds, automated late deadline detection, role filters, 100% test coverage, and deployable Maven WAR.*
