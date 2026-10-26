package com.ecotech.backend.service;

import com.ecotech.backend.domain.StockLoss;
import com.ecotech.backend.dto.StockLossDto;
import com.ecotech.backend.dto.StockLossRequest;
import com.ecotech.backend.repository.StockLossRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StockLossService {

    private final StockLossRepository repository;
    private final UserRepository userRepository;

    public StockLossService(StockLossRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public StockLossDto logStockLoss(UUID userId, StockLossRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        StockLoss s = new StockLoss();
        s.setUserId(userId);
        s.setItem(req.getItem());
        s.setQuantity(req.getQuantity());
        s.setUnitCost(req.getUnitCost());
        s.setLossReason(req.getLossReason());
        s.setRecordedAt(req.getRecordedAt() != null ? req.getRecordedAt() : Instant.now());
        s = repository.save(s);
        return toDto(s);
    }

    public List<StockLossDto> listStockLosses(UUID userId, int page, int size) {
        return repository.findByUserId(userId, PageRequest.of(page, size)).stream()
                .map(this::toDto).toList();
    }

    private StockLossDto toDto(StockLoss s) {
        StockLossDto d = new StockLossDto();
        d.setId(s.getId());
        d.setUserId(s.getUserId());
        d.setItem(s.getItem());
        d.setQuantity(s.getQuantity());
        d.setUnitCost(s.getUnitCost());
        d.setLossReason(s.getLossReason());
        d.setTotalValue(s.getQuantity() * s.getUnitCost());
        d.setRecordedAt(s.getRecordedAt());
        return d;
    }
}
