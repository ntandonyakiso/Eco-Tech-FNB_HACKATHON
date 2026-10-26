package com.ecotech.backend.telegram;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.repository.TelegramLinkRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.service.AlertService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TelegramBot {

    private final RestClient restClient;
    private final String botToken;
    private final TelegramLinkRepository linkRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;

    public TelegramBot(@Value("${app.telegram.bot-token:#{null}}") String botToken,
                       TelegramLinkRepository linkRepository,
                       UserRepository userRepository,
                       AlertService alertService) {
        this.botToken = botToken;
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.alertService = alertService;
        this.restClient = RestClient.create();
    }

    public String getBaseUrl() {
        return botToken != null ? "https://api.telegram.org/bot" + botToken : null;
    }

    public boolean registerWebhook(String publicUrl) {
        if (botToken == null || publicUrl == null) {
            return false;
        }
        try {
            restClient.post()
                    .uri(getBaseUrl() + "/setWebhook")
                    .body(Map.of("url", publicUrl + "/api/v1/telegram/webhook"))
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            System.err.println("Telegram webhook registration failed: " + e.getMessage());
            return false;
        }
    }

    public void processUpdate(Map<String, Object> update) {
        if (botToken == null) return;
        Object messageObj = update.get("message");
        if (!(messageObj instanceof Map)) return;
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) messageObj;
        Object chatObj = message.get("chat");
        if (!(chatObj instanceof Map)) return;
        @SuppressWarnings("unchecked")
        Map<String, Object> chat = (Map<String, Object>) chatObj;
        Object textObj = message.get("text");
        if (!(textObj instanceof String)) return;
        String text = (String) textObj;
        Object idObj = chat.get("id");
        String chatId = idObj != null ? idObj.toString() : null;

        if ("/start".equals(text) && chatId != null) {
            sendMessage(chatId, "Welcome to Eco-Tech. Send me your user ID to link this chat to your account.");
        } else if (text.startsWith("/link ") && chatId != null) {
            String userIdStr = text.substring(6).trim();
            try {
                UUID userId = UUID.fromString(userIdStr);
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    // Store link
                    com.ecotech.backend.domain.TelegramLink link = new com.ecotech.backend.domain.TelegramLink();
                    link.setUserId(userId);
                    link.setChatId(chatId);
                    linkRepository.save(link);
                    sendMessage(chatId, "Linked to account " + userId);
                } else {
                    sendMessage(chatId, "User not found.");
                }
            } catch (IllegalArgumentException e) {
                sendMessage(chatId, "Invalid user ID.");
            }
        } else if ("/alerts".equals(text) && chatId != null) {
            UUID userId = linkRepository.findByChatId(chatId)
                    .map(com.ecotech.backend.domain.TelegramLink::getUserId)
                    .orElse(null);
            if (userId == null) {
                sendMessage(chatId, "Please /link your account first.");
                return;
            }
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty() || userOpt.get().getWardId() == null) {
                sendMessage(chatId, "No ward assigned to your account.");
                return;
            }
            List<com.ecotech.backend.dto.AlertDto> alerts = alertService.listActiveForWard(userOpt.get().getWardId());
            if (alerts.isEmpty()) {
                sendMessage(chatId, "No active alerts for your ward.");
            } else {
                StringBuilder sb = new StringBuilder("Active alerts:\n");
                for (com.ecotech.backend.dto.AlertDto a : alerts) {
                    sb.append("• ").append(a.getTitle()).append("\n");
                }
                sendMessage(chatId, sb.toString());
            }
        } else if ("/report".equals(text)) {
            sendMessage(chatId, "Open the Eco-Tech app to submit a fault report.");
        } else {
            sendMessage(chatId, "Commands: /start, /link <userId>, /alerts, /report");
        }
    }

    private void sendMessage(String chatId, String text) {
        if (botToken == null) return;
        try {
            restClient.post()
                    .uri(getBaseUrl() + "/sendMessage")
                    .body(Map.of("chat_id", chatId, "text", text))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.err.println("Telegram send failed: " + e.getMessage());
        }
    }
}
