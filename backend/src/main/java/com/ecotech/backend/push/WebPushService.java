package com.ecotech.backend.push;

import com.ecotech.backend.domain.PushSubscription;
import com.ecotech.backend.repository.PushSubscriptionRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.dto.PushSubscriptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
public class WebPushService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final UserRepository userRepository;
    private final RestClient restClient;
    private final String vapidPublicKey;
    private final String vapidPrivateKey;
    private final String vapidSubject;

    public WebPushService(PushSubscriptionRepository pushSubscriptionRepository,
                          UserRepository userRepository,
                          @Value("${app.vapid.public-key:#{null}}") String vapidPublicKey,
                          @Value("${app.vapid.private-key:#{null}}") String vapidPrivateKey,
                          @Value("${app.vapid.subject:#{null}}") String vapidSubject) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
        this.userRepository = userRepository;
        this.restClient = RestClient.create();
        this.vapidPublicKey = vapidPublicKey;
        this.vapidPrivateKey = vapidPrivateKey;
        this.vapidSubject = vapidSubject;
    }

    public boolean isConfigured() {
        return vapidPublicKey != null && vapidPrivateKey != null;
    }

    public String getVapidPublicKey() {
        return vapidPublicKey;
    }

    public void saveSubscription(UUID userId, PushSubscriptionRequest request) {
        pushSubscriptionRepository.findByUserIdAndEndpoint(userId, request.getEndpoint())
                .ifPresentOrElse(
                        existing -> {
                            existing.setP256dhKey(request.getP256dhKey());
                            existing.setAuthKey(request.getAuthKey());
                        },
                        () -> {
                            PushSubscription sub = new PushSubscription();
                            sub.setUserId(userId);
                            sub.setEndpoint(request.getEndpoint());
                            sub.setP256dhKey(request.getP256dhKey());
                            sub.setAuthKey(request.getAuthKey());
                            pushSubscriptionRepository.save(sub);
                        });
    }

    public void removeSubscription(UUID userId, String endpoint) {
        pushSubscriptionRepository.findByUserIdAndEndpoint(userId, endpoint)
                .ifPresent(pushSubscriptionRepository::delete);
    }

    public void sendToUser(UUID userId, String title, String body) {
        if (!isConfigured()) {
            System.out.println("[WEB PUSH not configured] to=" + userId + " | " + title + " | " + body);
            return;
        }
        List<PushSubscription> subs = pushSubscriptionRepository.findByUserId(userId);
        for (PushSubscription sub : subs) {
            try {
                restClient.post()
                        .uri(sub.getEndpoint())
                        .body(title + "\n" + body)
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                System.err.println("Web push failed: " + e.getMessage());
            }
        }
    }
}
