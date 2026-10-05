package com.college.assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileUtil file validations and safe filename generation.
 */
public class FileUtilTest {

    @Test
    @DisplayName("Should accept allowed file extensions")
    void testValidFileExtensions() {
        assertTrue(FileUtil.isValidExtension("report.pdf"));
        assertTrue(FileUtil.isValidExtension("document.docx"));
        assertTrue(FileUtil.isValidExtension("presentation.pptx"));
        assertTrue(FileUtil.isValidExtension("source_code.zip"));
    }

    @Test
    @DisplayName("Should reject disallowed file extensions (e.g. .exe, .sh, .jsp, .php)")
    void testInvalidFileExtensions() {
        assertFalse(FileUtil.isValidExtension("malicious.exe"));
        assertFalse(FileUtil.isValidExtension("script.sh"));
        assertFalse(FileUtil.isValidExtension("webshell.jsp"));
        assertFalse(FileUtil.isValidExtension("bad.php"));
        assertFalse(FileUtil.isValidExtension("no_extension"));
    }

    @Test
    @DisplayName("Should enforce 10MB maximum file size limit")
    void testFileSizeValidation() {
        assertTrue(FileUtil.isValidSize(1024 * 1024 * 5)); // 5 MB
        assertTrue(FileUtil.isValidSize(10 * 1024 * 1024)); // exactly 10 MB
        assertFalse(FileUtil.isValidSize(10 * 1024 * 1024 + 1)); // > 10 MB
        assertFalse(FileUtil.isValidSize(0));
        assertFalse(FileUtil.isValidSize(-50));
    }

    @Test
    @DisplayName("Should generate sanitized unique filenames without directory traversal")
    void testSafeFilenameGeneration() {
        String safeName = FileUtil.generateSubmissionFileName(10, 25, "../../../evil.pdf");
        assertTrue(safeName.startsWith("submission_10_25_"));
        assertTrue(safeName.endsWith(".pdf"));
        assertFalse(safeName.contains(".."));
        assertFalse(safeName.contains("/"));
    }
}
