package com.ecotech.backend.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReportResponse {

    private UUID id;
    private UUID reporterId;
    private UUID wardId;
    private String category;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private String status;
    private Integer upvoteCount;
    private Instant createdAt;
    private Instant updatedAt;
    private List<ReportStatusHistoryDto> statusHistory;
    private Boolean userVoted;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getReporterId() { return reporterId; }
    public void setReporterId(UUID reporterId) { this.reporterId = reporterId; }
    public UUID getWardId() { return wardId; }
    public void setWardId(UUID wardId) { this.wardId = wardId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getUpvoteCount() { return upvoteCount; }
    public void setUpvoteCount(Integer upvoteCount) { this.upvoteCount = upvoteCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<ReportStatusHistoryDto> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<ReportStatusHistoryDto> statusHistory) { this.statusHistory = statusHistory; }
    public Boolean getUserVoted() { return userVoted; }
    public void setUserVoted(Boolean userVoted) { this.userVoted = userVoted; }
}
