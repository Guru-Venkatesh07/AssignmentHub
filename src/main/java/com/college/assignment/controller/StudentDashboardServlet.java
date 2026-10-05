package com.college.assignment.controller;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.StudentDashboardStats;
import com.college.assignment.model.Submission;
import com.college.assignment.service.DashboardService;
import com.college.assignment.service.EvaluationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for Student Dashboard with live dynamic metrics and upcoming assignments.
 */
@WebServlet(name = "StudentDashboardServlet", urlPatterns = {"/student/dashboard"})
public class StudentDashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();
    private final EvaluationService evaluationService = new EvaluationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int studentId = (Integer) session.getAttribute("userId");

        StudentDashboardStats stats = dashboardService.getStudentDashboardStats(studentId);
        List<Assignment> upcoming = dashboardService.getUpcomingAssignmentsForStudent(studentId, 5);
        List<Submission> allSubmissions = evaluationService.getStudentResults(studentId);

        req.setAttribute("stats", stats);
        req.setAttribute("upcomingAssignments", upcoming);
        req.setAttribute("recentSubmissions", allSubmissions);

        req.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(req, resp);
    }
}
