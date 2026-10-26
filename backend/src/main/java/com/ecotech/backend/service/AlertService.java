package com.ecotech.backend.service;

import com.ecotech.backend.domain.Alert;
import com.ecotech.backend.domain.Notification;
import com.ecotech.backend.domain.Subscription;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.AlertDto;
import com.ecotech.backend.repository.AlertRepository;
import com.ecotech.backend.repository.NotificationRepository;
import com.ecotech.backend.repository.SubscriptionRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.notification.NotificationService;
import com.ecotech.backend.push.WebPushService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;
    private final WebPushService webPushService;
    private final WeatherAlertSource weatherSource;
    private final LoadSheddingSource loadSheddingSource;

    public AlertService(AlertRepository alertRepository,
                        SubscriptionRepository subscriptionRepository,
                        UserRepository userRepository,
                        NotificationRepository notificationRepository,
                        NotificationService notificationService,
                        SubscriptionService subscriptionService,
                        WebPushService webPushService,
                        WeatherAlertSource weatherSource,
                        LoadSheddingSource loadSheddingSource) {
        this.alertRepository = alertRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
        this.webPushService = webPushService;
        this.weatherSource = weatherSource;
        this.loadSheddingSource = loadSheddingSource;
    }

    @Scheduled(fixedDelayString = "${app.alerts.ingest-interval-ms:900000}")
    @Transactional
    public void ingestAlerts() {
        List<AlertDto> weather = weatherSource.fetch();
        List<AlertDto> loadshedding = loadSheddingSource.fetch();

        Instant now = Instant.now();
        for (AlertDto dto : weather) {
            saveAlertIfNew(dto, now);
        }
        for (AlertDto dto : loadshedding) {
            saveAlertIfNew(dto, now);
        }
    }

    @Transactional
    public void ingestAlertsManual() {
        ingestAlerts();
    }

    private void saveAlertIfNew(AlertDto dto, Instant now) {
        if (alertRepository.existsBySourceAndSourceId(dto.getSource(), dto.getSourceId())) {
            return;
        }
        Alert alert = new Alert();
        alert.setWardId(dto.getWardId());
        alert.setType(Alert.Type.valueOf(dto.getType()));
        alert.setTitle(dto.getTitle());
        alert.setBody(dto.getBody());
        alert.setSource(dto.getSource());
        alert.setSourceId(dto.getSourceId());
        alert.setSeverity(dto.getSeverity());
        alert.setStartsAt(dto.getStartsAt());
        alert.setEndsAt(dto.getEndsAt());
        alert.setActive(true);
        alert = alertRepository.save(alert);

        notifySubscribers(alert);
    }

    private void notifySubscribers(Alert alert) {
        List<Subscription> subs = subscriptionRepository
                .findByAlertTypeAndEnabledTrue(Subscription.AlertType.valueOf(alert.getType().name()));

        List<User> users = subs.stream()
                .map(s -> userRepository.findById(s.getUserId()).orElse(null))
                .filter(u -> u != null && u.getWardId() != null && u.getWardId().equals(alert.getWardId()))
                .collect(Collectors.toList());

        String title = alert.getTitle();
        String body = alert.getBody() != null ? alert.getBody() : "";

        for (User user : users) {
            List<String> channels = subscriptionService.getEnabledChannelNames(user.getId());
            if (channels.isEmpty()) {
                channels = List.of("console");
            }
            notificationService.notify(user, channels, title, body);
            if (channels.contains("WEB_PUSH") && webPushService.isConfigured()) {
                webPushService.sendToUser(user.getId(), title, body);
            }
            for (String channel : channels) {
                saveNotificationRecord(user.getId(), alert.getId(), channel);
            }
        }
    }

    private void saveNotificationRecord(UUID userId, UUID alertId, String channel) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setAlertId(alertId);
        n.setChannel(channel);
        n.setStatus(Notification.Status.SENT);
        notificationRepository.save(n);
    }

    @Transactional(readOnly = true)
    public List<AlertDto> listActiveForWard(UUID wardId) {
        Instant now = Instant.now();
        return alertRepository.findActiveByWard(wardId, now).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AlertDto toDto(Alert a) {
        AlertDto dto = new AlertDto();
        dto.setId(a.getId());
        dto.setWardId(a.getWardId());
        dto.setType(a.getType().name());
        dto.setTitle(a.getTitle());
        dto.setBody(a.getBody());
        dto.setSource(a.getSource());
        dto.setSeverity(a.getSeverity());
        dto.setStartsAt(a.getStartsAt());
        dto.setEndsAt(a.getEndsAt());
        dto.setActive(a.isActive());
        return dto;
    }
}
