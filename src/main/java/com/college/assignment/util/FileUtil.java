package com.college.assignment.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility for secure file upload, validation, unique filename generation, and storage.
 */
public class FileUtil {
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());

    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf", "docx", "doc", "pptx", "ppt", "zip"
    ));

    /**
     * Checks if the uploaded file has an allowed extension.
     */
    public static boolean isValidExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return false;
        }
        String ext = getFileExtension(fileName).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(ext);
    }

    /**
     * Checks if the file size is within the allowed 10MB limit.
     */
    public static boolean isValidSize(long size) {
        return size > 0 && size <= MAX_FILE_SIZE;
    }

    /**
     * Extracts extension without dot.
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Generates a safe server-side filename for assignment attachments.
     */
    public static String generateAssignmentFileName(int subjectId, int facultyId, String originalFileName) {
        String ext = getFileExtension(originalFileName);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "assignment_" + subjectId + "_" + facultyId + "_" + timestamp + (ext.isEmpty() ? "" : "." + ext);
    }

    /**
     * Generates a safe server-side filename for student submissions.
     */
    public static String generateSubmissionFileName(int assignmentId, int studentId, String originalFileName) {
        String ext = getFileExtension(originalFileName);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "submission_" + assignmentId + "_" + studentId + "_" + timestamp + (ext.isEmpty() ? "" : "." + ext);
    }

    /**
     * Saves an input stream to disk and returns the relative file path.
     */
    public static String saveFile(InputStream inputStream, String baseUploadDir, String subDir, String safeFileName) throws IOException {
        Path uploadPath = Paths.get(baseUploadDir, subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetPath = uploadPath.resolve(safeFileName);
        try (FileOutputStream out = new FileOutputStream(targetPath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        LOGGER.info("File saved successfully to: " + targetPath.toAbsolutePath());
        return subDir + "/" + safeFileName;
    }

    /**
     * Deletes a file if it exists.
     */
    public static boolean deleteFile(String baseUploadDir, String relativeFilePath) {
        if (relativeFilePath == null || relativeFilePath.trim().isEmpty()) {
            return false;
        }
        try {
            Path path = Paths.get(baseUploadDir, relativeFilePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to delete file: " + relativeFilePath + " | " + e.getMessage());
            return false;
        }
    }
}
