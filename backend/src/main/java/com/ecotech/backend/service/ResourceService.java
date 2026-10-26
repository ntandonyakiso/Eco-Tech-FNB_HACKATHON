package com.ecotech.backend.service;

import com.ecotech.backend.domain.ResourceListing;
import com.ecotech.backend.domain.ResourceRequest;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.dto.PageResponse;
import com.ecotech.backend.dto.ResourceListingDto;
import com.ecotech.backend.dto.ResourceListingRequest;
import com.ecotech.backend.dto.ResourceRequestDto;
import com.ecotech.backend.dto.ResourceRequestRequest;
import com.ecotech.backend.repository.ResourceListingRepository;
import com.ecotech.backend.repository.ResourceRequestRepository;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.notification.NotificationService;
import com.ecotech.backend.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import com.ecotech.backend.domain.ResourceListing.Category;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ResourceService {

    private final ResourceListingRepository listingRepository;
    private final ResourceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    public ResourceService(ResourceListingRepository listingRepository,
                           ResourceRequestRepository requestRepository,
                           UserRepository userRepository,
                           NotificationService notificationService,
                           SubscriptionService subscriptionService) {
        this.listingRepository = listingRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
    }

    public PageResponse<ResourceListingDto> listListings(String category, Double lat, Double lon,
                                                         Double radiusKm, int page, int size) {
        Category cat = category == null ? null : Category.valueOf(category);
        Page<ResourceListing> p;
        if (cat != null) {
            p = listingRepository.findByIsActiveTrueAndCategory(
                    org.springframework.data.domain.PageRequest.of(page, size), true, cat);
        } else {
            p = listingRepository.findByIsActiveTrue(
                    org.springframework.data.domain.PageRequest.of(page, size), true);
        }
        List<ResourceListingDto> dtos = p.getContent().stream()
                .map(l -> toDto(l, lat, lon))
                .toList();
        PageResponse<ResourceListingDto> resp = new PageResponse<>();
        resp.setContent(dtos);
        resp.setTotalPages(p.getTotalPages());
        resp.setTotalElements(p.getTotalElements());
        resp.setPage(p.getNumber());
        resp.setSize(p.getSize());
        resp.setHasNext(p.hasNext());
        return resp;
    }

    public ResourceListingDto getListing(UUID id) {
        ResourceListing l = listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        return toDto(l, null, null);
    }

    public ResourceListingDto createListing(UUID ownerId, ResourceListingRequest req) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
        ResourceListing l = new ResourceListing();
        l.setOwnerId(ownerId);
        l.setWardId(req.getWardId());
        l.setCategory(domainCategory(req.getCategory()));
        l.setTitle(req.getTitle());
        l.setDescription(req.getDescription());
        l.setLatitude(req.getLatitude());
        l.setLongitude(req.getLongitude());
        l.setAvailabilityStart(req.getAvailabilityStart());
        l.setAvailabilityEnd(req.getAvailabilityEnd());
        l.setContactMethod(req.getContactMethod());
        l.setContactValue(req.getContactValue());
        l.setActive(true);
        l = listingRepository.save(l);
        return toDto(l, null, null);
    }

    public ResourceListingDto updateListing(UUID id, UUID ownerId, ResourceListingRequest req) {
        ResourceListing l = listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!l.getOwnerId().equals(ownerId)) {
            throw new SecurityException("Not the owner");
        }
        l.setCategory(domainCategory(req.getCategory()));
        l.setTitle(req.getTitle());
        l.setDescription(req.getDescription());
        l.setLatitude(req.getLatitude());
        l.setLongitude(req.getLongitude());
        l.setAvailabilityStart(req.getAvailabilityStart());
        l.setAvailabilityEnd(req.getAvailabilityEnd());
        l.setContactMethod(req.getContactMethod());
        l.setContactValue(req.getContactValue());
        l = listingRepository.save(l);
        return toDto(l, null, null);
    }

    public void deleteListing(UUID id, UUID ownerId) {
        ResourceListing l = listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!l.getOwnerId().equals(ownerId)) {
            throw new SecurityException("Not the owner");
        }
        listingRepository.delete(l);
    }

    public List<ResourceListingDto> myListings(UUID ownerId) {
        return listingRepository.findByOwnerId(ownerId).stream()
                .map(l -> toDto(l, null, null))
                .toList();
    }

    public ResourceRequestDto requestResource(UUID requesterId, ResourceRequestRequest req) {
        ResourceListing listing = listingRepository.findById(req.getListingId())
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!listing.isActive()) {
            throw new IllegalStateException("Listing is not active");
        }
        if (listing.getOwnerId().equals(requesterId)) {
            throw new IllegalStateException("Cannot request your own listing");
        }
        if (requestRepository.existsByListingIdAndRequesterId(req.getListingId(), requesterId)) {
            throw new IllegalStateException("Already requested");
        }
        ResourceRequest r = new ResourceRequest();
        r.setListingId(req.getListingId());
        r.setRequesterId(requesterId);
        r.setStatus(ResourceRequest.Status.PENDING);
        r.setMessage(req.getMessage());
        r = requestRepository.save(r);
        return toDto(r);
    }

    public ResourceRequestDto respondToRequest(UUID requestId, UUID ownerId, ResourceRequest.Status status) {
        ResourceRequest r = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        ResourceListing listing = listingRepository.findById(r.getListingId())
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!listing.getOwnerId().equals(ownerId)) {
            throw new SecurityException("Not the owner");
        }
        if (r.getStatus() != ResourceRequest.Status.PENDING) {
            throw new IllegalStateException("Already responded");
        }
        r.setStatus(status);
        r.setRespondedAt(Instant.now());
        r = requestRepository.save(r);
        User requester = userRepository.findById(r.getRequesterId()).orElse(null);
        if (requester != null) {
            List<String> channels = subscriptionService.getEnabledChannelNames(requester.getId());
            if (channels.isEmpty()) {
                channels = List.of("console");
            }
            notificationService.notify(requester, channels,
                    "Resource request " + status.name(),
                    "Your request for \"" + listing.getTitle() + "\" was " + status.name().toLowerCase());
        }
        return toDto(r);
    }

    public List<ResourceRequestDto> myRequests(UUID requesterId) {
        return requestRepository.findByRequesterId(requesterId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<ResourceRequestDto> requestsForListing(UUID listingId, UUID ownerId) {
        ResourceListing l = listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found"));
        if (!l.getOwnerId().equals(ownerId)) {
            throw new SecurityException("Not the owner");
        }
        return requestRepository.findByListingId(listingId).stream()
                .map(this::toDto)
                .toList();
    }

    private ResourceListingDto toDto(ResourceListing l, Double userLat, Double userLon) {
        ResourceListingDto d = new ResourceListingDto();
        d.setId(l.getId());
        d.setOwnerId(l.getOwnerId());
        d.setWardId(l.getWardId());
        d.setCategory(dtoCategory(l.getCategory()));
        d.setTitle(l.getTitle());
        d.setDescription(l.getDescription());
        d.setLatitude(l.getLatitude());
        d.setLongitude(l.getLongitude());
        d.setAvailabilityStart(l.getAvailabilityStart());
        d.setAvailabilityEnd(l.getAvailabilityEnd());
        d.setContactMethod(l.getContactMethod());
        d.setContactValue(l.getContactValue());
        d.setActive(l.isActive());
        d.setCreatedAt(l.getCreatedAt());
        if (userLat != null && userLon != null && l.getLatitude() != null && l.getLongitude() != null) {
            d.setDistanceKm(haversine(userLat, userLon, l.getLatitude(), l.getLongitude()));
        }
        return d;
    }

    private ResourceRequestDto toDto(ResourceRequest r) {
        ResourceRequestDto d = new ResourceRequestDto();
        d.setId(r.getId());
        d.setListingId(r.getListingId());
        d.setRequesterId(r.getRequesterId());
        d.setStatus(dtoStatus(r.getStatus()));
        d.setMessage(r.getMessage());
        d.setRespondedAt(r.getRespondedAt());
        d.setCreatedAt(r.getCreatedAt());
        return d;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private com.ecotech.backend.domain.ResourceListing.Category domainCategory(ResourceListingRequest.Category c) {
        return com.ecotech.backend.domain.ResourceListing.Category.valueOf(c.name());
    }

    private ResourceListingDto.Category dtoCategory(com.ecotech.backend.domain.ResourceListing.Category c) {
        return ResourceListingDto.Category.valueOf(c.name());
    }

    private ResourceRequestDto.Status dtoStatus(ResourceRequest.Status s) {
        return ResourceRequestDto.Status.valueOf(s.name());
    }
}
