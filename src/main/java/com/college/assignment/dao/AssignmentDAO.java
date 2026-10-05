package com.college.assignment.dao;

import com.college.assignment.model.Assignment;
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
 * Data Access Object for Assignments table.
 */
public class AssignmentDAO {
    private static final Logger LOGGER = Logger.getLogger(AssignmentDAO.class.getName());

    public Assignment findById(int id) {
        String sql = "SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, " +
                     "a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, " +
                     "s.subject_name, s.subject_code, u.name AS faculty_name " +
                     "FROM assignments a " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "INNER JOIN users u ON a.faculty_id = u.id " +
                     "WHERE a.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithDetails(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in AssignmentDAO.findById: " + e.getMessage(), e);
        }
        return null;
    }

    public Assignment findByIdForStudent(int id, int studentId) {
        String sql = "SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, " +
                     "a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, " +
                     "s.subject_name, s.subject_code, u.name AS faculty_name, " +
                     "sub.id AS submission_id, sub.status AS student_sub_status, sub.is_late AS student_is_late, " +
                     "ev.marks AS student_marks " +
                     "FROM assignments a " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "INNER JOIN users u ON a.faculty_id = u.id " +
                     "LEFT JOIN submissions sub ON a.id = sub.assignment_id AND sub.student_id = ? " +
                     "LEFT JOIN evaluations ev ON sub.id = ev.submission_id " +
                     "WHERE a.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Assignment a = mapRowWithDetails(rs);
                    String subStatus = rs.getString("student_sub_status");
                    if (subStatus != null) {
                        a.setSubmittedByCurrentStudent(true);
                        a.setStudentSubmissionStatus(subStatus);
                        int marks = rs.getInt("student_marks");
                        if (!rs.wasNull()) {
                            a.setStudentMarks(marks);
                        }
                    } else {
                        a.setSubmittedByCurrentStudent(false);
                        a.setStudentSubmissionStatus("PENDING");
                    }
                    return a;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByIdForStudent: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Assignment> findByFacultyId(int facultyId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, " +
                     "a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, " +
                     "s.subject_name, s.subject_code, u.name AS faculty_name, " +
                     "(SELECT COUNT(*) FROM submissions WHERE assignment_id = a.id) AS sub_count, " +
                     "(SELECT COUNT(*) FROM submissions sub2 LEFT JOIN evaluations ev ON sub2.id = ev.submission_id " +
                     " WHERE sub2.assignment_id = a.id AND ev.id IS NULL) AS pending_eval_count " +
                     "FROM assignments a " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "INNER JOIN users u ON a.faculty_id = u.id " +
                     "WHERE a.faculty_id = ? " +
                     "ORDER BY a.due_date DESC, a.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = mapRowWithDetails(rs);
                    a.setSubmissionCount(rs.getInt("sub_count"));
                    a.setPendingEvaluationCount(rs.getInt("pending_eval_count"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findByFacultyId: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Assignment> findAllForStudent(int studentId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, " +
                     "a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, " +
                     "s.subject_name, s.subject_code, u.name AS faculty_name, " +
                     "sub.status AS student_sub_status, ev.marks AS student_marks " +
                     "FROM assignments a " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "INNER JOIN users u ON a.faculty_id = u.id " +
                     "LEFT JOIN submissions sub ON a.id = sub.assignment_id AND sub.student_id = ? " +
                     "LEFT JOIN evaluations ev ON sub.id = ev.submission_id " +
                     "ORDER BY a.due_date ASC, a.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = mapRowWithDetails(rs);
                    String subStatus = rs.getString("student_sub_status");
                    if (subStatus != null) {
                        a.setSubmittedByCurrentStudent(true);
                        a.setStudentSubmissionStatus(subStatus);
                        int marks = rs.getInt("student_marks");
                        if (!rs.wasNull()) {
                            a.setStudentMarks(marks);
                        }
                    } else {
                        a.setSubmittedByCurrentStudent(false);
                        a.setStudentSubmissionStatus("PENDING");
                    }
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findAllForStudent: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Assignment> findUpcomingForStudent(int studentId, int limit) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, " +
                     "a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, " +
                     "s.subject_name, s.subject_code, u.name AS faculty_name, " +
                     "sub.status AS student_sub_status " +
                     "FROM assignments a " +
                     "INNER JOIN subjects s ON a.subject_id = s.id " +
                     "INNER JOIN users u ON a.faculty_id = u.id " +
                     "LEFT JOIN submissions sub ON a.id = sub.assignment_id AND sub.student_id = ? " +
                     "WHERE a.status = 'ACTIVE' AND (sub.id IS NULL OR sub.status = 'PENDING') " +
                     "ORDER BY a.due_date ASC, a.due_time ASC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = mapRowWithDetails(rs);
                    a.setStudentSubmissionStatus("PENDING");
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in findUpcomingForStudent: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean create(Assignment a) {
        String sql = "INSERT INTO assignments (subject_id, faculty_id, title, description, instructions, " +
                     "attachment_path, due_date, due_time, max_marks, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getSubjectId());
            ps.setInt(2, a.getFacultyId());
            ps.setString(3, a.getTitle());
            ps.setString(4, a.getDescription());
            ps.setString(5, a.getInstructions());

            if (a.getAttachmentPath() != null) {
                ps.setString(6, a.getAttachmentPath());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setDate(7, a.getDueDate());
            ps.setTime(8, a.getDueTime());
            ps.setInt(9, a.getMaxMarks());
            ps.setString(10, a.getStatus() != null ? a.getStatus().name() : "ACTIVE");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in AssignmentDAO.create: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean update(Assignment a) {
        String sql = "UPDATE assignments SET subject_id = ?, title = ?, description = ?, instructions = ?, " +
                     "attachment_path = COALESCE(?, attachment_path), due_date = ?, due_time = ?, max_marks = ?, status = ? " +
                     "WHERE id = ? AND faculty_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getSubjectId());
            ps.setString(2, a.getTitle());
            ps.setString(3, a.getDescription());
            ps.setString(4, a.getInstructions());

            if (a.getAttachmentPath() != null) {
                ps.setString(5, a.getAttachmentPath());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setDate(6, a.getDueDate());
            ps.setTime(7, a.getDueTime());
            ps.setInt(8, a.getMaxMarks());
            ps.setString(9, a.getStatus() != null ? a.getStatus().name() : "ACTIVE");
            ps.setInt(10, a.getId());
            ps.setInt(11, a.getFacultyId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in AssignmentDAO.update: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean delete(int assignmentId, int facultyId) {
        String sql = "DELETE FROM assignments WHERE id = ? AND faculty_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            ps.setInt(2, facultyId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in AssignmentDAO.delete: " + e.getMessage(), e);
        }
        return false;
    }

    public int countByFaculty(int facultyId) {
        String sql = "SELECT COUNT(*) FROM assignments WHERE faculty_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in countByFaculty: " + e.getMessage(), e);
        }
        return 0;
    }

    public List<Assignment> searchAndFilter(String query, Integer subjectId, String statusFilter, Integer studentId) {
        List<Assignment> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id, a.subject_id, a.faculty_id, a.title, a.description, a.instructions, ")
           .append("a.attachment_path, a.due_date, a.due_time, a.max_marks, a.status, a.created_at, a.updated_at, ")
           .append("s.subject_name, s.subject_code, u.name AS faculty_name, ")
           .append("sub.status AS student_sub_status, ev.marks AS student_marks ")
           .append("FROM assignments a ")
           .append("INNER JOIN subjects s ON a.subject_id = s.id ")
           .append("INNER JOIN users u ON a.faculty_id = u.id ");

        if (studentId != null) {
            sql.append("LEFT JOIN submissions sub ON a.id = sub.assignment_id AND sub.student_id = ? ");
            sql.append("LEFT JOIN evaluations ev ON sub.id = ev.submission_id ");
        }

        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (studentId != null) {
            params.add(studentId);
        }

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (LOWER(a.title) LIKE ? OR LOWER(s.subject_name) LIKE ? OR LOWER(s.subject_code) LIKE ? OR LOWER(u.name) LIKE ?) ");
            String like = "%" + query.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (subjectId != null && subjectId > 0) {
            sql.append("AND a.subject_id = ? ");
            params.add(subjectId);
        }

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL") && !statusFilter.trim().isEmpty()) {
            if ("PENDING".equalsIgnoreCase(statusFilter) && studentId != null) {
                sql.append("AND (sub.id IS NULL OR sub.status = 'PENDING') ");
            } else if ("SUBMITTED".equalsIgnoreCase(statusFilter) && studentId != null) {
                sql.append("AND (sub.status = 'SUBMITTED' OR sub.status = 'LATE') ");
            } else if ("EVALUATED".equalsIgnoreCase(statusFilter) && studentId != null) {
                sql.append("AND sub.status = 'EVALUATED' ");
            } else if ("LATE".equalsIgnoreCase(statusFilter) && studentId != null) {
                sql.append("AND sub.is_late = TRUE ");
            }
        }

        sql.append("ORDER BY a.due_date ASC, a.created_at DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = mapRowWithDetails(rs);
                    if (studentId != null) {
                        String subStatus = rs.getString("student_sub_status");
                        if (subStatus != null) {
                            a.setSubmittedByCurrentStudent(true);
                            a.setStudentSubmissionStatus(subStatus);
                            int marks = rs.getInt("student_marks");
                            if (!rs.wasNull()) {
                                a.setStudentMarks(marks);
                            }
                        } else {
                            a.setSubmittedByCurrentStudent(false);
                            a.setStudentSubmissionStatus("PENDING");
                        }
                    }
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchAndFilter: " + e.getMessage(), e);
        }
        return list;
    }

    private Assignment mapRowWithDetails(ResultSet rs) throws SQLException {
        Assignment a = new Assignment();
        a.setId(rs.getInt("id"));
        a.setSubjectId(rs.getInt("subject_id"));
        a.setFacultyId(rs.getInt("faculty_id"));
        a.setTitle(rs.getString("title"));
        a.setDescription(rs.getString("description"));
        a.setInstructions(rs.getString("instructions"));
        a.setAttachmentPath(rs.getString("attachment_path"));
        a.setDueDate(rs.getDate("due_date"));
        a.setDueTime(rs.getTime("due_time"));
        a.setMaxMarks(rs.getInt("max_marks"));
        a.setStatus(Assignment.Status.valueOf(rs.getString("status")));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        a.setUpdatedAt(rs.getTimestamp("updated_at"));
        a.setSubjectName(rs.getString("subject_name"));
        a.setSubjectCode(rs.getString("subject_code"));
        a.setFacultyName(rs.getString("faculty_name"));
        return a;
    }
}
