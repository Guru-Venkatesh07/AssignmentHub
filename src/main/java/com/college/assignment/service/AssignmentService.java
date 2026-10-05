package com.college.assignment.service;

import com.college.assignment.dao.AssignmentDAO;
import com.college.assignment.dao.FacultySubjectDAO;
import com.college.assignment.dao.NotificationDAO;
import com.college.assignment.dao.SubjectDAO;
import com.college.assignment.dao.UserDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.Notification;
import com.college.assignment.model.Subject;
import com.college.assignment.model.User;
import com.college.assignment.util.DateUtil;
import com.college.assignment.util.ValidationUtil;
import com.college.assignment.util.XmlUtil;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service managing Academic Assignments, CRUD, deadline calculation, and XML generation.
 */
public class AssignmentService {
    private static final Logger LOGGER = Logger.getLogger(AssignmentService.class.getName());

    private final AssignmentDAO assignmentDAO;
    private final SubjectDAO subjectDAO;
    private final FacultySubjectDAO facultySubjectDAO;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;

    public AssignmentService() {
        this.assignmentDAO = new AssignmentDAO();
        this.subjectDAO = new SubjectDAO();
        this.facultySubjectDAO = new FacultySubjectDAO();
        this.userDAO = new UserDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public AssignmentService(AssignmentDAO assignmentDAO, SubjectDAO subjectDAO,
                             FacultySubjectDAO facultySubjectDAO, UserDAO userDAO,
                             NotificationDAO notificationDAO) {
        this.assignmentDAO = assignmentDAO;
        this.subjectDAO = subjectDAO;
        this.facultySubjectDAO = facultySubjectDAO;
        this.userDAO = userDAO;
        this.notificationDAO = notificationDAO;
    }

    public Assignment getAssignmentById(int id) {
        Assignment a = assignmentDAO.findById(id);
        if (a != null) {
            enrichAssignment(a);
        }
        return a;
    }

    public Assignment getAssignmentForStudent(int id, int studentId) {
        Assignment a = assignmentDAO.findByIdForStudent(id, studentId);
        if (a != null) {
            enrichAssignment(a);
        }
        return a;
    }

    public List<Assignment> getAssignmentsForFaculty(int facultyId) {
        List<Assignment> list = assignmentDAO.findByFacultyId(facultyId);
        for (Assignment a : list) {
            enrichAssignment(a);
        }
        return list;
    }

    public List<Assignment> getAssignmentsForStudent(int studentId) {
        List<Assignment> list = assignmentDAO.findAllForStudent(studentId);
        for (Assignment a : list) {
            enrichAssignment(a);
        }
        return list;
    }

    public List<Assignment> searchAndFilter(String query, Integer subjectId, String statusFilter, Integer studentId) {
        List<Assignment> list = assignmentDAO.searchAndFilter(query, subjectId, statusFilter, studentId);
        for (Assignment a : list) {
            enrichAssignment(a);
        }
        return list;
    }

    public String createAssignment(int facultyId, int subjectId, String title, String description,
                                   String instructions, String attachmentPath, Date dueDate, Time dueTime, int maxMarks) {
        if (!ValidationUtil.isNotEmpty(title)) {
            return "Assignment title is required.";
        }
        if (subjectId <= 0) {
            return "Please select a valid subject.";
        }
        if (!ValidationUtil.isNotEmpty(description)) {
            return "Description is required.";
        }
        if (dueDate == null) {
            return "Valid due date is required.";
        }
        if (maxMarks <= 0) {
            return "Maximum marks must be greater than 0.";
        }

        Assignment a = new Assignment();
        a.setFacultyId(facultyId);
        a.setSubjectId(subjectId);
        a.setTitle(title.trim());
        a.setDescription(description.trim());
        a.setInstructions(instructions != null ? instructions.trim() : "");
        a.setAttachmentPath(attachmentPath);
        a.setDueDate(dueDate);
        a.setDueTime(dueTime != null ? dueTime : Time.valueOf("23:59:00"));
        a.setMaxMarks(maxMarks);
        a.setStatus(Assignment.Status.ACTIVE);

        boolean created = assignmentDAO.create(a);
        if (created) {
            LOGGER.info("Assignment created [ID: " + a.getId() + "] by Faculty [ID: " + facultyId + "]");

            // Notify all relevant students
            notifyStudentsForNewAssignment(a);
            return null; // success
        } else {
            return "Failed to save assignment in database.";
        }
    }

    public String updateAssignment(int assignmentId, int facultyId, int subjectId, String title,
                                   String description, String instructions, String attachmentPath,
                                   Date dueDate, Time dueTime, int maxMarks, Assignment.Status status) {
        Assignment existing = assignmentDAO.findById(assignmentId);
        if (existing == null) {
            return "Assignment not found.";
        }
        if (existing.getFacultyId() != facultyId) {
            return "Unauthorized: You can only edit your own assignments.";
        }
        if (!ValidationUtil.isNotEmpty(title)) {
            return "Assignment title is required.";
        }
        if (subjectId <= 0) {
            return "Please select a valid subject.";
        }
        if (dueDate == null) {
            return "Valid due date is required.";
        }
        if (maxMarks <= 0) {
            return "Maximum marks must be greater than 0.";
        }

        existing.setSubjectId(subjectId);
        existing.setTitle(title.trim());
        existing.setDescription(description.trim());
        existing.setInstructions(instructions != null ? instructions.trim() : "");
        if (attachmentPath != null) {
            existing.setAttachmentPath(attachmentPath);
        }
        existing.setDueDate(dueDate);
        existing.setDueTime(dueTime != null ? dueTime : Time.valueOf("23:59:00"));
        existing.setMaxMarks(maxMarks);
        existing.setStatus(status != null ? status : Assignment.Status.ACTIVE);

        boolean updated = assignmentDAO.update(existing);
        return updated ? null : "Failed to update assignment in database.";
    }

    public boolean deleteAssignment(int assignmentId, int facultyId) {
        Assignment a = assignmentDAO.findById(assignmentId);
        if (a == null || a.getFacultyId() != facultyId) {
            LOGGER.warning("Delete assignment failed: Not owned or not found - " + assignmentId);
            return false;
        }
        return assignmentDAO.delete(assignmentId, facultyId);
    }

    public String getAssignmentsXml() {
        List<Assignment> list = assignmentDAO.searchAndFilter(null, null, null, null);
        return XmlUtil.toAssignmentsXml(list);
    }

    public String getTimetableXml() {
        List<Subject> list = subjectDAO.findAll();
        return XmlUtil.toTimetableXml(list);
    }

    private void enrichAssignment(Assignment a) {
        a.setOverdue(DateUtil.isOverdue(a.getDueDate(), a.getDueTime()));
        a.setDeadlineStatusText(DateUtil.getDeadlineStatusText(a.getDueDate(), a.getDueTime()));
    }

    private void notifyStudentsForNewAssignment(Assignment a) {
        try {
            Subject subj = subjectDAO.findById(a.getSubjectId());
            String subjName = (subj != null) ? subj.getSubjectName() : "Course Subject";
            List<User> students = (subj != null) ? userDAO.findStudentsByDepartment(subj.getDepartment()) : userDAO.findAllStudents();

            if (students.isEmpty()) {
                students = userDAO.findAllStudents();
            }

            List<Integer> studentIds = new ArrayList<>();
            for (User s : students) {
                studentIds.add(s.getId());
            }

            notificationDAO.createForMultipleUsers(
                    studentIds,
                    "New Assignment: " + a.getTitle(),
                    "A new assignment for " + subjName + " has been posted. Due Date: " + DateUtil.formatDate(a.getDueDate()),
                    Notification.Type.ASSIGNMENT
            );
        } catch (Exception e) {
            LOGGER.warning("Failed to dispatch notifications for new assignment: " + e.getMessage());
        }
    }
}
