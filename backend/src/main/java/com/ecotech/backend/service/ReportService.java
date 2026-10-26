package com.ecotech.backend.service;

import com.ecotech.backend.domain.FaultReport;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.ReportStatusHistory;
import com.ecotech.backend.domain.ReportVote;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.ReportRequest;
import com.ecotech.backend.dto.ReportResponse;
import com.ecotech.backend.dto.ReportStatusHistoryDto;
import com.ecotech.backend.dto.StatusChangeRequest;
import com.ecotech.backend.notification.NotificationService;
import com.ecotech.backend.repository.FaultReportRepository;
import com.ecotech.backend.repository.ReportStatusHistoryRepository;
import com.ecotech.backend.repository.ReportVoteRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.service.SubscriptionService;
import com.ecotech.backend.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final FaultReportRepository reportRepository;
    private final ReportStatusHistoryRepository historyRepository;
    private final ReportVoteRepository voteRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;
    private final StorageService storageService;

    public ReportService(FaultReportRepository reportRepository,
                         ReportStatusHistoryRepository historyRepository,
                         ReportVoteRepository voteRepository,
                         UserRepository userRepository,
                         NotificationService notificationService,
                         SubscriptionService subscriptionService,
                         StorageService storageService) {
        this.reportRepository = reportRepository;
        this.historyRepository = historyRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
        this.storageService = storageService;
    }

    @Transactional
    public ReportResponse createReport(UUID reporterId, ReportRequest request, MultipartFile photo) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));

        String photoUrl = null;
        if (photo != null && !photo.isEmpty()) {
            try {
                photoUrl = storageService.upload("reports/" + reporterId, photo);
            } catch (IOException e) {
                throw new IllegalStateException("Photo upload failed: " + e.getMessage());
            }
        }

        FaultReport report = new FaultReport();
        report.setReporterId(reporterId);
        report.setWardId(reporter.getWardId());
        report.setCategory(FaultReport.Category.valueOf(request.getCategory()));
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setPhotoUrl(photoUrl);
        report.setStatus(FaultReport.Status.OPEN);
        report = reportRepository.save(report);

        ReportStatusHistory history = new ReportStatusHistory();
        history.setReportId(report.getId());
        history.setOldStatus(null);
        history.setNewStatus("OPEN");
        history.setChangedBy(reporterId);
        history.setNote("Report created");
        historyRepository.save(history);

        return toResponse(report, List.of(), false);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReportResponse> listReports(String status, String category, UUID wardId,
                                                    Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 20,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<FaultReport> reports;
        if (status != null && category != null) {
            reports = reportRepository.findByCategoryAndStatus(
                    FaultReport.Category.valueOf(category),
                    FaultReport.Status.valueOf(status), pageable);
        } else if (status != null) {
            reports = reportRepository.findByStatus(FaultReport.Status.valueOf(status), pageable);
        } else if (category != null) {
            reports = reportRepository.findByCategory(FaultReport.Category.valueOf(category), pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }

        return toPageResponse(reports);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(UUID reportId, UUID viewerId) {
        FaultReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        List<ReportStatusHistory> history = historyRepository.findByReportIdOrderByChangedAtAsc(reportId);
        boolean userVoted = voteRepository.findByReportIdAndUserId(reportId, viewerId).isPresent();

        return toResponse(report,
                history.stream().map(this::toHistoryDto).collect(Collectors.toList()),
                userVoted);
    }

    @Transactional
    public void upvoteReport(UUID reportId, UUID userId) {
        FaultReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (voteRepository.findByReportIdAndUserId(reportId, userId).isPresent()) {
            throw new IllegalArgumentException("Already voted");
        }

        ReportVote vote = new ReportVote();
        vote.setReportId(reportId);
        vote.setUserId(userId);
        voteRepository.save(vote);

        report.setUpvoteCount(report.getUpvoteCount() + 1);
        reportRepository.save(report);
    }

    @Transactional
    public void changeStatus(UUID reportId, String changerId, StatusChangeRequest request) {
        FaultReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        FaultReport.Status oldStatus = report.getStatus();
        FaultReport.Status newStatus = FaultReport.Status.valueOf(request.getStatus());
        if (oldStatus == newStatus) {
            return;
        }

        report.setStatus(newStatus);
        reportRepository.save(report);

        ReportStatusHistory history = new ReportStatusHistory();
        history.setReportId(reportId);
        history.setOldStatus(oldStatus.name());
        history.setNewStatus(newStatus.name());
        history.setChangedBy(UUID.fromString(changerId));
        history.setNote(request.getNote());
        historyRepository.save(history);

        User reporter = userRepository.findById(report.getReporterId()).orElse(null);
        if (reporter != null) {
            List<String> channels = subscriptionService.getEnabledChannelNames(reporter.getId());
            if (channels.isEmpty()) {
                channels = List.of("console");
            }
            notificationService.notify(reporter, channels,
                    "Report status updated",
                    "Report \"" + report.getTitle() + "\" is now " + newStatus.name());
        }
    }

    private PageResponse<ReportResponse> toPageResponse(Page<FaultReport> reports) {
        List<ReportResponse> content = reports.getContent().stream()
                .map(r -> toResponse(r, List.of(), null))
                .collect(Collectors.toList());
        return new PageResponse<>(content, reports.getNumber(), reports.getSize(),
                reports.getTotalElements(), reports.getTotalPages(),
                reports.hasNext(), reports.hasPrevious());
    }

    private ReportResponse toResponse(FaultReport report,
                                      List<ReportStatusHistoryDto> history,
                                      Boolean userVoted) {
        ReportResponse resp = new ReportResponse();
        resp.setId(report.getId());
        resp.setReporterId(report.getReporterId());
        resp.setWardId(report.getWardId());
        resp.setCategory(report.getCategory().name());
        resp.setTitle(report.getTitle());
        resp.setDescription(report.getDescription());
        resp.setLatitude(report.getLatitude());
        resp.setLongitude(report.getLongitude());
        resp.setPhotoUrl(report.getPhotoUrl());
        resp.setStatus(report.getStatus().name());
        resp.setUpvoteCount(report.getUpvoteCount());
        resp.setCreatedAt(report.getCreatedAt());
        resp.setUpdatedAt(report.getUpdatedAt());
        resp.setStatusHistory(history);
        resp.setUserVoted(userVoted);
        return resp;
    }

    private ReportStatusHistoryDto toHistoryDto(ReportStatusHistory h) {
        ReportStatusHistoryDto dto = new ReportStatusHistoryDto();
        dto.setId(h.getId());
        dto.setOldStatus(h.getOldStatus());
        dto.setNewStatus(h.getNewStatus());
        dto.setChangedBy(h.getChangedBy());
        dto.setNote(h.getNote());
        dto.setChangedAt(h.getChangedAt());
        return dto;
    }
}
