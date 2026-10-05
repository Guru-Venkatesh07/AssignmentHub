<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Assignments &bull; Student Portal" />
<c:set var="pageHeading" value="Course Assignments" />
<c:set var="activeMenu" value="assignments" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <!-- Filter & Search Toolbar -->
            <div class="card" style="padding: 1.25rem;">
                <form action="${pageContext.request.contextPath}/student/assignments" method="get" style="display: flex; gap: 1rem; flex-wrap: wrap; align-items: center;">
                    <div style="flex: 2; min-width: 240px;">
                        <input type="text" name="search" class="form-control" placeholder="Search by assignment title, subject, or faculty..." value="${search}">
                    </div>
                    <div style="flex: 1; min-width: 180px;">
                        <select name="subjectId" class="form-select">
                            <option value="0">All Subjects</option>
                            <c:forEach var="s" items="${subjects}">
                                <option value="${s.id}" ${selectedSubjectId == s.id ? 'selected' : ''}>
                                    ${s.subjectCode} - ${s.subjectName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div style="flex: 1; min-width: 150px;">
                        <select name="status" class="form-select">
                            <option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>All Statuses</option>
                            <option value="PENDING" ${selectedStatus == 'PENDING' ? 'selected' : ''}>Pending Submission</option>
                            <option value="SUBMITTED" ${selectedStatus == 'SUBMITTED' ? 'selected' : ''}>Submitted</option>
                            <option value="EVALUATED" ${selectedStatus == 'EVALUATED' ? 'selected' : ''}>Evaluated</option>
                            <option value="LATE" ${selectedStatus == 'LATE' ? 'selected' : ''}>Late Submissions</option>
                        </select>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-filter"></i> Filter
                        </button>
                        <a href="${pageContext.request.contextPath}/student/assignments" class="btn btn-secondary">
                            Reset
                        </a>
                    </div>
                </form>
            </div>

            <!-- Assignments Table / Cards -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-list-check"></i> Available Assignments (${assignments.size()})</h3>
                </div>

                <c:choose>
                    <c:when test="${not empty assignments}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Assignment Title</th>
                                        <th>Subject &amp; Faculty</th>
                                        <th>Due Date &amp; Time</th>
                                        <th>Max Marks</th>
                                        <th>My Status</th>
                                        <th>Score</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="a" items="${assignments}">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/student/assignments/view?id=${a.id}" class="fw-bold" style="color: var(--gray-900);">
                                                    ${a.title}
                                                </a>
                                                <div class="text-muted" style="font-size: 0.8rem; margin-top: 2px;">
                                                    <c:choose>
                                                        <c:when test="${a.description.length() > 60}">
                                                            ${a.description.substring(0, 60)}...
                                                        </c:when>
                                                        <c:otherwise>${a.description}</c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge badge-active">${a.subjectCode}</span>
                                                <div style="font-size: 0.85rem; font-weight: 600; margin-top: 2px;">${a.subjectName}</div>
                                                <div class="text-muted" style="font-size: 0.78rem;">Faculty: ${a.facultyName}</div>
                                            </td>
                                            <td>
                                                <div class="fw-semibold">${a.dueDate}</div>
                                                <span class="badge ${a.overdue ? 'badge-late' : 'badge-pending'}" style="margin-top: 4px;">
                                                    ${a.deadlineStatusText}
                                                </span>
                                            </td>
                                            <td>
                                                <strong>${a.maxMarks} pts</strong>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${a.studentSubmissionStatus == 'EVALUATED'}">
                                                        <span class="badge badge-evaluated"><i class="fa-solid fa-check-double"></i> Evaluated</span>
                                                    </c:when>
                                                    <c:when test="${a.studentSubmissionStatus == 'SUBMITTED'}">
                                                        <span class="badge badge-submitted"><i class="fa-solid fa-check"></i> Submitted</span>
                                                    </c:when>
                                                    <c:when test="${a.studentSubmissionStatus == 'LATE'}">
                                                        <span class="badge badge-late"><i class="fa-solid fa-clock"></i> Late</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-pending"><i class="fa-solid fa-hourglass"></i> Pending</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty a.studentMarks}">
                                                        <strong class="text-success">${a.studentMarks} / ${a.maxMarks}</strong>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/student/assignments/view?id=${a.id}" class="btn btn-sm btn-outline">
                                                    View Details
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
                            <div class="icon"><i class="fa-solid fa-folder-open"></i></div>
                            <h4>No Assignments Found</h4>
                            <p>No assignments match your search and filter criteria.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
