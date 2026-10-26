package com.ecotech.backend.repository;

import com.ecotech.backend.domain.FoodPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface FoodPointRepository extends JpaRepository<FoodPoint, UUID>, JpaSpecificationExecutor<FoodPoint> {

    List<FoodPoint> findByIsApprovedTrue(boolean approved);

    List<FoodPoint> findByAddedBy(UUID addedBy);
}
