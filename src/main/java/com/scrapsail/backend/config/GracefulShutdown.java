package com.scrapsail.backend.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Handles graceful shutdown on SIGTERM.
 * Closes database connections cleanly when Render stops the application.
 */
@Component
public class GracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        logger.info("🛑 Shutdown signal received. Starting graceful shutdown...");

        if (dataSource != null && dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            
            logger.info("Closing database connection pool...");
            try {
                // Close all active connections
                hikariDataSource.close();
                logger.info("✅ Database connection pool closed successfully");
            } catch (Exception e) {
                logger.error("❌ Error closing database connections: {}", e.getMessage());
            }
        }

        logger.info("✅ Graceful shutdown completed");
    }
}

