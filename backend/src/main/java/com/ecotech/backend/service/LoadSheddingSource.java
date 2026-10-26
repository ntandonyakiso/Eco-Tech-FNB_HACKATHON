package com.ecotech.backend.service;

import com.ecotech.backend.domain.Ward;
import com.ecotech.backend.dto.AlertDto;
import com.ecotech.backend.repository.WardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LoadSheddingSource implements AlertSource {

    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;
    private final WardRepository wardRepository;

    public LoadSheddingSource(WardRepository wardRepository,
                              @Value("${app.eskom.base-url:https://developer.sepush.co.za/business/2.0}") String baseUrl,
                              @Value("${app.eskom.api-key:#{null}}") String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.wardRepository = wardRepository;
        this.restClient = RestClient.create();
    }

    @Override
    public List<AlertDto> fetch() {
        if (apiKey == null) {
            return List.of();
        }
        List<AlertDto> alerts = new ArrayList<>();
        List<Ward> wards = wardRepository.findAll();
        for (Ward ward : wards) {
            try {
                Map<String, Object> data = restClient.get()
                        .uri(baseUrl + "/areas/{areaId}", ward.getName())
                        .header("token", apiKey)
                        .retrieve()
                        .body(Map.class);
                if (data == null) continue;
                Object events = data.get("events");
                if (!(events instanceof List)) continue;
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> eventList = (List<Map<String, Object>>) events;
                for (Map<String, Object> event : eventList) {
                    AlertDto a = new AlertDto();
                    a.setWardId(ward.getId());
                    a.setType("LOAD_SHEDDING");
                    a.setTitle("Load shedding: " + event.getOrDefault("stage", "unknown"));
                    a.setBody(String.valueOf(event.getOrDefault("description", "")));
                    a.setSource("eskom");
                    a.setSourceId("eskom:" + event.getOrDefault("id", ward.getId()));
                    a.setSeverity("medium");
                    a.setStartsAt(Instant.now());
                    a.setEndsAt(Instant.now().plusSeconds(86400));
                    a.setActive(true);
                    alerts.add(a);
                }
            } catch (Exception e) {
                System.err.println("Load shedding fetch failed for ward " + ward.getId() + ": " + e.getMessage());
            }
        }
        return alerts;
    }
}
