package com.ecotech.backend.service;

import com.ecotech.backend.domain.FaultReport;
import com.ecotech.backend.domain.ReportStatusHistory;
import com.ecotech.backend.domain.ReportVote;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.ReportRequest;
import com.ecotech.backend.dto.ReportResponse;
import com.ecotech.backend.dto.StatusChangeRequest;
import com.ecotech.backend.notification.NotificationService;
import com.ecotech.backend.repository.FaultReportRepository;
import com.ecotech.backend.repository.ReportStatusHistoryRepository;
import com.ecotech.backend.repository.ReportVoteRepository;
import com.ecotech.backend.repository.SubscriptionRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.service.SubscriptionService;
import com.ecotech.backend.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private FaultReportRepository reportRepository;
    @Mock private ReportStatusHistoryRepository historyRepository;
    @Mock private ReportVoteRepository voteRepository;
    @Mock private UserRepository userRepository;
    @Mock private NotificationService notificationService;
    @Mock private SubscriptionService subscriptionService;
    @Mock private StorageService storageService;
    @Mock private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private ReportService reportService;

    private UUID reporterId;
    private UUID reportId;
    private User reporter;
    private FaultReport report;
    private ReportRequest request;

    @BeforeEach
    void setUp() {
        reporterId = UUID.randomUUID();
        reportId = UUID.randomUUID();
        reporter = new User();
        reporter.setId(reporterId);
        reporter.setWardId(UUID.randomUUID());

        report = new FaultReport();
        report.setId(reportId);
        report.setReporterId(reporterId);
        report.setWardId(reporter.getWardId());
        report.setCategory(FaultReport.Category.POTHOLE);
        report.setTitle("Test pothole");
        report.setDescription("A big pothole");
        report.setLatitude(-29.8587);
        report.setLongitude(31.0218);
        report.setStatus(FaultReport.Status.OPEN);
        report.setUpvoteCount(0);
        report.setCreatedAt(Instant.parse("2026-10-24T10:00:00Z"));

        request = new ReportRequest();
        request.setTitle("Test pothole");
        request.setDescription("A big pothole");
        request.setLatitude(-29.8587);
        request.setLongitude(31.0218);
        request.setCategory("POTHOLE");
        request.setWardId(null);
    }

    @Test
    void createReport_withoutPhoto_savesReportAndHistory() throws Exception {
        when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
        when(reportRepository.save(any(FaultReport.class))).thenAnswer(inv -> inv.getArgument(0));

        ReportResponse response = reportService.createReport(reporterId, request, null);

        assertThat(response.getTitle()).isEqualTo("Test pothole");
        assertThat(response.getStatus()).isEqualTo("OPEN");
        assertThat(response.getReporterId()).isEqualTo(reporterId);
        verify(reportRepository).save(any(FaultReport.class));
        verify(historyRepository).save(any(ReportStatusHistory.class));
        verify(storageService, never()).upload(any(), any());
    }

    @Test
    void createReport_withPhoto_uploadsPhotoAndSetsUrl() throws Exception {
        when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
        when(reportRepository.save(any(FaultReport.class))).thenAnswer(inv -> inv.getArgument(0));
        when(storageService.upload(eq("reports/" + reporterId), any())).thenReturn("http://storage/photo.jpg");

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", new byte[]{1, 2, 3});

        ReportResponse response = reportService.createReport(reporterId, request, photo);

        assertThat(response.getPhotoUrl()).isEqualTo("http://storage/photo.jpg");
        verify(storageService).upload("reports/" + reporterId, photo);
    }

    @Test
    void createReport_photoUploadFailure_throwsIllegalState() throws Exception {
        when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
        when(storageService.upload(any(), any()))
                .thenThrow(new java.io.IOException("disk full"));

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", new byte[]{1});

        assertThatThrownBy(() -> reportService.createReport(reporterId, request, photo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Photo upload failed");
    }

    @Test
    void createReport_reporterNotFound_throwsIllegalArgument() throws Exception {
        when(userRepository.findById(reporterId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.createReport(reporterId, request, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reporter not found");
    }

    @Test
    void changeStatus_validTransition_savesHistoryAndNotifies() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
        when(subscriptionService.getEnabledChannelNames(reporterId)).thenReturn(List.of("console"));
        when(reportRepository.save(any(FaultReport.class))).thenAnswer(inv -> inv.getArgument(0));

        StatusChangeRequest statusChange = new StatusChangeRequest();
        statusChange.setStatus("RESOLVED");
        statusChange.setNote("Fixed by crew");

        reportService.changeStatus(reportId, reporterId.toString(), statusChange);

        assertThat(report.getStatus()).isEqualTo(FaultReport.Status.RESOLVED);
        verify(historyRepository).save(any(ReportStatusHistory.class));
        verify(notificationService).notify(eq(reporter), (List<String>) any(), anyString(), anyString());
    }

    @Test
    void changeStatus_sameStatus_isNoOp() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        StatusChangeRequest statusChange = new StatusChangeRequest();
        statusChange.setStatus("OPEN");
        statusChange.setNote("no change");

        reportService.changeStatus(reportId, reporterId.toString(), statusChange);

        verify(historyRepository, never()).save(any());
        verify(notificationService, never()).notify(any(), (List<String>) any(), anyString(), anyString());
    }

    @Test
    void changeStatus_reportNotFound_throws() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        StatusChangeRequest statusChange = new StatusChangeRequest();
        statusChange.setStatus("RESOLVED");

        assertThatThrownBy(() -> reportService.changeStatus(reportId, reporterId.toString(), statusChange))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report not found");
    }

    @Test
    void upvoteReport_firstVote_incrementsCount() {
        report.setUpvoteCount(5);
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(voteRepository.findByReportIdAndUserId(reportId, reporterId)).thenReturn(Optional.empty());
        when(reportRepository.save(any(FaultReport.class))).thenAnswer(inv -> inv.getArgument(0));
        when(voteRepository.save(any(ReportVote.class))).thenAnswer(inv -> inv.getArgument(0));

        reportService.upvoteReport(reportId, reporterId);

        assertThat(report.getUpvoteCount()).isEqualTo(6);
        verify(voteRepository).save(any(ReportVote.class));
        verify(reportRepository).save(report);
    }

    @Test
    void upvoteReport_alreadyVoted_throws() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(voteRepository.findByReportIdAndUserId(reportId, reporterId)).thenReturn(Optional.of(new ReportVote()));

        assertThatThrownBy(() -> reportService.upvoteReport(reportId, reporterId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Already voted");
    }

    @Test
    void listReports_noFilters_returnsPage() {
        Page<FaultReport> page = new PageImpl<>(List.of(report), PageRequest.of(0, 20), 1);
        when(reportRepository.findAll(PageRequest.of(0, 20,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"))))
                .thenReturn(page);

        PageResponse<ReportResponse> response = reportService.listReports(null, null, null, 0, 20);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("Test pothole");
    }

    @Test
    void getReport_returnsHistoryAndVoteStatus() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(historyRepository.findByReportIdOrderByChangedAtAsc(reportId)).thenReturn(List.of());
        when(voteRepository.findByReportIdAndUserId(reportId, reporterId)).thenReturn(Optional.empty());

        ReportResponse response = reportService.getReport(reportId, reporterId);

        assertThat(response.getId()).isEqualTo(reportId);
        assertThat(response.getUserVoted()).isFalse();
    }

    @Test
    void getReport_reportNotFound_throws() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.getReport(reportId, reporterId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report not found");
    }
}
