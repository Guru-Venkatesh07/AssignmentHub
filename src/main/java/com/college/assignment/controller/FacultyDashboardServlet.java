package com.college.assignment.controller;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.FacultyDashboardStats;
import com.college.assignment.model.Submission;
import com.college.assignment.service.AssignmentService;
import com.college.assignment.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for Faculty Dashboard with summary metrics, recent assignments, and pending evaluations.
 */
@WebServlet(name = "FacultyDashboardServlet", urlPatterns = {"/faculty/dashboard"})
public class FacultyDashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();
    private final AssignmentService assignmentService = new AssignmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int facultyId = (Integer) session.getAttribute("userId");

        FacultyDashboardStats stats = dashboardService.getFacultyDashboardStats(facultyId);
        List<Assignment> recentAssignments = assignmentService.getAssignmentsForFaculty(facultyId);
        List<Submission> pendingEvaluations = dashboardService.getPendingEvaluationsForFaculty(facultyId, 5);

        req.setAttribute("stats", stats);
        req.setAttribute("recentAssignments", recentAssignments);
        req.setAttribute("pendingEvaluations", pendingEvaluations);

        req.getRequestDispatcher("/WEB-INF/views/faculty/dashboard.jsp").forward(req, resp);
    }
}
