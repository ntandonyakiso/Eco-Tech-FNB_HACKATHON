package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class ResourceListingRequest {

    public enum Category { WATER, SOLAR_CHARGING, GENERATOR, TRANSPORT, OTHER }

    private UUID wardId;
    private Category category;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private Instant availabilityStart;
    private Instant availabilityEnd;
    private String contactMethod;
    private String contactValue;

    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Instant getAvailabilityStart() { return availabilityStart; }
    public void setAvailabilityStart(Instant availabilityStart) { this.availabilityStart = availabilityStart; }
    public Instant getAvailabilityEnd() { return availabilityEnd; }
    public void setAvailabilityEnd(Instant availabilityEnd) { this.availabilityEnd = availabilityEnd; }
    public String getContactMethod() { return contactMethod; }
    public void setContactMethod(String contactMethod) { this.contactMethod = contactMethod; }
    public String getContactValue() { return contactValue; }
    public void setContactValue(String contactValue) { this.contactValue = contactValue; }
}
