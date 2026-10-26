package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class SurplusPostRequest {

    private String title;
    private String description;
    private String pickupLocation;
    private Instant pickupStart;
    private Instant pickupEnd;
    private Instant expiresAt;

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
}
