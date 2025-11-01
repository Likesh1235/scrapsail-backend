package com.scrapsail.backend.repository;

import com.scrapsail.backend.model.CarbonCredit;
import com.scrapsail.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarbonCreditRepository extends JpaRepository<CarbonCredit, Long> {
    List<CarbonCredit> findByUser(User user);
    List<CarbonCredit> findByUserId(Long userId);
    Optional<CarbonCredit> findByUserIdAndId(Long userId, Long id);
}
