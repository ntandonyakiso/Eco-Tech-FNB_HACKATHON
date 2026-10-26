package com.ecotech.backend.repository;

import com.ecotech.backend.domain.ResourceListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ResourceListingRepository extends JpaRepository<ResourceListing, UUID>, JpaSpecificationExecutor<ResourceListing> {

    Page<ResourceListing> findByIsActiveTrueAndCategory(Pageable pageable, boolean active, ResourceListing.Category category);

    Page<ResourceListing> findByIsActiveTrue(Pageable pageable, boolean active);

    List<ResourceListing> findByOwnerId(UUID ownerId);

    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);
}
