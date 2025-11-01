package com.scrapsail.backend.repository;

import com.scrapsail.backend.model.Leaderboard;
import com.scrapsail.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Optional<Leaderboard> findByUser(User user);
    Optional<Leaderboard> findByUserId(Long userId);
    List<Leaderboard> findAllByOrderByRankPositionAsc();
    List<Leaderboard> findAllByOrderByPointsDesc();
}

