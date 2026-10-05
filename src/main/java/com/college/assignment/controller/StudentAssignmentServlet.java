package com.college.assignment.controller;

import com.college.assignment.dao.SubjectDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.Submission;
import com.college.assignment.service.AssignmentService;
import com.college.assignment.service.SubmissionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for Student Assignment viewing, searching, and filtering.
 */
@WebServlet(name = "StudentAssignmentServlet", urlPatterns = {"/student/assignments", "/student/assignments/view"})
public class StudentAssignmentServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final SubmissionService submissionService = new SubmissionService();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int studentId = (Integer) session.getAttribute("userId");
        String servletPath = req.getServletPath();

        if ("/student/assignments/view".equals(servletPath)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/student/assignments");
                return;
            }

            try {
                int assignmentId = Integer.parseInt(idStr.trim());
                Assignment assignment = assignmentService.getAssignmentForStudent(assignmentId, studentId);
                if (assignment == null) {
                    req.setAttribute("errorMessage", "Assignment not found.");
                    req.getRequestDispatcher("/WEB-INF/views/errors/404.jsp").forward(req, resp);
                    return;
                }

                Submission submission = submissionService.getSubmissionForAssignmentAndStudent(assignmentId, studentId);
                boolean canResubmit = submissionService.canStudentResubmit(assignmentId, studentId);

                req.setAttribute("assignment", assignment);
                req.setAttribute("submission", submission);
                req.setAttribute("canResubmit", canResubmit);

                req.getRequestDispatcher("/WEB-INF/views/student/assignment-details.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/student/assignments");
            }
        } else {
            // Assignment List with Search and Filtering
            String search = req.getParameter("search");
            String subjectIdStr = req.getParameter("subjectId");
            String status = req.getParameter("status");

            Integer subjectId = null;
            if (subjectIdStr != null && !subjectIdStr.trim().isEmpty() && !"0".equals(subjectIdStr)) {
                try {
                    subjectId = Integer.parseInt(subjectIdStr.trim());
                } catch (NumberFormatException ignored) {}
            }

            List<Assignment> assignments = assignmentService.searchAndFilter(search, subjectId, status, studentId);

            req.setAttribute("assignments", assignments);
            req.setAttribute("subjects", subjectDAO.findAll());
            req.setAttribute("search", search);
            req.setAttribute("selectedSubjectId", subjectId);
            req.setAttribute("selectedStatus", status != null ? status : "ALL");

            req.getRequestDispatcher("/WEB-INF/views/student/assignments.jsp").forward(req, resp);
        }
    }
}
