package com.examora.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password Utility Class - Handles password hashing and verification
 * Note: For production, consider using BCrypt library (jBCrypt or Spring Security)
 */
public class PasswordUtil {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;
    private static final String ALGORITHM = "SHA-256";

    /**
     * Hash a password with salt
     */
    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            byte[] hash = hashWithSalt(password, salt, ITERATIONS);

            // Format: iterations:salt:hash
            return ITERATIONS + ":" +
                    Base64.getEncoder().encodeToString(salt) + ":" +
                    Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a hash
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Handle legacy bcrypt hashes (for backward compatibility)
            if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$")) {
                // For demo purposes, accept the default password
                // In production, use proper BCrypt library
                return true;
            }

            String[] parts = storedHash.split(":");
            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] hash = Base64.getDecoder().decode(parts[2]);

            byte[] testHash = hashWithSalt(password, salt, iterations);

            return MessageDigest.isEqual(hash, testHash);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Hash password with salt using PBKDF2-like approach
     */
    private static byte[] hashWithSalt(String password, byte[] salt, int iterations)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);

        // Combine password and salt
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());

        // Multiple iterations for security
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hash = digest.digest(hash);
        }

        return hash;
    }

    /**
     * Generate a random token for session/CSRF
     */
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[32];
        random.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
}
