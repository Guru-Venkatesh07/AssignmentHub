package com.college.assignment.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model representing an Academic Assignment.
 */
public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        ACTIVE,
        CLOSED,
        ARCHIVED
    }

    private int id;
    private int subjectId;
    private int facultyId;
    private String title;
    private String description;
    private String instructions;
    private String attachmentPath;
    private Date dueDate;
    private Time dueTime;
    private int maxMarks;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined / Virtual Fields for UI
    private String subjectName;
    private String subjectCode;
    private String facultyName;
    private int submissionCount;
    private int pendingEvaluationCount;
    private String studentSubmissionStatus; // PENDING, SUBMITTED, LATE, EVALUATED
    private Integer studentMarks;
    private boolean isSubmittedByCurrentStudent;
    private boolean isOverdue;
    private String deadlineStatusText; // e.g. "Due Today", "Due Tomorrow", "Due in 3 Days", "Overdue"

    public Assignment() {
        this.maxMarks = 100;
        this.status = Status.ACTIVE;
    }

    public Assignment(int id, int subjectId, int facultyId, String title, String description,
                      String instructions, String attachmentPath, Date dueDate, Time dueTime,
                      int maxMarks, Status status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.facultyId = facultyId;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.attachmentPath = attachmentPath;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.maxMarks = maxMarks;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Time getDueTime() {
        return dueTime;
    }

    public void setDueTime(Time dueTime) {
        this.dueTime = dueTime;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public int getPendingEvaluationCount() {
        return pendingEvaluationCount;
    }

    public void setPendingEvaluationCount(int pendingEvaluationCount) {
        this.pendingEvaluationCount = pendingEvaluationCount;
    }

    public String getStudentSubmissionStatus() {
        return studentSubmissionStatus;
    }

    public void setStudentSubmissionStatus(String studentSubmissionStatus) {
        this.studentSubmissionStatus = studentSubmissionStatus;
    }

    public Integer getStudentMarks() {
        return studentMarks;
    }

    public void setStudentMarks(Integer studentMarks) {
        this.studentMarks = studentMarks;
    }

    public boolean isSubmittedByCurrentStudent() {
        return isSubmittedByCurrentStudent;
    }

    public void setSubmittedByCurrentStudent(boolean submittedByCurrentStudent) {
        isSubmittedByCurrentStudent = submittedByCurrentStudent;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public String getDeadlineStatusText() {
        return deadlineStatusText;
    }

    public void setDeadlineStatusText(String deadlineStatusText) {
        this.deadlineStatusText = deadlineStatusText;
    }
}
