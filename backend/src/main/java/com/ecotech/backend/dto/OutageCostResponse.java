package com.ecotech.backend.dto;

public class OutageCostResponse {

    private Double hours;
    private Double averageHourlyRevenue;
    private Double totalCost;

    public Double getHours() { return hours; }
    public void setHours(Double hours) { this.hours = hours; }
    public Double getAverageHourlyRevenue() { return averageHourlyRevenue; }
    public void setAverageHourlyRevenue(Double averageHourlyRevenue) { this.averageHourlyRevenue = averageHourlyRevenue; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
}
