package com.scrapsail.backend.service;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.PickupRequest;
import com.scrapsail.backend.repository.ScrapOrderRepository;
import com.scrapsail.backend.repository.UserRepository;
import com.scrapsail.backend.repository.PickupRequestRepository;
import com.scrapsail.backend.util.GpsLinkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ScrapOrderRepository scrapOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditService creditService;

    @Autowired
    private PickupRequestRepository pickupRequestRepository;

    /**
     * Get user wallet balance - helper method for storing balance at order time
     */
    public Double getUserWalletBalance(Long userId) {
        try {
            return creditService.getUserBalance(userId);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public ScrapOrder createOrder(Long userId, String itemType, Double weight) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Optional<User> user = userRepository.findById(userId);
        User foundUser = user.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Calculate the next order number for this user
        Integer maxOrderNumber = scrapOrderRepository.findMaxUserOrderNumberByUserId(userId);
        Integer nextOrderNumber = (maxOrderNumber == null ? 0 : maxOrderNumber) + 1;

        ScrapOrder order = new ScrapOrder();
        order.setUser(foundUser);
        order.setUserOrderNumber(nextOrderNumber);
        order.setItemType(itemType);
        order.setWeight(weight);
        order.setStatus("PENDING");
        order.setCreatedAt(new Date());

        return scrapOrderRepository.save(order);
    }

    public ScrapOrder updateOrderStatus(Long orderId, String status) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        Optional<ScrapOrder> order = scrapOrderRepository.findById(orderId);
        ScrapOrder existingOrder = order.orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        String previousStatus = existingOrder.getStatus();
        existingOrder.setStatus(status);
        
        // Ensure GPS link exists - generate from coordinates or address if missing
        if (existingOrder.getGpsLink() == null || existingOrder.getGpsLink().isEmpty()) {
            String gpsLink = GpsLinkGenerator.generate(existingOrder.getLatitude(), existingOrder.getLongitude(), existingOrder.getAddress());
            if (gpsLink != null) {
                existingOrder.setGpsLink(gpsLink);
            }
        }
        
        // If order is being completed, award credits
        if ("COMPLETED".equals(status) && !"COMPLETED".equals(previousStatus)) {
            // Calculate credits based on weight (1 kg = 1 point = ₹1)
            Double creditPoints = existingOrder.getWeight();
            if (existingOrder.getUser() != null) {
                creditService.earnCredits(existingOrder.getUser().getId(), creditPoints);
                
                // Update corresponding PickupRequest status
                List<PickupRequest> pickupRequests = pickupRequestRepository.findByUserId(existingOrder.getUser().getId());
                pickupRequests.stream()
                    .filter(pr -> pr.getWasteType().equals(existingOrder.getItemType()) && 
                                  Math.abs(pr.getWeightKg() - existingOrder.getWeight()) < 0.01)
                    .findFirst()
                    .ifPresent(pr -> {
                        pr.setStatus("COMPLETED");
                        pickupRequestRepository.save(pr);
                    });
            }
        }
        
        // Update PickupRequest status to match order status for other status changes
        if (existingOrder.getUser() != null && !"COMPLETED".equals(status)) {
            List<PickupRequest> pickupRequests = pickupRequestRepository.findByUserId(existingOrder.getUser().getId());
            pickupRequests.stream()
                .filter(pr -> pr.getWasteType().equals(existingOrder.getItemType()) && 
                              Math.abs(pr.getWeightKg() - existingOrder.getWeight()) < 0.01)
                .findFirst()
                .ifPresent(pr -> {
                    pr.setStatus(status);
                    pickupRequestRepository.save(pr);
                });
        }
        
        return scrapOrderRepository.save(existingOrder);
    }

    public List<ScrapOrder> getOrdersByUser(Long userId) {
        return scrapOrderRepository.findByUserId(userId);
    }

    public List<ScrapOrder> getOrdersByUser(User user) {
        return scrapOrderRepository.findByUser(user);
    }

    public List<ScrapOrder> getOrdersByStatus(String status) {
        return scrapOrderRepository.findByStatus(status);
    }

    public Optional<ScrapOrder> findById(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return scrapOrderRepository.findById(orderId);
    }

    public List<ScrapOrder> findAll() {
        return scrapOrderRepository.findAll();
    }

    public List<ScrapOrder> getAllOrders() {
        return scrapOrderRepository.findAll();
    }

    public List<ScrapOrder> getOrdersByCollector(Long collectorId) {
        return scrapOrderRepository.findByCollectorId(collectorId);
    }

    public ScrapOrder save(ScrapOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("ScrapOrder cannot be null");
        }
        return scrapOrderRepository.save(order);
    }

    /**
     * Get the maximum order number for a user
     */
    public Integer getMaxUserOrderNumber(Long userId) {
        if (userId == null) {
            return 0;
        }
        Integer maxOrderNumber = scrapOrderRepository.findMaxUserOrderNumberByUserId(userId);
        return maxOrderNumber == null ? 0 : maxOrderNumber;
    }
}
