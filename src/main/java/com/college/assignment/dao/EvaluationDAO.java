package com.college.assignment.dao;

import com.college.assignment.model.Evaluation;
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
 * Data Access Object for Evaluations table.
 */
public class EvaluationDAO {
    private static final Logger LOGGER = Logger.getLogger(EvaluationDAO.class.getName());

    public Evaluation findById(int id) {
        String sql = "SELECT e.id, e.submission_id, e.faculty_id, e.marks, e.feedback, e.evaluated_at, e.updated_at, " +
                     "u.name AS faculty_name, stud.name AS student_name, stud.register_number, " +
                     "a.title AS assignment_title, s.subject_name, a.max_marks " +
                     "FROM evaluations e " +
                     "INNER JOIN users u ON e.faculty_id = u.id " +
                     "INNER JOIN submissions sub ON e.submission_id = sub.id " +
                     "INNER JOIN users stud ON sub.student_id = stud.id " +
                     "INNER JOIN assignments a ON sub.assignment_id = a.id " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "WHERE e.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in EvaluationDAO.findById: " + e.getMessage(), e);
        }
        return null;
    }

    public Evaluation findBySubmissionId(int submissionId) {
        String sql = "SELECT e.id, e.submission_id, e.faculty_id, e.marks, e.feedback, e.evaluated_at, e.updated_at, " +
                     "u.name AS faculty_name, stud.name AS student_name, stud.register_number, " +
                     "a.title AS assignment_title, s.subject_name, a.max_marks " +
                     "FROM evaluations e " +
                     "INNER JOIN users u ON e.faculty_id = u.id " +
                     "INNER JOIN submissions sub ON e.submission_id = sub.id " +
                     "INNER JOIN users stud ON sub.student_id = stud.id " +
                     "INNER JOIN assignments a ON sub.assignment_id = a.id " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "WHERE e.submission_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findBySubmissionId: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean create(Evaluation e) {
        String sql = "INSERT INTO evaluations (submission_id, faculty_id, marks, feedback) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE marks = VALUES(marks), feedback = VALUES(feedback), updated_at = CURRENT_TIMESTAMP";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getSubmissionId());
            ps.setInt(2, e.getFacultyId());
            ps.setInt(3, e.getMarks());
            ps.setString(4, e.getFeedback());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        e.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error in EvaluationDAO.create: " + ex.getMessage(), ex);
        }
        return false;
    }

    public double getAveragePercentageForStudent(int studentId) {
        String sql = "SELECT AVG((ev.marks / a.max_marks) * 100) AS avg_percent " +
                     "FROM evaluations ev " +
                     "INNER JOIN submissions sub ON ev.submission_id = sub.id " +
                     "INNER JOIN assignments a ON sub.assignment_id = a.id " +
                     "WHERE sub.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double val = rs.getDouble("avg_percent");
                    return rs.wasNull() ? 0.0 : Math.round(val * 100.0) / 100.0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAveragePercentageForStudent: " + e.getMessage(), e);
        }
        return 0.0;
    }

    private Evaluation mapRow(ResultSet rs) throws SQLException {
        Evaluation e = new Evaluation();
        e.setId(rs.getInt("id"));
        e.setSubmissionId(rs.getInt("submission_id"));
        e.setFacultyId(rs.getInt("faculty_id"));
        e.setMarks(rs.getInt("marks"));
        e.setFeedback(rs.getString("feedback"));
        e.setEvaluatedAt(rs.getTimestamp("evaluated_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));

        e.setFacultyName(rs.getString("faculty_name"));
        e.setStudentName(rs.getString("student_name"));
        e.setStudentRegisterNumber(rs.getString("register_number"));
        e.setAssignmentTitle(rs.getString("assignment_title"));
        e.setSubjectName(rs.getString("subject_name"));
        e.setMaxMarks(rs.getInt("max_marks"));
        return e;
    }
}
