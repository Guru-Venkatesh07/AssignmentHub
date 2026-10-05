package com.college.assignment.service;

import com.college.assignment.dao.UserDAO;
import com.college.assignment.model.User;
import com.college.assignment.util.PasswordUtil;
import com.college.assignment.util.ValidationUtil;

import java.util.logging.Logger;

/**
 * Service handling Authentication, Registration, and User Profile operations.
 */
public class AuthenticationService {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates user by email and plain-text password.
     * @return Authenticated User or null if invalid
     */
    public User login(String email, String plainPassword) {
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(plainPassword)) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            LOGGER.warning("Login failed: Email not found - " + email);
            return null;
        }

        if (PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash())) {
            LOGGER.info("Login successful for user: " + user.getEmail() + " [Role: " + user.getRole() + "]");
            return user;
        } else {
            LOGGER.warning("Login failed: Invalid password for email - " + email);
            return null;
        }
    }

    /**
     * Registers a new Student account.
     */
    public String registerStudent(String name, String email, String registerNumber,
                                  String department, Integer year, String password, String confirmPassword) {
        if (!ValidationUtil.isNotEmpty(name)) {
            return "Full Name is required.";
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return "Please provide a valid email address.";
        }
        if (!ValidationUtil.isValidRegisterNumber(registerNumber)) {
            return "Register Number must be 5-20 alphanumeric characters.";
        }
        if (!ValidationUtil.isNotEmpty(department)) {
            return "Department is required.";
        }
        if (year == null || year < 1 || year > 5) {
            return "Academic Year must be between 1 and 5.";
        }
        if (!ValidationUtil.isValidPassword(password)) {
            return "Password must be at least 6 characters long.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        if (userDAO.existsByEmail(email.trim())) {
            return "An account with this email already exists.";
        }
        if (userDAO.existsByRegisterNumber(registerNumber.trim())) {
            return "An account with this register number already exists.";
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User student = new User();
        student.setName(name.trim());
        student.setEmail(email.trim().toLowerCase());
        student.setPasswordHash(hashedPassword);
        student.setRole(User.Role.STUDENT);
        student.setRegisterNumber(registerNumber.trim().toUpperCase());
        student.setDepartment(department.trim());
        student.setYear(year);

        boolean created = userDAO.create(student);
        if (created) {
            LOGGER.info("Student registered successfully: " + student.getEmail() + " (" + student.getRegisterNumber() + ")");
            return null; // success
        } else {
            return "Failed to register student. Please try again later.";
        }
    }

    public boolean isEmailAvailable(String email) {
        if (!ValidationUtil.isValidEmail(email)) {
            return false;
        }
        return !userDAO.existsByEmail(email.trim());
    }

    public boolean isRegisterNumberAvailable(String registerNumber) {
        if (!ValidationUtil.isValidRegisterNumber(registerNumber)) {
            return false;
        }
        return !userDAO.existsByRegisterNumber(registerNumber.trim());
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public boolean updateProfile(int userId, String name, String department, Integer year) {
        User u = userDAO.findById(userId);
        if (u == null) return false;
        u.setName(name);
        u.setDepartment(department);
        u.setYear(year);
        return userDAO.updateProfile(u);
    }

    public String changePassword(int userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!ValidationUtil.isValidPassword(newPassword)) {
            return "New password must be at least 6 characters long.";
        }
        if (!newPassword.equals(confirmNewPassword)) {
            return "New passwords do not match.";
        }
        User u = userDAO.findById(userId);
        if (u == null) {
            return "User not found.";
        }
        if (!PasswordUtil.verifyPassword(currentPassword, u.getPasswordHash())) {
            return "Current password is incorrect.";
        }

        String hashed = PasswordUtil.hashPassword(newPassword);
        boolean ok = userDAO.updatePassword(userId, hashed);
        return ok ? null : "Failed to update password.";
    }
}
