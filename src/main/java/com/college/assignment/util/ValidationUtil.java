package com.college.assignment.util;

import java.util.regex.Pattern;

/**
 * Utility for input validation on the server side.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern REGISTER_NUMBER_PATTERN =
            Pattern.compile("^[A-Za-z0-9]{5,20}$");

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidRegisterNumber(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            return false;
        }
        return REGISTER_NUMBER_PATTERN.matcher(regNo.trim()).matches();
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidMarks(int marks, int maxMarks) {
        return marks >= 0 && marks <= maxMarks && maxMarks > 0;
    }

    /**
     * Sanitizes user input string against HTML tags to prevent XSS.
     */
    public static String sanitizeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
