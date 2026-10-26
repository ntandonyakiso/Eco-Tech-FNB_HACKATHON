package com.ecotech.backend.repository;

import com.ecotech.backend.domain.ReportVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReportVoteRepository extends JpaRepository<ReportVote, UUID> {

    Optional<ReportVote> findByReportIdAndUserId(UUID reportId, UUID userId);

    void deleteByReportIdAndUserId(UUID reportId, UUID userId);
}
