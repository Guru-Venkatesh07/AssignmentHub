package com.college.assignment.service;

import com.college.assignment.dao.NotificationDAO;
import com.college.assignment.model.Notification;

import java.util.List;

/**
 * Service managing user notifications and read/unread statuses.
 */
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public List<Notification> getNotificationsForUser(int userId) {
        return notificationDAO.findByUserId(userId);
    }

    public List<Notification> getUnreadNotificationsForUser(int userId) {
        return notificationDAO.findUnreadByUserId(userId);
    }

    public int getUnreadCount(int userId) {
        return notificationDAO.countUnreadByUserId(userId);
    }

    public boolean markAsRead(int notificationId, int userId) {
        return notificationDAO.markAsRead(notificationId, userId);
    }

    public boolean markAllAsRead(int userId) {
        return notificationDAO.markAllAsRead(userId);
    }
}
