package com.ecotech.backend.controller;

import com.ecotech.backend.dto.OutageCostRequest;
import com.ecotech.backend.dto.OutageCostResponse;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.BusinessService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessService service;

    public BusinessController(BusinessService service) {
        this.service = service;
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public com.ecotech.backend.dto.BusinessProfileDto upsert(@CurrentUser UUID userId,
                                                             @Valid @RequestBody com.ecotech.backend.dto.BusinessProfileRequest req) {
        return service.upsertProfile(userId, req);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public com.ecotech.backend.dto.BusinessProfileDto get(@CurrentUser UUID userId) {
        return service.getProfile(userId);
    }

    @PostMapping("/outage-cost")
    @PreAuthorize("isAuthenticated()")
    public OutageCostResponse outageCost(@CurrentUser UUID userId,
                                         @Valid @RequestBody OutageCostRequest req) {
        double hours = req.getHours() != null ? req.getHours() : 0;
        double rate = req.getAverageHourlyRevenue() != null ? req.getAverageHourlyRevenue() : 0;
        OutageCostResponse r = new OutageCostResponse();
        r.setHours(hours);
        r.setAverageHourlyRevenue(rate);
        r.setTotalCost(hours * rate);
        return r;
    }
}
