package com.ecotech.backend.notification;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.TelegramLink;
import com.ecotech.backend.repository.TelegramLinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component("telegram")
public class TelegramNotificationChannel implements NotificationChannel {

    private final RestClient restClient;
    private final String botToken;
    private final TelegramLinkRepository linkRepository;

    public TelegramNotificationChannel(@Value("${app.telegram.bot-token:#{null}}") String botToken,
                                       TelegramLinkRepository linkRepository) {
        this.botToken = botToken;
        this.linkRepository = linkRepository;
        this.restClient = RestClient.create();
    }

    @Override
    public String channelName() {
        return "telegram";
    }

    @Override
    public void send(User user, String title, String body) {
        if (botToken == null) {
            return;
        }
        String chatId = linkRepository.findByUserId(user.getId())
                .map(TelegramLink::getChatId)
                .orElse(null);
        if (chatId == null) {
            return;
        }
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        try {
            restClient.post()
                    .uri(url)
                    .body(Map.of("chat_id", chatId, "text", title + "\n" + body))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            System.err.println("Telegram send failed: " + e.getMessage());
        }
    }
}
