<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examora.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    String redirectUrl = "";
    if (user != null) {
        redirectUrl = user.isAdmin() ? "admin/dashboard.jsp" : "user/dashboard.jsp";
        response.sendRedirect(redirectUrl);
        return;
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Examora - Smart Assessment Platform</title>
    <meta name="description" content="Modern, Secure, and Scalable Online Examination System">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="assets/css/style.css" rel="stylesheet">
</head>
<body class="landing-page">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">
                <i class="bi bi-journal-check me-2"></i>Examora
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#features">Fitur</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#about">Tentang</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-light ms-2" href="LoginServlet">
                            <i class="bi bi-box-arrow-in-right me-1"></i>Masuk
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-outline-light ms-2" href="RegisterServlet">
                            <i class="bi bi-person-plus me-1"></i>Daftar
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <div class="row align-items-center min-vh-100">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold text-white mb-4">
                        Examora
                        <span class="d-block text-warning">Smart Assessment Platform</span>
                    </h1>
                    <p class="lead text-white-50 mb-4">
                        Modern, Secure, and Scalable Online Examination System.
                        Platform ujian online modern yang aman dan dapat diandalkan untuk
                        sekolah, corporate training, dan sertifikasi.
                    </p>
                    <div class="d-flex gap-3">
                        <a href="RegisterServlet" class="btn btn-warning btn-lg px-4">
                            <i class="bi bi-rocket-takeoff me-2"></i>Mulai Sekarang
                        </a>
                        <a href="#features" class="btn btn-outline-light btn-lg px-4">
                            Pelajari Lebih Lanjut
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 text-center">
                    <div class="hero-illustration">
                        <i class="bi bi-journal-richtext" style="font-size: 15rem; color: rgba(255,255,255,0.2);"></i>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section id="features" class="py-5 bg-light">
        <div class="container py-5">
            <div class="text-center mb-5">
                <h2 class="display-6 fw-bold">Fitur Unggulan</h2>
                <p class="text-muted">Semua yang Anda butuhkan untuk sistem ujian online</p>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-primary text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-speedometer2"></i>
                            </div>
                            <h5 class="card-title">Auto Koreksi</h5>
                            <p class="card-text text-muted">
                                Sistem koreksi otomatis yang cepat dan akurat dengan perhitungan skor real-time.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-success text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-shield-check"></i>
                            </div>
                            <h5 class="card-title">Aman & Terpercaya</h5>
                            <p class="card-text text-muted">
                                Password hashing, CSRF protection, dan session management yang aman.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-info text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-stopwatch"></i>
                            </div>
                            <h5 class="card-title">Timer Ujian</h5>
                            <p class="card-text text-muted">
                                Timer dengan countdown dan auto-submit saat waktu habis.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-warning text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-graph-up"></i>
                            </div>
                            <h5 class="card-title">Dashboard Statistik</h5>
                            <p class="card-text text-muted">
                                Lihat rata-rata nilai, distribusi skor, dan performa peserta.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-danger text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-people"></i>
                            </div>
                            <h5 class="card-title">Multi-Role</h5>
                            <p class="card-text text-muted">
                                Sistem role admin dan peserta dengan akses yang berbeda.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon bg-secondary text-white rounded-circle mb-3 mx-auto">
                                <i class="bi bi-phone"></i>
                            </div>
                            <h5 class="card-title">Responsive Design</h5>
                            <p class="card-text text-muted">
                                Tampilan yang optimal di desktop, tablet, dan smartphone.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- About Section -->
    <section id="about" class="py-5">
        <div class="container py-5">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h2 class="display-6 fw-bold mb-4">Tentang Examora</h2>
                    <p class="lead text-muted mb-4">
                        Examora adalah platform ujian online yang dirancang untuk kebutuhan
                        modern dengan fokus pada keamanan, kecepatan, dan kemudahan penggunaan.
                    </p>
                    <ul class="list-unstyled">
                        <li class="mb-3">
                            <i class="bi bi-check-circle-fill text-success me-2"></i>
                            Cocok untuk ujian sekolah dan tryout
                        </li>
                        <li class="mb-3">
                            <i class="bi bi-check-circle-fill text-success me-2"></i>
                            Ideal untuk corporate training dan sertifikasi
                        </li>
                        <li class="mb-3">
                            <i class="bi bi-check-circle-fill text-success me-2"></i>
                            Siap untuk deployment produksi
                        </li>
                        <li class="mb-3">
                            <i class="bi bi-check-circle-fill text-success me-2"></i>
                            Arsitektur yang scalable dan maintainable
                        </li>
                    </ul>
                </div>
                <div class="col-lg-6">
                    <div class="card border-0 shadow-lg">
                        <div class="card-body p-5">
                            <h4 class="mb-4">Default Login</h4>
                            <div class="mb-3">
                                <strong>Admin:</strong>
                                <ul class="mt-2 mb-3">
                                    <li>Email: admin@examora.com</li>
                                    <li>Password: admin123</li>
                                </ul>
                            </div>
                            <div class="mb-3">
                                <strong>Peserta:</strong>
                                <ul class="mt-2">
                                    <li>Email: user@examora.com</li>
                                    <li>Password: user123</li>
                                </ul>
                            </div>
                            <a href="LoginServlet" class="btn btn-primary w-100">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Login Sekarang
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white py-4">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5><i class="bi bi-journal-check me-2"></i>Examora</h5>
                    <p class="text-muted mb-0">
                        Modern, Secure, and Scalable Online Examination System
                    </p>
                </div>
                <div class="col-md-6 text-md-end">
                    <p class="mb-0 text-muted">
                        &copy; 2024 Examora. All rights reserved.
                    </p>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
