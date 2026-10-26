package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class BusinessProfileRequest {

    private String businessName;
    private String businessType;
    private Double averageHourlyRevenue;
    private Double monthlyRent;
    private Integer staffCount;

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
}
