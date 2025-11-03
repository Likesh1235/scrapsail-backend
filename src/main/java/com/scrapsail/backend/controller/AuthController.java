package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.dto.LoginRequest;
import com.scrapsail.backend.service.UserService;
import com.scrapsail.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email == null || password == null) {
                response.put("success", false);
                response.put("message", "Email and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // STRICT ROLE SEPARATION: Reject admin and collector emails
            // They must use their dedicated login endpoints
            if (email.equalsIgnoreCase("admin@scrapsail.com")) {
                response.put("success", false);
                response.put("message", "Admin must login using the admin login page");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            if (email.equalsIgnoreCase("collector@scrapsail.com")) {
                response.put("success", false);
                response.put("message", "Collector must login using the collector login page");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Regular user login ONLY - UserService.login already blocks admin/collector
            Optional<User> userOpt = userService.login(email, password);
            
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOpt.get();
            
            // Double-check role is USER (should already be enforced by UserService)
            if (user.getRole() != com.scrapsail.backend.model.Role.user) {
                response.put("success", false);
                response.put("message", "Invalid login endpoint for this role. Please use the correct login page.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Generate JWT token
            String token = jwtService.generateToken(user.getEmail(), user.getRole().toString());
            
            // Create user map with null checks
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole().toString());
            userMap.put("phone", user.getPhone() != null ? user.getPhone() : "");
            userMap.put("address", user.getAddress() != null ? user.getAddress() : "");
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", userMap);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            response.put("error", e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Ensure role is set to USER for registration (ignore any role from request)
            user.setRole(com.scrapsail.backend.model.Role.user);
            User registeredUser = userService.register(user);
            response.put("success", true);
            response.put("message", "User registered successfully");
            
            // Return user details
            Map<String, Object> registeredUserMap = new HashMap<>();
            registeredUserMap.put("id", registeredUser.getId());
            registeredUserMap.put("name", registeredUser.getName());
            registeredUserMap.put("email", registeredUser.getEmail());
            registeredUserMap.put("role", registeredUser.getRole().toString());
            registeredUserMap.put("phone", registeredUser.getPhone() != null ? registeredUser.getPhone() : "");
            registeredUserMap.put("address", registeredUser.getAddress() != null ? registeredUser.getAddress() : "");
            response.put("user", registeredUserMap);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email == null || password == null) {
                response.put("success", false);
                response.put("message", "Email and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // STRICT: Only allow admin role login via this endpoint
            Optional<User> userOpt = userService.adminLogin(email, password, com.scrapsail.backend.model.Role.admin);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid email or password for admin account");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOpt.get();
            
            // Double-check role is ADMIN (should already be enforced by UserService)
            if (user.getRole() != com.scrapsail.backend.model.Role.admin) {
                response.put("success", false);
                response.put("message", "This account is not an admin. Please use the correct login page.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Generate JWT token
            String token = jwtService.generateToken(user.getEmail(), user.getRole().toString());
            
            // Create user map with null checks
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole().toString());
            userMap.put("phone", user.getPhone() != null ? user.getPhone() : "");
            userMap.put("address", user.getAddress() != null ? user.getAddress() : "");
            
            response.put("success", true);
            response.put("message", "Admin login successful");
            response.put("token", token);
            response.put("user", userMap);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            response.put("error", e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/collector-login")
    public ResponseEntity<Map<String, Object>> collectorLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email == null || password == null) {
                response.put("success", false);
                response.put("message", "Email and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // STRICT: Only allow collector role login via this endpoint
            Optional<User> userOpt = userService.adminLogin(email, password, com.scrapsail.backend.model.Role.collector);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid email or password for collector account");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            User user = userOpt.get();
            
            // Double-check role is COLLECTOR (should already be enforced by UserService)
            if (user.getRole() != com.scrapsail.backend.model.Role.collector) {
                response.put("success", false);
                response.put("message", "This account is not a collector. Please use the correct login page.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Generate JWT token
            String token = jwtService.generateToken(user.getEmail(), user.getRole().toString());
            
            // Create user map with null checks
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole().toString());
            userMap.put("phone", user.getPhone() != null ? user.getPhone() : "");
            userMap.put("address", user.getAddress() != null ? user.getAddress() : "");
            
            response.put("success", true);
            response.put("message", "Collector login successful");
            response.put("token", token);
            response.put("user", userMap);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            response.put("error", e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Auth controller is working!");
        response.put("userServiceExists", userService != null);
        response.put("jwtServiceExists", jwtService != null);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test-login")
    public ResponseEntity<Map<String, Object>> testLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = loginRequest.getEmail();
            response.put("emailReceived", email);
            response.put("step", "1 - received email");
            
            // Just find user without password check
            Optional<User> userOpt = userService.findByEmail(email);
            response.put("step", "2 - found user: " + userOpt.isPresent());
            
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            response.put("step", "3 - got user object");
            response.put("userId", user.getId());
            response.put("step", "4 - got id");
            response.put("userName", user.getName());
            response.put("step", "5 - got name");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("stackTrace", e.getStackTrace()[0].toString());
            return ResponseEntity.ok(response); // Return 200 even on error for debugging
        }
    }
}

