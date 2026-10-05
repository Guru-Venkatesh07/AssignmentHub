package com.college.assignment.util;

import com.college.assignment.model.Assignment;
import com.college.assignment.model.Subject;

import java.util.List;

/**
 * Utility for producing well-formed XML responses.
 * Specifically demonstrates XML-based data exchange for the Web Technology Lab.
 */
public class XmlUtil {

    /**
     * Escapes XML special characters to prevent malformed XML structures.
     */
    public static String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Converts a list of Assignment objects into well-formed XML.
     */
    public static String toAssignmentsXml(List<Assignment> assignments) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<assignments total=\"").append(assignments != null ? assignments.size() : 0).append("\">\n");

        if (assignments != null) {
            for (Assignment a : assignments) {
                xml.append("    <assignment id=\"").append(a.getId()).append("\">\n");
                xml.append("        <title>").append(escapeXml(a.getTitle())).append("</title>\n");
                xml.append("        <subjectCode>").append(escapeXml(a.getSubjectCode())).append("</subjectCode>\n");
                xml.append("        <subjectName>").append(escapeXml(a.getSubjectName())).append("</subjectName>\n");
                xml.append("        <facultyName>").append(escapeXml(a.getFacultyName())).append("</facultyName>\n");
                xml.append("        <description>").append(escapeXml(a.getDescription())).append("</description>\n");
                xml.append("        <dueDate>").append(a.getDueDate() != null ? a.getDueDate().toString() : "").append("</dueDate>\n");
                xml.append("        <dueTime>").append(a.getDueTime() != null ? a.getDueTime().toString() : "").append("</dueTime>\n");
                xml.append("        <maxMarks>").append(a.getMaxMarks()).append("</maxMarks>\n");
                xml.append("        <status>").append(a.getStatus() != null ? a.getStatus().name() : "ACTIVE").append("</status>\n");
                xml.append("    </assignment>\n");
            }
        }

        xml.append("</assignments>");
        return xml.toString();
    }

    /**
     * Converts a list of Subject objects into academic timetable/course XML.
     */
    public static String toTimetableXml(List<Subject> subjects) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<curriculum totalCourses=\"").append(subjects != null ? subjects.size() : 0).append("\">\n");

        if (subjects != null) {
            for (Subject s : subjects) {
                xml.append("    <subject id=\"").append(s.getId()).append("\">\n");
                xml.append("        <code>").append(escapeXml(s.getSubjectCode())).append("</code>\n");
                xml.append("        <name>").append(escapeXml(s.getSubjectName())).append("</name>\n");
                xml.append("        <semester>").append(s.getSemester()).append("</semester>\n");
                xml.append("        <department>").append(escapeXml(s.getDepartment())).append("</department>\n");
                xml.append("    </subject>\n");
            }
        }

        xml.append("</curriculum>");
        return xml.toString();
    }
}
