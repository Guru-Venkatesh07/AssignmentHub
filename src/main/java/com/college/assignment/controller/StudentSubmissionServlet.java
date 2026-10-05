package com.college.assignment.controller;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.Submission;
import com.college.assignment.service.AssignmentService;
import com.college.assignment.service.FileService;
import com.college.assignment.service.SubmissionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

/**
 * Servlet handling Student Assignment submissions (with file upload) and submission history.
 */
@WebServlet(name = "StudentSubmissionServlet", urlPatterns = {"/student/submit", "/student/submissions"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB threshold
        maxFileSize = 1024 * 1024 * 10,       // 10MB max file size
        maxRequestSize = 1024 * 1024 * 12     // 12MB max request size
)
public class StudentSubmissionServlet extends HttpServlet {

    private final SubmissionService submissionService = new SubmissionService();
    private final AssignmentService assignmentService = new AssignmentService();
    private FileService fileService;

    @Override
    public void init() throws ServletException {
        super.init();
        String uploadBaseDir = getServletContext().getRealPath("/uploads");
        if (uploadBaseDir == null) {
            uploadBaseDir = System.getProperty("user.dir") + "/uploads";
        }
        this.fileService = new FileService(uploadBaseDir);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int studentId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();

        if ("/student/submit".equals(path)) {
            String assignmentIdStr = req.getParameter("assignmentId");
            if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/student/assignments");
                return;
            }

            try {
                int assignmentId = Integer.parseInt(assignmentIdStr.trim());
                Assignment assignment = assignmentService.getAssignmentForStudent(assignmentId, studentId);
                if (assignment == null) {
                    resp.sendRedirect(req.getContextPath() + "/student/assignments");
                    return;
                }

                Submission submission = submissionService.getSubmissionForAssignmentAndStudent(assignmentId, studentId);
                boolean canResubmit = submissionService.canStudentResubmit(assignmentId, studentId);

                req.setAttribute("assignment", assignment);
                req.setAttribute("submission", submission);
                req.setAttribute("canResubmit", canResubmit);

                req.getRequestDispatcher("/WEB-INF/views/student/submit.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/student/assignments");
            }
        } else {
            // My Submissions list
            List<Submission> submissions = submissionService.getSubmissionsForStudent(studentId);
            req.setAttribute("submissions", submissions);
            req.getRequestDispatcher("/WEB-INF/views/student/submissions.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int studentId = (Integer) session.getAttribute("userId");

        String assignmentIdStr = req.getParameter("assignmentId");
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/student/assignments");
            return;
        }

        int assignmentId;
        try {
            assignmentId = Integer.parseInt(assignmentIdStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/student/assignments");
            return;
        }

        try {
            Part filePart = req.getPart("submissionFile");
            if (filePart == null || filePart.getSize() == 0) {
                req.setAttribute("errorMessage", "Please select a file to upload.");
                forwardToSubmitPage(req, resp, assignmentId, studentId);
                return;
            }

            String originalFileName = fileService.extractFileName(filePart);
            String savedRelativePath = fileService.validateAndUploadSubmission(filePart, assignmentId, studentId);

            String error = submissionService.submitAssignment(assignmentId, studentId, originalFileName, savedRelativePath);
            if (error == null) {
                resp.sendRedirect(req.getContextPath() + "/student/assignments/view?id=" + assignmentId + "&success=Assignment+submitted+successfully!");
            } else {
                req.setAttribute("errorMessage", error);
                forwardToSubmitPage(req, resp, assignmentId, studentId);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            forwardToSubmitPage(req, resp, assignmentId, studentId);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "File upload failed: " + e.getMessage());
            forwardToSubmitPage(req, resp, assignmentId, studentId);
        }
    }

    private void forwardToSubmitPage(HttpServletRequest req, HttpServletResponse resp, int assignmentId, int studentId) throws ServletException, IOException {
        Assignment assignment = assignmentService.getAssignmentForStudent(assignmentId, studentId);
        Submission submission = submissionService.getSubmissionForAssignmentAndStudent(assignmentId, studentId);
        boolean canResubmit = submissionService.canStudentResubmit(assignmentId, studentId);

        req.setAttribute("assignment", assignment);
        req.setAttribute("submission", submission);
        req.setAttribute("canResubmit", canResubmit);
        req.getRequestDispatcher("/WEB-INF/views/student/submit.jsp").forward(req, resp);
    }
}
