package com.college.assignment.dao;

import com.college.assignment.model.Submission;
import com.college.assignment.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Submissions table.
 */
public class SubmissionDAO {
    private static final Logger LOGGER = Logger.getLogger(SubmissionDAO.class.getName());

    public Submission findById(int id) {
        String sql = "SELECT s.id, s.assignment_id, s.student_id, s.file_name, s.file_path, " +
                     "s.submitted_at, s.status, s.is_late, s.updated_at, " +
                     "u.name AS student_name, u.email AS student_email, u.register_number, u.department, u.year, " +
                     "a.title AS assignment_title, a.max_marks, subj.subject_name, subj.subject_code, " +
                     "e.marks, e.feedback, e.evaluated_at, fac.name AS faculty_name " +
                     "FROM submissions s " +
                     "INNER JOIN users u ON s.student_id = u.id " +
                     "INNER JOIN assignments a ON s.assignment_id = a.id " +
                     "INNER JOIN subjects subj ON a.subject_id = subj.id " +
                     "INNER JOIN users fac ON a.faculty_id = fac.id " +
                     "LEFT JOIN evaluations e ON s.id = e.submission_id " +
                     "WHERE s.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubmissionDAO.findById: " + e.getMessage(), e);
        }
        return null;
    }

    public Submission findByAssignmentAndStudent(int assignmentId, int studentId) {
        String sql = "SELECT s.id, s.assignment_id, s.student_id, s.file_name, s.file_path, " +
                     "s.submitted_at, s.status, s.is_late, s.updated_at, " +
                     "u.name AS student_name, u.email AS student_email, u.register_number, u.department, u.year, " +
                     "a.title AS assignment_title, a.max_marks, subj.subject_name, subj.subject_code, " +
                     "e.marks, e.feedback, e.evaluated_at, fac.name AS faculty_name " +
                     "FROM submissions s " +
                     "INNER JOIN users u ON s.student_id = u.id " +
                     "INNER JOIN assignments a ON s.assignment_id = a.id " +
                     "INNER JOIN subjects subj ON a.subject_id = subj.id " +
                     "INNER JOIN users fac ON a.faculty_id = fac.id " +
                     "LEFT JOIN evaluations e ON s.id = e.submission_id " +
                     "WHERE s.assignment_id = ? AND s.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByAssignmentAndStudent: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Submission> findByAssignmentId(int assignmentId) {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT s.id, s.assignment_id, s.student_id, s.file_name, s.file_path, " +
                     "s.submitted_at, s.status, s.is_late, s.updated_at, " +
                     "u.name AS student_name, u.email AS student_email, u.register_number, u.department, u.year, " +
                     "a.title AS assignment_title, a.max_marks, subj.subject_name, subj.subject_code, " +
                     "e.marks, e.feedback, e.evaluated_at, fac.name AS faculty_name " +
                     "FROM submissions s " +
                     "INNER JOIN users u ON s.student_id = u.id " +
                     "INNER JOIN assignments a ON s.assignment_id = a.id " +
                     "INNER JOIN subjects subj ON a.subject_id = subj.id " +
                     "INNER JOIN users fac ON a.faculty_id = fac.id " +
                     "LEFT JOIN evaluations e ON s.id = e.submission_id " +
                     "WHERE s.assignment_id = ? " +
                     "ORDER BY s.submitted_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByAssignmentId: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Submission> findByStudentId(int studentId) {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT s.id, s.assignment_id, s.student_id, s.file_name, s.file_path, " +
                     "s.submitted_at, s.status, s.is_late, s.updated_at, " +
                     "u.name AS student_name, u.email AS student_email, u.register_number, u.department, u.year, " +
                     "a.title AS assignment_title, a.max_marks, subj.subject_name, subj.subject_code, " +
                     "e.marks, e.feedback, e.evaluated_at, fac.name AS faculty_name " +
                     "FROM submissions s " +
                     "INNER JOIN users u ON s.student_id = u.id " +
                     "INNER JOIN assignments a ON s.assignment_id = a.id " +
                     "INNER JOIN subjects subj ON a.subject_id = subj.id " +
                     "INNER JOIN users fac ON a.faculty_id = fac.id " +
                     "LEFT JOIN evaluations e ON s.id = e.submission_id " +
                     "WHERE s.student_id = ? " +
                     "ORDER BY s.submitted_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByStudentId: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean create(Submission s) {
        String sql = "INSERT INTO submissions (assignment_id, student_id, file_name, file_path, status, is_late) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getAssignmentId());
            ps.setInt(2, s.getStudentId());
            ps.setString(3, s.getFileName());
            ps.setString(4, s.getFilePath());
            ps.setString(5, s.getStatus() != null ? s.getStatus().name() : "SUBMITTED");
            ps.setBoolean(6, s.isLate());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        s.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubmissionDAO.create: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean update(Submission s) {
        String sql = "UPDATE submissions SET file_name = ?, file_path = ?, status = ?, is_late = ?, submitted_at = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFileName());
            ps.setString(2, s.getFilePath());
            ps.setString(3, s.getStatus() != null ? s.getStatus().name() : "SUBMITTED");
            ps.setBoolean(4, s.isLate());
            ps.setInt(5, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubmissionDAO.update: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean updateStatus(int submissionId, Submission.Status status) {
        String sql = "UPDATE submissions SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, submissionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in updateStatus: " + e.getMessage(), e);
        }
        return false;
    }

    public List<Submission> getPendingEvaluationsForFaculty(int facultyId, int limit) {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT s.id, s.assignment_id, s.student_id, s.file_name, s.file_path, " +
                     "s.submitted_at, s.status, s.is_late, s.updated_at, " +
                     "u.name AS student_name, u.email AS student_email, u.register_number, u.department, u.year, " +
                     "a.title AS assignment_title, a.max_marks, subj.subject_name, subj.subject_code, " +
                     "e.marks, e.feedback, e.evaluated_at, fac.name AS faculty_name " +
                     "FROM submissions s " +
                     "INNER JOIN users u ON s.student_id = u.id " +
                     "INNER JOIN assignments a ON s.assignment_id = a.id " +
                     "INNER JOIN subjects subj ON a.subject_id = subj.id " +
                     "INNER JOIN users fac ON a.faculty_id = fac.id " +
                     "LEFT JOIN evaluations e ON s.id = e.submission_id " +
                     "WHERE a.faculty_id = ? AND e.id IS NULL " +
                     "ORDER BY s.submitted_at ASC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getPendingEvaluationsForFaculty: " + e.getMessage(), e);
        }
        return list;
    }

    private Submission mapRow(ResultSet rs) throws SQLException {
        Submission s = new Submission();
        s.setId(rs.getInt("id"));
        s.setAssignmentId(rs.getInt("assignment_id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setFileName(rs.getString("file_name"));
        s.setFilePath(rs.getString("file_path"));
        s.setSubmittedAt(rs.getTimestamp("submitted_at"));
        s.setStatus(Submission.Status.valueOf(rs.getString("status")));
        s.setLate(rs.getBoolean("is_late"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));

        s.setStudentName(rs.getString("student_name"));
        s.setStudentEmail(rs.getString("student_email"));
        s.setStudentRegisterNumber(rs.getString("register_number"));
        s.setStudentDepartment(rs.getString("department"));
        int yr = rs.getInt("year");
        s.setStudentYear(rs.wasNull() ? null : yr);

        s.setAssignmentTitle(rs.getString("assignment_title"));
        s.setMaxMarks(rs.getInt("max_marks"));
        s.setSubjectName(rs.getString("subject_name"));
        s.setSubjectCode(rs.getString("subject_code"));
        s.setFacultyName(rs.getString("faculty_name"));

        int marks = rs.getInt("marks");
        if (!rs.wasNull()) {
            s.setMarks(marks);
        }
        s.setFeedback(rs.getString("feedback"));
        s.setEvaluatedAt(rs.getTimestamp("evaluated_at"));
        return s;
    }
}
