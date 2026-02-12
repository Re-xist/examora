package com.examora.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Database Utility Class - Manages database connections
 * Supports environment variables for Docker deployment
 */
public class DBUtil {
    private static String url;
    private static String username;
    private static String password;
    private static boolean initialized = false;

    // Database configuration defaults
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/examora_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";

    static {
        initialize();
    }

    private static void initialize() {
        if (initialized) return;

        try {
            // Priority 1: Environment variables (for Docker)
            String envUrl = System.getenv("DB_URL");
            String envUsername = System.getenv("DB_USERNAME");
            String envPassword = System.getenv("DB_PASSWORD");

            if (envUrl != null && !envUrl.isEmpty()) {
                url = envUrl;
                username = envUsername != null ? envUsername : DEFAULT_USERNAME;
                password = envPassword != null ? envPassword : DEFAULT_PASSWORD;
                System.out.println("DBUtil: Using environment variables for DB configuration");
            } else {
                // Priority 2: Properties file
                Properties props = new Properties();
                InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");

                if (is != null) {
                    props.load(is);
                    url = props.getProperty("db.url", DEFAULT_URL);
                    username = props.getProperty("db.username", DEFAULT_USERNAME);
                    password = props.getProperty("db.password", DEFAULT_PASSWORD);
                    is.close();
                    System.out.println("DBUtil: Using db.properties for DB configuration");
                } else {
                    // Priority 3: Default configuration
                    url = DEFAULT_URL;
                    username = DEFAULT_USERNAME;
                    password = DEFAULT_PASSWORD;
                    System.out.println("DBUtil: Using default DB configuration");
                }
            }

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            initialized = true;

            System.out.println("DBUtil initialized successfully. URL: " + url);

        } catch (Exception e) {
            System.err.println("DBUtil initialization error: " + e.getMessage());
            e.printStackTrace();
            // Use defaults on error
            url = DEFAULT_URL;
            username = DEFAULT_USERNAME;
            password = DEFAULT_PASSWORD;
        }
    }

    /**
     * Set database configuration programmatically
     */
    public static void configure(String dbUrl, String dbUsername, String dbPassword) {
        url = dbUrl;
        username = dbUsername;
        password = dbPassword;
        initialized = true;
    }

    /**
     * Get a database connection
     */
    public static Connection getConnection() throws SQLException {
        initialize();
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Close a database connection safely
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(5);
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
