package com.college.assignment.controller;

import com.college.assignment.model.Notification;
import com.college.assignment.model.User;
import com.college.assignment.service.NotificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet handling User Notifications page and read actions.
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications", "/notifications/read", "/notifications/read-all"})
public class NotificationServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        User.Role role = (User.Role) session.getAttribute("userRole");

        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        req.setAttribute("notifications", notifications);

        if (role == User.Role.FACULTY) {
            req.getRequestDispatcher("/WEB-INF/views/faculty/notifications.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/student/notifications.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();

        if ("/notifications/read-all".equals(path)) {
            notificationService.markAllAsRead(userId);
            resp.sendRedirect(req.getContextPath() + "/notifications");
        } else if ("/notifications/read".equals(path)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                notificationService.markAsRead(id, userId);
            } catch (Exception ignored) {}
            resp.sendRedirect(req.getContextPath() + "/notifications");
        } else {
            doGet(req, resp);
        }
    }
}
