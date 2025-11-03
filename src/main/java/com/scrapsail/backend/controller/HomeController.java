package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "ScrapSail Backend API");
        response.put("version", "1.0.0");
        response.put("status", "✅ Running Successfully");
        response.put("message", "Welcome to ScrapSail Smart Waste Management System");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        endpoints.put("orders", "/api/orders");
        endpoints.put("wallet", "/api/wallet/{userId}");
        endpoints.put("leaderboard", "/api/leaderboard");
        endpoints.put("admin", "/api/admin");
        endpoints.put("otp", "/api/otp");
        
        response.put("availableEndpoints", endpoints);
        response.put("documentation", "Use these API endpoints to interact with the system");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "ScrapSail Backend is healthy and running!");
        return ResponseEntity.ok(response);
    }

    /**
     * Readiness probe endpoint - checks database connectivity.
     * Returns 200 if DB is accessible, 503 if not.
     * Used by Render for health checks.
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test database connection by counting users
            userRepository.count(); // Test DB connection
            response.put("status", "ready");
            response.put("database", "connected");
            response.put("timestamp", System.currentTimeMillis());
            response.put("message", "Backend is ready and database is accessible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "not ready");
            response.put("database", "disconnected");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * Test database connection and operations
     */
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test 1: Count users
            long userCount = userRepository.count();
            response.put("userCount", userCount);
            
            // Test 2: Get all users
            List<User> allUsers = userRepository.findAll();
            response.put("totalUsers", allUsers.size());
            
            // Test 3: Check for default users
            boolean adminExists = userRepository.existsByEmail("admin@scrapsail.com");
            boolean collectorExists = userRepository.existsByEmail("collector@scrapsail.com");
            
            response.put("adminExists", adminExists);
            response.put("collectorExists", collectorExists);
            
            // Test 4: Get user details
            if (adminExists) {
                User admin = userRepository.findByEmail("admin@scrapsail.com").orElse(null);
                if (admin != null) {
                    Map<String, Object> adminInfo = new HashMap<>();
                    adminInfo.put("id", admin.getId());
                    adminInfo.put("name", admin.getName());
                    adminInfo.put("email", admin.getEmail());
                    adminInfo.put("role", admin.getRole());
                    response.put("adminInfo", adminInfo);
                }
            }
            
            response.put("success", true);
            response.put("message", "Database connection is working!");
            response.put("status", userCount > 0 ? "✅ Data found in database" : "⚠️ Database is empty - check DataInitializer logs");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("message", "❌ Database connection failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
