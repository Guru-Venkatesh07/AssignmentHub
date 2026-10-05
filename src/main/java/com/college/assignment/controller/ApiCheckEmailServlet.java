package com.college.assignment.controller;

import com.college.assignment.service.AuthenticationService;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AJAX Endpoint for Real-time Email Availability Verification.
 */
@WebServlet(name = "ApiCheckEmailServlet", urlPatterns = {"/api/check-email"})
public class ApiCheckEmailServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        boolean available = authService.isEmailAvailable(email);

        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("available", available);
        json.addProperty("message", available ? "Email address is available." : "Email is already registered.");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json.toString());
    }
}
