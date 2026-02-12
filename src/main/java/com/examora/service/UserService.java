package com.examora.service;

import com.examora.dao.UserDAO;
import com.examora.model.User;
import com.examora.util.PasswordUtil;
import com.examora.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * User Service - Business logic for user operations
 */
public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     */
    public User register(String name, String email, String password, String role) throws ServiceException {
        // Validate inputs
        if (!ValidationUtil.isValidName(name)) {
            throw new ServiceException("Nama tidak valid");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ServiceException("Format email tidak valid");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new ServiceException("Password minimal 6 karakter");
        }

        try {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                throw new ServiceException("Email sudah terdaftar");
            }

            // Hash password
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Create user
            User user = new User(name, email, hashedPassword, role != null ? role : "peserta");
            return userDAO.create(user);

        } catch (SQLException e) {
            throw new ServiceException("Gagal mendaftarkan user: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticate user
     */
    public User authenticate(String email, String password) throws ServiceException {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ServiceException("Format email tidak valid");
        }

        try {
            User user = userDAO.findByEmail(email);

            if (user == null) {
                throw new ServiceException("Email atau password salah");
            }

            if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                throw new ServiceException("Email atau password salah");
            }

            return user;

        } catch (SQLException e) {
            throw new ServiceException("Gagal melakukan autentikasi: " + e.getMessage(), e);
        }
    }

    /**
     * Get user by ID
     */
    public User getUserById(Integer id) throws ServiceException {
        try {
            User user = userDAO.findById(id);
            if (user == null) {
                throw new ServiceException("User tidak ditemukan");
            }
            return user;
        } catch (SQLException e) {
            throw new ServiceException("Gagal mengambil data user: " + e.getMessage(), e);
        }
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            throw new ServiceException("Gagal mengambil data users: " + e.getMessage(), e);
        }
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(String role) throws ServiceException {
        try {
            return userDAO.findByRole(role);
        } catch (SQLException e) {
            throw new ServiceException("Gagal mengambil data users: " + e.getMessage(), e);
        }
    }

    /**
     * Update user profile
     */
    public User updateProfile(Integer id, String name, String email) throws ServiceException {
        if (!ValidationUtil.isValidName(name)) {
            throw new ServiceException("Nama tidak valid");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ServiceException("Format email tidak valid");
        }

        try {
            User user = userDAO.findById(id);
            if (user == null) {
                throw new ServiceException("User tidak ditemukan");
            }

            // Check if email is taken by another user
            User existingUser = userDAO.findByEmail(email);
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new ServiceException("Email sudah digunakan oleh user lain");
            }

            user.setName(name);
            user.setEmail(email);
            userDAO.update(user);

            return user;
        } catch (SQLException e) {
            throw new ServiceException("Gagal mengupdate profil: " + e.getMessage(), e);
        }
    }

    /**
     * Change password
     */
    public void changePassword(Integer id, String oldPassword, String newPassword) throws ServiceException {
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new ServiceException("Password baru minimal 6 karakter");
        }

        try {
            User user = userDAO.findById(id);
            if (user == null) {
                throw new ServiceException("User tidak ditemukan");
            }

            if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
                throw new ServiceException("Password lama salah");
            }

            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePassword(id, hashedPassword);

        } catch (SQLException e) {
            throw new ServiceException("Gagal mengubah password: " + e.getMessage(), e);
        }
    }

    /**
     * Reset password by admin (without old password verification)
     */
    public void resetPasswordByAdmin(Integer id, String newPassword) throws ServiceException {
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new ServiceException("Password baru minimal 6 karakter");
        }

        try {
            User user = userDAO.findById(id);
            if (user == null) {
                throw new ServiceException("User tidak ditemukan");
            }

            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            userDAO.updatePassword(id, hashedPassword);

        } catch (SQLException e) {
            throw new ServiceException("Gagal mereset password: " + e.getMessage(), e);
        }
    }

    /**
     * Update user role (admin only)
     */
    public void updateUserRole(Integer id, String role) throws ServiceException {
        try {
            User user = userDAO.findById(id);
            if (user == null) {
                throw new ServiceException("User tidak ditemukan");
            }

            if (!role.equals("admin") && !role.equals("peserta")) {
                throw new ServiceException("Role tidak valid");
            }

            user.setRole(role);
            userDAO.update(user);

        } catch (SQLException e) {
            throw new ServiceException("Gagal mengupdate role: " + e.getMessage(), e);
        }
    }

    /**
     * Delete user
     */
    public void deleteUser(Integer id) throws ServiceException {
        try {
            if (!userDAO.delete(id)) {
                throw new ServiceException("Gagal menghapus user");
            }
        } catch (SQLException e) {
            throw new ServiceException("Gagal menghapus user: " + e.getMessage(), e);
        }
    }

    /**
     * Count users by role
     */
    public int countByRole(String role) throws ServiceException {
        try {
            return userDAO.countByRole(role);
        } catch (SQLException e) {
            throw new ServiceException("Gagal menghitung users: " + e.getMessage(), e);
        }
    }

    /**
     * Service Exception
     */
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }

        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
