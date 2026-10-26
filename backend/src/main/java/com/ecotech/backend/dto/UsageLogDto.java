package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class UsageLogDto {

    public enum Type { WATER, ENERGY }

    private UUID id;
    private UUID userId;
    private Type type;
    private Double amount;
    private String unit;
    private Instant recordedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
