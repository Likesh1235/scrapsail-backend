package com.scrapsail.backend.model;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "carbon_wallet")
public class CarbonWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double points;
    private double balance;
    
    @Column(name = "total_redeemed")
    private Double totalRedeemed = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_redeem")
    private Date lastRedeem;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getPoints() { return points; }
    public void setPoints(double points) { this.points = points; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getLastRedeem() { return lastRedeem; }
    public void setLastRedeem(Date lastRedeem) { this.lastRedeem = lastRedeem; }

    public Date getUpdatedAt() { return updatedAt; }
    
    public Double getTotalRedeemed() { return totalRedeemed; }
    public void setTotalRedeemed(Double totalRedeemed) { this.totalRedeemed = totalRedeemed; }
}
