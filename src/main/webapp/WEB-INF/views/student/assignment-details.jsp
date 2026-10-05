<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${assignment.title} &bull; Assignment Details" />
<c:set var="pageHeading" value="Assignment Overview" />
<c:set var="activeMenu" value="assignments" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div style="margin-bottom: 1rem;">
                <a href="${pageContext.request.contextPath}/student/assignments" class="btn btn-sm btn-outline">
                    <i class="fa-solid fa-arrow-left"></i> Back to Assignments List
                </a>
            </div>

            <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1.5rem;">
                <!-- Main Assignment Details -->
                <div class="card">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
                        <div>
                            <span class="badge badge-active" style="margin-bottom: 0.5rem;">${assignment.subjectCode} &bull; ${assignment.subjectName}</span>
                            <h1 style="font-size: 1.6rem; color: var(--gray-900);">${assignment.title}</h1>
                            <div class="text-muted" style="font-size: 0.88rem; margin-top: 0.25rem;">
                                Faculty: <strong>${assignment.facultyName}</strong> &bull; Max Score: <strong>${assignment.maxMarks} Points</strong>
                            </div>
                        </div>
                        <span class="badge ${assignment.overdue ? 'badge-late' : 'badge-pending'}" style="font-size: 0.85rem; padding: 0.4rem 0.8rem;">
                            ${assignment.deadlineStatusText}
                        </span>
                    </div>

                    <div style="margin-bottom: 1.5rem;">
                        <h4 style="font-size: 1rem; color: var(--gray-800); margin-bottom: 0.5rem;">Description</h4>
                        <p style="color: var(--gray-700); line-height: 1.7; white-space: pre-line;">${assignment.description}</p>
                    </div>

                    <c:if test="${not empty assignment.instructions}">
                        <div style="margin-bottom: 1.5rem; background: var(--gray-50); padding: 1.25rem; border-radius: var(--radius-md); border-left: 3px solid var(--primary);">
                            <h4 style="font-size: 0.95rem; color: var(--gray-800); margin-bottom: 0.4rem;">
                                <i class="fa-solid fa-circle-info text-primary"></i> Instructions
                            </h4>
                            <p style="color: var(--gray-700); font-size: 0.9rem; line-height: 1.6; white-space: pre-line;">${assignment.instructions}</p>
                        </div>
                    </c:if>

                    <c:if test="${not empty assignment.attachmentPath}">
                        <div style="padding: 1rem; background: var(--primary-light); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: space-between;">
                            <div style="display: flex; align-items: center; gap: 0.75rem;">
                                <i class="fa-solid fa-paperclip text-primary" style="font-size: 1.25rem;"></i>
                                <div>
                                    <div style="font-weight: 600; font-size: 0.9rem;">Course Resource Attachment</div>
                                    <div class="text-muted" style="font-size: 0.78rem;">Provided by faculty</div>
                                </div>
                            </div>
                            <a href="${pageContext.request.contextPath}/download/attachment?assignmentId=${assignment.id}" class="btn btn-sm btn-primary">
                                <i class="fa-solid fa-download"></i> Download Resource
                            </a>
                        </div>
                    </c:if>
                </div>

                <!-- Submission & Evaluation Status Panel -->
                <div>
                    <!-- Status Card -->
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title"><i class="fa-solid fa-cloud-arrow-up text-primary"></i> My Submission</h3>
                        </div>

                        <c:choose>
                            <c:when test="${not empty submission}">
                                <div style="margin-bottom: 1.25rem;">
                                    <div class="text-muted" style="font-size: 0.8rem; text-transform: uppercase; font-weight: 700;">Current Status</div>
                                    <div style="margin-top: 0.35rem;">
                                        <c:choose>
                                            <c:when test="${submission.status == 'EVALUATED'}">
                                                <span class="badge badge-evaluated" style="font-size: 0.85rem; padding: 0.35rem 0.75rem;"><i class="fa-solid fa-check-double"></i> Evaluated</span>
                                            </c:when>
                                            <c:when test="${submission.isLate()}">
                                                <span class="badge badge-late" style="font-size: 0.85rem; padding: 0.35rem 0.75rem;"><i class="fa-solid fa-clock"></i> Submitted (Late)</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-submitted" style="font-size: 0.85rem; padding: 0.35rem 0.75rem;"><i class="fa-solid fa-check"></i> Submitted on Time</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div style="margin-bottom: 1rem; font-size: 0.88rem;">
                                    <div class="text-muted">Uploaded Deliverable:</div>
                                    <div class="fw-semibold" style="word-break: break-all; margin-top: 2px;">
                                        <i class="fa-regular fa-file"></i> ${submission.fileName}
                                    </div>
                                </div>

                                <div style="margin-bottom: 1.25rem; font-size: 0.85rem;">
                                    <div class="text-muted">Submission Date:</div>
                                    <div class="fw-semibold">${submission.submittedAt}</div>
                                </div>

                                <div style="display: flex; flex-direction: column; gap: 0.5rem;">
                                    <a href="${pageContext.request.contextPath}/download/submission?submissionId=${submission.id}" class="btn btn-secondary" style="width: 100%;">
                                        <i class="fa-solid fa-download"></i> Download Submitted File
                                    </a>

                                    <c:if test="${canResubmit}">
                                        <a href="${pageContext.request.contextPath}/student/submit?assignmentId=${assignment.id}" class="btn btn-outline" style="width: 100%;">
                                            <i class="fa-solid fa-arrows-rotate"></i> Replace / Resubmit File
                                        </a>
                                    </c:if>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state" style="padding: 1.5rem 0;">
                                    <div class="icon"><i class="fa-regular fa-file-circle-question"></i></div>
                                    <h4>Not Submitted</h4>
                                    <p style="font-size: 0.85rem;">You have not submitted a response for this assignment yet.</p>
                                </div>

                                <a href="${pageContext.request.contextPath}/student/submit?assignmentId=${assignment.id}" class="btn btn-primary" style="width: 100%;">
                                    <i class="fa-solid fa-upload"></i> Submit Assignment
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Evaluation & Feedback Panel if Evaluated -->
                    <c:if test="${not empty submission and submission.status == 'EVALUATED'}">
                        <div class="card" style="border-top: 4px solid var(--success);">
                            <div class="card-header">
                                <h3 class="card-title text-success"><i class="fa-solid fa-award"></i> Evaluation Results</h3>
                            </div>
                            <div style="text-align: center; margin-bottom: 1.25rem;">
                                <div style="font-size: 2.25rem; font-weight: 800; color: var(--success);">
                                    ${submission.marks} <span style="font-size: 1.2rem; color: var(--gray-500); font-weight: 500;">/ ${assignment.maxMarks}</span>
                                </div>
                                <div class="text-muted" style="font-size: 0.8rem;">Evaluated by ${submission.facultyName}</div>
                            </div>

                            <c:if test="${not empty submission.feedback}">
                                <div style="background: var(--gray-50); padding: 1rem; border-radius: var(--radius-md); font-size: 0.88rem;">
                                    <strong style="color: var(--gray-800); display: block; margin-bottom: 0.25rem;">Faculty Feedback:</strong>
                                    <p style="color: var(--gray-700); line-height: 1.5; white-space: pre-line;">${submission.feedback}</p>
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </div>

        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
