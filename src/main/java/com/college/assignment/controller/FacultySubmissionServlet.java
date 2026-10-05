package com.college.assignment.controller;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for Faculty to view and filter Student Submissions.
 */
@WebServlet(name = "FacultySubmissionServlet", urlPatterns = {"/faculty/submissions"})
public class FacultySubmissionServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final SubmissionService submissionService = new SubmissionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int facultyId = (Integer) session.getAttribute("userId");

        List<Assignment> facultyAssignments = assignmentService.getAssignmentsForFaculty(facultyId);

        String assignmentIdStr = req.getParameter("assignmentId");
        String statusFilter = req.getParameter("status");

        List<Submission> submissions = new ArrayList<>();
        Assignment selectedAssignment = null;

        if (assignmentIdStr != null && !assignmentIdStr.trim().isEmpty()) {
            try {
                int assignmentId = Integer.parseInt(assignmentIdStr.trim());
                selectedAssignment = assignmentService.getAssignmentById(assignmentId);
                if (selectedAssignment != null && selectedAssignment.getFacultyId() == facultyId) {
                    submissions = submissionService.getSubmissionsForAssignment(assignmentId);
                }
            } catch (NumberFormatException ignored) {}
        } else if (!facultyAssignments.isEmpty()) {
            // Default to first assignment if none selected
            selectedAssignment = facultyAssignments.get(0);
            submissions = submissionService.getSubmissionsForAssignment(selectedAssignment.getId());
        }

        // Apply status filter in memory if provided
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL") && !statusFilter.trim().isEmpty()) {
            List<Submission> filtered = new ArrayList<>();
            for (Submission s : submissions) {
                if ("PENDING".equalsIgnoreCase(statusFilter) && s.getStatus() != Submission.Status.EVALUATED) {
                    filtered.add(s);
                } else if ("EVALUATED".equalsIgnoreCase(statusFilter) && s.getStatus() == Submission.Status.EVALUATED) {
                    filtered.add(s);
                } else if ("LATE".equalsIgnoreCase(statusFilter) && s.isLate()) {
                    filtered.add(s);
                }
            }
            submissions = filtered;
        }

        req.setAttribute("assignments", facultyAssignments);
        req.setAttribute("selectedAssignment", selectedAssignment);
        req.setAttribute("submissions", submissions);
        req.setAttribute("selectedStatus", statusFilter != null ? statusFilter : "ALL");

        req.getRequestDispatcher("/WEB-INF/views/faculty/submissions.jsp").forward(req, resp);
    }
}
