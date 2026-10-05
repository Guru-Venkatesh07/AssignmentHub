<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Student Dashboard &bull; AssignmentHub" />
<c:set var="pageHeading" value="Student Dashboard" />
<c:set var="activeMenu" value="dashboard" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <!-- Welcome Banner -->
            <div class="card" style="background: linear-gradient(135deg, #4f46e5 0%, #3b82f6 100%); color: #ffffff; border: none; padding: 2rem;">
                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <span class="badge" style="background: rgba(255,255,255,0.2); color: #ffffff; margin-bottom: 0.5rem;">
                            Academic Year ${sessionScope.year} &bull; ${sessionScope.registerNumber}
                        </span>
                        <h1 style="color: #ffffff; font-size: 1.85rem; margin-bottom: 0.25rem;">
                            Welcome back, ${sessionScope.userName}!
                        </h1>
                        <p style="color: #e0e7ff; font-size: 0.95rem;">
                            ${sessionScope.department}
                        </p>
                    </div>
                    <div>
                        <button id="refreshStatsBtn" class="btn btn-secondary" onclick="refreshStudentDashboard()" style="background: rgba(255,255,255,0.2); color: #ffffff; border: 1px solid rgba(255,255,255,0.3);">
                            <i class="fa-solid fa-arrows-rotate"></i> Refresh Metrics
                        </button>
                    </div>
                </div>
            </div>

            <!-- Dynamic Statistics Metrics Cards -->
            <div class="grid-stats">
                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Total Assignments</div>
                        <div class="value" id="statTotalAssignments">${stats.totalAssignments}</div>
                    </div>
                    <div class="stat-icon primary">
                        <i class="fa-solid fa-book-open"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Submitted</div>
                        <div class="value text-success" id="statSubmitted">${stats.submitted}</div>
                    </div>
                    <div class="stat-icon success">
                        <i class="fa-solid fa-circle-check"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Pending Action</div>
                        <div class="value text-warning" id="statPending">${stats.pending}</div>
                    </div>
                    <div class="stat-icon warning">
                        <i class="fa-solid fa-hourglass-half"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Evaluated &amp; Graded</div>
                        <div class="value" style="color: #0284c7;" id="statEvaluated">${stats.evaluated}</div>
                    </div>
                    <div class="stat-icon accent">
                        <i class="fa-solid fa-award"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Average Score</div>
                        <div class="value text-primary" id="statAverageMarks">${stats.averageMarks}%</div>
                    </div>
                    <div class="stat-icon primary">
                        <i class="fa-solid fa-chart-line"></i>
                    </div>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 3fr 2fr; gap: 1.5rem;">
                <!-- Upcoming Deadlines -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-regular fa-clock text-warning"></i> Upcoming Assignment Deadlines</h3>
                        <a href="${pageContext.request.contextPath}/student/assignments" class="btn btn-sm btn-outline">View All</a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty upcomingAssignments}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Assignment &amp; Subject</th>
                                            <th>Faculty</th>
                                            <th>Deadline</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="a" items="${upcomingAssignments}">
                                            <tr>
                                                <td>
                                                    <strong><a href="${pageContext.request.contextPath}/student/assignments/view?id=${a.id}">${a.title}</a></strong>
                                                    <div class="text-muted" style="font-size: 0.78rem;">${a.subjectCode} - ${a.subjectName}</div>
                                                </td>
                                                <td>${a.facultyName}</td>
                                                <td>
                                                    <span class="badge ${a.overdue ? 'badge-late' : 'badge-pending'}">${a.deadlineStatusText}</span>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/student/submit?assignmentId=${a.id}" class="btn btn-sm btn-primary">
                                                        Submit
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="icon"><i class="fa-solid fa-circle-check"></i></div>
                                <h4>All Caught Up!</h4>
                                <p>You have no pending assignments with approaching deadlines.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Recent Submissions / Results -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-award text-primary"></i> Recent Evaluations</h3>
                        <a href="${pageContext.request.contextPath}/student/results" class="btn btn-sm btn-outline">View All</a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty recentSubmissions}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Assignment</th>
                                            <th>Score</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="sub" items="${recentSubmissions}" begin="0" end="4">
                                            <tr>
                                                <td>
                                                    <div style="font-weight: 600;">${sub.assignmentTitle}</div>
                                                    <div class="text-muted" style="font-size: 0.75rem;">${sub.subjectName}</div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty sub.marks}">
                                                            <strong class="text-success">${sub.marks} / ${sub.maxMarks}</strong>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <span class="badge badge-${sub.status.name().toLowerCase()}">${sub.status}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="icon"><i class="fa-regular fa-file-lines"></i></div>
                                <h4>No Submissions Yet</h4>
                                <p>Submit your assignments to see evaluations and marks here.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
