package com.scrapsail.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Adds security headers to all HTTP responses.
 * Similar to Helmet.js for Node.js, but for Spring Boot.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Prevent clickjacking
        response.setHeader("X-Frame-Options", "DENY");
        
        // Prevent MIME type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // Enable XSS protection
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Referrer policy
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Content Security Policy (restrictive for API, adjust if needed)
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; frame-ancestors 'none';");
        
        // Permissions Policy
        response.setHeader("Permissions-Policy", 
            "geolocation=(), microphone=(), camera=()");

        filterChain.doFilter(request, response);
    }
}

