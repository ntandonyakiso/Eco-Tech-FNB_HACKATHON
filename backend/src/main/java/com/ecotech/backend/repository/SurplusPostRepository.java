package com.ecotech.backend.repository;

import com.ecotech.backend.domain.SurplusPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SurplusPostRepository extends JpaRepository<SurplusPost, UUID>, JpaSpecificationExecutor<SurplusPost> {

    List<SurplusPost> findByIsClaimedFalseAndExpiresAtAfter(boolean claimed, Instant now);

    List<SurplusPost> findByShopId(UUID shopId);

    boolean existsByIdAndShopId(UUID id, UUID shopId);
}
