package com.ecotech.backend.controller;

import com.ecotech.backend.dto.FoodPointDto;
import com.ecotech.backend.dto.FoodPointRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.FoodPointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/food-points")
public class FoodPointController {

    private final FoodPointService service;

    public FoodPointController(FoodPointService service) {
        this.service = service;
    }

    @GetMapping
    public List<FoodPointDto> listApproved() {
        return service.listApproved();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public FoodPointDto add(@CurrentUser UUID userId, @Valid @RequestBody FoodPointRequest req) {
        return service.addFoodPoint(userId, req);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public FoodPointDto approve(@PathVariable UUID id) {
        return service.approve(id);
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<FoodPointDto> mine(@CurrentUser UUID userId) {
        return service.myFoodPoints(userId);
    }
}
