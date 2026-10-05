<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Evaluation Results &bull; Student Portal" />
<c:set var="pageHeading" value="Academic Results &amp; Feedback" />
<c:set var="activeMenu" value="results" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <!-- Overall Performance Summary Banner -->
            <div class="card" style="background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%); color: #ffffff;">
                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <span class="badge badge-active" style="background: rgba(255,255,255,0.15); color: #38bdf8; margin-bottom: 0.5rem;">Academic Grade Summary</span>
                        <h2 style="color: #ffffff; font-size: 1.6rem;">Overall Evaluation Performance</h2>
                        <p style="color: #94a3b8; font-size: 0.9rem; margin-top: 0.25rem;">Cumulative average across all evaluated academic assignments</p>
                    </div>
                    <div style="text-align: right;">
                        <div style="font-size: 2.75rem; font-weight: 800; color: #38bdf8; line-height: 1;">
                            ${averagePercentage}%
                        </div>
                        <div style="color: #94a3b8; font-size: 0.8rem; margin-top: 4px;">Overall Score Average</div>
                    </div>
                </div>
            </div>

            <!-- Detailed Evaluation Results Table -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-solid fa-square-poll-vertical text-primary"></i> Evaluated Assignment Scores</h3>
                </div>

                <c:choose>
                    <c:when test="${not empty results}">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Assignment &amp; Subject</th>
                                        <th>Submitted Date</th>
                                        <th>Score / Max</th>
                                        <th>Performance Bar</th>
                                        <th>Faculty Feedback</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="r" items="${results}">
                                        <tr>
                                            <td>
                                                <div class="fw-bold" style="color: var(--gray-900);">${r.assignmentTitle}</div>
                                                <div class="text-muted" style="font-size: 0.78rem;">${r.subjectCode} - ${r.subjectName}</div>
                                            </td>
                                            <td>
                                                <div style="font-size: 0.85rem;">${r.submittedAt}</div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${r.status == 'EVALUATED'}">
                                                        <span style="font-size: 1.1rem; font-weight: 800; color: var(--success);">${r.marks}</span>
                                                        <span class="text-muted" style="font-size: 0.85rem;">/ ${r.maxMarks}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-pending">Under Evaluation</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="min-width: 140px;">
                                                <c:choose>
                                                    <c:when test="${r.status == 'EVALUATED'}">
                                                        <c:set var="pct" value="${(r.marks * 100) / r.maxMarks}" />
                                                        <div style="background: var(--gray-200); border-radius: var(--radius-full); height: 8px; overflow: hidden; width: 100%;">
                                                            <div style="background: ${pct >= 80 ? 'var(--success)' : (pct >= 50 ? 'var(--primary)' : 'var(--danger)')}; height: 100%; width: ${pct}%;"></div>
                                                        </div>
                                                        <div style="font-size: 0.75rem; color: var(--text-muted); margin-top: 2px;">${pct}%</div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted" style="font-size: 0.8rem;">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="max-width: 300px;">
                                                <c:choose>
                                                    <c:when test="${not empty r.feedback}">
                                                        <div style="font-size: 0.85rem; color: var(--gray-700); background: var(--gray-50); padding: 0.5rem 0.75rem; border-radius: var(--radius-sm);">
                                                            "${r.feedback}"
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted" style="font-size: 0.8rem;">No written remarks provided.</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge badge-${r.status.name().toLowerCase()}">${r.status}</span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="icon"><i class="fa-solid fa-chart-simple"></i></div>
                            <h4>No Results Available</h4>
                            <p>You have no evaluated submissions at this time.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
