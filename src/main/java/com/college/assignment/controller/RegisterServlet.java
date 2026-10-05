package com.college.assignment.controller;

import com.college.assignment.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet handling Student Registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register", "/auth/register"})
public class RegisterServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String registerNumber = req.getParameter("registerNumber");
        String department = req.getParameter("department");
        String yearStr = req.getParameter("year");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        Integer year = null;
        try {
            if (yearStr != null && !yearStr.trim().isEmpty()) {
                year = Integer.parseInt(yearStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        String error = authService.registerStudent(name, email, registerNumber, department, year, password, confirmPassword);

        if (error == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?success=Registration+successful!+Please+sign+in.");
        } else {
            req.setAttribute("errorMessage", error);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("registerNumber", registerNumber);
            req.setAttribute("department", department);
            req.setAttribute("year", yearStr);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
