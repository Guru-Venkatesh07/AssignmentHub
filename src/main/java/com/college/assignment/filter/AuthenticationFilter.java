package com.college.assignment.filter;

import com.college.assignment.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to enforce authentication and role-based authorization across protected routes.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/student/*", "/faculty/*", "/profile/*", "/notifications/*",
        "/download/*", "/api/student/*", "/api/faculty/*", "/api/notifications/*"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prevent browser caching of protected pages
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (!isLoggedIn) {
            if (isAjaxRequest(httpRequest)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"success\":false,\"error\":\"Unauthorized. Session expired.\"}");
                return;
            }
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=Please+login+to+continue");
            return;
        }

        User.Role userRole = (User.Role) session.getAttribute("userRole");

        // Role-based Access Control
        if (path.startsWith("/student") || path.startsWith("/api/student")) {
            if (userRole != User.Role.STUDENT) {
                sendForbidden(httpRequest, httpResponse, "Students only. Faculty accounts cannot access student pages.");
                return;
            }
        } else if (path.startsWith("/faculty") || path.startsWith("/api/faculty")) {
            if (userRole != User.Role.FACULTY) {
                sendForbidden(httpRequest, httpResponse, "Faculty only. Student accounts cannot access faculty pages.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        String uri = request.getRequestURI();
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith) ||
                (accept != null && accept.contains("application/json")) ||
                uri.contains("/api/");
    }

    private void sendForbidden(HttpServletRequest request, HttpServletResponse response, String message) throws IOException, ServletException {
        if (isAjaxRequest(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"error\":\"" + message + "\"}");
        } else {
            request.setAttribute("errorMessage", message);
            request.getRequestDispatcher("/WEB-INF/views/errors/403.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
