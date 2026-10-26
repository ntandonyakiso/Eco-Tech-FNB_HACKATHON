package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class PreparednessPlanDto {

    private UUID id;
    private UUID userId;
    private String householdType;
    private UUID wardId;
    private String planData;
    private Instant createdAt;
    private Instant updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getHouseholdType() { return householdType; }
    public void setHouseholdType(String householdType) { this.householdType = householdType; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public String getPlanData() { return planData; }
    public void setPlanData(String planData) { this.planData = planData; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
