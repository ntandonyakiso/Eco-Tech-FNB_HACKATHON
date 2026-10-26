package com.ecotech.backend.service;

import com.ecotech.backend.domain.BusinessProfile;
import com.ecotech.backend.dto.BusinessProfileDto;
import com.ecotech.backend.dto.BusinessProfileRequest;
import com.ecotech.backend.repository.BusinessProfileRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class BusinessService {

    private final BusinessProfileRepository repository;
    private final UserRepository userRepository;

    public BusinessService(BusinessProfileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public BusinessProfileDto upsertProfile(UUID userId, BusinessProfileRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        BusinessProfile profile = repository.findByUserId(userId).orElse(new BusinessProfile());
        profile.setUserId(userId);
        profile.setBusinessName(req.getBusinessName());
        profile.setBusinessType(req.getBusinessType());
        profile.setAverageHourlyRevenue(req.getAverageHourlyRevenue() != null ? req.getAverageHourlyRevenue() : 0.0);
        profile.setMonthlyRent(req.getMonthlyRent() != null ? req.getMonthlyRent() : 0.0);
        profile.setStaffCount(req.getStaffCount() != null ? req.getStaffCount() : 0);
        profile = repository.save(profile);
        return toDto(profile);
    }

    public BusinessProfileDto getProfile(UUID userId) {
        BusinessProfile p = repository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Business profile not found"));
        return toDto(p);
    }

    private BusinessProfileDto toDto(BusinessProfile p) {
        BusinessProfileDto d = new BusinessProfileDto();
        d.setId(p.getId());
        d.setUserId(p.getUserId());
        d.setBusinessName(p.getBusinessName());
        d.setBusinessType(p.getBusinessType());
        d.setAverageHourlyRevenue(p.getAverageHourlyRevenue());
        d.setMonthlyRent(p.getMonthlyRent());
        d.setStaffCount(p.getStaffCount());
        d.setCreatedAt(p.getCreatedAt());
        d.setUpdatedAt(p.getUpdatedAt());
        return d;
    }
}
