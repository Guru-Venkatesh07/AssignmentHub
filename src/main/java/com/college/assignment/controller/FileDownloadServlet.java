package com.college.assignment.controller;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.Submission;
import com.college.assignment.service.AssignmentService;
import com.college.assignment.service.FileService;
import com.college.assignment.service.SubmissionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Servlet handling secure file downloads for Assignment Attachments and Student Submissions.
 */
@WebServlet(name = "FileDownloadServlet", urlPatterns = {"/download/attachment", "/download/submission"})
public class FileDownloadServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final SubmissionService submissionService = new SubmissionService();
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
        String path = req.getServletPath();

        if ("/download/attachment".equals(path)) {
            String assignmentIdStr = req.getParameter("assignmentId");
            if (assignmentIdStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing assignment ID.");
                return;
            }
            try {
                int id = Integer.parseInt(assignmentIdStr);
                Assignment assignment = assignmentService.getAssignmentById(id);
                if (assignment == null || assignment.getAttachmentPath() == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Attachment file not found.");
                    return;
                }
                File file = fileService.getFileForDownload(assignment.getAttachmentPath());
                if (file == null || !file.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Physical file not found on server.");
                    return;
                }
                serveFile(resp, file, file.getName());
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid assignment ID.");
            }
        } else if ("/download/submission".equals(path)) {
            String submissionIdStr = req.getParameter("submissionId");
            if (submissionIdStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing submission ID.");
                return;
            }
            try {
                int id = Integer.parseInt(submissionIdStr);
                Submission sub = submissionService.getSubmissionById(id);
                if (sub == null || sub.getFilePath() == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Submission file record not found.");
                    return;
                }
                File file = fileService.getFileForDownload(sub.getFilePath());
                if (file == null || !file.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Physical file not found on server.");
                    return;
                }
                serveFile(resp, file, sub.getFileName());
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid submission ID.");
            }
        }
    }

    private void serveFile(HttpServletResponse resp, File file, String downloadName) throws IOException {
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        resp.setContentType(mimeType);
        resp.setContentLengthLong(file.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + downloadName.replace("\"", "") + "\"");

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
