package com.ecotech.backend.service;

import com.ecotech.backend.domain.PushSubscription;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.PushSubscriptionRequest;
import com.ecotech.backend.repository.PushSubscriptionRepository;
import com.ecotech.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PushSubscriptionService {

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final UserRepository userRepository;

    public PushSubscriptionService(PushSubscriptionRepository pushSubscriptionRepository, UserRepository userRepository) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveSubscription(UUID userId, PushSubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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

    @Transactional
    public void removeSubscription(UUID userId, String endpoint) {
        pushSubscriptionRepository.findByUserIdAndEndpoint(userId, endpoint)
                .ifPresent(pushSubscriptionRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<PushSubscription> findAll() {
        return (List<PushSubscription>) pushSubscriptionRepository.findAll();
    }
}
