package com.college.assignment.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an In-App Notification.
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        ASSIGNMENT,
        SUBMISSION,
        DEADLINE,
        EVALUATION,
        SYSTEM
    }

    private int id;
    private int userId;
    private String title;
    private String message;
    private Type type;
    private boolean isRead;
    private Timestamp createdAt;

    public Notification() {
        this.type = Type.SYSTEM;
        this.isRead = false;
    }

    public Notification(int id, int userId, String title, String message, Type type, boolean isRead, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
