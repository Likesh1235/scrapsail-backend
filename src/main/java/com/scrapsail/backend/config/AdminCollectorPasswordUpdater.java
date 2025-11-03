package com.scrapsail.backend.config;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

// Disabled - DataInitializer handles user creation
// This class is intentionally disabled to prevent duplicate user initialization
public class AdminCollectorPasswordUpdater implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Update admin password to admin123
        Optional<User> adminOpt = userRepository.findByEmail("admin@scrapsail.com");
        if (adminOpt.isPresent()) {
            User admin = adminOpt.get();
            // Always reset to ensure correct password
            String hashedAdminPassword = passwordEncoder.encode("admin123");
            admin.setPassword(hashedAdminPassword);
            admin.setRole(Role.admin);
            userRepository.save(admin);
            System.out.println("✅ Admin password updated to admin123");
        } else {
            // Create admin if doesn't exist
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@scrapsail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.admin);
            admin.setPhone("0000000000");
            userRepository.save(admin);
            System.out.println("✅ Admin user created with password admin123");
        }

        // Update collector password to collector123
        Optional<User> collectorOpt = userRepository.findByEmail("collector@scrapsail.com");
        if (collectorOpt.isPresent()) {
            User collector = collectorOpt.get();
            // Always reset to ensure correct password
            String hashedCollectorPassword = passwordEncoder.encode("collector123");
            collector.setPassword(hashedCollectorPassword);
            collector.setRole(Role.collector);
            userRepository.save(collector);
            System.out.println("✅ Collector password updated to collector123");
        } else {
            // Create collector if doesn't exist
            User collector = new User();
            collector.setName("Collector User");
            collector.setEmail("collector@scrapsail.com");
            collector.setPassword(passwordEncoder.encode("collector123"));
            collector.setRole(Role.collector);
            collector.setPhone("0000000000");
            userRepository.save(collector);
            System.out.println("✅ Collector user created with password collector123");
        }
    }
}

