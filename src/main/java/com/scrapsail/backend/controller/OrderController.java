package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.PickupRequest;
import com.scrapsail.backend.service.OrderService;
import com.scrapsail.backend.service.UserService;
import com.scrapsail.backend.repository.PickupRequestRepository;
import com.scrapsail.backend.util.GpsLinkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
// CORS is handled globally in CorsConfig.java - no need for @CrossOrigin here
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PickupRequestRepository pickupRequestRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Extract essential fields only
            String itemType = orderRequest.get("itemType").toString();
            Double weight = Double.valueOf(orderRequest.get("weight").toString());
            String address = orderRequest.get("address") != null ? orderRequest.get("address").toString() : "";
            String userEmail = orderRequest.get("userEmail") != null ? orderRequest.get("userEmail").toString() : "";
            
            // Extract GPS coordinates if provided (handle both formats: separate fields or coordinates object)
            Double latitude = null;
            Double longitude = null;
            String gpsLink = null;
            
            // Try to get latitude/longitude as separate fields first
            if (orderRequest.get("latitude") != null && !orderRequest.get("latitude").toString().isEmpty()) {
                try {
                    latitude = Double.valueOf(orderRequest.get("latitude").toString());
                } catch (NumberFormatException e) {
                    // Ignore invalid latitude
                }
            }
            if (orderRequest.get("longitude") != null && !orderRequest.get("longitude").toString().isEmpty()) {
                try {
                    longitude = Double.valueOf(orderRequest.get("longitude").toString());
                } catch (NumberFormatException e) {
                    // Ignore invalid longitude
                }
            }
            
            // Fallback: If not found as separate fields, try coordinates object
            if (latitude == null && longitude == null && orderRequest.get("coordinates") != null) {
                try {
                    Object coordinatesObj = orderRequest.get("coordinates");
                    if (coordinatesObj instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> coordsMap = (java.util.Map<String, Object>) coordinatesObj;
                        if (coordsMap.get("latitude") != null) {
                            latitude = Double.valueOf(coordsMap.get("latitude").toString());
                        }
                        if (coordsMap.get("longitude") != null) {
                            longitude = Double.valueOf(coordsMap.get("longitude").toString());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing coordinates object: " + e.getMessage());
                }
            }
            
            System.out.println("📍 Order creation - GPS coordinates: lat=" + latitude + ", lng=" + longitude);
            
            // Generate GPS link - prefer coordinates, fallback to address-based search
            gpsLink = GpsLinkGenerator.generate(latitude, longitude, address);
            
            // Create order with essential fields only
            ScrapOrder order = new ScrapOrder();
            order.setItemType(itemType);
            order.setWeight(weight);
            order.setAddress(address);
            order.setUserEmail(userEmail);
            order.setLatitude(latitude);
            order.setLongitude(longitude);
            order.setGpsLink(gpsLink);
            order.setStatus("PENDING_APPROVAL");
            order.setCreatedAt(new Date());
            
            // Find user by email and link to order
            if (!userEmail.isEmpty()) {
                Optional<User> userOpt = userService.findByEmail(userEmail);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    order.setUser(user);
                    
                    // Calculate the next order number for this user
                    Integer maxOrderNumber = orderService.getMaxUserOrderNumber(user.getId());
                    Integer nextOrderNumber = (maxOrderNumber == null ? 0 : maxOrderNumber) + 1;
                    order.setUserOrderNumber(nextOrderNumber);
                    
                    // Update user record with pickup form details (for regular users only)
                    if (user.getRole() == com.scrapsail.backend.model.Role.user) {
                        // Update phone if provided in pickup form
                        String userPhone = orderRequest.get("userPhone") != null ? orderRequest.get("userPhone").toString() : null;
                        if (userPhone != null && !userPhone.isEmpty()) {
                            user.setPhone(userPhone);
                        }
                        
                        // Update address from pickup form
                        if (address != null && !address.isEmpty()) {
                            user.setAddress(address);
                        }
                        
                        // Save updated user record
                        userService.save(user);
                    }
                }
            }
            
            ScrapOrder savedOrder = orderService.save(order);
            
            // Create corresponding PickupRequest entry
            if (savedOrder.getUser() != null) {
                PickupRequest pickupRequest = new PickupRequest();
                pickupRequest.setWasteType(savedOrder.getItemType());
                pickupRequest.setWeightKg(savedOrder.getWeight() != null ? savedOrder.getWeight() : 0.0);
                pickupRequest.setPickupDate(LocalDate.now());
                pickupRequest.setStatus(savedOrder.getStatus());
                pickupRequest.setUser(savedOrder.getUser());
                pickupRequestRepository.save(pickupRequest);
            }
            
            response.put("success", true);
            response.put("message", "Pickup request submitted successfully. Pending admin approval.");
            response.put("order", savedOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrderWithUser(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = Long.valueOf(orderRequest.get("userId").toString());
            String itemType = orderRequest.get("itemType").toString();
            Double weight = Double.valueOf(orderRequest.get("weight").toString());
            
            ScrapOrder order = orderService.createOrder(userId, itemType, weight);
            response.put("success", true);
            response.put("message", "Order created successfully");
            response.put("order", order);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String status = statusRequest.get("status");
            if (status == null || status.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Status is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            ScrapOrder order = orderService.updateOrderStatus(orderId, status);
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating order status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrdersByUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> orders = orderService.getOrdersByUser(userId);
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ScrapOrder> order = orderService.findById(orderId);
            if (order.isPresent()) {
                response.put("success", true);
                response.put("order", order.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> orders = orderService.getOrdersByStatus(status);
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ScrapOrder> orders = orderService.findAll();
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
