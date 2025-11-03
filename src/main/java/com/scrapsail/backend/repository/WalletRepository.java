package com.scrapsail.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.User;

@Repository
public interface WalletRepository extends JpaRepository<CarbonWallet, Long> {
    Optional<CarbonWallet> findByUserId(Long userId);
    Optional<CarbonWallet> findByUser(User user);
}
