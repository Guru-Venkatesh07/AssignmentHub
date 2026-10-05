<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Notifications &bull; Student Portal" />
<c:set var="pageHeading" value="My Notifications" />
<c:set var="activeMenu" value="notifications" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><i class="fa-regular fa-bell text-primary"></i> In-App Alerts &amp; Updates</h3>
                    <c:if test="${not empty notifications}">
                        <form action="${pageContext.request.contextPath}/notifications/read-all" method="post">
                            <button type="submit" class="btn btn-sm btn-outline">
                                <i class="fa-solid fa-check-double"></i> Mark All as Read
                            </button>
                        </form>
                    </c:if>
                </div>

                <c:choose>
                    <c:when test="${not empty notifications}">
                        <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                            <c:forEach var="n" items="${notifications}">
                                <div style="padding: 1rem 1.25rem; border-radius: var(--radius-md); border: 1px solid var(--border-color); background: ${n.read ? 'var(--bg-card)' : 'var(--primary-light)'}; display: flex; justify-content: space-between; align-items: center;">
                                    <div style="display: flex; gap: 1rem; align-items: center;">
                                        <div style="font-size: 1.25rem; color: ${n.type == 'EVALUATION' ? 'var(--success)' : (n.type == 'DEADLINE' ? 'var(--warning)' : 'var(--primary)')};">
                                            <c:choose>
                                                <c:when test="${n.type == 'EVALUATION'}"><i class="fa-solid fa-award"></i></c:when>
                                                <c:when test="${n.type == 'DEADLINE'}"><i class="fa-regular fa-clock"></i></c:when>
                                                <c:when test="${n.type == 'ASSIGNMENT'}"><i class="fa-solid fa-book"></i></c:when>
                                                <c:otherwise><i class="fa-solid fa-circle-info"></i></c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div>
                                            <div style="font-weight: 700; color: var(--gray-900); font-size: 0.95rem;">${n.title}</div>
                                            <div style="color: var(--gray-700); font-size: 0.88rem; margin-top: 2px;">${n.message}</div>
                                            <div class="text-muted" style="font-size: 0.75rem; margin-top: 4px;">${n.createdAt}</div>
                                        </div>
                                    </div>
                                    <c:if test="${not n.read}">
                                        <form action="${pageContext.request.contextPath}/notifications/read" method="post">
                                            <input type="hidden" name="id" value="${n.id}">
                                            <button type="submit" class="btn btn-sm btn-outline" title="Mark Read">
                                                <i class="fa-solid fa-check"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="icon"><i class="fa-regular fa-bell-slash"></i></div>
                            <h4>No Notifications</h4>
                            <p>You have no notifications or alerts at this time.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
