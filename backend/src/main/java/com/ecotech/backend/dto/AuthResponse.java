package com.ecotech.backend.dto;

public class AuthResponse {

    private String token;
    private String userId;
    private String role;
    private String wardId;
    private String language;

    public AuthResponse() {}

    public AuthResponse(String token, String userId, String role, String wardId, String language) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.wardId = wardId;
        this.language = language;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
