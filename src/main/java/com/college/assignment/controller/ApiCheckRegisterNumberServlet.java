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
 * AJAX Endpoint for Real-time Register Number Availability Verification.
 */
@WebServlet(name = "ApiCheckRegisterNumberServlet", urlPatterns = {"/api/check-register-number"})
public class ApiCheckRegisterNumberServlet extends HttpServlet {

    private final AuthenticationService authService = new AuthenticationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String registerNumber = req.getParameter("registerNumber");
        boolean available = authService.isRegisterNumberAvailable(registerNumber);

        JsonObject json = new JsonObject();
        json.addProperty("registerNumber", registerNumber);
        json.addProperty("available", available);
        json.addProperty("message", available ? "Register Number is available." : "Register Number is already registered.");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json.toString());
    }
}
