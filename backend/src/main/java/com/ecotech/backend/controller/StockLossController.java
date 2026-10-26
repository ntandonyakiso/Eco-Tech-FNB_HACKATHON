package com.ecotech.backend.controller;

import com.ecotech.backend.dto.StockLossDto;
import com.ecotech.backend.dto.StockLossRequest;
import com.ecotech.backend.security.CurrentUser;
import com.ecotech.backend.service.StockLossService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business/stock-loss")
public class StockLossController {

    private final StockLossService service;

    public StockLossController(StockLossService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public StockLossDto log(@CurrentUser UUID userId, @Valid @RequestBody StockLossRequest req) {
        return service.logStockLoss(userId, req);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<StockLossDto> list(@CurrentUser UUID userId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return service.listStockLosses(userId, page, size);
    }
}
