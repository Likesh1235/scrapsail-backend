package com.scrapsail.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    // Comprehensive list of allowed origins - covers all common development ports and production domains
    public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        // Local Development
        "http://localhost:3000",
        "https://localhost:3000",
        "http://localhost:3001",
        "https://localhost:3001",
        "http://localhost:3003",
        "https://localhost:3003",
        "http://localhost:3006",
        "https://localhost:3006",
        "http://localhost:5173",
        "https://localhost:5173",
        "http://127.0.0.1:3000",
        "http://127.0.0.1:3001",
        "http://127.0.0.1:3003",
        "http://127.0.0.1:3006",
        "http://127.0.0.1:5173",
        // Production - Vercel Domains (add your exact Vercel URL after deployment)
        "https://scrapsail-frontend.vercel.app",
        "https://scrapsail-frontend-git-main-likesh1235s-projects.vercel.app"
        // Note: After deployment, add your exact Vercel URL here if different
    );

    // Global CORS configuration for Spring MVC - applies to ALL endpoints
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            @SuppressWarnings("null")
            public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
                String[] originsArray = ALLOWED_ORIGINS.toArray(new String[0]);
                registry.addMapping("/**")
                        .allowedOrigins(originsArray)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    // CORS configuration for Spring Security - MUST match MVC configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
