package com.ecotech.backend.repository;

import com.ecotech.backend.domain.VulnerableContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface VulnerableContactRepository extends JpaRepository<VulnerableContact, UUID>, JpaSpecificationExecutor<VulnerableContact> {

    List<VulnerableContact> findByUserId(UUID userId);

    List<VulnerableContact> findByWardId(UUID wardId);

    boolean existsByUserIdAndPhone(UUID userId, String phone);
}
