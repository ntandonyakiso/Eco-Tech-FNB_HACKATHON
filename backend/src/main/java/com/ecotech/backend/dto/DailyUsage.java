package com.ecotech.backend.dto;

import java.time.Instant;

public class DailyUsage {

    private Instant date;
    private Double water;
    private Double energy;

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
    public Double getWater() { return water; }
    public void setWater(Double water) { this.water = water; }
    public Double getEnergy() { return energy; }
    public void setEnergy(Double energy) { this.energy = energy; }
}
