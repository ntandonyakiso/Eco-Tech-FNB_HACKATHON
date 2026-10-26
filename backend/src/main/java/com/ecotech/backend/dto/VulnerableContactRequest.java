package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.UUID;

public class VulnerableContactRequest {

    private String name;
    private String phone;
    private UUID wardId;
    private boolean consent;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public boolean isConsent() { return consent; }
    public void setConsent(boolean consent) { this.consent = consent; }
}
