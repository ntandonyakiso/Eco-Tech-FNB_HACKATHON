package com.ecotech.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String municipality;

    @Column(name = "centroid_lat")
    private Double centroidLat;

    @Column(name = "centroid_lon")
    private Double centroidLon;

    @Column(name = "ward_number")
    private Integer wardNumber;

    @Column(columnDefinition = "text")
    private String boundaryWkt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }
    public Double getCentroidLat() { return centroidLat; }
    public void setCentroidLat(Double centroidLat) { this.centroidLat = centroidLat; }
    public Double getCentroidLon() { return centroidLon; }
    public void setCentroidLon(Double centroidLon) { this.centroidLon = centroidLon; }
    public Integer getWardNumber() { return wardNumber; }
    public void setWardNumber(Integer wardNumber) { this.wardNumber = wardNumber; }
    public String getBoundaryWkt() { return boundaryWkt; }
    public void setBoundaryWkt(String boundaryWkt) { this.boundaryWkt = boundaryWkt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
