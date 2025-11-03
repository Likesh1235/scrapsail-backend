package com.scrapsail.backend.repository;

import com.scrapsail.backend.model.PickupRequest;
import com.scrapsail.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {
    List<PickupRequest> findByUser(User user);
    List<PickupRequest> findByUserId(Long userId);
    List<PickupRequest> findByStatus(String status);
    Optional<PickupRequest> findById(Long id);
}

