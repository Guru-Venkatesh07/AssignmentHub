package com.college.assignment.controller;

import com.college.assignment.model.Evaluation;
import com.college.assignment.model.Submission;
import com.college.assignment.service.EvaluationService;
import com.college.assignment.service.SubmissionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for Faculty Evaluation of student submissions (Assign marks and feedback).
 */
@WebServlet(name = "FacultyEvaluationServlet", urlPatterns = {"/faculty/evaluate"})
public class FacultyEvaluationServlet extends HttpServlet {

    private final SubmissionService submissionService = new SubmissionService();
    private final EvaluationService evaluationService = new EvaluationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String subIdStr = req.getParameter("submissionId");
        if (subIdStr == null || subIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/faculty/submissions");
            return;
        }

        try {
            int submissionId = Integer.parseInt(subIdStr.trim());
            Submission submission = submissionService.getSubmissionById(submissionId);
            if (submission == null) {
                resp.sendRedirect(req.getContextPath() + "/faculty/submissions");
                return;
            }

            Evaluation evaluation = evaluationService.getEvaluationBySubmissionId(submissionId);

            req.setAttribute("submission", submission);
            req.setAttribute("evaluation", evaluation);

            req.getRequestDispatcher("/WEB-INF/views/faculty/evaluate.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/faculty/submissions");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int facultyId = (Integer) session.getAttribute("userId");

        String subIdStr = req.getParameter("submissionId");
        String marksStr = req.getParameter("marks");
        String feedback = req.getParameter("feedback");

        int submissionId;
        int marks;
        try {
            submissionId = Integer.parseInt(subIdStr);
            marks = Integer.parseInt(marksStr);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid numeric value for marks or submission ID.");
            doGet(req, resp);
            return;
        }

        String error = evaluationService.evaluateSubmission(submissionId, facultyId, marks, feedback);
        if (error == null) {
            Submission s = submissionService.getSubmissionById(submissionId);
            int assignmentId = (s != null) ? s.getAssignmentId() : 0;
            resp.sendRedirect(req.getContextPath() + "/faculty/submissions?assignmentId=" + assignmentId + "&success=Submission+evaluated+successfully!");
        } else {
            req.setAttribute("errorMessage", error);
            doGet(req, resp);
        }
    }
}
