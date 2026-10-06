-- ====================================================================
-- Assignment & Submission Management System
-- Seed Data for Testing, Evaluation, and Demo Presentation
-- ====================================================================

USE assignment_manager;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE notifications;
TRUNCATE TABLE evaluations;
TRUNCATE TABLE submissions;
TRUNCATE TABLE assignments;
TRUNCATE TABLE faculty_subjects;
TRUNCATE TABLE subjects;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- --------------------------------------------------------------------
-- 1. SEED USERS
-- Verified working BCrypt hashes:
-- 'student123' -> $2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m
-- 'faculty123' -> $2a$10$uCL5MCEXQMEUrQnSjEk5eOW4YDPlpY.AKA4iaK1K3NYLreeXc04rO
-- --------------------------------------------------------------------

-- Faculty accounts (id: 1, 2)
INSERT INTO users (id, name, email, password_hash, role, register_number, department, year) VALUES
(1, 'Dr. Rajesh Kumar', 'faculty@example.com', '$2a$10$uCL5MCEXQMEUrQnSjEk5eOW4YDPlpY.AKA4iaK1K3NYLreeXc04rO', 'FACULTY', NULL, 'Computer Science & Engineering', NULL),
(2, 'Prof. Priya Sharma', 'priya.sharma@example.com', '$2a$10$uCL5MCEXQMEUrQnSjEk5eOW4YDPlpY.AKA4iaK1K3NYLreeXc04rO', 'FACULTY', NULL, 'Information Technology', NULL);

-- Student accounts (id: 3, 4, 5, 6, 7)
INSERT INTO users (id, name, email, password_hash, role, register_number, department, year) VALUES
(3, 'Guru V', 'student@example.com', '$2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m', 'STUDENT', '23CS088', 'Computer Science & Engineering', 3),
(4, 'Arun Prakash', 'arun.p@example.com', '$2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m', 'STUDENT', '23CS012', 'Computer Science & Engineering', 3),
(5, 'Karthik Raja', 'karthik.r@example.com', '$2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m', 'STUDENT', '23CS045', 'Computer Science & Engineering', 3),
(6, 'Deepa Sundaram', 'deepa.s@example.com', '$2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m', 'STUDENT', '23IT022', 'Information Technology', 3),
(7, 'Naveen Kumar', 'naveen.k@example.com', '$2a$10$l0vgujO2OPhcSn5i2NnnBOZFIKHRLv8ZlA5uvheKvW.DYDyaN9T7m', 'STUDENT', '23IT067', 'Information Technology', 3);

-- --------------------------------------------------------------------
-- 2. SEED SUBJECTS (6 academic subjects)
-- --------------------------------------------------------------------
INSERT INTO subjects (id, subject_code, subject_name, semester, department) VALUES
(1, 'CS8501', 'Database Management Systems', 5, 'Computer Science & Engineering'),
(2, 'CS8502', 'Computer Networks', 5, 'Computer Science & Engineering'),
(3, 'CS8503', 'Web Technology', 5, 'Computer Science & Engineering'),
(4, 'CS8504', 'Operating Systems', 5, 'Computer Science & Engineering'),
(5, 'CS8505', 'Java & Object Oriented Programming', 5, 'Computer Science & Engineering'),
(6, 'CS8506', 'Compiler Design', 5, 'Computer Science & Engineering');

-- --------------------------------------------------------------------
-- 3. SEED FACULTY_SUBJECTS MAPPING
-- --------------------------------------------------------------------
INSERT INTO faculty_subjects (id, faculty_id, subject_id) VALUES
(1, 1, 1), -- Dr. Rajesh -> DBMS
(2, 1, 2), -- Dr. Rajesh -> Computer Networks
(3, 1, 3), -- Dr. Rajesh -> Web Technology
(4, 2, 4), -- Prof. Priya -> Operating Systems
(5, 2, 5), -- Prof. Priya -> Java OOP
(6, 2, 6); -- Prof. Priya -> Compiler Design

-- --------------------------------------------------------------------
-- 4. SEED ASSIGNMENTS (10 realistic assignments across subjects)
-- --------------------------------------------------------------------
INSERT INTO assignments (id, subject_id, faculty_id, title, description, instructions, attachment_path, due_date, due_time, max_marks, status) VALUES
(1, 1, 1, 'Database Normalization & Dependency Theory', 'Explain First, Second, Third Normal Form (1NF, 2NF, 3NF) and BCNF with real-world enterprise relation examples. Demonstrate decomposition steps.', '1. Submit in PDF format.\n2. Include diagrams showing Functional Dependencies (FDs).\n3. Maximum length 4 pages.', NULL, '2026-10-25', '23:59:00', 100, 'ACTIVE'),
(2, 2, 1, 'TCP/IP Protocol Suite & Wireshark Packet Analysis', 'Capture HTTP, TCP 3-way handshake, and DNS traffic using Wireshark. Explain packet headers and sequence numbers.', '1. Attach screenshot captures.\n2. Submit report as PDF or DOCX.\n3. Keep file size under 10 MB.', NULL, '2026-10-28', '23:59:00', 100, 'ACTIVE'),
(3, 3, 1, 'Servlet, Session & JDBC Web Application', 'Implement a complete full-stack web application using Jakarta Servlets, JSP, JDBC, and MySQL demonstrating MVC architecture.', '1. Upload source archive ZIP or project PDF report.\n2. Follow clean package hierarchy.', NULL, '2026-11-05', '23:59:00', 100, 'ACTIVE'),
(4, 4, 2, 'Process Scheduling Algorithms Simulation', 'Compare FCFS, SJF, Round Robin, and Priority Scheduling with Gantt charts, Average Turnaround Time, and Average Waiting Time calculations.', '1. Include source code and output charts.\n2. Submit PDF format.', NULL, '2026-10-30', '23:59:00', 100, 'ACTIVE'),
(5, 5, 2, 'Java Multithreading & Producer-Consumer Problem', 'Implement synchronization using Java BlockingQueue and wait/notify mechanism. Handle thread deadlock prevention.', '1. Submit runnable code and analysis document.\n2. Explain synchronized blocks.', NULL, '2026-11-02', '23:59:00', 50, 'ACTIVE'),
(6, 6, 2, 'Lexical Analysis & YACC Parser Construction', 'Construct a lexical analyzer using Flex/Lex and parser using Bison/YACC for arithmetic expressions with operator precedence.', '1. Provide grammar rules.\n2. Show sample inputs and parsed parse trees.', NULL, '2026-11-10', '23:59:00', 100, 'ACTIVE'),
(7, 1, 1, 'SQL Complex Queries & Index Tuning', 'Write complex SQL queries involving correlated subqueries, window functions, and execute EXPLAIN ANALYZE for query optimization.', '1. Include schema DDL and query scripts.\n2. Submit PDF report.', NULL, '2026-09-20', '23:59:00', 100, 'CLOSED'),
(8, 2, 1, 'Subnetting and VLAN Configuration', 'Design a subnetting scheme for 5 departments with CIDR notation. Configure virtual LANs (VLANs) in Cisco Packet Tracer.', '1. Submit PKT file or PDF configuration report.', NULL, '2026-09-25', '23:59:00', 50, 'CLOSED'),
(9, 3, 1, 'AJAX and XML DOM Parsing in Web Applications', 'Create an interactive web page that loads XML documents asynchronously using Fetch API and renders data into responsive tables.', '1. Submit HTML/JS code and screenshots.\n2. Explain DOM parsing methods.', NULL, '2026-11-15', '23:59:00', 50, 'ACTIVE'),
(10, 5, 2, 'Design Patterns in Java (Creational & Structural)', 'Implement Factory Method, Singleton, Adapter, and Observer patterns with UML class diagrams and unit test cases.', '1. Submit PDF report with UML diagrams.', NULL, '2026-11-20', '23:59:00', 100, 'ACTIVE');

-- --------------------------------------------------------------------
-- 5. SEED SUBMISSIONS
-- --------------------------------------------------------------------
INSERT INTO submissions (id, assignment_id, student_id, file_name, file_path, submitted_at, status, is_late) VALUES
-- Student Guru (id: 3)
(1, 1, 3, 'DBMS_Normalization_Guru_23CS088.pdf', 'uploads/submissions/submission_1_3_20261001.pdf', '2026-10-01 14:30:00', 'EVALUATED', FALSE),
(2, 2, 3, 'TCP_Analysis_Guru_23CS088.pdf', 'uploads/submissions/submission_2_3_20261002.pdf', '2026-10-02 18:20:00', 'SUBMITTED', FALSE),
(3, 7, 3, 'SQL_Queries_Guru_23CS088.pdf', 'uploads/submissions/submission_7_3_20260918.pdf', '2026-09-18 11:15:00', 'EVALUATED', FALSE),
(4, 8, 3, 'Subnetting_Guru_23CS088.pdf', 'uploads/submissions/submission_8_3_20260927.pdf', '2026-09-27 10:00:00', 'EVALUATED', TRUE),

-- Student Arun (id: 4)
(5, 1, 4, 'DBMS_Normalization_Arun.pdf', 'uploads/submissions/submission_1_4_20261003.pdf', '2026-10-03 16:45:00', 'EVALUATED', FALSE),
(6, 2, 4, 'TCP_Arun.pdf', 'uploads/submissions/submission_2_4_20261004.pdf', '2026-10-04 09:30:00', 'SUBMITTED', FALSE),

-- Student Karthik (id: 5)
(7, 1, 5, 'DBMS_Karthik.pdf', 'uploads/submissions/submission_1_5_20261004.pdf', '2026-10-04 22:10:00', 'SUBMITTED', FALSE),
(8, 4, 5, 'OS_Scheduling_Karthik.pdf', 'uploads/submissions/submission_4_5_20261005.pdf', '2026-10-05 13:00:00', 'SUBMITTED', FALSE);

-- --------------------------------------------------------------------
-- 6. SEED EVALUATIONS
-- --------------------------------------------------------------------
INSERT INTO evaluations (id, submission_id, faculty_id, marks, feedback, evaluated_at) VALUES
(1, 1, 1, 94, 'Outstanding explanation of 1NF to BCNF with clean dependency diagrams. Well structured.', '2026-10-02 10:00:00'),
(2, 3, 1, 88, 'Great optimization with window functions. Query plans well explained.', '2026-09-20 15:30:00'),
(3, 4, 1, 42, 'Good subnet calculations, but submission was 2 days late. 8 marks deducted as per college policy.', '2026-09-28 12:00:00'),
(4, 5, 1, 85, 'Solid decomposition proof. Can improve on multi-valued dependency explanations.', '2026-10-04 11:20:00');

-- --------------------------------------------------------------------
-- 7. SEED NOTIFICATIONS
-- --------------------------------------------------------------------
INSERT INTO notifications (id, user_id, title, message, type, is_read, created_at) VALUES
-- Student Guru notifications
(1, 3, 'New Assignment: Database Normalization', 'Dr. Rajesh Kumar posted a new assignment in Database Management Systems.', 'ASSIGNMENT', TRUE, '2026-10-01 09:00:00'),
(2, 3, 'Submission Confirmed', 'Your submission for Database Normalization & Dependency Theory was received.', 'SUBMISSION', TRUE, '2026-10-01 14:30:00'),
(3, 3, 'Assignment Evaluated: DBMS Normalization', 'Dr. Rajesh Kumar evaluated your submission. Marks: 94/100. Feedback: Outstanding explanation.', 'EVALUATION', FALSE, '2026-10-02 10:00:00'),
(4, 3, 'Upcoming Deadline Alert', 'TCP/IP Protocol Suite assignment is due in 3 days. Complete your submission on time.', 'DEADLINE', FALSE, '2026-10-05 08:00:00'),

-- Faculty Dr. Rajesh Kumar notifications
(5, 1, 'New Student Submission', 'Guru V (23CS088) submitted Database Normalization & Dependency Theory.', 'SUBMISSION', TRUE, '2026-10-01 14:30:00'),
(6, 1, 'New Student Submission', 'Arun Prakash (23CS012) submitted TCP/IP Protocol Suite & Wireshark Packet Analysis.', 'SUBMISSION', FALSE, '2026-10-04 09:30:00'),
(7, 1, 'Late Submission Received', 'Guru V submitted Subnetting and VLAN Configuration after the deadline.', 'SUBMISSION', TRUE, '2026-09-27 10:00:00');
