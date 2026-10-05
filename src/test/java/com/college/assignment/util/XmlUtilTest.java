package com.college.assignment.util;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for XML payload building and escaping.
 */
public class XmlUtilTest {

    @Test
    @DisplayName("Should correctly escape XML special characters")
    void testEscapeXml() {
        String input = "DBMS & Networks <1NF & 2NF> 'Theory' \"Project\"";
        String escaped = XmlUtil.escapeXml(input);

        assertFalse(escaped.contains("<1NF"));
        assertTrue(escaped.contains("&amp;"));
        assertTrue(escaped.contains("&lt;1NF"));
        assertTrue(escaped.contains("&gt;"));
        assertTrue(escaped.contains("&apos;"));
        assertTrue(escaped.contains("&quot;"));
    }

    @Test
    @DisplayName("Should generate valid XML structure for assignments")
    void testToAssignmentsXml() {
        List<Assignment> list = new ArrayList<>();
        Assignment a = new Assignment();
        a.setId(1);
        a.setTitle("Test Assignment & Lab");
        a.setSubjectCode("CS8501");
        a.setSubjectName("DBMS");
        a.setFacultyName("Dr. Rajesh");
        a.setDescription("Explain BCNF & 3NF");
        a.setDueDate(Date.valueOf("2026-10-25"));
        a.setDueTime(Time.valueOf("23:59:00"));
        a.setMaxMarks(100);
        list.add(a);

        String xml = XmlUtil.toAssignmentsXml(list);

        assertNotNull(xml);
        assertTrue(xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(xml.contains("<assignments total=\"1\">"));
        assertTrue(xml.contains("<assignment id=\"1\">"));
        assertTrue(xml.contains("<title>Test Assignment &amp; Lab</title>"));
        assertTrue(xml.contains("</assignments>"));
    }

    @Test
    @DisplayName("Should generate valid XML structure for timetable/curriculum")
    void testToTimetableXml() {
        List<Subject> list = new ArrayList<>();
        Subject s = new Subject(1, "CS8503", "Web Technology & Lab", 5, "CSE", null);
        list.add(s);

        String xml = XmlUtil.toTimetableXml(list);

        assertNotNull(xml);
        assertTrue(xml.contains("<curriculum totalCourses=\"1\">"));
        assertTrue(xml.contains("<code>CS8503</code>"));
        assertTrue(xml.contains("<name>Web Technology &amp; Lab</name>"));
        assertTrue(xml.contains("</curriculum>"));
    }
}
