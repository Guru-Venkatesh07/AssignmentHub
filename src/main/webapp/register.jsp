<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Registration &bull; Assignment &amp; Submission Management System</title>
    <!-- Fonts & Icons -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body class="auth-wrapper" data-context-path="${pageContext.request.contextPath}">

    <div class="auth-card" style="max-width: 540px;">
        <div class="auth-brand">
            <div class="logo-icon">
                <i class="fa-solid fa-user-plus"></i>
            </div>
            <h2>Create Student Account</h2>
            <p class="text-muted" style="font-size: 0.9rem; margin-top: 0.25rem;">Register to submit and manage academic assignments</p>
        </div>

        <!-- Flash Alert Messages -->
        <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

        <form action="${pageContext.request.contextPath}/register" method="post" id="registrationForm">
            <div class="form-group">
                <label for="name" class="form-label">Full Name <span class="text-danger">*</span></label>
                <input type="text" id="name" name="name" class="form-control" placeholder="e.g. Guru V" value="${name}" required>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="regNoInput" class="form-label">Register Number <span class="text-danger">*</span></label>
                    <input type="text" id="regNoInput" name="registerNumber" class="form-control" placeholder="e.g. 23CS088" value="${registerNumber}" required>
                    <div id="regNoFeedback" class="form-text"></div>
                </div>

                <div class="form-group">
                    <label for="emailInput" class="form-label">Email Address <span class="text-danger">*</span></label>
                    <input type="email" id="emailInput" name="email" class="form-control" placeholder="e.g. student@example.com" value="${email}" required>
                    <div id="emailFeedback" class="form-text"></div>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="department" class="form-label">Department <span class="text-danger">*</span></label>
                    <select id="department" name="department" class="form-select" required>
                        <option value="">Select Department</option>
                        <option value="Computer Science &amp; Engineering" ${department == 'Computer Science & Engineering' ? 'selected' : ''}>Computer Science &amp; Engineering</option>
                        <option value="Information Technology" ${department == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                        <option value="Electronics &amp; Communication" ${department == 'Electronics & Communication' ? 'selected' : ''}>Electronics &amp; Communication</option>
                        <option value="Electrical &amp; Electronics" ${department == 'Electrical & Electronics' ? 'selected' : ''}>Electrical &amp; Electronics</option>
                        <option value="Mechanical Engineering" ${department == 'Mechanical Engineering' ? 'selected' : ''}>Mechanical Engineering</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="year" class="form-label">Year <span class="text-danger">*</span></label>
                    <select id="year" name="year" class="form-select" required>
                        <option value="1" ${year == '1' ? 'selected' : ''}>1st Year</option>
                        <option value="2" ${year == '2' ? 'selected' : ''}>2nd Year</option>
                        <option value="3" ${year == '3' || empty year ? 'selected' : ''}>3rd Year</option>
                        <option value="4" ${year == '4' ? 'selected' : ''}>4th Year</option>
                    </select>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="passwordInput" class="form-label">Password <span class="text-danger">*</span></label>
                    <input type="password" id="passwordInput" name="password" class="form-control" placeholder="Min. 6 characters" required>
                </div>

                <div class="form-group">
                    <label for="confirmPasswordInput" class="form-label">Confirm Password <span class="text-danger">*</span></label>
                    <input type="password" id="confirmPasswordInput" name="confirmPassword" class="form-control" placeholder="Re-enter password" required>
                </div>
            </div>
            <div id="passwordFeedback" class="form-text" style="margin-top: -0.5rem; margin-bottom: 1.25rem;"></div>

            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem; font-size: 0.95rem;">
                <i class="fa-solid fa-user-check"></i> Register Student Account
            </button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.88rem; color: var(--text-muted);">
            Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" class="fw-semibold">Sign in here</a>
        </div>
        <div style="margin-top: 0.75rem; text-align: center;">
            <a href="${pageContext.request.contextPath}/" style="font-size: 0.82rem; color: var(--gray-500);">
                <i class="fa-solid fa-arrow-left"></i> Back to Homepage
            </a>
        </div>
    </div>

    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
</body>
</html>
