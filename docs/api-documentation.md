# API & XML Endpoints Documentation

## 1. Asynchronous AJAX JSON Endpoints

### 1.1 Verify Email Availability
- **URL**: `/api/check-email`
- **Method**: `GET`
- **Authentication**: None (Public)
- **Parameters**: `email` (string, required)
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "email": "student@example.com",
  "available": false,
  "message": "Email is already registered."
}
```

---

### 1.2 Verify Register Number Availability
- **URL**: `/api/check-register-number`
- **Method**: `GET`
- **Authentication**: None (Public)
- **Parameters**: `registerNumber` (string, required)
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "registerNumber": "23CS088",
  "available": true,
  "message": "Register Number is available."
}
```

---

### 1.3 Fetch Student Dynamic Dashboard Statistics
- **URL**: `/api/student/dashboard`
- **Method**: `GET`
- **Authentication**: `STUDENT` Session Required
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "totalAssignments": 10,
  "submitted": 4,
  "pending": 6,
  "evaluated": 3,
  "late": 1,
  "averageMarks": 89.5,
  "unreadNotifications": 2
}
```

---

### 1.4 Fetch Faculty Dynamic Dashboard Statistics
- **URL**: `/api/faculty/dashboard`
- **Method**: `GET`
- **Authentication**: `FACULTY` Session Required
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "assignmentsCreated": 8,
  "totalSubmissions": 24,
  "pendingEvaluations": 4,
  "evaluatedSubmissions": 20,
  "lateSubmissions": 2,
  "totalSubjectsAssigned": 3,
  "unreadNotifications": 1
}
```

---

### 1.5 Unread Notifications Count
- **URL**: `/api/notifications/count`
- **Method**: `GET`
- **Authentication**: Logged-In User Session Required
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "unreadCount": 3
}
```

---

### 1.6 Mark Notification As Read
- **URL**: `/api/notifications/read`
- **Method**: `POST`
- **Authentication**: Logged-In User Session Required
- **Parameters**: `id` (integer, required)
- **Response Format**: `application/json`
- **Response Example**:
```json
{
  "success": true,
  "unreadCount": 2
}
```

---

### 1.7 Search & Filter Assignments Asynchronously
- **URL**: `/api/assignments/search`
- **Method**: `GET`
- **Authentication**: Optional
- **Parameters**:
  - `query` (string, optional)
  - `subjectId` (integer, optional)
  - `status` (string, optional: `PENDING`, `SUBMITTED`, `EVALUATED`, `LATE`, `ALL`)
- **Response Format**: `application/json`
- **Response Example**:
```json
[
  {
    "id": 1,
    "subjectId": 1,
    "facultyId": 1,
    "title": "Database Normalization & Dependency Theory",
    "description": "Explain First, Second, Third Normal Form...",
    "dueDate": "2026-10-25",
    "dueTime": "23:59:00",
    "maxMarks": 100,
    "status": "ACTIVE",
    "subjectName": "Database Management Systems",
    "subjectCode": "CS8501",
    "facultyName": "Dr. Rajesh Kumar",
    "studentSubmissionStatus": "EVALUATED",
    "studentMarks": 94
  }
]
```

---

## 2. XML Endpoints (Web Technology Lab Syllabus)

### 2.1 Course Assignments XML Feed
- **URL**: `/api/assignments.xml`
- **Method**: `GET`
- **Authentication**: Public / Session
- **Response Format**: `application/xml`
- **XML Structure**:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<assignments total="2">
    <assignment id="1">
        <title>Database Normalization &amp; Dependency Theory</title>
        <subjectCode>CS8501</subjectCode>
        <subjectName>Database Management Systems</subjectName>
        <facultyName>Dr. Rajesh Kumar</facultyName>
        <description>Explain First, Second, Third Normal Form (1NF, 2NF, 3NF)...</description>
        <dueDate>2026-10-25</dueDate>
        <dueTime>23:59:00</dueTime>
        <maxMarks>100</maxMarks>
        <status>ACTIVE</status>
    </assignment>
    <assignment id="2">
        <title>TCP/IP Protocol Suite &amp; Wireshark Packet Analysis</title>
        <subjectCode>CS8502</subjectCode>
        <subjectName>Computer Networks</subjectName>
        <facultyName>Dr. Rajesh Kumar</facultyName>
        <description>Capture HTTP, TCP 3-way handshake traffic...</description>
        <dueDate>2026-10-28</dueDate>
        <dueTime>23:59:00</dueTime>
        <maxMarks>100</maxMarks>
        <status>ACTIVE</status>
    </assignment>
</assignments>
```

---

### 2.2 Academic Curriculum & Timetable XML Feed
- **URL**: `/api/timetable.xml`
- **Method**: `GET`
- **Authentication**: Public
- **Response Format**: `application/xml`
- **XML Structure**:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<curriculum totalCourses="2">
    <subject id="1">
        <code>CS8501</code>
        <name>Database Management Systems</name>
        <semester>5</semester>
        <department>Computer Science &amp; Engineering</department>
    </subject>
    <subject id="2">
        <code>CS8502</code>
        <name>Computer Networks</name>
        <semester>5</semester>
        <department>Computer Science &amp; Engineering</department>
    </subject>
</curriculum>
```
