package com.college.assignment.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility for date/time operations, deadline tracking, and late submission calculation.
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    /**
     * Checks if current server time has surpassed the assignment deadline.
     */
    public static boolean isOverdue(Date dueDate, Time dueTime) {
        if (dueDate == null) return false;
        LocalDateTime deadline = getDeadlineLocalDateTime(dueDate, dueTime);
        return LocalDateTime.now().isAfter(deadline);
    }

    /**
     * Checks if a submission timestamp was after the assignment deadline.
     */
    public static boolean isLateSubmission(Date dueDate, Time dueTime, Timestamp submittedAt) {
        if (dueDate == null || submittedAt == null) return false;
        LocalDateTime deadline = getDeadlineLocalDateTime(dueDate, dueTime);
        LocalDateTime submissionTime = submittedAt.toLocalDateTime();
        return submissionTime.isAfter(deadline);
    }

    /**
     * Generates user-friendly deadline label: "Due Today", "Due Tomorrow", "Due in X days", "Overdue".
     */
    public static String getDeadlineStatusText(Date dueDate, Time dueTime) {
        if (dueDate == null) return "No Deadline";
        LocalDateTime deadline = getDeadlineLocalDateTime(dueDate, dueTime);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(deadline)) {
            long hoursAgo = ChronoUnit.HOURS.between(deadline, now);
            if (hoursAgo < 24) {
                return "Overdue (" + hoursAgo + " hrs ago)";
            }
            long daysAgo = ChronoUnit.DAYS.between(deadline.toLocalDate(), now.toLocalDate());
            return "Overdue (" + daysAgo + (daysAgo == 1 ? " day ago)" : " days ago)");
        }

        LocalDate today = now.toLocalDate();
        LocalDate due = dueDate.toLocalDate();

        if (today.isEqual(due)) {
            return "Due Today (" + deadline.format(TIME_FORMAT) + ")";
        } else if (today.plusDays(1).isEqual(due)) {
            return "Due Tomorrow (" + deadline.format(TIME_FORMAT) + ")";
        } else {
            long daysLeft = ChronoUnit.DAYS.between(today, due);
            return "Due in " + daysLeft + " days";
        }
    }

    public static LocalDateTime getDeadlineLocalDateTime(Date dueDate, Time dueTime) {
        LocalDate date = dueDate.toLocalDate();
        LocalTime time = (dueTime != null) ? dueTime.toLocalTime() : LocalTime.of(23, 59, 0);
        return LocalDateTime.of(date, time);
    }

    public static String formatDate(Date date) {
        if (date == null) return "-";
        return date.toLocalDate().format(DATE_FORMAT);
    }

    public static String formatTime(Time time) {
        if (time == null) return "-";
        return time.toLocalTime().format(TIME_FORMAT);
    }

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return timestamp.toLocalDateTime().format(DATE_TIME_FORMAT);
    }

    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return Date.valueOf(dateStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static Time parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return Time.valueOf("23:59:00");
        }
        try {
            String trimmed = timeStr.trim();
            if (trimmed.length() == 5) {
                trimmed += ":00"; // convert HH:mm to HH:mm:ss
            }
            return Time.valueOf(trimmed);
        } catch (Exception e) {
            return Time.valueOf("23:59:00");
        }
    }
}
