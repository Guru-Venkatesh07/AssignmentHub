package com.college.assignment.listener;

import com.college.assignment.util.DatabaseConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Lifecycle Listener that automatically validates and initializes
 * the database schema and seed data on web application startup.
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("=================================================");
        LOGGER.info("Initializing Assignment & Submission Manager Webapp");
        LOGGER.info("=================================================");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            LOGGER.info("Connected to database successfully. Checking tables...");

            boolean tablesExist = false;
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    LOGGER.info("Database already initialized with " + count + " users.");
                    tablesExist = (count > 0);
                }
            } catch (Exception ignored) {
                LOGGER.info("Users table not found or empty. Running auto-initialization...");
            }

            if (!tablesExist) {
                // Execute schema.sql from classpath or fallback
                runSqlResource(stmt, "/schema.sql");
                runSqlResource(stmt, "/seed.sql");
                LOGGER.info("Schema and seed data applied successfully!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Database auto-initialization check skipped/deferred: " + e.getMessage(), e);
        }
    }

    private void runSqlResource(Statement stmt, String resourcePath) {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                LOGGER.fine("Resource " + resourcePath + " not found in classpath root.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sql.append(line).append("\n");
                    if (trimmed.endsWith(";")) {
                        String query = sql.toString().trim();
                        if (query.endsWith(";")) {
                            query = query.substring(0, query.length() - 1);
                        }
                        if (!query.isEmpty() && !query.toUpperCase().startsWith("CREATE DATABASE") && !query.toUpperCase().startsWith("USE ")) {
                            try {
                                stmt.execute(query);
                            } catch (Exception ex) {
                                if (!ex.getMessage().toLowerCase().contains("exists") &&
                                    !ex.getMessage().toLowerCase().contains("duplicate")) {
                                    LOGGER.warning("SQL execution notice: " + ex.getMessage());
                                }
                            }
                        }
                        sql.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error executing SQL resource " + resourcePath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Assignment & Submission Manager Webapp context destroyed.");
    }
}
