package com.college.assignment.dao;

import com.college.assignment.model.Subject;
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
 * Data Access Object for Subjects table.
 */
public class SubjectDAO {
    private static final Logger LOGGER = Logger.getLogger(SubjectDAO.class.getName());

    public Subject findById(int id) {
        String sql = "SELECT id, subject_code, subject_name, semester, department, created_at FROM subjects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubjectDAO.findById: " + e.getMessage(), e);
        }
        return null;
    }

    public Subject findByCode(String code) {
        String sql = "SELECT id, subject_code, subject_name, semester, department, created_at FROM subjects WHERE subject_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubjectDAO.findByCode: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Subject> findAll() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT id, subject_code, subject_name, semester, department, created_at FROM subjects ORDER BY subject_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubjectDAO.findAll: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Subject> findByFacultyId(int facultyId) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.id, s.subject_code, s.subject_name, s.semester, s.department, s.created_at " +
                     "FROM subjects s " +
                     "INNER JOIN faculty_subjects fs ON s.id = fs.subject_id " +
                     "WHERE fs.faculty_id = ? " +
                     "ORDER BY s.subject_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in SubjectDAO.findByFacultyId: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean create(Subject s) {
        String sql = "INSERT INTO subjects (subject_code, subject_name, semester, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getSubjectCode().trim().toUpperCase());
            ps.setString(2, s.getSubjectName().trim());
            ps.setInt(3, s.getSemester());
            ps.setString(4, s.getDepartment().trim());
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
            LOGGER.log(Level.SEVERE, "Error in SubjectDAO.create: " + e.getMessage(), e);
        }
        return false;
    }

    private Subject mapRow(ResultSet rs) throws SQLException {
        Subject s = new Subject();
        s.setId(rs.getInt("id"));
        s.setSubjectCode(rs.getString("subject_code"));
        s.setSubjectName(rs.getString("subject_name"));
        s.setSemester(rs.getInt("semester"));
        s.setDepartment(rs.getString("department"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
