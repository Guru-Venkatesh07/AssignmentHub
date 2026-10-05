<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Submissions &bull; Student Portal" />
<c:set var="pageHeading" value="My Submission History" />
<c:set var="activeMenu" value="submissions" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-cloud-arrow-up text-primary"></i> Submitted Assignments (${submissions.size()})</h3>
                </div>

                <c:choose>
                    <c:when test="${not empty submissions}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Assignment Title</th>
                                        <th>Subject</th>
                                        <th>Submitted Deliverable</th>
                                        <th>Submitted At</th>
                                        <th>Late Status</th>
                                        <th>Status</th>
                                        <th>Score</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="sub" items="${submissions}">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/student/assignments/view?id=${sub.assignmentId}" class="fw-bold" style="color: var(--gray-900);">
                                                    ${sub.assignmentTitle}
                                                </a>
                                            </td>
                                            <td>
                                                <span class="badge badge-active">${sub.subjectCode}</span>
                                                <div style="font-size: 0.8rem; margin-top: 2px;">${sub.subjectName}</div>
                                            </td>
                                            <td>
                                                <div style="font-size: 0.85rem; word-break: break-all;">
                                                    <i class="fa-regular fa-file"></i> ${sub.fileName}
                                                </div>
                                            </td>
                                            <td>
                                                <div style="font-size: 0.85rem;">${sub.submittedAt}</div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${sub.isLate()}">
                                                        <span class="badge badge-late"><i class="fa-solid fa-clock"></i> Late</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-evaluated"><i class="fa-solid fa-check"></i> On-Time</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge badge-${sub.status.name().toLowerCase()}">${sub.status}</span>
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
                                                <a href="${pageContext.request.contextPath}/download/submission?submissionId=${sub.id}" class="btn btn-sm btn-outline" title="Download submitted file">
                                                    <i class="fa-solid fa-download"></i> Download
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
                            <div class="icon"><i class="fa-regular fa-file"></i></div>
                            <h4>No Submissions Yet</h4>
                            <p>You have not uploaded any assignment deliverables yet.</p>
                            <a href="${pageContext.request.contextPath}/student/assignments" class="btn btn-primary" style="margin-top: 1rem;">
                                Browse Assignments
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
