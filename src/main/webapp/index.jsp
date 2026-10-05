<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assignment &amp; Submission Management System</title>
    <!-- Fonts & Icons -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

    <!-- Top Navigation -->
    <header style="background: #0f172a; padding: 1.25rem 2rem; border-bottom: 1px solid #1e293b; display: flex; justify-content: space-between; align-items: center;">
        <div style="display: flex; align-items: center; gap: 0.75rem;">
            <div class="sidebar-brand-icon">
                <i class="fa-solid fa-graduation-cap"></i>
            </div>
            <span style="color: #ffffff; font-weight: 800; font-size: 1.2rem;">AssignmentHub</span>
        </div>
        <div style="display: flex; gap: 1rem; align-items: center;">
            <a href="${pageContext.request.contextPath}/xml-viewer" class="btn btn-sm btn-outline" style="color: #cbd5e1; border-color: #334155;">
                <i class="fa-solid fa-code"></i> Course Data XML
            </a>
            <c:choose>
                <c:when test="${not empty sessionScope.userId}">
                    <c:choose>
                        <c:when test="${sessionScope.userRole == 'FACULTY'}">
                            <a href="${pageContext.request.contextPath}/faculty/dashboard" class="btn btn-sm btn-primary">
                                <i class="fa-solid fa-chart-pie"></i> Go to Dashboard
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-sm btn-primary">
                                <i class="fa-solid fa-chart-pie"></i> Go to Dashboard
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-sm btn-primary">
                        <i class="fa-solid fa-right-to-bracket"></i> Sign In
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </header>

    <!-- Hero Section -->
    <section class="landing-hero">
        <span class="badge badge-active" style="background: rgba(79, 70, 229, 0.2); color: #a5b4fc; margin-bottom: 1.5rem; padding: 0.4rem 1rem; font-size: 0.85rem;">
            Academic Management &amp; Evaluation Platform
        </span>
        <h1>Assignment &amp; Submission Management System</h1>
        <p>Manage coursework, submit on time, track academic evaluations, and collaborate with faculty seamlessly.</p>
        <div class="hero-cta">
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-lg btn-primary">
                <i class="fa-solid fa-user-graduate"></i> Student Portal
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-lg btn-secondary" style="background: #1e293b; color: #ffffff; border-color: #334155;">
                <i class="fa-solid fa-chalkboard-user"></i> Faculty Portal
            </a>
            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-lg btn-outline" style="color: #ffffff; border-color: #475569;">
                <i class="fa-solid fa-user-plus"></i> Student Registration
            </a>
        </div>
    </section>

    <!-- Core Features Grid -->
    <main class="features-grid">
        <div class="feature-box">
            <div class="icon"><i class="fa-solid fa-file-pen"></i></div>
            <h3>Assignment Management</h3>
            <p class="text-muted">Faculty create detailed coursework with instructions, deadlines, maximum marks, and course resources.</p>
        </div>

        <div class="feature-box">
            <div class="icon" style="background: #ecfeff; color: #0891b2;"><i class="fa-solid fa-cloud-arrow-up"></i></div>
            <h3>Online Submissions</h3>
            <p class="text-muted">Students securely upload PDF, DOCX, PPTX, or ZIP deliverables with automated deadline and late delivery tracking.</p>
        </div>

        <div class="feature-box">
            <div class="icon" style="background: #ecfdf5; color: #059669;"><i class="fa-solid fa-square-poll-vertical"></i></div>
            <h3>Evaluation &amp; Feedback</h3>
            <p class="text-muted">Faculty review student submissions, award scores, and provide constructive academic feedback.</p>
        </div>

        <div class="feature-box">
            <div class="icon" style="background: #fffbeb; color: #d97706;"><i class="fa-solid fa-bell"></i></div>
            <h3>Instant Notifications</h3>
            <p class="text-muted">Real-time alerts notify students upon assignment creation or grading, and notify faculty upon student submissions.</p>
        </div>

        <div class="feature-box">
            <div class="icon" style="background: #fdf2f8; color: #db2777;"><i class="fa-solid fa-bolt"></i></div>
            <h3>Live Verification</h3>
            <p class="text-muted">Instant live availability checks during registration and real-time dashboard analytics.</p>
        </div>

        <div class="feature-box">
            <div class="icon" style="background: #f5f3ff; color: #7c3aed;"><i class="fa-solid fa-code"></i></div>
            <h3>Course Data Feeds</h3>
            <p class="text-muted">Standard data feeds and live interactive viewer for course assignments and departmental curriculum.</p>
        </div>
    </main>

    <!-- Platform Highlights Banner -->
    <section style="background: #ffffff; border-top: 1px solid var(--border-color); border-bottom: 1px solid var(--border-color); padding: 3.5rem 2rem;">
        <div style="max-width: 900px; margin: 0 auto; text-align: center;">
            <h2 style="margin-bottom: 0.75rem;">Streamlined Academic Workflow</h2>
            <p class="text-muted" style="margin-bottom: 2rem;">An all-in-one platform connecting students and faculty through every stage of the assignment process.</p>
            <div style="display: flex; flex-wrap: wrap; gap: 1rem; justify-content: center;">
                <span class="badge badge-active" style="padding: 0.6rem 1.1rem; font-size: 0.85rem;">Role-Based Security</span>
                <span class="badge badge-submitted" style="padding: 0.6rem 1.1rem; font-size: 0.85rem;">Digital Deliverables</span>
                <span class="badge badge-evaluated" style="padding: 0.6rem 1.1rem; font-size: 0.85rem;">Evaluation Rubrics</span>
                <span class="badge badge-pending" style="padding: 0.6rem 1.1rem; font-size: 0.85rem;">Deadline Management</span>
                <span class="badge badge-active" style="padding: 0.6rem 1.1rem; font-size: 0.85rem;">Performance Analytics</span>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer style="padding: 2rem; background: #0f172a; color: #94a3b8; text-align: center; font-size: 0.85rem;">
        <p style="color: #ffffff; font-weight: 600; margin-bottom: 0.25rem;">Assignment &amp; Submission Management System</p>
        <p>&copy; Academic Assignment and Evaluation Platform. All rights reserved.</p>
    </footer>

</body>
</html>
