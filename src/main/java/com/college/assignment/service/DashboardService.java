package com.college.assignment.service;

import com.college.assignment.dao.AssignmentDAO;
import com.college.assignment.dao.EvaluationDAO;
import com.college.assignment.dao.FacultySubjectDAO;
import com.college.assignment.dao.NotificationDAO;
import com.college.assignment.dao.SubmissionDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.FacultyDashboardStats;
import com.college.assignment.model.FacultySubject;
import com.college.assignment.model.StudentDashboardStats;
import com.college.assignment.model.Submission;
import com.college.assignment.util.DateUtil;

import java.util.List;

/**
 * Service calculating dynamic real-time Dashboard statistics from the database.
 */
public class DashboardService {

    private final AssignmentDAO assignmentDAO;
    private final SubmissionDAO submissionDAO;
    private final EvaluationDAO evaluationDAO;
    private final NotificationDAO notificationDAO;
    private final FacultySubjectDAO facultySubjectDAO;

    public DashboardService() {
        this.assignmentDAO = new AssignmentDAO();
        this.submissionDAO = new SubmissionDAO();
        this.evaluationDAO = new EvaluationDAO();
        this.notificationDAO = new NotificationDAO();
        this.facultySubjectDAO = new FacultySubjectDAO();
    }

    public DashboardService(AssignmentDAO assignmentDAO, SubmissionDAO submissionDAO,
                            EvaluationDAO evaluationDAO, NotificationDAO notificationDAO,
                            FacultySubjectDAO facultySubjectDAO) {
        this.assignmentDAO = assignmentDAO;
        this.submissionDAO = submissionDAO;
        this.evaluationDAO = evaluationDAO;
        this.notificationDAO = notificationDAO;
        this.facultySubjectDAO = facultySubjectDAO;
    }

    public StudentDashboardStats getStudentDashboardStats(int studentId) {
        List<Assignment> allAssignments = assignmentDAO.findAllForStudent(studentId);
        int total = allAssignments.size();
        int submitted = 0;
        int pending = 0;
        int evaluated = 0;
        int late = 0;

        for (Assignment a : allAssignments) {
            String status = a.getStudentSubmissionStatus();
            if (status == null || "PENDING".equalsIgnoreCase(status)) {
                pending++;
            } else if ("EVALUATED".equalsIgnoreCase(status)) {
                evaluated++;
                submitted++;
            } else if ("LATE".equalsIgnoreCase(status)) {
                late++;
                submitted++;
            } else if ("SUBMITTED".equalsIgnoreCase(status)) {
                submitted++;
            }
        }

        double avgPercentage = evaluationDAO.getAveragePercentageForStudent(studentId);
        int unread = notificationDAO.countUnreadByUserId(studentId);

        return new StudentDashboardStats(total, submitted, pending, evaluated, late, avgPercentage, unread);
    }

    public FacultyDashboardStats getFacultyDashboardStats(int facultyId) {
        List<Assignment> facultyAssignments = assignmentDAO.findByFacultyId(facultyId);
        int assignmentsCreated = facultyAssignments.size();
        int totalSubmissions = 0;
        int pendingEvaluations = 0;
        int evaluatedSubmissions = 0;
        int lateSubmissions = 0;

        for (Assignment a : facultyAssignments) {
            List<Submission> subs = submissionDAO.findByAssignmentId(a.getId());
            totalSubmissions += subs.size();
            for (Submission s : subs) {
                if (s.getStatus() == Submission.Status.EVALUATED) {
                    evaluatedSubmissions++;
                } else {
                    pendingEvaluations++;
                }
                if (s.isLate()) {
                    lateSubmissions++;
                }
            }
        }

        List<FacultySubject> subjects = facultySubjectDAO.findByFacultyId(facultyId);
        int totalSubjects = subjects.size();
        int unread = notificationDAO.countUnreadByUserId(facultyId);

        return new FacultyDashboardStats(assignmentsCreated, totalSubmissions, pendingEvaluations,
                evaluatedSubmissions, lateSubmissions, totalSubjects, unread);
    }

    public List<Assignment> getUpcomingAssignmentsForStudent(int studentId, int limit) {
        List<Assignment> list = assignmentDAO.findUpcomingForStudent(studentId, limit);
        for (Assignment a : list) {
            a.setOverdue(DateUtil.isOverdue(a.getDueDate(), a.getDueTime()));
            a.setDeadlineStatusText(DateUtil.getDeadlineStatusText(a.getDueDate(), a.getDueTime()));
        }
        return list;
    }

    public List<Submission> getPendingEvaluationsForFaculty(int facultyId, int limit) {
        return submissionDAO.getPendingEvaluationsForFaculty(facultyId, limit);
    }
}
