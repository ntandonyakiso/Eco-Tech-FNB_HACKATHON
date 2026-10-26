package com.ecotech.backend.service;

import com.ecotech.backend.domain.FoodPoint;
import com.ecotech.backend.dto.FoodPointDto;
import com.ecotech.backend.dto.FoodPointRequest;
import com.ecotech.backend.repository.FoodPointRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FoodPointService {

    private final FoodPointRepository repository;
    private final UserRepository userRepository;

    public FoodPointService(FoodPointRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public FoodPointDto addFoodPoint(UUID userId, FoodPointRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        FoodPoint fp = new FoodPoint();
        fp.setAddedBy(userId);
        fp.setName(req.getName());
        fp.setType(FoodPoint.Type.valueOf(req.getType().name()));
        fp.setDescription(req.getDescription());
        fp.setLatitude(req.getLatitude());
        fp.setLongitude(req.getLongitude());
        fp.setWardId(req.getWardId());
        fp.setApproved(false);
        fp = repository.save(fp);
        return toDto(fp);
    }

    public List<FoodPointDto> listApproved() {
        return repository.findByIsApprovedTrue(true).stream().map(this::toDto).toList();
    }

    public FoodPointDto approve(UUID id) {
        FoodPoint fp = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food point not found"));
        fp.setApproved(true);
        fp = repository.save(fp);
        return toDto(fp);
    }

    public List<FoodPointDto> myFoodPoints(UUID userId) {
        return repository.findByAddedBy(userId).stream().map(this::toDto).toList();
    }

    private FoodPointDto toDto(FoodPoint fp) {
        FoodPointDto d = new FoodPointDto();
        d.setId(fp.getId());
        d.setAddedBy(fp.getAddedBy());
        d.setName(fp.getName());
        d.setType(FoodPointDto.Type.valueOf(fp.getType().name()));
        d.setDescription(fp.getDescription());
        d.setLatitude(fp.getLatitude());
        d.setLongitude(fp.getLongitude());
        d.setWardId(fp.getWardId());
        d.setApproved(fp.isApproved());
        d.setCreatedAt(fp.getCreatedAt());
        return d;
    }
}
