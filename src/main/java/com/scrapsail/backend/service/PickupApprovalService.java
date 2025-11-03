package com.scrapsail.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.WalletRepository;

@Service
public class PickupApprovalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void onPickupApproved(Long userId, double pointsEarned, double amountEarned) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Update leaderboard points on User
        int addPoints = (int) Math.round(pointsEarned);
        Integer current = user.getTotalPoints() == null ? 0 : user.getTotalPoints();
        user.setTotalPoints(current + addPoints);
        userRepository.save(user);

        // Update or create wallet
        CarbonWallet wallet = walletRepository.findByUserId(userId)
            .orElseGet(() -> {
                CarbonWallet w = new CarbonWallet();
                w.setUser(user);
                w.setPoints(0);
                w.setBalance(0);
                return w;
            });

        wallet.setPoints(wallet.getPoints() + pointsEarned);
        wallet.setBalance(wallet.getBalance() + amountEarned);
        walletRepository.save(wallet);
    }
}
