package com.ecotech.backend.controller;

import com.ecotech.backend.dto.VulnerableContactDto;
import com.ecotech.backend.dto.VulnerableContactRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.VulnerableContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vulnerable-contacts")
public class VulnerableContactController {

    private final VulnerableContactService service;

    public VulnerableContactController(VulnerableContactService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public VulnerableContactDto create(@CurrentUser UUID userId,
                                       @Valid @RequestBody VulnerableContactRequest req) {
        return service.create(userId, req);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<VulnerableContactDto> list(@CurrentUser UUID userId) {
        return service.list(userId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@CurrentUser UUID userId, @PathVariable UUID id) {
        service.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ward/{wardId}")
    @PreAuthorize("isAuthenticated()")
    public List<VulnerableContactDto> byWard(@PathVariable UUID wardId) {
        return service.listByWard(wardId);
    }
}
