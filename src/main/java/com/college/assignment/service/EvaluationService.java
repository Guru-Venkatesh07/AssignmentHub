package com.college.assignment.service;

import com.college.assignment.dao.AssignmentDAO;
import com.college.assignment.dao.EvaluationDAO;
import com.college.assignment.dao.NotificationDAO;
import com.college.assignment.dao.SubmissionDAO;
import com.college.assignment.dao.UserDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.Evaluation;
import com.college.assignment.model.Notification;
import com.college.assignment.model.Submission;
import com.college.assignment.model.User;
import com.college.assignment.util.ValidationUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service managing Faculty Evaluations, marks validation, and student result publishing.
 */
public class EvaluationService {
    private static final Logger LOGGER = Logger.getLogger(EvaluationService.class.getName());

    private final EvaluationDAO evaluationDAO;
    private final SubmissionDAO submissionDAO;
    private final AssignmentDAO assignmentDAO;
    private final NotificationDAO notificationDAO;
    private final UserDAO userDAO;

    public EvaluationService() {
        this.evaluationDAO = new EvaluationDAO();
        this.submissionDAO = new SubmissionDAO();
        this.assignmentDAO = new AssignmentDAO();
        this.notificationDAO = new NotificationDAO();
        this.userDAO = new UserDAO();
    }

    public EvaluationService(EvaluationDAO evaluationDAO, SubmissionDAO submissionDAO,
                             AssignmentDAO assignmentDAO, NotificationDAO notificationDAO,
                             UserDAO userDAO) {
        this.evaluationDAO = evaluationDAO;
        this.submissionDAO = submissionDAO;
        this.assignmentDAO = assignmentDAO;
        this.notificationDAO = notificationDAO;
        this.userDAO = userDAO;
    }

    public Evaluation getEvaluationBySubmissionId(int submissionId) {
        return evaluationDAO.findBySubmissionId(submissionId);
    }

    public String evaluateSubmission(int submissionId, int facultyId, int marks, String feedback) {
        Submission sub = submissionDAO.findById(submissionId);
        if (sub == null) {
            return "Submission record not found.";
        }

        Assignment a = assignmentDAO.findById(sub.getAssignmentId());
        if (a == null) {
            return "Assignment not found.";
        }

        if (a.getFacultyId() != facultyId) {
            return "Unauthorized: You can only evaluate submissions for your own assignments.";
        }

        if (marks < 0) {
            return "Marks cannot be negative.";
        }
        if (marks > a.getMaxMarks()) {
            return "Marks (" + marks + ") cannot exceed maximum marks (" + a.getMaxMarks() + ").";
        }

        Evaluation eval = new Evaluation();
        eval.setSubmissionId(submissionId);
        eval.setFacultyId(facultyId);
        eval.setMarks(marks);
        eval.setFeedback(feedback != null ? feedback.trim() : "");

        boolean saved = evaluationDAO.create(eval);
        if (saved) {
            // Update submission status to EVALUATED
            submissionDAO.updateStatus(submissionId, Submission.Status.EVALUATED);
            LOGGER.info("Evaluation recorded for submission [" + submissionId + "] by faculty [" + facultyId + "] | Marks: " + marks + "/" + a.getMaxMarks());

            // Send notification to student
            notifyStudentOnEvaluation(a, sub, marks, feedback);
            return null; // success
        } else {
            return "Failed to save evaluation to database.";
        }
    }

    public List<Submission> getStudentResults(int studentId) {
        return submissionDAO.findByStudentId(studentId);
    }

    private void notifyStudentOnEvaluation(Assignment a, Submission sub, int marks, String feedback) {
        try {
            User faculty = userDAO.findById(a.getFacultyId());
            String facultyName = (faculty != null) ? faculty.getName() : "Faculty";

            Notification notif = new Notification();
            notif.setUserId(sub.getStudentId());
            notif.setTitle("Evaluation Result: " + a.getTitle());
            notif.setMessage(facultyName + " evaluated your submission for '" + a.getTitle() + "'. " +
                    "Score: " + marks + "/" + a.getMaxMarks() + (feedback != null && !feedback.isEmpty() ? " | Feedback: " + feedback : ""));
            notif.setType(Notification.Type.EVALUATION);
            notificationDAO.create(notif);
        } catch (Exception e) {
            LOGGER.warning("Failed to notify student on evaluation: " + e.getMessage());
        }
    }
}
