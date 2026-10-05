package com.college.assignment.controller;

import com.college.assignment.service.AssignmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * XML Endpoint serving curriculum subjects/courses in XML format.
 */
@WebServlet(name = "XmlTimetableServlet", urlPatterns = {"/api/timetable.xml"})
public class XmlTimetableServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xml = assignmentService.getTimetableXml();

        resp.setContentType("application/xml");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(xml);
    }
}
