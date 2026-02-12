package com.examora.controller;

import com.examora.model.User;
import com.examora.service.QuizService;
import com.examora.service.SubmissionService;
import com.examora.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Admin Servlet - Handles admin dashboard and statistics
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private UserService userService;
    private QuizService quizService;
    private SubmissionService submissionService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        quizService = new QuizService();
        submissionService = new SubmissionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("dashboard".equals(action) || action == null) {
                showDashboard(request, response);
            } else if ("users".equals(action)) {
                listUsers(request, response);
            } else if ("statistics".equals(action)) {
                showStatistics(request, response);
            } else {
                showDashboard(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("deleteUser".equals(action)) {
                deleteUser(request, response);
            } else if ("createUser".equals(action)) {
                createUser(request, response);
            } else if ("editUser".equals(action)) {
                editUser(request, response);
            } else if ("resetPassword".equals(action)) {
                resetPassword(request, response);
            } else {
                response.sendRedirect("../AdminServlet?action=dashboard");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");
            if (userIdStr == null || userIdStr.isEmpty()) {
                request.setAttribute("error", "User ID tidak valid");
                listUsers(request, response);
                return;
            }

            Integer userId = Integer.parseInt(userIdStr);

            // Prevent deleting yourself
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser != null && currentUser.getId().equals(userId)) {
                request.setAttribute("error", "Tidak dapat menghapus akun Anda sendiri");
                listUsers(request, response);
                return;
            }

            userService.deleteUser(userId);
            request.setAttribute("success", "User berhasil dihapus");
            listUsers(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "User ID tidak valid");
            listUsers(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Gagal menghapus user: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Nama tidak boleh kosong");
                listUsers(request, response);
                return;
            }
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email tidak boleh kosong");
                listUsers(request, response);
                return;
            }
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Password tidak boleh kosong");
                listUsers(request, response);
                return;
            }

            userService.register(name.trim(), email.trim(), password, role != null ? role : "peserta");
            request.setAttribute("success", "User berhasil dibuat");
            listUsers(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Gagal membuat user: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String role = request.getParameter("role");

            if (userIdStr == null || userIdStr.isEmpty()) {
                request.setAttribute("error", "User ID tidak valid");
                listUsers(request, response);
                return;
            }

            Integer userId = Integer.parseInt(userIdStr);

            // Update user profile
            userService.updateProfile(userId, name, email);

            // Update role if provided
            if (role != null && !role.isEmpty()) {
                userService.updateUserRole(userId, role);
            }

            request.setAttribute("success", "User berhasil diupdate");
            listUsers(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "User ID tidak valid");
            listUsers(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Gagal mengupdate user: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");
            String newPassword = request.getParameter("newPassword");

            if (userIdStr == null || userIdStr.isEmpty()) {
                request.setAttribute("error", "User ID tidak valid");
                listUsers(request, response);
                return;
            }

            if (newPassword == null || newPassword.length() < 6) {
                request.setAttribute("error", "Password baru minimal 6 karakter");
                listUsers(request, response);
                return;
            }

            Integer userId = Integer.parseInt(userIdStr);
            userService.resetPasswordByAdmin(userId, newPassword);

            request.setAttribute("success", "Password berhasil direset");
            listUsers(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "User ID tidak valid");
            listUsers(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Gagal mereset password: " + e.getMessage());
            listUsers(request, response);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int totalUsers = userService.countByRole("peserta");
            int totalAdmins = userService.countByRole("admin");
            List<?> quizzes = quizService.getAllQuizzes();
            List<?> submissions = submissionService.getAllSubmissions();

            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalAdmins", totalAdmins);
            request.setAttribute("totalQuizzes", quizzes.size());
            request.setAttribute("totalSubmissions", submissions.size());
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("submissions", submissions);

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading dashboard", e);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String roleFilter = request.getParameter("role");
            List<User> users;

            if ("admin".equals(roleFilter)) {
                users = userService.getUsersByRole("admin");
            } else if ("peserta".equals(roleFilter)) {
                users = userService.getUsersByRole("peserta");
            } else {
                users = userService.getAllUsers();
            }

            request.setAttribute("users", users);
            request.setAttribute("roleFilter", roleFilter);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading users", e);
        }
    }

    private void showStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String quizIdStr = request.getParameter("quizId");
            Integer quizId = quizIdStr != null && !quizIdStr.isEmpty() ?
                    Integer.parseInt(quizIdStr) : null;

            if (quizId != null) {
                Map<String, Object> stats = submissionService.getQuizStatistics(quizId);
                request.setAttribute("statistics", stats);
                request.setAttribute("selectedQuizId", quizId);
            }

            request.setAttribute("quizzes", quizService.getAllQuizzes());
            request.getRequestDispatcher("/admin/statistics.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading statistics", e);
        }
    }
}
