package com.ecotech.backend.controller;

import com.ecotech.backend.dto.AlertDto;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.service.AlertService;
import com.ecotech.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;
    private final UserRepository userRepository;

    public AlertController(AlertService alertService, UserRepository userRepository) {
        this.alertService = alertService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AlertDto>> listActive(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = currentUserId(userDetails);
        com.ecotech.backend.domain.User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<AlertDto> alerts = alertService.listActiveForWard(user.getWardId());
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/ingest")
    public ResponseEntity<?> ingest() {
        alertService.ingestAlertsManual();
        return ResponseEntity.ok().build();
    }

    private UUID currentUserId(UserDetails userDetails) {
        com.ecotech.backend.domain.User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
