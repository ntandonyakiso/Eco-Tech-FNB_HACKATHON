package com.ecotech.backend.controller;

import com.ecotech.backend.dto.PreparednessPlanDto;
import com.ecotech.backend.dto.PreparednessPlanRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.PreparednessService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/preparedness")
public class PreparednessController {

    private final PreparednessService service;

    public PreparednessController(PreparednessService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public PreparednessPlanDto get(@CurrentUser UUID userId) {
        return service.getPlan(userId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public PreparednessPlanDto save(@CurrentUser UUID userId, @Valid @RequestBody PreparednessPlanRequest req) {
        return service.savePlan(userId, req);
    }
}
