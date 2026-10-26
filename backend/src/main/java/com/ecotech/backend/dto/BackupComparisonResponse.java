package com.ecotech.backend.dto;

public class BackupComparisonResponse {

    private String recommendation;
    private Double solarTotal;
    private Double generatorTotal;
    private Double gasTotal;

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public Double getSolarTotal() { return solarTotal; }
    public void setSolarTotal(Double solarTotal) { this.solarTotal = solarTotal; }
    public Double getGeneratorTotal() { return generatorTotal; }
    public void setGeneratorTotal(Double generatorTotal) { this.generatorTotal = generatorTotal; }
    public Double getGasTotal() { return gasTotal; }
    public void setGasTotal(Double gasTotal) { this.gasTotal = gasTotal; }
}
