package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UsageSummary {

    private Instant periodStart;
    private Instant periodEnd;
    private Double totalWater;
    private Double totalEnergy;
    private Double estimatedRandSavings;
    private List<DailyUsage> daily;

    public Instant getPeriodStart() { return periodStart; }
    public void setPeriodStart(Instant periodStart) { this.periodStart = periodStart; }
    public Instant getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(Instant periodEnd) { this.periodEnd = periodEnd; }
    public Double getTotalWater() { return totalWater; }
    public void setTotalWater(Double totalWater) { this.totalWater = totalWater; }
    public Double getTotalEnergy() { return totalEnergy; }
    public void setTotalEnergy(Double totalEnergy) { this.totalEnergy = totalEnergy; }
    public Double getEstimatedRandSavings() { return estimatedRandSavings; }
    public void setEstimatedRandSavings(Double estimatedRandSavings) { this.estimatedRandSavings = estimatedRandSavings; }
    public List<DailyUsage> getDaily() { return daily; }
    public void setDaily(List<DailyUsage> daily) { this.daily = daily; }
}
