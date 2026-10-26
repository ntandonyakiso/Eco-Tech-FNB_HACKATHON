package com.ecotech.backend.service;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.WardAnnouncement;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.WardAnnouncementRequest;
import com.ecotech.backend.dto.WardAnnouncementResponse;
import com.ecotech.backend.notification.NotificationService;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.repository.WardAnnouncementRepository;
import com.ecotech.backend.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WardAnnouncementService {

    private final WardAnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    public WardAnnouncementService(WardAnnouncementRepository announcementRepository,
                                   UserRepository userRepository,
                                   NotificationService notificationService,
                                   SubscriptionService subscriptionService) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
    }

    public WardAnnouncementResponse create(UUID authorId, WardAnnouncementRequest req) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        if (author.getWardId() == null) {
            throw new IllegalStateException("Author has no ward assigned");
        }
        if (req.getWardId() != null && !req.getWardId().equals(author.getWardId())) {
            throw new IllegalStateException("Cannot post to another ward");
        }

        WardAnnouncement.Type type = WardAnnouncement.Type.valueOf(req.getType() != null ? req.getType() : "GENERAL");

        WardAnnouncement a = new WardAnnouncement();
        a.setWardId(author.getWardId());
        a.setAuthorId(authorId);
        a.setTitle(req.getTitle());
        a.setBody(req.getBody());
        a.setType(type);
        if (req.getExpiresAt() != null && !req.getExpiresAt().isBlank()) {
            a.setExpiresAt(Instant.parse(req.getExpiresAt()));
        }
        a.setActive(true);
        a = announcementRepository.save(a);

        notifyWardResidents(a);
        return toResponse(a);
    }

    public PageResponse<WardAnnouncementResponse> list(UUID wardId, int page, int size) {
        Instant now = Instant.now();
        Page<WardAnnouncement> p = announcementRepository.findByWardIdAndIsActiveTrueAndExpiresAtAfterOrderByCreatedAtDesc(
                wardId, now, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<WardAnnouncementResponse> content = p.getContent().stream().map(this::toResponse).toList();
        PageResponse<WardAnnouncementResponse> resp = new PageResponse<>();
        resp.setContent(content);
        resp.setTotalPages(p.getTotalPages());
        resp.setTotalElements(p.getTotalElements());
        resp.setPage(p.getNumber());
        resp.setSize(p.getSize());
        resp.setHasNext(p.hasNext());
        resp.setHasPrevious(p.hasPrevious());
        return resp;
    }

    public WardAnnouncementResponse get(UUID id, UUID wardId) {
        WardAnnouncement a = announcementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Announcement not found"));
        if (!a.getWardId().equals(wardId)) {
            throw new SecurityException("Not in your ward");
        }
        return toResponse(a);
    }

    public void delete(UUID id, UUID authorId) {
        WardAnnouncement a = announcementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Announcement not found"));
        if (!a.getAuthorId().equals(authorId)) {
            throw new SecurityException("Not the author");
        }
        a.setActive(false);
        announcementRepository.save(a);
    }

    public List<WardAnnouncementResponse> listByAuthor(UUID authorId) {
        return announcementRepository.findByAuthorIdOrderByCreatedAtDesc(authorId).stream()
                .map(this::toResponse).toList();
    }

    private void notifyWardResidents(WardAnnouncement a) {
        List<User> residents = userRepository.findByWardId(a.getWardId()).stream()
                .filter(u -> u.getRole() == User.Role.RESIDENT)
                .toList();
        String title = "New announcement: " + a.getTitle();
        String body = a.getBody();
        for (User resident : residents) {
            List<String> channels = subscriptionService.getEnabledChannelNames(resident.getId());
            if (channels.isEmpty()) {
                channels = List.of("console");
            }
            notificationService.notify(resident, channels, title, body);
        }
    }

    private WardAnnouncementResponse toResponse(WardAnnouncement a) {
        WardAnnouncementResponse d = new WardAnnouncementResponse();
        d.setId(a.getId());
        d.setWardId(a.getWardId());
        d.setAuthorId(a.getAuthorId());
        d.setTitle(a.getTitle());
        d.setBody(a.getBody());
        d.setType(a.getType().name());
        d.setExpiresAt(a.getExpiresAt());
        d.setActive(a.isActive());
        d.setCreatedAt(a.getCreatedAt());
        return d;
    }
}
