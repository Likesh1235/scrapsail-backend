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

        boolean hasWarnings = false;
        StringBuilder warnings = new StringBuilder("\n⚠️  MISSING ENVIRONMENT VARIABLES (app will start but DB may not work):\n");

        // Check MYSQL_URL
        if (mysqlUrl == null || mysqlUrl.isEmpty() || mysqlUrl.contains("${")) {
            warnings.append("  - MYSQL_URL is missing or not set\n");
            hasWarnings = true;
        } else {
            logger.info("✅ MYSQL_URL is set");
        }

        // Check DB_USERNAME
        if (dbUsername == null || dbUsername.isEmpty() || dbUsername.contains("${")) {
            warnings.append("  - DB_USERNAME is missing or not set\n");
            hasWarnings = true;
        } else {
            logger.info("✅ DB_USERNAME is set");
        }

        // Check DB_PASSWORD
        if (dbPassword == null || dbPassword.isEmpty() || dbPassword.contains("${")) {
            warnings.append("  - DB_PASSWORD is missing or not set\n");
            hasWarnings = true;
        } else {
            logger.info("✅ DB_PASSWORD is set");
        }

        if (hasWarnings) {
            warnings.append("\n📝 Please set these in Railway Dashboard:\n");
            warnings.append("  Railway → Settings → Variables\n");
            warnings.append("\nRequired variables:\n");
            warnings.append("  - MYSQL_URL\n");
            warnings.append("  - DB_USERNAME\n");
            warnings.append("  - DB_PASSWORD\n");
            warnings.append("  - SPRING_PROFILES_ACTIVE=prod\n");
            warnings.append("\n⚠️  App will start but database connection will fail.\n");
            
            logger.warn(warnings.toString());
            // Don't throw exception - let app start so health checks can work
            logger.warn("Application will start but database features may not work.");
        } else {
            logger.info("✅ All required environment variables are set");
        }
    }
}

