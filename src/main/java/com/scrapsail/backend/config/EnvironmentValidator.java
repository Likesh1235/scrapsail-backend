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

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${MYSQL_URL:}")
    private String mysqlUrl;

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @PostConstruct
    public void validateEnvironment() {
        logger.info("Validating environment variables...");

        boolean hasWarnings = false;
        StringBuilder warnings = new StringBuilder("\n⚠️  MISSING ENVIRONMENT VARIABLES (app will start but DB may not work):\n");

        // Check for Railway DATABASE_URL or MYSQL_URL
        boolean hasDatabaseUrl = (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.contains("${"));
        boolean hasMysqlUrl = (mysqlUrl != null && !mysqlUrl.isEmpty() && !mysqlUrl.contains("${"));
        boolean hasDatasourceUrl = (datasourceUrl != null && !datasourceUrl.isEmpty() 
                && !datasourceUrl.contains("${") && !datasourceUrl.contains("localhost:3306"));

        if (hasDatabaseUrl) {
            logger.info("✅ DATABASE_URL is set (Railway MySQL service detected)");
        } else if (hasMysqlUrl) {
            logger.info("✅ MYSQL_URL is set");
        } else if (hasDatasourceUrl) {
            logger.info("✅ Database URL is configured");
        } else {
            warnings.append("  - DATABASE_URL or MYSQL_URL is missing\n");
            warnings.append("    💡 Add MySQL service in Railway: + New → Database → Add MySQL\n");
            warnings.append("    Railway will automatically set DATABASE_URL\n");
            hasWarnings = true;
        }

        if (hasWarnings) {
            warnings.append("\n📝 Railway Setup Instructions:\n");
            warnings.append("  1. Go to Railway Dashboard → Your Project\n");
            warnings.append("  2. Click '+ New' → 'Database' → 'Add MySQL'\n");
            warnings.append("  3. Railway automatically sets DATABASE_URL\n");
            warnings.append("  4. Optionally set SPRING_PROFILES_ACTIVE=prod\n");
            warnings.append("\n⚠️  App will start but database connection will fail.\n");
            
            logger.warn(warnings.toString());
            // Don't throw exception - let app start so health checks can work
            logger.warn("Application will start but database features may not work.");
        } else {
            logger.info("✅ Database connection configured");
        }
    }
}

