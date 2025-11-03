package com.scrapsail.backend.util;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component  // ENABLED: Auto-creates admin and collector on startup
public class DataInitializer implements CommandLineRunner {

    @Autowired(required = false)
    private UserRepository userRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private WalletRepository walletRepository;

    @Override
    public void run(String... args) throws Exception {
        // Don't block startup if repositories are not available (e.g., DB not connected)
        if (userRepository == null || passwordEncoder == null || walletRepository == null) {
            System.out.println("⚠️ DataInitializer: Skipping initialization - DB not available");
            return; // Skip initialization if DB not available
        }
        
        System.out.println("🔄 DataInitializer: Starting user initialization...");
        
        try {
            // Create default admin account if it doesn't exist
            if (!userRepository.existsByEmail("admin@scrapsail.com")) {
                System.out.println("📝 Creating admin user...");
                User admin = new User();
                admin.setName("System Administrator");
                admin.setEmail("admin@scrapsail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.admin);
                admin.setTotalPoints(0);
                admin = userRepository.saveAndFlush(admin);
                
                // Initialize wallet for admin
                initializeWallet(admin);
                userRepository.flush();
                
                System.out.println("✅ Default admin account created: admin@scrapsail.com / admin123 (ID: " + admin.getId() + ")");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }

            // Create default collector account if it doesn't exist
            if (!userRepository.existsByEmail("collector@scrapsail.com")) {
                System.out.println("📝 Creating collector user...");
                User collector = new User();
                collector.setName("John Collector");
                collector.setEmail("collector@scrapsail.com");
                collector.setPassword(passwordEncoder.encode("collector123"));
                collector.setRole(Role.collector);
                collector.setTotalPoints(0);
                collector = userRepository.saveAndFlush(collector);
                
                // Initialize wallet for collector
                initializeWallet(collector);
                userRepository.flush();
                
                System.out.println("✅ Default collector account created: collector@scrapsail.com / collector123 (ID: " + collector.getId() + ")");
            } else {
                System.out.println("ℹ️ Collector user already exists");
            }

            // Create a sample user account if it doesn't exist
            if (!userRepository.existsByEmail("user@scrapsail.com")) {
                System.out.println("📝 Creating default user...");
                User user = new User();
                user.setName("Jane User");
                user.setEmail("user@scrapsail.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole(Role.user);
                user.setTotalPoints(0);
                user = userRepository.saveAndFlush(user);

                // Initialize wallet for user
                initializeWallet(user);
                userRepository.flush();

                System.out.println("✅ Default user account created: user@scrapsail.com / user123 (ID: " + user.getId() + ")");
            } else {
                System.out.println("ℹ️ Default user already exists");
            }
            
            // Verify users were created
            long totalUsers = userRepository.count();
            System.out.println("📊 Total users in database: " + totalUsers);
            
        } catch (Exception e) {
            System.err.println("⚠️ WARNING in DataInitializer: " + e.getMessage());
            e.printStackTrace();
            // Don't throw - just log the error so app can start
            // This allows health checks to work even if DB initialization fails
        }
    }

    private void initializeWallet(User user) {
        try {
            // Check if wallet already exists
            if (walletRepository.findByUserId(user.getId()).isEmpty()) {
                // Initialize CarbonWallet with 0 points and balance
                CarbonWallet wallet = new CarbonWallet();
                wallet.setUser(user);
                wallet.setPoints(0.0);
                wallet.setBalance(0.0);
                walletRepository.saveAndFlush(wallet);
                System.out.println("  ✅ Wallet created for user: " + user.getEmail());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize wallet for user: " + user.getEmail() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
