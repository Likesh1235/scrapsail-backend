package com.scrapsail.backend.service;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.CarbonCredit;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.WalletRepository;
import com.scrapsail.backend.repository.CarbonCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CarbonCreditRepository carbonCreditRepository;

    public User register(User user) {
        // Prevent registration with reserved admin/collector emails
        String email = user.getEmail().toLowerCase();
        if (email.equals("admin@scrapsail.com") || 
            email.equals("collector@scrapsail.com")) {
            throw new RuntimeException("Cannot register with this email address. Please use a different email.");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default values - Force USER role for registration
        user.setRole(Role.user);
        
        // Save user to database (flush immediately to ensure it's saved)
        User savedUser = userRepository.saveAndFlush(user);
        
        System.out.println("✅ User saved to database: " + savedUser.getEmail() + " (ID: " + savedUser.getId() + ")");
        
        // Automatically create wallet and credits for new user
        initializeWalletAndCredits(savedUser);
        
        // Flush again to ensure wallet and credits are saved
        userRepository.flush();
        
        return savedUser;
    }

    /**
     * Initialize wallet and credits for a newly registered user
     */
    private void initializeWalletAndCredits(User user) {
        try {
            // Check if wallet already exists to avoid duplicates
            if (walletRepository.findByUserId(user.getId()).isEmpty()) {
                // Create CarbonWallet with 0 points and balance
                CarbonWallet wallet = new CarbonWallet();
                wallet.setUser(user);
                wallet.setPoints(0.0);
                wallet.setBalance(0.0);
                walletRepository.saveAndFlush(wallet);
                System.out.println("  ✅ Wallet created for user: " + user.getEmail());
            }
            
            // Check if credit already exists to avoid duplicates
            if (carbonCreditRepository.findByUserId(user.getId()).isEmpty()) {
                // Create CarbonCredit with 0 earned/redeemed/balance
                CarbonCredit credit = new CarbonCredit();
                credit.setUser(user);
                credit.setEarned(0.0);
                credit.setRedeemed(0.0);
                credit.setBalance(0.0);
                carbonCreditRepository.saveAndFlush(credit);
                System.out.println("  ✅ Carbon credits created for user: " + user.getEmail());
            }
            
            System.out.println("✅ Wallet and credits initialized for user: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize wallet/credits for user: " + user.getEmail() + " - " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception - user registration should succeed even if wallet init fails
        }
    }

    // User login - blocks ADMIN and COLLECTOR
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User foundUser = user.get();
            
            // Prevent ADMIN and COLLECTOR from logging in through user login
            // They should use their dedicated login pages
            if (foundUser.getRole() == Role.admin || foundUser.getRole() == Role.collector) {
                return Optional.empty();
            }
            
            // Regular USER login only
            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                return user;
            }
        }
        return Optional.empty();
    }
    
    // Admin/Collector login - only allows specific roles
    public Optional<User> adminLogin(String email, String password, Role expectedRole) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User foundUser = user.get();
            
            // Check if role matches expected role
            if (foundUser.getRole() == expectedRole) {
                // Validate password
                if (passwordEncoder.matches(password, foundUser.getPassword())) {
                    return user;
                }
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
