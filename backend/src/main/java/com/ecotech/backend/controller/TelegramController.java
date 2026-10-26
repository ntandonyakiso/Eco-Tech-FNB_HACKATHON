package com.ecotech.backend.controller;

import com.ecotech.backend.dto.TelegramLinkRequest;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.security.JwtService;
import com.ecotech.backend.service.TelegramLinkService;
import com.ecotech.backend.telegram.TelegramBot;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/telegram")
public class TelegramController {

    private final TelegramLinkService telegramLinkService;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelegramController(TelegramLinkService telegramLinkService, UserRepository userRepository, TelegramBot telegramBot) {
        this.telegramLinkService = telegramLinkService;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    @PostMapping("/link")
    public ResponseEntity<?> link(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody TelegramLinkRequest request) {
        UUID userId = currentUserId(userDetails);
        telegramLinkService.link(userId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody String body) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> update = objectMapper.readValue(body, Map.class);
            telegramBot.processUpdate(update);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid update");
        }
        return ResponseEntity.ok().build();
    }

    private UUID currentUserId(UserDetails userDetails) {
        com.ecotech.backend.domain.User user = userRepository.findByEmail(userDetails.getUsername())
                .or(() -> userRepository.findByPhone(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
