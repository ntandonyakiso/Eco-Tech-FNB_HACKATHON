package com.ecotech.backend.dto;

public class OutageCostRequest {

    private Double hours;
    private Double averageHourlyRevenue;

    public Double getHours() { return hours; }
    public void setHours(Double hours) { this.hours = hours; }
    public Double getAverageHourlyRevenue() { return averageHourlyRevenue; }
    public void setAverageHourlyRevenue(Double averageHourlyRevenue) { this.averageHourlyRevenue = averageHourlyRevenue; }
}
