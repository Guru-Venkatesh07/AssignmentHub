package com.college.assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for input validation, email patterns, register numbers, and XSS sanitization.
 */
public class ValidationUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"student@example.com", "guru.v@college.edu", "test.user_1@sub.domain.org"})
    @DisplayName("Should accept valid email formats")
    void testValidEmails(String email) {
        assertTrue(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@domain.com", "user@.com", "", "   "})
    @DisplayName("Should reject invalid email formats")
    void testInvalidEmails(String email) {
        assertFalse(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"23CS088", "23IT012", "REG12345", "STD99999"})
    @DisplayName("Should accept valid register number formats")
    void testValidRegisterNumbers(String regNo) {
        assertTrue(ValidationUtil.isValidRegisterNumber(regNo));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "a", "", "   ", "REG#$@!"})
    @DisplayName("Should reject invalid register numbers")
    void testInvalidRegisterNumbers(String regNo) {
        assertFalse(ValidationUtil.isValidRegisterNumber(regNo));
    }

    @Test
    @DisplayName("Should validate marks within allowed max bounds")
    void testMarksValidation() {
        assertTrue(ValidationUtil.isValidMarks(0, 100));
        assertTrue(ValidationUtil.isValidMarks(50, 100));
        assertTrue(ValidationUtil.isValidMarks(100, 100));

        assertFalse(ValidationUtil.isValidMarks(-1, 100));
        assertFalse(ValidationUtil.isValidMarks(105, 100));
        assertFalse(ValidationUtil.isValidMarks(50, 0));
    }

    @Test
    @DisplayName("Should sanitize HTML characters to prevent XSS")
    void testSanitizeHtml() {
        String unsafe = "<script>alert('xss');</script>&\"test\"";
        String safe = ValidationUtil.sanitizeHtml(unsafe);

        assertFalse(safe.contains("<script>"));
        assertTrue(safe.contains("&lt;script&gt;"));
        assertTrue(safe.contains("&amp;"));
    }
}
