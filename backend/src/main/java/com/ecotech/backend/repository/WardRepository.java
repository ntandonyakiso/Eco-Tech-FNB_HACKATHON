package com.ecotech.backend.repository;

import com.ecotech.backend.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WardRepository extends JpaRepository<Ward, UUID>, JpaSpecificationExecutor<Ward> {

    List<Ward> findAll();

    Optional<Ward> findById(UUID id);
}
