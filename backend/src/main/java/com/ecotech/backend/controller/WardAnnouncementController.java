package com.ecotech.backend.controller;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.WardAnnouncementRequest;
import com.ecotech.backend.dto.WardAnnouncementResponse;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.WardAnnouncementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/announcements")
public class WardAnnouncementController {

    private final WardAnnouncementService service;

    public WardAnnouncementController(WardAnnouncementService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COUNCILLOR','ADMIN')")
    public WardAnnouncementResponse create(@CurrentUser UUID authorId,
                                           @Valid @RequestBody WardAnnouncementRequest req) {
        return service.create(authorId, req);
    }

    @GetMapping("/ward/{wardId}")
    public PageResponse<WardAnnouncementResponse> listByWard(@PathVariable UUID wardId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        return service.list(wardId, page, size);
    }

    @GetMapping("/{id}")
    public WardAnnouncementResponse get(@PathVariable UUID id,
                                        @RequestParam UUID wardId) {
        return service.get(id, wardId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COUNCILLOR','ADMIN')")
    public ResponseEntity<Void> delete(@CurrentUser UUID authorId,
                                       @PathVariable UUID id) {
        service.delete(id, authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('COUNCILLOR','ADMIN')")
    public List<WardAnnouncementResponse> myAnnouncements(@CurrentUser UUID authorId) {
        return service.listByAuthor(authorId);
    }
}
