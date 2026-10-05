<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Submit: ${assignment.title} &bull; Student Portal" />
<c:set var="pageHeading" value="Upload Assignment Submission" />
<c:set var="activeMenu" value="assignments" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container" style="max-width: 800px;">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div style="margin-bottom: 1rem;">
                <a href="${pageContext.request.contextPath}/student/assignments/view?id=${assignment.id}" class="btn btn-sm btn-outline">
                    <i class="fa-solid fa-arrow-left"></i> Back to Assignment Overview
                </a>
            </div>

            <div class="card">
                <div class="card-header">
                    <div>
                        <span class="badge badge-active">${assignment.subjectCode} - ${assignment.subjectName}</span>
                        <h2 style="font-size: 1.4rem; margin-top: 0.25rem;">${assignment.title}</h2>
                    </div>
                    <span class="badge ${assignment.overdue ? 'badge-late' : 'badge-pending'}">
                        ${assignment.deadlineStatusText}
                    </span>
                </div>

                <c:if test="${assignment.overdue}">
                    <div class="alert alert-warning">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                        <span><strong>Deadline Warning:</strong> The submission deadline for this assignment has passed. Your submission will be recorded as <strong>LATE</strong>.</span>
                    </div>
                </c:if>

                <c:if test="${not empty submission}">
                    <div class="alert alert-info">
                        <i class="fa-solid fa-info-circle"></i>
                        <span>You previously submitted <strong>${submission.fileName}</strong> on ${submission.submittedAt}. Uploading a new file will replace your previous submission.</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/student/submit" method="post" enctype="multipart/form-data" id="submissionForm">
                    <input type="hidden" name="assignmentId" value="${assignment.id}">

                    <div class="form-group">
                        <label class="form-label">Select File to Upload <span class="text-danger">*</span></label>
                        <div class="dropzone" onclick="document.getElementById('fileUploadInput').click()">
                            <div class="icon"><i class="fa-solid fa-cloud-arrow-up"></i></div>
                            <h4 style="font-size: 1.05rem;">Click to browse or drop your submission file here</h4>
                            <p class="text-muted" style="font-size: 0.85rem; margin-top: 0.25rem;">
                                Supported Formats: <strong>PDF, DOCX, PPTX, ZIP</strong> (Max Size: <strong>10 MB</strong>)
                            </p>
                            <input type="file" id="fileUploadInput" name="submissionFile" style="display: none;" accept=".pdf,.docx,.doc,.pptx,.ppt,.zip" required>
                        </div>
                        <div id="fileNameDisplay" style="margin-top: 0.75rem; font-weight: 600; color: var(--primary);"></div>
                        <div id="fileFeedback" class="form-text"></div>
                    </div>

                    <div style="display: flex; gap: 1rem; justify-content: flex-end; margin-top: 1.5rem;">
                        <a href="${pageContext.request.contextPath}/student/assignments/view?id=${assignment.id}" class="btn btn-secondary">
                            Cancel
                        </a>
                        <button type="submit" class="btn btn-primary" id="submitBtn">
                            <i class="fa-solid fa-upload"></i> ${not empty submission ? 'Resubmit Assignment' : 'Confirm &amp; Upload Submission'}
                        </button>
                    </div>
                </form>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
