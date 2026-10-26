package com.ecotech.backend.repository;

import com.ecotech.backend.domain.WardAnnouncement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WardAnnouncementRepository extends JpaRepository<WardAnnouncement, UUID>, JpaSpecificationExecutor<WardAnnouncement> {

    Page<WardAnnouncement> findByWardIdAndIsActiveTrueAndExpiresAtAfterOrderByCreatedAtDesc(
            UUID wardId, Instant now, Pageable pageable);

    List<WardAnnouncement> findByWardIdOrderByCreatedAtDesc(UUID wardId);

    Optional<WardAnnouncement> findById(UUID id);

    boolean existsByIdAndAuthorId(UUID id, UUID authorId);

    @Query("SELECT a FROM WardAnnouncement a WHERE a.authorId = :authorId ORDER BY a.createdAt DESC")
    List<WardAnnouncement> findByAuthorIdOrderByCreatedAtDesc(@Param("authorId") UUID authorId);
}
