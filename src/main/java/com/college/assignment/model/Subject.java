package com.college.assignment.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an Academic Subject.
 */
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String subjectCode;
    private String subjectName;
    private int semester;
    private String department;
    private Timestamp createdAt;

    public Subject() {}

    public Subject(int id, String subjectCode, String subjectName, int semester, String department, Timestamp createdAt) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.department = department;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", semester=" + semester +
                ", department='" + department + '\'' +
                '}';
    }
}
