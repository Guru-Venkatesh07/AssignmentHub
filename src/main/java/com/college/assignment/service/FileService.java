package com.college.assignment.service;

import com.college.assignment.util.FileUtil;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service managing File validation, upload storage, and downloads.
 */
public class FileService {
    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private final String baseUploadDir;

    public FileService(String baseUploadDir) {
        this.baseUploadDir = baseUploadDir;
    }

    public String validateAndUploadAssignmentAttachment(Part filePart, int subjectId, int facultyId) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null; // no attachment uploaded
        }

        String submittedFileName = extractFileName(filePart);
        if (!FileUtil.isValidExtension(submittedFileName)) {
            throw new IllegalArgumentException("Invalid file type. Allowed: PDF, DOCX, PPTX, ZIP.");
        }
        if (!FileUtil.isValidSize(filePart.getSize())) {
            throw new IllegalArgumentException("File size exceeds 10MB limit.");
        }

        String safeFileName = FileUtil.generateAssignmentFileName(subjectId, facultyId, submittedFileName);
        try (InputStream in = filePart.getInputStream()) {
            return FileUtil.saveFile(in, baseUploadDir, "assignments", safeFileName);
        }
    }

    public String validateAndUploadSubmission(Part filePart, int assignmentId, int studentId) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IllegalArgumentException("Please select a file to submit.");
        }

        String submittedFileName = extractFileName(filePart);
        if (!FileUtil.isValidExtension(submittedFileName)) {
            throw new IllegalArgumentException("Invalid file type. Allowed: PDF, DOCX, PPTX, ZIP.");
        }
        if (!FileUtil.isValidSize(filePart.getSize())) {
            throw new IllegalArgumentException("File size exceeds 10MB limit.");
        }

        String safeFileName = FileUtil.generateSubmissionFileName(assignmentId, studentId, submittedFileName);
        try (InputStream in = filePart.getInputStream()) {
            return FileUtil.saveFile(in, baseUploadDir, "submissions", safeFileName);
        }
    }

    public File getFileForDownload(String relativeFilePath) {
        if (relativeFilePath == null || relativeFilePath.trim().isEmpty()) {
            return null;
        }
        Path path = Paths.get(baseUploadDir, relativeFilePath);
        File file = path.toFile();
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    public String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            for (String token : contentDisp.split(";")) {
                if (token.trim().startsWith("filename")) {
                    String fileName = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                    // Handle browser file path separator issues
                    int lastIndex = Math.max(fileName.lastIndexOf('\\'), fileName.lastIndexOf('/'));
                    if (lastIndex >= 0) {
                        fileName = fileName.substring(lastIndex + 1);
                    }
                    return fileName;
                }
            }
        }
        return "file";
    }
}
