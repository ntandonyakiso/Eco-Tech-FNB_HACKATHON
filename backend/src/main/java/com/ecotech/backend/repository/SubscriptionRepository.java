package com.ecotech.backend.repository;

import com.ecotech.backend.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription> {

    List<Subscription> findByUserIdAndEnabledTrue(UUID userId);

    List<Subscription> findByAlertTypeAndEnabledTrue(Subscription.AlertType alertType);

    List<Subscription> findByChannelAndAlertTypeAndEnabledTrue(
            Subscription.Channel channel,
            Subscription.AlertType alertType);

    boolean existsByUserIdAndChannelAndAlertType(UUID userId, Subscription.Channel channel, Subscription.AlertType alertType);

    Optional<Subscription> findByUserIdAndChannelAndAlertType(UUID userId, Subscription.Channel channel, Subscription.AlertType alertType);
}
