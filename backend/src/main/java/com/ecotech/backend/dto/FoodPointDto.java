package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class FoodPointDto {

    public enum Type { GARDEN, FOOD_BANK, SOUP_KITCHEN, MARKET, OTHER }

    private UUID id;
    private UUID addedBy;
    private String name;
    private Type type;
    private String description;
    private Double latitude;
    private Double longitude;
    private UUID wardId;
    private boolean approved;
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAddedBy() { return addedBy; }
    public void setAddedBy(UUID addedBy) { this.addedBy = addedBy; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
