package com.ecotech.backend.repository;

import com.ecotech.backend.domain.StockLoss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface StockLossRepository extends JpaRepository<StockLoss, UUID>, JpaSpecificationExecutor<StockLoss> {

    List<StockLoss> findByUserIdOrderByRecordedAtDesc(UUID userId);

    Page<StockLoss> findByUserId(UUID userId, Pageable pageable);

    List<StockLoss> findByUserIdAndRecordedAtBetween(UUID userId, Instant start, Instant end);
}
