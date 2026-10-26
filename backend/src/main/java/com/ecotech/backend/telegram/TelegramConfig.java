package com.ecotech.backend.telegram;

import com.ecotech.backend.repository.TelegramLinkRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.service.AlertService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramBot telegramBot(@Value("${app.telegram.bot-token:#{null}}") String botToken,
                                   TelegramLinkRepository linkRepository,
                                   UserRepository userRepository,
                                   AlertService alertService) {
        return new TelegramBot(botToken, linkRepository, userRepository, alertService);
    }
}
