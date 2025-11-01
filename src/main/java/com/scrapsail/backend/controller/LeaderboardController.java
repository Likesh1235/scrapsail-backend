package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.Leaderboard;
import com.scrapsail.backend.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> getLeaderboard() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get leaderboard entries from leaderboard table
            List<Leaderboard> leaderboardEntries = leaderboardService.getAllLeaderboard();
            
            // Convert to response format
            List<Map<String, Object>> leaderboard = new ArrayList<>();
            
            leaderboardEntries.stream()
                .filter(entry -> entry.getPoints() != null && entry.getPoints() > 0)
                .limit(10) // Top 10 users
                .forEach(entry -> {
                    Map<String, Object> userMap = new HashMap<>();
                    if (entry.getUser() != null) {
                        userMap.put("name", entry.getUser().getName());
                        userMap.put("email", entry.getUser().getEmail());
                    }
                    userMap.put("points", entry.getPoints());
                    userMap.put("rank", entry.getRankPosition());
                    userMap.put("totalRecycled", entry.getTotalRecycled());
                    // Add medal for top 3
                    int currentRank = entry.getRankPosition();
                    if (currentRank == 1) userMap.put("medal", "🥇");
                    else if (currentRank == 2) userMap.put("medal", "🥈");
                    else if (currentRank == 3) userMap.put("medal", "🥉");
                    leaderboard.add(userMap);
                });

            response.put("success", true);
            response.put("leaderboard", leaderboard);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving leaderboard: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}




