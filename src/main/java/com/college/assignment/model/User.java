package com.college.assignment.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an Application User (STUDENT or FACULTY).
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Role {
        STUDENT,
        FACULTY
    }

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private Role role;
    private String registerNumber;
    private String department;
    private Integer year;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(int id, String name, String email, String passwordHash, Role role,
                String registerNumber, String department, Integer year,
                Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.registerNumber = registerNumber;
        this.department = department;
        this.year = year;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public boolean isStudent() {
        return Role.STUDENT.equals(this.role);
    }

    public boolean isFaculty() {
        return Role.FACULTY.equals(this.role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", registerNumber='" + registerNumber + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                '}';
    }
}
