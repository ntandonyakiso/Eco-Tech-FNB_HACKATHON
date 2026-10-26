package com.ecotech.backend.dto;

import java.util.UUID;

public class SubscriptionRequest {

    private String channel; // TELEGRAM, WEB_PUSH, SMS
    private String alertType; // WEATHER, LOAD_SHEDDING, WATER, FLOOD, HEAT
    private boolean enabled;

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
