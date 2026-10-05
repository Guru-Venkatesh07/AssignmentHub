package com.college.assignment.controller;

import com.college.assignment.dao.FacultySubjectDAO;
import com.college.assignment.dao.SubjectDAO;
import com.college.assignment.model.Assignment;
import com.college.assignment.model.FacultySubject;
import com.college.assignment.model.Subject;
import com.college.assignment.service.AssignmentService;
import com.college.assignment.service.FileService;
import com.college.assignment.util.DateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Servlet handling Faculty Assignment Management (CREATE, READ, UPDATE, DELETE).
 */
@WebServlet(name = "FacultyAssignmentServlet", urlPatterns = {
        "/faculty/assignments",
        "/faculty/assignments/create",
        "/faculty/assignments/edit",
        "/faculty/assignments/delete"
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 12
)
public class FacultyAssignmentServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final FacultySubjectDAO facultySubjectDAO = new FacultySubjectDAO();
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
        int facultyId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();

        if ("/faculty/assignments/create".equals(path)) {
            List<FacultySubject> assignedSubjects = facultySubjectDAO.findByFacultyId(facultyId);
            List<Subject> subjects = assignedSubjects.isEmpty() ? subjectDAO.findAll() : null;
            req.setAttribute("assignedSubjects", assignedSubjects);
            req.setAttribute("allSubjects", subjects);
            req.getRequestDispatcher("/WEB-INF/views/faculty/create-assignment.jsp").forward(req, resp);
        } else if ("/faculty/assignments/edit".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null) {
                resp.sendRedirect(req.getContextPath() + "/faculty/assignments");
                return;
            }
            try {
                int id = Integer.parseInt(idStr);
                Assignment assignment = assignmentService.getAssignmentById(id);
                if (assignment == null || assignment.getFacultyId() != facultyId) {
                    req.setAttribute("errorMessage", "Assignment not found or access denied.");
                    req.getRequestDispatcher("/WEB-INF/views/errors/403.jsp").forward(req, resp);
                    return;
                }
                List<FacultySubject> assignedSubjects = facultySubjectDAO.findByFacultyId(facultyId);
                req.setAttribute("assignment", assignment);
                req.setAttribute("assignedSubjects", assignedSubjects.isEmpty() ? null : assignedSubjects);
                req.setAttribute("allSubjects", assignedSubjects.isEmpty() ? subjectDAO.findAll() : null);
                req.getRequestDispatcher("/WEB-INF/views/faculty/edit-assignment.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/faculty/assignments");
            }
        } else {
            // Assignment List
            List<Assignment> assignments = assignmentService.getAssignmentsForFaculty(facultyId);
            req.setAttribute("assignments", assignments);
            req.getRequestDispatcher("/WEB-INF/views/faculty/assignments.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int facultyId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();

        if ("/faculty/assignments/delete".equals(path)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                boolean deleted = assignmentService.deleteAssignment(id, facultyId);
                if (deleted) {
                    resp.sendRedirect(req.getContextPath() + "/faculty/assignments?success=Assignment+deleted+successfully.");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/faculty/assignments?error=Failed+to+delete+assignment.");
                }
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/faculty/assignments?error=Invalid+assignment+ID.");
            }
        } else if ("/faculty/assignments/create".equals(path)) {
            handleCreateAssignment(req, resp, facultyId);
        } else if ("/faculty/assignments/edit".equals(path)) {
            handleEditAssignment(req, resp, facultyId);
        }
    }

    private void handleCreateAssignment(HttpServletRequest req, HttpServletResponse resp, int facultyId) throws ServletException, IOException {
        String title = req.getParameter("title");
        String subjectIdStr = req.getParameter("subjectId");
        String description = req.getParameter("description");
        String instructions = req.getParameter("instructions");
        String dueDateStr = req.getParameter("dueDate");
        String dueTimeStr = req.getParameter("dueTime");
        String maxMarksStr = req.getParameter("maxMarks");

        int subjectId = 0;
        int maxMarks = 100;
        try {
            subjectId = Integer.parseInt(subjectIdStr);
            if (maxMarksStr != null && !maxMarksStr.trim().isEmpty()) {
                maxMarks = Integer.parseInt(maxMarksStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        Date dueDate = DateUtil.parseDate(dueDateStr);
        Time dueTime = DateUtil.parseTime(dueTimeStr);

        String attachmentPath = null;
        try {
            Part filePart = req.getPart("attachment");
            if (filePart != null && filePart.getSize() > 0) {
                attachmentPath = fileService.validateAndUploadAssignmentAttachment(filePart, subjectId, facultyId);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Attachment upload error: " + e.getMessage());
            doGet(req, resp);
            return;
        }

        String error = assignmentService.createAssignment(facultyId, subjectId, title, description, instructions, attachmentPath, dueDate, dueTime, maxMarks);
        if (error == null) {
            resp.sendRedirect(req.getContextPath() + "/faculty/assignments?success=Assignment+created+successfully!");
        } else {
            req.setAttribute("errorMessage", error);
            req.setAttribute("title", title);
            req.setAttribute("description", description);
            req.setAttribute("instructions", instructions);
            req.setAttribute("dueDate", dueDateStr);
            req.setAttribute("dueTime", dueTimeStr);
            req.setAttribute("maxMarks", maxMarksStr);
            req.setAttribute("selectedSubjectId", subjectId);
            doGet(req, resp);
        }
    }

    private void handleEditAssignment(HttpServletRequest req, HttpServletResponse resp, int facultyId) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String subjectIdStr = req.getParameter("subjectId");
        String description = req.getParameter("description");
        String instructions = req.getParameter("instructions");
        String dueDateStr = req.getParameter("dueDate");
        String dueTimeStr = req.getParameter("dueTime");
        String maxMarksStr = req.getParameter("maxMarks");
        String statusStr = req.getParameter("status");

        int id = Integer.parseInt(idStr);
        int subjectId = Integer.parseInt(subjectIdStr);
        int maxMarks = Integer.parseInt(maxMarksStr);
        Date dueDate = DateUtil.parseDate(dueDateStr);
        Time dueTime = DateUtil.parseTime(dueTimeStr);
        Assignment.Status status = (statusStr != null) ? Assignment.Status.valueOf(statusStr) : Assignment.Status.ACTIVE;

        String attachmentPath = null;
        try {
            Part filePart = req.getPart("attachment");
            if (filePart != null && filePart.getSize() > 0) {
                attachmentPath = fileService.validateAndUploadAssignmentAttachment(filePart, subjectId, facultyId);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Attachment upload error: " + e.getMessage());
            doGet(req, resp);
            return;
        }

        String error = assignmentService.updateAssignment(id, facultyId, subjectId, title, description, instructions, attachmentPath, dueDate, dueTime, maxMarks, status);
        if (error == null) {
            resp.sendRedirect(req.getContextPath() + "/faculty/assignments?success=Assignment+updated+successfully!");
        } else {
            req.setAttribute("errorMessage", error);
            doGet(req, resp);
        }
    }
}
