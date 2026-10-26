package com.ecotech.backend.dto;

public class BackupComparisonRequest {

    private Double hoursOfOutage;
    private Double solarCost;
    private Double generatorCost;
    private Double gasCost;
    private Double fuelCostPerHour;
    private Double gasCostPerHour;

    public Double getHoursOfOutage() { return hoursOfOutage; }
    public void setHoursOfOutage(Double hoursOfOutage) { this.hoursOfOutage = hoursOfOutage; }
    public Double getSolarCost() { return solarCost; }
    public void setSolarCost(Double solarCost) { this.solarCost = solarCost; }
    public Double getGeneratorCost() { return generatorCost; }
    public void setGeneratorCost(Double generatorCost) { this.generatorCost = generatorCost; }
    public Double getGasCost() { return gasCost; }
    public void setGasCost(Double gasCost) { this.gasCost = gasCost; }
    public Double getFuelCostPerHour() { return fuelCostPerHour; }
    public void setFuelCostPerHour(Double fuelCostPerHour) { this.fuelCostPerHour = fuelCostPerHour; }
    public Double getGasCostPerHour() { return gasCostPerHour; }
    public void setGasCostPerHour(Double gasCostPerHour) { this.gasCostPerHour = gasCostPerHour; }
}
