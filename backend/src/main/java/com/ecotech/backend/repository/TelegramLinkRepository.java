package com.ecotech.backend.repository;

import com.ecotech.backend.domain.TelegramLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TelegramLinkRepository extends JpaRepository<TelegramLink, UUID> {

    Optional<TelegramLink> findByChatId(String chatId);

    Optional<TelegramLink> findByUserId(UUID userId);
}
