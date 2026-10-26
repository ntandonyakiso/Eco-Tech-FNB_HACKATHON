package com.ecotech.backend.repository;

import com.ecotech.backend.domain.ReportStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ReportStatusHistoryRepository extends JpaRepository<ReportStatusHistory, UUID> {

    List<ReportStatusHistory> findByReportIdOrderByChangedAtAsc(UUID reportId);

    Page<ReportStatusHistory> findByReportId(UUID reportId, Pageable pageable);
}
