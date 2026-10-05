# Comprehensive Test Plan & Test Cases

## 1. Automated Unit Test Summary

- **Framework**: JUnit 5 (Jupiter Engine)
- **Suite Execution**: `mvn test`
- **Total Test Cases Executed**: 34
- **Pass Rate**: 100% (0 Failures, 0 Errors, 0 Skipped)

---

## 2. Comprehensive Test Cases (30 Detailed Test Scenarios)

| Test ID | Feature / Module | Input Data | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| **TC-01** | Student Registration | Valid Name, Email, Reg No (`23CS999`), Dept, Year, Password | Account created, redirected to `login.jsp` with success message | Account created, success message shown | **PASS** |
| **TC-02** | Registration Duplicate Email | Email `student@example.com` | Registration rejected: "Email already exists" | Error alert displayed | **PASS** |
| **TC-03** | Registration Duplicate Reg No | Register No `23CS088` | Registration rejected: "Register number already exists" | Error alert displayed | **PASS** |
| **TC-04** | Registration Invalid Email | Email `invalid-email-string` | Rejected by client & server validation | Validation error shown | **PASS** |
| **TC-05** | Student Login | `student@example.com` / `student123` | Session created, redirected to `/student/dashboard` | Redirected to student dashboard | **PASS** |
| **TC-06** | Wrong Password | `student@example.com` / `wrongpass` | Login rejected: "Invalid email or password" | Error banner displayed | **PASS** |
| **TC-07** | Logout | Click "Logout" | `session.invalidate()`, redirected to `login.jsp` | Session destroyed, redirected | **PASS** |
| **TC-08** | Unauthorized URL Access | Direct URL `/student/dashboard` without session | Filter intercepts, redirects to `login.jsp` | Redirected to login page | **PASS** |
| **TC-09** | Faculty Login | `faculty@example.com` / `faculty123` | Session created, redirected to `/faculty/dashboard` | Redirected to faculty dashboard | **PASS** |
| **TC-10** | Role Authorization (Student on Faculty page) | Student visits `/faculty/assignments/create` | HTTP 403 Forbidden page returned | 403 page displayed | **PASS** |
| **TC-11** | Role Authorization (Faculty on Student page) | Faculty visits `/student/results` | HTTP 403 Forbidden page returned | 403 page displayed | **PASS** |
| **TC-12** | Faculty Create Assignment | Title, Subject, Due Date, Max Marks=100, Attachment | Saved in DB, students notified, redirected to list | Assignment created & listed | **PASS** |
| **TC-13** | Faculty Edit Assignment | Modify Title, Due Date, Marks | DB record updated successfully | Changes reflected | **PASS** |
| **TC-14** | Faculty Delete Assignment | Click Delete on owned assignment | Assignment and submissions deleted from DB | Assignment removed | **PASS** |
| **TC-15** | Student View Assignments | Navigate to `/student/assignments` | Displays active assignments with deadlines | Table rendered | **PASS** |
| **TC-16** | Course Attachment Download | Click "Download Resource" on assignment | File downloaded with correct filename and MIME | File downloaded | **PASS** |
| **TC-17** | Student Valid Submission | Upload `report.pdf` (2MB) before deadline | File saved to `uploads/submissions/`, DB record created, status=`SUBMITTED` | Submission recorded | **PASS** |
| **TC-18** | Invalid File Extension Upload | Upload `malicious.exe` or `hack.jsp` | File rejected: "Only PDF, DOCX, PPTX, ZIP allowed" | Error alert displayed | **PASS** |
| **TC-19** | File Size Limit (>10MB) | Upload `huge_archive.zip` (15MB) | Rejected: "File size exceeds 10MB limit" | Upload blocked | **PASS** |
| **TC-20** | Late Submission Detection | Submit assignment after due date/time | Server detects current time > due date, sets `is_late=TRUE`, status=`LATE` | Late badge displayed | **PASS** |
| **TC-21** | Student Resubmission Policy | Student uploads new file before deadline | Previous submission file replaced in storage & DB | Record updated | **PASS** |
| **TC-22** | Faculty View Submissions | Open `/faculty/submissions?assignmentId=1` | Lists all student submissions for assignment | List displayed | **PASS** |
| **TC-23** | Faculty Evaluate Submission | Award Marks=95/100, Feedback="Great work" | Evaluation stored, status=`EVALUATED`, student notified | Marks saved & alert sent | **PASS** |
| **TC-24** | Marks Range Validation | Enter Marks=120 on Max Marks=100 | Form validation rejects: "Marks cannot exceed 100" | Input rejected | **PASS** |
| **TC-25** | Student Result Viewing | Open `/student/results` | Displays marks (95/100), percentage bar, faculty feedback | Score & feedback shown | **PASS** |
| **TC-26** | AJAX Live Email Availability | Type email in registration field | `GET /api/check-email` returns JSON `{available: true/false}` | Live green/red message | **PASS** |
| **TC-27** | AJAX Live Register No. Check | Type reg no in registration field | `GET /api/check-register-number` returns JSON availability | Live indicator shown | **PASS** |
| **TC-28** | XML Feed Generation | `GET /api/assignments.xml` | Returns well-formed XML with `application/xml` header | Valid XML rendered | **PASS** |
| **TC-29** | XML Client DOM Parsing | Open `/xml-viewer`, click "Load XML" | JavaScript fetches XML, parses with `DOMParser`, renders HTML table | Responsive table loaded | **PASS** |
| **TC-30** | SQL Injection Attempt | Enter `' OR '1'='1` in email field | Parameterized `PreparedStatement` treats input as literal string; login safely fails | Login failed safely | **PASS** |
