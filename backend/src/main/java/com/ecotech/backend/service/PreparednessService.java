package com.ecotech.backend.service;

import com.ecotech.backend.domain.PreparednessPlan;
import com.ecotech.backend.dto.PreparednessPlanDto;
import com.ecotech.backend.dto.PreparednessPlanRequest;
import com.ecotech.backend.repository.PreparednessPlanRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class PreparednessService {

    private static final String BASE_CHECKLIST = """
            - Flashlight + spare batteries
            - Drinking water (4L per person per day)
            - Non-perishable food
            - First aid kit
            - Power bank for phone
            """;

    private static final String ELDERLY_ADD = """
            - Extra medication supply (2 weeks)
            - Mobility aid check
            - Emergency contact list nearby
            """;

    private static final String CHILDREN_ADD = """
            - Baby formula / nappies
            - Children's medication
            - Activities for kids
            """;

    private static final String MEDICAL_ADD = """
            - Backup power for medical devices
            - Medication list and prescriptions
            - Nearest clinic contact
            """;

    private static final String BUSINESS_ADD = """
            - Cash float for card outages
            - Backup supplier contacts
            - Staff communication plan
            """;

    private final PreparednessPlanRepository repository;
    private final UserRepository userRepository;

    public PreparednessService(PreparednessPlanRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public PreparednessPlanDto savePlan(UUID userId, PreparednessPlanRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        PreparednessPlan plan = repository.findByUserId(userId).orElse(new PreparednessPlan());
        plan.setUserId(userId);
        plan.setHouseholdType(req.getHouseholdType());
        plan.setWardId(req.getWardId());
        plan.setPlanData(buildPlan(req.getHouseholdType(), req.getPlanData()));
        plan = repository.save(plan);
        return toDto(plan);
    }

    public PreparednessPlanDto getPlan(UUID userId) {
        PreparednessPlan p = repository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Preparedness plan not found"));
        return toDto(p);
    }

    private String buildPlan(String householdType, String custom) {
        StringBuilder sb = new StringBuilder(BASE_CHECKLIST);
        if ("ELDERLY".equalsIgnoreCase(householdType)) sb.append("\n").append(ELDERLY_ADD);
        if ("CHILDREN".equalsIgnoreCase(householdType)) sb.append("\n").append(CHILDREN_ADD);
        if ("MEDICAL".equalsIgnoreCase(householdType)) sb.append("\n").append(MEDICAL_ADD);
        if ("BUSINESS".equalsIgnoreCase(householdType)) sb.append("\n").append(BUSINESS_ADD);
        if (custom != null && !custom.isBlank()) {
            sb.append("\nCustom:\n").append(custom);
        }
        return sb.toString();
    }

    private PreparednessPlanDto toDto(PreparednessPlan p) {
        PreparednessPlanDto d = new PreparednessPlanDto();
        d.setId(p.getId());
        d.setUserId(p.getUserId());
        d.setHouseholdType(p.getHouseholdType());
        d.setWardId(p.getWardId());
        d.setPlanData(p.getPlanData());
        d.setCreatedAt(p.getCreatedAt());
        d.setUpdatedAt(p.getUpdatedAt());
        return d;
    }
}
