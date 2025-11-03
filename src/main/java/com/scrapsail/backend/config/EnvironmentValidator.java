package com.scrapsail.backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Validates required environment variables on application startup.
 * Exits with clear error message if critical variables are missing.
 */
@Component
public class EnvironmentValidator {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentValidator.class);

    @Value("${spring.datasource.url:}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @PostConstruct
    public void validateEnvironment() {
        logger.info("Validating environment variables...");

        boolean hasErrors = false;
        StringBuilder errors = new StringBuilder("\n❌ MISSING REQUIRED ENVIRONMENT VARIABLES:\n");

        // Check MYSQL_URL
        if (mysqlUrl == null || mysqlUrl.isEmpty() || mysqlUrl.contains("${")) {
            errors.append("  - MYSQL_URL is missing or not set\n");
            hasErrors = true;
        } else {
            logger.info("✅ MYSQL_URL is set");
        }

        // Check DB_USERNAME
        if (dbUsername == null || dbUsername.isEmpty() || dbUsername.contains("${")) {
            errors.append("  - DB_USERNAME is missing or not set\n");
            hasErrors = true;
        } else {
            logger.info("✅ DB_USERNAME is set");
        }

        // Check DB_PASSWORD
        if (dbPassword == null || dbPassword.isEmpty() || dbPassword.contains("${")) {
            errors.append("  - DB_PASSWORD is missing or not set\n");
            hasErrors = true;
        } else {
            logger.info("✅ DB_PASSWORD is set");
        }

        if (hasErrors) {
            errors.append("\n📝 Please set these in Render Dashboard:\n");
            errors.append("  Render → Settings → Environment Variables\n");
            errors.append("\nRequired variables:\n");
            errors.append("  - MYSQL_URL\n");
            errors.append("  - DB_USERNAME\n");
            errors.append("  - DB_PASSWORD\n");
            errors.append("  - SPRING_PROFILES_ACTIVE=prod\n");
            errors.append("  - PORT=8080\n");
            
            logger.error(errors.toString());
            throw new IllegalStateException("Application cannot start: missing required environment variables. " +
                    "Check logs above for details.");
        }

        logger.info("✅ All required environment variables are set");
    }
}

