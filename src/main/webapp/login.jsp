<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In &bull; Assignment &amp; Submission Management System</title>
    <!-- Fonts & Icons -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body class="auth-wrapper" data-context-path="${pageContext.request.contextPath}">

    <div class="auth-card">
        <div class="auth-brand">
            <div class="logo-icon">
                <i class="fa-solid fa-graduation-cap"></i>
            </div>
            <h2>Welcome Back</h2>
            <p class="text-muted" style="font-size: 0.9rem; margin-top: 0.25rem;">Sign in to your academic portal</p>
        </div>

        <!-- Flash Alert Messages -->
        <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <div style="position: relative;">
                    <input type="email" id="email" name="email" class="form-control" 
                           placeholder="e.g. student@example.com" 
                           value="${not empty rememberedEmail ? rememberedEmail : (not empty email ? email : '')}" 
                           required autofocus>
                </div>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Enter your account password" required>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <label class="form-check">
                    <input type="checkbox" name="rememberMe" value="true" class="form-check-input" ${not empty rememberedEmail ? 'checked' : ''}>
                    <span style="font-size: 0.85rem; color: var(--gray-700);">Remember Username (Cookie)</span>
                </label>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem; font-size: 0.95rem;">
                <i class="fa-solid fa-arrow-right-to-bracket"></i> Sign In to Account
            </button>
        </form>

        <!-- Quick Demo Accounts Helper -->
        <div style="margin-top: 1.75rem; padding-top: 1.25rem; border-top: 1px solid var(--border-color); text-align: center;">
            <p style="font-size: 0.78rem; font-weight: 700; color: var(--gray-500); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.75rem;">
                Demo Credentials (Click to Autofill)
            </p>
            <div style="display: flex; gap: 0.5rem; justify-content: center;">
                <button type="button" class="btn btn-sm btn-outline" onclick="fillDemo('student@example.com', 'student123')" style="font-size: 0.8rem;">
                    <i class="fa-solid fa-user-graduate"></i> Student Demo
                </button>
                <button type="button" class="btn btn-sm btn-outline" onclick="fillDemo('faculty@example.com', 'faculty123')" style="font-size: 0.8rem;">
                    <i class="fa-solid fa-chalkboard-user"></i> Faculty Demo
                </button>
            </div>
        </div>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.88rem; color: var(--text-muted);">
            Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" class="fw-semibold">Register as Student</a>
        </div>
        <div style="margin-top: 0.75rem; text-align: center;">
            <a href="${pageContext.request.contextPath}/" style="font-size: 0.82rem; color: var(--gray-500);">
                <i class="fa-solid fa-arrow-left"></i> Back to Homepage
            </a>
        </div>
    </div>

    <script>
        function fillDemo(email, pass) {
            document.getElementById('email').value = email;
            document.getElementById('password').value = pass;
        }
    </script>
</body>
</html>
