package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class FoodPointRequest {

    public enum Type { GARDEN, FOOD_BANK, SOUP_KITCHEN, MARKET, OTHER }

    private String name;
    private Type type;
    private String description;
    private Double latitude;
    private Double longitude;
    private UUID wardId;

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
}
