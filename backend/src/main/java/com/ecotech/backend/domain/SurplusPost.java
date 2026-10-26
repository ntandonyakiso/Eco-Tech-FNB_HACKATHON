package com.ecotech.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "surplus_posts")
public class SurplusPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "pickup_location", nullable = false, columnDefinition = "text")
    private String pickupLocation;

    @Column(name = "pickup_start", nullable = false)
    private Instant pickupStart;

    @Column(name = "pickup_end", nullable = false)
    private Instant pickupEnd;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "is_claimed", nullable = false)
    private boolean claimed = false;

    @Column(name = "claimed_by")
    private UUID claimedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getShopId() { return shopId; }
    public void setShopId(UUID shopId) { this.shopId = shopId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public Instant getPickupStart() { return pickupStart; }
    public void setPickupStart(Instant pickupStart) { this.pickupStart = pickupStart; }
    public Instant getPickupEnd() { return pickupEnd; }
    public void setPickupEnd(Instant pickupEnd) { this.pickupEnd = pickupEnd; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isClaimed() { return claimed; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }
    public UUID getClaimedBy() { return claimedBy; }
    public void setClaimedBy(UUID claimedBy) { this.claimedBy = claimedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
