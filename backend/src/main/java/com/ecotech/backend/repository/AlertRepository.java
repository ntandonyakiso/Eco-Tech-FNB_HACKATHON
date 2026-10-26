package com.ecotech.backend.repository;

import com.ecotech.backend.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID>, JpaSpecificationExecutor<Alert> {

    List<Alert> findByWardIdAndIsActiveTrue(UUID wardId);

    @Query("SELECT a FROM Alert a WHERE a.wardId = :wardId AND a.isActive = true AND a.endsAt > :now")
    List<Alert> findActiveByWard(@Param("wardId") UUID wardId, @Param("now") Instant now);

    boolean existsBySourceAndSourceId(String source, String sourceId);
}
