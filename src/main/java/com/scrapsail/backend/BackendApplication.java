package com.scrapsail.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);
    private final Environment environment;

    public BackendApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = environment.getProperty("server.port", "8080");
        String profile = environment.getProperty("spring.profiles.active", "default");
        String railwayUrl = environment.getProperty("RAILWAY_PUBLIC_DOMAIN", "");
        
        logger.info("╔════════════════════════════════════════════════════════════╗");
        logger.info("║  🚀 ScrapSail Backend Started Successfully                ║");
        logger.info("╠════════════════════════════════════════════════════════════╣");
        logger.info("║  Server: Running on port {}                                ║", port);
        logger.info("║  Profile: {}                                               ║", profile);
        if (!railwayUrl.isEmpty()) {
            logger.info("║  URL:     https://{}                                ║", railwayUrl);
        }
        logger.info("║  Health:  /health                                          ║");
        logger.info("║  Ready:   /ready                                           ║");
        logger.info("║  Root:   /                                                ║");
        logger.info("╚════════════════════════════════════════════════════════════╝");
        logger.info("✅ Application is ready to accept requests");
    }
}
