package com.scrapsail.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Lazy DataSource configuration that doesn't validate connection on startup.
 * This allows the app to start even if database is not available.
 */
@Configuration
public class LazyDataSourceConfig {

    // DataSourceProperties is provided by RailwayDatabaseConfig
    // No need to create it here - RailwayDatabaseConfig handles URL conversion

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());
        
        // CRITICAL: Don't validate connection on startup
        config.setConnectionTestQuery(null); // Don't test on startup
        config.setInitializationFailTimeout(-1); // Don't fail on startup
        config.setMinimumIdle(0); // Don't create connections on startup
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(5);
        
        // Return DataSource - connections will be created on first use
        return new HikariDataSource(config);
    }
}



