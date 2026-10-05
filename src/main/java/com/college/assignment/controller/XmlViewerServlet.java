package com.college.assignment.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for the XML Laboratory Demonstration Viewer Page.
 */
@WebServlet(name = "XmlViewerServlet", urlPatterns = {"/xml-viewer"})
public class XmlViewerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/xml-viewer.jsp").forward(req, resp);
    }
}
