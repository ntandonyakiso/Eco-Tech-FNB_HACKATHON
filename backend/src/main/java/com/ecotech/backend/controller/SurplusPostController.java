package com.ecotech.backend.controller;

import com.ecotech.backend.dto.SurplusPostDto;
import com.ecotech.backend.dto.SurplusPostRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.SurplusPostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surplus-posts")
public class SurplusPostController {

    private final SurplusPostService service;

    public SurplusPostController(SurplusPostService service) {
        this.service = service;
    }

    @GetMapping
    public List<SurplusPostDto> listActive() {
        return service.listActive();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public SurplusPostDto create(@CurrentUser UUID shopId, @Valid @RequestBody SurplusPostRequest req) {
        return service.createPost(shopId, req);
    }

    @PostMapping("/{id}/claim")
    @PreAuthorize("isAuthenticated()")
    public SurplusPostDto claim(@CurrentUser UUID userId, @PathVariable UUID id) {
        return service.claim(id, userId);
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public List<SurplusPostDto> myPosts(@CurrentUser UUID shopId) {
        return service.myPosts(shopId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@CurrentUser UUID shopId, @PathVariable UUID id) {
        service.deletePost(id, shopId);
        return ResponseEntity.noContent().build();
    }
}
