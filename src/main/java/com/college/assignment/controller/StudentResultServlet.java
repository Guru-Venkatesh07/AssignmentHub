package com.college.assignment.controller;

import com.college.assignment.dao.EvaluationDAO;
import com.college.assignment.model.Submission;
import com.college.assignment.service.EvaluationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet handling Student Results and Evaluation View.
 */
@WebServlet(name = "StudentResultServlet", urlPatterns = {"/student/results"})
public class StudentResultServlet extends HttpServlet {

    private final EvaluationService evaluationService = new EvaluationService();
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int studentId = (Integer) session.getAttribute("userId");

        List<Submission> results = evaluationService.getStudentResults(studentId);
        double avgPercentage = evaluationDAO.getAveragePercentageForStudent(studentId);

        req.setAttribute("results", results);
        req.setAttribute("averagePercentage", avgPercentage);

        req.getRequestDispatcher("/WEB-INF/views/student/results.jsp").forward(req, resp);
    }
}
