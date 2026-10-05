<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Course Data XML &bull; AssignmentHub" />
<c:set var="pageHeading" value="Course Data Feeds &amp; XML Viewer" />
<c:set var="activeMenu" value="xml" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <c:choose>
        <c:when test="${not empty sessionScope.userId}">
            <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />
            <div class="app-main">
                <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />
                <div class="content-container">
                    <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />
                    <div id="xmlViewContent">
        </c:when>
        <c:otherwise>
            <div class="app-main" style="margin-left: 0;">
                <header style="background: #0f172a; padding: 1.25rem 2rem; border-bottom: 1px solid #1e293b; display: flex; justify-content: space-between; align-items: center;">
                    <a href="${pageContext.request.contextPath}/" class="sidebar-brand">
                        <div class="sidebar-brand-icon"><i class="fa-solid fa-graduation-cap"></i></div>
                        <span>AssignmentHub</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-sm btn-primary">Sign In</a>
                </header>
                <div class="content-container">
                    <div id="xmlViewContent">
        </c:otherwise>
    </c:choose>

    <!-- Header Banner -->
    <div class="card" style="border-left: 4px solid var(--primary);">
        <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 1rem;">
            <div>
                <span class="badge badge-active" style="margin-bottom: 0.5rem;">Data Export &amp; Feeds</span>
                <h2>Course Data Exchange &amp; XML Viewer</h2>
                <p class="text-muted" style="max-width: 800px; margin-top: 0.25rem;">
                    Fetch and explore live academic course feeds and schedules in structured XML format with real-time browser-side parsing.
                </p>
            </div>
            <div style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
                <button type="button" class="btn btn-primary" onclick="loadAssignmentsXml()">
                    <i class="fa-solid fa-cloud-arrow-down"></i> Load Assignments XML
                </button>
                <button type="button" class="btn btn-secondary" onclick="loadTimetableXml()">
                    <i class="fa-solid fa-calendar-days"></i> Load Timetable XML
                </button>
                <a href="${pageContext.request.contextPath}/api/assignments.xml" target="_blank" class="btn btn-outline" title="Open Raw XML endpoint">
                    <i class="fa-solid fa-arrow-up-right-from-square"></i> Raw XML
                </a>
            </div>
        </div>
    </div>

    <!-- Status Message Display -->
    <div id="xmlStatusMsg" style="margin-bottom: 1rem; font-size: 0.92rem;"></div>

    <!-- Dynamic Rendered HTML Table Container -->
    <div class="card">
        <div class="card-header">
            <h3 class="card-title"><i class="fa-solid fa-table"></i> Parsed XML HTML Table</h3>
        </div>
        <div id="xmlOutputContainer">
            <div class="empty-state">
                <div class="icon"><i class="fa-solid fa-code"></i></div>
                <h4>No XML Data Loaded Yet</h4>
                <p>Click "Load Assignments XML" or "Load Timetable XML" above to fetch and parse server XML asynchronously.</p>
            </div>
        </div>
    </div>

    <!-- Raw XML Payload Inspector -->
    <div class="card">
        <div class="card-header">
            <h3 class="card-title"><i class="fa-solid fa-file-code"></i> Raw XML Payload Received From Server</h3>
        </div>
        <pre id="rawXmlDisplay" style="background: #0f172a; color: #38bdf8; padding: 1.25rem; border-radius: var(--radius-md); overflow-x: auto; font-family: 'JetBrains Mono', monospace; font-size: 0.85rem; max-height: 350px;">Click one of the buttons above to inspect the raw XML HTTP response...</pre>
    </div>

    <!-- End Content -->
    </div>
    </div>
    <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>

<script>
    // Auto load assignments XML on page ready
    document.addEventListener('DOMContentLoaded', () => {
        loadAssignmentsXml();
    });
</script>
