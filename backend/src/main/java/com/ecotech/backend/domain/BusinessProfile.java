package com.ecotech.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "business_profiles")
public class BusinessProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "average_hourly_revenue", nullable = false)
    private Double averageHourlyRevenue = 0.0;

    @Column(name = "monthly_rent", nullable = false)
    private Double monthlyRent = 0.0;

    @Column(name = "staff_count", nullable = false)
    private Integer staffCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
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
