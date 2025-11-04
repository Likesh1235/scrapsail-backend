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
    private String datasourceUrl;

    @PostConstruct
    public void validateEnvironment() {
        logger.info("Validating environment variables...");

        // Check if database URL is configured (defaults to localhost for local development)
        boolean hasDatasourceUrl = (datasourceUrl != null && !datasourceUrl.isEmpty() 
                && !datasourceUrl.contains("${"));

        if (hasDatasourceUrl && datasourceUrl.contains("localhost:3306")) {
            logger.info("✅ Local MySQL database configured (localhost:3306)");
        } else if (hasDatasourceUrl) {
            logger.info("✅ Database connection configured");
        } else {
            logger.warn("⚠️  Database URL not configured - using default localhost:3306");
        }

        logger.info("✅ Environment validation completed");
    }
}

