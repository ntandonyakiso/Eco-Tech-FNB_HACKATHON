package com.ecotech.backend.repository;

import com.ecotech.backend.domain.PreparednessPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PreparednessPlanRepository extends JpaRepository<PreparednessPlan, UUID> {

    Optional<PreparednessPlan> findByUserId(UUID userId);
}
