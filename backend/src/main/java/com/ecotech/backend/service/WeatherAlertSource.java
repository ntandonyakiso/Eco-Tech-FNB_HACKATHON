package com.ecotech.backend.service;

import com.ecotech.backend.domain.Ward;
import com.ecotech.backend.dto.AlertDto;
import com.ecotech.backend.repository.WardRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WeatherAlertSource implements AlertSource {

    private final RestClient restClient;
    private final String baseUrl;
    private final WardRepository wardRepository;

    public WeatherAlertSource(WardRepository wardRepository,
            @org.springframework.beans.factory.annotation.Value("${app.open-meteo.base-url:https://api.open-meteo.com/v1}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.wardRepository = wardRepository;
        this.restClient = RestClient.create();
    }

    @Override
    public List<AlertDto> fetch() {
        List<AlertDto> alerts = new ArrayList<>();
        List<Ward> wards = wardRepository.findAll();
        for (Ward ward : wards) {
            if (ward.getCentroidLat() == null || ward.getCentroidLon() == null) {
                continue;
            }
            try {
                Map<String, Object> data = restClient.get()
                        .uri(baseUrl + "/forecast?latitude={lat}&longitude={lon}&current_weather=true&daily=weathercode&timezone=auto",
                                ward.getCentroidLat(), ward.getCentroidLon())
                        .retrieve()
                        .body(Map.class);
                if (data == null) continue;
                Object daily = data.get("daily");
                if (!(daily instanceof Map)) continue;
                @SuppressWarnings("unchecked")
                Map<String, Object> dailyMap = (Map<String, Object>) daily;
                List<Object> codes = (List<Object>) dailyMap.get("weathercode");
                List<Object> dates = (List<Object>) dailyMap.get("time");
                if (codes == null || dates == null || codes.isEmpty()) continue;
                Integer latestCode = ((Number) codes.get(codes.size() - 1)).intValue();
                if (isSevere(latestCode)) {
                    AlertDto a = new AlertDto();
                    a.setWardId(ward.getId());
                    a.setType("WEATHER");
                    a.setTitle("Severe weather warning for " + ward.getName());
                    a.setBody("Open-Meteo weather code " + latestCode + " detected for " + ward.getName());
                    a.setSource("open-meteo");
                    a.setSourceId("open-meteo:" + ward.getId() + ":" + dates.get(codes.size() - 1));
                    a.setSeverity("high");
                    a.setStartsAt(Instant.now());
                    a.setEndsAt(Instant.now().plusSeconds(86400));
                    a.setActive(true);
                    alerts.add(a);
                }
            } catch (Exception e) {
                System.err.println("Weather fetch failed for ward " + ward.getId() + ": " + e.getMessage());
            }
        }
        return alerts;
    }

    private boolean isSevere(int code) {
        return code >= 51 || code == 45 || code == 48 || code >= 56 || code >= 66 || code >= 80;
    }
}
