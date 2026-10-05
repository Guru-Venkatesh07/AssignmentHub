package com.college.assignment.dao;

import com.college.assignment.model.User;
import com.college.assignment.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Users table.
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public User findById(int id) {
        String sql = "SELECT id, name, email, password_hash, role, register_number, department, year, created_at, updated_at " +
                     "FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findById: " + e.getMessage(), e);
        }
        return null;
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        String sql = "SELECT id, name, email, password_hash, role, register_number, department, year, created_at, updated_at " +
                     "FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByEmail: " + e.getMessage(), e);
        }
        return null;
    }

    public User findByRegisterNumber(String registerNumber) {
        if (registerNumber == null) return null;
        String sql = "SELECT id, name, email, password_hash, role, register_number, department, year, created_at, updated_at " +
                     "FROM users WHERE LOWER(register_number) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNumber.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByRegisterNumber: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        if (email == null) return false;
        String sql = "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in existsByEmail: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean existsByRegisterNumber(String registerNumber) {
        if (registerNumber == null) return false;
        String sql = "SELECT 1 FROM users WHERE LOWER(register_number) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, registerNumber.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in existsByRegisterNumber: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean create(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, role, register_number, department, year) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail().trim().toLowerCase());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());

            if (user.getRegisterNumber() != null && !user.getRegisterNumber().trim().isEmpty()) {
                ps.setString(5, user.getRegisterNumber().trim().toUpperCase());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setString(6, user.getDepartment());

            if (user.getYear() != null) {
                ps.setInt(7, user.getYear());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in user create: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET name = ?, department = ?, year = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getDepartment());
            if (user.getYear() != null) {
                ps.setInt(3, user.getYear());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setInt(4, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in updateProfile: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in updatePassword: " + e.getMessage(), e);
        }
        return false;
    }

    public List<User> findAllStudents() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, name, email, password_hash, role, register_number, department, year, created_at, updated_at " +
                     "FROM users WHERE role = 'STUDENT' ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findAllStudents: " + e.getMessage(), e);
        }
        return list;
    }

    public List<User> findStudentsByDepartment(String department) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, name, email, password_hash, role, register_number, department, year, created_at, updated_at " +
                     "FROM users WHERE role = 'STUDENT' AND department = ? ORDER BY name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, department);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findStudentsByDepartment: " + e.getMessage(), e);
        }
        return list;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(User.Role.valueOf(rs.getString("role")));
        u.setRegisterNumber(rs.getString("register_number"));
        u.setDepartment(rs.getString("department"));
        int year = rs.getInt("year");
        u.setYear(rs.wasNull() ? null : year);
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
}
