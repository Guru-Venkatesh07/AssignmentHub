package com.college.assignment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FixPasswords {
    public static void main(String[] args) {
        String studentHash = PasswordUtil.hashPassword("student123");
        String facultyHash = PasswordUtil.hashPassword("faculty123");

        System.out.println("Generated Student Hash: " + studentHash);
        System.out.println("Generated Faculty Hash: " + facultyHash);

        String url = "jdbc:mysql://localhost:3306/assignment_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        try (Connection conn = DriverManager.getConnection(url, "root", "root")) {
            // Update student password
            try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET password_hash = ? WHERE role = 'STUDENT'")) {
                ps.setString(1, studentHash);
                int count = ps.executeUpdate();
                System.out.println("Updated " + count + " student account(s) with password 'student123'");
            }

            // Update faculty password
            try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET password_hash = ? WHERE role = 'FACULTY'")) {
                ps.setString(1, facultyHash);
                int count = ps.executeUpdate();
                System.out.println("Updated " + count + " faculty account(s) with password 'faculty123'");
            }

            System.out.println("PASSWORDS FIXED SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
