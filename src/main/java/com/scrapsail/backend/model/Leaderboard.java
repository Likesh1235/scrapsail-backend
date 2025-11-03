package com.scrapsail.backend.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rank_position", nullable = false)
    private Integer rankPosition;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "total_recycled")
    private Double totalRecycled = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getRankPosition() { return rankPosition; }
    public void setRankPosition(Integer rankPosition) { this.rankPosition = rankPosition; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Double getTotalRecycled() { return totalRecycled; }
    public void setTotalRecycled(Double totalRecycled) { this.totalRecycled = totalRecycled; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
}

