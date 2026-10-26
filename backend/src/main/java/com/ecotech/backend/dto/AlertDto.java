package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class AlertDto {

    private UUID id;
    private UUID wardId;
    private String type;
    private String title;
    private String body;
    private String source;
    private String sourceId;
    private String severity;
    private Instant startsAt;
    private Instant endsAt;
    private boolean active;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    public Instant getStartsAt() { return startsAt; }
    public void setStartsAt(Instant startsAt) { this.startsAt = startsAt; }
    public Instant getEndsAt() { return endsAt; }
    public void setEndsAt(Instant endsAt) { this.endsAt = endsAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
