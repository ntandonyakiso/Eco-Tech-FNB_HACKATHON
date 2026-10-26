package com.ecotech.backend.controller;

import com.ecotech.backend.dto.BackupComparisonRequest;
import com.ecotech.backend.dto.BackupComparisonResponse;
import com.ecotech.backend.security.CurrentUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business")
public class BackupController {

    @PostMapping("/backup-comparison")
    @PreAuthorize("isAuthenticated()")
    public BackupComparisonResponse compare(@CurrentUser UUID userId,
                                            @RequestBody BackupComparisonRequest req) {
        double hours = req.getHoursOfOutage() != null ? req.getHoursOfOutage() : 0;
        double solar = req.getSolarCost() != null ? req.getSolarCost() : 0;
        double generator = req.getGeneratorCost() != null ? req.getGeneratorCost() : 0;
        double gas = req.getGasCost() != null ? req.getGasCost() : 0;
        double fuelPerHour = req.getFuelCostPerHour() != null ? req.getFuelCostPerHour() : 0;
        double gasPerHour = req.getGasCostPerHour() != null ? req.getGasCostPerHour() : 0;

        double genTotal = generator + (fuelPerHour * hours);
        double gasTotal = gas + (gasPerHour * hours);

        BackupComparisonResponse r = new BackupComparisonResponse();
        r.setSolarTotal(solar);
        r.setGeneratorTotal(genTotal);
        r.setGasTotal(gasTotal);

        if (solar <= genTotal && solar <= gasTotal) {
            r.setRecommendation("Solar is cheapest for this outage length");
        } else if (genTotal <= gasTotal) {
            r.setRecommendation("Generator is cheapest for this outage length");
        } else {
            r.setRecommendation("Gas is cheapest for this outage length");
        }
        return r;
    }
}
