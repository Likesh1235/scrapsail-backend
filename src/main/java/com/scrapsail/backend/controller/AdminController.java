package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.PickupRequest;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.ScrapOrderRepository;
import com.scrapsail.backend.repository.PickupRequestRepository;
import com.scrapsail.backend.service.CreditService;
import com.scrapsail.backend.service.EmailService;
import com.scrapsail.backend.util.GpsLinkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScrapOrderRepository scrapOrderRepository;

    @Autowired
    private CreditService creditService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PickupRequestRepository pickupRequestRepository;

    // Removed unused userService - not needed for reset functionality

    /**
     * Reset admin and collector passwords - Development endpoint
     * This recreates the users with correct password hashes
     */
    @PostMapping("/reset-default-users")
    public ResponseEntity<Map<String, Object>> resetDefaultUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Delete existing admin and collector
            userRepository.findByEmail("admin@scrapsail.com").ifPresent(userRepository::delete);
            userRepository.findByEmail("collector@scrapsail.com").ifPresent(userRepository::delete);

            // Create admin with correct password hash
            User admin = new User();
            admin.setName("System Administrator");
            admin.setEmail("admin@scrapsail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.admin);
            admin.setTotalPoints(0);
            admin = userRepository.saveAndFlush(admin);

            // Create collector with correct password hash
            User collector = new User();
            collector.setName("John Collector");
            collector.setEmail("collector@scrapsail.com");
            collector.setPassword(passwordEncoder.encode("collector123"));
            collector.setRole(Role.collector);
            collector.setTotalPoints(0);
            collector = userRepository.saveAndFlush(collector);

            response.put("success", true);
            response.put("message", "Default users reset successfully");
            response.put("admin", Map.of("email", admin.getEmail(), "role", admin.getRole().toString()));
            response.put("collector", Map.of("email", collector.getEmail(), "role", collector.getRole().toString()));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error resetting users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all pending orders for admin approval
     */
    @GetMapping("/pending-orders")
    public ResponseEntity<Map<String, Object>> getPendingOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> pendingOrders = scrapOrderRepository.findByStatus("PENDING_APPROVAL");
            
            // Convert to include GPS coordinates explicitly
            List<Map<String, Object>> ordersWithGPS = pendingOrders.stream()
                .map(this::buildOrderMap)
                .collect(java.util.stream.Collectors.toList());
            
            response.put("success", true);
            response.put("orders", ordersWithGPS);
            response.put("count", ordersWithGPS.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving pending orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Approve an order and optionally assign a collector
     * Awards credits to the user when order is approved
     */
    @PostMapping("/approve-order/{orderId}")
    public ResponseEntity<Map<String, Object>> approveOrder(
            @PathVariable @NonNull Long orderId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ScrapOrder order = scrapOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

            // Award credits to the user when order is approved (1 credit per kg)
            if (order.getUser() != null && order.getStatus().equals("PENDING_APPROVAL")) {
                Double creditPoints = order.getWeight(); // 1 kg = 1 credit
                creditService.earnCredits(order.getUser().getId(), creditPoints);
            }

            // Set collector if provided
            String collectorEmail = request.get("collectorEmail");
            if (collectorEmail != null && !collectorEmail.isEmpty()) {
                Optional<User> collectorOpt = userRepository.findByEmail(collectorEmail);
                if (collectorOpt.isPresent()) {
                    order.setCollector(collectorOpt.get());
                    order.setStatus("ASSIGNED"); // Set to ASSIGNED when collector is assigned
                    order.setAssignedAt(new Date());
                    System.out.println("✅ Order " + orderId + " assigned to collector: " + collectorEmail);
                } else {
                    // Collector email provided but not found - keep as APPROVED
                    order.setStatus("APPROVED");
                    System.out.println("⚠️ Collector not found with email: " + collectorEmail + ", keeping order as APPROVED");
                }
            } else {
                // No collector assigned - status is just APPROVED
                order.setStatus("APPROVED");
            }
            
            // Update approved timestamp
            order.setApprovedAt(new Date());

            // Generate/update GPS link - prefer coordinates, fallback to address-based search
            if (order.getGpsLink() == null || order.getGpsLink().isEmpty()) {
                String gpsLink = GpsLinkGenerator.generate(order.getLatitude(), order.getLongitude(), order.getAddress());
                if (gpsLink != null) {
                    order.setGpsLink(gpsLink);
                }
            }

            scrapOrderRepository.save(order);
            
            // Update corresponding PickupRequest status
            if (order.getUser() != null) {
                List<PickupRequest> pickupRequests = pickupRequestRepository.findByUserId(order.getUser().getId());
                // Update the most recent pickup request for this user with matching waste type and weight
                pickupRequests.stream()
                    .filter(pr -> pr.getWasteType().equals(order.getItemType()) && 
                                  Math.abs(pr.getWeightKg() - order.getWeight()) < 0.01)
                    .findFirst()
                    .ifPresent(pr -> {
                        pr.setStatus("APPROVED");
                        pickupRequestRepository.save(pr);
                    });
            }
            
            // Send order details to collector email
            String collectorEmailToNotify = "collectorscrapsail@gmail.com";
            sendOrderDetailsToCollector(order, collectorEmailToNotify);
            
            // Build response with explicit GPS coordinates
            Map<String, Object> orderMap = buildOrderMap(order);
            
            response.put("success", true);
            response.put("message", "Order approved successfully");
            response.put("order", orderMap);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error approving order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Reject an order
     */
    @PostMapping("/reject-order/{orderId}")
    public ResponseEntity<Map<String, Object>> rejectOrder(
            @PathVariable @NonNull Long orderId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ScrapOrder order = scrapOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update order status to rejected
            order.setStatus("REJECTED");
            order.setApprovedAt(new Date());

            scrapOrderRepository.save(order);
            
            response.put("success", true);
            response.put("message", "Order rejected");
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error rejecting order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

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
        
        // Ensure GPS link exists - ALWAYS use coordinates when available, never search/address
        // Priority: coordinates (always regenerate from coordinates, never use address search) > saved coordinate link > null
        String gpsLink = null;
        
        // First priority: Generate from coordinates if available (always use coordinates, never address)
        if (order.getLatitude() != null && order.getLongitude() != null) {
            // ALWAYS generate coordinate-based link (not search)
            gpsLink = GpsLinkGenerator.generateFromCoordinates(order.getLatitude(), order.getLongitude());
            // Update database with coordinate-based link (replace any old address-based link)
            if (gpsLink != null && !gpsLink.isEmpty()) {
                // Always update to ensure it's coordinate-based, not search-based
                if (order.getGpsLink() == null || !order.getGpsLink().equals(gpsLink) || order.getGpsLink().contains("/search/")) {
                    order.setGpsLink(gpsLink);
                    try {
                        scrapOrderRepository.save(order);
                        System.out.println("✅ Updated GPS link for order " + order.getId() + " using coordinates (not search)");
                    } catch (Exception e) {
                        System.err.println("Failed to save GPS link: " + e.getMessage());
                    }
                }
            }
        } else {
            // No coordinates - don't create address-based search link
            // Leave gpsLink as null - frontend will handle fallback
            gpsLink = null;
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

    /**
     * Get all orders for admin management (including assigned, approved, completed, etc.)
     * Returns all orders sorted by creation date (newest first)
     */
    @GetMapping("/all-orders")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("📋 Admin: Fetching all orders from database...");
            List<ScrapOrder> allOrders = scrapOrderRepository.findAll();
            System.out.println("✅ Found " + allOrders.size() + " orders in database");
            
            // Convert to include GPS coordinates explicitly
            List<Map<String, Object>> ordersWithGPS = allOrders.stream()
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
            
            System.out.println("✅ Successfully built " + ordersWithGPS.size() + " order maps");
            
            // Sort by creation date - newest first
            ordersWithGPS.sort((o1, o2) -> {
                Date d1 = (Date) o1.get("createdAt");
                Date d2 = (Date) o2.get("createdAt");
                if (d1 == null) d1 = new Date(0);
                if (d2 == null) d2 = new Date(0);
                return d2.compareTo(d1); // Descending order (newest first)
            });
            
            // Separate orders by status for easier frontend handling
            Map<String, List<Map<String, Object>>> ordersByStatus = new HashMap<>();
            ordersByStatus.put("PENDING_APPROVAL", ordersWithGPS.stream()
                .filter(o -> "PENDING_APPROVAL".equals(o.get("status")))
                .collect(Collectors.toList()));
            ordersByStatus.put("APPROVED", ordersWithGPS.stream()
                .filter(o -> "APPROVED".equals(o.get("status")))
                .collect(Collectors.toList()));
            ordersByStatus.put("ASSIGNED", ordersWithGPS.stream()
                .filter(o -> "ASSIGNED".equals(o.get("status")))
                .collect(Collectors.toList()));
            ordersByStatus.put("COMPLETED", ordersWithGPS.stream()
                .filter(o -> "COMPLETED".equals(o.get("status")))
                .collect(Collectors.toList()));
            ordersByStatus.put("REJECTED", ordersWithGPS.stream()
                .filter(o -> "REJECTED".equals(o.get("status")))
                .collect(Collectors.toList()));
            
            response.put("success", true);
            response.put("orders", ordersWithGPS); // All orders sorted by date with GPS
            response.put("ordersByStatus", ordersByStatus); // Grouped by status with GPS
            response.put("count", ordersWithGPS.size());
            response.put("pendingCount", ordersByStatus.get("PENDING_APPROVAL").size());
            response.put("approvedCount", ordersByStatus.get("APPROVED").size());
            response.put("assignedCount", ordersByStatus.get("ASSIGNED").size());
            response.put("completedCount", ordersByStatus.get("COMPLETED").size());
            System.out.println("✅ Successfully returning " + ordersWithGPS.size() + " orders to admin dashboard");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error in getAllOrders: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving orders: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Send order details to collector email
     */
    private void sendOrderDetailsToCollector(ScrapOrder order, String collectorEmail) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
            
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Dear Collector,\n\n");
            emailBody.append("A new pickup request has been approved and assigned to you.\n\n");
            emailBody.append("ORDER DETAILS:\n");
            emailBody.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            Integer orderNumber = order.getUserOrderNumber() != null ? order.getUserOrderNumber() : order.getId().intValue();
            emailBody.append("Order ID: #").append(orderNumber).append("\n");
            emailBody.append("Waste Type: ").append(order.getItemType() != null ? order.getItemType() : "N/A").append("\n");
            emailBody.append("Weight: ").append(order.getWeight() != null ? order.getWeight() + " kg" : "N/A").append("\n");
            emailBody.append("Pickup Address: ").append(order.getAddress() != null ? order.getAddress() : "N/A").append("\n\n");
            
            // Customer Information
            emailBody.append("CUSTOMER INFORMATION:\n");
            emailBody.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            emailBody.append("Name: ").append(order.getUser() != null && order.getUser().getName() != null ? order.getUser().getName() : "N/A").append("\n");
            emailBody.append("Email: ").append(order.getUserEmail() != null ? order.getUserEmail() : "N/A").append("\n");
            emailBody.append("Phone: ").append(order.getUser() != null && order.getUser().getPhone() != null ? order.getUser().getPhone() : "N/A").append("\n\n");
            
            // Timestamps
            emailBody.append("TIMESTAMPS:\n");
            emailBody.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            if (order.getCreatedAt() != null) {
                emailBody.append("Request Created: ").append(dateFormat.format(order.getCreatedAt())).append("\n");
            }
            if (order.getApprovedAt() != null) {
                emailBody.append("Approved At: ").append(dateFormat.format(order.getApprovedAt())).append("\n");
            }
            if (order.getAssignedAt() != null) {
                emailBody.append("Assigned At: ").append(dateFormat.format(order.getAssignedAt())).append("\n");
            }
            
            emailBody.append("\n");
            emailBody.append("Please proceed with the pickup as soon as possible.\n\n");
            emailBody.append("Best regards,\n");
            emailBody.append("ScrapSail Admin Team");
            
            String subject = "New Pickup Request Approved - Order #" + orderNumber;
            
            emailService.sendPlainEmail(collectorEmail, subject, emailBody.toString());
            System.out.println("✅ Order details sent to collector: " + collectorEmail);
            
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send order details email: " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception - order approval should succeed even if email fails
        }
    }

    /**
     * Update GPS links for all existing orders that don't have them
     * This endpoint can be called to fix existing orders
     */
    @PostMapping("/update-gps-links")
    public ResponseEntity<Map<String, Object>> updateGpsLinks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> allOrders = scrapOrderRepository.findAll();
            int updatedCount = 0;
            
            for (ScrapOrder order : allOrders) {
                // Generate GPS link if missing
                if (order.getGpsLink() == null || order.getGpsLink().isEmpty()) {
                    String gpsLink = GpsLinkGenerator.generate(order.getLatitude(), order.getLongitude(), order.getAddress());
                    if (gpsLink != null) {
                        order.setGpsLink(gpsLink);
                        scrapOrderRepository.save(order);
                        updatedCount++;
                    }
                }
            }
            
            response.put("success", true);
            response.put("message", "Updated GPS links for " + updatedCount + " orders");
            response.put("updatedCount", updatedCount);
            response.put("totalOrders", allOrders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating GPS links: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
