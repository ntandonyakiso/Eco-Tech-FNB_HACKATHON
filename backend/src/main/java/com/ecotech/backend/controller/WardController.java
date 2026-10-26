package com.ecotech.backend.controller;

import com.ecotech.backend.domain.Ward;
import com.ecotech.backend.dto.ErrorResponse;
import com.ecotech.backend.dto.WardDto;
import com.ecotech.backend.repository.WardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wards")
public class WardController {

    private final WardRepository wardRepository;

    public WardController(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    @GetMapping
    public ResponseEntity<List<WardDto>> listWards() {
        List<WardDto> wards = wardRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWard(@PathVariable String id) {
        try {
            java.util.UUID uuid = java.util.UUID.fromString(id);
            Ward ward = wardRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("Ward not found"));
            return ResponseEntity.ok(toDto(ward));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", "Invalid ward id", "/api/v1/wards/" + id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", e.getMessage(), "/api/v1/wards/" + id));
        }
    }

    private WardDto toDto(Ward ward) {
        return new WardDto(
                ward.getId().toString(),
                ward.getName(),
                ward.getProvince(),
                ward.getMunicipality(),
                ward.getCentroidLat(), ward.getCentroidLon(), ward.getWardNumber());
    }
}
