package com.college.assignment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateUtil, deadline calculations, and late submission handling.
 */
public class DateUtilTest {

    @Test
    @DisplayName("Should detect overdue assignments properly")
    void testIsOverdue() {
        Date pastDate = Date.valueOf(LocalDate.now().minusDays(2));
        Time dueTime = Time.valueOf("23:59:00");
        assertTrue(DateUtil.isOverdue(pastDate, dueTime));

        Date futureDate = Date.valueOf(LocalDate.now().plusDays(5));
        assertFalse(DateUtil.isOverdue(futureDate, dueTime));
    }

    @Test
    @DisplayName("Should detect late submissions accurately against deadline")
    void testIsLateSubmission() {
        Date dueDate = Date.valueOf("2026-10-10");
        Time dueTime = Time.valueOf("18:00:00");

        Timestamp onTime = Timestamp.valueOf("2026-10-10 17:30:00");
        assertFalse(DateUtil.isLateSubmission(dueDate, dueTime, onTime));

        Timestamp late = Timestamp.valueOf("2026-10-10 18:05:00");
        assertTrue(DateUtil.isLateSubmission(dueDate, dueTime, late));

        Timestamp nextDay = Timestamp.valueOf("2026-10-11 09:00:00");
        assertTrue(DateUtil.isLateSubmission(dueDate, dueTime, nextDay));
    }

    @Test
    @DisplayName("Should generate appropriate deadline status text")
    void testGetDeadlineStatusText() {
        Date today = Date.valueOf(LocalDate.now());
        Time dueTime = Time.valueOf("23:59:00");
        String todayText = DateUtil.getDeadlineStatusText(today, dueTime);
        assertTrue(todayText.contains("Due Today"));

        Date tomorrow = Date.valueOf(LocalDate.now().plusDays(1));
        String tomorrowText = DateUtil.getDeadlineStatusText(tomorrow, dueTime);
        assertTrue(tomorrowText.contains("Due Tomorrow"));

        Date in5Days = Date.valueOf(LocalDate.now().plusDays(5));
        String in5DaysText = DateUtil.getDeadlineStatusText(in5Days, dueTime);
        assertEquals("Due in 5 days", in5DaysText);

        Date past = Date.valueOf(LocalDate.now().minusDays(3));
        String pastText = DateUtil.getDeadlineStatusText(past, dueTime);
        assertTrue(pastText.contains("Overdue"));
    }
}
