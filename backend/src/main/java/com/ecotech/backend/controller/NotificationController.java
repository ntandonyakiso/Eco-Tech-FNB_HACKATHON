package com.ecotech.backend.controller;

import com.ecotech.backend.repository.NotificationRepository;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationController(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<?>> list(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = currentUserId(userDetails);
        List<?> notifications = notificationRepository.findByUserIdOrderBySentAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }

    private UUID currentUserId(UserDetails userDetails) {
        com.ecotech.backend.domain.User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
