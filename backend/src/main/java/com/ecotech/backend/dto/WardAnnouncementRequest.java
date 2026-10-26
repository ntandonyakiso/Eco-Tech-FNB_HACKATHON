package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class WardAnnouncementRequest {

    private UUID wardId;
    private String title;
    private String body;
    private String type; // GENERAL, EMERGENCY, MEETING, ALERT
    private String expiresAt; // ISO-8601, optional

    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
}
