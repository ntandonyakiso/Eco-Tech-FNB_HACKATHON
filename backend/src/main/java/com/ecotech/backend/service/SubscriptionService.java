package com.ecotech.backend.service;

import com.ecotech.backend.domain.Subscription;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.SubscriptionRequest;
import com.ecotech.backend.repository.SubscriptionRepository;
import com.ecotech.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void setSubscription(UUID userId, SubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Subscription.Channel channel = Subscription.Channel.valueOf(request.getChannel());
        Subscription.AlertType alertType = Subscription.AlertType.valueOf(request.getAlertType());

        subscriptionRepository.findByUserIdAndChannelAndAlertType(userId, channel, alertType)
                .ifPresentOrElse(
                        sub -> sub.setEnabled(request.isEnabled()),
                        () -> {
                            Subscription sub = new Subscription();
                            sub.setUserId(userId);
                            sub.setChannel(channel);
                            sub.setAlertType(alertType);
                            sub.setEnabled(request.isEnabled());
                            subscriptionRepository.save(sub);
                        });
    }

    @Transactional(readOnly = true)
    public List<String> listUserSubscriptions(UUID userId) {
        return subscriptionRepository.findByUserIdAndEnabledTrue(userId).stream()
                .map(s -> s.getChannel() + ":" + s.getAlertType())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getEnabledChannelNames(UUID userId) {
        return subscriptionRepository.findByUserIdAndEnabledTrue(userId).stream()
                .map(s -> s.getChannel().name())
                .distinct()
                .collect(Collectors.toList());
    }
}
