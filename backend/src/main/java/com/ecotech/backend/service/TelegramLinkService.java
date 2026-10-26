package com.ecotech.backend.service;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.TelegramLink;
import com.ecotech.backend.dto.TelegramLinkRequest;
import com.ecotech.backend.repository.TelegramLinkRepository;
import com.ecotech.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TelegramLinkService {

    private final TelegramLinkRepository telegramLinkRepository;
    private final UserRepository userRepository;

    public TelegramLinkService(TelegramLinkRepository telegramLinkRepository, UserRepository userRepository) {
        this.telegramLinkRepository = telegramLinkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void link(UUID userId, TelegramLinkRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        telegramLinkRepository.findByChatId(request.getChatId())
                .ifPresent(existing -> telegramLinkRepository.delete(existing));

        TelegramLink link = new TelegramLink();
        link.setUserId(userId);
        link.setChatId(request.getChatId());
        telegramLinkRepository.save(link);
    }

    @Transactional(readOnly = true)
    public String findChatIdByUserId(UUID userId) {
        return telegramLinkRepository.findByUserId(userId)
                .map(TelegramLink::getChatId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public UUID findUserIdByChatId(String chatId) {
        return telegramLinkRepository.findByChatId(chatId)
                .map(TelegramLink::getUserId)
                .orElse(null);
    }
}
