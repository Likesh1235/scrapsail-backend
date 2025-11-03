package com.scrapsail.backend.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "scrap_orders")
public class ScrapOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemType;
    private Double weight;
    private String status;
    private String address; // Pickup address
    private String userEmail; // User email for notifications
    private Double latitude; // GPS latitude for pickup location
    private Double longitude; // GPS longitude for pickup location
    private String gpsLink; // Google Maps link for navigation

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    @JsonIgnore
    private User collector;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getGpsLink() { return gpsLink; }
    public void setGpsLink(String gpsLink) { this.gpsLink = gpsLink; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Date approvedAt) { this.approvedAt = approvedAt; }

    public Date getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Date assignedAt) { this.assignedAt = assignedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getCollector() { return collector; }
    public void setCollector(User collector) { this.collector = collector; }
}
