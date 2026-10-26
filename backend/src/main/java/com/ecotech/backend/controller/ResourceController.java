package com.ecotech.backend.controller;

import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.ResourceListingDto;
import com.ecotech.backend.dto.ResourceListingRequest;
import com.ecotech.backend.dto.ResourceRequestDto;
import com.ecotech.backend.dto.ResourceRequestRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<ResourceListingDto> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.listListings(category, lat, lon, radiusKm, page, size);
    }

    @GetMapping("/{id}")
    public ResourceListingDto get(@PathVariable UUID id) {
        return service.getListing(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResourceListingDto create(@CurrentUser UUID userId,
                                     @Valid @RequestBody ResourceListingRequest req) {
        return service.createListing(userId, req);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResourceListingDto update(@CurrentUser UUID userId, @PathVariable UUID id,
                                     @Valid @RequestBody ResourceListingRequest req) {
        return service.updateListing(id, userId, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@CurrentUser UUID userId, @PathVariable UUID id) {
        service.deleteListing(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-listings")
    @PreAuthorize("isAuthenticated()")
    public java.util.List<ResourceListingDto> myListings(@CurrentUser UUID userId) {
        return service.myListings(userId);
    }

    @PostMapping("/{id}/requests")
    @PreAuthorize("isAuthenticated()")
    public ResourceRequestDto request(@CurrentUser UUID userId, @PathVariable UUID id,
                                      @Valid @RequestBody ResourceRequestRequest req) {
        req.setListingId(id);
        return service.requestResource(userId, req);
    }

    @PatchMapping("/requests/{requestId}")
    @PreAuthorize("isAuthenticated()")
    public ResourceRequestDto respond(@CurrentUser UUID userId, @PathVariable UUID requestId,
                                      @RequestParam com.ecotech.backend.domain.ResourceRequest.Status status) {
        return service.respondToRequest(requestId, userId, status);
    }

    @GetMapping("/requests/my-requests")
    @PreAuthorize("isAuthenticated()")
    public java.util.List<ResourceRequestDto> myRequests(@CurrentUser UUID userId) {
        return service.myRequests(userId);
    }

    @GetMapping("/{listingId}/requests")
    @PreAuthorize("isAuthenticated()")
    public java.util.List<ResourceRequestDto> requestsForListing(@CurrentUser UUID userId,
                                                                 @PathVariable UUID listingId) {
        return service.requestsForListing(listingId, userId);
    }
}
