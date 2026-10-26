package com.ecotech.backend.repository;

import com.ecotech.backend.domain.BusinessProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, UUID> {

    Optional<BusinessProfile> findByUserId(UUID userId);
}
