package com.scrapsail.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.WalletRepository;

@RestController
@RequestMapping("/api/wallet")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class WalletController {

    private static final double MIN_REDEEM_AMOUNT = 50.0; // ₹50

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getWalletByUserId(@org.springframework.web.bind.annotation.PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();
        try {
            CarbonWallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User u = userRepository.findById(userId != null ? userId : 0L).orElseThrow(() -> new IllegalArgumentException("User not found"));
                    CarbonWallet w = new CarbonWallet();
                    w.setUser(u);
                    w.setPoints(0);
                    w.setBalance(0);
                    return walletRepository.save(w);
                });

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("balance", wallet.getBalance());
            data.put("points", wallet.getPoints());
            data.put("totalRedeemed", wallet.getTotalRedeemed() != null ? wallet.getTotalRedeemed() : 0.0);
            data.put("availableToRedeem", wallet.getBalance()); // Same as balance
            data.put("lastRedeem", wallet.getLastRedeem());

            res.put("success", true);
            res.put("data", data);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "Error fetching wallet: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/redeem")
    public ResponseEntity<Map<String, Object>> redeem(
        @RequestParam Long userId,
        @RequestParam double amount
    ) {
        Map<String, Object> res = new HashMap<>();
        try {
            if (amount < MIN_REDEEM_AMOUNT) {
                res.put("success", false);
                res.put("message", "Minimum redeem amount is ₹" + (int)MIN_REDEEM_AMOUNT);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if (userId == null || !userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User not found: " + userId);
            }

            CarbonWallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for user: " + userId));

            if (wallet.getBalance() < amount) {
                res.put("success", false);
                res.put("message", "Insufficient wallet balance");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            // Assume 1 point = ₹1 for redemption parity
            double pointsToDeduct = amount;
            double currentPoints = wallet.getPoints();
            if (currentPoints < pointsToDeduct) {
                res.put("success", false);
                res.put("message", "Insufficient points to redeem this amount");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            wallet.setBalance(wallet.getBalance() - amount);
            wallet.setPoints(currentPoints - pointsToDeduct);
            wallet.setLastRedeem(new java.util.Date());
            Double currentRedeemed = wallet.getTotalRedeemed() != null ? wallet.getTotalRedeemed() : 0.0;
            wallet.setTotalRedeemed(currentRedeemed + amount);
            walletRepository.save(wallet);

            // DO NOT reduce totalPoints - it should reflect lifetime earnings for leaderboard
            // totalPoints is for leaderboard and shows all-time points earned, not current balance

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("balance", wallet.getBalance());
            data.put("points", wallet.getPoints());
            data.put("totalRedeemed", wallet.getTotalRedeemed());
            data.put("availableToRedeem", wallet.getBalance());
            data.put("redeemedAmount", amount);
            data.put("lastRedeem", wallet.getLastRedeem());

            res.put("success", true);
            res.put("message", "Successfully redeemed ₹" + amount + "! Total redeemed: ₹" + wallet.getTotalRedeemed());
            res.put("data", data);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            res.put("success", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "Error during redeem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
