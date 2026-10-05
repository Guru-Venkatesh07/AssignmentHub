<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create Assignment &bull; Faculty Portal" />
<c:set var="pageHeading" value="Create Academic Assignment" />
<c:set var="activeMenu" value="create-assignment" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container" style="max-width: 900px;">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div style="margin-bottom: 1rem;">
                <a href="${pageContext.request.contextPath}/faculty/assignments" class="btn btn-sm btn-outline">
                    <i class="fa-solid fa-arrow-left"></i> Back to Assignments List
                </a>
            </div>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-pen-to-square text-primary"></i> New Assignment Details</h3>
                </div>

                <form action="${pageContext.request.contextPath}/faculty/assignments/create" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="title" class="form-label">Assignment Title <span class="text-danger">*</span></label>
                        <input type="text" id="title" name="title" class="form-control" placeholder="e.g. Database Normalization &amp; BCNF" value="${title}" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label for="subjectId" class="form-label">Subject / Course <span class="text-danger">*</span></label>
                            <select id="subjectId" name="subjectId" class="form-select" required>
                                <option value="">-- Select Subject --</option>
                                <c:choose>
                                    <c:when test="${not empty assignedSubjects}">
                                        <c:forEach var="fs" items="${assignedSubjects}">
                                            <option value="${fs.subjectId}" ${selectedSubjectId == fs.subjectId ? 'selected' : ''}>
                                                ${fs.subjectCode} - ${fs.subjectName}
                                            </option>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="s" items="${allSubjects}">
                                            <option value="${s.id}" ${selectedSubjectId == s.id ? 'selected' : ''}>
                                                ${s.subjectCode} - ${s.subjectName}
                                            </option>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="maxMarks" class="form-label">Maximum Marks <span class="text-danger">*</span></label>
                            <input type="number" id="maxMarks" name="maxMarks" class="form-control" value="${not empty maxMarks ? maxMarks : 100}" min="1" max="1000" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Assignment Description <span class="text-danger">*</span></label>
                        <textarea id="description" name="description" class="form-control" rows="3" placeholder="Provide a summary of the assignment task, problem statement, or expected output..." required>${description}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="instructions" class="form-label">Detailed Guidelines / Instructions</label>
                        <textarea id="instructions" name="instructions" class="form-control" rows="3" placeholder="e.g. 1. Submit report in PDF format. 2. Include ER diagram screenshots. 3. Maximum file size 10MB.">${instructions}</textarea>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label for="dueDate" class="form-label">Submission Due Date <span class="text-danger">*</span></label>
                            <input type="date" id="dueDate" name="dueDate" class="form-control" value="${dueDate}" required>
                        </div>

                        <div class="form-group">
                            <label for="dueTime" class="form-label">Due Time</label>
                            <input type="time" id="dueTime" name="dueTime" class="form-control" value="${not empty dueTime ? dueTime : '23:59'}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="attachment" class="form-label">Optional Course Material / Problem Attachment</label>
                        <input type="file" id="attachment" name="attachment" class="form-control" accept=".pdf,.docx,.doc,.pptx,.ppt,.zip">
                        <div class="form-text">Allowed formats: PDF, DOCX, PPTX, ZIP (Max 10MB).</div>
                    </div>

                    <div style="display: flex; gap: 1rem; justify-content: flex-end; margin-top: 1.5rem;">
                        <a href="${pageContext.request.contextPath}/faculty/assignments" class="btn btn-secondary">
                            Cancel
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-floppy-disk"></i> Publish Assignment
                        </button>
                    </div>
                </form>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
