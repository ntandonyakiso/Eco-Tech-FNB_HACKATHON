package com.ecotech.backend.repository;

import com.ecotech.backend.domain.ResourceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, UUID>, JpaSpecificationExecutor<ResourceRequest> {

    List<ResourceRequest> findByListingId(UUID listingId);

    List<ResourceRequest> findByRequesterId(UUID requesterId);

    boolean existsByListingIdAndRequesterId(UUID listingId, UUID requesterId);
}
