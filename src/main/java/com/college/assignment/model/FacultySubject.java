package com.college.assignment.model;

import java.io.Serializable;

/**
 * Model representing Mapping between Faculty and Subject.
 */
public class FacultySubject implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int facultyId;
    private int subjectId;
    private String facultyName;
    private String subjectCode;
    private String subjectName;
    private String department;

    public FacultySubject() {}

    public FacultySubject(int id, int facultyId, int subjectId) {
        this.id = id;
        this.facultyId = facultyId;
        this.subjectId = subjectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
