package com.example.user_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class DummyService {

    @CircuitBreaker(name = "dummyService", fallbackMethod = "dummyFallback")
    public void call(String username) {
        // Always fail to simulate external service down
        throw new RuntimeException("External service down!");
    }

    // Fallback method: must have same args + Throwable
    public void dummyFallback(String username, Throwable t) {
        System.out.println("✅ Fallback triggered for " + username);
        System.out.println("Reason: " + t.getMessage());
    }
}
