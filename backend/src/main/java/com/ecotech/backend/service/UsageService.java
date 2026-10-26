package com.ecotech.backend.service;

import com.ecotech.backend.domain.UsageLog;
import com.ecotech.backend.dto.DailyUsage;
import com.ecotech.backend.dto.UsageLogDto;
import com.ecotech.backend.dto.UsageLogRequest;
import com.ecotech.backend.dto.UsageSummary;
import com.ecotech.backend.repository.UsageLogRepository;
import com.ecotech.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsageService {

    private static final double DEFAULT_WATER_TARIFF = 35.0;
    private static final double DEFAULT_ENERGY_TARIFF = 2.50;

    @Value("${app.usage.water-tariff:#{null}}")
    private Double waterTariff;

    @Value("${app.usage.energy-tariff:#{null}}")
    private Double energyTariff;

    private final UsageLogRepository repository;
    private final UserRepository userRepository;

    public UsageService(UsageLogRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public UsageLogDto logUsage(UUID userId, UsageLogRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        UsageLog log = new UsageLog();
        log.setUserId(userId);
        log.setType(UsageLog.Type.valueOf(req.getType().name()));
        log.setAmount(req.getAmount());
        log.setUnit(req.getUnit());
        log.setRecordedAt(req.getRecordedAt() != null ? req.getRecordedAt() : Instant.now());
        log = repository.save(log);
        return toDto(log);
    }

    public List<UsageLogDto> listUsage(UUID userId, int page, int size) {
        return repository.findByUserId(userId, PageRequest.of(page, size)).stream()
                .map(this::toDto).toList();
    }

    public UsageSummary weeklySummary(UUID userId) {
        Instant now = Instant.now();
        Instant start = now.minus(7, ChronoUnit.DAYS);
        List<UsageLog> logs = repository.findByUserIdAndTypeAndRecordedAtBetween(
                userId, null, start, now);

        double totalWater = logs.stream()
                .filter(l -> l.getType() == UsageLog.Type.WATER)
                .mapToDouble(UsageLog::getAmount).sum();
        double totalEnergy = logs.stream()
                .filter(l -> l.getType() == UsageLog.Type.ENERGY)
                .mapToDouble(UsageLog::getAmount).sum();

        double waterRate = waterTariff != null ? waterTariff : DEFAULT_WATER_TARIFF;
        double energyRate = energyTariff != null ? energyTariff : DEFAULT_ENERGY_TARIFF;

        // estimated rand savings vs baseline (no tariff data = 0 savings)
        double savings = totalWater * waterRate + totalEnergy * energyRate;

        Map<Instant, List<UsageLog>> byDay = logs.stream()
                .collect(Collectors.groupingBy(l -> l.getRecordedAt().truncatedTo(ChronoUnit.DAYS)));

        List<DailyUsage> daily = byDay.entrySet().stream()
                .map(e -> {
                    DailyUsage d = new DailyUsage();
                    d.setDate(e.getKey());
                    d.setWater(e.getValue().stream()
                            .filter(l -> l.getType() == UsageLog.Type.WATER)
                            .mapToDouble(UsageLog::getAmount).sum());
                    d.setEnergy(e.getValue().stream()
                            .filter(l -> l.getType() == UsageLog.Type.ENERGY)
                            .mapToDouble(UsageLog::getAmount).sum());
                    return d;
                }).sorted((a, b) -> a.getDate().compareTo(b.getDate())).toList();

        UsageSummary s = new UsageSummary();
        s.setPeriodStart(start);
        s.setPeriodEnd(now);
        s.setTotalWater(totalWater);
        s.setTotalEnergy(totalEnergy);
        s.setEstimatedRandSavings(savings);
        s.setDaily(daily);
        return s;
    }

    private UsageLogDto toDto(UsageLog l) {
        UsageLogDto d = new UsageLogDto();
        d.setId(l.getId());
        d.setUserId(l.getUserId());
        d.setType(UsageLogDto.Type.valueOf(l.getType().name()));
        d.setAmount(l.getAmount());
        d.setUnit(l.getUnit());
        d.setRecordedAt(l.getRecordedAt());
        return d;
    }
}
