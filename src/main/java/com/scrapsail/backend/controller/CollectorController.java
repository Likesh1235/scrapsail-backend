package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.repository.ScrapOrderRepository;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.service.OrderService;
import com.scrapsail.backend.service.UserService;
import com.scrapsail.backend.util.GpsLinkGenerator;
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
@RequestMapping("/api/collector")
public class CollectorController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScrapOrderRepository scrapOrderRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Helper method to build order map with GPS coordinates and navigation link
     * Generates GPS link on-the-fly if missing in database
     */
    private Map<String, Object> buildOrderMap(ScrapOrder order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("userOrderNumber", order.getUserOrderNumber() != null ? order.getUserOrderNumber() : order.getId()); // Use userOrderNumber if available, fallback to id
        orderMap.put("itemType", order.getItemType());
        orderMap.put("weight", order.getWeight());
        orderMap.put("status", order.getStatus());
        orderMap.put("address", order.getAddress());
        orderMap.put("userEmail", order.getUserEmail());
        orderMap.put("latitude", order.getLatitude());
        orderMap.put("longitude", order.getLongitude());
        
        // Ensure GPS link exists - ALWAYS generate from latitude/longitude when available
        // This GPS link will be used for navigation with the captured coordinates
        String gpsLink = null;
        
        // First priority: Generate GPS link from latitude/longitude if available
        if (order.getLatitude() != null && order.getLongitude() != null) {
            // ALWAYS generate GPS link from coordinates (this is what frontend will use for navigation)
            gpsLink = GpsLinkGenerator.generateFromCoordinates(order.getLatitude(), order.getLongitude());
            System.out.println("📍 Order " + order.getId() + " - Generated GPS link from coordinates: lat=" + order.getLatitude() + ", lng=" + order.getLongitude());
            
            // Update database with coordinate-based link (replace any old address-based link)
            if (gpsLink != null && !gpsLink.isEmpty()) {
                // Always update to ensure it's coordinate-based, not search-based
                if (order.getGpsLink() == null || !order.getGpsLink().equals(gpsLink) || order.getGpsLink().contains("/search/")) {
                    order.setGpsLink(gpsLink);
                    try {
                        scrapOrderRepository.save(order);
                        System.out.println("✅ Saved GPS link for order " + order.getId() + ": " + gpsLink);
                    } catch (Exception e) {
                        System.err.println("Failed to save GPS link: " + e.getMessage());
                    }
                }
            }
        } else {
            // No coordinates - don't create address-based search link
            // Leave gpsLink as null - frontend will handle fallback
            gpsLink = null;
            System.out.println("⚠️ Order " + order.getId() + " - No latitude/longitude available");
        }
        // Always include GPS link (even if null) and add helper fields for frontend
        orderMap.put("gpsLink", gpsLink);
        orderMap.put("navigationLink", gpsLink); // Alias for easier frontend access
        orderMap.put("mapLink", gpsLink); // Another alias
        orderMap.put("locationLink", gpsLink); // Another alias
        orderMap.put("hasNavigationLink", gpsLink != null && !gpsLink.isEmpty());
        orderMap.put("hasCoordinates", order.getLatitude() != null && order.getLongitude() != null);
        
        // Add location info for easy access
        if (gpsLink != null && !gpsLink.isEmpty()) {
            Map<String, Object> locationInfo = new HashMap<>();
            locationInfo.put("address", order.getAddress() != null ? order.getAddress() : "");
            locationInfo.put("latitude", order.getLatitude());
            locationInfo.put("longitude", order.getLongitude());
            locationInfo.put("navigationUrl", gpsLink);
            orderMap.put("location", locationInfo);
        }
        
        orderMap.put("createdAt", order.getCreatedAt());
        orderMap.put("approvedAt", order.getApprovedAt());
        orderMap.put("assignedAt", order.getAssignedAt());
        
        // Add user information
        if (order.getUser() != null) {
            User user = order.getUser();
            orderMap.put("userId", user.getId());
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getName() != null ? user.getName() : "");
            userInfo.put("email", user.getEmail() != null ? user.getEmail() : "");
            userInfo.put("phone", user.getPhone() != null ? user.getPhone() : "");
            orderMap.put("user", userInfo);
        } else {
            // If no user linked, create minimal user object from userEmail
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", null);
            String userName = "Unknown";
            if (order.getUserEmail() != null && !order.getUserEmail().isEmpty()) {
                String[] parts = order.getUserEmail().split("@");
                if (parts.length > 0) {
                    userName = parts[0];
                }
            }
            userInfo.put("name", userName);
            userInfo.put("email", order.getUserEmail() != null ? order.getUserEmail() : "");
            userInfo.put("phone", "Not provided");
            orderMap.put("user", userInfo);
        }
        
        // Add collector information
        if (order.getCollector() != null) {
            User collector = order.getCollector();
            orderMap.put("collectorId", collector.getId());
            Map<String, Object> collectorInfo = new HashMap<>();
            collectorInfo.put("id", collector.getId());
            collectorInfo.put("name", collector.getName() != null ? collector.getName() : "");
            collectorInfo.put("email", collector.getEmail() != null ? collector.getEmail() : "");
            orderMap.put("collector", collectorInfo);
            orderMap.put("collectorEmail", collector.getEmail());
            orderMap.put("collectorAssigned", collector.getName());
        } else {
            orderMap.put("collectorId", null);
            orderMap.put("collectorEmail", null);
            orderMap.put("collectorAssigned", null);
        }
        
        // Add fields that frontend might expect
        orderMap.put("userPhone", order.getUser() != null && order.getUser().getPhone() != null ? order.getUser().getPhone() : "Not provided");
        
        return orderMap;
    }

    // Collector Dashboard
    @GetMapping("/dashboard/{collectorId}")
    public ResponseEntity<Map<String, Object>> getCollectorDashboard(@PathVariable Long collectorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("📋 Collector: Fetching dashboard for collectorId: " + collectorId);
            Optional<User> collectorOpt = userService.findById(collectorId);
            if (!collectorOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Collector not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User collector = collectorOpt.get();
            if (collector.getRole() != Role.collector) {
                response.put("success", false);
                response.put("message", "Access denied. Collector role required.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Get orders assigned to this collector (APPROVED, ASSIGNED, ACCEPTED, PICKED_UP statuses)
            List<ScrapOrder> assignedOrders = scrapOrderRepository.findByCollectorId(collectorId);
            System.out.println("✅ Found " + assignedOrders.size() + " orders assigned to collector ID: " + collectorId);
            
            // Also include APPROVED orders not yet assigned (for collector to pick up)
            List<ScrapOrder> availableOrders = orderService.getOrdersByStatus("APPROVED");
            availableOrders.stream()
                .filter(o -> o.getCollector() == null || !assignedOrders.contains(o))
                .forEach(assignedOrders::add);
            
            System.out.println("✅ Total orders (assigned + available): " + assignedOrders.size());
            
            // Convert orders to include GPS coordinates explicitly
            List<Map<String, Object>> ordersWithGPS = assignedOrders.stream()
                .map(order -> {
                    try {
                        return buildOrderMap(order);
                    } catch (Exception e) {
                        System.err.println("⚠️ Error building order map for order ID: " + order.getId() + " - " + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(map -> map != null) // Filter out any null entries from failed builds
                .collect(Collectors.toList());
            
            System.out.println("✅ Successfully built " + ordersWithGPS.size() + " order maps");
            
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("collector", collector);
            dashboardData.put("assignedOrders", ordersWithGPS);
            dashboardData.put("totalAssigned", ordersWithGPS.size());
            dashboardData.put("pendingPickups", assignedOrders.stream().filter(o -> "PENDING".equals(o.getStatus()) || "APPROVED".equals(o.getStatus())).count());
            dashboardData.put("completedPickups", assignedOrders.stream().filter(o -> "COMPLETED".equals(o.getStatus())).count());

            response.put("success", true);
            response.put("data", dashboardData);
            System.out.println("✅ Successfully returning collector dashboard data");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error in getCollectorDashboard: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving collector dashboard: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get assigned orders for collector (APPROVED status with collector email)
    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getAssignedOrders(@RequestParam(required = false) String collectorEmail) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("📋 Collector: Fetching orders for collectorEmail: " + collectorEmail);
            List<ScrapOrder> assignedOrders;
            
            if (collectorEmail != null && !collectorEmail.isEmpty()) {
                // Find collector by email first
                Optional<User> collectorOpt = userRepository.findByEmail(collectorEmail);
                if (collectorOpt.isPresent()) {
                    User collector = collectorOpt.get();
                    // Get orders assigned to this specific collector
                    assignedOrders = scrapOrderRepository.findByCollectorId(collector.getId());
                    System.out.println("✅ Found " + assignedOrders.size() + " orders assigned to collector ID: " + collector.getId());
                } else {
                    // Collector not found, return empty list
                    System.out.println("⚠️ Collector not found with email: " + collectorEmail);
                    assignedOrders = new java.util.ArrayList<>();
                }
            } else {
                // Get all APPROVED orders (assignable to any collector)
                assignedOrders = orderService.getOrdersByStatus("APPROVED");
                System.out.println("✅ Found " + assignedOrders.size() + " APPROVED orders (no specific collector)");
            }
            
            // Ensure all orders have userOrderNumber set (group by user and assign sequential numbers)
            // Group orders by user
            Map<Long, List<ScrapOrder>> ordersByUser = assignedOrders.stream()
                .filter(order -> order.getUser() != null)
                .collect(java.util.stream.Collectors.groupingBy(order -> order.getUser().getId()));
            
            // For each user, ensure their orders have userOrderNumber set
            for (Map.Entry<Long, List<ScrapOrder>> entry : ordersByUser.entrySet()) {
                Long userId = entry.getKey();
                List<ScrapOrder> userOrders = entry.getValue();
                
                // Sort by creation date (oldest first)
                userOrders.sort((o1, o2) -> {
                    if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) return 0;
                    if (o1.getCreatedAt() == null) return 1;
                    if (o2.getCreatedAt() == null) return -1;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                });
                
                // Assign userOrderNumber for orders that don't have it
                for (int i = 0; i < userOrders.size(); i++) {
                    ScrapOrder order = userOrders.get(i);
                    if (order.getUserOrderNumber() == null) {
                        order.setUserOrderNumber(i + 1);
                        orderService.save(order);
                        System.out.println("✅ Set userOrderNumber=" + (i + 1) + " for order ID=" + order.getId() + " (user ID=" + userId + ")");
                    }
                }
            }
            
            // Ensure all orders include GPS coordinates in response
            List<Map<String, Object>> ordersWithGPS = assignedOrders.stream()
                .map(order -> {
                    try {
                        return buildOrderMap(order);
                    } catch (Exception e) {
                        System.err.println("⚠️ Error building order map for order ID: " + order.getId() + " - " + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(map -> map != null) // Filter out any null entries from failed builds
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("✅ Successfully built " + ordersWithGPS.size() + " order maps for collector");
            response.put("success", true);
            response.put("orders", ordersWithGPS);
            response.put("count", ordersWithGPS.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error in getAssignedOrders: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving assigned orders: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Collector accepts an assigned order
    @PostMapping("/orders/{orderId}/accept")
    public ResponseEntity<Map<String, Object>> acceptOrder(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ScrapOrder> orderOpt = orderService.findById(orderId);
            if (!orderOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            ScrapOrder order = orderOpt.get();
            
            // Check if order is in ASSIGNED status
            if (!"ASSIGNED".equals(order.getStatus())) {
                response.put("success", false);
                response.put("message", "Order is not in ASSIGNED status. Current status: " + order.getStatus());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Update order status to ACCEPTED
            order.setStatus("ACCEPTED");
            ScrapOrder savedOrder = orderService.save(order);

            // Build response with explicit GPS coordinates
            Map<String, Object> orderMap = buildOrderMap(savedOrder);

            response.put("success", true);
            response.put("message", "Order accepted successfully. You can now proceed with pickup.");
            response.put("order", orderMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error accepting order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update order status (pickup started, completed, etc.)
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId, 
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String status = request.get("status");
            String notes = request.get("notes");
            
            ScrapOrder order = orderService.updateOrderStatus(orderId, status);
            
            // Build response with explicit GPS coordinates
            Map<String, Object> orderMap = buildOrderMap(order);
            
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("order", orderMap);
            response.put("notes", notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating order status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Mark order as picked up
    @PutMapping("/orders/{orderId}/pickup")
    public ResponseEntity<Map<String, Object>> markOrderPickedUp(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Double actualWeight = Double.valueOf(request.get("actualWeight").toString());
            String notes = request.get("notes") != null ? request.get("notes").toString() : "";
            
            ScrapOrder order = orderService.updateOrderStatus(orderId, "PICKED_UP");
            
            // Build response with explicit GPS coordinates
            Map<String, Object> orderMap = buildOrderMap(order);
            
            response.put("success", true);
            response.put("message", "Order marked as picked up");
            response.put("order", orderMap);
            response.put("actualWeight", actualWeight);
            response.put("notes", notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error marking order as picked up: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Mark order as completed
    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<Map<String, Object>> markOrderCompleted(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Double actualWeight = Double.valueOf(request.get("actualWeight").toString());
            String notes = request.get("notes") != null ? request.get("notes").toString() : "";
            
            ScrapOrder order = orderService.updateOrderStatus(orderId, "COMPLETED");
            
            // Build response with explicit GPS coordinates
            Map<String, Object> orderMap = buildOrderMap(order);
            
            response.put("success", true);
            response.put("message", "Order completed successfully");
            response.put("order", orderMap);
            response.put("actualWeight", actualWeight);
            response.put("notes", notes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error completing order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get collector dashboard stats
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getCollectorStats(@RequestParam Long collectorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> allOrders = orderService.findAll();
            List<ScrapOrder> assignedOrders = orderService.getOrdersByStatus("APPROVED");
            List<ScrapOrder> pickedUpOrders = orderService.getOrdersByStatus("PICKED_UP");
            List<ScrapOrder> completedOrders = orderService.getOrdersByStatus("COMPLETED");
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("assignedOrders", assignedOrders.size());
            stats.put("pickedUpOrders", pickedUpOrders.size());
            stats.put("completedOrders", completedOrders.size());
            stats.put("totalOrders", allOrders.size());
            
            response.put("success", true);
            response.put("stats", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving collector stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get order details
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var orderOpt = orderService.findById(orderId);
            if (orderOpt.isPresent()) {
                ScrapOrder order = orderOpt.get();
                // Build response with explicit GPS coordinates
                Map<String, Object> orderMap = buildOrderMap(order);
                
                response.put("success", true);
                response.put("order", orderMap);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving order details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
