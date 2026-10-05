package com.college.assignment.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an Assignment Evaluation.
 */
public class Evaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int submissionId;
    private int facultyId;
    private int marks;
    private String feedback;
    private Timestamp evaluatedAt;
    private Timestamp updatedAt;

    // Joined Fields
    private String facultyName;
    private String studentName;
    private String studentRegisterNumber;
    private String assignmentTitle;
    private String subjectName;
    private int maxMarks;

    public Evaluation() {}

    public Evaluation(int id, int submissionId, int facultyId, int marks, String feedback, Timestamp evaluatedAt, Timestamp updatedAt) {
        this.id = id;
        this.submissionId = submissionId;
        this.facultyId = facultyId;
        this.marks = marks;
        this.feedback = feedback;
        this.evaluatedAt = evaluatedAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Timestamp getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Timestamp evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegisterNumber() {
        return studentRegisterNumber;
    }

    public void setStudentRegisterNumber(String studentRegisterNumber) {
        this.studentRegisterNumber = studentRegisterNumber;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }
}
