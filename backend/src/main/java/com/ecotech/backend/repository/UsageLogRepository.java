package com.ecotech.backend.repository;

import com.ecotech.backend.domain.UsageLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UsageLogRepository extends JpaRepository<UsageLog, UUID>, JpaSpecificationExecutor<UsageLog> {

    List<UsageLog> findByUserIdOrderByRecordedAtDesc(UUID userId);

    Page<UsageLog> findByUserId(UUID userId, Pageable pageable);

    List<UsageLog> findByUserIdAndTypeAndRecordedAtBetween(UUID userId, UsageLog.Type type, Instant start, Instant end);
}
