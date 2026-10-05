<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Assignments &bull; Faculty Portal" />
<c:set var="pageHeading" value="Faculty Assignments" />
<c:set var="activeMenu" value="assignments" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-book text-primary"></i> Created Assignments (${assignments.size()})</h3>
                    <a href="${pageContext.request.contextPath}/faculty/assignments/create" class="btn btn-primary">
                        <i class="fa-solid fa-circle-plus"></i> Create New Assignment
                    </a>
                </div>

                <c:choose>
                    <c:when test="${not empty assignments}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Title &amp; Description</th>
                                        <th>Subject</th>
                                        <th>Due Date &amp; Time</th>
                                        <th>Submissions</th>
                                        <th>Max Score</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="a" items="${assignments}">
                                        <tr>
                                            <td style="max-width: 280px;">
                                                <div class="fw-bold" style="color: var(--gray-900);">${a.title}</div>
                                                <div class="text-muted" style="font-size: 0.78rem; margin-top: 2px;">
                                                    <c:choose>
                                                        <c:when test="${a.description.length() > 70}">
                                                            ${a.description.substring(0, 70)}...
                                                        </c:when>
                                                        <c:otherwise>${a.description}</c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge badge-active">${a.subjectCode}</span>
                                                <div style="font-size: 0.8rem; margin-top: 2px;">${a.subjectName}</div>
                                            </td>
                                            <td>
                                                <div class="fw-semibold">${a.dueDate}</div>
                                                <span class="badge ${a.overdue ? 'badge-late' : 'badge-pending'}" style="margin-top: 4px;">
                                                    ${a.deadlineStatusText}
                                                </span>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${a.id}" class="fw-bold text-primary">
                                                    ${a.submissionCount} Submissions
                                                </a>
                                                <c:if test="${a.pendingEvaluationCount > 0}">
                                                    <div><span class="badge badge-pending" style="font-size: 0.7rem;">${a.pendingEvaluationCount} pending</span></div>
                                                </c:if>
                                            </td>
                                            <td>
                                                <strong>${a.maxMarks} pts</strong>
                                            </td>
                                            <td>
                                                <span class="badge badge-${a.status.name().toLowerCase()}">${a.status}</span>
                                            </td>
                                            <td>
                                                <div style="display: flex; gap: 0.4rem; align-items: center;">
                                                    <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${a.id}" class="btn btn-sm btn-primary" title="View Submissions">
                                                        <i class="fa-solid fa-users-viewfinder"></i> Submissions
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/faculty/assignments/edit?id=${a.id}" class="btn btn-sm btn-secondary" title="Edit Assignment">
                                                        <i class="fa-solid fa-pen-to-square"></i>
                                                    </a>
                                                    <form action="${pageContext.request.contextPath}/faculty/assignments/delete" method="post" onsubmit="return confirm('Are you sure you want to permanently delete this assignment? All student submissions will also be deleted.');" style="display: inline;">
                                                        <input type="hidden" name="id" value="${a.id}">
                                                        <button type="submit" class="btn btn-sm btn-danger" title="Delete Assignment">
                                                            <i class="fa-solid fa-trash"></i>
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="icon"><i class="fa-solid fa-folder-open"></i></div>
                            <h4>No Assignments Found</h4>
                            <p>You have not created any assignments yet.</p>
                            <a href="${pageContext.request.contextPath}/faculty/assignments/create" class="btn btn-primary" style="margin-top: 1rem;">
                                <i class="fa-solid fa-plus"></i> Create Your First Assignment
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
