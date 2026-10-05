package com.college.assignment.controller;

import com.college.assignment.service.AssignmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * XML Endpoint serving live assignment curriculum data in standard XML format.
 * Demonstrates XML data exchange for the Web Technology Laboratory syllabus.
 */
@WebServlet(name = "XmlAssignmentServlet", urlPatterns = {"/api/assignments.xml"})
public class XmlAssignmentServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xml = assignmentService.getAssignmentsXml();

        resp.setContentType("application/xml");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(xml);
    }
}
