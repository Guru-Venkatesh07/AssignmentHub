<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty errorMessage or not empty param.error}">
    <div class="alert alert-danger alert-dismissible" role="alert">
        <i class="fa-solid fa-circle-exclamation"></i>
        <span>${not empty errorMessage ? errorMessage : param.error}</span>
    </div>
</c:if>

<c:if test="${not empty successMessage or not empty param.success}">
    <div class="alert alert-success alert-dismissible" role="alert">
        <i class="fa-solid fa-circle-check"></i>
        <span>${not empty successMessage ? successMessage : param.success}</span>
    </div>
</c:if>

<c:if test="${not empty infoMessage or not empty param.info}">
    <div class="alert alert-info alert-dismissible" role="alert">
        <i class="fa-solid fa-circle-info"></i>
        <span>${not empty infoMessage ? infoMessage : param.info}</span>
    </div>
</c:if>
