package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class StockLossRequest {

    private String item;
    private Double quantity;
    private Double unitCost;
    private String lossReason;
    private Instant recordedAt;

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public Double getUnitCost() { return unitCost; }
    public void setUnitCost(Double unitCost) { this.unitCost = unitCost; }
    public String getLossReason() { return lossReason; }
    public void setLossReason(String lossReason) { this.lossReason = lossReason; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
