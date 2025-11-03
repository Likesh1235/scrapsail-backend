package com.scrapsail.backend.repository;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapOrderRepository extends JpaRepository<ScrapOrder, Long> {
    List<ScrapOrder> findByUser(User user);
    List<ScrapOrder> findByUserId(Long userId);
    List<ScrapOrder> findByStatus(String status);
    List<ScrapOrder> findByCollectorId(Long collectorId);
    List<ScrapOrder> findByStatusAndCollectorEmail(String status, String collectorEmail);
}
