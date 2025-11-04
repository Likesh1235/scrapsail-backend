package com.scrapsail.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Railway Database Configuration
 * Converts Railway's DATABASE_URL (mysql://...) to JDBC format (jdbc:mysql://...)
 * Railway automatically provides DATABASE_URL when you add a MySQL service
 */
@Configuration
public class RailwayDatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${MYSQL_URL:}")
    private String mysqlUrl;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        // Priority: MYSQL_URL > DATABASE_URL > default from application.properties
        String jdbcUrl = null;
        
        if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
            // If MYSQL_URL is set, use it (convert if needed)
            jdbcUrl = convertToJdbcUrl(mysqlUrl);
        } else if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Railway provides DATABASE_URL automatically - convert to JDBC format
            jdbcUrl = convertToJdbcUrl(databaseUrl);
        }
        
        if (jdbcUrl != null) {
            properties.setUrl(jdbcUrl);
        }
        // Otherwise, use default from application.properties
        
        return properties;
    }

    /**
     * Converts mysql:// URL to jdbc:mysql:// format
     * Handles Railway DATABASE_URL format: mysql://user:password@host:port/database
     * Converts to: jdbc:mysql://host:port/database?user=user&password=password
     */
    private String convertToJdbcUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }

        // If already in JDBC format, return as-is
        if (url.startsWith("jdbc:mysql://") || url.startsWith("jdbc:")) {
            return url;
        }

        // If starts with mysql://, convert to JDBC format
        if (url.startsWith("mysql://")) {
            try {
                URI uri = new URI(url);
                
                String host = uri.getHost();
                String port = uri.getPort() != -1 ? ":" + uri.getPort() : "";
                String path = uri.getPath();
                if (path != null && path.startsWith("/")) {
                    path = path.substring(1);
                }
                
                String query = uri.getQuery();
                String userInfo = uri.getUserInfo();
                
                // Build JDBC URL
                StringBuilder jdbcUrl = new StringBuilder("jdbc:mysql://");
                jdbcUrl.append(host).append(port);
                
                if (path != null && !path.isEmpty()) {
                    jdbcUrl.append("/").append(path);
                }
                
                // Parse user and password from userInfo (format: user:password)
                String user = null;
                String password = null;
                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    user = parts[0];
                    password = parts.length > 1 ? parts[1] : null;
                } else if (userInfo != null) {
                    user = userInfo;
                }
                
                // Build query string
                StringBuilder queryString = new StringBuilder();
                if (query != null && !query.isEmpty()) {
                    queryString.append(query);
                }
                
                // Add user and password as query params
                if (user != null) {
                    if (queryString.length() > 0) {
                        queryString.append("&");
                    }
                    queryString.append("user=").append(user);
                }
                
                if (password != null) {
                    if (queryString.length() > 0) {
                        queryString.append("&");
                    }
                    queryString.append("password=").append(password);
                }
                
                if (queryString.length() > 0) {
                    jdbcUrl.append("?").append(queryString);
                }
                
                return jdbcUrl.toString();
            } catch (URISyntaxException e) {
                // If parsing fails, try to return as JDBC format anyway
                return url.startsWith("mysql://") ? url.replace("mysql://", "jdbc:mysql://") : url;
            }
        }

        // Return as-is if format is unknown
        return url;
    }
}

