package com.college.assignment.service;

import com.college.assignment.dao.AssignmentDAO;
import com.college.assignment.dao.NotificationDAO;
import com.college.assignment.dao.SubmissionDAO;
import com.college.assignment.dao.UserDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.Notification;
import com.college.assignment.model.Submission;
import com.college.assignment.model.User;
import com.college.assignment.util.DateUtil;
import com.college.assignment.util.FileUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service managing Student Submissions, late checks, resubmissions, and faculty alerts.
 */
public class SubmissionService {
    private static final Logger LOGGER = Logger.getLogger(SubmissionService.class.getName());

    private final SubmissionDAO submissionDAO;
    private final AssignmentDAO assignmentDAO;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;

    public SubmissionService() {
        this.submissionDAO = new SubmissionDAO();
        this.assignmentDAO = new AssignmentDAO();
        this.userDAO = new UserDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public SubmissionService(SubmissionDAO submissionDAO, AssignmentDAO assignmentDAO,
                             UserDAO userDAO, NotificationDAO notificationDAO) {
        this.submissionDAO = submissionDAO;
        this.assignmentDAO = assignmentDAO;
        this.userDAO = userDAO;
        this.notificationDAO = notificationDAO;
    }

    public Submission getSubmissionById(int submissionId) {
        return submissionDAO.findById(submissionId);
    }

    public Submission getSubmissionForAssignmentAndStudent(int assignmentId, int studentId) {
        return submissionDAO.findByAssignmentAndStudent(assignmentId, studentId);
    }

    public List<Submission> getSubmissionsForAssignment(int assignmentId) {
        return submissionDAO.findByAssignmentId(assignmentId);
    }

    public List<Submission> getSubmissionsForStudent(int studentId) {
        return submissionDAO.findByStudentId(studentId);
    }

    public boolean canStudentResubmit(int assignmentId, int studentId) {
        Assignment a = assignmentDAO.findById(assignmentId);
        if (a == null || a.getStatus() == Assignment.Status.CLOSED || a.getStatus() == Assignment.Status.ARCHIVED) {
            return false;
        }

        Submission existing = submissionDAO.findByAssignmentAndStudent(assignmentId, studentId);
        if (existing == null) {
            return true; // first time submission is allowed
        }

        // If already evaluated, cannot resubmit
        if (existing.getStatus() == Submission.Status.EVALUATED) {
            return false;
        }

        // Before deadline, student can resubmit/replace their submission
        boolean overdue = DateUtil.isOverdue(a.getDueDate(), a.getDueTime());
        return !overdue;
    }

    public String submitAssignment(int assignmentId, int studentId, String originalFileName, String relativeFilePath) {
        Assignment a = assignmentDAO.findById(assignmentId);
        if (a == null) {
            return "Assignment not found.";
        }
        if (a.getStatus() == Assignment.Status.ARCHIVED) {
            return "This assignment is archived and no longer accepts submissions.";
        }

        User student = userDAO.findById(studentId);
        if (student == null || !student.isStudent()) {
            return "Invalid student user.";
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean isLate = DateUtil.isLateSubmission(a.getDueDate(), a.getDueTime(), now);

        Submission existing = submissionDAO.findByAssignmentAndStudent(assignmentId, studentId);

        if (existing != null) {
            // Resubmission Check
            if (existing.getStatus() == Submission.Status.EVALUATED) {
                return "This assignment has already been evaluated and cannot be replaced.";
            }
            if (isLate) {
                return "The deadline has passed. Resubmissions after the deadline are not permitted.";
            }

            // Update existing submission
            existing.setFileName(originalFileName);
            existing.setFilePath(relativeFilePath);
            existing.setStatus(isLate ? Submission.Status.LATE : Submission.Status.SUBMITTED);
            existing.setLate(isLate);

            boolean updated = submissionDAO.update(existing);
            if (updated) {
                LOGGER.info("Submission updated for student [" + studentId + "] on assignment [" + assignmentId + "]");
                notifyFacultyOnSubmission(a, student, isLate, true);
                return null; // success
            } else {
                return "Failed to update submission record.";
            }
        } else {
            // New submission
            Submission sub = new Submission();
            sub.setAssignmentId(assignmentId);
            sub.setStudentId(studentId);
            sub.setFileName(originalFileName);
            sub.setFilePath(relativeFilePath);
            sub.setStatus(isLate ? Submission.Status.LATE : Submission.Status.SUBMITTED);
            sub.setLate(isLate);

            boolean created = submissionDAO.create(sub);
            if (created) {
                LOGGER.info("New submission recorded for student [" + studentId + "] on assignment [" + assignmentId + "] | Late=" + isLate);
                notifyFacultyOnSubmission(a, student, isLate, false);
                notifyStudentSubmissionSuccess(a, student, isLate);
                return null; // success
            } else {
                return "Failed to save submission record.";
            }
        }
    }

    private void notifyFacultyOnSubmission(Assignment a, User student, boolean isLate, boolean isResubmission) {
        try {
            String title = isLate ? "Late Submission: " + a.getTitle() : "New Submission: " + a.getTitle();
            String msg = student.getName() + " (" + student.getRegisterNumber() + ") " +
                    (isResubmission ? "resubmitted" : "submitted") + " assignment '" + a.getTitle() + "'." +
                    (isLate ? " (Submitted LATE after deadline)" : "");

            Notification notif = new Notification();
            notif.setUserId(a.getFacultyId());
            notif.setTitle(title);
            notif.setMessage(msg);
            notif.setType(Notification.Type.SUBMISSION);
            notificationDAO.create(notif);
        } catch (Exception e) {
            LOGGER.warning("Failed to notify faculty on submission: " + e.getMessage());
        }
    }

    private void notifyStudentSubmissionSuccess(Assignment a, User student, boolean isLate) {
        try {
            Notification notif = new Notification();
            notif.setUserId(student.getId());
            notif.setTitle(isLate ? "Assignment Submitted (Late)" : "Assignment Submitted Successfully");
            notif.setMessage("Your submission for '" + a.getTitle() + "' was received on " +
                    DateUtil.formatDateTime(new Timestamp(System.currentTimeMillis())) +
                    (isLate ? ". Note: Submission was recorded as late." : "."));
            notif.setType(Notification.Type.SUBMISSION);
            notificationDAO.create(notif);
        } catch (Exception e) {
            LOGGER.warning("Failed to notify student on submission: " + e.getMessage());
        }
    }
}
