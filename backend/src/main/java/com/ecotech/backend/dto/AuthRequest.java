package com.ecotech.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    public enum Type { PHONE, EMAIL }

    @NotBlank
    private String identifier;

    @NotBlank
    @Size(min = 6)
    private String password;

    private Type type = Type.PHONE;

    @NotBlank
    @Size(min = 2, max = 32)
    private String language = "en";

    @NotBlank
    @Size(min = 2, max = 100)
    private String wardName;

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }
}
