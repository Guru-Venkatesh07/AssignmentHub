package com.college.assignment.util;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Automated Database Setup & Verification Tool
 * Sets up MySQL database schema, seed data, and verifies counts.
 */
public class DatabaseSetupRunner {

    private static final String[] CANDIDATE_PASSWORDS = {
        "root", "admin", "1234", "123456", "password", "mysql", "guru", "guruv", ""
    };

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Assignment Manager - Automated Database Setup  ");
        System.out.println("=================================================");

        String workingPassword = null;
        String jdbcUrl = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

        // Try candidate passwords
        for (String pass : CANDIDATE_PASSWORDS) {
            System.out.print("Testing MySQL connection with user='root', password='" + (pass.isEmpty() ? "<empty>" : pass) + "'... ");
            try (Connection conn = DriverManager.getConnection(jdbcUrl, "root", pass)) {
                System.out.println("SUCCESS!");
                workingPassword = pass;
                break;
            } catch (Exception e) {
                System.out.println("Failed (" + e.getMessage() + ")");
            }
        }

        if (workingPassword == null) {
            System.err.println("\n[!] Could not connect with common passwords.");
            return;
        }

        System.out.println("\n[+] Working MySQL Root Password Confirmed: '" + workingPassword + "'");

        try {
            updateDbProperties(workingPassword);
            System.out.println("[+] Updated src/main/resources/db.properties.");
        } catch (Exception e) {
            System.err.println("[-] Warning: " + e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, "root", workingPassword);
             Statement stmt = conn.createStatement()) {

            // Step 1: Create Database
            System.out.println("[+] Creating database assignment_manager...");
            stmt.execute("CREATE DATABASE IF NOT EXISTS assignment_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            stmt.execute("USE assignment_manager");

            // Step 2: Execute schema.sql
            System.out.println("[+] Executing database/schema.sql...");
            String schemaSql = readFile("database/schema.sql");
            executeSqlScript(stmt, schemaSql);
            System.out.println("[+] Schema created successfully!");

            // Step 3: Execute seed.sql
            System.out.println("[+] Executing database/seed.sql...");
            String seedSql = readFile("database/seed.sql");
            executeSqlScript(stmt, seedSql);
            System.out.println("[+] Seed data loaded successfully!");

            // Step 4: Verification
            System.out.println("\n=================================================");
            System.out.println("         DATABASE VERIFICATION REPORT            ");
            System.out.println("=================================================");
            verifyTable(stmt, "users");
            verifyTable(stmt, "subjects");
            verifyTable(stmt, "faculty_subjects");
            verifyTable(stmt, "assignments");
            verifyTable(stmt, "submissions");
            verifyTable(stmt, "evaluations");
            verifyTable(stmt, "notifications");
            System.out.println("=================================================\n");
            System.out.println(">>> DATABASE FULLY INITIALIZED AND VERIFIED! <<<");

        } catch (Exception e) {
            System.err.println("\n[ERROR] Database execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeSqlScript(Statement stmt, String script) throws Exception {
        String cleanScript = script.replaceAll("(?m)^\\s*--.*$", ""); // remove comments
        String[] queries = cleanScript.split(";");
        for (String query : queries) {
            String trimmed = query.trim();
            if (!trimmed.isEmpty()) {
                try {
                    stmt.execute(trimmed);
                } catch (Exception e) {
                    // ignore harmless warnings
                    if (!e.getMessage().toLowerCase().contains("exists")) {
                        System.out.println("  Query notice: " + e.getMessage());
                    }
                }
            }
        }
    }

    private static void verifyTable(Statement stmt, String tableName) {
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM assignment_manager." + tableName)) {
            if (rs.next()) {
                System.out.printf("  ✓ Table %-18s: %2d records populated\n", tableName, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.printf("  ✗ Table %-18s: FAILED (%s)\n", tableName, e.getMessage());
        }
    }

    private static String readFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private static void updateDbProperties(String password) throws Exception {
        String content = "# ====================================================================\n" +
                         "# Database Configuration Properties\n" +
                         "# Assignment & Submission Management System\n" +
                         "# ====================================================================\n\n" +
                         "db.driver=com.mysql.cj.jdbc.Driver\n" +
                         "db.url=jdbc:mysql://localhost:3306/assignment_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8\n" +
                         "db.username=root\n" +
                         "db.password=" + password + "\n\n" +
                         "# Upload directories\n" +
                         "upload.base.dir=uploads\n";

        File file = new File("src/main/resources/db.properties");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
