package com.scrapsail.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${FRONTEND_URL:}")
    private String frontendUrl;

    // Get allowed origins from environment or use defaults
    private List<String> getAllowedOrigins() {
        List<String> origins = new ArrayList<>();
        
        // Add FRONTEND_URL from environment if set
        if (frontendUrl != null && !frontendUrl.isEmpty()) {
            origins.add(frontendUrl);
        }
        
        // Add local development origins
        origins.addAll(Arrays.asList(
            "http://localhost:3000",
            "https://localhost:3000",
            "http://localhost:3001",
            "https://localhost:3001",
            "http://localhost:3003",
            "https://localhost:3003",
            "http://localhost:5173",
            "https://localhost:5173",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3001",
            "http://127.0.0.1:3003",
            "http://127.0.0.1:5173"
        ));
        
        // Add production domains if FRONTEND_URL not set
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            origins.addAll(Arrays.asList(
                "https://scrapsail-frontend.vercel.app",
                "https://scrapsail-frontend-git-main-likesh1235s-projects.vercel.app"
            ));
        }
        
        return origins;
    }

    // Global CORS configuration for Spring MVC - applies to ALL endpoints
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            @SuppressWarnings("null")
            public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
                List<String> allowedOrigins = getAllowedOrigins();
                String[] originsArray = allowedOrigins.toArray(new String[0]);
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
        configuration.setAllowedOrigins(getAllowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "X-Request-ID"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
