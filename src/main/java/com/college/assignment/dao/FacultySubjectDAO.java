package com.college.assignment.dao;

import com.college.assignment.model.FacultySubject;
import com.college.assignment.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for faculty_subjects table.
 */
public class FacultySubjectDAO {
    private static final Logger LOGGER = Logger.getLogger(FacultySubjectDAO.class.getName());

    public List<FacultySubject> findByFacultyId(int facultyId) {
        List<FacultySubject> list = new ArrayList<>();
        String sql = "SELECT fs.id, fs.faculty_id, fs.subject_id, u.name AS faculty_name, " +
                     "s.subject_code, s.subject_name, s.department " +
                     "FROM faculty_subjects fs " +
                     "INNER JOIN users u ON fs.faculty_id = u.id " +
                     "INNER JOIN subjects s ON fs.subject_id = s.id " +
                     "WHERE fs.faculty_id = ? " +
                     "ORDER BY s.subject_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FacultySubject fs = new FacultySubject();
                    fs.setId(rs.getInt("id"));
                    fs.setFacultyId(rs.getInt("faculty_id"));
                    fs.setSubjectId(rs.getInt("subject_id"));
                    fs.setFacultyName(rs.getString("faculty_name"));
                    fs.setSubjectCode(rs.getString("subject_code"));
                    fs.setSubjectName(rs.getString("subject_name"));
                    fs.setDepartment(rs.getString("department"));
                    list.add(fs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in FacultySubjectDAO.findByFacultyId: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean isFacultyAssignedToSubject(int facultyId, int subjectId) {
        String sql = "SELECT 1 FROM faculty_subjects WHERE faculty_id = ? AND subject_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ps.setInt(2, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in isFacultyAssignedToSubject: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean assignFacultyToSubject(int facultyId, int subjectId) {
        String sql = "INSERT INTO faculty_subjects (faculty_id, subject_id) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE id=id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            ps.setInt(2, subjectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in assignFacultyToSubject: " + e.getMessage(), e);
        }
        return false;
    }
}
