<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside id="appSidebar" class="app-sidebar">
    <div class="sidebar-header">
        <a href="${pageContext.request.contextPath}/" class="sidebar-brand">
            <div class="sidebar-brand-icon">
                <i class="fa-solid fa-graduation-cap"></i>
            </div>
            <span>AssignmentHub</span>
        </a>
    </div>

    <div class="sidebar-nav">
        <c:choose>
            <%-- FACULTY NAVIGATION --%>
            <c:when test="${sessionScope.userRole == 'FACULTY'}">
                <div class="nav-category">Faculty Portal</div>
                <a href="${pageContext.request.contextPath}/faculty/dashboard" class="nav-link ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <i class="fa-solid fa-chart-pie"></i>
                    <span>Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/faculty/assignments" class="nav-link ${activeMenu == 'assignments' ? 'active' : ''}">
                    <i class="fa-solid fa-book-open"></i>
                    <span>My Assignments</span>
                </a>
                <a href="${pageContext.request.contextPath}/faculty/assignments/create" class="nav-link ${activeMenu == 'create-assignment' ? 'active' : ''}">
                    <i class="fa-solid fa-circle-plus"></i>
                    <span>Create Assignment</span>
                </a>
                <a href="${pageContext.request.contextPath}/faculty/submissions" class="nav-link ${activeMenu == 'submissions' ? 'active' : ''}">
                    <i class="fa-solid fa-file-circle-check"></i>
                    <span>Submissions</span>
                </a>

                <div class="nav-category">Course Resources</div>
                <a href="${pageContext.request.contextPath}/notifications" class="nav-link ${activeMenu == 'notifications' ? 'active' : ''}">
                    <i class="fa-solid fa-bell"></i>
                    <span>Notifications</span>
                </a>
                <a href="${pageContext.request.contextPath}/xml-viewer" class="nav-link ${activeMenu == 'xml' ? 'active' : ''}">
                    <i class="fa-solid fa-code"></i>
                    <span>Course Data XML</span>
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link ${activeMenu == 'profile' ? 'active' : ''}">
                    <i class="fa-solid fa-user-gear"></i>
                    <span>Profile &amp; Security</span>
                </a>
            </c:when>

            <%-- STUDENT NAVIGATION --%>
            <c:otherwise>
                <div class="nav-category">Student Portal</div>
                <a href="${pageContext.request.contextPath}/student/dashboard" class="nav-link ${activeMenu == 'dashboard' ? 'active' : ''}">
                    <i class="fa-solid fa-chart-pie"></i>
                    <span>Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/assignments" class="nav-link ${activeMenu == 'assignments' ? 'active' : ''}">
                    <i class="fa-solid fa-book"></i>
                    <span>Assignments</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/submissions" class="nav-link ${activeMenu == 'submissions' ? 'active' : ''}">
                    <i class="fa-solid fa-cloud-arrow-up"></i>
                    <span>My Submissions</span>
                </a>
                <a href="${pageContext.request.contextPath}/student/results" class="nav-link ${activeMenu == 'results' ? 'active' : ''}">
                    <i class="fa-solid fa-award"></i>
                    <span>Results &amp; Marks</span>
                </a>

                <div class="nav-category">Course Resources</div>
                <a href="${pageContext.request.contextPath}/notifications" class="nav-link ${activeMenu == 'notifications' ? 'active' : ''}">
                    <i class="fa-solid fa-bell"></i>
                    <span>Notifications</span>
                </a>
                <a href="${pageContext.request.contextPath}/xml-viewer" class="nav-link ${activeMenu == 'xml' ? 'active' : ''}">
                    <i class="fa-solid fa-code"></i>
                    <span>Course Data XML</span>
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-link ${activeMenu == 'profile' ? 'active' : ''}">
                    <i class="fa-solid fa-user-gear"></i>
                    <span>Profile &amp; Security</span>
                </a>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="sidebar-footer">
        <div class="user-mini-card">
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty sessionScope.userName}">
                        ${sessionScope.userName.substring(0, 1).toUpperCase()}
                    </c:when>
                    <c:otherwise>U</c:otherwise>
                </c:choose>
            </div>
            <div class="user-meta">
                <div class="name">${sessionScope.userName}</div>
                <span class="role-pill">${sessionScope.userRole}</span>
            </div>
        </div>
    </div>
</aside>
