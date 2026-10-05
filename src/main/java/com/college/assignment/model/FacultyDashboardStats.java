package com.college.assignment.model;

import java.io.Serializable;

/**
 * Model holding calculated Dashboard metrics for a Faculty member.
 */
public class FacultyDashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int assignmentsCreated;
    private int totalSubmissions;
    private int pendingEvaluations;
    private int evaluatedSubmissions;
    private int lateSubmissions;
    private int totalSubjectsAssigned;
    private int unreadNotifications;

    public FacultyDashboardStats() {}

    public FacultyDashboardStats(int assignmentsCreated, int totalSubmissions, int pendingEvaluations,
                                 int evaluatedSubmissions, int lateSubmissions, int totalSubjectsAssigned,
                                 int unreadNotifications) {
        this.assignmentsCreated = assignmentsCreated;
        this.totalSubmissions = totalSubmissions;
        this.pendingEvaluations = pendingEvaluations;
        this.evaluatedSubmissions = evaluatedSubmissions;
        this.lateSubmissions = lateSubmissions;
        this.totalSubjectsAssigned = totalSubjectsAssigned;
        this.unreadNotifications = unreadNotifications;
    }

    public int getAssignmentsCreated() {
        return assignmentsCreated;
    }

    public void setAssignmentsCreated(int assignmentsCreated) {
        this.assignmentsCreated = assignmentsCreated;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public int getPendingEvaluations() {
        return pendingEvaluations;
    }

    public void setPendingEvaluations(int pendingEvaluations) {
        this.pendingEvaluations = pendingEvaluations;
    }

    public int getEvaluatedSubmissions() {
        return evaluatedSubmissions;
    }

    public void setEvaluatedSubmissions(int evaluatedSubmissions) {
        this.evaluatedSubmissions = evaluatedSubmissions;
    }

    public int getLateSubmissions() {
        return lateSubmissions;
    }

    public void setLateSubmissions(int lateSubmissions) {
        this.lateSubmissions = lateSubmissions;
    }

    public int getTotalSubjectsAssigned() {
        return totalSubjectsAssigned;
    }

    public void setTotalSubjectsAssigned(int totalSubjectsAssigned) {
        this.totalSubjectsAssigned = totalSubjectsAssigned;
    }

    public int getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(int unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }
}
