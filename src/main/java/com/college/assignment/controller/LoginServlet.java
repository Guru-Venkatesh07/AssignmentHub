package com.college.assignment.controller;

import com.college.assignment.model.User;
import com.college.assignment.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet handling User Authentication and Remember Me Cookie operations.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/auth/login"})
public class LoginServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            User.Role role = (User.Role) session.getAttribute("userRole");
            if (role == User.Role.FACULTY) {
                resp.sendRedirect(req.getContextPath() + "/faculty/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            }
            return;
        }

        // Cookie demonstration: check for remembered username/email
        String rememberedEmail = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("rememberedUsername".equals(c.getName())) {
                    rememberedEmail = c.getValue();
                    break;
                }
            }
        }

        req.setAttribute("rememberedEmail", rememberedEmail);
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        User user = authService.login(email, password);

        if (user != null) {
            // Prevent session fixation
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("registerNumber", user.getRegisterNumber());
            session.setAttribute("department", user.getDepartment());
            session.setAttribute("year", user.getYear());

            // Handle Remember Me Cookie
            if ("true".equalsIgnoreCase(rememberMe) || "on".equalsIgnoreCase(rememberMe)) {
                Cookie cookie = new Cookie("rememberedUsername", user.getEmail());
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                cookie.setHttpOnly(true);
                resp.addCookie(cookie);
            } else {
                // Clear cookie if unchecked
                Cookie cookie = new Cookie("rememberedUsername", "");
                cookie.setMaxAge(0);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);
            }

            if (user.getRole() == User.Role.FACULTY) {
                resp.sendRedirect(req.getContextPath() + "/faculty/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            }
        } else {
            req.setAttribute("errorMessage", "Invalid email or password. Please try again.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
