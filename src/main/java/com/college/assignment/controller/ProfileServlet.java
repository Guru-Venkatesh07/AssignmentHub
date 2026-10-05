package com.college.assignment.controller;

import com.college.assignment.model.User;
import com.college.assignment.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet handling User Profile viewing, profile editing, and password updates.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/update", "/profile/change-password"})
public class ProfileServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        User user = authService.getUserById(userId);
        req.setAttribute("user", user);

        User.Role role = (User.Role) session.getAttribute("userRole");
        if (role == User.Role.FACULTY) {
            req.getRequestDispatcher("/WEB-INF/views/faculty/profile.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/student/profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        String servletPath = req.getServletPath();

        if ("/profile/change-password".equals(servletPath)) {
            String currentPassword = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmNewPassword = req.getParameter("confirmNewPassword");

            String error = authService.changePassword(userId, currentPassword, newPassword, confirmNewPassword);
            if (error == null) {
                req.setAttribute("successMessage", "Password updated successfully!");
            } else {
                req.setAttribute("errorMessage", error);
            }
        } else {
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            String yearStr = req.getParameter("year");
            Integer year = null;
            try {
                if (yearStr != null && !yearStr.trim().isEmpty()) {
                    year = Integer.parseInt(yearStr.trim());
                }
            } catch (NumberFormatException ignored) {}

            boolean ok = authService.updateProfile(userId, name, department, year);
            if (ok) {
                session.setAttribute("userName", name);
                session.setAttribute("department", department);
                session.setAttribute("year", year);
                req.setAttribute("successMessage", "Profile details updated successfully!");
            } else {
                req.setAttribute("errorMessage", "Failed to update profile.");
            }
        }

        doGet(req, resp);
    }
}
