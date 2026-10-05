<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Submissions &bull; Faculty Portal" />
<c:set var="pageHeading" value="Student Submissions &amp; Evaluations" />
<c:set var="activeMenu" value="submissions" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <!-- Assignment Selector & Filter Toolbar -->
            <div class="card" style="padding: 1.25rem;">
                <form action="${pageContext.request.contextPath}/faculty/submissions" method="get" style="display: flex; gap: 1rem; flex-wrap: wrap; align-items: center;">
                    <div style="flex: 2; min-width: 250px;">
                        <label class="form-label" style="font-size: 0.8rem;">Select Assignment:</label>
                        <select name="assignmentId" class="form-select" onchange="this.form.submit()">
                            <c:forEach var="a" items="${assignments}">
                                <option value="${a.id}" ${selectedAssignment.id == a.id ? 'selected' : ''}>
                                    ${a.title} (${a.subjectCode})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div style="flex: 1; min-width: 160px;">
                        <label class="form-label" style="font-size: 0.8rem;">Filter by Status:</label>
                        <select name="status" class="form-select" onchange="this.form.submit()">
                            <option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>All Submissions</option>
                            <option value="PENDING" ${selectedStatus == 'PENDING' ? 'selected' : ''}>Pending Evaluation</option>
                            <option value="EVALUATED" ${selectedStatus == 'EVALUATED' ? 'selected' : ''}>Evaluated</option>
                            <option value="LATE" ${selectedStatus == 'LATE' ? 'selected' : ''}>Late Submissions</option>
                        </select>
                    </div>
                </form>
            </div>

            <!-- Submissions Table -->
            <div class="card">
                <div class="card-header">
                    <div>
                        <h3 class="card-title"><i class="fa-solid fa-users-viewfinder text-primary"></i> Submissions for: ${selectedAssignment.title}</h3>
                        <div class="text-muted" style="font-size: 0.82rem; margin-top: 2px;">
                            Due: ${selectedAssignment.dueDate} &bull; Max Marks: ${selectedAssignment.maxMarks} &bull; Total: ${submissions.size()}
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty submissions}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Student</th>
                                        <th>Register No.</th>
                                        <th>Department &amp; Year</th>
                                        <th>Submitted Deliverable</th>
                                        <th>Submission Time</th>
                                        <th>Late</th>
                                        <th>Status</th>
                                        <th>Score</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${submissions}">
                                        <tr>
                                            <td>
                                                <div class="fw-bold" style="color: var(--gray-900);">${s.studentName}</div>
                                                <div class="text-muted" style="font-size: 0.78rem;">${s.studentEmail}</div>
                                            </td>
                                            <td>
                                                <span class="badge badge-active font-mono">${s.studentRegisterNumber}</span>
                                            </td>
                                            <td>
                                                <div style="font-size: 0.85rem;">${s.studentDepartment}</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">Year ${s.studentYear}</div>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/download/submission?submissionId=${s.id}" class="btn btn-sm btn-outline" style="font-size: 0.78rem;" title="Download file">
                                                    <i class="fa-solid fa-download"></i> ${s.fileName}
                                                </a>
                                            </td>
                                            <td>
                                                <div style="font-size: 0.82rem;">${s.submittedAt}</div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${s.isLate()}">
                                                        <span class="badge badge-late">Yes (Late)</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-evaluated">On-Time</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge badge-${s.status.name().toLowerCase()}">${s.status}</span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty s.marks}">
                                                        <strong class="text-success">${s.marks} / ${selectedAssignment.maxMarks}</strong>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/faculty/evaluate?submissionId=${s.id}" class="btn btn-sm ${s.status == 'EVALUATED' ? 'btn-secondary' : 'btn-primary'}">
                                                    <i class="fa-solid fa-pen-to-square"></i> ${s.status == 'EVALUATED' ? 'Re-evaluate' : 'Evaluate'}
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
                            <div class="icon"><i class="fa-regular fa-file-excel"></i></div>
                            <h4>No Submissions Found</h4>
                            <p>No student submissions have been received for the selected assignment under this filter.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
