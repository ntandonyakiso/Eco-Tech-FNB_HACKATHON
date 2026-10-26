package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class ReportStatusHistoryDto {

    private UUID id;
    private String oldStatus;
    private String newStatus;
    private UUID changedBy;
    private String note;
    private Instant changedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public UUID getChangedBy() { return changedBy; }
    public void setChangedBy(UUID changedBy) { this.changedBy = changedBy; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}
