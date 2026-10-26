package com.ecotech.backend.service;

import com.ecotech.backend.domain.SurplusPost;
import com.ecotech.backend.dto.SurplusPostDto;
import com.ecotech.backend.dto.SurplusPostRequest;
import com.ecotech.backend.repository.SurplusPostRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SurplusPostService {

    private final SurplusPostRepository repository;
    private final UserRepository userRepository;

    public SurplusPostService(SurplusPostRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public SurplusPostDto createPost(UUID shopId, SurplusPostRequest req) {
        userRepository.findById(shopId).orElseThrow(() -> new EntityNotFoundException("Shop not found"));
        SurplusPost p = new SurplusPost();
        p.setShopId(shopId);
        p.setTitle(req.getTitle());
        p.setDescription(req.getDescription());
        p.setPickupLocation(req.getPickupLocation());
        p.setPickupStart(req.getPickupStart());
        p.setPickupEnd(req.getPickupEnd());
        p.setExpiresAt(req.getExpiresAt());
        p.setClaimed(false);
        p = repository.save(p);
        return toDto(p);
    }

    public List<SurplusPostDto> listActive() {
        Instant now = Instant.now();
        return repository.findByIsClaimedFalseAndExpiresAtAfter(false, now).stream()
                .map(this::toDto).toList();
    }

    public List<SurplusPostDto> myPosts(UUID shopId) {
        return repository.findByShopId(shopId).stream().map(this::toDto).toList();
    }

    public SurplusPostDto claim(UUID id, UUID userId) {
        SurplusPost p = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Surplus post not found"));
        if (p.isClaimed()) {
            throw new IllegalStateException("Already claimed");
        }
        if (p.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Expired");
        }
        p.setClaimed(true);
        p.setClaimedBy(userId);
        p = repository.save(p);
        return toDto(p);
    }

    public void deletePost(UUID id, UUID shopId) {
        SurplusPost p = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Surplus post not found"));
        if (!p.getShopId().equals(shopId)) {
            throw new SecurityException("Not your post");
        }
        repository.delete(p);
    }

    private SurplusPostDto toDto(SurplusPost p) {
        SurplusPostDto d = new SurplusPostDto();
        d.setId(p.getId());
        d.setShopId(p.getShopId());
        d.setTitle(p.getTitle());
        d.setDescription(p.getDescription());
        d.setPickupLocation(p.getPickupLocation());
        d.setPickupStart(p.getPickupStart());
        d.setPickupEnd(p.getPickupEnd());
        d.setExpiresAt(p.getExpiresAt());
        d.setClaimed(p.isClaimed());
        d.setClaimedBy(p.getClaimedBy());
        d.setCreatedAt(p.getCreatedAt());
        return d;
    }
}
