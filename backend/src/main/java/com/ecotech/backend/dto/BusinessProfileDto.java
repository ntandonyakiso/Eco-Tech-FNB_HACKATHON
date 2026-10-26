package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class BusinessProfileDto {

    private UUID id;
    private UUID userId;
    private String businessName;
    private String businessType;
    private Double averageHourlyRevenue;
    private Double monthlyRent;
    private Integer staffCount;
    private Instant createdAt;
    private Instant updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public Double getAverageHourlyRevenue() { return averageHourlyRevenue; }
    public void setAverageHourlyRevenue(Double averageHourlyRevenue) { this.averageHourlyRevenue = averageHourlyRevenue; }
    public Double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Double monthlyRent) { this.monthlyRent = monthlyRent; }
    public Integer getStaffCount() { return staffCount; }
    public void setStaffCount(Integer staffCount) { this.staffCount = staffCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
