package com.scrapsail.backend.controller;

import com.scrapsail.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class OtpController {

    @Autowired
    private EmailService emailService;

    // Store OTPs temporarily (in production, use Redis or database)
    private Map<String, String> otpStorage = new HashMap<>();

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Send OTP email
            String otp = emailService.sendOtpEmail(email);
            
            // Store OTP with email as key
            otpStorage.put(email, otp);
            
            response.put("success", true);
            response.put("message", "✅ OTP sent successfully to " + email + ". Please check your email.");
            // For development: include OTP in response (remove in production)
            response.put("otp", otp);
            response.put("note", "Development Mode: OTP shown here. In production, check your email.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sending OTP: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            String otp = request.get("otp");
            
            if (email == null || otp == null) {
                response.put("success", false);
                response.put("message", "Email and OTP are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            String storedOtp = otpStorage.get(email);
            
            if (storedOtp == null) {
                response.put("success", false);
                response.put("message", "OTP not found or expired");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (storedOtp.equals(otp)) {
                // Remove OTP after successful verification
                otpStorage.remove(email);
                response.put("success", true);
                response.put("message", "OTP verified successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid OTP");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error verifying OTP: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
