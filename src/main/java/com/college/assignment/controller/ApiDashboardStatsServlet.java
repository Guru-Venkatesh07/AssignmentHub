package com.college.assignment.controller;

import com.college.assignment.model.FacultyDashboardStats;
import com.college.assignment.model.StudentDashboardStats;
import com.college.assignment.service.DashboardService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AJAX Endpoint returning dynamic real-time dashboard statistics in JSON format.
 */
@WebServlet(name = "ApiDashboardStatsServlet", urlPatterns = {"/api/student/dashboard", "/api/faculty/dashboard"})
public class ApiDashboardStatsServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Session expired\"}");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if ("/api/faculty/dashboard".equals(path)) {
            FacultyDashboardStats stats = dashboardService.getFacultyDashboardStats(userId);
            resp.getWriter().write(gson.toJson(stats));
        } else {
            StudentDashboardStats stats = dashboardService.getStudentDashboardStats(userId);
            resp.getWriter().write(gson.toJson(stats));
        }
    }
}
