-- ====================================================================
-- Assignment & Submission Management System
-- Web-Based Academic Assignment and Evaluation Platform
-- Database Schema for MySQL 8.0+
-- ====================================================================

CREATE DATABASE IF NOT EXISTS assignment_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE assignment_manager;

-- Disable foreign key checks during creation
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------------------------------------------------------
-- 1. USERS TABLE
-- Roles: 'STUDENT' or 'FACULTY' (Strictly no admin)
-- --------------------------------------------------------------------
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS faculty_subjects;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'FACULTY') NOT NULL,
    register_number VARCHAR(50) UNIQUE NULL,
    department VARCHAR(100) NOT NULL,
    year INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_reg_no (register_number),
    INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 2. SUBJECTS TABLE
-- Academic subjects taught in the institution
-- --------------------------------------------------------------------
CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    subject_name VARCHAR(150) NOT NULL,
    semester INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_subjects_code (subject_code),
    INDEX idx_subjects_dept_sem (department, semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 3. FACULTY_SUBJECTS TABLE
-- Maps faculty members to the subjects they teach
-- --------------------------------------------------------------------
CREATE TABLE faculty_subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faculty_id INT NOT NULL,
    subject_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    UNIQUE KEY uk_faculty_subject (faculty_id, subject_id),
    INDEX idx_faculty_sub_faculty (faculty_id),
    INDEX idx_faculty_sub_subject (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 4. ASSIGNMENTS TABLE
-- Academic assignments created by faculty
-- --------------------------------------------------------------------
CREATE TABLE assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    faculty_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    instructions TEXT,
    attachment_path VARCHAR(255) NULL,
    due_date DATE NOT NULL,
    due_time TIME NOT NULL DEFAULT '23:59:00',
    max_marks INT NOT NULL DEFAULT 100,
    status ENUM('ACTIVE', 'CLOSED', 'ARCHIVED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE RESTRICT,
    FOREIGN KEY (faculty_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_assignments_subject (subject_id),
    INDEX idx_assignments_faculty (faculty_id),
    INDEX idx_assignments_due_date (due_date),
    INDEX idx_assignments_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 5. SUBMISSIONS TABLE
-- Student submissions for assignments
-- --------------------------------------------------------------------
CREATE TABLE submissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT NOT NULL,
    student_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'SUBMITTED', 'LATE', 'EVALUATED') DEFAULT 'SUBMITTED',
    is_late BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_student_assignment (assignment_id, student_id),
    INDEX idx_submissions_assignment (assignment_id),
    INDEX idx_submissions_student (student_id),
    INDEX idx_submissions_status (status),
    INDEX idx_submissions_is_late (is_late)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 6. EVALUATIONS TABLE
-- Faculty marks and feedback for student submissions
-- --------------------------------------------------------------------
CREATE TABLE evaluations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    submission_id INT NOT NULL UNIQUE,
    faculty_id INT NOT NULL,
    marks INT NOT NULL,
    feedback TEXT,
    evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_evaluations_submission (submission_id),
    INDEX idx_evaluations_faculty (faculty_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------------------
-- 7. NOTIFICATIONS TABLE
-- Real-time in-app notifications for students and faculty
-- --------------------------------------------------------------------
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('ASSIGNMENT', 'SUBMISSION', 'DEADLINE', 'EVALUATION', 'SYSTEM') DEFAULT 'SYSTEM',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notifications_user_read (user_id, is_read),
    INDEX idx_notifications_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
