package com.ecotech.backend.repository;

import com.ecotech.backend.domain.FaultReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FaultReportRepository extends JpaRepository<FaultReport, UUID>, JpaSpecificationExecutor<FaultReport> {

    List<FaultReport> findByWardId(UUID wardId);

    Page<FaultReport> findByCategory(com.ecotech.backend.domain.FaultReport.Category category, Pageable pageable);

    @Query("SELECT r FROM FaultReport r WHERE r.status = :status ORDER BY r.createdAt DESC")
    Page<FaultReport> findByStatus(@Param("status") com.ecotech.backend.domain.FaultReport.Status status, Pageable pageable);

    @Query("SELECT r FROM FaultReport r WHERE r.category = :category AND r.status = :status ORDER BY r.createdAt DESC")
    Page<FaultReport> findByCategoryAndStatus(
            @Param("category") com.ecotech.backend.domain.FaultReport.Category category,
            @Param("status") com.ecotech.backend.domain.FaultReport.Status status,
            Pageable pageable);

    boolean existsByIdAndReporterId(UUID id, UUID reporterId);

    Optional<FaultReport> findById(UUID id);
}
