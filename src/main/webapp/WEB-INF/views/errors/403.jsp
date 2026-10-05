<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 Forbidden &bull; Access Denied</title>
    <!-- Fonts & Icons -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body class="auth-wrapper" data-context-path="${pageContext.request.contextPath}">
    <div class="auth-card" style="text-align: center; max-width: 460px;">
        <div style="font-size: 3.5rem; color: var(--danger); margin-bottom: 1rem;">
            <i class="fa-solid fa-shield-halved"></i>
        </div>
        <h1 style="font-size: 1.75rem; margin-bottom: 0.5rem;">403 - Access Forbidden</h1>
        <p class="text-muted" style="margin-bottom: 1.5rem;">
            ${not empty errorMessage ? errorMessage : "You do not have permission to access this resource with your current role."}
        </p>
        <div style="display: flex; gap: 0.75rem; justify-content: center;">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                <i class="fa-solid fa-house"></i> Return Home
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary">
                <i class="fa-solid fa-arrow-right-to-bracket"></i> Switch Account
            </a>
        </div>
    </div>
</body>
</html>
