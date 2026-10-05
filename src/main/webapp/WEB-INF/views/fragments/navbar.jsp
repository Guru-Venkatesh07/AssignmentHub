<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="app-navbar">
    <div class="navbar-left">
        <button id="sidebarToggle" class="mobile-toggle" aria-label="Toggle Sidebar">
            <i class="fa-solid fa-bars"></i>
        </button>
        <div class="page-title-crumb">
            <h2>${pageHeading != null ? pageHeading : "Dashboard"}</h2>
        </div>
    </div>
    <div class="navbar-right">
        <!-- Course Data XML Link -->
        <a href="${pageContext.request.contextPath}/xml-viewer" class="btn btn-sm btn-outline" title="Course Data XML">
            <i class="fa-solid fa-code"></i> Course Data XML
        </a>

        <!-- In-App Notification Bell -->
        <a href="${pageContext.request.contextPath}/notifications" class="notif-bell-btn" title="Notifications">
            <i class="fa-regular fa-bell"></i>
            <span id="notifCountBadge" class="notif-badge-pill" style="display: none;">0</span>
        </a>

        <!-- Logout Quick Button -->
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-secondary" title="Sign Out">
            <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
        </a>
    </div>
</nav>
