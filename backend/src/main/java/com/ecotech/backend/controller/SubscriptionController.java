package com.ecotech.backend.controller;

import com.ecotech.backend.dto.SubscriptionRequest;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    public SubscriptionController(SubscriptionService subscriptionService, UserRepository userRepository) {
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> setSubscription(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody SubscriptionRequest request) {
        UUID userId = currentUserId(userDetails);
        subscriptionService.setSubscription(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> unsubscribe(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam String channel,
                                         @RequestParam String alertType) {
        UUID userId = currentUserId(userDetails);
        SubscriptionRequest req = new SubscriptionRequest();
        req.setChannel(channel);
        req.setAlertType(alertType);
        req.setEnabled(false);
        subscriptionService.setSubscription(userId, req);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> listSubscriptions(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = currentUserId(userDetails);
        List<String> subs = subscriptionService.listUserSubscriptions(userId);
        return ResponseEntity.ok(subs);
    }

    private UUID currentUserId(UserDetails userDetails) {
        com.ecotech.backend.domain.User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
