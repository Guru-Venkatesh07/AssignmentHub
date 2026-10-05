package com.college.assignment.controller;

import com.college.assignment.service.NotificationService;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AJAX Endpoint for unread notification count and marking notifications read.
 */
@WebServlet(name = "ApiNotificationServlet", urlPatterns = {
        "/api/notifications/count",
        "/api/notifications/read",
        "/api/notifications/read-all"
})
public class ApiNotificationServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        int count = notificationService.getUnreadCount(userId);

        JsonObject json = new JsonObject();
        json.addProperty("unreadCount", count);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String path = req.getServletPath();
        JsonObject json = new JsonObject();

        if ("/api/notifications/read-all".equals(path)) {
            boolean ok = notificationService.markAllAsRead(userId);
            json.addProperty("success", ok);
            json.addProperty("unreadCount", 0);
        } else if ("/api/notifications/read".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    int notifId = Integer.parseInt(idStr);
                    boolean ok = notificationService.markAsRead(notifId, userId);
                    json.addProperty("success", ok);
                } catch (NumberFormatException e) {
                    json.addProperty("success", false);
                }
            }
            json.addProperty("unreadCount", notificationService.getUnreadCount(userId));
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json.toString());
    }
}
