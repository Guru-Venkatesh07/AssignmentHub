package com.college.assignment.model;

import java.io.Serializable;

/**
 * Model holding calculated Dashboard metrics for a Student.
 */
public class StudentDashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalAssignments;
    private int submitted;
    private int pending;
    private int evaluated;
    private int late;
    private double averageMarks;
    private int unreadNotifications;

    public StudentDashboardStats() {}

    public StudentDashboardStats(int totalAssignments, int submitted, int pending, int evaluated, int late, double averageMarks, int unreadNotifications) {
        this.totalAssignments = totalAssignments;
        this.submitted = submitted;
        this.pending = pending;
        this.evaluated = evaluated;
        this.late = late;
        this.averageMarks = averageMarks;
        this.unreadNotifications = unreadNotifications;
    }

    public int getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(int totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(int evaluated) {
        this.evaluated = evaluated;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public int getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(int unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }
}
