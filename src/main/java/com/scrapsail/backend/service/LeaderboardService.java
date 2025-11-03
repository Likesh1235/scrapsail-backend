package com.scrapsail.backend.service;

import com.scrapsail.backend.model.Leaderboard;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.repository.LeaderboardRepository;
import com.scrapsail.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaderboardService {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Update or create leaderboard entry for a user
     */
    public void updateLeaderboard(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        Optional<Leaderboard> leaderboardOpt = leaderboardRepository.findByUserId(userId);

        Leaderboard leaderboard;
        if (leaderboardOpt.isPresent()) {
            leaderboard = leaderboardOpt.get();
        } else {
            leaderboard = new Leaderboard();
            leaderboard.setUser(user);
            // Set a temporary rankPosition to avoid null constraint violation
            // It will be recalculated in recalculateRanks()
            leaderboard.setRankPosition(9999); // Temporary high rank
        }

        // Update points and total recycled from user
        leaderboard.setPoints(user.getTotalPoints() != null ? user.getTotalPoints() : 0);
        leaderboard.setTotalRecycled(user.getTotalRecycled() != null ? user.getTotalRecycled() : 0.0);

        // Save the leaderboard entry first
        leaderboardRepository.saveAndFlush(leaderboard);

        // Recalculate all ranks to set correct positions
        recalculateRanks();
    }

    /**
     * Recalculate rank positions for all users
     */
    private void recalculateRanks() {
        List<Leaderboard> allEntries = leaderboardRepository.findAllByOrderByPointsDesc();
        int rank = 1;
        for (Leaderboard entry : allEntries) {
            // Only update if rank has changed or if it's the temporary value
            if (entry.getRankPosition() == null || entry.getRankPosition() != rank || entry.getRankPosition() == 9999) {
                entry.setRankPosition(rank);
                leaderboardRepository.save(entry);
            }
            rank++;
        }
    }

    /**
     * Get all leaderboard entries
     */
    public List<Leaderboard> getAllLeaderboard() {
        return leaderboardRepository.findAllByOrderByRankPositionAsc();
    }
}

