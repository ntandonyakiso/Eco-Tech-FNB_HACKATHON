package com.ecotech.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

public class ReportRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    private Double latitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    private Double longitude;

    @NotBlank
    @Size(max = 32)
    private String category; // POTHOLE, BURST_PIPE, STREETLIGHT, OUTAGE, OTHER

    @Size(max = 100)
    private String wardId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }
}
