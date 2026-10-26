package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class SurplusPostDto {

    private UUID id;
    private UUID shopId;
    private String shopName;
    private String title;
    private String description;
    private String pickupLocation;
    private Instant pickupStart;
    private Instant pickupEnd;
    private Instant expiresAt;
    private boolean claimed;
    private UUID claimedBy;
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getShopId() { return shopId; }
    public void setShopId(UUID shopId) { this.shopId = shopId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
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
