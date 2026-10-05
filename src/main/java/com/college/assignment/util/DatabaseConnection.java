package com.college.assignment.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to manage JDBC Database Connections.
 * Reads configuration from db.properties or Environment Variables.
 */
public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static boolean initialized = false;

    static {
        init();
    }

    private static synchronized void init() {
        if (initialized) {
            return;
        }

        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                LOGGER.warning("db.properties not found in classpath. Using defaults.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error reading db.properties: " + e.getMessage(), e);
        }

        // Support environment variable overrides
        String envHost = System.getenv("DB_HOST");
        String envPort = System.getenv("DB_PORT");
        String envName = System.getenv("DB_NAME");
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASSWORD");
        String envUrl  = System.getenv("DB_URL");

        driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

        if (envUrl != null && !envUrl.trim().isEmpty()) {
            url = envUrl;
        } else if (envHost != null && envName != null) {
            String port = (envPort != null) ? envPort : "3306";
            url = "jdbc:mysql://" + envHost + ":" + port + "/" + envName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        } else {
            url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/assignment_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        }

        username = (envUser != null) ? envUser : props.getProperty("db.username", "root");
        password = (envPass != null) ? envPass : props.getProperty("db.password", "root");

        try {
            Class.forName(driver);
            LOGGER.info("MySQL JDBC Driver registered successfully: " + driver);
            initialized = true;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found: " + driver, e);
        }
    }

    /**
     * Gets a new database connection.
     * @return Connection instance
     * @throws SQLException if database connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            init();
        }
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to MySQL database at: " + url + " | Error: " + e.getMessage(), e);
            throw new SQLException("Unable to connect to the database. Please verify MySQL service and credentials.", e);
        }
    }

    /**
     * Closes resources safely.
     */
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
