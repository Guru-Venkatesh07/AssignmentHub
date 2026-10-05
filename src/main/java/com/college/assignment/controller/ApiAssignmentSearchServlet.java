package com.college.assignment.controller;

import com.college.assignment.model.Assignment;
import com.college.assignment.service.AssignmentService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * AJAX Endpoint for Real-time Assignment Search and Filtering.
 */
@WebServlet(name = "ApiAssignmentSearchServlet", urlPatterns = {"/api/assignments/search"})
public class ApiAssignmentSearchServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        String subjectIdStr = req.getParameter("subjectId");
        String status = req.getParameter("status");

        Integer studentId = null;
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            studentId = (Integer) session.getAttribute("userId");
        }

        Integer subjectId = null;
        if (subjectIdStr != null && !subjectIdStr.trim().isEmpty() && !"0".equals(subjectIdStr)) {
            try {
                subjectId = Integer.parseInt(subjectIdStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        List<Assignment> results = assignmentService.searchAndFilter(query, subjectId, status, studentId);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(results));
    }
}
