<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Profile &amp; Settings &bull; Faculty Portal" />
<c:set var="pageHeading" value="Faculty Profile &amp; Account" />
<c:set var="activeMenu" value="profile" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="app-main">
        <jsp:include page="/WEB-INF/views/fragments/navbar.jsp" />

        <main class="content-container">
            <jsp:include page="/WEB-INF/views/fragments/alerts.jsp" />

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
                <!-- Profile Information Card -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-user-pen text-primary"></i> Personal Details</h3>
                    </div>

                    <form action="${pageContext.request.contextPath}/profile/update" method="post">
                        <div class="form-group">
                            <label class="form-label">Email Address (Read-Only)</label>
                            <input type="text" class="form-control" value="${user.email}" disabled>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Role</label>
                            <input type="text" class="form-control" value="Faculty Member" disabled>
                        </div>

                        <div class="form-group">
                            <label for="name" class="form-label">Full Name &amp; Title <span class="text-danger">*</span></label>
                            <input type="text" id="name" name="name" class="form-control" value="${user.name}" required>
                        </div>

                        <div class="form-group">
                            <label for="department" class="form-label">Academic Department</label>
                            <input type="text" id="department" name="department" class="form-control" value="${user.department}" required>
                        </div>

                        <button type="submit" class="btn btn-primary" style="margin-top: 0.5rem;">
                            <i class="fa-solid fa-floppy-disk"></i> Save Profile Details
                        </button>
                    </form>
                </div>

                <!-- Change Password Card -->
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fa-solid fa-lock text-primary"></i> Change Password</h3>
                    </div>

                    <form action="${pageContext.request.contextPath}/profile/change-password" method="post">
                        <div class="form-group">
                            <label for="currentPassword" class="form-label">Current Password <span class="text-danger">*</span></label>
                            <input type="password" id="currentPassword" name="currentPassword" class="form-control" placeholder="Enter current password" required>
                        </div>

                        <div class="form-group">
                            <label for="newPassword" class="form-label">New Password <span class="text-danger">*</span></label>
                            <input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="Min. 6 characters" required>
                        </div>

                        <div class="form-group">
                            <label for="confirmNewPassword" class="form-label">Confirm New Password <span class="text-danger">*</span></label>
                            <input type="password" id="confirmNewPassword" name="confirmNewPassword" class="form-control" placeholder="Re-enter new password" required>
                        </div>

                        <button type="submit" class="btn btn-primary" style="margin-top: 0.5rem;">
                            <i class="fa-solid fa-key"></i> Update Password
                        </button>
                    </form>
                </div>
            </div>
        </main>

        <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
    </div>
</div>
