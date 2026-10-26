package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class UsageLogRequest {

    public enum Type { WATER, ENERGY }

    private Type type;
    private Double amount;
    private String unit;
    private Instant recordedAt;

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
