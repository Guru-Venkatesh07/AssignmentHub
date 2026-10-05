package com.college.assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BCrypt Password Hashing and Verification.
 */
public class PasswordUtilTest {

    @Test
    @DisplayName("Should generate valid BCrypt hash starting with $2a$")
    void testHashPasswordValid() {
        String rawPassword = "student123";
        String hash = PasswordUtil.hashPassword(rawPassword);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$10$") || hash.startsWith("$2a$"));
        assertTrue(hash.length() >= 60);
    }

    @Test
    @DisplayName("Should successfully verify matching plain-text password against BCrypt hash")
    void testVerifyPasswordSuccess() {
        String rawPassword = "facultySecurePass2026";
        String hash = PasswordUtil.hashPassword(rawPassword);

        assertTrue(PasswordUtil.verifyPassword(rawPassword, hash));
    }

    @Test
    @DisplayName("Should fail verification for incorrect password")
    void testVerifyPasswordFailure() {
        String correctPassword = "correctPassword123";
        String wrongPassword = "wrongPassword123";
        String hash = PasswordUtil.hashPassword(correctPassword);

        assertFalse(PasswordUtil.verifyPassword(wrongPassword, hash));
    }

    @Test
    @DisplayName("Should reject null and empty inputs safely")
    void testNullAndEmptyHandling() {
        assertFalse(PasswordUtil.verifyPassword(null, "$2a$10$dummyHashValue"));
        assertFalse(PasswordUtil.verifyPassword("test", null));
        assertFalse(PasswordUtil.verifyPassword("test", ""));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(null));
    }
}
