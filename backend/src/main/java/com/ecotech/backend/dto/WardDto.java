package com.ecotech.backend.dto;

public class WardDto {

    private String id;
    private String name;
    private String province;
    private String municipality;
    private Double latitude;
    private Double longitude;
    private Integer wardNumber;

    public WardDto() {}

    public WardDto(String id, String name, String province, String municipality, Double latitude, Double longitude, Integer wardNumber) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.municipality = municipality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wardNumber = wardNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Integer getWardNumber() { return wardNumber; }
    public void setWardNumber(Integer wardNumber) { this.wardNumber = wardNumber; }
}
