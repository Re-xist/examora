package com.examora.util;

import java.util.regex.Pattern;

/**
 * Validation Utility Class - Handles input validation
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() &&
               EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null &&
               password.length() >= MIN_PASSWORD_LENGTH &&
               password.length() <= MAX_PASSWORD_LENGTH;
    }

    /**
     * Validate name (non-empty, reasonable length)
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 100;
    }

    /**
     * Sanitize string input (basic XSS prevention)
     */
    public static String sanitize(String input) {
        if (input == null) return null;

        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * Sanitize for HTML output
     */
    public static String sanitizeHtml(String input) {
        if (input == null) return "";
        return sanitize(input);
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validate quiz answer (A, B, C, or D)
     */
    public static boolean isValidAnswer(String answer) {
        return answer != null && answer.matches("[ABCD]");
    }

    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(Integer value) {
        return value != null && value > 0;
    }
}
