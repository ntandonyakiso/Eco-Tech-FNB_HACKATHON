package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class PreparednessPlanRequest {

    private String householdType;
    private UUID wardId;
    private String planData;

    public String getHouseholdType() { return householdType; }
    public void setHouseholdType(String householdType) { this.householdType = householdType; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public String getPlanData() { return planData; }
    public void setPlanData(String planData) { this.planData = planData; }
}
