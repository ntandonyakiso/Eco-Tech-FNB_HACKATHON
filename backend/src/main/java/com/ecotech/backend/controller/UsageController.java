package com.ecotech.backend.controller;

import com.ecotech.backend.dto.UsageLogDto;
import com.ecotech.backend.dto.UsageLogRequest;
import com.ecotech.backend.dto.UsageSummary;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.UsageService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

    private final UsageService service;

    public UsageController(UsageService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public UsageLogDto log(@CurrentUser UUID userId, @Valid @RequestBody UsageLogRequest req) {
        return service.logUsage(userId, req);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<UsageLogDto> list(@CurrentUser UUID userId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        return service.listUsage(userId, page, size);
    }

    @GetMapping("/summary")
    @PreAuthorize("isAuthenticated()")
    public UsageSummary summary(@CurrentUser UUID userId) {
        return service.weeklySummary(userId);
    }
}
