package com.ecotech.backend.controller;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.ErrorResponse;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.ReportRequest;
import com.ecotech.backend.dto.ReportResponse;
import com.ecotech.backend.dto.StatusChangeRequest;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ReportController(ReportService reportService, JwtService jwtService, UserRepository userRepository) {
        this.reportService = reportService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    private UUID currentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReport(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestPart("request") ReportRequest request,
                                          @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            UUID reporterId = currentUserId(userDetails);
            ReportResponse response = reportService.createReport(reporterId, request, photo);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/api/v1/reports"));
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReportResponse>> listReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String wardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID wardUuid = wardId != null ? UUID.fromString(wardId) : null;
        PageResponse<ReportResponse> response = reportService.listReports(status, category, wardUuid, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable String id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UUID reportId = UUID.fromString(id);
            UUID viewerId = userDetails != null ? currentUserId(userDetails) : null;
            ReportResponse response = reportService.getReport(reportId, viewerId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/api/v1/reports/" + id));
        }
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<?> upvoteReport(@PathVariable String id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UUID reportId = UUID.fromString(id);
            UUID userId = currentUserId(userDetails);
            reportService.upvoteReport(reportId, userId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/api/v1/reports/" + id + "/upvote"));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COUNCILLOR','ADMIN')")
    public ResponseEntity<?> changeStatus(@PathVariable String id,
                                          @AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody StatusChangeRequest request) {
        try {
            UUID reportId = UUID.fromString(id);
            User changer = userRepository.findByEmail(userDetails.getUsername())
                    .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            reportService.changeStatus(reportId, changer.getId().toString(), request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/api/v1/reports/" + id + "/status"));
        }
    }
}
