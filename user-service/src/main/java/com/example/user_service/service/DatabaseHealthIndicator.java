package com.example.user_service.service;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean isDBUp = checkDBConnection();

        if (isDBUp) {
            return Health.up()
                    .withDetail("Database", "Available")
                    .build();
        } else {
            return Health.down()
                    .withDetail("Database", "Not reachable")
                    .build();
        }
    }

    // Dummy method for demonstration, replace with actual DB check
    private boolean checkDBConnection() {
        try {
            // Example: perform a simple DB query or connection test
            return true; // Assume DB is up
        } catch (Exception e) {
            return false;
        }
    }
}
