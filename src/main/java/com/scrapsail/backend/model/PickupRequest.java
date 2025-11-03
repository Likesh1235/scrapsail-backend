package com.scrapsail.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pickup_requests")
public class PickupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String wasteType;
    private double weightKg;
    private LocalDate pickupDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWasteType() { return wasteType; }
    public void setWasteType(String wasteType) { this.wasteType = wasteType; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
