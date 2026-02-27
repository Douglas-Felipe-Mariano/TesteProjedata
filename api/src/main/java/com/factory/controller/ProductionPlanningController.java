package com.factory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.factory.dto.productsugestion.ProductionSuggestionResponseDTO;
import com.factory.services.ProductionPlanningService;

@RestController
@RequestMapping("/api/production-planning")
public class ProductionPlanningController {

    private final ProductionPlanningService productionPlanningService;

    public ProductionPlanningController(ProductionPlanningService productionPlanningService) {
        this.productionPlanningService = productionPlanningService;
    }

    @GetMapping("/suggestion")
    public ResponseEntity<ProductionSuggestionResponseDTO> getSuggestion() {
        return ResponseEntity.ok(productionPlanningService.suggestProduction());
    }
}