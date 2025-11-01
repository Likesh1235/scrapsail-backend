package com.scrapsail.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scrapsail.backend.model.CarbonCredit;
import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.repository.CarbonCreditRepository;
import com.scrapsail.backend.repository.WalletRepository;
import com.scrapsail.backend.repository.UserRepository;

@Service
@Transactional
public class CreditService {

    @Autowired
    private CarbonCreditRepository carbonCreditRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    /**
     * Earn credits for a user and update both CarbonCredit, CarbonWallet, and User totalPoints.
     * Assumption: amount is the credit points to add (e.g. 1 point per kg). Adjust as needed.
     */
    public CarbonCredit earnCredits(Long userId, Double amount) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update CarbonCredit
        List<CarbonCredit> existingCredits = carbonCreditRepository.findByUserId(userId);
        CarbonCredit carbonCredit;

        if (existingCredits.isEmpty()) {
            carbonCredit = new CarbonCredit();
            carbonCredit.setUser(user);
            carbonCredit.setEarned(amount);
            carbonCredit.setRedeemed(0.0);
            carbonCredit.setBalance(amount);
        } else {
            carbonCredit = existingCredits.get(0);
            carbonCredit.setEarned(carbonCredit.getEarned() + amount);
            carbonCredit.setBalance(carbonCredit.getBalance() + amount);
        }

        CarbonCredit saved = carbonCreditRepository.save(carbonCredit);

        // Update CarbonWallet in sync
        Optional<CarbonWallet> walletOpt = walletRepository.findByUserId(userId);
        CarbonWallet wallet;
        if (walletOpt.isEmpty()) {
            wallet = new CarbonWallet();
            wallet.setUser(user);
            wallet.setPoints(amount);
            wallet.setBalance(amount);
        } else {
            wallet = walletOpt.get();
            wallet.setPoints(wallet.getPoints() + amount);
            wallet.setBalance(wallet.getBalance() + amount);
        }
        walletRepository.save(wallet);

        // Update User totalPoints for leaderboard
        Integer currentPoints = user.getTotalPoints() != null ? user.getTotalPoints() : 0;
        int pointsToAdd = (int) Math.round(amount);
        user.setTotalPoints(currentPoints + pointsToAdd);
        
        // Update totalRecycled based on weight (assuming amount represents kg)
        Double currentRecycled = user.getTotalRecycled() != null ? user.getTotalRecycled() : 0.0;
        user.setTotalRecycled(currentRecycled + amount);
        
        userRepository.save(user);

        // Update leaderboard table
        leaderboardService.updateLeaderboard(userId);

        return saved;
    }

    /**
     * Redeem credits for a user; update CarbonCredit and CarbonWallet accordingly.
     */
    public CarbonCredit redeemCredits(Long userId, Double amount) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        List<CarbonCredit> existingCredits = carbonCreditRepository.findByUserId(userId);
        if (existingCredits.isEmpty()) {
            throw new RuntimeException("No carbon credits found for user: " + userId);
        }

        CarbonCredit carbonCredit = existingCredits.get(0);
        if (carbonCredit.getBalance() < amount) {
            throw new RuntimeException("Insufficient carbon credit balance. Available: " + carbonCredit.getBalance());
        }

        carbonCredit.setRedeemed(carbonCredit.getRedeemed() + amount);
        carbonCredit.setBalance(carbonCredit.getBalance() - amount);

        CarbonCredit saved = carbonCreditRepository.save(carbonCredit);

        // Update CarbonWallet to reflect redemption
        Optional<CarbonWallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isPresent()) {
            CarbonWallet wallet = walletOpt.get();
            double newBalance = wallet.getBalance() - amount;
            wallet.setBalance(newBalance);
            double newPoints = wallet.getPoints() - amount;
            wallet.setPoints(newPoints < 0 ? 0 : newPoints);
            
            // Update totalRedeemed to track all redemptions
            Double currentRedeemed = wallet.getTotalRedeemed() != null ? wallet.getTotalRedeemed() : 0.0;
            wallet.setTotalRedeemed(currentRedeemed + amount);
            wallet.setLastRedeem(new java.util.Date());
            
            walletRepository.save(wallet);
        }

        return saved;
    }

    public Double getUserBalance(Long userId) {
        List<CarbonCredit> existingCredits = carbonCreditRepository.findByUserId(userId);
        if (existingCredits.isEmpty()) {
            return 0.0;
        }
        return existingCredits.get(0).getBalance();
    }

    public List<CarbonCredit> getUserCredits(Long userId) {
        return carbonCreditRepository.findByUserId(userId);
    }

    public List<CarbonCredit> getUserCredits(User user) {
        return carbonCreditRepository.findByUser(user);
    }

    public CarbonCredit save(CarbonCredit carbonCredit) {
        if (carbonCredit == null) {
            throw new IllegalArgumentException("CarbonCredit cannot be null");
        }
        return carbonCreditRepository.save(carbonCredit);
    }
}
