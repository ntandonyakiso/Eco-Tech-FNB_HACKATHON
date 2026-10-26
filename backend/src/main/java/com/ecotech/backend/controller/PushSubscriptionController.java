package com.ecotech.backend.controller;

import com.ecotech.backend.dto.PushSubscriptionRequest;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.push.WebPushService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/push-subscribe")
public class PushSubscriptionController {

    private final WebPushService pushService;
    private final UserRepository userRepository;

    public PushSubscriptionController(WebPushService pushService, UserRepository userRepository) {
        this.pushService = pushService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> save(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody PushSubscriptionRequest request) {
        UUID userId = currentUserId(userDetails);
        pushService.saveSubscription(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam String endpoint) {
        UUID userId = currentUserId(userDetails);
        pushService.removeSubscription(userId, endpoint);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vapid-public-key")
    public ResponseEntity<?> vapidPublicKey() {
        return ResponseEntity.ok(java.util.Map.of("publicKey", pushService.getVapidPublicKey()));
    }

    private UUID currentUserId(UserDetails userDetails) {
        com.ecotech.backend.domain.User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
