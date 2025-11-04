package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.service.OrderService;
import com.scrapsail.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    // Get user's own orders
    @GetMapping("/{userId}/orders")
    public ResponseEntity<Map<String, Object>> getUserOrders(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userService.findById(userId);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOpt.get();
            
            // Get all orders and filter by user email
            List<ScrapOrder> allOrders = orderService.findAll();
            List<ScrapOrder> userOrders = allOrders.stream()
                .filter(order -> order.getUserEmail() != null && 
                               order.getUserEmail().equals(user.getEmail()))
                .sorted((o1, o2) -> {
                    // Sort by creation date (oldest first)
                    if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) return 0;
                    if (o1.getCreatedAt() == null) return 1;
                    if (o2.getCreatedAt() == null) return -1;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                })
                .collect(Collectors.toList());

            // Ensure all orders have userOrderNumber set (for orders created before this feature)
            for (int i = 0; i < userOrders.size(); i++) {
                ScrapOrder order = userOrders.get(i);
                if (order.getUserOrderNumber() == null) {
                    // Set userOrderNumber based on position (1-indexed)
                    order.setUserOrderNumber(i + 1);
                    // Save the updated order to database
                    orderService.save(order);
                }
            }

            response.put("success", true);
            response.put("orders", userOrders);
            response.put("count", userOrders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get user's own orders by email
    @GetMapping("/orders/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserOrdersByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Get all orders and filter by user email
            List<ScrapOrder> allOrders = orderService.findAll();
            List<ScrapOrder> userOrders = allOrders.stream()
                .filter(order -> order.getUserEmail() != null && 
                               order.getUserEmail().equalsIgnoreCase(email))
                .sorted((o1, o2) -> {
                    // Sort by creation date (oldest first)
                    if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) return 0;
                    if (o1.getCreatedAt() == null) return 1;
                    if (o2.getCreatedAt() == null) return -1;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                })
                .collect(Collectors.toList());

            // Ensure all orders have userOrderNumber set (for orders created before this feature)
            for (int i = 0; i < userOrders.size(); i++) {
                ScrapOrder order = userOrders.get(i);
                if (order.getUserOrderNumber() == null) {
                    // Set userOrderNumber based on position (1-indexed)
                    order.setUserOrderNumber(i + 1);
                    // Save the updated order to database
                    orderService.save(order);
                }
            }

            response.put("success", true);
            response.put("orders", userOrders);
            response.put("count", userOrders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get user profile
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userService.findById(userId);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOpt.get();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole().toString());
            userMap.put("phone", user.getPhone());
            userMap.put("address", user.getAddress());
            userMap.put("totalPoints", user.getTotalPoints());

            response.put("success", true);
            response.put("user", userMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving user profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
