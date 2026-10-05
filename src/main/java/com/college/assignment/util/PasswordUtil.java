package com.college.assignment.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for BCrypt password hashing and verification.
 */
public class PasswordUtil {

    private static final int BCRYPT_LOG_ROUNDS = 10;

    /**
     * Hashes a plain-text password using BCrypt with salt rounds = 10.
     * @param plainPassword plain-text password
     * @return BCrypt hashed string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty or null");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    /**
     * Verifies a plain-text candidate password against a stored BCrypt hash.
     * @param plainPassword plain-text candidate
     * @param hashedPassword stored BCrypt hash
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
