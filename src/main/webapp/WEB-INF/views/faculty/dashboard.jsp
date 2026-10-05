<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Faculty Dashboard &bull; AssignmentHub" />
<c:set var="pageHeading" value="Faculty Dashboard" />
<c:set var="activeMenu" value="dashboard" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <!-- Welcome Banner -->
            <div class="card" style="background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); color: #ffffff; border: none; padding: 2rem;">
                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <span class="badge" style="background: rgba(79, 70, 229, 0.3); color: #a5b4fc; margin-bottom: 0.5rem;">
                            Faculty Member &bull; ${sessionScope.department}
                        </span>
                        <h1 style="color: #ffffff; font-size: 1.85rem; margin-bottom: 0.25rem;">
                            Welcome, ${sessionScope.userName}!
                        </h1>
                        <p style="color: #94a3b8; font-size: 0.95rem;">
                            Manage assignments, evaluate student submissions, and publish grades.
                        </p>
                    </div>
                    <div style="display: flex; gap: 0.75rem;">
                        <a href="${pageContext.request.contextPath}/faculty/assignments/create" class="btn btn-primary">
                            <i class="fa-solid fa-circle-plus"></i> Create Assignment
                        </a>
                        <button id="refreshStatsBtn" class="btn btn-secondary" onclick="refreshFacultyDashboard()" style="background: rgba(255,255,255,0.1); color: #ffffff; border: 1px solid rgba(255,255,255,0.2);">
                            <i class="fa-solid fa-arrows-rotate"></i> Refresh Metrics
                        </button>
                    </div>
                </div>
            </div>

            <!-- Dynamic Statistics Metrics Cards -->
            <div class="grid-stats">
                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Assignments Created</div>
                        <div class="value text-primary" id="statCreated">${stats.assignmentsCreated}</div>
                    </div>
                    <div class="stat-icon primary">
                        <i class="fa-solid fa-book-open"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Total Submissions</div>
                        <div class="value" style="color: #0284c7;" id="statTotalSubs">${stats.totalSubmissions}</div>
                    </div>
                    <div class="stat-icon accent">
                        <i class="fa-solid fa-file-arrow-up"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Pending Evaluation</div>
                        <div class="value text-warning" id="statPendingEval">${stats.pendingEvaluations}</div>
                    </div>
                    <div class="stat-icon warning">
                        <i class="fa-solid fa-hourglass-half"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Evaluated Submissions</div>
                        <div class="value text-success" id="statEvaluatedSubs">${stats.evaluatedSubmissions}</div>
                    </div>
                    <div class="stat-icon success">
                        <i class="fa-solid fa-circle-check"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-info">
                        <div class="label">Late Submissions</div>
                        <div class="value text-danger" id="statLateSubs">${stats.lateSubmissions}</div>
                    </div>
                    <div class="stat-icon danger">
                        <i class="fa-solid fa-clock"></i>
                    </div>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 3fr 2fr; gap: 1.5rem;">
                <!-- Recent Assignments Created -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-book text-primary"></i> My Active Assignments</h3>
                        <a href="${pageContext.request.contextPath}/faculty/assignments" class="btn btn-sm btn-outline">Manage All</a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty recentAssignments}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Assignment</th>
                                            <th>Subject</th>
                                            <th>Submissions</th>
                                            <th>Due Date</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="a" items="${recentAssignments}" begin="0" end="4">
                                            <tr>
                                                <td>
                                                    <div class="fw-bold">${a.title}</div>
                                                    <div class="text-muted" style="font-size: 0.75rem;">Max: ${a.maxMarks} pts</div>
                                                </td>
                                                <td><span class="badge badge-active">${a.subjectCode}</span></td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${a.id}" class="fw-bold text-primary">
                                                        ${a.submissionCount} Submissions
                                                    </a>
                                                    <c:if test="${a.pendingEvaluationCount > 0}">
                                                        <span class="badge badge-pending" style="margin-left: 4px;">${a.pendingEvaluationCount} pending</span>
                                                    </c:if>
                                                </td>
                                                <td>${a.dueDate}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${a.id}" class="btn btn-sm btn-outline">
                                                        Review
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
                                <div class="icon"><i class="fa-solid fa-folder-plus"></i></div>
                                <h4>No Assignments Created</h4>
                                <p>Click "Create Assignment" to post your first academic task.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Pending Evaluations Queue -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-pen-ruler text-warning"></i> Pending Evaluations Queue</h3>
                        <a href="${pageContext.request.contextPath}/faculty/submissions?status=PENDING" class="btn btn-sm btn-outline">View Queue</a>
                    </div>
                    <c:choose>
                        <c:when test="${not empty pendingEvaluations}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Student</th>
                                            <th>Assignment</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="sub" items="${pendingEvaluations}">
                                            <tr>
                                                <td>
                                                    <div class="fw-bold">${sub.studentName}</div>
                                                    <div class="text-muted" style="font-size: 0.75rem;">${sub.studentRegisterNumber}</div>
                                                </td>
                                                <td>
                                                    <div style="font-size: 0.85rem; font-weight: 500;">${sub.assignmentTitle}</div>
                                                    <c:if test="${sub.isLate()}">
                                                        <span class="badge badge-late" style="font-size: 0.65rem;">Late</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/faculty/evaluate?submissionId=${sub.id}" class="btn btn-sm btn-primary">
                                                        Evaluate
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
                                <h4>Evaluations Cleared!</h4>
                                <p>No pending student submissions waiting for evaluation.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
