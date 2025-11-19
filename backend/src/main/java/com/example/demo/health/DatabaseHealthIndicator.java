package com.example.demo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator for database connectivity.
 * Provides detailed health information about the PostgreSQL database connection.
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            // Execute a simple query to check database connectivity
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            if (result != null && result == 1) {
                // Get additional database information
                String version = jdbcTemplate.queryForObject(
                    "SELECT version()",
                    String.class
                );

                Long userCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM users",
                    Long.class
                );

                Long taskCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM tasks",
                    Long.class
                );

                return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("version", version)
                    .withDetail("userCount", userCount)
                    .withDetail("taskCount", taskCount)
                    .withDetail("status", "Connection successful")
                    .build();
            } else {
                return Health.down()
                    .withDetail("error", "Database query returned unexpected result")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .withException(e)
                .build();
        }
    }
}
