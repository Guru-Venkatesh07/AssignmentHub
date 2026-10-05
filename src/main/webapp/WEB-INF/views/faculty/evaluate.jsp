<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Evaluate Submission &bull; Faculty Portal" />
<c:set var="pageHeading" value="Evaluate Student Submission" />
<c:set var="activeMenu" value="submissions" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container" style="max-width: 900px;">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div style="margin-bottom: 1rem;">
                <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${submission.assignmentId}" class="btn btn-sm btn-outline">
                    <i class="fa-solid fa-arrow-left"></i> Back to Submissions List
                </a>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
                <!-- Student & Submission Info -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-user-graduate text-primary"></i> Submission Details</h3>
                    </div>

                    <div style="margin-bottom: 1rem;">
                        <div class="text-muted" style="font-size: 0.8rem; text-transform: uppercase;">Assignment</div>
                        <div class="fw-bold" style="font-size: 1.1rem; color: var(--gray-900);">${submission.assignmentTitle}</div>
                        <div class="text-muted" style="font-size: 0.82rem;">${submission.subjectCode} - ${submission.subjectName}</div>
                    </div>

                    <div style="margin-bottom: 1rem;">
                        <div class="text-muted" style="font-size: 0.8rem; text-transform: uppercase;">Student</div>
                        <div class="fw-bold" style="color: var(--gray-900);">${submission.studentName}</div>
                        <div class="text-muted" style="font-size: 0.82rem;">Reg No: <strong class="font-mono">${submission.studentRegisterNumber}</strong></div>
                        <div class="text-muted" style="font-size: 0.82rem;">${submission.studentDepartment} &bull; Year ${submission.studentYear}</div>
                    </div>

                    <div style="margin-bottom: 1rem;">
                        <div class="text-muted" style="font-size: 0.8rem; text-transform: uppercase;">Submitted Deliverable</div>
                        <div style="margin-top: 0.35rem;">
                            <a href="${pageContext.request.contextPath}/download/submission?submissionId=${submission.id}" class="btn btn-sm btn-primary" style="word-break: break-all;">
                                <i class="fa-solid fa-file-arrow-down"></i> Download ${submission.fileName}
                            </a>
                        </div>
                    </div>

                    <div style="margin-bottom: 1rem;">
                        <div class="text-muted" style="font-size: 0.8rem; text-transform: uppercase;">Submission Date &amp; Time</div>
                        <div style="font-size: 0.9rem; font-weight: 600; margin-top: 2px;">${submission.submittedAt}</div>
                        <div style="margin-top: 4px;">
                            <c:choose>
                                <c:when test="${submission.isLate()}">
                                    <span class="badge badge-late"><i class="fa-solid fa-triangle-exclamation"></i> Late Submission</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-evaluated"><i class="fa-solid fa-check"></i> On-Time Submission</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <!-- Evaluation Form -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-award text-success"></i> Score &amp; Feedback</h3>
                    </div>

                    <form action="${pageContext.request.contextPath}/faculty/evaluate" method="post">
                        <input type="hidden" name="submissionId" value="${submission.id}">

                        <div class="form-group">
                            <label for="marks" class="form-label">
                                Award Marks (Max: ${submission.maxMarks}) <span class="text-danger">*</span>
                            </label>
                            <input type="number" id="marks" name="marks" class="form-control" 
                                   value="${not empty evaluation ? evaluation.marks : (not empty submission.marks ? submission.marks : '')}" 
                                   min="0" max="${submission.maxMarks}" required>
                            <div class="form-text">Marks must be between 0 and ${submission.maxMarks}.</div>
                        </div>

                        <div class="form-group">
                            <label for="feedback" class="form-label">Academic Feedback &amp; Remarks</label>
                            <textarea id="feedback" name="feedback" class="form-control" rows="5" 
                                      placeholder="Provide constructive feedback, areas of improvement, or commendations...">${not empty evaluation ? evaluation.feedback : submission.feedback}</textarea>
                        </div>

                        <div style="display: flex; gap: 1rem; justify-content: flex-end; margin-top: 1.5rem;">
                            <a href="${pageContext.request.contextPath}/faculty/submissions?assignmentId=${submission.assignmentId}" class="btn btn-secondary">
                                Cancel
                            </a>
                            <button type="submit" class="btn btn-success">
                                <i class="fa-solid fa-check-double"></i> Save &amp; Publish Evaluation
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
